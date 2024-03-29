package com.alex.atour.ui.create.memrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.R;
import com.alex.atour.models.NetworkStateChangeReceiver;
import com.google.android.material.snackbar.Snackbar;

public class MembershipRequestActivity extends AppCompatActivity  implements NetworkStateChangeReceiver.NetworkStateChangeListener{

    private EditText etComment, etLink;
    private MemReqViewModel viewModel;
    private boolean isReferee;
    private Button btnSend;
    private ImageButton btnOk;
    private ProgressBar pBar;
    private MembershipRequest memReq;
    private NetworkStateChangeReceiver receiver;
    private TextView tvNetworkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_request);

        viewModel = new ViewModelProvider(this).get(MemReqViewModel.class);

        memReq = new MembershipRequest();
        ChampInfo info = (ChampInfo) getIntent().getSerializableExtra("champInfo");
        enableNeededTypes(info);
        memReq.setChampID(info.getChampID());
        memReq.setRole(1);//по умолчанию это участник

        //init ui
        etLink = findViewById(R.id.et_link);
        btnSend = findViewById(R.id.btn_send);
        btnOk = findViewById(R.id.btn_ok);
        etComment = findViewById(R.id.et_comment);
        pBar = findViewById(R.id.progress_bar);
        tvNetworkState = findViewById(R.id.tv_network_bar);
        RadioGroup rgRole = findViewById(R.id.rg_role);
        rgRole.setOnCheckedChangeListener((radioGroup, id)->{
            isReferee = (id==R.id.rb_referee);

            memReq.setRole(isReferee? 2: 1);//1 - member, 2 - referee
            etLink.setVisibility( isReferee?
                            View.INVISIBLE:
                            View.VISIBLE
            );
        });

        //observers
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                            View.VISIBLE:
                            View.INVISIBLE
            );
            btnSend.setEnabled(!isLoading);
            btnOk.setEnabled(!isLoading);
        });
        viewModel.getErrorMessage().observe(this, this::showError);
        viewModel.getIsRequestSuccess().observe(this, isRequestSuccess->{
            if (isRequestSuccess){  //если запрос отправлен успешно, то активность закрывается и выходит на экран чемпионата
                finish();
            }
        });

        //проверка подключения internet
        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    public void onClickBackBtn(View view) { finish(); }

    public void onClickSendBtn(View view) {
        memReq.setCloudLink(etLink.getText().toString());
        //todo: make uploading files and link with memReq.docsLink
        memReq.setComment(etComment.getText().toString());

        viewModel.sendMembershipRequest(memReq);
    }

    public void onChooseType(View view) {
        boolean isChecked = ((ToggleButton)view).isChecked();

        switch (view.getId()){
            case R.id.tb_walk: memReq.setTypeWalk(isChecked);
                break;
            case R.id.tb_ski: memReq.setTypeSki(isChecked);
                break;
            case R.id.tb_hike: memReq.setTypeHike(isChecked);
                break;
            case R.id.tb_water: memReq.setTypeWater(isChecked);
                break;
            case R.id.tb_speleo: memReq.setTypeSpeleo(isChecked);
                break;
            case R.id.tb_bike: memReq.setTypeBike(isChecked);
                break;
            case R.id.tb_auto: memReq.setTypeAuto(isChecked);
                break;
            case R.id.tb_other: memReq.setTypeOther(isChecked);
                break;
        }
    }

    private void enableNeededTypes(ChampInfo info){
        if (!info.isTypeWalk()) findViewById(R.id.tb_walk).setEnabled(false);
        if (!info.isTypeSki()) findViewById(R.id.tb_ski).setEnabled(false);
        if (!info.isTypeHike()) findViewById(R.id.tb_hike).setEnabled(false);
        if (!info.isTypeWater()) findViewById(R.id.tb_water).setEnabled(false);

        if (!info.isTypeSpeleo()) findViewById(R.id.tb_speleo).setEnabled(false);
        if (!info.isTypeBike()) findViewById(R.id.tb_bike).setEnabled(false);
        if (!info.isTypeAuto()) findViewById(R.id.tb_auto).setEnabled(false);
        if (!info.isTypeOther()) findViewById(R.id.tb_other).setEnabled(false);
    }
    private void showError(String err){
        Snackbar.make(findViewById(R.id.main_layout), err, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            tvNetworkState.setVisibility(View.GONE);
        }else {
            tvNetworkState.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver.detach();
        }
    }
}