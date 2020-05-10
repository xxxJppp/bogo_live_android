package com.bogokj.live.business;

import android.util.Log;

import com.bogokj.games.model.App_plugin_initActModel;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.live.model.App_requestCancelPKRequest;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.looper.ISDLooper;
import com.fanwe.lib.looper.impl.SDSimpleLooper;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.IMHelper;
import com.bogokj.live.room.ILiveInterface;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_end_videoActModel;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.App_monitorActModel;
import com.bogokj.live.model.App_request_pkActModel;
import com.bogokj.live.model.App_start_lianmaiActModel;
import com.bogokj.live.model.CustomMsgCancelPK;
import com.bogokj.live.model.LiveQualityData;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgAcceptPK;
import com.bogokj.live.model.custommsg.CustomMsgApplyLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgEndPK;
import com.bogokj.live.model.custommsg.CustomMsgRejectLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgRejectPK;
import com.bogokj.live.model.custommsg.CustomMsgRequestPK;
import com.bogokj.live.model.custommsg.CustomMsgStartPK;
import com.bogokj.live.model.custommsg.CustomMsgStopLinkMic;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

/**
 * 直播间主播业务类
 */
public class LiveCreaterBusiness extends LiveBusiness {
    /**
     * 连麦数量
     */
    private int mLinkMicCount;
    /**
     * 最后一次申请连麦的用户id
     */
    private String mApplyLinkMicUserId;
    private String mApplyPKUserId;

    public String getmApplyPKUserId() {
        return mApplyPKUserId;
    }

    public void setmApplyPKUserId(String mApplyPKUserId) {
        this.mApplyPKUserId = mApplyPKUserId;
    }

    private UserModel pkuserModel;

    public UserModel getPkuserModel() {
        return pkuserModel;
    }

    public void setPkuserModel(UserModel pkuserModel) {
        this.pkuserModel = pkuserModel;
    }

    private ISDLooper mLooperMonitor;
    private LiveCreaterBusinessCallback mBusinessCallback;
    //是否在Pk
    private boolean mIsInApplyPK;
    //状态是否在惩罚
    private boolean mIsInPunish;

    public boolean ismIsInPunish() {
        return mIsInPunish;
    }

    public void setmIsInPunish(boolean mIsInPunish) {
        this.mIsInPunish = mIsInPunish;
    }

    public LiveCreaterBusiness(ILiveInterface liveActivity) {
        super(liveActivity);
    }

    public void setBusinessCallback(LiveCreaterBusinessCallback businessCallback) {
        this.mBusinessCallback = businessCallback;
        super.setBusinessCallback(businessCallback);
    }

    public void setLinkMicCount(int linkMicCount) {
        mLinkMicCount = linkMicCount;
    }

    public int getLinkMicCount() {
        return mLinkMicCount;
    }

