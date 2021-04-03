package com.alex.atour.ui.champ;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alex.atour.R;
import com.alex.atour.ui.create.memrequest.MembershipRequestActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChampActivity extends AppCompatActivity {

    TextView tvResultInfo;
    Button btnSendRequest;

    //todo: hide btnSendRequest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);

        tvResultInfo = findViewById(R.id.tv_result_info);
        btnSendRequest = findViewById(R.id.btn_send_request);
    }

    public void onClickBackBtn(View view) {
        finish();
    }

    public void onClickMemRequestBtn(View view) {
        Intent intent = new Intent(this, MembershipRequestActivity.class);
        intent.putExtra("chamID", "asdjkl");//TODO: send real id
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
}