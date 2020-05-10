package com.bogokj.live.room.back;

import android.os.Bundle;

import com.bogokj.live.LiveInformation;
import com.bogokj.live.appview.LiveVideoView;
import com.bogokj.live.control.LivePlayerSDK;
import com.bogokj.live.event.EOnBackground;
import com.bogokj.live.event.EOnResumeFromBackground;
import com.bogokj.live.model.LiveQualityData;
import com.bogokj.live.room.audience.LiveLayoutViewerExtendFragment;

/**
 * @author kn update
 * @description: 直播间回放界面父类
 * @time 2020/2/17
 */
public class LivePlayFragment extends LiveLayoutViewerExtendFragment implements LivePlayerSDK.TPlayCallback {

    private LiveVideoView mVideoView;
    private boolean mIsPauseMode = false;

    private boolean mIsPlayEnd = false;

    @Override
    protected void init() {
        super.init();
        LiveInformation.getInstance().setPlayback(true);
    }


    public void setVideoView(LiveVideoView videoView) {
        this.mVideoView = videoView;

        this.mVideoView.setPlayCallback(this);
    }

    public LivePlayerSDK getPlayer() {
        if (mVideoView != null) {
            return mVideoView.getPlayer();
        }
        return null;
    }

    public LiveVideoView getVideoView() {
        return mVideoView;
    }

    public void setPauseMode(boolean pauseMode) {
        mIsPauseMode = pauseMode;
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {

    }

    @Override
    public void onPlayBegin() {
        mIsPlayEnd = false;
    }

    @Override
    public void onPlayRecvFirstFrame() {

    }

    @Override
    public void onPlayProgress(long total, long progress) {

    }

    @Override
    public void onPlayEnd() {
        mIsPlayEnd = true;
    }

    @Override
    public void onPlayLoading() {

    }

    @Override
    public LiveQualityData onBsGetLiveQualityData() {
        return getPlayer().getLiveQualityData();
    }

    public boolean isPlaying() {
        if (getPlayer() != null) {
            return getPlayer().isPlaying();
        }
        return false;
    }

    public void onEventMainThread(EOnBackground event) {
        getPlayer().pause();
    }

    public void onEventMainThread(EOnResumeFromBackground event) {
        if (mIsPauseMode) {
            //暂停模式不处理
        } else {
            if (!mIsPlayEnd) {
                getPlayer().resume();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPlayer().onDestroy();
    }
}
