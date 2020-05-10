package com.bogokj.live.room.extension;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.bogokj.baimei.dialog.BMDiceResultHistoryDialog;
import com.bogokj.games.DiceGameBusiness;
import com.bogokj.games.PokerGameBusiness;
import com.bogokj.games.constant.GameType;
import com.bogokj.games.dialog.GameLogDialog;
import com.bogokj.games.dialog.GamesWinnerDialog;
import com.bogokj.games.model.App_requestGameIncomeActModel;
import com.bogokj.games.model.GameBankerModel;
import com.bogokj.games.model.Games_betActModel;
import com.bogokj.games.model.Games_logActModel;
import com.bogokj.games.model.custommsg.GameMsgModel;
import com.bogokj.hybrid.http.AppHttpUtil;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.http.AppRequestParams;
import com.bogokj.libgame.dice.view.DiceGameView;
import com.bogokj.libgame.dice.view.base.DiceScoreBaseBoardView;
import com.bogokj.libgame.poker.bull.view.BullGameView;
import com.bogokj.libgame.poker.goldflower.view.GoldFlowerGameView;
import com.bogokj.libgame.poker.model.PokerGroupResultData;
import com.bogokj.libgame.poker.view.PokerGameView;
import com.bogokj.library.common.SDHandlerManager;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.appview.room.RoomGameBankerView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.LiveRechargeDialog;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.model.App_pop_propActModel;
import com.bogokj.live.model.LiveGiftModel;
import com.bogokj.live.room.ILiveInterface;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;

import java.util.List;

/**
 * @author kn
 * @description: 直播间 游戏样式工具类
 * @time kn 2019/12/21
 */
