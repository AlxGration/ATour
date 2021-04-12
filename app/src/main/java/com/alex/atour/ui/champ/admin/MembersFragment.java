package com.alex.atour.ui.champ.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.alex.atour.DTO.Member;
import com.alex.atour.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MembersFragment extends Fragment {

    private MembersViewModel viewModel;
    public static final String CHAMP_ID = "CHAMP_ID";
    private String champID;

    public static MembersFragment getInstance(String champID) {
        Bundle args = new Bundle();
        args.putString(CHAMP_ID, champID);
        MembersFragment fragment = new MembersFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private MembersFragment(){    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MembersViewModel.class);

        if (getArguments() != null) {
            champID = getArguments().getString(CHAMP_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_members, container, false);

        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new MembersPagerAdapter(getActivity().getSupportFragmentManager()));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = view.findViewById(R.id.tabs_layout);
        tabLayout.setupWithViewPager(viewPager);

        //get all members
        viewModel.requestMembersList(champID);
        return view;
    }
}
