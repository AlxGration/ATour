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
import com.alex.atour.models.MembersListRecyclerAdapter;
import com.alex.atour.ui.champ.ChampActivity;

public class MembersForRefereeFragment extends Fragment {

    private MembersViewModel viewModel;
    public static final String CHAMP_ID = "CHAMP_ID";
    private String champID;

    public static MembersForRefereeFragment getInstance(String champID) {
        Bundle args = new Bundle();
        args.putString(CHAMP_ID, champID);
        MembersForRefereeFragment fragment = new MembersForRefereeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MembersForRefereeFragment(){    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MembersViewModel.class);

        if (getArguments() != null) {
            champID = getArguments().getString(CHAMP_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_referee_members, container, false);

        TextView tvError = view.findViewById(R.id.tv_error);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getMembersLiveData().observe(getViewLifecycleOwner(), members -> {
            MembersListRecyclerAdapter adapter = new MembersListRecyclerAdapter(members);
            MembersListRecyclerAdapter.setOnItemClickListener(2, ((ChampActivity) getActivity()));
            recyclerView.setAdapter(adapter);
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);

        viewModel.requestMembersList(champID);
        return view;
    }
}
