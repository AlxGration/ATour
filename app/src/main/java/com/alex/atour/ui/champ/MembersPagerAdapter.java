package com.alex.atour.ui.champ;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alex.atour.ui.champ.admin.MembersFragment;
import com.alex.atour.ui.champ.admin.MembersListFragment;
import com.alex.atour.ui.list.champs.ChampsListFragment;
import com.alex.atour.ui.list.manage.ManagedChampsFragment;
import com.alex.atour.ui.list.my.MyChampsFragment;

public class MembersPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Участники", "Судьи" };

    public MembersPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // 1 - участники
        // 2 - судьи
        return MembersListFragment.newInstance(position + 1);
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        return tabTitles[position];
    }
}