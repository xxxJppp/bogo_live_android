package com.bogokj.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bogokj.live.control.LivePlayerSDK;
import com.bogokj.live.control.LivePushSDK;
import com.bogokj.live.control.TPlayCallbackWrapper;
import com.bogokj.library.utils.SDViewUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 视频播放view
 */
public class LiveVideoView extends TXCloudVideoView {
    private LivePlayerSDK playerSDK;
    private LivePushSDK pushSDK;

    public LiveVideoView(Context context) {
        super(context);
        init();
    }

    public LiveVideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {

    }

    /**
     * 设置播放监听
     *
     * @param playCallback
     */
    public void setPlayCallback(LivePlayerSDK.TPlayCallback playCallback) {
        mPlayCallbackWrapper.setPlayCallback(playCallback);
    }

    /**
     * 获得播放对象
     *
     * @return
     */
    public LivePlayerSDK getPlayer() {
        if (playerSDK == null) {
            playerSDK = new LivePlayerSDK();
            playerSDK.init(this);
            playerSDK.setPlayCallback(mPlayCallbackWrapper);
        }
        return playerSDK;
    }

    /**
     * 获得推流对象
     *
     * @return
     */
    public LivePushSDK getPusher() {
        if (pushSDK == null) {
            pushSDK = LivePushSDK.getInstance();
        }
        return pushSDK;
    }

    //改变为PK状态的尺寸
    public void changePKView(boolean status) {

        if (status) {
            int width = SDViewUtil.getScreenWidth() / 2;
            RelativeLayout.LayoutParams videoLeftParams = new RelativeLayout.LayoutParams(width, width / 2 * 3);
            videoLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            videoLeftParams.setMargins(0, SDViewUtil.dp2px(100), 0, 0);
            setLayoutParams(videoLeftParams);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParams(params);
        }

    }

    private TPlayCallbackWrapper mPlayCallbackWrapper = new TPlayCallbackWrapper() {

    };

    public void destroy() {
        if (playerSDK != null) {
            playerSDK.onDestroy();
            playerSDK = null;
        }
        if (pushSDK != null) {
            pushSDK.onDestroy();
            pushSDK = null;
        }
    }
}
