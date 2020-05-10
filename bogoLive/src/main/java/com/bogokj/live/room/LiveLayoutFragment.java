package com.bogokj.live.room;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.games.GameBusiness;
import com.bogokj.games.constant.GameType;
import com.bogokj.games.model.App_requestGameIncomeActModel;
import com.bogokj.games.model.App_startGameActModel;
import com.bogokj.games.model.PluginModel;
import com.bogokj.games.model.custommsg.GameMsgModel;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.libgame.dice.view.DiceGameView;
import com.bogokj.libgame.poker.view.PokerGameView;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.SDReplaceableLayout;
import com.bogokj.live.R;
import com.bogokj.live.appview.room.RoomBuyGuardianSuccessSVGAPlayView;
import com.bogokj.live.appview.room.RoomCarsSVGAPlayView;
import com.bogokj.live.appview.room.RoomCreaterBottomView;
import com.bogokj.live.appview.room.RoomGameBankerView;
import com.bogokj.live.appview.room.RoomGiftGifView;
import com.bogokj.live.appview.room.RoomGiftPlayView;
import com.bogokj.live.appview.room.RoomHeartView;
import com.bogokj.live.appview.room.RoomInfoView;
import com.bogokj.live.appview.room.RoomInviteFriendsView;
import com.bogokj.live.appview.room.RoomLuckGiftView;
import com.bogokj.live.appview.room.RoomMsgView;
import com.bogokj.live.appview.room.RoomPopMsgView;
import com.bogokj.live.appview.room.RoomRemoveViewerView;
import com.bogokj.live.appview.room.RoomSendGiftView;
import com.bogokj.live.appview.room.RoomSendMsgView;
import com.bogokj.live.appview.room.RoomViewerBottomView;
import com.bogokj.live.appview.room.RoomViewerJoinRoomView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.LiveAddViewerDialog;
import com.bogokj.live.dialog.LiveChatC2CDialog;
import com.bogokj.live.dialog.LiveRechargeDialog;
import com.bogokj.live.dialog.LiveRedEnvelopeNewDialog;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.App_plugin_statusActModel;
import com.bogokj.live.model.LiveQualityData;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.bogokj.live.model.custommsg.CustomMsgRedEnvelope;
import com.bogokj.live.model.custommsg.CustomMsgRefreshWish;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.room.activity.BaseLiveActivity;
import com.bogokj.live.room.extension.LiveLayoutGameExtendUtils;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * @author kn
 * @description: 直播间 基本功能父类
 * @time kn 2019/12/19
 */

public class LiveLayoutFragment extends LiveFragment implements GameBusiness.GameBusinessCallback, LiveLayoutGameExtendUtils.GameCallBackListener {

    protected ViewGroup fl_live_pay_mode;
//    protected RoomAuctionInfoView fl_live_auction_info;//竞拍信息
//    protected RoomAuctionInfoCountdownView fl_live_auction_countdown;//竞拍倒计时
//    protected AuctionUserRanklistView fl_live_auction_rank_list;//付款排行榜
//    protected ViewGroup fl_live_goods_push;//购物推送
    public ViewGroup ll_pay_bg_black;
    public RoomGameBankerView mGameBankerView;

    public SDReplaceableLayout fl_bottom_extend; // 底部扩展
    public RoomGiftPlayView mRoomGiftPlayView;
    public RoomInfoView mRoomInfoView;
    public RoomPopMsgView mRoomPopMsgView;
    public RoomViewerJoinRoomView mRoomViewerJoinRoomView;
    public RoomMsgView mRoomMsgView;
    public RoomSendMsgView mRoomSendMsgView;
    public RoomHeartView mRoomHeartView;
    public RoomGiftGifView mRoomGiftGifView;
    public RoomLuckGiftView mRoomLuckGiftView;
    private RoomCarsSVGAPlayView mRoomCarsView;
    private RoomBuyGuardianSuccessSVGAPlayView mRoomBuyGuardianSuccessSVGAPlayView;
    private RoomInviteFriendsView mRoomInviteFriendsView;
    private RoomRemoveViewerView mRoomRemoveViewerView;
    protected RoomViewerBottomView mRoomViewerBottomView;
    protected RoomCreaterBottomView mRoomCreaterBottomView;
    public RoomSendGiftView mRoomSendGiftView;


//    private LiveLayoutExtendFragment liveLayoutExtendFragment;
    private LiveLayoutGameExtendUtils liveLayoutGameExtendFragment;

