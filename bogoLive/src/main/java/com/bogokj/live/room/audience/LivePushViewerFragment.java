package com.bogokj.live.room.audience;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bogokj.hybrid.event.EUnLogin;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.libgame.dice.view.DiceGameView;
import com.bogokj.libgame.poker.view.PokerGameView;
import com.bogokj.library.model.SDDelayRunnable;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.dialog.SDDialogNew;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.room.activity.LiveAudienceActivity;
import com.bogokj.live.appview.LiveLinkMicGroupView;
import com.bogokj.live.appview.LiveLinkMicView;
import com.bogokj.live.appview.LivePKViewerContentView;
import com.bogokj.live.appview.LiveVideoView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.control.LivePlayerSDK;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.event.EImOnForceOffline;
import com.bogokj.live.event.EOnBackground;
import com.bogokj.live.event.EOnCallStateChanged;
import com.bogokj.live.event.EOnResumeFromBackground;
import com.bogokj.live.event.EPushViewerOnDestroy;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.GuardTableListModel;
import com.bogokj.live.model.LiveQualityData;
import com.bogokj.live.model.custommsg.CustomMsgEndPK;
import com.bogokj.live.model.custommsg.CustomMsgEndVideo;
import com.bogokj.live.model.custommsg.CustomMsgOpenGuardSuccess;
import com.bogokj.live.model.custommsg.CustomMsgStartPK;
import com.bogokj.live.model.custommsg.CustomMsgStopLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgStopLive;
import com.bogokj.live.model.custommsg.CustomMsgUpdateTicketPK;
import com.bogokj.live.model.custommsg.data.DataLinkMicInfoModel;
import com.bogokj.xianrou.util.Event;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.sunday.eventbus.SDEventManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.rtmp.TXLivePlayer;

/**
 * @author kn update
 * @description: 观众界面 入口main
 * @time 2020/2/16
 */
public class LivePushViewerFragment extends LiveLayoutViewerExtendFragment implements LivePlayerSDK.TPlayCallback, LivePKViewerContentView.PKViewCallback {
    private LiveVideoView mPlayView;
    private LiveLinkMicGroupView mLinkMicGroupView;
    private LivePKViewerContentView livePKView;

    //pk背景
    private ImageView iv_pk_bg;