    /**
     * 更新房间状态为失败
     */
    public void requestUpdateLiveStateFail() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), null, 0, null);
    }

    /**
     * 更新房间状态为成功
     */
    public void requestUpdateLiveStateSuccess() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), getLiveActivity().getGroupId(), 1, null);
    }

    /**
     * 更新房间状态为主播离开
     */
    public void requestUpdateLiveStateLeave() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), null, 2, null);
    }

    /**
     * 更新房间状态为主播回来
     */
    public void requestUpdateLiveStateComeback() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), null, 3, null);
    }

    /**
     * 请求主播插件列表接口
     */
    public void requestInitPlugin() {
        CommonInterface.requestInitPlugin(new AppRequestCallback<App_plugin_initActModel>() {
            @Override
            public String getCancelTag() {
                return getHttpCancelTag();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mBusinessCallback.onBsCreaterRequestInitPluginSuccess(actModel);
                }
            }
        });
    }

    @Override
    protected void onRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onRequestRoomInfoSuccess(actModel);
        startMonitor();
    }

    /**
     * 开始主播心跳
     */
    private void startMonitor() {
        long time = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            time = model.getMonitor_second() * 1000;
        }
        if (time <= 0) {
            time = 5 * 1000;
        }

        if (mLooperMonitor == null) {
            mLooperMonitor = new SDSimpleLooper();
        }
        mLooperMonitor.start(time, mMonitorRunnable);
    }

    private Runnable mMonitorRunnable = new Runnable() {
        @Override
        public void run() {
            CreaterMonitorData data = mBusinessCallback.onBsCreaterGetMonitorData();
            if (data != null) {
                LogUtil.i("monitor data:" + data.toString());
            }
            CommonInterface.requestMonitor(data, new AppRequestCallback<App_monitorActModel>() {
                @Override
                public String getCancelTag() {
                    return getHttpCancelTag();
                }

                @Override
                protected void onSuccess(SDResponse resp) {
                    onRequestMonitorSuccess(actModel);
                }
            });
        }
    };

    /**
     * 主播心跳成功回调
     */
    private void onRequestMonitorSuccess(App_monitorActModel actModel) {
        mBusinessCallback.onBsCreaterRequestMonitorSuccess(actModel);
    }

    /**
     * 请求结束直播
     */
    public void requestEndVideo() {
        mEndVideoCallback.setCallback(mBusinessCallback);
        CommonInterface.requestEndVideo(getLiveActivity().getRoomId(), mEndVideoCallback);
    }

    public void setInPK(boolean is) {
        mIsInApplyPK = is;
    }

    private abstract static class CreaterRequestCallback<T> extends AppRequestCallback<T> {
        protected LiveCreaterBusinessCallback nCallback;

        public void setCallback(LiveCreaterBusinessCallback callback) {
            this.nCallback = callback;
        }
    }

    private static CreaterRequestCallback<App_end_videoActModel> mEndVideoCallback = new CreaterRequestCallback<App_end_videoActModel>() {
        @Override
        protected void onSuccess(SDResponse sdResponse) {
            if (actModel.isOk() && nCallback != null) {
                nCallback.onBsCreaterRequestEndVideoSuccess(actModel);
            }
        }
    };

    /**
     * 停止主播心跳
     */
    public void stopMonitor() {
        if (mLooperMonitor != null) {
            mLooperMonitor.stop();
        }
    }

    @Override
    public void onDestroy() {
        stopMonitor();
        mEndVideoCallback.setCallback(null);
        super.onDestroy();
    }

    @Override
    public void onMsgApplyLinkMic(CustomMsgApplyLinkMic msg) {
        super.onMsgApplyLinkMic(msg);

        String userId = msg.getSender().getUser_id();
        if (isInLinkMic()) {
            int maxLinkMicCount = AppRuntimeWorker.getMaxLinkMicCount();

            if (mLinkMicCount >= maxLinkMicCount) {
                rejectLinkMic(userId, CustomMsgRejectLinkMic.MSG_MAX);
                return;
            }
        }

        if (mBusinessCallback.onBsCreaterIsReceiveApplyLinkMicShow()) {
            // 主播有未处理的连麦请求
            rejectLinkMic(userId, CustomMsgRejectLinkMic.MSG_BUSY);
            return;
        }

        mApplyLinkMicUserId = userId;
        mBusinessCallback.onBsCreaterShowReceiveApplyLinkMic(msg);
    }

    @Override
    public void onMsgStopLinkMic(CustomMsgStopLinkMic msg) {
        super.onMsgStopLinkMic(msg);

        String userId = msg.getSender().getUser_id();
        if (mBusinessCallback.onBsCreaterIsReceiveApplyLinkMicShow()) {
            if (userId.equals(mApplyLinkMicUserId)) {
                mBusinessCallback.onBsCreaterHideReceiveApplyLinkMic();
            }
        }
        stopLinkMic(userId, false);
    }


    @Override
    public void onMsgRequestPK(CustomMsgRequestPK msg) {
        super.onMsgRequestPK(msg);

        String userId = msg.getUser_id();
        if (mBusinessCallback.onBsCreaterIsReceivePkShow()) {
            // 主播有未处理的连麦请求
            rejectPK(userId, CustomMsgRequestPK.MSG_BUSY);
            return;
        }

        mApplyPKUserId = userId;
        mBusinessCallback.onBsCreaterShowReceivePK(msg);

    }

    @Override
    public void onMsgAcceptPK(CustomMsgAcceptPK msg) {
        //String userId = msg.getSender().getUser_id();

        mBusinessCallback.onBsViewerApplyPKAccept(msg.getPk_id());

        CustomMsgStartPK customMsgStartPK = new CustomMsgStartPK();
        customMsgStartPK.setPk_id(msg.getPk_id());
        //通知群组观众开始PK
        IMHelper.sendMsgGroup(getLiveActivity().getGroupId(), customMsgStartPK, null);
    }

    @Override
    public void onMsgRejectPK(CustomMsgRejectPK msg) {
        String userId = msg.getSender().getUser_id();
        if (mIsInApplyPK) {
            mBusinessCallback.onBsViewerApplyPKRejected(msg.getMsg());

            mIsInApplyPK = false;
        }
    }

    @Override
    public void onMsgCancelPK(CustomMsgCancelPK msg) {

        mIsInApplyPK = false;
        mBusinessCallback.onBsViewerCancelPKRequest(msg);
    }

    @Override
    public void onMsgEndPK(CustomMsgEndPK msg) {
        mIsInApplyPK = false;
        mBusinessCallback.onBsViewerEndPK(msg);
    }

    //播放背景音效
    public void onClickBGSound(String url) {
        mBusinessCallback.onBsViewerClickSound(url);
    }

    /**
     * 接受连麦
     *
     * @param userId 连麦观众id
     */
    public void acceptLinkMic(final String userId) {
        CommonInterface.requestStartLianmai(getLiveActivity().getRoomId(), userId, new AppRequestCallback<App_start_lianmaiActModel>() {
            @Override
            public String getCancelTag() {
                return getHttpCancelTag();
            }

            @Override
            protected void onStart() {
                super.onStart();
                showProgress("");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    CustomMsgAcceptLinkMic msg = new CustomMsgAcceptLinkMic();
                    msg.fillValue(actModel);
                    IMHelper.sendMsgC2C(userId, msg, null);
                    mBusinessCallback.onBsViewerLinkMicAccept();
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                hideProgress();
            }
        });
    }


    /**
     * 拒绝连麦
     *
     * @param userId 连麦观众id
     * @param reason 原因
     */
    public void rejectLinkMic(String userId, String reason) {
        CustomMsgRejectLinkMic msg = new CustomMsgRejectLinkMic();
        msg.setMsg(reason);
        IMHelper.sendMsgC2C(userId, msg, null);
    }

    /**
     * 停止连麦
     *
     * @param userId      要停止连麦的用户id
     * @param sendStopMsg 是否发送结束连麦的消息
     */
    public void stopLinkMic(String userId, boolean sendStopMsg) {
        if (isInLinkMic()) {
            requestStopLianmai(userId);
            if (sendStopMsg) {
                IMHelper.sendMsgC2C(userId, new CustomMsgStopLinkMic(), null);
            }
        }
    }


    //申请PK
    public void requestPK(final String user_id, String timeid) {
        Log.d("ly", timeid);
        if (mIsInApplyPK) {
            SDToast.showToast("当前处于PK状态！");
            return;
        }

        CommonInterface.requestPK(user_id, timeid, new AppRequestCallback<App_request_pkActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    CustomMsgRequestPK customMsgRequestPK = new CustomMsgRequestPK();
                    customMsgRequestPK.setPkid(actModel.getPk_id());
                    IMHelper.sendMsgC2C(user_id, customMsgRequestPK, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int code, String msg) {
                            SDToast.showToast("申请PK失败！");
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            mBusinessCallback.onBsViewerShowApplyPK(true);
                            mIsInApplyPK = true;
                        }
                    });
                }
            }
        });
    }

    //取消申请PK
    public void cancelApplyPK() {
        if (mIsInApplyPK) {
            CommonInterface.requestCanclePKRequest(new AppRequestCallback<App_requestCancelPKRequest>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    IMHelper.sendMsgC2C(actModel.getTo_user_id(), new CustomMsgCancelPK(), null);
                    mIsInApplyPK = false;
                }
            });
        }
    }


    //接受PK
    public void acceptPK(String userId, String pkId) {
        CustomMsgAcceptPK customMsgAcceptPK = new CustomMsgAcceptPK();
        customMsgAcceptPK.setPk_id(pkId);
        IMHelper.sendMsgC2C(userId, customMsgAcceptPK, null);

//        CustomMsgStartPK customMsgStartPK = new CustomMsgStartPK();
//        customMsgStartPK.setPk_id(pkId);
//        //通知群组观众开始PK
//        IMHelper.sendMsgGroup(getLiveActivity().getGroupId(),customMsgStartPK ,null);
    }

    //拒绝PK
    public void rejectPK(String uid, String msg) {
        CustomMsgRejectPK customMsgRejectPK = new CustomMsgRejectPK();
        customMsgRejectPK.setMsg(msg);
        IMHelper.sendMsgC2C(uid, customMsgRejectPK, null);

    }

    public boolean isInPK() {
        return mIsInApplyPK;
    }


    /**
     * 主播心跳需要上传的数据
     */
    public static class CreaterMonitorData {
        /**
         * 房间id
         */
        public int roomId;
        /**
         * 真实观众数量
         */
        public int viewerNumber;
        /**
         * 直播间印票数量
         */
        public long ticketNumber;
        /**
         * 连麦数量
         */
        public int linkMicNumber;
        /**
         * 直播的质量
         */
        public LiveQualityData liveQualityData;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("roomId:").append(roomId).append("\r\n");
            sb.append("viewerNumber:").append(viewerNumber).append("\r\n");
            sb.append("ticketNumber:").append(ticketNumber).append("\r\n");
            sb.append("linkMicNumber:").append(linkMicNumber).append("\r\n");
            sb.append("liveQualityData:").append(liveQualityData).append("\r\n");
            return sb.toString();
        }
    }

    public interface LiveCreaterBusinessCallback extends LiveBusinessCallback {
        /**
         * 获得主播心跳要提交的数据
         *
         * @return
         */
        CreaterMonitorData onBsCreaterGetMonitorData();

        /**
         * 请求主播心跳成功回调
         *
         * @param actModel
         */
        void onBsCreaterRequestMonitorSuccess(App_monitorActModel actModel);

        /**
         * 请求主播插件列表成功回调
         *
         * @param actModel
         */
        void onBsCreaterRequestInitPluginSuccess(App_plugin_initActModel actModel);

        /**
         * 请求结束直播接口成功回调
         *
         * @param actModel
         */
        void onBsCreaterRequestEndVideoSuccess(App_end_videoActModel actModel);

        /**
         * 显示收到连麦申请界面
         *
         * @param msg
         */
        void onBsCreaterShowReceiveApplyLinkMic(CustomMsgApplyLinkMic msg);

        /**
         * 隐藏连麦邀请界面
         */
        void onBsCreaterHideReceiveApplyLinkMic();

        /**
         * 连麦邀请界面是否显示
         *
         * @return
         */
        boolean onBsCreaterIsReceiveApplyLinkMicShow();

        //邀请PK界面是否显示
        boolean onBsCreaterIsReceivePkShow();

        //邀请PK展示
        void onBsCreaterShowReceivePK(CustomMsgRequestPK msg);

        //显示申请PK中
        void onBsViewerShowApplyPK(boolean show);

        //拒绝PK
        void onBsViewerApplyPKRejected(String msg);

        //同意PK
        void onBsViewerApplyPKAccept(String pkId);

        //取消申请
        void onBsViewerCancelPKRequest(CustomMsgCancelPK msg);

        //结束PK
        void onBsViewerEndPK(CustomMsgEndPK msg);

        void onBsViewerLinkMicAccept();

        //点击音效
        void onBsViewerClickSound(String url);
    }

}
