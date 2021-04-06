package com.alex.atour.ui.champ;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.R;
import com.alex.atour.ui.create.memrequest.MembershipRequestActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChampActivity extends AppCompatActivity {

    private TextView tvResultInfo;
    private Button btnSendRequest;
    private ChampInfo info;

    //todo: hide btnSendRequest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);

        //init ui
        tvResultInfo = findViewById(R.id.tv_result_info);
        btnSendRequest = findViewById(R.id.btn_send_request);

        //setting ChampInfo on UI
        setDataOnUI((ChampInfo) getIntent().getSerializableExtra("champInfo"));
    }

    public void onClickBackBtn(View view) {
        finish();
    }

    public void onClickMemRequestBtn(View view) {
        Intent intent = new Intent(this, MembershipRequestActivity.class);
        intent.putExtra("chamID", info.getChampID());//TODO: send real id
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

    private void setDataOnUI(ChampInfo info){
        if (info == null) return;
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
}