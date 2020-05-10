package com.bogokj.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.activity.BogoInviteCodeActivity;
import com.bogokj.live.activity.LiveChatC2CActivity;
import com.bogokj.live.activity.LiveClubDetailsActivity;
import com.bogokj.live.activity.LiveDistributionActivity;
import com.bogokj.live.activity.LiveFamilyDetailsActivity;
import com.bogokj.live.activity.LiveGamesDistributionActivity;
import com.bogokj.live.activity.LiveMySelfContActivity;
import com.bogokj.live.activity.LiveRechargeDiamondsActivity;
import com.bogokj.live.activity.LiveRechargeVipActivity;
import com.bogokj.live.activity.LiveSearchUserActivity;
import com.bogokj.live.activity.LiveSociatyUpdateEditActivity;
import com.bogokj.live.activity.LiveToolsShopActivity;
import com.bogokj.live.activity.LiveUserCenterAuthentActivity;
import com.bogokj.live.activity.LiveUserProfitActivity;
import com.bogokj.live.activity.LiveUserSettingActivity;
import com.bogokj.live.activity.LiveWebViewActivity;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.databinding.FragLiveNewTabMeBlackBinding;
import com.bogokj.live.dialog.LiveAddNewFamilyDialog;
import com.bogokj.live.dialog.LiveGameExchangeDialog;
import com.bogokj.live.dialog.LiveInviteCodeDialog;
import com.bogokj.live.event.EUpdateUserInfo;
import com.bogokj.live.model.App_InitH5Model;
import com.bogokj.live.model.App_gameExchangeRateActModel;
import com.bogokj.live.model.App_request_check_is_full_invite_codeModel;
import com.bogokj.live.model.App_userinfoActModel;
import com.bogokj.live.model.Deal_send_propActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.LiveUtils;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.bogokj.o2o.activity.O2OShoppingMystoreActivity;
import com.bogokj.pay.activity.PayBalanceActivity;
import com.bogokj.shop.activity.ShopMyStoreActivity;
import com.fanwe.library.adapter.http.model.SDResponse;

/**
 * @author kn
 * @description: 首页我的
 * databinding for
 * @time kn 2019/12/17
 */
public class LiveMainMeView extends BaseAppView {

    private static FragLiveNewTabMeBlackBinding databinding;
    private App_userinfoActModel app_userinfoActModel;
    private LiveAddNewFamilyDialog dialogFam;


    public LiveMainMeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMainMeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainMeView(Context context) {
        super(context);
        init();
    }


    @Override
    protected int onCreateContentView() {
        return 0;
    }


