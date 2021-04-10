package com.alex.atour.ui.champ.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.R;
import com.alex.atour.models.MembersListRecyclerAdapter;
import com.alex.atour.ui.champ.ChampActivity;
import com.alex.atour.ui.list.champs.MembersViewModel;

public class MembersListFragment extends Fragment {

    private MembersViewModel viewModel;
    public static final String MEM_LIST = "MEM_LIST";
    private int role;


    public static MembersListFragment newInstance(int role) {
        Bundle args = new Bundle();
        args.putInt(MEM_LIST, role);
        MembersListFragment fragment = new MembersListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MembersViewModel.class);
        if (getArguments() != null) {
            role = getArguments().getInt(MEM_LIST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list, container, false);


        TextView tvError = view.findViewById(R.id.tv_error);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (role == 1) {//участники
            viewModel.getMembersLiveData().observe(getViewLifecycleOwner(), champsList -> {
                MembersListRecyclerAdapter adapter = new MembersListRecyclerAdapter(champsList);
                MembersListRecyclerAdapter.setOnItemClickListener(((ChampActivity) getActivity()));
                recyclerView.setAdapter(adapter);
            });
        }else{          //судьи
            viewModel.getRefereesLiveData().observe(getViewLifecycleOwner(), champsList -> {
                MembersListRecyclerAdapter adapter = new MembersListRecyclerAdapter(champsList);
                MembersListRecyclerAdapter.setOnItemClickListener(((ChampActivity) getActivity()));
                recyclerView.setAdapter(adapter);
            });
        }
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);

        viewModel.requestMembersList(role);
        return view;
    }
}
