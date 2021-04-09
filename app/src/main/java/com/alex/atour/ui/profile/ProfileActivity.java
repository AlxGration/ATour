package com.alex.atour.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.ui.login.LoginActivity;
import com.alex.atour.ui.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private ProgressBar pBar;
    private Button btnSignOut;
    private TextView tvError, tvName, tvSecName, tvCity, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        btnSignOut = findViewById(R.id.btn_sign_out);
        tvError = findViewById(R.id.tv_error);
        tvName = findViewById(R.id.tv_name);
        tvSecName = findViewById(R.id.tv_sec_name);
        tvCity = findViewById(R.id.tv_city);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        pBar = findViewById(R.id.progress_bar);

        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(
                    isLoading? View.VISIBLE: View.INVISIBLE
            );
        });
        viewModel.getErrorMessage().observe(this, tvError::setText);
        viewModel.getSecName().observe(this, tvSecName::setText);
        viewModel.getSecName().observe(this, tvSecName::setText);
        viewModel.getName().observe(this, tvName::setText);
        viewModel.getCity().observe(this, tvCity::setText);
        viewModel.getEmail().observe(this, tvEmail::setText);
        viewModel.getPhone().observe(this, tvPhone::setText);



        User u = (User) getIntent().getSerializableExtra("userInfo");
        MembershipRequest req = (MembershipRequest) getIntent().getSerializableExtra("request");
        String userID = getIntent().getStringExtra("userID");

        if (req != null){   //если пришли на этот экран для просмотра документов/заявки
            //todo:show MembershipRequest + requestUserInfo by userID
            viewModel.loadProfile(userID);

        }else {
            if (u == null) {                                                     //если открываю свой провиль
                viewModel.loadProfile(userID);
                btnSignOut.setVisibility(View.VISIBLE);
            } else {                                                              //если чей то другой
                viewModel.setUserInfo(u);
            }
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
}