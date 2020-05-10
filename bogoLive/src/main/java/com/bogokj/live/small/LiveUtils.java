package com.bogokj.live.small;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveVideoView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.control.LivePlayerSDK;
import com.bogokj.live.model.JoinLiveData;
import com.tencent.rtmp.TXLivePlayer;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

/**
 * @author kn create
 * @description: 直播小窗工具类
 * @date : 2020/1/15
 */
public class LiveUtils {

    public static final String TAG = "LiveUtils";
    private static FrameLayout toucherLayout;
    private static LivePlayerSDK player;
    private static boolean isCanInit = true;
    private static ImageView imageViewClose;

    public static void init(Context context, Activity activity, JoinLiveData joinLiveData) {

        //获取浮动窗口视图所在布局.
        toucherLayout = new FrameLayout(context);
        LiveVideoView liveVideoView = new LiveVideoView(context);
        player = liveVideoView.getPlayer();

        String play_url = LiveInformation.getInstance().getRoomInfo().getPlay_url();
        if (validatePlayUrl(play_url)) {
            player.stopPlay();
            player.setUrl(play_url);
            player.startPlay();
        }


        toucherLayout.addView(liveVideoView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        imageViewClose = new ImageView(context);
        imageViewClose.setImageResource(R.drawable.ic_live_bottom_close);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                SDViewUtil.dp2px(25), SDViewUtil.dp2px(25));
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        imageViewClose.setLayoutParams(layoutParams);

        toucherLayout.addView(imageViewClose, layoutParams);

        FloatWindow
                .with(context)
                .setView(toucherLayout)
                .setWidth(Screen.width, 0.3f) //设置悬浮控件宽高
                .setHeight(Screen.width, 0.5f)
                .setX(Screen.width, 0.4f)
                .setY(Screen.height, 0.6f)
                .setMoveType(MoveType.slide, 100, 0)
                .setMoveStyle(500, new BounceInterpolator())
                .setViewStateListener(mViewStateListener)
                .setPermissionListener(null)
                .setDesktopShow(true)
                .setTag("live")
                .build();

        if (isCanInit) {
            FloatWindow.get("live").show();
        }


        //删除
        imageViewClose.setOnClickListener(v -> {
            destoryWindow();
        });

        //点击进入直播间
        toucherLayout.setOnClickListener(v -> {
            destoryWindow();
            AppRuntimeWorker.joinLive(joinLiveData, activity);

        });

        isCanInit = false;

    }

    /**
     * 销毁悬浮窗
     */
    public static void destoryWindow() {

        if (player != null) {
            player.stopPlay();
            player = null;
        }
        if (toucherLayout != null) {
            FloatWindow.destroy("live");
            isCanInit = true;
        }

    }


    protected static boolean validatePlayUrl(String playUrl) {
        if (TextUtils.isEmpty(playUrl)) {
            SDToast.showToast("未找到直播地址");
            return false;
        }

        if (playUrl.startsWith("rtmp://")) {
            player.setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
            player.setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_FLV);
        } else {
            SDToast.showToast("播放地址不合法");
            return false;
        }

        return true;
    }


    public static void hideWindow() {
        FloatWindow.get("live").hide();
    }


    private static ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }
    };

}
