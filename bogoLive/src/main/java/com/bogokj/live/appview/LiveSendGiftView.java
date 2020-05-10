package com.bogokj.live.appview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveGiftAdapter;
import com.bogokj.live.adapter.LiveGiftTypelAdapter;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.LiveRechargeDialog;
import com.bogokj.live.event.EUpdateUserInfo;
import com.bogokj.live.model.App_propActModel;
import com.bogokj.live.model.LiveGiftModel;
import com.bogokj.live.model.LiveGiftTypeModel;
import com.bogokj.live.model.UserModel;
import com.fanwe.lib.blocker.SDDurationBlocker;
import com.fanwe.lib.looper.ISDLooper;
import com.fanwe.lib.looper.impl.SDSimpleLooper;
import com.fanwe.lib.viewpager.SDGridViewPager;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.ImagePagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.common.SDHandlerManager;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.model.SDTaskRunnable;
import com.bogokj.library.receiver.SDNetworkReceiver;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.SDRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn update
 * @description: 礼物页面布局
 * @time 2020/1/15
 */
public class LiveSendGiftView extends BaseAppView implements ILivePrivateChatMoreView {
    public LiveSendGiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveSendGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveSendGiftView(Context context) {
        super(context);
        init();
    }

    private static final long DURATION_CONTINUE = 5 * 1000;
    private static final long DURATION_COUNT = 100;

    private View ll_send_gift_all;

    private View view_pager_container;
    private SDGridViewPager vpg_content;
    private PagerIndicator view_pager_indicator;

    private View ll_charge;
    private TextView tv_diamonds;
    private TextView tv_send;
    private View view_continue_send;
    private View view_click_continue_send;
    private TextView tv_continue_number;
    private TextView tv_count_down_number;

    private LiveGiftAdapter mAdapterGift;

    private ISDLooper mLooper = new SDSimpleLooper();
    /**
     * 倒计时数字
     */
    private long mCountDownNumber = DURATION_CONTINUE / DURATION_COUNT;
    /**
     * 连击数量
     */
    private int mClickNumber = 0;

    private SDDurationBlocker mBlocker = new SDDurationBlocker(300);

    private SendGiftViewCallback mCallback;

    //礼物分类recyclerview
    private SDRecyclerView gift_type_recyclerview;
    //礼物分类适配器
    private LiveGiftTypelAdapter liveGiftTypelAdapter;

    /**
     * 设置回调
     *
     * @param sendGiftViewCallback
     */
    public void setCallback(SendGiftViewCallback sendGiftViewCallback) {
        this.mCallback = sendGiftViewCallback;
    }

    protected void init() {
        setContentView(R.layout.view_live_send_gift);

        ll_send_gift_all = findViewById(R.id.ll_send_gift_all);

        view_pager_container = findViewById(R.id.view_pager_container);
        vpg_content = (SDGridViewPager) findViewById(R.id.vpg_content);
        view_pager_indicator = (PagerIndicator) findViewById(R.id.view_pager_indicator);

        ll_charge = findViewById(R.id.ll_charge);
        tv_diamonds = (TextView) findViewById(R.id.tv_diamonds);
        tv_send = (TextView) findViewById(R.id.tv_send);
        view_continue_send = findViewById(R.id.view_continue_send);
        view_click_continue_send = findViewById(R.id.view_click_continue_send);
        tv_continue_number = (TextView) findViewById(R.id.tv_continue_number);
        tv_count_down_number = (TextView) findViewById(R.id.tv_count_down_number);
        //礼物类型适配器
        gift_type_recyclerview = (SDRecyclerView) findViewById(R.id.gift_type_recyclerview);
        register();
        bindUserData();
    }

    private void register() {
        initSlidingView();
        ll_charge.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickCharge();
            }
        });
        tv_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickSend();
            }
        });
        view_click_continue_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickContinueSend();
            }
        });
    }

    private void initSlidingView() {
        view_pager_indicator.setViewPager(vpg_content);
        view_pager_indicator.setAdapter(new PagerIndicatorAdapter() {
            ImagePagerIndicatorItem.IndicatorConfig indicatorConfig;

            public ImagePagerIndicatorItem.IndicatorConfig getIndicatorConfig() {
                if (indicatorConfig == null) {
                    indicatorConfig = new ImagePagerIndicatorItem.IndicatorConfig(getContext());
                    indicatorConfig.imageResIdNormal = R.drawable.ic_pager_indicator_gray_circle;
                    indicatorConfig.imageResIdSelected = R.drawable.ic_pager_indicator_white_rect;

                    indicatorConfig.widthNormal = SDViewUtil.dp2px(5);
                    indicatorConfig.widthSelected = SDViewUtil.dp2px(15);

                    indicatorConfig.heightNormal = SDViewUtil.dp2px(5);
                    indicatorConfig.heightSelected = SDViewUtil.dp2px(5);

                    indicatorConfig.margin = SDViewUtil.dp2px(5);
                }
                return indicatorConfig;
            }

            @Override
            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent) {
                ImagePagerIndicatorItem item = new ImagePagerIndicatorItem(getContext());
                item.setIndicatorConfig(getIndicatorConfig());
                item.onSelectedChanged(false);
                return item;
            }
        });

        vpg_content.setGridItemCountPerPage(8);
        vpg_content.setGridColumnCountPerPage(4);