public class LiveLayoutGameExtendUtils extends LiveLayoutGameUtils implements
        PokerGameBusiness.PokerGameBusinessCallback, DiceGameBusiness.GameDiceBusinessListener {
    private PokerGameView mPokerGameView;
    private PokerGameBusiness mPokerGameBusiness;

    private DiceGameView mDiceGameView;
    private DiceGameBusiness mDiceGameBusiness;


    public LiveLayoutGameExtendUtils(Activity activity, ILiveInterface liveinterface, RoomGameBankerView mGameBankerView) {

        toParentData(liveinterface, activity, mGameBankerView);
    }


    /**
     * 获得扑克牌游戏业务类
     *
     * @return
     */
    private PokerGameBusiness getPokerGameBusiness() {
        if (mPokerGameBusiness == null) {
            mPokerGameBusiness = new PokerGameBusiness(getGameBusiness());
            mPokerGameBusiness.setCallback(this);
        }
        return mPokerGameBusiness;
    }

    /**
     * 获投骰子游戏业务类
     *
     * @return
     */
    private DiceGameBusiness getDiceGameBusiness() {
        if (mDiceGameBusiness == null) {
            mDiceGameBusiness = new DiceGameBusiness(getGameBusiness());
            mDiceGameBusiness.setGameDiceBusinessListener(this);
        }
        return mDiceGameBusiness;
    }

    @Override
    public void onGameMsg(GameMsgModel msg, boolean isPush) {
        super.onGameMsg(msg, isPush);
        getPokerGameBusiness().onGameMsg(msg, isPush);
        getDiceGameBusiness().onGameMsg(msg, isPush);
    }

    @Override
    public void onGameInitPanel(GameMsgModel msg) {
        super.onGameInitPanel(msg);
    }


    @Override
    public void onGameRemovePanel() {
        super.onGameRemovePanel();
        removeGamePanel();
    }


    /**
     * 显示切换自动开始游戏模式窗口
     */
    private void showChangeAutoStartModeDialog() {
        AppDialogConfirm dialog = new AppDialogConfirm(activity);
        if (getGameBusiness().isAutoStartMode()) {
            dialog.setTextContent("是否切换为手动开始游戏模式？");
        } else {
            dialog.setTextContent("是否切换为自动开始游戏模式？");
        }

        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                getGameBusiness().requestAutoStartGame(!getGameBusiness().isAutoStartMode());
            }
        }).show();
    }

    @Override
    public void onGameUpdateGameCurrency(long value) {
        super.onGameUpdateGameCurrency(value);
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setUserCoins(value);
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().setUserCoins(value);
        }
    }

    @Override
    public void onGameMsgStopGame() {
        super.onGameMsgStopGame();
        //关闭纸牌游戏
        getGameBusiness().requestGameCurrency();
        removeGamePanel();
    }

    @Override
    public void onGameHasAutoStartMode(boolean hasAutoStartMode) {
        super.onGameHasAutoStartMode(hasAutoStartMode);
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setHasAutoStartMode(hasAutoStartMode);
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().setHasAutoStartMode(hasAutoStartMode);
        }
    }

    @Override
    public void onGameAutoStartModeChanged(boolean isAutoStartMode) {
        super.onGameAutoStartModeChanged(isAutoStartMode);
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setAutoStartMode(isAutoStartMode);
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().setAutoStartMode(isAutoStartMode);
        }
    }

    @Override
    public void onGameRequestGameIncomeSuccess(App_requestGameIncomeActModel actModel) {
        super.onGameRequestGameIncomeSuccess(actModel);
        //游戏轮数收益
//        final int coin = actModel.getGain();
//        if (coin == 0 || getGameView() == null)
//        {
//            return;
//        }
        int coin = actModel.getGain();
        if (coin > 0) {
//            GameWinDialog dialog = new GameWinDialog(this);
//            dialog.setTextGain(String.valueOf(coin) + getGameBusiness().getGameCurrencyUnit());
//            dialog.showTop(true);

            GamesWinnerDialog dialogWin = new GamesWinnerDialog(activity);
            dialogWin.setGameIncomeModel(actModel);
            dialogWin.setSendGiftClickListener(new GamesWinnerDialog.OnSendGiftClickListener() {
                @Override
                public void onClickSendGift(LiveGiftModel model) {
                    sendGift(model);
                }
            });
            dialogWin.showDialog();
        }
    }

    private void sendGift(final LiveGiftModel model) {
        if (model != null) {
            if (liveinterface.getRoomInfo() == null) {
                return;
            }
            SDToast.showToast("发送完成");

            AppRequestParams params = CommonInterface.requestSendGiftParams(model.getId(), 1, 0, liveinterface.getRoomId());
            AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_propActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    // 扣费
                    if (actModel.isOk()) {
                        UserModelDao.payDiamonds(model.getDiamonds());
                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    CommonInterface.requestMyUserInfo(null);
                }
            });
        }
    }

    @Override
    public void onBsGameBetMsgBegin(GameMsgModel msg, boolean isPush) {
        if (mPokerGameView != null) {
            //开始游戏，倒计时
            mPokerGameView.getManager().start(msg.getTime() * 1000);

            //发牌
            if (SDViewUtil.isVisible(mPokerGameView) && isPush) {
                mPokerGameView.getManager().startDealPoker(true);
            } else {
                mPokerGameView.getManager().startDealPoker(false);
            }
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().start(msg.getTime() * 1000);
        }
    }

    @Override
    public void onBsGameBetUpdateTotalBet(List<Integer> listData) {
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setTotalBetData(listData);
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().setTotalBetData(listData);
        }
    }

    @Override
    public void onBsGameBetUpdateUserBet(List<Integer> listData) {
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setUserBetData(listData);
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().setUserBetData(listData);
        }
    }

    @Override
    public void onBsGameBetUpdateBetCoinsOption(List<Integer> listData) {
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setBetCoinsOptionData(listData);
        }
    }

    @Override
    public void onBsGamePokerUpdatePokerDatas(List<PokerGroupResultData> listData, int winPosition, boolean isPush) {
        if (mPokerGameView != null) {
            mPokerGameView.getManager().setResultData(listData);
            mPokerGameView.getManager().setWinPosition(winPosition);
            mPokerGameView.getManager().showResult(isPush);
        }
    }

    @Override
    public void onBsGameBetRequestGameLogSuccess(Games_logActModel actModel) {
        if (mPokerGameView != null) {
            GameLogDialog dialog = new GameLogDialog(activity);
            dialog.setGameId(getGameBusiness().getGameId());
            dialog.setData(actModel.getList());
            dialog.show();
        }
        if (mDiceGameView != null) {
            BMDiceResultHistoryDialog dialog = new BMDiceResultHistoryDialog(activity);
            dialog.setData(actModel.getData());
            dialog.show();
        }
    }

    @Override
    public void onBsGameBetRequestDoBetSuccess(Games_betActModel actModel, int betPosition, long betCoin) {
        if (mPokerGameView != null) {
            mPokerGameView.getManager().onBetSuccess(betPosition, betCoin);
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().onBetSuccess(betPosition, betCoin);
        }
    }

