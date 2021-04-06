package com.alex.atour.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.atour.R;

public class SearchFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewModel = new ViewModelProvider(this).get(MyChampsViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        //viewModel.getFavouriteStocksFromDB();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_champs_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_champs_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*
        viewModel.getFavouriteStocksMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stocks) {
                StocksListRecyclerAdapter adapter = new StocksListRecyclerAdapter(stocks);
                adapter.setOnItemClickListener(((MainActivity)getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });
        */
        return view;
    }

    public void setSearchQuery(String searchRequest){
        //TODO: SEARCH QUERY
    }
}