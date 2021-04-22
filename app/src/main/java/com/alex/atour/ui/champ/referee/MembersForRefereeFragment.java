package com.alex.atour.ui.champ.referee;

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
import com.alex.atour.models.EstimsRecyclerAdapter;
import com.alex.atour.models.MembersListRecyclerAdapter;
import com.alex.atour.ui.champ.ChampActivity;

public class MembersForRefereeFragment extends Fragment {

    private MembersViewModel viewModel;
    private final String champID;
    EstimsRecyclerAdapter adapter;

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
        //viewModel.getMembersFromLocalDB();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_referee_members, container, false);

        TextView tvError = view.findViewById(R.id.tv_error);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        viewModel.getMembersLiveData().observe(getViewLifecycleOwner(), members -> {
            adapter = new EstimsRecyclerAdapter(members);
            EstimsRecyclerAdapter.setOnItemClickListener(2, ((ChampActivity) getActivity()));
            recyclerView.setAdapter(adapter);
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), ((ChampActivity)getActivity())::showLoadingProcess);

        return view;
    }
}
