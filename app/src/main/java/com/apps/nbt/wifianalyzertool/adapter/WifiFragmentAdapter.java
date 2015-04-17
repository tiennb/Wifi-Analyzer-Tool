package com.apps.nbt.wifianalyzertool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.apps.nbt.wifianalyzertool.R;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;


public class WifiFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private List<Fragment> mListFragment;

    protected static final int[] ICONS = new int[]{
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location
    };

    public WifiFragmentAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        mListFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mListFragment.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

}