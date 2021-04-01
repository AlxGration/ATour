package com.alex.atour.ui.memrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alex.atour.R;


public class MembershipRequestActivity extends AppCompatActivity {

    private EditText etComment, etLink;
    private MemReqViewModel viewModel;
    private boolean isReferee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_request);

        viewModel = new ViewModelProvider(this).get(MemReqViewModel.class);


        //init ui
        TextView tvError = findViewById(R.id.tv_error);
        etLink = findViewById(R.id.et_link);
        etComment = findViewById(R.id.et_comment);
        RadioGroup rgRole = findViewById(R.id.rg_role);
        rgRole.setOnCheckedChangeListener((radioGroup, id)->{
            isReferee = (id==R.id.rb_referee);
            etLink.setVisibility(
                    isReferee?
                            View.INVISIBLE:
                            View.VISIBLE
            );
        });

        //observers
        viewModel.getErrorMessage().observe(this, tvError::setText);


    }

    public void onClickBackBtn(View view) { finish(); }

    public void onClickSendBtn(View view) {
    }

    public void onChooseType(View view) {
        boolean isChecked = ((ToggleButton)view).isChecked();

        switch (view.getId()){
            case R.id.tb_walk:

                break;
            case R.id.tb_ski:

                break;
            case R.id.tb_hike:

                break;
            case R.id.tb_water:

                break;
            case R.id.tb_speleo:

                break;
            case R.id.tb_bike:

                break;
            case R.id.tb_auto:

                break;
            case R.id.tb_other:

                break;
        }
    }
}