package com.alex.atour.ui.list.search;

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
import com.alex.atour.ui.list.ChampsListRecyclerAdapter;
import com.alex.atour.ui.list.ChampsListViewModel;
import com.alex.atour.ui.list.MainActivity;

public class SearchFragment extends Fragment {

    private ChampsListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ChampsListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list, container, false);

        TextView tvError = view.findViewById(R.id.tv_error);

        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getFoundChampsListLiveData().observe(getViewLifecycleOwner(), champsList -> {
            ChampsListRecyclerAdapter adapter = new ChampsListRecyclerAdapter(champsList);
            ChampsListRecyclerAdapter.setOnItemClickListener(((MainActivity)getActivity()));
            recyclerView.setAdapter(adapter);
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), ((MainActivity)getActivity())::showLoadingProcess);

        return view;
    }

    public void setSearchQuery(String searchRequest){
        if (searchRequest.isEmpty())
            viewModel.eraseFoundChampsList();
        else
            viewModel.requestChampsList(searchRequest);
    }
}