//    @Override
//    public void onShowSendMsgView(View view) {
//        super.onShowSendMsgView(view);
//        hideGamePanelView();
//    }
//
//    @Override
//    protected void onHideSendMsgView(View view) {
//        super.onHideSendMsgView(view);
//        showGamePanelView();
//    }
//
//    @Override
//    protected void onShowSendGiftView() {
//        super.onShowSendGiftView();
//        hideGamePanelView();
//    }
//
//    @Override
//    protected void onHideSendGiftView() {
//        super.onHideSendGiftView();
//        showGamePanelView();
//    }

    @Override
    public void onBsBankerShowBankerInfo(GameBankerModel model) {
        super.onBsBankerShowBankerInfo(model);

        if (getBankerBusiness().isMyBanker()) {
            getGameBusiness().requestGameCurrency();
            if (mPokerGameView != null) {
                mPokerGameView.getManager().setCanBet(false);
            }
            if (mDiceGameView != null) {
                mDiceGameView.getManager().setCanBet(false);
            }
        }
    }

    @Override
    public void onBsBankerRemoveBankerInfo() {
        super.onBsBankerRemoveBankerInfo();

        if (getBankerBusiness().isMyBanker()) {
            getGameBusiness().requestGameCurrency();
            if (mPokerGameView != null) {
                mPokerGameView.getManager().setCanBet(true);
            }
            if (mDiceGameView != null) {
                mDiceGameView.getManager().setCanBet(true);
            }
        }
    }

    private View getGameView() {
        switch (getGameBusiness().getGameId()) {
            case GameType.GOLD_FLOWER:
            case GameType.BULL:
                return mPokerGameView;
            case GameType.DICE:
                return mDiceGameView;
            default:
                return null;
        }
    }

    /**
     * 显示游戏面板
     */
    protected void showGamePanelView() {
        getGameBusiness().refreshGameCurrency();
        SDHandlerManager.postDelayed(new Runnable() {
            @Override
            public void run() {
                SDViewUtil.setVisible(getGameView());
            }
        }, 100);
    }

    /**
     * 隐藏游戏面板
     */
    public void hideGamePanelView() {
        SDViewUtil.setGone(mPokerGameView);
        SDViewUtil.setGone(mDiceGameView);
    }

    /**
     * 移除游戏面板
     */
    public void removeGamePanel() {
        if (mPokerGameView != null) {
            mPokerGameView.getManager().onDestroy();
            removeView(mPokerGameView);
            mPokerGameView = null;
        }
        if (mDiceGameView != null) {
            mDiceGameView.getManager().onDestroy();
            removeView(mDiceGameView);
            mDiceGameView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPokerGameBusiness != null) {
            mPokerGameBusiness.onDestroy();
        }
        if (mDiceGameBusiness != null) {
            mDiceGameBusiness.onDestroy();
        }
        removeGamePanel();
    }

