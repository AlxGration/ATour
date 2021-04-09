package com.alex.atour.ui.requests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.R;
import com.alex.atour.models.ChampsListRecyclerAdapter;
import com.alex.atour.models.RequestsListRecyclerAdapter;
import com.alex.atour.ui.list.MainActivity;
import com.alex.atour.ui.profile.ProfileActivity;

public class RequestsListActivity extends AppCompatActivity implements RequestsListRecyclerAdapter.IonItemClickListener {

    private RequestsViewModel viewModel;

    private String champID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        viewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        TextView tvError = findViewById(R.id.tv_error);
        ProgressBar pBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        champID = getIntent().getStringExtra("champID");

        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                    View.VISIBLE:
                    View.INVISIBLE
            );
        });
        viewModel.getErrorMessage().observe(this, tvError::setText);
        viewModel.getRequestsLiveData().observe(this, requests -> {
            RequestsListRecyclerAdapter adapter = new RequestsListRecyclerAdapter(requests);
            RequestsListRecyclerAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        });


        //ЗАПРОС СПИСКА ЗАЯВОК
        viewModel.getRequests(champID);
    }

    @Override
    public void startProfileActivityWith(MembershipRequest req) {
        Intent intent = new Intent(RequestsListActivity.this, ProfileActivity.class);
        intent.putExtra("request", req);
        intent.putExtra("userID", req.getUserID());

        startActivity(intent);
    }

    @Override
    public void acceptRequest(MembershipRequest req) {
        viewModel.acceptRequest(req);
        Toast.makeText(this, "acceptRequest", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void denyRequest(MembershipRequest req) {
        viewModel.denyRequest(req);
        Toast.makeText(this, "denyRequest", Toast.LENGTH_SHORT).show();
    }

    public void onClickBackBtn(View view) {
        finish();
    }
}