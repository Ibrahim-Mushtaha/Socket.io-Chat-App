package com.ix.ibrahim7.socketio.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerLogin extends FragmentPagerAdapter {


    public ArrayList<Fragment> getLf() {
        return lf;
    }

    public void setLf(ArrayList<Fragment> lf) {
        this.lf = lf;
    }

    public ArrayList<String> getLt() {
        return lt;
    }

    public void setLt(ArrayList<String> lt) {
        this.lt = lt;
    }

    public ViewPagerLogin(@NonNull FragmentManager fm) {
        super(fm);

    }

   private ArrayList<Fragment> lf = new ArrayList<>();
    private ArrayList<String> lt = new ArrayList<>();


    public void addFragment(Fragment fragment, String title) {
        lf.add(fragment);
        lt.add(title);
    }


    @Override
    public int getCount() {
        return lf.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return lf.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lt.get(position);
    }
}
