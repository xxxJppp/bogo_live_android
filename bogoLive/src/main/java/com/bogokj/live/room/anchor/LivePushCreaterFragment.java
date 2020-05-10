package com.bogokj.live.room.anchor;

import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bogokj.hybrid.event.EUnLogin;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.libgame.dice.view.DiceGameView;
import com.bogokj.libgame.poker.view.PokerGameView;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveLinkMicGroupView;
import com.bogokj.live.appview.LiveLinkMicView;
import com.bogokj.live.appview.LivePKContentView;
import com.bogokj.live.appview.room.ARoomMusicView;
import com.bogokj.live.appview.room.RoomPushMusicView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.control.IPushSDK;
import com.bogokj.live.control.LivePushSDK;
import com.bogokj.live.dialog.LiveCreaterReceiveApplyLinkMicDialog;
import com.bogokj.live.dialog.LiveCreaterReceiveApplyPKDialog;
import com.bogokj.live.dialog.LivePKEmceeListDialog;
import com.bogokj.live.dialog.LivePKOverDialog;
import com.bogokj.live.dialog.LiveSetBeautyDialog;
import com.bogokj.live.dialog.LiveSmallVideoInfoCreaterDialog;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.event.EImOnForceOffline;
import com.bogokj.live.event.EOnCallStateChanged;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.App_request_start_pkActModel;
import com.bogokj.live.model.CustomMsgCancelPK;
import com.bogokj.live.model.GuardTableListModel;
import com.bogokj.live.model.LiveQualityData;
import com.bogokj.live.model.custommsg.CustomMsgApplyLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgEndPK;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.model.custommsg.CustomMsgOpenGuardSuccess;
import com.bogokj.live.model.custommsg.CustomMsgRejectLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgRequestPK;
import com.bogokj.live.model.custommsg.CustomMsgStopLive;
import com.bogokj.live.model.custommsg.CustomMsgUpdateTicketPK;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.model.custommsg.data.DataLinkMicInfoModel;
import com.bogokj.live.room.activity.LiveAudienceActivity;
import com.bogokj.live.utils.PermissionUtil;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.rtmp.ui.TXCloudVideoView;

import cn.tillusory.tiui.TiPanelLayout;

/**
 * @author kn
 * @description: 主播界面 入口main
 * @time kn 2019/12/19
 */
public class LivePushCreaterFragment extends LiveLayoutCreaterExtendFragment implements LivePKContentView.PKViewCallback {

    private LiveLinkMicGroupView mLinkMicGroupView;
    private LivePKContentView livePKView;
    private boolean mIsCreaterLeaveByCall = false;
    private LiveCreaterReceiveApplyLinkMicDialog mDialogReceiveApplyLinkMic;
    private LiveCreaterReceiveApplyPKDialog mDialogReceiveApplyPK;

    private TXCloudVideoView liveVideoView;
    //pk背景
    private ImageView iv_pk_bg;

