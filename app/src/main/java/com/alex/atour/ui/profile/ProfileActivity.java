package com.alex.atour.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.ui.login.LoginActivity;


public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        Button btnSignOut = findViewById(R.id.btn_sign_out);
        TextView tvError = findViewById(R.id.tv_error);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvSecName = findViewById(R.id.tv_sec_name);
        TextView tvCity = findViewById(R.id.tv_city);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvPhone = findViewById(R.id.tv_phone);
        ProgressBar pBar = findViewById(R.id.progress_bar);

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


        int comeFrom = getIntent().getIntExtra("comeFrom", -1);
        String userID;
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
                showRequest(req);               // показ заявки на участие / доков
                break;
            case 3://show admin info
                User u = (User) getIntent().getSerializableExtra("userInfo");
                viewModel.setUserInfo(u);
                break;
            case 4://show docs (for referee)
                //todo::realise me
                Member mem = (Member) getIntent().getSerializableExtra("member");
                viewModel.setUserName(mem.getUserFIO());
                String champID = getIntent().getStringExtra("champID");
                viewModel.loadDocs(champID, mem.getUserID());
                showEstimationLayut(mem);
                break;
        }
    }

    public void onClickBackBtn(View view) {
        onBackPressed();
    }

    public void onClickSignOut(View view) {// выход из аккаунта
        viewModel.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showRequest(MembershipRequest req){
        TextView tvLink = findViewById(R.id.tv_link);
        TextView tvComment = findViewById(R.id.tv_comment);

        tvLink.setText(req.getCloudLink());
        tvComment.setText(req.getComment());

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

        ConstraintLayout layout = findViewById(R.id.layout_request);
        layout.setVisibility(View.VISIBLE);
    }

    private void showEstimationLayut(Member req){
        //types
        if (req.isTypeWalk()) findViewById(R.id.cp_walk).setVisibility(View.VISIBLE);
        if (req.isTypeSki()) findViewById(R.id.cp_ski).setVisibility(View.VISIBLE);
        if (req.isTypeHike()) findViewById(R.id.cp_hike).setVisibility(View.VISIBLE);
        if (req.isTypeWater()) findViewById(R.id.cp_water).setVisibility(View.VISIBLE);

        if (req.isTypeSpeleo()) findViewById(R.id.cp_speleo).setVisibility(View.VISIBLE);
        if (req.isTypeBike()) findViewById(R.id.cp_bike).setVisibility(View.VISIBLE);
        if (req.isTypeAuto()) findViewById(R.id.cp_auto).setVisibility(View.VISIBLE);
        if (req.isTypeOther()) findViewById(R.id.cp_other).setVisibility(View.VISIBLE);

        ConstraintLayout layoutDocs = findViewById(R.id.layout_request);
        layoutDocs.setVisibility(View.VISIBLE);

        LinearLayout layoutEstim = findViewById(R.id.layout_estim);
        layoutEstim.setVisibility(View.VISIBLE);
    }

}