    /**
     * 是否正在播放主播的加速拉流地址
     */
    private boolean mIsPlayACC = false;
    private String mAccUrl;
    private FrameLayout frame_layout_pk_touch;
    private RelativeLayout rl_pk_touch_layout;
    private int intentRoomId;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_push_viewer;
    }

    @Override
    protected void init() {
        super.init();

        Log.e("dealRequestRoomInfo", "init");

        //存储房间信息
        saveRoomData();

        mPlayView = (LiveVideoView) findViewById(R.id.view_video);
        mPlayView.setPlayCallback(this);
        livePKView = (LivePKViewerContentView) findViewById(R.id.view_live_pk_content);
        iv_pk_bg = (ImageView) findViewById(R.id.iv_pk_bg);
        frame_layout_pk_touch = (FrameLayout) findViewById(R.id.frame_layout_pk_touch);
        rl_pk_touch_layout = (RelativeLayout) findViewById(R.id.rl_pk_touch_layout);
        livePKView.setCreateId(getCreaterId());
        livePKView.setFrame_layout_pk_touch(frame_layout_pk_touch);

        livePKView.setRl_pk_touch_layout(rl_pk_touch_layout);
        livePKView.setPkViewCallback(this);

        initLinkMicGroupView();

        //请求房间数据
        if (validateParams(getRoomId(), getGroupId(), getCreaterId())) {
            if ((LiveAudienceActivity.room_id == intentRoomId)) {
                requestRoomInfo();
            }
        }
    }


    private void saveRoomData() {
        intentRoomId = getArguments().getInt(EXTRA_ROOM_ID, 0);
        String groupId = getArguments().getString(EXTRA_GROUP_ID);
        String createrId = getArguments().getString(EXTRA_CREATER_ID);

        if ((LiveAudienceActivity.room_id == intentRoomId)) {
            LiveInformation.getInstance().setGroupIdtoNull();
            LiveInformation.getInstance().setCreaterIdtoNull();

            LiveInformation.getInstance().setRoomId(intentRoomId);
            LiveInformation.getInstance().setGroupId(groupId);
            LiveInformation.getInstance().setCreaterId(createrId);
        }
    }


    /**
     * 加入房间runnable
     */
    private SDDelayRunnable mJoinRoomRunnable = new SDDelayRunnable() {

        @Override
        public void run() {
            if (LiveAudienceActivity.room_id == intentRoomId) {
                initIM();
                playUrlByRoomInfo();
            }

        }
    };


    public void onEventMainThread(Event.OnTouchLiveFinish event) {
        getViewerBusiness().exitRoom(true);
    }


    @Override
    public void onBsViewerExitRoom(boolean needFinishActivity) {
        super.onBsViewerExitRoom(needFinishActivity);

        Log.e("onBsViewerExitRoom", getGroupId() + "==" + getCreaterId());
        mIsPlayACC = false;
        mAccUrl = null;

        destroyIM();
        stopLinkMic(false, true);
        livePKView.stopPK();
        getPlayer().stopPlay();
        if (mIsNeedShowFinish) {
            addLiveFinish();
            return;
        }

        if (needFinishActivity) {
            getActivity().finish();
        }

    }


    /**
     * 初始化连麦view
     */
    private void initLinkMicGroupView() {
        mLinkMicGroupView = (LiveLinkMicGroupView) findViewById(R.id.view_link_mic_group);
        mLinkMicGroupView.mCallBack.set(new LiveLinkMicGroupView.LiveLinkMicGroupViewCallback() {
            @Override
            public void onPlayDisconnect(String userId) {
            }

            @Override
            public void onPlayRecvFirstFrame(String userId) {
            }

            @Override
            public void onClickView(LiveLinkMicView view) {
            }

            @Override
            public void onPushStart(LiveLinkMicView view) {
                if (!TextUtils.isEmpty(mAccUrl)) {
                    if (!mIsPlayACC) {
                        getViewerBusiness().requestMixStream(UserModelDao.getUserId());
                        getPlayer().stopPlay();
                        getPlayer().setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
                        getPlayer().setUrl(mAccUrl);
                        getPlayer().startPlay();
                        mIsPlayACC = true;
                        LogUtil.i("play acc:" + mAccUrl);
                    }
                } else {
                    LogUtil.e("大主播acc流地址为空");
                }
            }
        });
    }

    public LivePlayerSDK getPlayer() {
        return mPlayView.getPlayer();
    }

    @Override
    public LiveQualityData onBsGetLiveQualityData() {
        return getPlayer().getLiveQualityData();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        if (intent != null) {
//            int oldRoomId = getRoomId();
//            newRoomId = intent.getIntExtra(EXTRA_ROOM_ID, 0);
//            if (newRoomId != oldRoomId) {
//                setIntent(intent);
//                getViewerBusiness().exitRoom(false);
//                init(null);
//            } else {
//                SDToast.showToast("已经在直播间中");
//            }
//        }
//        super.onNewIntent(intent);
//    }


    protected boolean validateParams(int roomId, String groupId, String createrId) {
        if (roomId <= 0) {
            SDToast.showToast("房间id为空");
            getActivity().finish();
            return false;
        }

        if (TextUtils.isEmpty(groupId)) {
            SDToast.showToast("聊天室id为空");
            getActivity().finish();
            return false;
        }

        if (TextUtils.isEmpty(createrId)) {
            SDToast.showToast("主播id为空");
            getActivity().finish();
            return false;
        }

        return true;
    }

    @Override
    protected void initIM() {
        super.initIM();

        final String groupId = getGroupId();
        getViewerIM().joinGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                onErrorJoinGroup(code, desc);
            }

            @Override
            public void onSuccess() {
                onSuccessJoinGroup(groupId);
            }
        });
    }

    /**
     * 加入聊天组失败回调
     *
     * @param code
     * @param desc
     */
    public void onErrorJoinGroup(int code, String desc) {
        AppDialogConfirm dialog = new AppDialogConfirm(getActivity());
        dialog.setTextContent("加入聊天组失败:" + code + "，是否重试").setTextCancel("退出").setTextConfirm("重试");
        dialog.setCancelable(false);
        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                getViewerBusiness().exitRoom(true);
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                initIM();
            }
        }).show();
    }

    @Override
    protected void onSuccessJoinGroup(String groupId) {
        super.onSuccessJoinGroup(groupId);
        sendViewerJoinMsg();
    }


