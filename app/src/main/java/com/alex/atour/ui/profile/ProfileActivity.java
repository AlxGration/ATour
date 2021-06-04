package com.alex.atour.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.atour.BuildConfig;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.ConfirmationDialog;
import com.alex.atour.models.NetworkStateChangeReceiver;
import com.alex.atour.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements NetworkStateChangeReceiver.NetworkStateChangeListener {

    private ProfileViewModel viewModel;
    private EstimationViewModel estimVM;
    private String champID, userID;
    private MemberEstimation mEstim;
    private NetworkStateChangeReceiver receiver;
    private TextView tvNetworkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        estimVM = new ViewModelProvider(this).get(EstimationViewModel.class);

        Button btnSignOut = findViewById(R.id.btn_sign_out);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvSecName = findViewById(R.id.tv_sec_name);
        TextView tvCity = findViewById(R.id.tv_city);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvPhone = findViewById(R.id.tv_phone);
        ProgressBar pBar = findViewById(R.id.progress_bar);
        TextView tvDLink = findViewById(R.id.tv_d_link);
        TextView tvDComment = findViewById(R.id.tv_d_comment);
        TextView tvDownloadTSM = findViewById(R.id.tv_d_tsm);
        tvNetworkState = findViewById(R.id.tv_network_bar);


        tvDownloadTSM.setOnClickListener(onClickDownloadTSM);
        viewModel.getEstimations().observe(this, estims->{
            if (estims!= null) {
                RecyclerView recyclerView = findViewById(R.id.rv_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                EstimsRecyclerAdapter adapter = new EstimsRecyclerAdapter(estims);
                recyclerView.setAdapter(adapter);
            }
        });
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                    View.VISIBLE:
                    View.INVISIBLE
            );
        });
        estimVM.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                    View.VISIBLE:
                    View.INVISIBLE
            );
        });
        estimVM.getIsSuccess().observe(this, isSuccess->{
            if (isSuccess){ finish(); }
        });
        estimVM.getErrorMessage().observe(this, this::showError);
        viewModel.getErrorMessage().observe(this, this::showError);
        viewModel.getSecName().observe(this, tvSecName::setText);
        viewModel.getName().observe(this, tvName::setText);
        viewModel.getCity().observe(this, tvCity::setText);
        viewModel.getEmail().observe(this, tvEmail::setText);
        viewModel.getPhone().observe(this, tvPhone::setText);
        viewModel.getDocument().observe(this, document -> {
            showLayout(R.id.layout_docs, View.VISIBLE);
            tvDLink.setText(document.getLink());
            tvDComment.setText(document.getComment());
        });
        // показ заявки на участие
        viewModel.getMembershipRequest().observe(this, this::showRequest);

        //проверка подключения internet
        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



        int comeFrom = getIntent().getIntExtra("comeFrom", -1);

        Member mem;
        switch (comeFrom){
            case 1://my profile
                userID = getIntent().getStringExtra("userID");
                viewModel.loadProfile(userID);
                btnSignOut.setVisibility(View.VISIBLE);
                break;
            case 2://show membership request (for admin)
                MembershipRequest req = (MembershipRequest) getIntent().getSerializableExtra("request");
                userID = req.getUserID();
                viewModel.loadProfile(userID);  // показ регистрационных данных пользователя
                showRequest(req);               // показ заявки на участие
                break;
            case 3://show admin info
                User u = (User) getIntent().getSerializableExtra("userInfo");
                viewModel.setUserInfo(u);
                break;
            case 4://show docs (for referee)
                mEstim = DBManager.getInstance().getRealmDB().getMemberEstimationByID(getIntent().getStringExtra("memberID"));//участник
                userID = mEstim.getMemberID();
                viewModel.setUserName(mEstim.getMemberFIO());
                champID = mEstim.getChampID();
                viewModel.loadDocs(champID, mEstim.getMemberID());
                showEstimationLayout(mEstim);
                break;
            case 5://show request and docs (for admin)
                mem = (Member) getIntent().getSerializableExtra("member");
                userID = mem.getUserID();
                champID = getIntent().getStringExtra("champID");
                viewModel.loadProfile(userID);          // показ регистрационных данных пользователя
                viewModel.loadMembershipRequest(champID, userID);
                if (mem.getRole() == 1)
                    viewModel.loadDocs(champID, userID);        // показ документов
                else showRefereeEstimations(champID, userID);   // показ всех оценок рефери
                break;
        }
    }

    public void onClickBackBtn(View view) {
        onBackPressed();
    }

    // выход из аккаунта
    public void onClickSignOut(View view) {
        viewModel.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // show membership request
    private void showRequest(MembershipRequest req){
        ((TextView)findViewById(R.id.tv_link)).setText(req.getCloudLink());
        ((TextView)findViewById(R.id.tv_comment)).setText(req.getComment());

        //role
        TextView tvTitle = findViewById(R.id.tv_title);
        if (req.getRole() == 1){
            tvTitle.setText(R.string.member);
        }else{
            tvTitle.setText(R.string.referee);
        }

        //types
        if (req.isTypeWalk()) findViewById(R.id.cp_walk).setVisibility(View.VISIBLE);
        if (req.isTypeSki()) findViewById(R.id.cp_ski).setVisibility(View.VISIBLE);
        if (req.isTypeHike()) findViewById(R.id.cp_hike).setVisibility(View.VISIBLE);
        if (req.isTypeWater()) findViewById(R.id.cp_water).setVisibility(View.VISIBLE);

        if (req.isTypeSpeleo()) findViewById(R.id.cp_speleo).setVisibility(View.VISIBLE);
        if (req.isTypeBike()) findViewById(R.id.cp_bike).setVisibility(View.VISIBLE);
        if (req.isTypeAuto()) findViewById(R.id.cp_auto).setVisibility(View.VISIBLE);
        if (req.isTypeOther()) findViewById(R.id.cp_other).setVisibility(View.VISIBLE);

        showLayout(R.id.layout_request, View.VISIBLE);
    }

    // for referee (to estimate member)
    private void showEstimationLayout(MemberEstimation req){
        //types
        if (req.isTypeWalk()) findViewById(R.id.cp_walk).setVisibility(View.VISIBLE);
        if (req.isTypeSki()) findViewById(R.id.cp_ski).setVisibility(View.VISIBLE);
        if (req.isTypeHike()) findViewById(R.id.cp_hike).setVisibility(View.VISIBLE);
        if (req.isTypeWater()) findViewById(R.id.cp_water).setVisibility(View.VISIBLE);

        if (req.isTypeSpeleo()) findViewById(R.id.cp_speleo).setVisibility(View.VISIBLE);
        if (req.isTypeBike()) findViewById(R.id.cp_bike).setVisibility(View.VISIBLE);
        if (req.isTypeAuto()) findViewById(R.id.cp_auto).setVisibility(View.VISIBLE);
        if (req.isTypeOther()) findViewById(R.id.cp_other).setVisibility(View.VISIBLE);

        showLayout(R.id.layout_docs, View.VISIBLE);
        showLayout(R.id.layout_estim, View.VISIBLE);

        ((EditText)findViewById(R.id.et_complexity)).setText(String.format(Locale.ENGLISH, "%.2f", req.getComplexity()));
        ((EditText)findViewById(R.id.et_novelty)).setText( String.format(Locale.ENGLISH, "%.2f", req.getNovelty()));
        ((EditText)findViewById(R.id.et_strategy)).setText( String.format(Locale.ENGLISH, "%.2f", req.getStrategy()));
        ((EditText)findViewById(R.id.et_tactics)).setText(String.format(Locale.ENGLISH, "%.2f", req.getTactics()));
        ((EditText)findViewById(R.id.et_technique)).setText(String.format(Locale.ENGLISH, "%.2f", req.getTechnique()));
        ((EditText)findViewById(R.id.et_tension)).setText(String.format(Locale.ENGLISH, "%.2f", req.getTension()));
        ((EditText)findViewById(R.id.et_informativeness)).setText( String.format(Locale.ENGLISH, "%.2f", req.getInformativeness()));
        ((EditText)findViewById(R.id.et_comment)).setText(req.getComment());

        showLayout(R.id.img_title_city, View.GONE);
        showLayout(R.id.tv_title_email, View.GONE);
        showLayout(R.id.tv_title_phone, View.GONE);
    }

    private void showLayout(int id, int visibility){
        findViewById(id).setVisibility(visibility);
    }

    //показ оценок от всех рефери userID участника
    private void showEstimations(String champID, String userID){
        //todo:: create me

    }

    //save estimation locally(referee)
    public void onClickSendBtn(View v){
        estimVM.saveEstimation(
                mEstim,
                ((EditText)findViewById(R.id.et_complexity)).getText().toString(),
                ((EditText)findViewById(R.id.et_novelty)).getText().toString(),
                ((EditText)findViewById(R.id.et_strategy)).getText().toString(),
                ((EditText)findViewById(R.id.et_tactics)).getText().toString(),
                ((EditText)findViewById(R.id.et_technique)).getText().toString(),
                ((EditText)findViewById(R.id.et_tension)).getText().toString(),
                ((EditText)findViewById(R.id.et_informativeness)).getText().toString(),
                ((EditText)findViewById(R.id.et_comment)).getText().toString()
        );
    }
    private void showError(String err){
        if (err.length() == 0) return;
        Snackbar.make(findViewById(R.id.main_layout), err, Snackbar.LENGTH_LONG).show();
    }
    private void showRefereeEstimations(String champID, String userID){
        showLayout(R.id.layout_ref_estims, View.VISIBLE);
        viewModel.loadEstimations(champID, userID);
    }

   private View.OnClickListener onClickDownloadTSM = v -> {
       viewModel.isTSMDownloaded().observe(this, isDownloaded -> {
           if (isDownloaded) {
               ConfirmationDialog confirmationDialog = new ConfirmationDialog("Справка ТСМ сохранена в папке Documents.\nОткрыть?", () -> {
                   File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/ATour/" + userID + "/"),
                           "TSM.xlsx");
                   if (file.exists()) {
                       Intent xlsxOpenintent = new Intent();
                       Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
                       xlsxOpenintent.setAction(Intent.ACTION_VIEW);
                       xlsxOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       xlsxOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       xlsxOpenintent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                       xlsxOpenintent.setDataAndType(uri, "*/*");
                       xlsxOpenintent.setDataAndType(uri, "application/vnd.ms-excel");
                       try {
                           //Toast.makeText(this, "i got file", Toast.LENGTH_LONG).show();
                           startActivity(xlsxOpenintent);
                       } catch (ActivityNotFoundException e) {
                           Toast.makeText(this, "К сожалению, открыть файл не получилось.", Toast.LENGTH_LONG).show();
                           e.printStackTrace();
                       }
                   } else {
                       Toast.makeText(this, "Файл не найден", Toast.LENGTH_LONG).show();
                   }
               });
               confirmationDialog.show(getSupportFragmentManager(), "myDialog");
           }
       });

       ConfirmationDialog confirmationDialog = new ConfirmationDialog("Вы уверены?\nФайл будет сохранен в папке Documents", () -> {
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
               Log.e("TAG", "start loading tsm for " + userID);
               viewModel.downloadTSMFile(champID, userID);
           }else {
               //запрашиваем разрешение
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
           }
       });
       confirmationDialog.show(getSupportFragmentManager(), "myDialog");
   };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 ){// save tsm
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.e("TAG", "start loading tsm for " + userID);
                viewModel.downloadTSMFile(champID, userID);
            }  else {
                viewModel.requestError("Для сохранения файла\nНеобходимо разрешение");
            }
        }

    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            tvNetworkState.setVisibility(View.GONE);
        }else {
            tvNetworkState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver.detach();
        }
    }
}