    @Override
    protected void init() {
        super.init();

        // 关闭房间监听
        findViewById(R.id.view_close_room).setOnClickListener(v -> onClickCloseRoom(v));
        findViewById(R.id.fl_live_heart).setOnClickListener(view -> mRoomHeartView.addHeart());

        //设置游戏监听
        getGameClassUtils().getGameBusiness().setCallback(this);

    }


    protected void initLayout(View view) {
        ll_pay_bg_black = (ViewGroup) findViewById(R.id.ll_pay_bg_black);
        fl_live_pay_mode = (ViewGroup) findViewById(R.id.fl_live_pay_mode);
//        fl_live_auction_info = (RoomAuctionInfoView) findViewById(R.id.fl_live_auction_info);
//        fl_live_auction_countdown = (RoomAuctionInfoCountdownView) findViewById(R.id.fl_live_auction_countdown);
//        fl_live_auction_rank_list = (AuctionUserRanklistView) findViewById(R.id.fl_live_auction_rank_list);
//        fl_live_goods_push = (ViewGroup) findViewById(R.id.fl_live_goods_push);
        mGameBankerView = (RoomGameBankerView) findViewById(R.id.fl_container_banker);

        //礼物横向弹出
        mRoomGiftPlayView = (RoomGiftPlayView) findViewById(R.id.fl_live_gift_play);
        //gif动画
        mRoomGiftGifView = (RoomGiftGifView) findViewById(R.id.fl_live_gift_gif);
        //弹幕
        mRoomPopMsgView = (RoomPopMsgView) findViewById(R.id.fl_live_pop_msg);
        //加入房间特效
        mRoomViewerJoinRoomView = (RoomViewerJoinRoomView) findViewById(R.id.fl_live_viewer_join_room);
        //公屏消息
        mRoomMsgView = (RoomMsgView) findViewById(R.id.fl_live_msg);

        //发送消息view
        mRoomSendMsgView = (RoomSendMsgView) findViewById(R.id.fl_live_send_msg);
        mRoomSendMsgView.addVisibilityCallback((view1, visibility) -> {
            if (View.VISIBLE == visibility) {
                onLiveShowSendMsgView(view1);
            } else {
                oLiveHideSendMsgView(view1);
            }
        });

        //点亮心心动画
        mRoomHeartView = (RoomHeartView) findViewById(R.id.fl_live_heart);
        //幸运礼物动画
        mRoomLuckGiftView = (RoomLuckGiftView) findViewById(R.id.fl_live_luck_gift);

        //svga动画
        mRoomCarsView = (RoomCarsSVGAPlayView) findViewById(R.id.fl_live_cars);
        mRoomBuyGuardianSuccessSVGAPlayView = (RoomBuyGuardianSuccessSVGAPlayView) findViewById(R.id.fl_live_bug_guardian);

        //房间信息
        mRoomInfoView = (RoomInfoView) findViewById(R.id.fl_live_room_info);
        mRoomInfoView.setClickListener(roomInfoClickListener);

        //用户底部操作菜单
        mRoomViewerBottomView = (RoomViewerBottomView) findViewById(R.id.fl_live_bottom_menu_view);
        //主播底部操作菜单
        mRoomCreaterBottomView = (RoomCreaterBottomView) findViewById(R.id.fl_live_bottom_menu_create);
        //送礼物
        mRoomSendGiftView = (RoomSendGiftView) findViewById(R.id.fl_live_send_gift);

        //存放斗牛等控件
        fl_bottom_extend = (SDReplaceableLayout) findViewById(R.id.fl_bottom_extend);
        fl_bottom_extend.addCallback(new SDReplaceableLayout.SDReplaceableLayoutCallback() {
            @Override
            public void onContentReplaced(View view) {
                showLiveBottomExtendSwitch(true);
                onContentVisibilityChanged(view, view.getVisibility());
            }

            @Override
            public void onContentRemoved(View view) {
                showLiveBottomExtendSwitch(false);
            }

            @Override
            public void onContentVisibilityChanged(View view, int visibility) {
                if (View.VISIBLE == visibility) {
                    onShowLiveBottomExtend();
                } else {
                    onHideLiveBottomExtend();
                }
            }
        });

        
        addRoomBottomView(view);

    }


//    /**
//     * 初始化购物模块扩展 未联调
//     */
//    public LiveLayoutExtendFragment getShopClassUtils() {
//        if (liveLayoutExtendFragment == null) {
//            return liveLayoutExtendFragment = new LiveLayoutExtendFragment();
//        } else {
//            return liveLayoutExtendFragment;
//        }
//    }

