package com.bogokj.live.room.back;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDDateUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveVideoView;
import com.bogokj.live.appview.room.RoomPlayControlView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.GuardTableListModel;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.model.custommsg.CustomMsgLuckGift;
import com.bogokj.live.model.custommsg.MsgModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.tencent.imsdk.TIMCallBack;

/**
 * @author kn update
 * @description: 直播回放页面 main
 * @time 2020/1/3
 */
public class LivePlaybackFragment extends LivePlayFragment {
    private RoomPlayControlView mRoomPlayControlView;
    private FrameLayout fl_live_play_control;

    private long mSeekValue;
    private boolean mHasVideoControl;

    private String user_id;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_playback_new;
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);
        fl_live_play_control = (FrameLayout) view.findViewById(R.id.fl_live_play_control);
        addLivePlayControl();
    }

    private void saveRoomData() {
        int roomId = getArguments().getInt(EXTRA_ROOM_ID, 0);
        if (roomId <= 0) {
            SDToast.showToast("房间id为空");
            getActivity().finish();
            return;
        } else {
            LiveInformation.getInstance().setRoomId(roomId);
        }
    }


    @Override
    protected void init() {
        super.init();
        //存储直播间id
        saveRoomData();
        user_id = getArguments().getString("user_id");

        setVideoView((LiveVideoView) findViewById(R.id.view_video));
        requestRoomInfo();
        getGuardInfo();
    }


    public void getGuardInfo() {

        CommonInterface.requestGuardTableList(user_id, new AppRequestCallback<GuardTableListModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    //更新守护数目
                    mRoomInfoView.updateGuard(actModel.getList().size());
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    private void addLivePlayControl() {
        mRoomPlayControlView = new RoomPlayControlView(getActivity());
        mRoomPlayControlView.setClickListener(controlClickListener);
        mRoomPlayControlView.setOnSeekBarChangeListener(controlSeekBarListener);
        replaceView(fl_live_play_control, mRoomPlayControlView);
        SDViewUtil.setGone(mRoomPlayControlView);

        updatePlayButton();
        updateDuration(0, 0);
    }

    @Override
    protected void onShowSendGiftView() {
        if (mHasVideoControl) {
            SDViewUtil.setInvisible(mRoomPlayControlView);
        }
        super.onShowSendGiftView();
    }

    @Override
    protected void onHideSendGiftView() {
        if (mHasVideoControl) {
            SDViewUtil.setVisible(mRoomPlayControlView);
        }
        super.onHideSendGiftView();
    }

    @Override
    protected void initIM() {
        super.initIM();

        final String groupId = getGroupId();
        getViewerIM().joinGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess() {
                sendViewerJoinMsg();
            }
        });
    }

    @Override
    protected void destroyIM() {
        super.destroyIM();
        getViewerIM().destroyIM();
    }

    @Override
    protected void requestRoomInfo() {
        getViewerBusiness().requestRoomInfo(mStrPrivateKey);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);
        dealRequestRoomInfoSuccess(actModel);
    }

    private void dealRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        Log.e("dealRequestRoomInfo", "加载出数据来了");
        mHasVideoControl = actModel.getHas_video_control() == 1;
        if (mHasVideoControl) {
            SDViewUtil.setVisible(mRoomPlayControlView);
        } else {
            SDViewUtil.setGone(mRoomPlayControlView);
        }

        switchVideoViewMode();

        if (actModel.getIs_del_vod() == 1) {
            SDToast.showToast("视频已删除");
            getViewerBusiness().exitRoom(true);
            return;
        } else {
            initIM();
            String playUrl = actModel.getPlay_url();
            if (!TextUtils.isEmpty(playUrl)) {
                prePlay(playUrl);
            }
        }
    }

    @Override
    public void onBsRequestRoomInfoError(App_get_videoActModel actModel) {
        LogUtil.i("onLiveRequestRoomInfoError");
        if (!actModel.canJoinRoom()) {
            super.onBsRequestRoomInfoError(actModel);
            return;
        }
        super.onBsRequestRoomInfoError(actModel);
        getViewerBusiness().exitRoom(true);
    }

    private void switchVideoViewMode() {
        if (getVideoView() == null) {
            return;
        }
        if (getLiveBusiness().isPCCreate()) {
            getPlayer().setRenderModeAdjustResolution();
        } else {
            getPlayer().setRenderModeFill();
        }
    }

    @Override
    public void onBsViewerStartJoinRoom() {
        super.onBsViewerStartJoinRoom();
        if (getRoomInfo() == null) {
            return;
        }

        String playUrl = getRoomInfo().getPlay_url();
        Log.e("dealRequestRoomInfo", playUrl);
        if (TextUtils.isEmpty(playUrl)) {
            requestRoomInfo();
        } else {
            dealRequestRoomInfoSuccess(getRoomInfo());
        }
    }

    private void prePlay(String url) {
        if (getPlayer().setVodUrl(url)) {
            clickPlayVideo();
        } else {
            SDToast.showToast("播放地址不合法");
        }
    }


    private boolean seekPlayerIfNeed() {
        if (mSeekValue > 0 && mSeekValue < getPlayer().getTotalDuration()) {
            getPlayer().seek(mSeekValue);
            mSeekValue = 0;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPlayBegin() {
        super.onPlayBegin();
        updatePlayButton();
    }

    @Override
    public void onPlayProgress(long total, long progress) {
        super.onPlayProgress(total, progress);
        if (seekPlayerIfNeed()) {
            return;
        }

        mRoomPlayControlView.setMax((int) total);
        mRoomPlayControlView.setProgress((int) progress);
    }

    //幸运礼物
    @Override
    public void OnMsgLuckGift(CustomMsgLuckGift msg) {
//        mRoomInfoView.addLuckGift(msg);
//        //给用户的中奖提示
//        if(msg.getSender().getUser_id().equals(UserModelDao.getUserId())){
//            SDToast.showToast("恭喜您中奖"+msg.getUser_multiple()+"倍！");
//
//        }
    }


    @Override
    public void onPlayEnd() {
        super.onPlayEnd();
        Log.e("onPlay", "End   videoPlayEnd");
        getPlayer().pause();
        updateDuration(getPlayer().getTotalDuration(), getPlayer().getProgressDuration());
        updatePlayButton();
    }

    private SeekBar.OnSeekBarChangeListener controlSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        private boolean needResume = false;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (getPlayer().getTotalDuration() > 0) {
                mSeekValue = seekBar.getProgress();
                if (needResume) {
                    needResume = false;
                    getPlayer().resume();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (getPlayer().getTotalDuration() > 0) {
                if (getPlayer().isPlaying()) {
                    getPlayer().pause();
                    needResume = true;
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (getPlayer().getTotalDuration() > 0) {
                updateDuration(seekBar.getMax(), progress);
            }
        }
    };

    private RoomPlayControlView.ClickListener controlClickListener = new RoomPlayControlView.ClickListener() {
        @Override
        public void onClickPlayVideo(View v) {
            clickPlayVideo();
        }
    };

    protected void clickPlayVideo() {
        getPlayer().performPlayPause();
        updatePlayButton();

        setPauseMode(!isPlaying());
    }

    private void updatePlayButton() {
        if (isPlaying()) {
            mRoomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_pause_video);
        } else {
            mRoomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_play_video);
        }
    }

    private void updateDuration(long total, long progress) {
        Log.e("onPlay", "Ing" + total + "=" + progress);

        StringBuilder sb = new StringBuilder();
        sb.append(SDDateUtil.formatDuring2mmss(progress)).append("/").append(SDDateUtil.formatDuring2mmss(total));

        mRoomPlayControlView.setTextDuration(sb.toString());
    }

//    @Override
//    protected void initSDVerticalScollView(FSwipeMenu scollView)
//    {
////        super.initSDVerticalScollView(scollView);
////        scollView.setEnableVerticalScroll(false);
//    }

    @Override
    protected void onClickCloseRoom(View v) {
        getViewerBusiness().exitRoom(true);
    }


    @Override
    public void onBsViewerExitRoom(boolean needFinishActivity) {
        super.onBsViewerExitRoom(needFinishActivity);

        destroyIM();
        getActivity().finish();
    }

    public void exitRoom() {
        getViewerBusiness().exitRoom(true);
    }


    @Override
    public void onMsgGift(CustomMsgGift msg) {
        super.onMsgGift(msg);
        mRoomGiftPlayView.addMsg(msg);

    }

    @Override
    public void onMsgLiveChat(MsgModel msg) {
        super.onMsgLiveChat(msg);
        if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_LIGHT) {
            CustomMsgLight msgLight = msg.getCustomMsgLight();
            if (msgLight.getShowMsg() == 0) {
                return;
            }
        }
        mRoomMsgView.addModel(msg);

    }


}
