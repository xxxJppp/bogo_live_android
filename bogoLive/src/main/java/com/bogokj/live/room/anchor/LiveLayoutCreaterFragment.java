package com.bogokj.live.room.anchor;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bogokj.games.BankerBusiness;
import com.bogokj.games.model.App_plugin_initActModel;
import com.bogokj.games.model.GameBankerModel;
import com.bogokj.games.model.PluginModel;
import com.bogokj.hybrid.umeng.UmengSocialManager;
import com.bogokj.libgame.poker.bull.view.BullGameView;
import com.bogokj.libgame.poker.goldflower.view.GoldFlowerGameView;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveSongChooseActivity;
import com.bogokj.live.appview.LiveCreaterPluginToolView;
import com.bogokj.live.appview.room.ARoomMusicView;
import com.bogokj.live.appview.room.RoomCountDownView;
import com.bogokj.live.appview.room.RoomCreaterBottomView;
import com.bogokj.live.appview.room.RoomCreaterFinishView;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.BogoLiveSoundDialog;
import com.bogokj.live.dialog.BogoWishListDialog;
import com.bogokj.live.dialog.LiveApplyRequestPKDialog;
import com.bogokj.live.dialog.LiveCreaterPluginDialog;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.event.ECreateLiveSuccess;
import com.bogokj.live.model.App_end_videoActModel;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.CustomMsgCancelPK;
import com.bogokj.live.model.RoomShareModel;
import com.bogokj.live.model.custommsg.CustomMsgWarning;
import com.bogokj.live.room.LiveLayoutFragment;
import com.bogokj.live.room.activity.BaseLiveActivity;
import com.bogokj.live.view.RoomPluginToolView;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.sunday.eventbus.SDEventManager;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author kn
 * @description: 主播界面 样式相关
 * @time kn 2019/12/19
 */
public class LiveLayoutCreaterFragment extends LiveLayoutFragment implements LiveCreaterPluginToolView.ClickListener, BankerBusiness.BankerBusinessCallback {
    /**
     * 1：主播界面被强制关闭后回来(int)
     */
    public static final String EXTRA_IS_CLOSED_BACK = "EXTRA_IS_CLOSED_BACK";
    /**
     * 1：主播界面被强制关闭后回来
     */
    protected int mIsClosedBack;

    /**
     * 主播是否离开
     */
    protected boolean mIsCreaterLeave = false;

    /**
     * 是否静音模式
     */
    protected boolean mIsMuteMode = false;
    /**
     * 是否显示直播结束界面
     */
    protected boolean mIsNeedShowFinish = false;


    private RoomCreaterFinishView mRoomCreaterFinishView;
    private ARoomMusicView mRoomMusicView;
    private RoomCountDownView mRoomCountDownView;

    private LiveCreaterPluginDialog mDialogCreaterPlugin;
    protected LiveApplyRequestPKDialog mDialogApplyPK;
    private GoldFlowerGameView mPokerGameView;
    private BullGameView mPokerGameView1;

    @Override
    protected void init() {
        super.init();
        mIsClosedBack = getArguments().getInt(EXTRA_IS_CLOSED_BACK, 0);

        ImageView iv_live_sound = (ImageView) findViewById(R.id.iv_live_sound);
        iv_live_sound.setOnClickListener(this);

        LiveInformation.getInstance().setCreaterId(UserModelDao.getUserId());

        //游戏模块回调实现
          getGameClassUtils().getBankerBusiness().setCallback(this);

    }


    @Override
    protected void initLayout(View view) {
        super.initLayout(view);

        addRoomMusicView();
    }

    /**
     * 添加音乐播放view
     */
    private void addRoomMusicView() {
        if (mRoomMusicView == null) {
            mRoomMusicView = createRoomMusicView();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mRoomMusicView.setLayoutParams(params);
            ((BaseLiveActivity) getActivity()).replaceView(R.id.fl_live_play_music, mRoomMusicView);
        }
    }

    /**
     * 获得音乐播放view
     *
     * @return
     */
    public ARoomMusicView getRoomMusicView() {
        return mRoomMusicView;
    }

    /**
     * 添加倒计时开播view
     */
    protected void addRoomCountDownView() {
        boolean showCount = getResources().getBoolean(R.bool.show_create_room_count_down);
        if (showCount) {
            if (mRoomCountDownView == null) {
                mRoomCountDownView = new RoomCountDownView(getContext());
                ((BaseLiveActivity) getActivity()).addView(mRoomCountDownView);
                mRoomCountDownView.startCountDown(3);
            }
        }
    }

    @Override
    protected void addLiveFinish() {
        if (mRoomCreaterFinishView == null) {
            mRoomCreaterFinishView = new RoomCreaterFinishView(getContext());
            mRoomCreaterFinishView.setClickListener(() -> openShare(null));
            ((BaseLiveActivity) getActivity()).addView(mRoomCreaterFinishView);
        }
    }