    /**
     * 初始化游戏模块扩展
     */
    public LiveLayoutGameExtendUtils getGameClassUtils() {
        if (liveLayoutGameExtendFragment == null) {
            liveLayoutGameExtendFragment = new LiveLayoutGameExtendUtils(getActivity(), this, mGameBankerView);
            liveLayoutGameExtendFragment.setGameCallBackListener(this);
            return liveLayoutGameExtendFragment;
        } else {
            return liveLayoutGameExtendFragment;
        }
    }


    /**
     * 替换底部扩展
     *
     * @param view
     */
    protected void replaceBottomExtend(View view) {
        fl_bottom_extend.replaceContent(view);
    }

    /**
     * 切换显示隐藏底部扩展
     */
    protected void toggleBottomExtend() {
        fl_bottom_extend.toggleContentVisibleOrGone();
    }

    /**
     * 底部扩展显示回调
     */
    protected void onShowBottomExtend() {
        LogUtil.i("onShowBottomExtend");
    }

    /**
     * 底部扩展隐藏回调
     */
    protected void onHideBottomExtend() {
        LogUtil.i("onHideBottomExtend");
    }

    /**
     * 显示隐藏底部扩展开关
     *
     * @param show
     */
    protected void showBottomExtendSwitch(boolean show) {
        // 子类实现
        LogUtil.i("showBottomExtendSwitch:" + show);
    }

    /**
     * 发送礼物的view显示
     */
    protected void onShowSendGiftView() {
        showBottomView(false);
        showMsgView(false);
    }

    /**
     * 发送礼物的view隐藏
     */
    protected void onHideSendGiftView() {
        showBottomView(true);
        showMsgView(true);
    }

    /**
     * 发送消息的view显示
     *
     * @param view
     */
    public void onShowSendMsgView(View view) {
        showBottomView(false);
    }

    /**
     * 发送消息的view隐藏
     *
     * @param view
     */
    protected void onHideSendMsgView(View view) {
        showBottomView(true);
    }


    /**
     * 底部菜单
     */
    protected void addRoomBottomView(View view) {
        //子类实现
    }

    /**
     * 私密直播踢人
     */
    protected void addRoomPrivateRemoveViewerView() {
        ToastUtil.toastShortMessage("私密直播踢人");
        roomPrivateRemoveViewerView(getActivity(), getRoomId());
    }


    /**
     * 结束界面
     */
    protected void addLiveFinish() {
        // 子类实现
    }

    /**
     * 房间信息view点击监听
     */
    private RoomInfoView.ClickListener roomInfoClickListener = new RoomInfoView.ClickListener() {
        @Override
        public void onClickAddViewer(View v) {
            LiveLayoutFragment.this.onClickAddViewer(v);
        }

        @Override
        public void onClickMinusViewer(View v) {
            LiveLayoutFragment.this.onClickMinusViewer(v);
        }
    };

