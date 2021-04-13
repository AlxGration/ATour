package com.alex.atour.ui.champ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.atour.DTO.Champ;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.User;
import com.alex.atour.R;
import com.alex.atour.models.MembersListRecyclerAdapter;
import com.alex.atour.ui.champ.admin.MembersFragment;
import com.alex.atour.ui.champ.referee.MembersForRefereeFragment;
import com.alex.atour.ui.create.memrequest.MembershipRequestActivity;
import com.alex.atour.ui.profile.ProfileActivity;
import com.alex.atour.ui.requests.RequestsListActivity;

public class ChampActivity extends AppCompatActivity implements MembersListRecyclerAdapter.IonItemClickListener {

    private TextView tvMessage, tvAdminFio;
    private Button btnSendRequest;
    private ProgressBar pBar;
    private ChampInfo info;
    private User admin;
    private int role;

    private ChampViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);

        viewModel = new ViewModelProvider(this).get(ChampViewModel.class);

        //init ui
        tvMessage = findViewById(R.id.tv_message);
        btnSendRequest = findViewById(R.id.btn_send_request);
        tvAdminFio = findViewById(R.id.tv_fio);
        TextView tvError = findViewById(R.id.tv_error);
        pBar = findViewById(R.id.progress_bar);

        //setting ChampInfo on UI
        setChampInfoOnUI((ChampInfo) getIntent().getSerializableExtra("champInfo"));

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

        viewModel.getRoleLiveData().observe(this, role->{
            this.role = role;
            showLayoutDependsOnRoleAndState(role, -1);
        });

        viewModel.getStateLiveData().observe(this, state->{
            showLayoutDependsOnRoleAndState(role, state);
        });

        // request admin info
        // and user status and role (admin, referee, member)
        viewModel.loadPage(info.getAdminID(), info.getChampID());
    }

    public void onClickBackBtn(View view) {
        finish();
    }

    public void onClickMemRequestBtn(View view) {
        Intent intent = new Intent(this, MembershipRequestActivity.class);
        intent.putExtra("champInfo", info);
        startActivity(intent);
    }


    public void onClickShowProfile(View view){
        if (admin == null) {
            viewModel.requestError("Не удалось загрузить информацию об админестраторе");
            return;
        }

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userInfo", admin);
        intent.putExtra("comeFrom", 3);//show admin info
        startActivity(intent);
    }

    private void setChampInfoOnUI(ChampInfo info){
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
    }

    // Для админа, переход на активность для просмотра заявок на чемпионат
    private final View.OnClickListener onClickRequests = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChampActivity.this, RequestsListActivity.class);
            intent.putExtra("champID", info.getChampID());
            startActivity(intent);
        }
    };

    @Override
    public void startProfileActivityWith(int role, Member member) {
        //role - от чьего имени открывается экран
        if (role == 2){
            Intent intent = new Intent(ChampActivity.this, ProfileActivity.class);
            intent.putExtra("member", member);
            intent.putExtra("champID", info.getChampID());
            intent.putExtra("comeFrom", 4);//show docs (for referee)
            startActivity(intent);
        }
        //todo:startProfileActivityWith member (for admin, referee)
    }

    //todo:use me
    private void showLayoutDependsOnRoleAndState(int role, int state){
        Log.e("TAG", "RS "+ role+" "+state);

        if (role == 0){//admin
            btnSendRequest.setOnClickListener(onClickRequests);
            btnSendRequest.setText("Заявки");
            showMembersFragment();
        }
        if (role == 1 && state != -1){//member
            switch (state){
                case 0://MembershipState.WAIT
                    tvMessage.setText(R.string.request_pending);
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    break;
                case 1://MembershipState.DENIED
                    tvMessage.setText(R.string.request_denied);
                    btnSendRequest.setVisibility(View.VISIBLE);
                    break;
                case 2://MembershipState.ACCEPTED
                    tvMessage.setText("");
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    showDocsSendingFragment();
                    break;
                case 3://MembershipState.DOCS_SUBMISSION
                    tvMessage.setText(R.string.results_waiting);
                    break;
                case 4://MembershipState.RESULTS
                    showResultsFragment();
                    break;
            }
        }
        if (role == 2  && state != -1) {//referee
            switch (state){
                case 0://MembershipState.WAIT
                    tvMessage.setText(R.string.request_pending);
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    break;
                case 1://MembershipState.DENIED
                    tvMessage.setText(R.string.request_denied);
                    btnSendRequest.setVisibility(View.VISIBLE);
                    break;
                case 2://MembershipState.ACCEPTED
                    tvMessage.setText("");
                    btnSendRequest.setVisibility(View.INVISIBLE);
                    showDocsFragment();
                    break;
                case 4://MembershipState.RESULTS
                    //todo:check me
                    showResultsFragment();
                    break;
            }
        }
    }

    // (for admin)
    private void showMembersFragment(){
        //show FrameLayout
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        // and fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,  MembersFragment.getInstance(info.getChampID()))
                .commitNow();
    }

    // (for members)
    private void showDocsSendingFragment(){
        //todo:create me
        tvMessage.setText("members: showDocsSendingFragment");
    }
    // (for members and referees)
    private void showResultsFragment(){
        //todo:create me
        tvMessage.setText(": showResultsFragment");
    }

    // (for referee)
    private void showDocsFragment(){
        //todo:create me
        //show FrameLayout
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        // and fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,  MembersForRefereeFragment.getInstance(info.getChampID()))
                .commitNow();
    }
}