    @Override
    public void onBsCreaterRequestEndVideoSuccess(App_end_videoActModel actModel) {
        super.onBsCreaterRequestEndVideoSuccess(actModel);
        if (mRoomCreaterFinishView != null) {
            mRoomCreaterFinishView.onLiveCreaterRequestEndVideoSuccess(actModel);
        }
    }

    /**
     * 移除倒计时开播view
     */
    protected void removeRoomCountDownView() {
        removeView(mRoomCountDownView);
    }

    /**
     * 创建播放音乐的view
     */
    protected ARoomMusicView createRoomMusicView() {
        // 子类实现
        return null;
    }

    /**
     * 停止播放音乐
     */
    protected void stopMusic() {
        if (mRoomMusicView != null) {
            mRoomMusicView.stopMusic();
        }
    }

    @Override
    protected void addRoomBottomView(View view) {
        mRoomCreaterBottomView.setVisibility(View.VISIBLE);
        mRoomCreaterBottomView.setClickListener(mBottomClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_live_sound:
                clickLiveSound();
                break;
            default:
                break;
        }
    }


    /**
     * 底部菜单点击监听
     */
    private RoomCreaterBottomView.ClickListener mBottomClickListener = new RoomCreaterBottomView.ClickListener() {
        @Override
        public void onClickMenuSendMsg(View v) {
            LiveLayoutCreaterFragment.this.onClickMenuSendMsg(v);
        }

        @Override
        public void onClickMenuPrivateMsg(View v) {
            LiveLayoutCreaterFragment.this.onClickMenuPrivateMsg(v);
        }

        @Override
        public void onClickMenuShare(View v) {
            LiveLayoutCreaterFragment.this.onClickMenuShare(v);
        }

        @Override
        public void onClickMenuCreaterPlugin(View v) {
            showCreaterPlugin();
        }

        @Override
        public void onClickMenuStart(View v) {
              getGameClassUtils().onClickGameCtrlStart(v);
        }

        @Override
        public void onClickMenuPayMode(View v) {
            LiveLayoutCreaterFragment.this.onClickMenuPayMode(v);
        }

        @Override
        public void onClickMenuPayModeUpagrade(View v) {
            LiveLayoutCreaterFragment.this.onClickMenuPayUpagrade(v);
        }

        @Override
        public void onClickMenuBottomExtendSwitch(View v) {
            toggleBottomExtend();
        }

        @Override
        public void onClickMenuOpenBanker(View v) {
              getGameClassUtils().onClickBankerCtrlCreaterOpenBanker();
        }

        @Override
        public void onClickMenuStopBanker(View v) {
              getGameClassUtils().onClickBankerCtrlCreaterStopBanker();
        }

        @Override
        public void onClickMenuBankerList(View v) {
              getGameClassUtils().onClickBankerCtrlCreaterOpenBankerList();
        }

        @Override
        public void onClickMenuPK(View v) {
            onClickPK();
        }

        @Override
        public void onClickMenuWish(View v) {
            onClickWish();
        }
    };

    //点击音效
    private void clickLiveSound() {
        new BogoLiveSoundDialog(getActivity(), getCreaterBusiness()).showBottom();
    }

    //点击心愿单
    private void onClickWish() {
        new BogoWishListDialog(getActivity()).showBottom();
    }

    //点击PK
    protected void onClickPK() {

    }

    /**
     * 显示主播插件菜单
     */
    protected void showCreaterPlugin() {
        if (mDialogCreaterPlugin == null) {
            mDialogCreaterPlugin = new LiveCreaterPluginDialog(getActivity());
            mDialogCreaterPlugin.setItemClickCallback(new SDItemClickCallback<PluginModel>() {
                @Override
                public void onItemClick(int position, PluginModel model, View view) {
                    onClickCreaterPlugin(model);
                }
            });
            mDialogCreaterPlugin.getPluginToolView().setClickListener(LiveLayoutCreaterFragment.this);
        }

        mDialogCreaterPlugin.getPluginToolView().dealIsBackCamera(getPushSDK().isBackCamera());

        mDialogCreaterPlugin.showBottom();
        getCreaterBusiness().requestInitPlugin();
    }

    /**
     * 隐藏主播插件菜单
     */
    protected void hideCreaterPlugin() {
        if (mDialogCreaterPlugin != null) {
            mDialogCreaterPlugin.dismiss();
        }
    }