    /**
     * 私密直播点击加号加人
     *
     * @param v
     */
    protected void onClickAddViewer(View v) {
        LiveAddViewerDialog dialog = new LiveAddViewerDialog(getActivity(), getRoomInfo().getPrivate_share());
        dialog.setCallback(new LiveAddViewerDialog.Callback() {
            @Override
            public void onClickShareFriends(View v) {
                ToastUtil.toastShortMessage("私密直播加人");
                roomPrivateAddViewerView(getActivity(), getRoomId());

            }
        });
        dialog.showBottom();
    }


    private int oldRoomId;

    /**
     * 私密直播踢人
     *
     * @param context
     */
    public void roomPrivateRemoveViewerView(Activity context, int roomId) {

        if (mRoomRemoveViewerView == null || oldRoomId != roomId) {
            mRoomRemoveViewerView = new RoomRemoveViewerView(context);
            mRoomRemoveViewerView.setRoomId(roomId);
            mRoomRemoveViewerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    mRoomRemoveViewerView.requestData(false);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    mRoomRemoveViewerView = null;
                }
            });
            ((BaseLiveActivity) context).addView(mRoomRemoveViewerView);

            oldRoomId = roomId;
        }
    }

    /**
     * 私密直播加人
     *
     * @param context
     */
    public void roomPrivateAddViewerView(Activity context, int roomId) {
        if (mRoomInviteFriendsView == null || oldRoomId != roomId) {
            mRoomInviteFriendsView = new RoomInviteFriendsView(context);
            mRoomInviteFriendsView.setRoomId(roomId);
            mRoomInviteFriendsView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    mRoomInviteFriendsView.requestData(false);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    mRoomInviteFriendsView = null;
                }
            });
            ((BaseLiveActivity) context).addView(mRoomInviteFriendsView);
            oldRoomId = roomId;
        }
    }


    /**
     * 私密直播点击减号踢人
     *
     * @param v
     */
    protected void onClickMinusViewer(View v) {
        addRoomPrivateRemoveViewerView();
    }

    @Override
    public void onBsRefreshViewerList(List<UserModel> listModel) {
        super.onBsRefreshViewerList(listModel);
        mRoomInfoView.onLiveRefreshViewerList(listModel);
    }

    @Override
    public void onBsRemoveViewer(UserModel model) {
        super.onBsRemoveViewer(model);
        mRoomInfoView.onLiveRemoveViewer(model);
    }

    @Override
    public void onBsInsertViewer(int position, UserModel model) {
        super.onBsInsertViewer(position, model);
        mRoomInfoView.onLiveInsertViewer(position, model);
    }

    @Override
    public void onBsTicketChange(long ticket) {
        super.onBsTicketChange(ticket);
        mRoomInfoView.updateTicket(ticket);
    }

    @Override
    public void onBsViewerNumberChange(int viewerNumber) {
        super.onBsViewerNumberChange(viewerNumber);
        mRoomInfoView.updateViewerNumber(viewerNumber);
    }

    @Override
    public void onBsBindCreaterData(UserModel model) {
        super.onBsBindCreaterData(model);
        mRoomInfoView.bindCreaterData(model);
    }

    @Override
    public void onBsShowOperateViewer(boolean show) {
        super.onBsShowOperateViewer(show);
        mRoomInfoView.showOperateViewerView(show);
    }

    @Override
    public void onBsUpdateLiveQualityData(LiveQualityData data) {
        super.onBsUpdateLiveQualityData(data);
        mRoomInfoView.getSdkInfoView().updateLiveQuality(data);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);

        mRoomInfoView.bindData(actModel);
        bindShowShareView();
        getLiveBusiness().startLiveQualityLooper(getContext());
    }

    /**
     * 显示充值窗口
     * 整合到基类？
     */
    protected void showRechargeDialog() {
        LiveRechargeDialog dialog = new LiveRechargeDialog(getActivity());
        dialog.show();
    }

    /**
     * 点击关闭
     *
     * @param v
     */
    protected void onClickCloseRoom(View v) {
        //子类实现
    }

    /**
     * 点击打开发送窗口
     *
     * @param v
     */
    protected void onClickMenuSendMsg(View v) {
        showSendMsgView(true);
    }

    @Override
    public void openSendMsg(String content) {
        super.openSendMsg(content);
        showSendMsgView(true);
        mRoomSendMsgView.setContent(content);
    }

    /**
     * 点击私聊消息
     *
     * @param v
     */
    protected void onClickMenuPrivateMsg(View v) {
        LiveChatC2CDialog dialog = new LiveChatC2CDialog(getActivity());
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.showBottom();
    }

    /**
     * 绑定是否显示分享view
     */
    protected void bindShowShareView() {
    }

    /**
     * 主播插件被点击
     */
    protected void onClickCreaterPlugin(final PluginModel model) {
        if (model == null) {
            return;
        }
        CommonInterface.requestPlugin_status(model.getId(), new AppRequestCallback<App_plugin_statusActModel>() {
            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    if (actModel.getIs_enable() == 1) {
                        if (model.isNormalPlugin()) {
                            //点击普通插件
                            onClickCreaterPluginNormal(model);
                        } else if (model.isGamePlugin()) {
                            //点击游戏插件
                            onClickCreaterPluginGame(model);
                        }
                    }
                }
            }
        });

    }


    /**
     * 主播普通插件点击
     *
     * @param model
     */
    protected void onClickCreaterPluginNormal(PluginModel model) {

    }

    /**
     * 主播游戏插件点击
     *
     * @param model
     */
    protected void onClickCreaterPluginGame(PluginModel model) {

    }

    protected void replaceBankerView(View view) {
        ((BaseLiveActivity) getActivity()).replaceView(R.id.fl_container_banker, view);
    }

    /**
     * 点击分享
     *
     * @param v
     */
    protected void onClickMenuShare(View v) {
        openShare(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                getLiveBusiness().sendShareSuccessMsg();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        });
    }

    /**
     * 隐藏发送礼物
     */
    protected void hideSendGiftView() {

    }

    /**
     * 显示隐藏底部菜单view
     */
    protected void showBottomView(boolean show) {
        // 子类实现
    }

    /**
     * 显示隐藏消息列表
     */
    protected void showMsgView(boolean show) {
        if (show) {
            mRoomMsgView.getVisibilityHandler().setVisible(true);
        } else {
            mRoomMsgView.getVisibilityHandler().setInvisible(true);
        }
    }

    /**
     * 显示隐藏发送消息view
     */
    protected void showSendMsgView(boolean show) {
        if (show) {
            SDViewUtil.setVisible(mRoomSendMsgView);
        } else {
            SDViewUtil.setInvisible(mRoomSendMsgView);
        }
    }

    /**
     * 发送消息view是否可见
     *
     * @return
     */
    protected boolean isSendMsgViewVisible() {
        if (mRoomSendMsgView == null) {
            return false;
        }

        return mRoomSendMsgView.isVisible();
    }

    /**
     * 发送礼物view是否可见
     *
     * @return
     */
    protected boolean isSendGiftViewVisible() {
        return false;
    }

    @Override
    public void onMsgRedEnvelope(CustomMsgRedEnvelope msg) {
        super.onMsgRedEnvelope(msg);
        LiveRedEnvelopeNewDialog dialog = new LiveRedEnvelopeNewDialog(getActivity(), msg);
        dialog.show();
    }

    @Override
    public void onBsViewerShowCreaterLeave(boolean show) {
        super.onBsViewerShowCreaterLeave(show);
        if (isAuctioning()) {
            mRoomInfoView.showCreaterLeave(false);
        } else {
            mRoomInfoView.showCreaterLeave(show);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRoomInfoView != null) {
            mRoomInfoView.stopPlay();
            mRoomInfoView.dismissDialog();
        }

    }

    @Override
    public void onMsgGift(CustomMsgGift msg) {
        super.onMsgGift(msg);
        mRoomInfoView.refreshWish();
    }


    @Override
    public void OnMsgRefreshWish(CustomMsgRefreshWish msg) {
        //刷新心愿单
        mRoomInfoView.refreshWish();
    }


    /**
     * 收到游戏消息 39
     *
     * @param msg
     */
    @Override
    public void onMsgGame(MsgModel msg) {
        super.onMsgGame(msg);
        getGameClassUtils().getGameBusiness().onMsgGame(msg);
    }


    /**
     * 单例出来的回调
     *
     * @param view
     */

    public void onLiveShowSendMsgView(View view) {
        onShowSendMsgView(view);
    }


    public void oLiveHideSendMsgView(View view) {
        onHideSendMsgView(view);
    }


    public void showLiveBottomExtendSwitch(boolean b) {
        showBottomExtendSwitch(b);
    }


    public void onShowLiveBottomExtend() {
        onShowBottomExtend();
    }


    public void onHideLiveBottomExtend() {
        onHideBottomExtend();
    }


    public void removeView(View view) {
        SDViewUtil.removeView(view);
    }

    public void replaceView(ViewGroup parent, View child) {
        SDViewUtil.replaceView(parent, child);
    }


    /**
     * 游戏模块回调
     * <p>
     * 流程
     * 01.消息一般在此接收或子类实现此方法
     * 02.在此或子类方法中调用 对应 模块方法（对应模块中是有实现逻辑的）
     *
     * @param show
     * @param gameId
     */
    @Override
    public void onGameCtrlShowStart(boolean show, int gameId) {
        //主播端实现
    }

    @Override
    public void onGameCtrlShowWaiting(boolean show, int gameId) {
        //主播端实现
    }

    @Override
    public void onGameCtrlShowClose(boolean show, int gameId) {
        //主播端实现
    }

    @Override
    public void onGameInitPanel(GameMsgModel msg) {
        switch (msg.getGame_id()) {
            case GameType.GOLD_FLOWER://扎金
                getGameClassUtils().flowGameInit(msg, getContext());

                break;
            case GameType.BULL://斗牛
                getGameClassUtils().bullGameInit(msg, getContext());
                break;

            case GameType.DICE:
                getGameClassUtils().initDiceGameView(msg, getContext());
                break;
            default:
                break;
        }

        if (isSendMsgViewVisible() || isSendGiftViewVisible()) {
            getGameClassUtils().hideGamePanelView();
        }
    }


    @Override
    public void onGameRemovePanel() {
        getGameClassUtils().onGameRemovePanel();
    }

    @Override
    public void onGameMsg(GameMsgModel msg, boolean isPush) {
        getGameClassUtils().getBankerBusiness().onGameMsg(msg);
        getGameClassUtils().onGameMsg(msg, isPush);
    }

    @Override
    public void onGameMsgStopGame() {
        //关闭纸牌等游戏
        getGameClassUtils().onGameMsgStopGame();

    }

    @Override
    public void onGameRequestGameIncomeSuccess(App_requestGameIncomeActModel actModel) {
        getGameClassUtils().onGameRequestGameIncomeSuccess(actModel);

    }

    @Override
    public void onGameRequestStartGameSuccess(App_startGameActModel actModel) {
        //此处为请求开启游戏成功回调 不用管
    }

    @Override
    public void onGameRequestStopGameSuccess(BaseActModel actModel) {
        //此处为请求关闭游戏成功回调 不用管
    }

    @Override
    public void onGameHasAutoStartMode(boolean hasAutoStartMode) {
        getGameClassUtils().onGameHasAutoStartMode(hasAutoStartMode);
    }

    @Override
    public void onGameAutoStartModeChanged(boolean isAutoStartMode) {
        getGameClassUtils().onGameAutoStartModeChanged(isAutoStartMode);
    }

    @Override
    public void onGameUpdateGameCurrency(long value) {
        getGameClassUtils().onGameUpdateGameCurrency(value);
    }

    @Override
    public void showPokerGameView(PokerGameView mPokerGameView) {

    }

    @Override
    public void showDiceGameView(DiceGameView mDiceGameView) {
    }
}
