package com.alex.atour.ui.champ.referee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.R;
import com.alex.atour.models.ConfirmationDialog;
import com.alex.atour.models.EstimsRecyclerAdapter;
import com.alex.atour.models.ExcelModule;
import com.alex.atour.models.MembersListRecyclerAdapter;
import com.alex.atour.ui.champ.ChampActivity;

import java.util.ArrayList;
import java.util.TreeSet;

public class MembersForRefereeFragment extends Fragment {

    private MembersViewModel viewModel;
    private final String champID;
    private EditText etRefereeInfo;
    private EstimsRecyclerAdapter adapter;

    public static MembersForRefereeFragment newInstance(String champID) {
        return new MembersForRefereeFragment(champID);
    }

    private MembersForRefereeFragment(String champID){this.champID = champID;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MembersViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.requestMembersList(champID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_referee_members, container, false);

        etRefereeInfo = view.findViewById(R.id.et_referee_info);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button btnSendEstimations = view.findViewById(R.id.btn_send);
        btnSendEstimations.setOnClickListener(v->{
            ConfirmationDialog confirmationDialog = new ConfirmationDialog("Вы уверены?\nКопия протокола будет сохранена в папке Documents", () -> {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Log.e("TAG", "start saving");
                    viewModel.sendEstimations(getActivity(), etRefereeInfo.getText().toString());
                }else {
                    //запрашиваем разрешение
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

            });
            confirmationDialog.show(getActivity().getSupportFragmentManager(), "myDialog");
        });
        viewModel.getMembersLiveData().observe(getViewLifecycleOwner(), members -> {
            adapter = new EstimsRecyclerAdapter(members);
            EstimsRecyclerAdapter.setOnItemClickListener(2, ((ChampActivity) getActivity()));
            recyclerView.setAdapter(adapter);
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), ((ChampActivity)getActivity())::showError);
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), ((ChampActivity)getActivity())::showLoadingProcess);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == -1){
            Log.e("TAG", "start saving");
            viewModel.sendEstimations(getActivity(), etRefereeInfo.getText().toString());
        }
    }
}