//    //全站广播
//    @Override
//    public void onMsgAllStation(CustomAllStationMsg msg) {
//        if (msg.getSender().getUser_id().equals(UserModelDao.getUserId())) {
//            UserModelDao.query().setNoble_gift(1);
//        }
//
//        mRoomInfoView.statStationMessage(msg);
//    }

    @Override
    public void onMsgDataLinkMicInfo(DataLinkMicInfoModel model) {
        super.onMsgDataLinkMicInfo(model);

        boolean isLocalUserLinkMic = model.isLocalUserLinkMic();
        if (isLocalUserLinkMic) {
            if (getViewerBusiness().isInLinkMic()) {
                mAccUrl = model.getPlay_rtmp_acc();
                mLinkMicGroupView.setLinkMicInfo(model);
            }
        } else {
            stopLinkMic(true, false);
        }
    }

    @Override
    public void onMsgStopLinkMic(CustomMsgStopLinkMic msg) {
        super.onMsgStopLinkMic(msg);
        stopLinkMic(true, false);
        SDToast.showToast("主播关闭了连麦");
    }

    @Override
    protected void onClickStopLinkMic() {
        super.onClickStopLinkMic();
        stopLinkMic(true, true);
    }


//    //观众进入
//    @Override
//    public void onMsgViewerJoin(CustomMsgViewerJoin msg) {
//        //判断是不是贵族
//        if (msg.getSender().getNoble_vip_type() == 1) {
//            mRoomInfoView.getNobleInfo();
//        }
//    }
//
//    //观众退出
//    @Override
//    public void onMsgViewerQuit(CustomMsgViewerQuit msg) {
//
//        if (msg.getSender().getNoble_vip_type() == 1) {
//            mRoomInfoView.getNobleInfo();
//        }
//    }

    //开通守护成功
    @Override
    public void OnMsgOpenGuardSuccess(CustomMsgOpenGuardSuccess msg) {
        //mRoomInfoView.starGuardMessage(msg);
        //获取最新守护数目
        CommonInterface.requestGuardTableList(getCreaterId(), new AppRequestCallback<GuardTableListModel>() {
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


    @Override
    public void onMsgStartPK(CustomMsgStartPK customMsgStartPK) {
        super.onMsgStartPK(customMsgStartPK);
        SDToast.showToast("主播开始了PK模式");
        getViewerBusiness().setInPK(true);
        livePKView.requestGetPkInfo(customMsgStartPK.getPk_id());
    }


    @Override
    public void onBsViewerEndPK(CustomMsgEndPK msg) {

        if (msg.getWin_user_id().equals(getCreaterId())) {
            if (!getViewerBusiness().ismIsInPunish()) {
                SDToast.showToast("恭喜主播获得本轮PK的胜利！");
            }
        }

        if (null == msg.getStatus()) {
            livePKView.stopPK();
        }

    }


    @Override
    public void onMsgUpdateTicketPK(CustomMsgUpdateTicketPK customMsgUpdateTicketPK) {
        //判断当前状态是否在惩罚状态
        if (!getViewerBusiness().ismIsInPunish()) {
            livePKView.updateProgress(customMsgUpdateTicketPK.getEmcee_user_id1(), getCreaterId(), customMsgUpdateTicketPK.getPk_ticket1(), customMsgUpdateTicketPK.getPk_ticket2());
        }
    }

    /**
     * 暂停连麦
     */
    private void pauseLinkMic() {
        if (getViewerBusiness().isInLinkMic()) {
            mLinkMicGroupView.onPause();
        }
    }

    /**
     * 恢复连麦
     */
    private void resumeLinkMic() {
        if (getViewerBusiness().isInLinkMic()) {
            mLinkMicGroupView.onResume();
        }
    }

    /**
     * 停止连麦
     *
     * @param needPlayOriginal 停止连麦后是否需要拉连麦之前的主播视频流
     * @param sendStopMsg      是否发送结束连麦的消息
     */
    private void stopLinkMic(boolean needPlayOriginal, boolean sendStopMsg) {
        if (getViewerBusiness().isInLinkMic()) {
            getViewerBusiness().stopLinkMic(sendStopMsg);

            mLinkMicGroupView.resetAllView();
            if (needPlayOriginal) {
                if (mIsPlayACC) {
                    playUrlByRoomInfo();
                    mIsPlayACC = false;
                }
            }
        }
    }

    @Override
    public void onMsgEndVideo(CustomMsgEndVideo msg) {
        super.onMsgEndVideo(msg);
        getViewerBusiness().exitRoom(false);
    }

    @Override
    public void onMsgStopLive(CustomMsgStopLive msg) {
        super.onMsgStopLive(msg);
//        if(msg.getUid().equals(UserModelDao.getUserId())){
        SDToast.showToast(msg.getDesc());
        getViewerBusiness().exitRoom(true);
        // }


    }


    @Override
    protected void destroyIM() {
        super.destroyIM();
        getViewerIM().destroyIM();
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        int rId = actModel.getRoom_id();
        String gId = actModel.getGroup_id();
        String cId = actModel.getUser_id();

        if (!validateParams(rId, gId, cId)) {
            return;
        }

        super.onBsRequestRoomInfoSuccess(actModel);
        switchVideoViewMode();
        getViewerBusiness().startJoinRoom();

        if (actModel.getPk_id() > 0) {
            livePKView.requestGetPkInfo(String.valueOf(actModel.getPk_id()));
        }
    }

    private void switchVideoViewMode() {
        if (mPlayView == null) {
            return;
        }
        if (getLiveBusiness().isPCCreate()) {
            getPlayer().setRenderModeAdjustResolution();
        } else {
            getPlayer().setRenderModeFill();
        }
    }

    @Override
    public void onBsRequestRoomInfoError(App_get_videoActModel actModel) {
        if (!actModel.canJoinRoom()) {
            super.onBsRequestRoomInfoError(actModel);
            return;
        }
        super.onBsRequestRoomInfoError(actModel);
        if (actModel.isVideoStoped()) {
            addLiveFinish();
        } else {
            getViewerBusiness().exitRoom(true);
        }
    }

    @Override
    public void onBsRequestRoomInfoException(String msg) {
        super.onBsRequestRoomInfoException(msg);

        SDDialogNew dialog = new SDDialogNew(getActivity());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTextContent("请求直播间信息失败")
                .setTextCancel("退出").setTextConfirm("")
                .setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {
                        getViewerBusiness().exitRoom(true);
                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog) {
                        requestRoomInfo();
                    }
                }).show();
    }


    @Override
    public void onBsViewerStartJoinRoom() {
        super.onBsViewerStartJoinRoom();
        if (getRoomInfo() == null) {
            return;
        }
        String playUrl = getRoomInfo().getPlay_url();
        if (TextUtils.isEmpty(playUrl)) {
            requestRoomInfo();
        } else {
            startJoinRoomRunnable();
        }
    }

    private void startJoinRoomRunnable() {
        mJoinRoomRunnable.run();
    }


    /**
     * 根据接口返回的拉流地址开始拉流
     */
    protected void playUrlByRoomInfo() {
        if (getRoomInfo() == null) {
            return;
        }
        String url = getRoomInfo().getPlay_url();
        if (validatePlayUrl(url)) {
            getPlayer().stopPlay();
            getPlayer().setUrl(url);
            getPlayer().startPlay();
            LogUtil.i("play normal:" + url);
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {

    }

    @Override
    public void onPlayBegin() {

    }

    @Override
    public void onPlayRecvFirstFrame() {
        hideLoadingVideo();
    }

    @Override
    public void onPlayProgress(long total, long progress) {

    }

    @Override
    public void onPlayEnd() {

    }

    @Override
    public void onPlayLoading() {

    }

    protected boolean validatePlayUrl(String playUrl) {
        if (TextUtils.isEmpty(playUrl)) {
            SDToast.showToast("未找到直播地址");
            return false;
        }

        if (playUrl.startsWith("rtmp://")) {
            getPlayer().setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
            getPlayer().setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_FLV);
        } else {
            SDToast.showToast("播放地址不合法");
            return false;
        }

        return true;
    }


    @Override
    protected void onClickCloseRoom(View v) {
        getViewerBusiness().exitRoom(true);
    }


    public void onEventMainThread(EUnLogin event) {
        getViewerBusiness().exitRoom(true);
    }

    public void onEventMainThread(EImOnForceOffline event) {
        getViewerBusiness().exitRoom(true);
    }

    public void onEventMainThread(EOnCallStateChanged event) {
        switch (event.state) {
            case TelephonyManager.CALL_STATE_RINGING:
                sdkEnableAudio(false);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                sdkEnableAudio(false);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                sdkEnableAudio(true);
                break;
            default:
                break;
        }
    }


    @Override
    protected void sdkEnableAudio(boolean enable) {
        getPlayer().setMute(!enable);
    }

    @Override
    protected void sdkPauseVideo() {
        super.sdkPauseVideo();
        getPlayer().stopPlay();
    }

    @Override
    protected void sdkResumeVideo() {
        super.sdkResumeVideo();
        getPlayer().startPlay();
    }

    @Override
    protected void sdkStopLinkMic() {
        super.sdkStopLinkMic();
        stopLinkMic(false, true);
    }

    @Override
    public void onStop() {
        super.onStop();
        pauseLinkMic();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeLinkMic();
    }

    public void onEventMainThread(EOnBackground event) {
        getPlayer().stopPlay();
    }

    public void onEventMainThread(EOnResumeFromBackground event) {
        getPlayer().startPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mJoinRoomRunnable.removeDelay();
        getPlayer().onDestroy();
        mLinkMicGroupView.onDestroy();
        SDEventManager.post(new EPushViewerOnDestroy());
    }

    @Override
    public void onPKViewCallStartPk() {
        iv_pk_bg.setVisibility(View.VISIBLE);
        mPlayView.changePKView(true);
        //设置状态
        getCreaterBusiness().setInPK(true);
        getCreaterBusiness().setmIsInPunish(false);
    }

    @Override
    public void onPKViewCallPunishTime() {
        iv_pk_bg.setVisibility(View.VISIBLE);
        mPlayView.changePKView(true);
        getCreaterBusiness().setInPK(true);
        getCreaterBusiness().setmIsInPunish(true);
    }

    @Override
    public void onPKViewCallStopPk() {
        iv_pk_bg.setVisibility(View.GONE);
        mPlayView.changePKView(false);
        getCreaterBusiness().setInPK(false);
        getCreaterBusiness().setmIsInPunish(false);
    }


    @Override
    public void onSwitchRoom(int hostRoomId2) {
        switchRoom(hostRoomId2);
    }


    /**
     * 视频页面滑动切换
     *
     * @param event
     */

    public void onEventMainThread(Event.OnTouchLivePlayerPageChange event) {
        Log.e("dealRequestRoomInfo", LiveAudienceActivity.room_id +"="+intentRoomId);
        if ((isVisible() && LiveAudienceActivity.room_id == intentRoomId)) {
            saveRoomData();
            initLinkMicGroupView();
            requestRoomInfo();
        } else {
            onBsViewerExitRoom(false);
        }
    }


    @Override
    public void showPokerGameView(PokerGameView mPokerGameView) {
        if (isVisible() && LiveAudienceActivity.room_id == intentRoomId) {
            replaceBottomExtend(mPokerGameView);
        }

    }

    @Override
    public void showDiceGameView(DiceGameView mDiceGameView) {
        if (isVisible() && LiveAudienceActivity.room_id == intentRoomId) {
            replaceBottomExtend(mDiceGameView);
        }
    }


    @Override
    public void onMsgGift(CustomMsgGift msg) {
        super.onMsgGift(msg);
        if ((isVisible() && LiveAudienceActivity.room_id == intentRoomId)) {
            mRoomGiftPlayView.addMsg(msg);
        }

    }


    @Override
    public void onMsgLiveChat(MsgModel msg) {
        super.onMsgLiveChat(msg);
        if ((isVisible() && LiveAudienceActivity.room_id == intentRoomId)) {
            if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_LIGHT) {
                CustomMsgLight msgLight = msg.getCustomMsgLight();
                if (msgLight.getShowMsg() == 0) {
                    return;
                }
            }
            mRoomMsgView.addModel(msg);
        }

    }


}
