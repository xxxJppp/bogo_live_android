package com.bogokj.xianrou.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bogokj.xianrou.fragment.ShortVideoPlayerFragment;
import com.bogokj.xianrou.model.QKSmallVideoListModel;

import java.util.List;

public class ShortVideoAdapter extends FragmentPagerAdapter {

    private List<QKSmallVideoListModel> list;
    private List<ShortVideoPlayerFragment> mViewSparseArray;

    public ShortVideoAdapter(FragmentManager fm, List<QKSmallVideoListModel> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        Bundle bundle = new Bundle();
        bundle.putParcelable(ShortVideoPlayerFragment.VIDEO_DATA,list.get(position));
        ShortVideoPlayerFragment shortVideoPlayerFragment = new ShortVideoPlayerFragment();
        shortVideoPlayerFragment.setArguments(bundle);
        return shortVideoPlayerFragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return list.size();
    }

}