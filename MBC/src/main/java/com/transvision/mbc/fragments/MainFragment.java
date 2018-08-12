package com.transvision.mbc.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transvision.mbc.MainActivity;
import com.transvision.mbc.R;

import java.util.ArrayList;
/**
 * Created by Sourav
 */
public class MainFragment extends Fragment {
    View view;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ArrayList<String> tabs_list;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        tabs_list = ((MainActivity) getActivity()).gettabs_list();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //return ((MainActivity) getActivity()).getFragment(MainActivity.Steps.FORM1);
                case 1:
                    return ((MainActivity) getActivity()).getFragment(MainActivity.Steps.FORM2);
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs_list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs_list.get(position);
        }
    }
}
