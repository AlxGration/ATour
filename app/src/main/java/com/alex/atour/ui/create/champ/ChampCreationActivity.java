package com.alex.atour.ui.create.champ;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.alex.atour.R;

public class ChampCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_creation);
    }

    public void onClickBackBtn(View view) { finish();    }

    public void onClickSendBtn(View view) {
    }

    public void onChooseType(View view) {
    }
}