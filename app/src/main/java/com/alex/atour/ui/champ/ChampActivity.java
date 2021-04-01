package com.alex.atour.ui.champ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alex.atour.R;
import com.alex.atour.ui.memrequest.MembershipRequestActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChampActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);
    }

    public void onClickBackBtn(View view) {
        finish();
    }

    public void onClickSendBtn(View view) {
        startActivity(
                new Intent(this, MembershipRequestActivity.class)
        );
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }
}