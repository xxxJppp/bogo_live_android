package com.bogokj.live.room.anchor;

import android.view.View;

import com.bogokj.auction.AuctionBusiness;
import com.bogokj.auction.dialog.AuctionCreateAuctionDialog;
import com.bogokj.auction.model.App_pai_user_get_videoActModel;
import com.bogokj.auction.model.PaiBuyerModel;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionFail;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionOffer;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.bogokj.games.model.PluginModel;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.appview.room.RoomInfoView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.App_monitorActModel;
import com.bogokj.o2o.dialog.O2OShoppingPodCastDialog;
import com.bogokj.pay.LiveScenePayCreaterBusiness;
import com.bogokj.pay.LiveTimePayCreaterBusiness;
import com.bogokj.pay.dialog.LiveImportPriceDialog;
import com.bogokj.pay.dialog.LiveScenePriceDialog;
import com.bogokj.pay.model.App_live_live_payActModel;
import com.bogokj.pay.model.App_monitorLiveModel;
import com.bogokj.pay.room.RoomLivePayInfoCreaterView;
import com.bogokj.pay.room.RoomLiveScenePayInfoView;
import com.bogokj.shop.dialog.ShopMyStoreDialog;
import com.bogokj.shop.dialog.ShopPodcastGoodsDialog;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;

import static com.bogokj.live.LiveConstant.PluginClassName.P_LIVE_PAY;
import static com.bogokj.live.LiveConstant.PluginClassName.P_LIVE_PAY_SCENE;
import static com.bogokj.live.LiveConstant.PluginClassName.P_PAI;
import static com.bogokj.live.LiveConstant.PluginClassName.P_PODCAST_GOODS;
import static com.bogokj.live.LiveConstant.PluginClassName.P_SHOP;

/**
 * @author kn update
 * @description 主播界面 功能扩展
 * @time 2020/2/24
 */
public class LiveLayoutCreaterExtendFragment extends LiveLayoutCreaterFragment implements LiveTimePayCreaterBusiness.LiveTimePayCreaterBusinessListener, LiveScenePayCreaterBusiness.LiveScenePayCreaterBusinessListener, AuctionBusiness.AuctionBusinessListener {

    //-----------竞拍--------------
    private AuctionCreateAuctionDialog dialogCreateAuction;

    //-----------付费模式--------------
    private LiveTimePayCreaterBusiness timePayCreaterBusiness;

    private RoomLivePayInfoCreaterView roomLivePayInfoView;

    //-----------按场付费---------------
    private LiveScenePayCreaterBusiness scenePayCreaterBusiness;
    private RoomLiveScenePayInfoView roomLiveScenePayInfoView;

    private RoomInfoView roomInfoView;

    @Override
    protected void init() {
        super.init();
        timePayCreaterBusiness = new LiveTimePayCreaterBusiness(this);
        timePayCreaterBusiness.setBusinessListener(this);

        scenePayCreaterBusiness = new LiveScenePayCreaterBusiness(this);
        scenePayCreaterBusiness.setBusinessListener(this);

        //拍卖
//          getShopClassUtils().getAuctionBusiness().setAuctionBusinessListener(this);
    }


    @Override
    public void onBsCreaterRequestMonitorSuccess(App_monitorActModel actModel) {
        super.onBsCreaterRequestMonitorSuccess(actModel);
        //开启付费模式监听启动回调
        timePayCreaterBusiness.onRequestMonitorSuccess(actModel);
        scenePayCreaterBusiness.onRequestMonitorSuccess(actModel);
    }

    @Override
    public void onBsViewerApplyPKAccept(String pkId) {

    }

    /**
     * 点击普通插件
     *
     * @param model
     */
    @Override
    protected void onClickCreaterPluginNormal(PluginModel model) {
        super.onClickCreaterPluginNormal(model);
        if (P_PAI.equalsIgnoreCase(model.getClass_name())) {
            clickPai();
        } else if (P_SHOP.equalsIgnoreCase(model.getClass_name())) {
            clickShop();
        } else if (P_PODCAST_GOODS.equalsIgnoreCase(model.getClass_name())) {
            clickPodcast_goods();
        } else if (P_LIVE_PAY.equalsIgnoreCase(model.getClass_name())) {
            clickPayMode(model);
        } else if (P_LIVE_PAY_SCENE.equalsIgnoreCase(model.getClass_name())) {
            clickPayScene();
        }
    }

    @Override
    protected void onClickCreaterPluginGame(PluginModel model) {
        super.onClickCreaterPluginGame(model);
          getGameClassUtils().getGameBusiness().selectGame(model);
    }

