package com.alex.atour.ui.list;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alex.atour.ui.list.champs.ChampsFragment;
import com.alex.atour.ui.list.manage.ManagedChampsFragment;
import com.alex.atour.ui.list.my.MyChampsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
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
                return new ChampsFragment();
            case 2:
            default:
                return new ManagedChampsFragment();
        }
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}