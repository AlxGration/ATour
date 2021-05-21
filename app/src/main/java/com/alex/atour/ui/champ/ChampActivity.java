package com.alex.atour.ui.champ;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.atour.BuildConfig;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.ConfirmationDialog;
import com.alex.atour.models.ExcelModule;
import com.alex.atour.ui.champ.referee.EstimsRecyclerAdapter;
import com.alex.atour.ui.champ.admin.MembersListRecyclerAdapter;
import com.alex.atour.ui.champ.admin.MembersFragment;
import com.alex.atour.ui.champ.member.DocsFragment;
import com.alex.atour.ui.champ.referee.MembersForRefereeFragment;
import com.alex.atour.ui.create.memrequest.MembershipRequestActivity;
import com.alex.atour.ui.profile.ProfileActivity;
import com.alex.atour.ui.requests.RequestsListActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class ChampActivity extends AppCompatActivity implements MembersListRecyclerAdapter.IonItemClickListener, EstimsRecyclerAdapter.IonItemClickListener {

    private TextView tvMessage, tvAdminFio;
    private Button btnSendRequest;
    private ProgressBar pBar;
    private ChampInfo info;
    private User admin;
    private int role;
    private Toolbar toolbar;
    private ChampViewModel viewModel;
    private MembersForRefereeFragment membersForRefereeFragment;
    private DocsFragment docsFragment;
    private ResultFragment resultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);

        viewModel = new ViewModelProvider(this).get(ChampViewModel.class);

        //init ui
        ConstraintLayout profileAdminLayout = findViewById(R.id.layout_main_referee);
        profileAdminLayout.setOnClickListener(onClickShowAdminProfile);
        tvMessage = findViewById(R.id.tv_message);
        btnSendRequest = findViewById(R.id.btn_send_request);
        tvAdminFio = findViewById(R.id.tv_fio);
        TextView tvError = findViewById(R.id.tv_error);
        pBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.toolbar);

        //setting ChampInfo on UI
        setChampInfoOnUI((ChampInfo) getIntent().getSerializableExtra("champInfo"));

        //setting adminInfo
        viewModel.getAdminLiveData().observe(this, user -> {
            tvAdminFio.setText(user.getFio());
            admin = user;
        });
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(
                    isLoading? View.VISIBLE: View.INVISIBLE
            );
        });
        viewModel.getIsTotalProtocolCreatedLiveData().observe(this, isCreated->{
            pBar.setVisibility(
                    isCreated? View.INVISIBLE: View.VISIBLE
            );
            if (isCreated){
                ConfirmationDialog confirmationDialog = new ConfirmationDialog("Протокол сохранен в папке Documents.\nОткрыть?", () -> {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/ATour/"),
                            "protocol_total.xlsx");
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
                    }else{
                        Toast.makeText(this, "Файл не найден", Toast.LENGTH_LONG).show();
                    }
                });
                confirmationDialog.show(getSupportFragmentManager(), "myDialog");
            }
        });
        viewModel.getErrorMessage().observe(this, tvError::setText);
        viewModel.getRoleLiveData().observe(this, role->{
            this.role = role;
            showLayoutDependsOnRoleAndState(role, -1);
        });
        viewModel.getStateLiveData().observe(this, state->{
            showLayoutDependsOnRoleAndState(role, state); });
        viewModel.getIsEnrollmentOpenLiveData().observe(this, isOpen->{
            if (isOpen) Toast.makeText(getApplicationContext(), "Регистрация открыта", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "Регистрация завершена", Toast.LENGTH_SHORT).show();
        });

        // request admin info
        // and user status and role (admin, referee, member)
        viewModel.loadPage(info.getAdminID(), info.getChampID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    // Для админа,
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ConfirmationDialog confirmationDialog;
        switch(id){
            case R.id.action_requests :// переход на активность для просмотра заявок на чемпионат
                Intent intent = new Intent(ChampActivity.this, RequestsListActivity.class);
                intent.putExtra("champID", info.getChampID());
                startActivity(intent);
                return true;
            case R.id.action_open_enrollment://открытие приема заявок
                confirmationDialog = new ConfirmationDialog("Вы уверены?", () -> {
                    viewModel.openEnrollment(info.getChampID());
                });
                confirmationDialog.show(getSupportFragmentManager(), "myDialog");
                return true;
            case R.id.action_close_enrollment://закрытие приема заявок
                confirmationDialog = new ConfirmationDialog("Вы уверены?", () -> {
                    viewModel.closeEnrollment(info.getChampID());
                });
                confirmationDialog.show(getSupportFragmentManager(), "myDialog");
                return true;
            case R.id.action_create_referees_protocols:// формирование судейских протоколов
                confirmationDialog = new ConfirmationDialog("Вы уверены?", () -> {
                    viewModel.createRefereesProtocols(info.getChampID());
                });
                confirmationDialog.show(getSupportFragmentManager(), "myDialog");
                return true;
            case R.id.action_create_total_protocol:// формирование итогового протокола
                confirmationDialog = new ConfirmationDialog("Вы уверены?\nКопия протокола будет сохранена в папке Documents", () -> {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Log.e("TAG", "start saving");
                        viewModel.createTotalProtocol(this, info);
                    }else {
                        //запрашиваем разрешение
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                });
                confirmationDialog.show(getSupportFragmentManager(), "myDialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClickBackBtn(View view) {
        finish();
    }
    public void showLoadingProcess(boolean isLoading){viewModel.setIsLoading(isLoading);}

    public void onClickMemRequestBtn(View view) {
        Intent intent = new Intent(this, MembershipRequestActivity.class);
        intent.putExtra("champInfo", info);
        startActivity(intent);
    }


    public View.OnClickListener onClickShowAdminProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (admin == null) {
                viewModel.requestError("Не удалось загрузить информацию об админестраторе");
                return;
            }

            Intent intent = new Intent(ChampActivity.this, ProfileActivity.class);
            intent.putExtra("userInfo", admin);
            intent.putExtra("comeFrom", 3);//show admin info
            startActivity(intent);
        }
    };


    private void setChampInfoOnUI(ChampInfo info){
        if (info == null) {
            viewModel.requestError("Не удалось загрузить информацию");
            return;
        }
        this.info = info;

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDataFrom = findViewById(R.id.tv_data_from);
        TextView tvDataTo = findViewById(R.id.tv_data_to);
        TextView tvCity = findViewById(R.id.tv_city);

        tvTitle.setText(info.getTitle());
        tvCity.setText(info.getCity());

        //rotate date format 2021.02.25 -> 25.02.2021
        String[] splittedDate = info.getDataFrom().split("\\.");
        tvDataFrom.setText(
                splittedDate[2]+"."+splittedDate[1]+"."+splittedDate[0]
        );
        splittedDate = info.getDataTo().split("\\.");
        tvDataTo.setText(
                splittedDate[2]+"."+splittedDate[1]+"."+splittedDate[0]
        );

        //types
        if (info.isTypeWalk()) findViewById(R.id.cp_walk).setVisibility(View.VISIBLE);
        if (info.isTypeSki()) findViewById(R.id.cp_ski).setVisibility(View.VISIBLE);
        if (info.isTypeHike()) findViewById(R.id.cp_hike).setVisibility(View.VISIBLE);
        if (info.isTypeWater()) findViewById(R.id.cp_water).setVisibility(View.VISIBLE);

        if (info.isTypeSpeleo()) findViewById(R.id.cp_speleo).setVisibility(View.VISIBLE);
        if (info.isTypeBike()) findViewById(R.id.cp_bike).setVisibility(View.VISIBLE);
        if (info.isTypeAuto()) findViewById(R.id.cp_auto).setVisibility(View.VISIBLE);
        if (info.isTypeOther()) findViewById(R.id.cp_other).setVisibility(View.VISIBLE);
    }

    @Override
    public void startProfileActivityWith(int role, Member member) {
        //администратор хочет посмотреть информацию участника или судьи
        if (role == 1){
            Intent intent = new Intent(ChampActivity.this, ProfileActivity.class);
            intent.putExtra("member", member);
            intent.putExtra("champID", info.getChampID());
            intent.putExtra("comeFrom", 5);//show request and docs (for admin)
            startActivity(intent);
        }
    }
    @Override
    public void startProfileActivityWith(int role, MemberEstimation member) {
        //судья хочет посмотреть документы для оценки участника
        if (role == 2){
            Intent intent = new Intent(ChampActivity.this, ProfileActivity.class);
            intent.putExtra("memberID", member.getId());
            intent.putExtra("comeFrom", 4);//show docs (for referee)
            startActivity(intent);
        }
    }

    private void showLayoutDependsOnRoleAndState(int role, int state){
        Log.e("TAG", "RS "+ role+" "+state);

        if (role == -1){// visitor
            btnSendRequest.setVisibility(
                    info.isEnrollmentOpen()?  // показать "подать заявку" только если набор продолжается
                    View.VISIBLE:View.INVISIBLE);
            return;
        }

        if (role == 0){//admin
            btnSendRequest.setVisibility(View.INVISIBLE);
            setSupportActionBar(toolbar);
            showMembersFragment();
            tvMessage.setVisibility(View.GONE);
        }
        if (role == 1 && state != -1){//member
            switch (state){
                case 0://MembershipState.WAIT
                    tvMessage.setText(R.string.request_pending);
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    break;
                case 1://MembershipState.DENIED
                    tvMessage.setText(R.string.request_denied);
                    btnSendRequest.setVisibility(View.VISIBLE);
                    break;
                case 2://MembershipState.ACCEPTED
                    tvMessage.setVisibility(View.GONE);
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    showDocsSendingFragment();
                    break;
                case 3://MembershipState.DOCS_SUBMITTED
                    tvMessage.setVisibility(View.VISIBLE);
                    findViewById(R.id.frame_layout).setVisibility(View.INVISIBLE);
                    tvMessage.setText(R.string.results_waiting);
                    break;
                case 4://MembershipState.RESULTS
                    showResultsFragment();
                    break;
            }
        }
        if (role == 2  && state != -1) {//referee
            switch (state){
                case 0://MembershipState.WAIT
                    tvMessage.setText(R.string.request_pending);
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    break;
                case 1://MembershipState.DENIED
                    tvMessage.setText(R.string.request_denied);
                    btnSendRequest.setVisibility(View.VISIBLE);
                    break;
                case 2://MembershipState.ACCEPTED
                    tvMessage.setText("Продолжается прием заявок.\nСудейские протоколы еще не сформированы.");
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    break;
                case 3://MembershipState.DOCS_SUBMITTED
                    tvMessage.setVisibility(View.GONE);
                    showDocsFragment();
                    break;
                case 4://MembershipState.RESULTS
                    showResultsFragment();
                    break;
            }
        }
    }

    // (for admin)
    private void showMembersFragment(){
        //show FrameLayout
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        // and fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,  MembersFragment.newInstance(info.getChampID()))
                .commitNow();
    }

    // (for members)
    private void showDocsSendingFragment(){
        //show FrameLayout
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        // and fragment
        docsFragment = DocsFragment.newInstance(info.getChampID());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,  docsFragment)
                .commitNow();
    }
    // (for members and referees)
    private void showResultsFragment(){
        tvMessage.setVisibility(View.VISIBLE);

        //((FrameLayout)findViewById(R.id.frame_layout)).removeAllViews();
        tvMessage.setText("Здесь будет итоговый протокол, как только ГСК его сформирует");
        //show FrameLayout
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        // and fragment
        resultFragment = ResultFragment.newInstance(info.getChampID());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,  resultFragment)
                .commitNow();
    }

    // (for referee)
    private void showDocsFragment(){
        //show FrameLayout
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        // and fragment
        membersForRefereeFragment = MembersForRefereeFragment.newInstance(info.getChampID());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,  membersForRefereeFragment)
                .commitNow();
    }
    @Override
    public void showError(String err){
        Snackbar.make(findViewById(R.id.main_layout), err, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //save path to Prefs
        if (data != null) {
            Uri tsmPath = data.getData();

            if (docsFragment != null) {
                docsFragment.tsmSave(tsmPath);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 ){//to save total protocol (admin)
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.e("TAG", "start saving");
                viewModel.createTotalProtocol(this, info);
            }  else {
                showError("Для сохранения протокола\nНеобходимо разрешение");
            }
        }
        if (requestCode == 2 ){//to save referee protocol (referee)
            Log.e("TAG", "estim permission");
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (membersForRefereeFragment != null){
                    membersForRefereeFragment.permissionGranted();
                }
            }  else {
                showError("Для сохранения протокола\nНеобходимо разрешение");
            }
        }
        if (requestCode == 3 ){//to open tsm (member)
            Log.e("TAG", "tsm permission granted");
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (docsFragment != null){
                    docsFragment.showTSMPickerDialog();
                }
            }  else {
                showError("Для отправки справки ТСМ\nНеобходимо разрешение");
            }
        }
        if (requestCode == 4 ){//download total protocol (member)
            Log.e("TAG", "resultFragment permission granted");
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (resultFragment != null){
                    resultFragment.downloadTotalProtocolFile();
                }
            }  else {
                showError("Для загрузки протокола\nНеобходимо разрешение");
            }
        }
    }

    private boolean isInfoLayoutOpen = true;
    public void onHideOrShowInfo(View view) {
        if (isInfoLayoutOpen){
            findViewById(R.id.info_layout).setVisibility(View.GONE);
        }else{
            findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
        }
        isInfoLayoutOpen = !isInfoLayoutOpen;
    }
}