    private TiPanelLayout tiPanelLayout;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_push_creater;
    }

    @Override
    protected ARoomMusicView createRoomMusicView() {
        return new RoomPushMusicView(getActivity());
    }

    @Override
    protected void init() {
        super.init();
        PermissionUtil.isCameraCanUse();
        //存储直播间id
        saveRoomData();

        iv_pk_bg = (ImageView) findViewById(R.id.iv_pk_bg);
        livePKView = (LivePKContentView) findViewById(R.id.view_live_pk_content);
        mLinkMicGroupView = (LiveLinkMicGroupView) findViewById(R.id.view_link_mic_group);

        livePKView.setPkViewCallback(this);
        livePKView.setCreateId(getCreaterId());

        initPusher();
        initLinkMicGroupView();
        initLayout(getActivity().getWindow().getDecorView());
        //请求房间信息
        requestRoomInfo();
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
    protected IPushSDK getPushSDK() {
        return LivePushSDK.getInstance();
    }

    /**
     * 初始化推流对象
     */
    private void initPusher() {
        liveVideoView = (TXCloudVideoView) findViewById(R.id.view_video);
        getPushSDK().init(liveVideoView);

        if (AppRuntimeWorker.get_is_open_bogo_beauty() == 1) {
            //美颜
            tiPanelLayout = new TiPanelLayout(getContext()).init(getPushSDK().getTiSDKManager());
            getActivity().addContentView(tiPanelLayout,
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 初始化连麦view
     */
    private void initLinkMicGroupView() {

        mLinkMicGroupView.mCallBack.set(new LiveLinkMicGroupView.LiveLinkMicGroupViewCallback() {
            @Override
            public void onPlayDisconnect(String userId) {
                getCreaterBusiness().stopLinkMic(userId, false);
            }

            @Override
            public void onPlayRecvFirstFrame(String userId) {
                getCreaterBusiness().requestMixStream(userId);
            }

            @Override
            public void onClickView(final LiveLinkMicView view) {
                LiveSmallVideoInfoCreaterDialog dialog = new LiveSmallVideoInfoCreaterDialog(getActivity(), view.getUserId());
                dialog.setClickListener((v, userId) -> getCreaterBusiness().stopLinkMic(userId, true));
                dialog.show();
            }

            @Override
            public void onPushStart(LiveLinkMicView view) {

            }
        });
    }

//
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
//        //判断是不是贵族
//        if (msg.getSender().getNoble_vip_type() == 1) {
//            mRoomInfoView.getNobleInfo();
//        }
//    }


    //开通守护成功
    @Override
    public void OnMsgOpenGuardSuccess(CustomMsgOpenGuardSuccess msg) {
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
    protected void initIM() {
        super.initIM();
        if (isClosedBack()) {
            getGameClassUtils().getGameBusiness().requestGameInfo();
        } else {
            if (!TextUtils.isEmpty(getRoomInfo().getGroup_id())) {
                final String groupId = getRoomInfo().getGroup_id();
                getCreaterIM().joinGroup(groupId, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        dealGroupError(code, desc);
                    }

                    @Override
                    public void onSuccess() {
                        dealGroupSuccess(groupId);
                    }
                });
            } else {
                getCreaterIM().createGroup(String.valueOf(getRoomId()), new TIMValueCallBack<String>() {
                    @Override
                    public void onError(int code, String desc) {
                        dealGroupError(code, desc);
                    }

                    @Override
                    public void onSuccess(String groupId) {
                        dealGroupSuccess(groupId);
                    }
                });
            }
        }
    }

    /**
     * 加入或者创建聊天组成功处理
     *
     * @param groupId
     */
    private void dealGroupSuccess(String groupId) {
        LiveInformation.getInstance().setGroupId(groupId);
        requestUpdateLiveStateSuccess();
    }

    /**
     * 加入或者创建聊天组失败处理
     *
     * @param code
     * @param desc
     */
    protected void dealGroupError(int code, String desc) {
        AppDialogConfirm dialog = new AppDialogConfirm(getActivity());
        dialog.setTextContent("创建聊天组失败:" + code + "，请退出重试").setTextCancel(null).setTextConfirm("确定");
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                requestUpdateLiveStateFail();
                exitRoom(false);
            }
        });
        dialog.show();
    }

    //onBsViewerCancelPKRequest


    @Override
    public void onBsViewerCancelPKRequest(CustomMsgCancelPK msg) {
        super.onBsViewerCancelPKRequest(msg);
        if (mDialogReceiveApplyPK != null) {
            mDialogReceiveApplyPK.dismiss();
        }
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);
        if (isClosedBack()) {
            final String groupId = actModel.getGroup_id();
            requestUpdateLiveStateComeback();
            getCreaterIM().onSuccessJoinGroup(groupId);
            getCreaterIM().sendCreaterComebackMsg(null);
        }

        initIM();
        startPush(actModel.getPush_rtmp());
    }

    @Override
    public void onBsRequestRoomInfoError(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoError(actModel);
        exitRoom(false);
    }

    /**
     * 开始推流
     */
    protected void startPush(String url) {
        if (TextUtils.isEmpty(url)) {
            SDToast.showToast("推流地址为空");
            return;
        }
        if (!isClosedBack()) {
            addRoomCountDownView();
        }
        getPushSDK().setUrl(url);
        getPushSDK().startPush();

        getPushSDK().enableBeauty(true);
    }

    @Override
    protected void onClickPK() {
        super.onClickPK();
        //判断当前是否处于pk状态
        if (getCreaterBusiness().isInPK()) {
            LivePKOverDialog livePKOverDialog = new LivePKOverDialog(getActivity(), getCreaterBusiness());
            livePKOverDialog.showBottom();
        } else {
            //结束pk
            livePKView.requestEndPk();
            LivePKEmceeListDialog livePKEmceeListDialog = new LivePKEmceeListDialog(getActivity(), getCreaterBusiness());
            livePKEmceeListDialog.showBottom();
        }
    }


    @Override
    public LiveQualityData onBsGetLiveQualityData() {
        return getPushSDK().getLiveQualityData();
    }

    @Override
    public void onBsCreaterShowReceiveApplyLinkMic(final CustomMsgApplyLinkMic msg) {
        super.onBsCreaterShowReceiveApplyLinkMic(msg);
        mDialogReceiveApplyLinkMic = new LiveCreaterReceiveApplyLinkMicDialog(getActivity(), msg);
        mDialogReceiveApplyLinkMic.setClickListener(new LiveCreaterReceiveApplyLinkMicDialog.ClickListener() {
            @Override
            public void onClickAccept() {
                getCreaterBusiness().acceptLinkMic(msg.getSender().getUser_id());
            }

            @Override
            public void onClickReject() {
                getCreaterBusiness().rejectLinkMic(msg.getSender().getUser_id(), CustomMsgRejectLinkMic.MSG_REJECT);
            }
        });
        mDialogReceiveApplyLinkMic.show();
    }

    @Override
    public void onBsCreaterHideReceiveApplyLinkMic() {
        super.onBsCreaterHideReceiveApplyLinkMic();
        if (mDialogReceiveApplyLinkMic != null) {
            mDialogReceiveApplyLinkMic.dismiss();
        }
    }

    @Override
    public boolean onBsCreaterIsReceiveApplyLinkMicShow() {
        if (mDialogReceiveApplyLinkMic != null) {
            return mDialogReceiveApplyLinkMic.isShowing();
        } else {
            return false;
        }
    }

    @Override
    public boolean onBsCreaterIsReceivePkShow() {
        return false;
//        if (mDialogReceiveApplyPK != null) {
//            return mDialogReceiveApplyPK.isShowing();
//        } else {
//            return false;
//        }
    }

    @Override
    public void onBsCreaterShowReceivePK(final CustomMsgRequestPK msg) {
        super.onBsCreaterShowReceivePK(msg);
        if (null != mDialogReceiveApplyPK) {
            mDialogReceiveApplyPK.dismiss();
        } else {
            //  mDialogReceiveApplyPK.dismiss();
        }

        mDialogReceiveApplyPK = new LiveCreaterReceiveApplyPKDialog(getActivity(), msg);
        mDialogReceiveApplyPK.setClickListener(new LiveCreaterReceiveApplyLinkMicDialog.ClickListener() {
            @Override
            public void onClickAccept() {
                getCreaterBusiness().setInPK(true);
                getCreaterBusiness().setmIsInPunish(false);
                getCreaterBusiness().setmApplyPKUserId(msg.getSender().getUser_id());
                acceptRequestPK(msg.getSender().getUser_id(), msg.getPkid());
            }

            @Override
            public void onClickReject() {
                getCreaterBusiness().setInPK(false);
                getCreaterBusiness().rejectPK(msg.getSender().getUser_id(), CustomMsgRequestPK.MSG_REJECT);
            }
        });
        mDialogReceiveApplyPK.show();
    }


    @Override
    public void onBsViewerApplyPKRejected(String msg) {
        super.onBsViewerApplyPKRejected(msg);
    }

    //对方接受了PK请求
    @Override
    public void onBsViewerApplyPKAccept(String pkId) {
        super.onBsViewerApplyPKAccept(pkId);

        getCreaterBusiness().setInPK(true);
        mDialogApplyPK.dismiss();
        livePKView.requestGetPkInfo(pkId);

    }

    @Override
    public void onBsViewerEndPK(CustomMsgEndPK msg) {

        //修改
        if (null == msg.getStatus()) {
            livePKView.stopPK();
            return;
        }

        if (msg.getStatus().equals("1")) {
            getCreaterBusiness().setInPK(true);
            getCreaterBusiness().setmIsInPunish(true);
        }

    }

    @Override
    public void onBsViewerClickSound(String url) {
        getPushSDK().playBGM(url);
    }

    //接受PK请求
    private void acceptRequestPK(final String user_id, String pkid) {

        showProgressDialog("正在接受...");
        CommonInterface.requestAcceptRequestPK(user_id, pkid, new AppRequestCallback<App_request_start_pkActModel>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {

                dismissProgressDialog();
                if (actModel.getStatus() == 0) {
                    getCreaterBusiness().setInPK(false);
                    getCreaterBusiness().setmIsInPunish(false);
                    return;
                }

                if (actModel.isOk()) {
                    getCreaterBusiness().acceptPK(user_id, actModel.getPk_id());
                    livePKView.requestGetPkInfo(actModel.getPk_id());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                getCreaterBusiness().setInPK(false);
                getCreaterBusiness().setmIsInPunish(false);
                dismissProgressDialog();
            }
        });
    }


    @Override
    public void onMsgUpdateTicketPK(CustomMsgUpdateTicketPK customMsgUpdateTicketPK) {
        //判断当前状态是否在惩罚状态
        if (!getCreaterBusiness().ismIsInPunish()) {
            livePKView.updateProgress(customMsgUpdateTicketPK.getEmcee_user_id1(), getCreaterId(), customMsgUpdateTicketPK.getPk_ticket1(), customMsgUpdateTicketPK.getPk_ticket2());
        }
    }

    @Override
    public void onMsgDataLinkMicInfo(DataLinkMicInfoModel model) {
        super.onMsgDataLinkMicInfo(model);
        boolean hasLinkMicItem = model.hasLinkMicItem();
        boolean needUpdateConfig = false;

        if (hasLinkMicItem) {
            if (!getCreaterBusiness().isInLinkMic()) {
                needUpdateConfig = true;
            }
            mLinkMicGroupView.setLinkMicInfo(model);
            getCreaterBusiness().setLinkMicCount(model.getLinkMicCount());
        } else {
            if (getCreaterBusiness().isInLinkMic()) {
                needUpdateConfig = true;
                mLinkMicGroupView.resetAllView();
            }
        }
        getCreaterBusiness().setInLinkMic(hasLinkMicItem);

        if (needUpdateConfig) {
            if (getCreaterBusiness().isInLinkMic()) {
                getPushSDK().setConfigLinkMicMain();
            } else {
                getPushSDK().setConfigDefault();
            }
        }
    }

    @Override
    public void onMsgStopLive(CustomMsgStopLive msg) {
        super.onMsgStopLive(msg);
        exitRoom(true);
    }