    protected void clickPai() {
//          getShopClassUtils().auctionBusiness.requestCreateAuctionAuthority(null);
    }

    protected void clickShop() {
        if (AppRuntimeWorker.getIsOpenWebviewMain()) {
            O2OShoppingPodCastDialog dialog = new O2OShoppingPodCastDialog(getActivity(), getCreaterId());
            dialog.showBottom();
        } else {
            ShopMyStoreDialog dialog = new ShopMyStoreDialog(getActivity(), getCreaterId(), isCreater());
            dialog.showBottom();
        }
    }

    protected void clickPodcast_goods() {
        ShopPodcastGoodsDialog dialog = new ShopPodcastGoodsDialog(getActivity(), isCreater(), getCreaterId());
        dialog.showBottom();
    }

    protected void clickPayMode(PluginModel model) {
        if (model.getIs_active() == 1) {
            mRoomCreaterBottomView.showMenuPayMode(false);
        } else {
            clickSwitchPay();
            mRoomCreaterBottomView.showMenuPayMode(true);
        }
    }

    @Override
    public void onAuctionMsgCreateSuccess(CustomMsgAuctionCreateSuccess customMsg) {

    }

    @Override
    public void onAuctionMsgOffer(CustomMsgAuctionOffer customMsg) {

    }

    @Override
    public void onAuctionMsgSuccess(CustomMsgAuctionSuccess customMsg) {
//          getShopClassUtils().onAuctionMsgSuccess(customMsg);
    }

    @Override
    public void onAuctionMsgNotifyPay(CustomMsgAuctionNotifyPay customMsg) {
//          getShopClassUtils().onAuctionMsgNotifyPay(customMsg);
    }

    @Override
    public void onAuctionMsgFail(CustomMsgAuctionFail customMsg) {
//          getShopClassUtils().onAuctionMsgFail(customMsg);
    }

    @Override
    public void onAuctionMsgPaySuccess(CustomMsgAuctionPaySuccess customMsg) {
//          getShopClassUtils().onAuctionMsgPaySuccess(customMsg);
    }

    @Override
    public void onAuctionPayRemaining(PaiBuyerModel buyer, long day, long hour, long min, long sec) {

    }

    @Override
    public void onAuctionNeedShowPay(boolean show) {

    }

    @Override
    public void onAuctionPayClick(View v) {

    }

    @Override
    public void onAuctioningChange(boolean isAuctioning) {

    }

    @Override
    public void onAuctionRequestPaiInfoSuccess(App_pai_user_get_videoActModel actModel) {
//          getShopClassUtils().onAuctionRequestPaiInfoSuccess(actModel);
    }

    //-----------竞拍--------------
    @Override
    public void onAuctionRequestCreateAuthoritySuccess() {
        if (dialogCreateAuction == null) {
            dialogCreateAuction = new AuctionCreateAuctionDialog(getActivity(), AppRuntimeWorker.getPai_virtual_btn(), AppRuntimeWorker.getPai_real_btn(), getCreaterId());
        }
        dialogCreateAuction.showBottom();
    }

    @Override
    public void onAuctionRequestCreateAuthorityError(String msg) {
        new AppDialogConfirm(getActivity()).setTextContent(msg)
                .setTextConfirm("确定")
                .setTextCancel(null)
                .show();
    }

