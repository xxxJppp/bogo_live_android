package com.bogokj.live.business;

import com.bogokj.live.room.ILiveInterface;

/**
 * Created by Administrator on 2017/6/3.
 */

public abstract class LiveBaseBusiness extends BaseBusiness {
    private ILiveInterface mLiveActivity;

    public LiveBaseBusiness(ILiveInterface liveActivity) {
        mLiveActivity = liveActivity;
    }

    public ILiveInterface getLiveActivity() {
        return mLiveActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
