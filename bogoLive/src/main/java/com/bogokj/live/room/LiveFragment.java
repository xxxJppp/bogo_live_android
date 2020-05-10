package com.bogokj.live.room;

import android.content.Context;
import android.text.TextUtils;

import com.bogokj.games.model.App_plugin_initActModel;
import com.bogokj.games.model.custommsg.CustomMsgGameBanker;
import com.bogokj.hybrid.event.EUnLogin;
import com.bogokj.hybrid.fragment.BaseFragment;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.live.IMHelper;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.LiveCreaterIM;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.LiveViewerIM;
import com.bogokj.live.business.LiveBusiness;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.business.LiveMsgBusiness;
import com.bogokj.live.business.LiveViewerBusiness;
import com.bogokj.live.control.IPushSDK;
import com.bogokj.live.event.EImOnForceOffline;
import com.bogokj.live.event.EImOnNewMessages;
import com.bogokj.live.event.EOnCallStateChanged;
import com.bogokj.live.model.App_end_videoActModel;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.App_monitorActModel;
import com.bogokj.live.model.App_viewerActModel;
import com.bogokj.live.model.CustomMsgCancelPK;
import com.bogokj.live.model.LiveQualityData;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgAcceptPK;
import com.bogokj.live.model.custommsg.CustomMsgApplyLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgBuyGuardianSuccess;
import com.bogokj.live.model.custommsg.CustomMsgCreaterComeback;
import com.bogokj.live.model.custommsg.CustomMsgCreaterLeave;
import com.bogokj.live.model.custommsg.CustomMsgData;
import com.bogokj.live.model.custommsg.CustomMsgEndPK;
import com.bogokj.live.model.custommsg.CustomMsgEndVideo;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.bogokj.live.model.custommsg.CustomMsgLargeGift;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.model.custommsg.CustomMsgLuckGift;
import com.bogokj.live.model.custommsg.CustomMsgOpenGuardSuccess;
import com.bogokj.live.model.custommsg.CustomMsgPopMsg;
import com.bogokj.live.model.custommsg.CustomMsgRedEnvelope;
import com.bogokj.live.model.custommsg.CustomMsgRefreshWish;
import com.bogokj.live.model.custommsg.CustomMsgRejectLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgRejectPK;
import com.bogokj.live.model.custommsg.CustomMsgRequestPK;
import com.bogokj.live.model.custommsg.CustomMsgStartPK;
import com.bogokj.live.model.custommsg.CustomMsgStopLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgStopLive;
import com.bogokj.live.model.custommsg.CustomMsgUpdateTicketPK;
import com.bogokj.live.model.custommsg.CustomMsgViewerJoin;
import com.bogokj.live.model.custommsg.CustomMsgViewerQuit;
import com.bogokj.live.model.custommsg.CustomMsgWarning;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.model.custommsg.data.DataLinkMicInfoModel;
import com.umeng.socialize.UMShareListener;

import java.util.List;

/**
 * @author kn update
 * @description: 直播间基类
 * @time 2020/2/16
 */
