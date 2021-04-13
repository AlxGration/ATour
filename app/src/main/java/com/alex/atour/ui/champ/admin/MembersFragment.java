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
    private final String champID;

    public static MembersFragment newInstance(String champID) {
        return new MembersFragment(champID);
    }

    private MembersFragment(String champID){this.champID = champID;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MembersViewModel.class);
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
