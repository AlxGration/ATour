package com.alex.atour.ui.list.manage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.R;
import com.alex.atour.models.ChampsListRecyclerAdapter;
import com.alex.atour.ui.list.MainActivity;

import java.util.ArrayList;


public class ManagedChampsFragment extends Fragment{

    private ManagedChampsViewModel viewModel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ManagedChampsViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_champs_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_champs_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getManagedChampsLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ChampInfo>>() {
            @Override
            public void onChanged(ArrayList<ChampInfo> champsList) {
                ChampsListRecyclerAdapter adapter = new ChampsListRecyclerAdapter(champsList);
                ChampsListRecyclerAdapter.setOnItemClickListener(((MainActivity)getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });

        viewModel.requestChampsList();

        return view;
    }
}