public class LiveFragment extends BaseFragment implements
        ILiveInterface,
        LiveMsgBusiness.LiveMsgBusinessCallback,
        LiveViewerBusiness.LiveViewerBusinessCallback,
        LiveCreaterBusiness.LiveCreaterBusinessCallback {
    /**
     * 房间id(int)
     */
    public static final String EXTRA_ROOM_ID = "extra_room_id";
    /**
     * 讨论组id(String)
     */
    public static final String EXTRA_GROUP_ID = "extra_group_id";
    /**
     * 主播identifier(String)
     */
    public static final String EXTRA_CREATER_ID = "extra_creater_id";

    private LiveViewerIM mViewerIM;
    private LiveCreaterIM mCreaterIM;

    private LiveMsgBusiness mMsgBusiness;

    private LiveViewerBusiness mViewerBusiness;
    private LiveCreaterBusiness mCreaterBusiness;
    public Context context;

    @Override
    protected void init() {
        super.init();
        context = getContext();
    }


    /**
     * 获得推流sdk
     *
     * @return
     */
    protected IPushSDK getPushSDK() {
        return null;
    }

    /**
     * 获得消息业务类
     *
     * @return
     */
    public LiveMsgBusiness getMsgBusiness() {
        if (mMsgBusiness == null) {
            mMsgBusiness = new LiveMsgBusiness();
            mMsgBusiness.setBusinessCallback(this);
        }
        return mMsgBusiness;
    }

    /**
     * 获得观众业务类
     *
     * @return
     */
    public LiveViewerBusiness getViewerBusiness() {
        if (mViewerBusiness == null) {
            mViewerBusiness = new LiveViewerBusiness(this);
            mViewerBusiness.setBusinessCallback(this);
        }
        return mViewerBusiness;
    }

    /**
     * 获得主播业务类
     *
     * @return
     */
    public LiveCreaterBusiness getCreaterBusiness() {
        if (mCreaterBusiness == null) {
            mCreaterBusiness = new LiveCreaterBusiness(this);
            mCreaterBusiness.setBusinessCallback(this);
        }
        return mCreaterBusiness;
    }

    /**
     * 获得直播业务类
     *
     * @return
     */
    public LiveBusiness getLiveBusiness() {
        if (isPlayback()) {
            return getViewerBusiness();
        } else {
            if (isCreater()) {
                return getCreaterBusiness();
            } else {
                return getViewerBusiness();
            }
        }
    }

    /**
     * 获得观众的IM操作类
     *
     * @return
     */
    public LiveViewerIM getViewerIM() {
        if (mViewerIM == null) {
            mViewerIM = new LiveViewerIM();
        }
        return mViewerIM;
    }

    /**
     * 获得主播的IM操作类
     *
     * @return
     */
    public LiveCreaterIM getCreaterIM() {
        if (mCreaterIM == null) {
            mCreaterIM = new LiveCreaterIM();
        }
        return mCreaterIM;
    }

    /**
     * 打开分享面板
     */
    public void openShare(UMShareListener listener) {
        getLiveBusiness().openShare(getActivity(), listener);
    }

    public void doShare(int platform, UMShareListener listener) {
        getLiveBusiness().doShare(platform, getActivity(), listener);
    }

    /**
     * 请求房间信息
     *
     * @return
     */
    protected void requestRoomInfo() {
        //子类实现
    }

    /**
     * 观众加入聊天组成功回调
     *
     * @param groupId
     */
    protected void onSuccessJoinGroup(String groupId) {

    }

    /**
     * 接收im新消息
     *
     * @param event
     */
    public void onEventMainThread(EImOnNewMessages event) {
        String groupId = getGroupId();

        getMsgBusiness().parseMsg(event.msg, groupId);
        getLiveBusiness().getMsgBusiness().parseMsg(event.msg, groupId);

        try {
            if (LiveConstant.CustomMsgType.MSG_DATA == event.msg.getCustomMsgType()) {
                String peer = event.msg.getConversationPeer();
                if (!TextUtils.isEmpty(groupId)) {
                    if (!groupId.equals(peer)) {
                        // 别的直播间消息
                        IMHelper.quitGroup(peer, null);
                        LogUtil.i("quitGroup other room:" + peer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMsgRedEnvelope(CustomMsgRedEnvelope msg) {
    }

    @Override
    public void onMsgApplyLinkMic(CustomMsgApplyLinkMic msg) {
    }

    @Override
    public void onMsgAcceptLinkMic(CustomMsgAcceptLinkMic msg) {
    }

    @Override
    public void onMsgRejectLinkMic(CustomMsgRejectLinkMic msg) {
    }

    @Override
    public void onMsgStopLinkMic(CustomMsgStopLinkMic msg) {
    }

    @Override
    public void onMsgEndVideo(CustomMsgEndVideo msg) {
    }

    @Override
    public void onMsgStopLive(CustomMsgStopLive msg) {
    }

    @Override
    public void onMsgPrivate(MsgModel msg) {
    }

    @Override
    public void onMsgAuction(MsgModel msg) {
    }

    @Override
    public void onMsgShop(MsgModel msg) {
    }

    @Override
    public void onMsgGame(MsgModel msg) {
    }

    @Override
    public void onMsgGameBanker(CustomMsgGameBanker msg) {

    }

    @Override
    public void onMsgGift(CustomMsgGift msg) {
    }

    @Override
    public void onMsgPopMsg(CustomMsgPopMsg msg) {
    }

    @Override
    public void onMsgViewerJoin(CustomMsgViewerJoin msg) {
    }

    @Override
    public void onMsgViewerQuit(CustomMsgViewerQuit msg) {
    }

    @Override
    public void onMsgCreaterLeave(CustomMsgCreaterLeave msg) {
    }

    @Override
    public void onMsgCreaterComeback(CustomMsgCreaterComeback msg) {
    }

    @Override
    public void onMsgLight(CustomMsgLight msg) {
    }

    @Override
    public void onMsgLiveChat(MsgModel msg) {
    }

    @Override
    public void onMsgWarning(CustomMsgWarning msgWarning) {

    }

    @Override
    public void onMsgData(CustomMsgData msg) {

    }

    @Override
    public void onMsgDataViewerList(App_viewerActModel model) {

    }

    @Override
    public void onMsgDataLinkMicInfo(DataLinkMicInfoModel model) {

    }

    @Override
    public void onMsgLargeGift(CustomMsgLargeGift customMsgLargeGift) {

    }

    @Override
    public void onMsgBuyGuardianSuccess(CustomMsgBuyGuardianSuccess msg) {

    }

    @Override
    public void OnMsgOpenGuardSuccess(CustomMsgOpenGuardSuccess msg) {

    }

    @Override
    public void OnMsgLuckGift(CustomMsgLuckGift msg) {

    }

    @Override
    public void OnMsgRefreshWish(CustomMsgRefreshWish msg) {
        //刷新心愿单
    }

    @Override
    public void onMsgRequestPK(CustomMsgRequestPK customMsgRequestPK) {

    }

    @Override
    public void onMsgRejectPK(CustomMsgRejectPK customMsgRejectPK) {

    }

    @Override
    public void onMsgAcceptPK(CustomMsgAcceptPK customMsgAcceptPK) {

    }

    @Override
    public void onMsgStartPK(CustomMsgStartPK customMsgStartPK) {

    }

    @Override
    public void onMsgUpdateTicketPK(CustomMsgUpdateTicketPK customMsgUpdateTicketPK) {

    }

    @Override
    public void onMsgCancelPK(CustomMsgCancelPK customMsgCancelPK) {

    }

    @Override
    public void onMsgEndPK(CustomMsgEndPK customMsgEndPK) {

    }

    @Override
    public void onMsgPayMode(MsgModel msg) {
    }

    /**
     * 退出登录
     *
     * @param event
     */
    public void onEventMainThread(EUnLogin event) {
        //子类实现
    }

    /**
     * 帐号异地登录
     *
     * @param event
     */
    public void onEventMainThread(EImOnForceOffline event) {
        //子类实现
    }

    /**
     * 电话监听
     *
     * @param event
     */
    public void onEventMainThread(EOnCallStateChanged event) {
        //子类实现
    }

    /**
     * 初始化IM
     */
    protected void initIM() {
        //子类实现
    }

    /**
     * 销毁IM
     */
    protected void destroyIM() {
        //子类实现
    }


    @Override
    public void onDestroy() {
        mViewerIM = null;
        mCreaterIM = null;
        if (mViewerBusiness != null) {
            mViewerBusiness.onDestroy();
            mViewerBusiness = null;
        }
        if (mCreaterBusiness != null) {
            mCreaterBusiness.onDestroy();
            mCreaterBusiness = null;
        }
        LiveInformation.getInstance().exitRoom();
        super.onDestroy();
    }

    //sdk

    /**
     * 开关声音
     *
     * @param enable true-打开，false-关闭
     */
    protected void sdkEnableAudio(boolean enable) {
        //子类实现
    }

    /**
     * 暂停视频播放
     */
    protected void sdkPauseVideo() {

    }

    /**
     * 恢复视频播放
     */
    protected void sdkResumeVideo() {

    }

    /**
     * 停止连麦
     */
    protected void sdkStopLinkMic() {
        //子类实现
    }

    @Override
    public LiveCreaterBusiness.CreaterMonitorData onBsCreaterGetMonitorData() {
        return null;
    }

    @Override
    public void onBsCreaterRequestMonitorSuccess(App_monitorActModel actModel) {
    }

    @Override
    public void onBsCreaterRequestInitPluginSuccess(App_plugin_initActModel actModel) {
    }

    @Override
    public void onBsCreaterRequestEndVideoSuccess(App_end_videoActModel actModel) {
    }

    @Override
    public void onBsCreaterShowReceiveApplyLinkMic(CustomMsgApplyLinkMic msg) {
    }

    @Override
    public void onBsCreaterHideReceiveApplyLinkMic() {
    }

    @Override
    public boolean onBsCreaterIsReceiveApplyLinkMicShow() {
        return false;
    }

    @Override
    public boolean onBsCreaterIsReceivePkShow() {
        return false;
    }

    @Override
    public void onBsCreaterShowReceivePK(CustomMsgRequestPK msg) {

    }

    @Override
    public void onBsViewerShowApplyPK(boolean show) {

    }

    @Override
    public void onBsViewerApplyPKRejected(String msg) {

    }

    @Override
    public void onBsViewerApplyPKAccept(String pkId) {

    }

    @Override
    public void onBsViewerCancelPKRequest(CustomMsgCancelPK msg) {

    }

    @Override
    public void onBsViewerEndPK(CustomMsgEndPK msg) {

    }

    @Override
    public void onBsViewerShowCreaterLeave(boolean show) {
    }

    @Override
    public void onBsViewerShowApplyLinkMic(boolean show) {
    }

    @Override
    public void onBsViewerApplyLinkMicError(String msg) {
    }

    @Override
    public void onBsViewerApplyLinkMicRejected(CustomMsgRejectLinkMic msg) {
    }

    @Override
    public void onBsViewerStartJoinRoom() {
    }

    @Override
    public void onBsViewerExitRoom(boolean needFinishActivity) {

    }

    @Override
    public void onBsViewerShowDailyTask(boolean show) {

    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
    }

    @Override
    public void onBsRequestRoomInfoError(App_get_videoActModel actModel) {
    }

    @Override
    public void onBsRequestRoomInfoException(String msg) {
    }

    @Override
    public void onBsRefreshViewerList(List<UserModel> listModel) {
    }

    @Override
    public void onBsViewerApplyLinkMicAccepted(CustomMsgAcceptLinkMic msg) {

    }

    @Override
    public void onBsViewerLinkMicAccept() {

    }

    @Override
    public void onBsViewerClickSound(String url) {

    }

    @Override
    public void onBsRemoveViewer(UserModel model) {
    }

    @Override
    public void onBsInsertViewer(int position, UserModel model) {
    }

    @Override
    public void onBsTicketChange(long ticket) {
    }

    @Override
    public void onBsGuardNumChange(int guardnum) {

    }

    @Override
    public void onBsViewerNumberChange(int viewerNumber) {
    }

    @Override
    public void onBsBindCreaterData(UserModel model) {
    }

    @Override
    public void onBsShowOperateViewer(boolean show) {
    }

    @Override
    public LiveQualityData onBsGetLiveQualityData() {
        return null;
    }

    @Override
    public void onBsUpdateLiveQualityData(LiveQualityData data) {
    }

    @Override
    public void onBsShowProgress(String msg) {
        showProgressDialog(msg);
    }


    @Override
    public void onBsHideProgress() {
        dismissProgressDialog();
    }

    //----------ILiveActivity implements start----------

    @Override
    public int getRoomId() {
        return LiveInformation.getInstance().getRoomId();
    }

    @Override
    public String getGroupId() {
        return LiveInformation.getInstance().getGroupId();
    }

    @Override
    public String getCreaterId() {
        return LiveInformation.getInstance().getCreaterId();
    }

    @Override
    public App_get_videoActModel getRoomInfo() {
        return LiveInformation.getInstance().getRoomInfo();
    }

    @Override
    public boolean isPrivate() {
        return LiveInformation.getInstance().isPrivate();
    }

    @Override
    public boolean isPlayback() {
        return LiveInformation.getInstance().isPlayback();
    }

    @Override
    public boolean isCreater() {
        return LiveInformation.getInstance().isCreater();
    }

    @Override
    public int getSdkType() {
        return LiveInformation.getInstance().getSdkType();
    }

    @Override
    public boolean isAuctioning() {
        return LiveInformation.getInstance().isAuctioning();
    }

    @Override
    public void openSendMsg(String content) {

    }

    //----------ILiveActivity implements start----------

}
