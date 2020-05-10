package com.bogokj.live.room;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bogokj.live.room.audience.LivePushViewerFragment;
import com.bogokj.live.model.LiveRoomModel;

import java.util.List;

/**
 * @author kn update
 * @description: 观众页面可上下滑动适配器
 * @time 2020/2/27
 */
public class LiveAdapter extends FragmentPagerAdapter {

    private List<LiveRoomModel> list;

    public LiveAdapter(FragmentManager fm, List<LiveRoomModel> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {

        LiveRoomModel liveDataBean = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(LivePushViewerFragment.EXTRA_ROOM_ID, liveDataBean.getRoom_id());
        bundle.putString(LivePushViewerFragment.EXTRA_GROUP_ID, liveDataBean.getGroup_id());
        bundle.putString(LivePushViewerFragment.EXTRA_CREATER_ID, liveDataBean.getUser_id());
        bundle.putString(LivePushViewerFragment.EXTRA_LOADING_VIDEO_IMAGE_URL, liveDataBean.getLive_image());

        LivePushViewerFragment shortVideoPlayerFragment = new LivePushViewerFragment();
        shortVideoPlayerFragment.setArguments(bundle);
        return shortVideoPlayerFragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return list.size();
    }

}