//        SDDrawable drawable = new SDDrawable().color(Color.parseColor("#8FFFFFFF")).size(1);
//        vpg_content.setGridVerticalDivider(drawable);
//        vpg_content.setGridHorizontalDivider(drawable);


        mAdapterGift = new LiveGiftAdapter(null, getActivity());
        mAdapterGift.setItemClickCallback(new SDItemClickCallback<LiveGiftModel>() {
            @Override
            public void onItemClick(int position, LiveGiftModel item, View view) {
                mAdapterGift.getSelectManager().performClick(position);
                resetClick();
                changeSendBg();
                if (item.getIs_lucky() == 1) {
                    SDToast.showToast("此礼物为幸运礼物,每送出一个有几率最高获得" + item.getMore_multiple() + "倍返奖");
                }

                ImageView iv_gift = view.findViewById(R.id.iv_gift);

                AnimatorSet animatorSet = new AnimatorSet();//组合动画
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv_gift, "scaleX", 1, 1.3f, 1);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv_gift, "scaleY", 1, 1.3f, 1);

                animatorSet.setDuration(1000);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                animatorSet.start();
            }
        });
        vpg_content.setGridAdapter(mAdapterGift);
    }

    public void changeSendBg() {
//        if (null == getSelectedGiftModel()) {
//            tv_send.setBackgroundResource(R.drawable.res_layer_gray_corner_l);
//        } else {
//            tv_send.setBackgroundResource(R.drawable.res_sel_second_color_corner_l);
//        }
//        if (null == getSelectedGiftModel()) {
//            tv_send.setBackgroundResource(R.drawable.ic_gift_send);
//        } else {
//            tv_send.setBackgroundResource(R.drawable.ic_gift_send);
//        }
    }

    /**
     * 更新用户数据
     */
    public void bindUserData() {
        updateDiamonds(UserModelDao.query());
    }

    /**
     * 更新钻石数量
     *
     * @param user
     */
    private void updateDiamonds(UserModel user) {
        if (user != null) {
            SDViewBinder.setTextView(tv_diamonds, String.valueOf(user.getDiamonds()));
        }
    }

    /**
     * 发送某礼物成功调用，更新本地钻石数量
     *
     * @param giftModel
     */
    public void sendGiftSuccess(final LiveGiftModel giftModel) {
        if (giftModel != null) {
            SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<UserModel>() {
                @Override
                public UserModel onBackground() {
                    return UserModelDao.payDiamonds(giftModel.getDiamonds());
                }

                @Override
                public void onMainThread(UserModel result) {
                    updateDiamonds(result);
                }
            });
        }
    }

    /**
     * 开始连击倒计时
     */
    private void startCountDownNumberLooper() {
        resetCountDownNumber();
        mLooper.start(DURATION_COUNT, mCountDownNumberRunnable);
    }

    private Runnable mCountDownNumberRunnable = new Runnable() {

        @Override
        public void run() {
            mCountDownNumber--;
            if (mCountDownNumber <= 0) {
                resetClick();
            } else {
                if (mClickNumber > 0) {
                    tv_continue_number.setText("X" + mClickNumber);
                }
                tv_count_down_number.setText(String.valueOf(mCountDownNumber));
            }
        }
    };

    protected void clickCharge() {
        LiveRechargeDialog dialog = new LiveRechargeDialog(getActivity());
        dialog.showCenter();
    }

    private void resetClick() {
        mLooper.stop();
        mClickNumber = 0;
        tv_continue_number.setText("");
        hideContinueMode();
    }

    private void resetCountDownNumber() {
        mCountDownNumber = DURATION_CONTINUE / DURATION_COUNT;
    }

    public LiveGiftModel getSelectedGiftModel() {
        return mAdapterGift.getSelectManager().getSelectedItem();
    }

    /**
     * 请求礼物列表
     */
    public void requestData() {
        if (mAdapterGift.getCount() <= 0) {
            CommonInterface.requestGift(new AppRequestCallback<App_propActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (actModel.isOk() && !actModel.getList().isEmpty()) {

                        LinearLayoutManager layoutManager_special = new LinearLayoutManager(getActivity());
                        layoutManager_special.setOrientation(LinearLayoutManager.HORIZONTAL);
                        gift_type_recyclerview.setLayoutManager(layoutManager_special);
                        liveGiftTypelAdapter = new LiveGiftTypelAdapter(actModel.getList(), getActivity());
                        gift_type_recyclerview.setAdapter(liveGiftTypelAdapter);
                        liveGiftTypelAdapter.setItemClickCallback((i, liveGiftTypeModel, view) -> {
                            liveGiftTypelAdapter.setPos(i);
                            liveGiftTypelAdapter.notifyDataSetChanged();
                            if (AppRuntimeWorker.is_vip_user()) {
                                setDataGift(actModel.getList().get(i).getList());
                            } else {
                                List<LiveGiftModel> giftList = new ArrayList<>();

                                for (LiveGiftModel liveGiftModel : actModel.getList().get(i).getList()) {
                                    if (liveGiftModel.getGift_type() != 2) {
                                        giftList.add(liveGiftModel);
                                    }
                                }
                                setDataGift(giftList);
                            }

                        });

                        if (AppRuntimeWorker.is_vip_user()) {
                            setDataGift(actModel.getList().get(0).getList());
                        } else {
                            List<LiveGiftModel> giftList = new ArrayList<>();

                            for (LiveGiftModel liveGiftModel : actModel.getList().get(0).getList()) {
                                if (liveGiftModel.getGift_type() != 2) {
                                    giftList.add(liveGiftModel);
                                }
                            }
                            setDataGift(giftList);
                        }

                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    super.onError(resp);
                }
            });
        } else {
            mAdapterGift.notifyDataSetChanged();
        }
    }

    /**
     * 设置礼物列表数据
     *
     * @param listGift
     */
    public void setDataGift(List<LiveGiftModel> listGift) {
        mAdapterGift.updateData(listGift);
    }

    private boolean validateSend() {
        if (!SDNetworkReceiver.isNetworkConnected(getActivity())) {
            SDToast.showToast("无网络");
            return false;
        }

        if (getSelectedGiftModel() == null) {
            SDToast.showToast("请选择礼物");
            return false;
        }

        if (!UserModelDao.canDiamondsPay(getSelectedGiftModel().getDiamonds())) {
            SDToast.showToast("余额不足");
            return false;
        }

        return true;
    }

    /**
     * 点击发送按钮
     */
    private void clickSend() {
        if (validateSend()) {
            if (getSelectedGiftModel().getIs_much() == 1) {
                showContinueMode();
                clickContinueSend();

            } else {
                //通知发送按钮被点击接口
                if (mCallback != null) {
                    mCallback.onClickSend(getSelectedGiftModel(), 0);
                }
            }
        }
    }

    /**
     * 触发连击调用方法
     */
    protected void clickContinueSend() {
        if (mBlocker.block()) {
            return;
        }

        if (validateSend()) {
            mClickNumber++;
            startCountDownNumberLooper();

            int is_plus = 0;
            if (mClickNumber > 1) {
                is_plus = 1;
            } else {
                is_plus = 0;
            }

            if (mCallback != null) {
                mCallback.onClickSend(getSelectedGiftModel(), is_plus);
            }
        }
    }

    /**
     * 显示连击模式
     */
    private void showContinueMode() {
        SDViewUtil.setGone(tv_send);
        SDViewUtil.setVisible(view_continue_send);
    }

    /**
     * 隐藏连击模式
     */
    private void hideContinueMode() {
        SDViewUtil.setGone(view_continue_send);
        SDViewUtil.setVisible(tv_send);
    }

    /**
     * 本地用户数据更新事件
     *
     * @param event
     */
    public void onEventMainThread(EUpdateUserInfo event) {
        bindUserData();
    }

    @Override
    public void setHeightMatchParent() {
        SDViewUtil.setHeightMatchParent(ll_send_gift_all);
        SDViewUtil.setHeightWeight(view_pager_container, 1);
        SDViewUtil.setHeightMatchParent(vpg_content);
    }

    @Override
    public void setHeightWrapContent() {
        SDViewUtil.setHeightWrapContent(ll_send_gift_all);
        SDViewUtil.setHeightWrapContent(view_pager_container);
        SDViewUtil.setHeightWrapContent(vpg_content);
    }

    public interface SendGiftViewCallback {
        /**
         * 礼物点击
         *
         * @param model   要发送的礼物
         * @param is_plus 1-需要叠加数量，0-不需要叠加数量
         */
        void onClickSend(LiveGiftModel model, int is_plus);
    }

}
