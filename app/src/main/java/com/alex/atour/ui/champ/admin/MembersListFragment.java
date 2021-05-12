package com.alex.atour.ui.champ.admin;

import android.os.Bundle;
import android.util.Log;
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
import com.alex.atour.ui.champ.ChampActivity;

public class MembersListFragment extends Fragment {

    private MembersViewModel viewModel;
    public static final String ROLE = "ROLE";
    private int role;

    public static MembersListFragment newInstance( int role) {
        Bundle args = new Bundle();
        args.putInt(ROLE, role);
        MembersListFragment fragment = new MembersListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MembersViewModel.class);
        if (getArguments() != null) {
            role = getArguments().getInt(ROLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("TAG", "MembersListFragment");

        View view = inflater.inflate(R.layout.list, container, false);

        TextView tvError = view.findViewById(R.id.tv_error);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (role == 1) {//участники
            viewModel.getMembersLiveData().observe(getViewLifecycleOwner(), members -> {
                Log.e("TAG", "MembersListFragment members = "+members.size());

                MembersListRecyclerAdapter adapter = new MembersListRecyclerAdapter(members);
                MembersListRecyclerAdapter.setOnItemClickListener(1, ((ChampActivity) getActivity()));
                recyclerView.setAdapter(adapter);
            });
        }else{          //судьи
            viewModel.getRefereesLiveData().observe(getViewLifecycleOwner(), members -> {
                Log.e("TAG", "MembersListFragment referees = "+members.size());

                MembersListRecyclerAdapter adapter = new MembersListRecyclerAdapter(members);
                MembersListRecyclerAdapter.setOnItemClickListener(1, ((ChampActivity) getActivity()));
                recyclerView.setAdapter(adapter);
            });
        }
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);

        return view;
    }
}