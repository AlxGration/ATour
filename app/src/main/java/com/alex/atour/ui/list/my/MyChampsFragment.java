package com.alex.atour.ui.list.my;

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
import com.alex.atour.models.ChampsListRecyclerAdapter;
import com.alex.atour.ui.list.MainActivity;


public class MyChampsFragment extends Fragment{

    private MyChampsViewModel viewModel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyChampsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_champs_list, container, false);

        TextView tvError = view.findViewById(R.id.tv_error);;
        RecyclerView recyclerView = view.findViewById(R.id.rv_champs_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getChampsLiveData().observe(getViewLifecycleOwner(), champs-> {
            ChampsListRecyclerAdapter adapter = new ChampsListRecyclerAdapter(champs);
            adapter.setOnItemClickListener(((MainActivity)getActivity()));
            recyclerView.setAdapter(adapter);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), tvError::setText);

        viewModel.requestChampsList();
        return view;
    }
}