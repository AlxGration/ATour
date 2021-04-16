package com.alex.atour.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private EstimationViewModel estimVM;
    private String champID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        estimVM = new ViewModelProvider(this).get(EstimationViewModel.class);

        Button btnSignOut = findViewById(R.id.btn_sign_out);
        TextView tvError = findViewById(R.id.tv_error);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvSecName = findViewById(R.id.tv_sec_name);
        TextView tvCity = findViewById(R.id.tv_city);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvPhone = findViewById(R.id.tv_phone);
        ProgressBar pBar = findViewById(R.id.progress_bar);
        TextView tvDLink = findViewById(R.id.tv_d_link);
        TextView tvDComment = findViewById(R.id.tv_d_comment);

        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                    View.VISIBLE:
                    View.INVISIBLE
            );
        });
        viewModel.getErrorMessage().observe(this, tvError::setText);
        viewModel.getSecName().observe(this, tvSecName::setText);
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
                mem = (Member) getIntent().getSerializableExtra("member");//участник
                userID = mem.getUserID();
                viewModel.setUserName(mem.getUserFIO());
                champID = getIntent().getStringExtra("champID");
                viewModel.loadDocs(champID, mem.getUserID());
                showEstimationLayout(mem);
                break;
            case 5://show request and docs (for admin)
                mem = (Member) getIntent().getSerializableExtra("member");
                userID = mem.getUserID();
                champID = getIntent().getStringExtra("champID");
                viewModel.loadProfile(userID);          // показ регистрационных данных пользователя
                viewModel.loadMembershipRequest(champID, userID);
                if (mem.getRole() == 1)
                    viewModel.loadDocs(champID, userID);    // показ документов
                else showEstimations(champID, userID);
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
    private void showEstimationLayout(Member req){
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

        (findViewById(R.id.img_title_city)).setVisibility(View.GONE);
        (findViewById(R.id.tv_title_email)).setVisibility(View.GONE);
        (findViewById(R.id.tv_title_phone)).setVisibility(View.GONE);
    }

    private void showLayout(int id, int visibility){
        findViewById(id).setVisibility(visibility);
    }

    //показ всех оценок, которые выставил судья
    private void showEstimations(String champID, String userID){
        //todo:: create me
    }

    //send estimation (referee)
    public void onClickSendBtn(View v){
        estimVM.sendEstimation(
                champID,
                userID, //участник
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
}