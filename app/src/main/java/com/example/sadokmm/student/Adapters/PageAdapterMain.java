package com.example.sadokmm.student.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sadokmm.student.Fragments.LoginFragment;
import com.example.sadokmm.student.Fragments.PostFragment;
import com.example.sadokmm.student.Fragments.RegisterFragment;
import com.example.sadokmm.student.Fragments.TimeFragment;

public class PageAdapterMain extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterMain(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TimeFragment();
            case 1:
                return new PostFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Horaires";
            case 1:
                return "Discussion";
            default:
                return null;
        }
    }
}