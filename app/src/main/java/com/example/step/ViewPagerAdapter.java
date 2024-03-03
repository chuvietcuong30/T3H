package com.example.step;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_1();
            case 1:
                return new Fragment_2();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
