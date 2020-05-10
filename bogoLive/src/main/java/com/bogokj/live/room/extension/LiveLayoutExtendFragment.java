//package com.bogokj.live.room.extension;
//
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.bogokj.auction.AuctionBusiness;
//import com.bogokj.auction.appview.AuctionUserRanklistView;
//import com.bogokj.auction.appview.room.RoomAuctionInfoCountdownView;
//import com.bogokj.auction.appview.room.RoomAuctionInfoView;
//import com.bogokj.auction.dialog.AuctionResultsFailDialog;
//import com.bogokj.auction.dialog.AuctionResultsNotifyPayDialog;
//import com.bogokj.auction.dialog.AuctionResultsPaySucDialog;
//import com.bogokj.auction.dialog.AuctionResultsSucDialog;
//import com.bogokj.auction.model.App_pai_user_get_videoActModel;
//import com.bogokj.auction.model.PaiBuyerModel;
//import com.bogokj.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
//import com.bogokj.auction.model.custommsg.CustomMsgAuctionFail;
//import com.bogokj.auction.model.custommsg.CustomMsgAuctionNotifyPay;
//import com.bogokj.auction.model.custommsg.CustomMsgAuctionOffer;
//import com.bogokj.auction.model.custommsg.CustomMsgAuctionPaySuccess;
//import com.bogokj.auction.model.custommsg.CustomMsgAuctionSuccess;
//import com.bogokj.library.utils.LogUtil;
//import com.bogokj.library.utils.SDViewUtil;
//import com.bogokj.live.R;
//import com.bogokj.live.common.AppRuntimeWorker;
//import com.bogokj.live.model.App_get_videoActModel;
//import com.bogokj.live.model.custommsg.MsgModel;
//import com.bogokj.live.room.LiveLayoutFragment;
//import com.bogokj.o2o.event.O2OEShoppingCartDialogShowing;
//import com.bogokj.shop.ShopBusiness;
//import com.bogokj.shop.appview.room.RoomShopGoodsPushView;
//import com.bogokj.shop.dialog.ShopGoodsPurchaseSucDialog;
//import com.bogokj.shop.model.custommsg.CustomMsgShopBuySuc;
//import com.bogokj.shop.model.custommsg.CustomMsgShopPush;
//
//import java.util.List;
//
///**
// * @author kn
// * @description: 直播间 购物业务父类
// * <p>
// * 此业务类未测试 主播用户公共方法请在LiveLayoutFragment 里实现
// * @time kn 2019/12/21
// */
//public class LiveLayoutExtendFragment extends LiveLayoutFragment implements AuctionBusiness.AuctionBusinessListener, ShopBusiness.ShopBusinessListener {
//
//    //-----------竞拍------------
//    public AuctionBusiness auctionBusiness;
//
//    protected RoomAuctionInfoView roomAuctionInfoView;//竞拍信息
//    protected RoomAuctionInfoCountdownView roomAuctionInfoCountdownView;//竞拍倒计时
//    protected AuctionUserRanklistView auctionUserRanklistView;//付款排行榜
//
//
//    private AuctionResultsNotifyPayDialog dialogResult;
//    private AuctionResultsSucDialog dialogResultSuc;
//    private AuctionResultsPaySucDialog dialogResultPaySuc;
//    private AuctionResultsFailDialog dialogResultFail;
//
//    //-----------购物------------
//    protected ShopBusiness liveShopBusiness;
//
//    protected RoomShopGoodsPushView roomShopGoodsPushView;
//
//
//    protected ShopGoodsPurchaseSucDialog shopGoodsPurchaseSucDialog;
//
//    @Override
//    protected void init() {
//        super.init();
//
//        //-----------竞拍------------
//        auctionBusiness = new AuctionBusiness();
//        auctionBusiness.setAuctionBusinessListener(this);
//
//        //-----------购物------------
//        liveShopBusiness = new ShopBusiness(this);
//        liveShopBusiness.setShopBusinessListener(this);
//    }
//
//
//    public AuctionBusiness getAuctionBusiness() {
//        if (auctionBusiness == null) {
//            auctionBusiness = new AuctionBusiness();
//            auctionBusiness.setAuctionBusinessListener(this);
//        }
//        return auctionBusiness;
//    }
//
//    public ShopBusiness getShopBusiness() {
//        if (liveShopBusiness == null) {
//            liveShopBusiness = new ShopBusiness(this);
//            liveShopBusiness.setShopBusinessListener(this);
//        }
//        return liveShopBusiness;
//    }
//
//    @Override
//    protected void initLayout(View view) {
//        super.initLayout(view);
//
//    }
//
//    @Override
//    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
//        super.onBsRequestRoomInfoSuccess(actModel);
//        //在开启竞拍功能的情况下进入直播间请求拍卖信息
//        if (AppRuntimeWorker.getShow_hide_pai_view() == 1) {
//            int paiId = actModel.getPai_id();
//            if (paiId > 0) {
//                auctionBusiness.requestPaiInfo(paiId, null);
//            }
//        }
//    }
//
//    /**
//     * 添加竞拍信息
//     */
//    protected void addLiveAuctionInfo(App_pai_user_get_videoActModel app_pai_user_get_videoActModel) {
//        roomAuctionInfoView.setVisibility(View.VISIBLE);
//        roomAuctionInfoView.bindAuctionDetailInfo(app_pai_user_get_videoActModel);
////
////        roomAuctionInfoCountdownView = new RoomAuctionInfoCountdownView(getActivity());
////        replaceView(fl_live_auction_countdown, roomAuctionInfoCountdownView);
//
//        roomAuctionInfoCountdownView.bindAuctionDetailInfo(app_pai_user_get_videoActModel);
//    }
//
//    /**
//     * 添加竞拍付款排行榜
//     */
//    protected void addLiveAuctionRankList(List<PaiBuyerModel> listBuyers) {
////        auctionUserRanklistView = new AuctionUserRanklistView(getActivity());
////        replaceView(fl_live_auction_rank_list, auctionUserRanklistView);
////        auctionUserRanklistView.setBuyers(listBuyers);
//    }
//
//    @Override
//    public void onMsgAuction(MsgModel msg) {
//        super.onMsgAuction(msg);
//        auctionBusiness.onAuctionMsg(msg);
//    }
//
//
//    @Override
//    public void onMsgShop(MsgModel msg) {
//        super.onMsgShop(msg);
//        liveShopBusiness.onMsgShop(msg);
//    }
//
//
//    public void removeView(View view) {
//        SDViewUtil.removeView(view);
//    }
//
//    public void replaceView(ViewGroup parent, View child) {
//        SDViewUtil.replaceView(parent, child);
//    }
//
//    /**
//     * 观众添加购物商品推送
//     *
//     * @param customMsgShopPush
//     */
//    private void addLiveShopGoodsPushView(CustomMsgShopPush customMsgShopPush) {
//        roomShopGoodsPushView = new RoomShopGoodsPushView(getActivity(), getCreaterId(), customMsgShopPush.getGoods().getGoods_id());
//        replaceView(fl_live_goods_push, roomShopGoodsPushView);
//        roomShopGoodsPushView.bindData(customMsgShopPush);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        auctionBusiness.onDestroy();
//    }
//
//    protected void changeAvVideoGlviewLayout() {
//        SDViewUtil.setGone(mRoomInfoView);
//        SDViewUtil.setHeight(findViewById(R.id.view_video), SDViewUtil.getScreenHeight() / 3);
//        SDViewUtil.setWidth(findViewById(R.id.view_video), SDViewUtil.getScreenWidth() / 3);
//        SDViewUtil.setGone(fl_live_goods_push);
//    }
//
//    protected void revertAvVideoGlviewLayout() {
//        SDViewUtil.setVisible(mRoomInfoView);
//        SDViewUtil.setHeight(findViewById(R.id.view_video), SDViewUtil.getScreenHeight());
//        SDViewUtil.setWidth(findViewById(R.id.view_video), SDViewUtil.getScreenWidth());
//        SDViewUtil.setVisible(fl_live_goods_push);
//    }
//
//    //实体产品列表弹出事件
//    public void onEventMainThread(O2OEShoppingCartDialogShowing event) {
//        if (event.isShowing) {
//            changeAvVideoGlviewLayout();
//        } else {
//            revertAvVideoGlviewLayout();
//        }
//    }
//
//    @Override
//    public void onBsViewerApplyPKRejected(String msg) {
//
//    }
//
//
//    //------------购物--------------//
//
//    @Override
//    public void onShopMsgShopPush(CustomMsgShopPush customMsgShopPush) {
//        addLiveShopGoodsPushView(customMsgShopPush);
//    }
//
//    @Override
//    public void onShopMsgShopBuySuc(CustomMsgShopBuySuc customMsgShopBuySuc) {
//        if (shopGoodsPurchaseSucDialog == null) {
//            shopGoodsPurchaseSucDialog = new ShopGoodsPurchaseSucDialog(getActivity());
//        }
//        if (shopGoodsPurchaseSucDialog.isShowing()) {
//            shopGoodsPurchaseSucDialog.dismiss();
//        }
//        shopGoodsPurchaseSucDialog.initData(customMsgShopBuySuc);
//        shopGoodsPurchaseSucDialog.showCenter();
//    }
//
//    @Override
//    public void onShopCountdownEnd() {
//        if (roomShopGoodsPushView != null) {
//            removeView(roomShopGoodsPushView);
//        }
//        if (shopGoodsPurchaseSucDialog != null && shopGoodsPurchaseSucDialog.isShowing()) {
//            shopGoodsPurchaseSucDialog.dismiss();
//        }
//    }
//
//    //------------竞拍--------------//
//
//    @Override
//    public void onAuctionMsgCreateSuccess(CustomMsgAuctionCreateSuccess customMsg) {
//        //竞拍创建成功
//        LogUtil.i("onAuctionMsgCreateSuccess");
//    }
//
//    @Override
//    public void onAuctionMsgOffer(CustomMsgAuctionOffer customMsg) {
//        //竞拍出价
//        LogUtil.i("onAuctionMsgOffer");
//    }
//
//    @Override
//    public void onAuctionMsgSuccess(CustomMsgAuctionSuccess customMsg) {
//        //竞拍成功
//        LogUtil.i("onAuctionMsgSuccess");
//        if (dialogResultSuc != null)
//            dialogResultSuc.dismiss();
//
//        dialogResultSuc = new AuctionResultsSucDialog(getActivity(), customMsg, auctionBusiness);
//        dialogResultSuc.showCenter();
//
//        addLiveAuctionRankList(customMsg.getBuyer());
//    }
//
//    @Override
//    public void onAuctionMsgNotifyPay(CustomMsgAuctionNotifyPay customMsg) {
//        //竞拍通知付款，比如第一名超时未付款，通知下一名付款
//        LogUtil.i("onAuctionMsgNotifyPay");
//        List<PaiBuyerModel> listBuyers = customMsg.getBuyer();
//        if (listBuyers == null || listBuyers.size() == 0) {
//            return;
//        }
//        addLiveAuctionRankList(listBuyers);
//
//        if (dialogResultSuc != null)
//            dialogResultSuc.dismiss();
//
//        if (dialogResult != null)
//            dialogResult.dismiss();
//
//        dialogResult = new AuctionResultsNotifyPayDialog(getActivity(), customMsg, auctionBusiness);
//        dialogResult.showCenter();
//    }
//
//    @Override
//    public void onAuctionMsgFail(CustomMsgAuctionFail customMsg) {
//        // 流拍
//        LogUtil.i("onAuctionMsgFail");
//        if (dialogResult != null) {
//            dialogResult.dismiss();
//        }
//
//        if (dialogResultSuc != null) {
//            dialogResultSuc.dismiss();
//        }
//
//        if (dialogResultFail != null) {
//            dialogResultFail.dismiss();
//        }
//
//        dialogResultFail = new AuctionResultsFailDialog(getActivity(), customMsg);
//        dialogResultFail.showCenter();
//
//        removeView(roomAuctionInfoView);
//        removeView(roomAuctionInfoCountdownView);
//        removeView(auctionUserRanklistView);
//    }
//
//    @Override
//    public void onAuctionMsgPaySuccess(CustomMsgAuctionPaySuccess customMsg) {
//        // 支付成功
//        LogUtil.i("onAuctionMsgPaySuccess");
//        if (dialogResultSuc != null)
//            dialogResultSuc.dismiss();
//
//        if (dialogResult != null)
//            dialogResult.dismiss();
//
//        if (dialogResultPaySuc != null)
//            dialogResultPaySuc.dismiss();
//
//        dialogResultPaySuc = new AuctionResultsPaySucDialog(getActivity(), customMsg);
//        dialogResultPaySuc.showCenter();
//
//        removeView(roomAuctionInfoView);
//        removeView(roomAuctionInfoCountdownView);
//        removeView(auctionUserRanklistView);
//    }
//
//    @Override
//    public void onAuctionPayRemaining(PaiBuyerModel buyer, long day, long hour, long min, long sec) {
//        //竞拍通知付款倒计时
//        LogUtil.i("onAuctionPayRemaining:" + min + ":" + sec);
//    }
//
//    @Override
//    public void onAuctionNeedShowPay(boolean show) {
//        //否显示竞拍支付入口
//        LogUtil.i("onAuctionNeedShowPay:" + show);
//    }
//
//    @Override
//    public void onAuctionPayClick(View v) {
//        // 竞拍支付入口被点击
//        LogUtil.i("onAuctionPayClick");
//    }
//
//    @Override
//    public void onAuctioningChange(boolean isAuctioning) {
//        //竞拍状态发生变化
//        LogUtil.i("onAuctioningChange:" + isAuctioning);
//    }
//
//    @Override
//    public void onAuctionRequestPaiInfoSuccess(App_pai_user_get_videoActModel actModel) {
//        //竞拍信息请求成功
//        LogUtil.i("onAuctionRequestPaiInfoSuccess");
//
//        addLiveAuctionInfo(actModel);
//        addLiveAuctionRankList(actModel.getData().getBuyer());
//    }
//
//    @Override
//    public void onAuctionRequestCreateAuthoritySuccess() {
//        //验证创建竞拍权限成功
//        LogUtil.i("onAuctionRequestCreateAuthoritySuccess");
//    }
//
//    @Override
//    public void onAuctionRequestCreateAuthorityError(String msg) {
//        //验证创建竞拍权限失败
//        LogUtil.i("onAuctionRequestCreateAuthorityError:" + msg);
//    }
//
//}