//    @Override
//    public void onMsgEndVideo(CustomMsgEndVideo msg) {
//        super.onMsgEndVideo(msg);
//        removeGamePanel();
//    }

    @Override
    public void onBsGameDiceThrowDice(List<Integer> listData, int winPosition, boolean isPush) {
        mDiceGameView.getManager().setWinPosition(winPosition);
        mDiceGameView.getManager().showResult(listData);
    }


    /**
     * 功能初始化以及回调
     */

    /**
     * 斗牛
     *
     * @param msg
     * @param context
     */
    public void bullGameInit(GameMsgModel msg, Context context) {
        if (mPokerGameView == null) {
            mPokerGameView = new BullGameView(context);
            initPokerGameView(msg);
        }
    }

    /**
     * 金画
     *
     * @param msg
     * @param context
     */
    public void flowGameInit(GameMsgModel msg, Context context) {
        if (mPokerGameView == null) {
            mPokerGameView = new GoldFlowerGameView(context);
            initPokerGameView(msg);
        }
    }


    /**
     * 初始化扑克牌游戏view
     *
     * @param msg
     */
    public void initPokerGameView(GameMsgModel msg) {
        mPokerGameView.setCallback(mPokerGameViewCallback);
        mPokerGameView.getManager().setCreater(liveinterface.isCreater());
        mPokerGameView.getManager().setBetMultipleData(msg.getOption());
        mPokerGameView.getManager().setBetCoinsOptionData(msg.getBet_option());
        mPokerGameView.getManager().setUserCoins(getGameBusiness().getGameCurrency());
        mPokerGameView.getManager().setUserCoinsImageRes(AppRuntimeWorker.isUseGameCurrency() ? R.drawable.ic_game_coins : R.drawable.ic_user_coins_diamond);
//        replaceBottomExtend(mPokerGameView);
        showView.showPokerGameView(mPokerGameView);
    }

    /**
     * 初始化猜大小游戏view
     *
     * @param msg
     */
    public void initDiceGameView(GameMsgModel msg, Context context) {
        if (mDiceGameView == null) {
            mDiceGameView = new DiceGameView(context);
            mDiceGameView.setCallback(mDiceGameViewCallback);
            mDiceGameView.getManager().setCreater(liveinterface.isCreater());
            mDiceGameView.getManager().setBetMultipleData(msg.getOption());
            mDiceGameView.getManager().setBetCoinsOptionData(msg.getBet_option());
            mDiceGameView.getManager().setUserCoins(getGameBusiness().getGameCurrency());
            mDiceGameView.getManager().setUserCoinsImageRes(AppRuntimeWorker.isUseGameCurrency() ? R.drawable.ic_game_coins : R.drawable.ic_user_coins_diamond);
//            replaceBottomExtend(mDiceGameView);
            showView.showDiceGameView(mDiceGameView);
        }
    }

    /**
     * 扑克牌游戏view点击回调
     */
    private PokerGameView.PokerGameViewCallback mPokerGameViewCallback = new PokerGameView.PokerGameViewCallback() {
        @Override
        public void onClickBetView(int betPosition, long betCoin) {
            if (betCoin <= 0) {
                return;
            }
            if (!getGameBusiness().canGameCurrencyPay(betCoin)) {
                SDToast.showToast("余额不足，请先充值");
                return;
            }
            getPokerGameBusiness().requestDoBet(betPosition, betCoin);
        }

        @Override
        public void onClockFinish() {
            if (getGameBusiness().isInGameRound()) {
                Log.i("poker", "倒计时结束，但是还处于投注状态，延时调用查询游戏信息接口");
                getGameBusiness().startRequestGameInfoDelay();
            }
        }

        @Override
        public void onClickRecharge() {
            showRechargeDialog();
        }

        @Override
        public void onClickGameLog() {
            getPokerGameBusiness().requestGameLog();
        }

        @Override
        public void onClickGameClose(View view) {
            onClickGameCtrlClose(view);
        }

        @Override
        public void onClickChangeAutoStartMode() {
            showChangeAutoStartModeDialog();
        }
    };


    /**
     * 猜大小游戏view回调
     */
    private DiceGameView.DiceGameViewCallback mDiceGameViewCallback = new DiceGameView.DiceGameViewCallback() {
        @Override
        public void onClickBetView(DiceScoreBaseBoardView view, int betPosition, long betCoin) {
            if (betCoin <= 0) {
                return;
            }
            if (!getGameBusiness().canGameCurrencyPay(betCoin)) {
                SDToast.showToast("余额不足，请先充值");
                return;
            }
            getPokerGameBusiness().requestDoBet(betPosition, betCoin);
        }

        @Override
        public void onClockFinish() {
            if (getGameBusiness().isInGameRound()) {
                Log.i("poker", "倒计时结束，但是还处于投注状态，延时调用查询游戏信息接口");
                getGameBusiness().startRequestGameInfoDelay();
            }
        }

        @Override
        public void onClickRecharge() {
            showRechargeDialog();
        }

        @Override
        public void onClickGameLog() {
            getPokerGameBusiness().requestGameLog();
        }

        @Override
        public void onClickGameClose(View view) {
            onClickGameCtrlClose(view);
        }

        @Override
        public void onClickChangeAutoStartMode() {
            showChangeAutoStartModeDialog();
        }
    };


    protected void showRechargeDialog() {
        LiveRechargeDialog dialog = new LiveRechargeDialog(activity);
        dialog.show();
    }


    private GameCallBackListener showView;

    public void setGameCallBackListener(GameCallBackListener showView) {
        this.showView = showView;
    }

    public interface GameCallBackListener {
        void showPokerGameView(PokerGameView mPokerGameView);

        void showDiceGameView(DiceGameView mDiceGameView);
    }
}