//    //全站广播
//    @Override
//    public void onMsgAllStation(CustomAllStationMsg msg) {
//        mRoomInfoView.statStationMessage(msg);
//    }


    /**
     * 退出房间
     *
     * @param addLiveFinish true-显示直播结束界面；false-关闭当前Activity
     */
    public void exitRoom(boolean addLiveFinish) {
        getCreaterBusiness().stopMonitor();
        removeRoomCountDownView();
        getPushSDK().stopPush();
        mLinkMicGroupView.onDestroy();
        livePKView.stopPK();
        stopMusic();
        if (addLiveFinish) {
            addLiveFinish();
        } else {
            getActivity().finish();
        }
    }

    @Override
    protected void addLiveFinish() {
        super.addLiveFinish();
        getCreaterBusiness().requestEndVideo();
    }

    /**
     * 主播离开
     */
    public void createrLeave() {
        if (!mIsCreaterLeave) {
            mIsCreaterLeave = true;
            requestUpdateLiveStateLeave();
            getPushSDK().pausePush();
            if (getPushSDK().getTiSDKManager() != null) {
                getPushSDK().getTiSDKManager().destroy();
            }
            getCreaterIM().sendCreaterLeaveMsg(null);
            if (getRoomMusicView() != null) {
                getRoomMusicView().onStopLifeCircle();
            }

            if (mLinkMicGroupView != null) {
                mLinkMicGroupView.onPause();
            }
            if (livePKView != null) {
                livePKView.getViewPKView().onPause();
            }

        }
    }

    /**
     * 主播回来
     */
    private void createrComeback() {
        if (mIsCreaterLeave) {
            mIsCreaterLeave = false;
            requestUpdateLiveStateComeback();
            getPushSDK().resumePush();
            getCreaterIM().sendCreaterComebackMsg(null);
            if (getRoomMusicView() != null) {
                getRoomMusicView().onResumeLifeCircle();
            }

            mLinkMicGroupView.onResume();
            livePKView.getViewPKView().onResume();
        }
    }


    @Override
    protected void onClickCloseRoom(View v) {
        super.onClickCloseRoom(v);
        if (isAuctioning()) {
            showActionExitDialog();
        } else {
            showNormalExitDialog();
        }
    }

    public void showActionExitDialog() {
        AppDialogConfirm dialog = new AppDialogConfirm(getActivity());
        dialog.setTextContent("您发起的竞拍暂未结束，不能关闭直播");
        dialog.setTextConfirm(null);
        dialog.show();
    }

    public void showNormalExitDialog() {
        AppDialogConfirm dialog = new AppDialogConfirm(getActivity());
        dialog.setTextContent("确定要结束直播吗？")
                .setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog) {
                        exitRoom(true);
                    }
                }).show();
    }

    private LiveSetBeautyDialog mBeautyDialog;

    @Override
    protected void showSetBeautyDialog() {

        if (AppRuntimeWorker.get_is_open_bogo_beauty() == 1) {
            //美颜
            tiPanelLayout.changeView();
        } else {
            if (mBeautyDialog == null) {
                mBeautyDialog = new LiveSetBeautyDialog(getActivity());
            }
            mBeautyDialog.showBottom();
        }
    }

    //----------EventBus start----------

    @Override
    public void onEventMainThread(EUnLogin event) {
        exitRoom(false);
    }

    @Override
    public void onEventMainThread(EImOnForceOffline event) {
        exitRoom(true);
    }

    @Override
    public void onEventMainThread(EOnCallStateChanged event) {
        switch (event.state) {
            case TelephonyManager.CALL_STATE_RINGING:
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (mIsCreaterLeave) {
                    mIsCreaterLeaveByCall = false;
                } else {
                    mIsCreaterLeaveByCall = true;
                }
                createrLeave();
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (mIsCreaterLeaveByCall) {
                    createrComeback();
                    mIsCreaterLeaveByCall = false;
                }
                break;
            default:
                break;
        }
    }

    //----------EventBus end----------

    //----------activity lifecycle start----------

    @Override
    public void onResume() {
        super.onResume();
        createrComeback();
    }


    @Override
    public void onDestroy() {
        getPushSDK().onDestroy();
        if (mLinkMicGroupView != null) {
            mLinkMicGroupView.onDestroy();
        }
        if (livePKView != null) {
            livePKView.getViewPKView().onDestroy();
        }

        super.onDestroy();
    }

    //----------activity lifecycle end----------

    @Override
    public void onPKViewCallStartPk() {
        iv_pk_bg.setVisibility(View.VISIBLE);
        getPushSDK().setVideoPKModel();

        int width = SDViewUtil.getScreenWidth() / 2;

        RelativeLayout.LayoutParams videoLeftParams = new RelativeLayout.LayoutParams(width, width / 2 * 3);
        videoLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        videoLeftParams.setMargins(0, SDViewUtil.dp2px(100), 0, 0);
        liveVideoView.setLayoutParams(videoLeftParams);

        //设置状态
        getCreaterBusiness().setInPK(true);
        getCreaterBusiness().setmIsInPunish(false);
    }

    @Override
    public void onPKViewCallPunishTime() {
        iv_pk_bg.setVisibility(View.VISIBLE);
        getCreaterBusiness().setInPK(true);
        getCreaterBusiness().setmIsInPunish(true);
    }

    @Override
    public void onPKViewCallStopPk() {
        //设置状态
        getCreaterBusiness().setInPK(false);
        getCreaterBusiness().setmIsInPunish(false);

        //推流SDK内部PK结束操作
        getPushSDK().setVideoEndPKModel();

        RelativeLayout.LayoutParams videoLeftParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        videoLeftParams.setMargins(0, 0, 0, 0);
        liveVideoView.setLayoutParams(videoLeftParams);
    }


    @Override
    public void showPokerGameView(PokerGameView mPokerGameView) {

        replaceBottomExtend(mPokerGameView);


    }

    @Override
    public void showDiceGameView(DiceGameView mDiceGameView) {
        replaceBottomExtend(mDiceGameView);

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
