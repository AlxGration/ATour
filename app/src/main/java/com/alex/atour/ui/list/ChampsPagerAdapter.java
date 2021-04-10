package com.alex.atour.ui.list;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alex.atour.ui.list.champs.ChampsListFragment;
import com.alex.atour.ui.list.manage.ManagedChampsFragment;
import com.alex.atour.ui.list.my.MyChampsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ChampsPagerAdapter extends FragmentPagerAdapter {

    public ChampsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position){
            case 0:
                return new MyChampsFragment();
            case 1:
                return new ChampsListFragment();
            case 2:
            default:
                return new ManagedChampsFragment();
        }
    }


    @Override
    public int getCount() {
        return 3;
    }
}