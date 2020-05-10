package com.bogokj.live.appview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.App_request_get_pk_infoActModel;
import com.bogokj.live.model.PkResultData;
import com.bogokj.live.view.LivePKView;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDViewUtil;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class LivePKViewerContentView extends BaseAppView {

    /*=========================PK================================*/
    private FrameLayout frame_layout_pk;
    private TXCloudVideoView mViewPKView;
    private TXLivePlayer mPKLivePlayer;
    private LivePKView livePKView;
    private FrameLayout frame_layout_pk_touch;
    private RelativeLayout rl_pk_touch_layout;
    private int hostRoomId2;
    //PK倒计时
    private CountDownTimer pkCountDownTimer;
    //惩罚倒计时
    private CountDownTimer pkPunishCountDownTimer;
    //pk整体布局
    private RelativeLayout rl_pk_layout;
    private RelativeLayout bg_pk_count;
    private TextView pk_count_timer;
    private String pk_id;
    private String createId;

    /*=========================PK end================================*/

    public LivePKViewerContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LivePKViewerContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LivePKViewerContentView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setContentView(R.layout.include_pk_viewer_view);
        frame_layout_pk = find(R.id.frame_layout_pk);
        mViewPKView = find(R.id.video_view_pk);
        livePKView = find(R.id.view_pk);
        bg_pk_count = find(R.id.bg_pk_count);
        pk_count_timer = find(R.id.pk_count_timer);
        rl_pk_layout = find(R.id.rl_pk_layout);
        //初始化PK播放器
        if (mPKLivePlayer == null) {
            mPKLivePlayer = new TXLivePlayer(getContext());
        }
        mViewPKView.setVisibility(View.VISIBLE);
        mPKLivePlayer.setPlayerView(mViewPKView);
        mPKLivePlayer.enableHardwareDecode(true);
        mPKLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

    }

    //获取PK信息
    public void requestGetPkInfo(final String pkId) {
        this.pk_id = pkId;
        showProgressDialog("正在加载数据...");
        CommonInterface.requestGetPKInfo(pkId, new AppRequestCallback<App_request_get_pk_infoActModel>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {

                dismissProgressDialog();
                if (actModel.isOk()) {
                    startShowPK(actModel);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }

    //开始PK，改变视图位置
    public void startShowPK(App_request_get_pk_infoActModel data) {
        if (data.getPk_status() == 0) {
            return;
        }

        rl_pk_layout.setVisibility(View.VISIBLE);
        rl_pk_touch_layout.setVisibility(View.VISIBLE);

        showPKLoadingAnimation(true);

        if (data.getEmcee_user_id1().equals(createId)) {
            mPKLivePlayer.startPlay(data.getPlay_url2_def(), data.getPlay_url2_def().contains("rtmp") ? TXLivePlayer.PLAY_TYPE_LIVE_RTMP : TXLivePlayer.PLAY_TYPE_LIVE_FLV);
            //playerPK.startPlay(data.getPlay_url2());
            livePKView.setProgress(data.getPk_ticket1(), data.getPk_ticket2());
            hostRoomId2 = Integer.parseInt(data.getGroup_id2());
        } else {
            mPKLivePlayer.startPlay(data.getPlay_url1_def(), data.getPlay_url1_def().contains("rtmp") ? TXLivePlayer.PLAY_TYPE_LIVE_RTMP : TXLivePlayer.PLAY_TYPE_LIVE_FLV);
            //playerPK.startPlay(data.getPlay_url1());
            livePKView.setProgress(data.getPk_ticket2(), data.getPk_ticket1());
            hostRoomId2 = Integer.parseInt(data.getGroup_id1());
        }
        mPKLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle bundle) {
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPKLoadingAnimation(false);
                        }
                    });
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });

        int width = SDViewUtil.getScreenWidth() / 2;
        RelativeLayout.LayoutParams videoRightParams = new RelativeLayout.LayoutParams(width, width / 2 * 3);
        videoRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoRightParams.setMargins(0, 0, 0, 0);
        frame_layout_pk.setLayoutParams(videoRightParams);
        frame_layout_pk_touch.setLayoutParams(videoRightParams);
        //mPlayView.changePKView(true);

        if (data.getPk_status() == 1) {

            if (pkViewCallback != null) {
                pkViewCallback.onPKViewCallStartPk();
            }

            if (data.getPk_time() > 0) {
                pkCountDownTimer = new CountDownTimer(data.getPk_time() * 1000, 1000) {
                    @Override
                    public void onTick(long leftTime) {
                        int time = (int) (leftTime / 1000);

                        pk_count_timer.setText(time + "秒");
                    }

                    @Override
                    public void onFinish() {
                        pkCountDownTimer.cancel();
                        startPunishTime();
                    }
                };

                pkCountDownTimer.start();
            }

        } else {
            startPunishTime();
        }
    }

    //结束PK
    public void stopPK() {
        if (isEmcee()) {
            requestEndPk();
        }
        showPKLoadingAnimation(false);

        if (pkViewCallback != null) {
            pkViewCallback.onPKViewCallStopPk();
        }

        rl_pk_layout.setVisibility(View.GONE);
        rl_pk_touch_layout.setVisibility(View.GONE);
        bg_pk_count.setBackground(getResources().getDrawable(R.drawable.pk_count_bg));
        livePKView.setInit();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        rl_pk_layout.setLayoutParams(layoutParams);
        rl_pk_touch_layout.setLayoutParams(layoutParams);

        //mPlayView.changePKView(false);
        // videoView.setVisibility(View.GONE);
        mPKLivePlayer.stopPlay(true);
        mViewPKView.stop(true);
        mViewPKView.onDestroy();

        if (pkPunishCountDownTimer != null) {
            pkPunishCountDownTimer.cancel();
        }
        if (pkCountDownTimer != null) {
            pkCountDownTimer.cancel();
        }

    }


    //开始惩罚时间
    public void startPunishTime() {
        if (pkViewCallback != null) {
            pkViewCallback.onPKViewCallPunishTime();
        }

        if (isEmcee()) {
            CommonInterface.requestStartPunishmentData(pk_id, new AppRequestCallback<PkResultData>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (actModel.getStatus() == 1) {
                        setPunishTimeView();
                    }
                }
            });
        } else {
            setPunishTimeView();
        }
    }

    private boolean isEmcee() {
        return UserModelDao.getUserId().equals(createId);
    }

    private void setPunishTimeView() {
        //设置PK结果
        livePKView.showPKResult();
        //开始惩罚倒计时
        bg_pk_count.setBackground(getResources().getDrawable(R.drawable.bg_pk_punish_time));

        if (pkPunishCountDownTimer != null) {
            pkPunishCountDownTimer.cancel();
        }
        pkPunishCountDownTimer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                int time = (int) (l / 1000);
                pk_count_timer.setText(time + "秒");
            }

            @Override
            public void onFinish() {
                stopPK();
            }
        };
        pkPunishCountDownTimer.start();
    }

    //更新PK进度条内容
    public void updateProgress(String emceeId1, String createEmceeId, int ticket1, int ticket2) {
        //更新PK进度条
        if (emceeId1.equals(createEmceeId)) {
            livePKView.setProgress(ticket1, ticket2);
        } else {
            livePKView.setProgress(ticket2, ticket1);
        }
    }

    protected void showPKLoadingAnimation(boolean show) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.loading_background_pk);
        ImageView imageView = (ImageView) findViewById(R.id.loading_imageview_pk);
        frameLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        imageView.setVisibility(show ? View.VISIBLE : View.GONE);
        imageView.setImageResource(R.drawable.linkmic_loading);
        AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();
        if (show) {
            ad.start();
        } else {
            ad.stop();
        }
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public void requestEndPk() {
        CommonInterface.requestEndLivePk(new AppRequestCallback<String>() {
            @Override
            protected void onSuccess(SDResponse resp) {

            }
        });
    }

    public TXCloudVideoView getViewPKView() {
        return mViewPKView;
    }

    private PKViewCallback pkViewCallback;

    public void setPkViewCallback(PKViewCallback pkViewCallback) {
        this.pkViewCallback = pkViewCallback;
    }

    public void setFrame_layout_pk_touch(FrameLayout frame_layout_pk_touch) {
        this.frame_layout_pk_touch = frame_layout_pk_touch;
        frame_layout_pk_touch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d("切换房间");
                if (pkViewCallback != null) {
                    pkViewCallback.onSwitchRoom(hostRoomId2);
                }

            }
        });
    }

    public void setRl_pk_touch_layout(RelativeLayout rl_pk_touch_layout) {
        this.rl_pk_touch_layout = rl_pk_touch_layout;
    }

    public interface PKViewCallback {
        void onPKViewCallStartPk();

        void onPKViewCallPunishTime();

        void onPKViewCallStopPk();

        void onSwitchRoom(int hostRoomId2);
    }
}
