package com.alex.atour.ui.champ;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.ui.create.memrequest.MembershipRequestActivity;
import com.alex.atour.ui.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChampActivity extends AppCompatActivity {

    private TextView tvResultInfo, tvAdminFio;
    private Button btnSendRequest;
    private ProgressBar pBar;
    private ChampInfo info;
    private User admin;

    private ChampViewModel viewModel;

    //todo: hide btnSendRequest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);

        viewModel = new ViewModelProvider(this).get(ChampViewModel.class);

        //init ui
        tvResultInfo = findViewById(R.id.tv_result_info);
        btnSendRequest = findViewById(R.id.btn_send_request);
        tvAdminFio = findViewById(R.id.tv_fio);
        TextView tvError = findViewById(R.id.tv_error);
        pBar = findViewById(R.id.progress_bar);

        //setting ChampInfo on UI
        setDataOnUI((ChampInfo) getIntent().getSerializableExtra("champInfo"));

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
        viewModel.getErrorMessage().observe(this, tvError::setText);
    }

    public void onClickBackBtn(View view) {
        finish();
    }

    public void onClickMemRequestBtn(View view) {
        Intent intent = new Intent(this, MembershipRequestActivity.class);
        intent.putExtra("chamID", info.getChampID());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            //todo: different views for roles
            // for member
            tvResultInfo.setText(R.string.request_pending);
            btnSendRequest.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickShowProfile(View view){
        if (admin == null) {
            viewModel.requestError("Не удалось загрузить информацию об админестраторе");
            return;
        }

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userInfo", admin);
        startActivity(intent);
    }

    private void setDataOnUI(ChampInfo info){
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


        //request admin info
        viewModel.requestAdminData(info.getAdminID());
    }
}