package com.bogokj.live.room.extension;

import android.app.Activity;
import android.view.View;

import com.bogokj.games.BankerBusiness;
import com.bogokj.games.GameBusiness;
import com.bogokj.games.dialog.GamesBankerDialog;
import com.bogokj.games.dialog.GamesBankerListDialog;
import com.bogokj.games.model.App_requestGameIncomeActModel;
import com.bogokj.games.model.App_startGameActModel;
import com.bogokj.games.model.GameBankerModel;
import com.bogokj.games.model.custommsg.GameMsgModel;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.appview.room.RoomGameBankerView;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.event.EUpdateUserInfo;
import com.bogokj.live.room.ILiveInterface;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;


/**
 * @author kn
 * @description: 直播间 游戏业务父类
 * @time kn 2019/12/21
 */
public class LiveLayoutGameUtils implements GameBusiness.GameBusinessCallback,
        GameBusiness.GameCtrlViewClickCallback,
        BankerBusiness.BankerBusinessCallback,
        BankerBusiness.BankerCtrlViewClickCallback {

    private GameBusiness mGameBusiness;
    private BankerBusiness mBankerBusiness;

    public ILiveInterface liveinterface;
    public Activity activity;
    public RoomGameBankerView mGameBankerView;


    public void toParentData(ILiveInterface liveinterface, Activity activity, RoomGameBankerView mGameBankerView) {
        this.liveinterface = liveinterface;
        this.activity = activity;
        this.mGameBankerView = mGameBankerView;
    }

    /**
     * 获得游戏基础业务类
     *
     * @return
     */
    public GameBusiness getGameBusiness() {
        if (mGameBusiness == null) {
            mGameBusiness = new GameBusiness(liveinterface);
            mGameBusiness.setCallback(this);
        }
        return mGameBusiness;
    }

    /**
     * 获得上庄业务类
     *
     * @return
     */
    public BankerBusiness getBankerBusiness() {
        if (mBankerBusiness == null) {
            mBankerBusiness = new BankerBusiness(liveinterface);
            mBankerBusiness.setCallback(this);
        }
        return mBankerBusiness;
    }

    public void onEventMainThread(EUpdateUserInfo event) {
        getGameBusiness().refreshGameCurrency();
    }

//    @Override
//    protected void onSuccessJoinGroup(String groupId) {
//        super.onSuccessJoinGroup(groupId);
//        getGameBusiness().requestGameInfo();
//    }
//
//    @Override
//    public void onMsgGame(MsgModel msg) {
//        super.onMsgGame(msg);
////        getGameBusiness().onMsgGame(msg);
//    }
//
//    @Override
//    public void onMsgGameBanker(CustomMsgGameBanker msg) {
//        super.onMsgGameBanker(msg);
//        getBankerBusiness().onMsgGameBanker(msg);
//    }


    @Override
    public void onClickGameCtrlStart(View view) {
        getGameBusiness().requestStartGame();
    }

    @Override
    public void onClickGameCtrlClose(View view) {
        AppDialogConfirm dialog = new AppDialogConfirm(activity);
        dialog.setTextContent("确定要关闭游戏？")
                .setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog) {
                        getGameBusiness().requestStopGame();
                    }
                }).show();
    }

    @Override
    public void onGameInitPanel(GameMsgModel msg) {

    }

    @Override
    public void onGameRemovePanel() {

    }

    @Override
    public void onGameMsg(GameMsgModel msg, boolean isPush) {
        getBankerBusiness().onGameMsg(msg);
    }

    @Override
    public void onGameMsgStopGame() {
        getBankerBusiness().setState(BankerBusiness.State.GAME_STOPPED);
    }

    @Override
    public void onGameRequestGameIncomeSuccess(App_requestGameIncomeActModel actModel) {

    }

    @Override
    public void onGameRequestStartGameSuccess(App_startGameActModel actModel) {
    }

    @Override
    public void onGameRequestStopGameSuccess(BaseActModel actModel) {
    }

    @Override
    public void onGameHasAutoStartMode(boolean hasAutoStartMode) {

    }

    @Override
    public void onGameAutoStartModeChanged(boolean isAutoStartMode) {

    }

    @Override
    public void onGameCtrlShowStart(boolean show, int gameId) {
    }

    @Override
    public void onGameCtrlShowClose(boolean show, int gameId) {
    }

    @Override
    public void onGameCtrlShowWaiting(boolean show, int gameId) {
    }

    @Override
    public void onGameUpdateGameCurrency(long value) {
    }


    public void onDestroy() {
        if (mGameBusiness != null) {
            mGameBusiness.onDestroy();
        }

        if (mBankerBusiness != null) {
            mBankerBusiness.onDestroy();
        }
    }

    //----------Banker start----------
    @Override
    public void onBankerCtrlCreaterShowOpenBanker(boolean show) {
    }

    @Override
    public void onBankerCtrlCreaterShowOpenBankerList(boolean show) {
    }

    @Override
    public void onBankerCtrlCreaterShowStopBanker(boolean show) {
    }

    @Override
    public void onBankerCtrlViewerShowApplyBanker(boolean show) {
    }

    @Override
    public void onBsBankerCreaterShowHasViewerApplyBanker(boolean show) {
    }

    @Override
    public void onBsBankerShowBankerInfo(GameBankerModel model) {

        mGameBankerView.setBnaker(model);
    }

    public void removeView(View view) {
        SDViewUtil.removeView(view);
    }

    @Override
    public void onBsBankerRemoveBankerInfo() {
        removeView(mGameBankerView);
        mGameBankerView = null;
    }

    @Override
    public void onClickBankerCtrlCreaterOpenBanker() {
        getBankerBusiness().requestOpenGameBanker();
    }

    @Override
    public void onClickBankerCtrlCreaterOpenBankerList() {
        GamesBankerListDialog dialog = new GamesBankerListDialog(activity, new GamesBankerListDialog.BankerSubmitListener() {
            @Override
            public void onClickChoose(SDDialogBase dialog, String bankerLogId) {
                getBankerBusiness().requestChooseBanker(bankerLogId);
            }
        });
        dialog.show();
    }

    @Override
    public void onClickBankerCtrlCreaterStopBanker() {
        AppDialogConfirm dialog = new AppDialogConfirm(activity);
        dialog.setTextContent("确定要移除该庄家？")
                .setTextConfirm("移除")
                .setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog) {
                        getBankerBusiness().requestStopGameBanker();
                    }
                }).show();
    }

    @Override
    public void onClickBankerCtrlViewerApplyBanker() {
        GamesBankerDialog dialog = new GamesBankerDialog(activity, new GamesBankerDialog.BankerSubmitListener() {
            @Override
            public void onClickSubmit(long coins) {
                getBankerBusiness().requestApplyBanker(coins);
            }
        });
        dialog.show(getBankerBusiness().getApplyBankerPrincipal(), getGameBusiness().getGameCurrency());
    }

    @Override
    public void onBsShowProgress(String msg) {

    }

    @Override
    public void onBsHideProgress() {

    }
    //----------Banker end----------
}