    protected void init() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.frag_live_new_tab_me_black, null, false);
        databinding = DataBindingUtil.bind(rootView);
        setContentView(rootView);


        initListener();
        initPullToRefresh();
        initData();
        //是否显示邀请码
        checkIsShowInviteCode();
    }


    public static void checkIsShowInviteCode() {

        CommonInterface.requestCheckIsFullInviteCode(new AppRequestCallback<App_request_check_is_full_invite_codeModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    if (actModel.getState() == 0) {
                        databinding.rlInviteCode.setVisibility(VISIBLE);
                        databinding.rlInviteCodeView.setVisibility(VISIBLE);
                    } else {
                        databinding.rlInviteCode.setVisibility(GONE);
                        databinding.rlInviteCodeView.setVisibility(GONE);
                    }
                }
            }
        });
    }

    private void initListener() {
        databinding.setClickUtils(new ClickClass());
    }

    private void initPullToRefresh() {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                changeUI();
                request();
            }

            @Override
            public void onRefreshingFromFooter() {

            }
        });
    }

    public void initData() {
        request();
        changeUI();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this == changedView && visibility == View.VISIBLE) {
            refreshData();
        }
    }

    public void refreshData() {
        request();
        changeUI();
    }

    private void changeUI() {
        SDViewBinder.setTextView(databinding.tvTicketName, AppRuntimeWorker.getTicketName());

        int live_pay = AppRuntimeWorker.getLive_pay();
        if (live_pay == 1) {
            SDViewUtil.setVisible(databinding.llPay);
        } else {
            SDViewUtil.setGone(databinding.llPay);
        }

        int distribution = AppRuntimeWorker.getDistribution();
        if (distribution == 1) {
            SDViewUtil.setVisible(databinding.llDistribution);
        } else {
            SDViewUtil.setGone(databinding.llDistribution);
        }
        if (AppRuntimeWorker.isOpenVip()) {
            SDViewUtil.setVisible(databinding.rlVip);
        } else {
            SDViewUtil.setGone(databinding.rlVip);
        }

        if (AppRuntimeWorker.isUseGameCurrency()) {
            SDViewUtil.setVisible(databinding.llGameCurrencyExchange);
        } else {
            SDViewUtil.setGone(databinding.llGameCurrencyExchange);
        }

        if (AppRuntimeWorker.getOpen_family_module() == 1) {
            SDViewUtil.setVisible(databinding.llFamily);
        } else {
            SDViewUtil.setGone(databinding.llFamily);
        }

        if (AppRuntimeWorker.getOpen_sociaty_module() == 1) {
            SDViewUtil.setVisible(databinding.relSociaty);
        } else {
            SDViewUtil.setGone(databinding.relSociaty);
        }

        if (AppRuntimeWorker.getGame_distribution() == 1) {
            SDViewUtil.setVisible(databinding.rlGamesDistribution);
        } else {
            SDViewUtil.setGone(databinding.rlGamesDistribution);
        }
    }

    private void request() {
        CommonInterface.requestMyUserInfo(new AppRequestCallback<App_userinfoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_userinfoActModel = actModel;
                    databinding.viewLiveUserInfo.setData(app_userinfoActModel);
                    UserModel userModel = actModel.getUser();
                    UserModelDao.insertOrUpdate(userModel);
                    databinding.setUserModel(userModel);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    private void bindNormalData(UserModel user) {
        if (user == null) {
            return;
        }

        if (databinding.viewLiveUserInfoTab != null) {
            databinding.viewLiveUserInfoTab.setData(user);
        }

        if (user.getSociety_id() == 0) {
            SDViewBinder.setTextView(databinding.tvSociaty, "创建公会");
        } else {
            SDViewBinder.setTextView(databinding.tvSociaty, "我的公会");
        }

        SDViewBinder.setTextView(databinding.tvAccout, LiveUtils.getFormatNumber(user.getDiamonds()));

        SDViewBinder.setTextView(databinding.tvIncome, LiveUtils.getFormatNumber(user.getUseable_ticket()));

        String str_user_diamonds = user.getUse_diamonds() + AppRuntimeWorker.getDiamondName();
        SDViewBinder.setTextView(databinding.tvUseDiamonds, str_user_diamonds);

        SDViewBinder.setTextView(databinding.tvLevel, String.valueOf(user.getUser_level()));

        String anchor = SDResourcesUtil.getString(R.string.live_account_authentication);
        anchor = anchor + "认证";
        SDViewBinder.setTextView(databinding.tvAnchor, anchor);

        int is_authentication = user.getIs_authentication();
        if (is_authentication == 0) {
            databinding.tvVType.setText("未认证");
        } else if (is_authentication == 1) {
            databinding.tvVType.setText("等待审核");
        } else if (is_authentication == 2) {
            databinding.tvVType.setText("已认证");
        } else if (is_authentication == 3) {
            databinding.tvVType.setText("审核不通过");
        }

        if (user.getIs_vip() == 1) {
            databinding.tvVip.setText("已开通");
            databinding.tvVip.setTextColor(SDResourcesUtil.getColor(R.color.res_main_color));
        } else {
            databinding.tvVip.setText(user.getVip_expire_time());
            databinding.tvVip.setTextColor(SDResourcesUtil.getColor(R.color.res_text_gray_m));
        }

        SDViewBinder.setTextView(databinding.tvGameCurrency, LiveUtils.getFormatNumber(user.getCoin()) + SDResourcesUtil.getString(R.string.game_currency));

        if (user.getShow_podcast_goods() == 1) {
            SDViewUtil.setVisible(databinding.llShowPodcastGoods);
            String podcast_goods_dec = String.valueOf(user.getPodcast_goods()) + "个商品";
            SDViewBinder.setTextView(databinding.tvShowPodcastGoods, podcast_goods_dec);
        } else {
            SDViewUtil.setGone(databinding.llShowPodcastGoods);
        }

        if (user.getShow_user_order() == 1) {
            SDViewUtil.setVisible(databinding.llShowUserOrder);
            String user_order_dec = String.valueOf(user.getUser_order()) + "个订单";
            SDViewBinder.setTextView(databinding.tvShowUserOrder, user_order_dec);
        } else {
            SDViewUtil.setGone(databinding.llShowUserOrder);
        }

        if (user.getShow_podcast_order() == 1) {
            SDViewUtil.setVisible(databinding.llShowPodcastOrder);
            String podcast_order_dec = String.valueOf(user.getPodcast_order()) + "个订单";
            SDViewBinder.setTextView(databinding.tvShowPodcastOrder, podcast_order_dec);
        } else {
            SDViewUtil.setGone(databinding.llShowPodcastOrder);
        }

        if (user.getShow_shopping_cart() == 1) {
            SDViewUtil.setVisible(databinding.llShowShoppingCart);
            String shopping_cart_dec = String.valueOf(user.getShopping_cart()) + "个商品";
            SDViewBinder.setTextView(databinding.tvShowShoppingCart, shopping_cart_dec);
        } else {
            SDViewUtil.setGone(databinding.llShowShoppingCart);
        }

        if (user.getShow_user_pai() == 1) {
            SDViewUtil.setVisible(databinding.llShowUserPai);
            String user_pai_dec = String.valueOf(user.getUser_pai()) + "个竞拍";
            SDViewBinder.setTextView(databinding.tvShowUserPai, user_pai_dec);
        } else {
            SDViewUtil.setGone(databinding.llShowUserPai);
        }

        if (user.getShow_podcast_pai() == 1) {
            SDViewUtil.setVisible(databinding.llShowPodcastPai);
            String podcast_pai_dec = String.valueOf(user.getPodcast_pai()) + "个竞拍";
            SDViewBinder.setTextView(databinding.tvShowPodcastPai, podcast_pai_dec);
        } else {
            SDViewUtil.setGone(databinding.llShowPodcastPai);
        }

        if (user.getOpen_podcast_goods() == 1) {
            SDViewUtil.setVisible(databinding.llOpenPodcastGoods);
            String shop_goods = String.valueOf(user.getShop_goods()) + "个商品";
            SDViewBinder.setTextView(databinding.tvOpenPodcastGoods, shop_goods);
        } else {
            SDViewUtil.setGone(databinding.llOpenPodcastGoods);
        }


    }


    /**
     * @param event 接收刷新UserModel信息事件
     */
    public void onEventMainThread(EUpdateUserInfo event) {
        UserModel user = event.user;
        bindNormalData(user);
    }


//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        switch (v.getId()) {
//
//            case R.id.include_cont_linear:
//                clickIncludeContLinear();
//                break;
//
//            default:
//                break;
//        }
//    }


    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        refreshData();
    }


    /**
     * 类方法
     */
    public class ClickClass {

        //印票贡献榜
        public void clickIncludeContLinear() {
            Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
            getActivity().startActivity(intent);
        }


        public void clickInvite() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getInviteUrl());
            getActivity().startActivity(intent);
        }

        public void clickInviteCode() {
            Intent intent = new Intent(getActivity(), BogoInviteCodeActivity.class);
            getActivity().startActivity(intent);
        }

        public void clickToolsShop() {
            Intent intent = new Intent(getContext(), LiveToolsShopActivity.class);
            getActivity().startActivity(intent);
        }

        public void clickGoodNumber() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getGoodNumberUrl());
            getActivity().startActivity(intent);
        }

        public void clickWebVip() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getVip_url());
            getActivity().startActivity(intent);
        }

        public void clickWebWeekStar() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getWeekGiftUrl());
            getActivity().startActivity(intent);
        }

        public void clickBuyCars() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getNoble_car_url());
            getActivity().startActivity(intent);
        }

        // 搜索
        public void clickLLSearch() {
            Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
            getActivity().startActivity(intent);
        }

        //聊天
        public void clickLlChat() {
            Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
            getActivity().startActivity(intent);
        }

        //账户
        public void clickRlAccout() {
            Intent intent = new Intent(getActivity(), LiveRechargeDiamondsActivity.class);
            getActivity().startActivity(intent);
        }

        //收益
        public void clickRlIncome() {
            Intent intent = new Intent(getActivity(), LiveUserProfitActivity.class);
            getActivity().startActivity(intent);
        }

        /**
         * VIP充值页面
         */
        public void clickVip() {
            Intent intent = new Intent(getActivity(), LiveRechargeVipActivity.class);
            getActivity().startActivity(intent);
        }

        /**
         * 游戏币兑换
         */
        public void doGameExchange() {
            showProgressDialog("");
            CommonInterface.requestGamesExchangeRate(new AppRequestCallback<App_gameExchangeRateActModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.isOk()) {
                        LiveGameExchangeDialog dialog = new LiveGameExchangeDialog(getActivity(), LiveGameExchangeDialog.TYPE_COIN_EXCHANGE, new LiveGameExchangeDialog.OnSuccessListener() {
                            @Override
                            public void onExchangeSuccess(long diamonds, long coins) {
                                UserModel user = UserModelDao.updateDiamondsAndCoins(diamonds, coins);
                                UserModelDao.insertOrUpdate(user);
                            }

                            @Override
                            public void onSendCurrencySuccess(Deal_send_propActModel model) {

                            }
                        });
                        dialog.setRate(actModel.getExchange_rate());
                        dialog.setCurrency(app_userinfoActModel.getUser().getDiamonds());
                        dialog.show();
                    }
                }

                @Override
                protected void onFinish(SDResponse resp) {
                    super.onFinish(resp);
                    dismissProgressDialog();
                }
            });
        }

        //付费榜
        public void clickRelPay() {
            Intent intent = new Intent(getActivity(), PayBalanceActivity.class);
            getActivity().startActivity(intent);
        }

        //等级
        public void clickRlLevel() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getUrl_my_grades());
            getActivity().startActivity(intent);
        }

        //贵族
        public void clickNobleList() {
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getUrl_noble());
            getActivity().startActivity(intent);
        }

        //认证
        public void clickLlUpgrade() {
            if (app_userinfoActModel != null) {
                Intent intent = new Intent(getActivity(), LiveUserCenterAuthentActivity.class);
                getActivity().startActivity(intent);
            }
        }


        /**
         * 分享收益
         */
        public void clickLlDistribution() {
            Intent intent = new Intent(getActivity(), LiveDistributionActivity.class);
            getActivity().startActivity(intent);
        }

        /**
         * 游戏分享收益
         */
        public void openGameDistributionAct() {
            Intent intent = new Intent(getActivity(), LiveGamesDistributionActivity.class);
            getActivity().startActivity(intent);
        }

        /**
         * 商品管理
         */
        public void clickLlShowPodcastGoods() {
            if (AppRuntimeWorker.getIsOpenWebviewMain()) {
                Intent intent = new Intent(getActivity(), O2OShoppingMystoreActivity.class);
                getActivity().startActivity(intent);
            } else {
                if (app_userinfoActModel == null) {
                    return;
                }
                App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
                if (h5Url == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
                intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_podcast_goods());
                getActivity().startActivity(intent);
            }
        }

        /**
         * 我的订单
         */
        public void clickLlShowUserOrder() {
            if (app_userinfoActModel == null) {
                return;
            }
            App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
            if (h5Url == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_user_order());
            getActivity().startActivity(intent);
        }

        /**
         * 订单管理
         */
        public void clickLlShowPodcastOrder() {
            if (app_userinfoActModel == null) {
                return;
            }
            App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
            if (h5Url == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_podcast_order());
            getActivity().startActivity(intent);
        }

        /**
         * 我的购物车
         */
        public void clickLlShowShoppingCart() {
            if (app_userinfoActModel == null) {
                return;
            }
            App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
            if (h5Url == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_shopping_cart());
            getActivity().startActivity(intent);
        }

        /**
         * 我的竞拍
         */
        public void clickLlShowUserPai() {
            if (app_userinfoActModel == null) {
                return;
            }
            App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
            if (h5Url == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_user_pai());
            getActivity().startActivity(intent);
        }

        /**
         * 竞拍管理
         */
        public void clickLlShowPodcastPai() {
            if (app_userinfoActModel == null) {
                return;
            }
            App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
            if (h5Url == null) {
                return;
            }
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_podcast_pai());
            getActivity().startActivity(intent);
        }

        /**
         * 我的小店
         */
        public void clickLlOpenPodcastGoods() {
            Intent intent = new Intent(getActivity(), ShopMyStoreActivity.class);
            getActivity().startActivity(intent);
        }

        /**
         * 我的家族
         */
        public void clickFamily() {
            UserModel dao = UserModelDao.query();
            if (dao.getFamily_id() == 0) {
                if (dialogFam == null) {
                    dialogFam = new LiveAddNewFamilyDialog(getActivity());
                }
                dialogFam.showCenter();
            } else {
                //家族详情
                Intent intent = new Intent(getActivity(), LiveFamilyDetailsActivity.class);
                getActivity().startActivity(intent);
            }
        }

        /**
         * 我的公会
         */
        public void clickSociaty() {
            UserModel user = UserModelDao.query();
            if (user == null) {
                return;
            }
            if (user.getSociety_id() == 0) {
                Intent intentNew = new Intent(getActivity(), LiveSociatyUpdateEditActivity.class);
                getActivity().startActivity(intentNew);
            } else {
                Intent intent = new Intent(getActivity(), LiveClubDetailsActivity.class);
                intent.putExtra(LiveClubDetailsActivity.SOCIETY_ID, user.getSociety_id());
                intent.putExtra(LiveClubDetailsActivity.SOCIETY_NAME, user.getSociety_name());
                intent.putExtra(LiveClubDetailsActivity.SOCIATY_STATE, user.getGh_status());
                intent.putExtra(LiveClubDetailsActivity.SOCIETY_IDENTITY_TYPE, user.getSociety_chieftain());
                getActivity().startActivity(intent);
            }
        }

        /**
         * 设置
         */
        public void clickSetting() {
            Intent intent = new Intent(getActivity(), LiveUserSettingActivity.class);
            getActivity().startActivity(intent);
        }
    }
}
