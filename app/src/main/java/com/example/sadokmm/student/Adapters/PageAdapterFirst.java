package com.example.sadokmm.student.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sadokmm.student.Fragments.LoginFragment;
import com.example.sadokmm.student.Fragments.RegisterFragment;

public class PageAdapterFirst extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterFirst(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new RegisterFragment();

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
                return "Connexion";
            case 1:
                return "Inscription";
            default:
                return null;
        }
    }
}