    //付费直播start=========================

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);
        //正在付费
        LogUtil.e("LiveLayoutCreaterExtendActivity====onLiveRequestRoomInfoSuccess");
        if (actModel.getLive_fee() > 0) {
            LogUtil.e("LiveLayoutCreaterExtendActivity====Live_fee=" + actModel.getLive_fee());
            //按场付费
            if (actModel.getLive_pay_type() == 1) {
                addRoomLiveScenePayInfoView();
                roomLiveScenePayInfoView.bindData(actModel.getLive_fee());
            } else {
                addLivePayModeView();
                roomLivePayInfoView.bindData(actModel.getLive_fee());
            }
        }
    }

    //按时付费start========================
    @Override
    protected void onClickCreaterPlugin(PluginModel model) {
        super.onClickCreaterPlugin(model);
        mRoomCreaterBottomView.showMenuPayMode(false);
    }

    @Override
    public void onTimePayCreaterRequestLiveLive_paySuccess(App_live_live_payActModel actModel) {
        if (roomLivePayInfoView != null) {
            roomLivePayInfoView.bindData(actModel.getLive_fee());
        }
    }

    @Override
    public void onTimePayCreaterShowHideUpgrade(boolean show) {
        mRoomCreaterBottomView.showMenuPayModeUpgrade(show);
    }

    @Override
    public void onTimePayCreaterShowHideSwitchPay(boolean show) {
        mRoomCreaterBottomView.showMenuPayMode(show);
    }

    @Override
    public void onTimePayCreaterCountDown(long leftTime) {
        if (roomLivePayInfoView != null) {
            roomLivePayInfoView.setPayInfoCountDownTime(leftTime);
        }
    }

    @Override
    public void onTimePayCreaterShowPayModeView() {
        addLivePayModeView();
    }

    @Override
    public void onTimePayCreaterRequestMonitorSuccess(App_monitorLiveModel actModel) {
        //主播刷新印票
        getLiveBusiness().setTicket(actModel.getTicket());
        if (roomLivePayInfoView != null) {
            //设置付费人数
            roomLivePayInfoView.setViewerNum(actModel.getLive_viewer());
        }
    }


    private void addLivePayModeView() {
        if (roomLivePayInfoView == null) {
            roomLivePayInfoView = new RoomLivePayInfoCreaterView(getActivity());
            replaceView(fl_live_pay_mode, roomLivePayInfoView);
        }
    }

    @Override
    protected void onClickMenuPayMode(View v) {
        super.onClickMenuPayMode(v);
        clickSwitchPay();
    }

    @Override
    protected void onClickMenuPayUpagrade(View v) {
        super.onClickMenuPayUpagrade(v);
        onClickUpagrade();
    }

    private void clickSwitchPay() {
        final LiveImportPriceDialog liveImportPriceDialog = new LiveImportPriceDialog(getActivity());
        liveImportPriceDialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                dialog.dismiss();
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                if (!timePayCreaterBusiness.isAllowLivePay()) {
                    SDToast.showToast("亲!您未达到付费条件!");
                    return;
                }
                int price = liveImportPriceDialog.getImportPrice();
                if (price < liveImportPriceDialog.getLive_pay_min()) {
                    SDToast.showToast("不能低于" + liveImportPriceDialog.getLive_pay_min());
                    liveImportPriceDialog.resetMinPrice();
                    return;
                }
                timePayCreaterBusiness.requestSwitchPayMode(liveImportPriceDialog.getImportPrice());
                dialog.dismiss();
            }
        });
        liveImportPriceDialog.showBottom();
    }

    private void onClickUpagrade() {
        AppDialogConfirm dialog = new AppDialogConfirm(getActivity());
        dialog.setTextTitle("按时付费提档");
        dialog.setTextContent("确定提档？");
        dialog.setCancelable(false);
        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                timePayCreaterBusiness.requestPayModeUpgrade();
            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timePayCreaterBusiness != null) {
            timePayCreaterBusiness.onDestroy();
        }
    }
    //按时付费 end====================================
    //按场付费start===================================

    protected void clickPayScene() {
        final LiveScenePriceDialog liveScenePriceDialog = new LiveScenePriceDialog(getActivity());
        liveScenePriceDialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                dialog.dismiss();
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                int price = liveScenePriceDialog.getImportPrice();
                if (price < liveScenePriceDialog.getLive_pay_scene_min()) {
                    SDToast.showToast("不能低于" + liveScenePriceDialog.getLive_pay_scene_min());
                    liveScenePriceDialog.resetMinPrice();
                    return;
                }
                scenePayCreaterBusiness.requestPayScene(price);
                dialog.dismiss();
            }
        });
        liveScenePriceDialog.showBottom();
    }

    @Override
    public void onScenePayCreaterShowView() {
        addRoomLiveScenePayInfoView();
    }

    @Override
    public void onScenePayCreaterSuccess(App_live_live_payActModel actModel) {
        roomLiveScenePayInfoView.bindData(actModel.getLive_fee());
    }

    @Override
    public void onScenePayCreaterRequestMonitorSuccess(App_monitorLiveModel app_monitorActModel) {
        getLiveBusiness().setTicket(app_monitorActModel.getTicket());
        if (roomLiveScenePayInfoView != null) {
            roomLiveScenePayInfoView.setScenePayLiveViewerNum(app_monitorActModel.getLive_viewer());
        }
    }

    private void addRoomLiveScenePayInfoView() {
        if (roomLiveScenePayInfoView == null) {
            roomLiveScenePayInfoView = new RoomLiveScenePayInfoView(getActivity());
            replaceView(fl_live_pay_mode, roomLiveScenePayInfoView);
        }
    }
    //按场付费end===================================
    //付费直播end===================================


}