    /**
     * 显示申请PK中窗口
     */
    protected void showApplyPKDialog() {
        dismissApplyPKDialog();
        mDialogApplyPK = new LiveApplyRequestPKDialog(getActivity());
        mDialogApplyPK.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                //取消申请PK
                getCreaterBusiness().cancelApplyPK();
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {

            }
        }).show();
    }

    /**
     * 隐藏申请PK中窗口
     */
    public void dismissApplyPKDialog() {
        if (mDialogApplyPK != null) {
            mDialogApplyPK.dismiss();
        }
    }

    //取消pk申请
    @Override
    public void onBsViewerCancelPKRequest(CustomMsgCancelPK msg) {
        //dismissApplyPKDialog();
    }

    @Override
    public void onBsViewerShowApplyPK(boolean show) {
        super.onBsViewerShowApplyPK(show);
        if (show) {
            showApplyPKDialog();
        } else {
            dismissApplyPKDialog();
        }
    }

    @Override
    public void onBsViewerApplyPKRejected(String msg) {

        super.onBsViewerApplyPKRejected(msg);
        if (mDialogApplyPK != null && mDialogApplyPK.isShowing()) {
            mDialogApplyPK.setTextContent(msg);
            mDialogApplyPK.startDismissRunnable(1000);
        }
    }

    @Override
    public void onBsViewerApplyPKAccept(String pkId) {

    }

    @Override
    public void onBsCreaterRequestInitPluginSuccess(App_plugin_initActModel actModel) {
        super.onBsCreaterRequestInitPluginSuccess(actModel);
        if (mDialogCreaterPlugin != null) {
            mDialogCreaterPlugin.onRequestInitPluginSuccess(actModel);
        }
    }

    @Override
    public void onClickMenuMusic(RoomPluginToolView view) {
//        if (getSdkType() == LiveConstant.LiveSdkType.KSY)
//        {
//            SDToast.showToast("亲，音乐模块后期开放，敬请期待...");
//            return;
//        }
        hideCreaterPlugin();

        Intent intent = new Intent(getContext(), LiveSongChooseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickMenuBeauty(RoomPluginToolView view) {
        showSetBeautyDialog();

        hideCreaterPlugin();
    }

    @Override
    public void onClickMenuMic(RoomPluginToolView view) {
        view.setSelected(!view.isSelected());
        boolean enable = view.isSelected();

        getPushSDK().enableMic(enable);
        mIsMuteMode = !enable;

        hideCreaterPlugin();
    }

    @Override
    public void onClickMenuSwitchCamera(RoomPluginToolView view) {
        view.setSelected(!view.isSelected());

        getPushSDK().switchCamera();

        hideCreaterPlugin();
    }


    @Override
    public void onClickMenuFlashLight(RoomPluginToolView view) {
        if (getPushSDK().isBackCamera()) {
            view.setSelected(!view.isSelected());
            boolean enable = view.isSelected();

            getPushSDK().enableFlashLight(enable);

            hideCreaterPlugin();
        } else {
            SDToast.showToast("暂时只支持后置摄像头打开闪关灯");
        }
    }

    @Override
    public void onClickMenuPushMirror(RoomPluginToolView view) {
        getPushSDK().setMirror(!getPushSDK().isMirror());
        view.setSelected(getPushSDK().isMirror());
        if (getPushSDK().isMirror()) {
            SDToast.showToast("观众与你看到的是一样的了");
        } else {
            SDToast.showToast("观众与你看到的是相反的");
        }
        hideCreaterPlugin();
    }

    /**
     * 显示美颜设置窗口
     */
    protected void showSetBeautyDialog() {
        //子类实现
    }

    @Override
    protected void requestRoomInfo() {
        getLiveBusiness().requestRoomInfo(null);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);

        String shareType = actModel.getShare_type();
        if (!TextUtils.isEmpty(shareType)) {
            RoomShareModel share = actModel.getShare();
            if (share != null) {
                String title = share.getShare_title();
                String content = share.getShare_content();
                String imageUrl = share.getShare_imageUrl();
                String clickUrl = share.getShare_url();

                // 弹出分享页面
                if (shareType.equalsIgnoreCase(SHARE_MEDIA.WEIXIN.toString())) {
                    UmengSocialManager.shareWeixin(title, content, imageUrl, clickUrl, getActivity(), null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.WEIXIN_CIRCLE.toString())) {
                    UmengSocialManager.shareWeixinCircle(title, content, imageUrl, clickUrl, getActivity(), null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.QQ.toString())) {
                    UmengSocialManager.shareQQ(title, content, imageUrl, clickUrl, getActivity(), null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.QZONE.toString())) {
                    UmengSocialManager.shareQzone(title, content, imageUrl, clickUrl, getActivity(), null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.SINA.toString())) {
                    UmengSocialManager.shareSina(title, content, imageUrl, clickUrl, getActivity(), null);
                }
            }
        }
    }

    @Override
    public void onBsGuardNumChange(int guardnum) {

    }

    @Override
    public LiveCreaterBusiness.CreaterMonitorData onBsCreaterGetMonitorData() {
        LiveCreaterBusiness.CreaterMonitorData data = new LiveCreaterBusiness.CreaterMonitorData();
        data.roomId = getRoomId();
        data.viewerNumber = -1;
        data.ticketNumber = getLiveBusiness().getTicket();
        data.liveQualityData = getLiveBusiness().getLiveQualityData();
        return data;
    }

    @Override
    protected void bindShowShareView() {
        super.bindShowShareView();
        if (mRoomCreaterBottomView != null) {
            if (isPrivate() || UmengSocialManager.isAllSocialDisable()) {
                mRoomCreaterBottomView.showMenuShare(false);
            } else {
                mRoomCreaterBottomView.showMenuShare(true);
            }
        }
    }


    protected boolean isClosedBack() {
        return mIsClosedBack == 1;
    }

    protected void requestUpdateLiveStateFail() {
        getCreaterBusiness().requestUpdateLiveStateFail();
    }

    protected void requestUpdateLiveStateSuccess() {
        getCreaterBusiness().requestUpdateLiveStateSuccess();

        ECreateLiveSuccess event = new ECreateLiveSuccess();
        SDEventManager.post(event);
    }

    protected void requestUpdateLiveStateLeave() {
        getCreaterBusiness().requestUpdateLiveStateLeave();
    }

    protected void requestUpdateLiveStateComeback() {
        getCreaterBusiness().requestUpdateLiveStateComeback();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDialogCreaterPlugin != null) {
            mDialogCreaterPlugin.getPluginToolView().setFlashLightSelected(false);
        }
    }

    @Override
    public void onDestroy() {
        try {
            hideCreaterPlugin();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    //----------Banker end----------

    //点击付费按钮
    protected void onClickMenuPayMode(View v) {

    }

    //点击提档按钮
    protected void onClickMenuPayUpagrade(View v) {

    }

    //点击开启上庄
    protected void onClickMenuOpenBanker(View v) {

    }

    @Override
    protected void onHideBottomExtend() {
        super.onHideBottomExtend();
        mRoomCreaterBottomView.setMenuBottomExtendSwitchStateOpen();
    }

    @Override
    protected void onShowBottomExtend() {
        super.onShowBottomExtend();
        mRoomCreaterBottomView.setMenuBottomExtendSwitchStateClose();
    }

    @Override
    protected void showBottomExtendSwitch(boolean show) {
        super.showBottomExtendSwitch(show);
        mRoomCreaterBottomView.showMenuBottomExtendSwitch(show);
    }

    @Override
    protected void showBottomView(boolean show) {
        super.showBottomView(show);
        if (show) {
            SDViewUtil.setVisible(mRoomCreaterBottomView);
        } else {
            SDViewUtil.setInvisible(mRoomCreaterBottomView);
        }
    }

    @Override
    public void onMsgWarning(CustomMsgWarning msgWarning) {
        super.onMsgWarning(msgWarning);
        new AppDialogConfirm(getActivity()).setTextTitle("警告")
                .setTextContent(msgWarning.getDesc())
                .setTextCancel(null)
                .setTextConfirm("好的")
                .show();
    }


    //----------游戏庄家模块----------//
    @Override
    public void onBankerCtrlCreaterShowOpenBankerList(boolean show) {
        mRoomCreaterBottomView.onBankerCtrlCreaterShowOpenBankerList(show);
    }

    @Override
    public void onBankerCtrlCreaterShowOpenBanker(boolean show) {
        mRoomCreaterBottomView.onBankerCtrlCreaterShowOpenBanker(show);
    }

    @Override
    public void onBankerCtrlCreaterShowStopBanker(boolean show) {
        mRoomCreaterBottomView.onBankerCtrlCreaterShowStopBanker(show);
    }

    @Override
    public void onBsBankerCreaterShowHasViewerApplyBanker(boolean show) {
        mRoomCreaterBottomView.showMenuOpenBankerListUnread(show);
    }

    @Override
    public void onBsBankerShowBankerInfo(GameBankerModel banker) {

    }

    @Override
    public void onBsBankerRemoveBankerInfo() {

    }


    @Override
    public void onBankerCtrlViewerShowApplyBanker(boolean show) {

    }

    //----------游戏模块----------//
    @Override
    public void onGameCtrlShowStart(boolean show, int gameId) {
        mRoomCreaterBottomView.onGameCtrlShowStart(show, gameId);
    }

    @Override
    public void onGameCtrlShowWaiting(boolean show, int gameId) {
        mRoomCreaterBottomView.onGameCtrlShowWaiting(show, gameId);
    }

    @Override
    public void onGameCtrlShowClose(boolean show, int gameId) {
        mRoomCreaterBottomView.onGameCtrlShowClose(show, gameId);
    }

}
