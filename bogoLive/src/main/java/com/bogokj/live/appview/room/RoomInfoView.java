package com.bogokj.live.appview.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveContActivity;
import com.bogokj.live.adapter.LiveViewerListRecyclerAdapter;
import com.bogokj.live.appview.RoomSdkInfoView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.BogoNobleListDialog;
import com.bogokj.live.dialog.LiveGuardTableDialog;
import com.bogokj.live.dialog.LiveUserInfoDialog;
import com.bogokj.live.event.ERequestFollowSuccess;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.App_get_videoActModel;
import com.bogokj.live.model.BogoWishLisDetailtModel;
import com.bogokj.live.model.RoomUserModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.view.EasyLayoutListener;
import com.bogokj.live.view.EasyLayoutScroll;
import com.bogokj.live.view.NoDoubleListener;
import com.bogokj.live.view.ZzHorizontalProgressBar;
import com.bogokj.xianrou.util.StringUtils;
import com.fanwe.lib.animator.SDAnimSet;
import com.fanwe.lib.animator.listener.OnEndGone;
import com.fanwe.lib.animator.listener.OnEndReset;
import com.fanwe.lib.blocker.SDOnClickBlocker;
import com.fanwe.lib.blocker.SDRunnableBlocker;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.SDRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author kn
 * @description: 直播间顶部view
 * @time kn 2019/12/23
 */
public class RoomInfoView extends RoomView {

    private RelativeLayout llWish;
    private int roomId;
    private BogoNobleListDialog bogoNobleListDialog;

    public RoomInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomInfoView(Context context, int roomId) {
        super(context);
        this.roomId = roomId;
        init();
    }

    private View ll_click_creater;
    private ImageView iv_head_image;
    private ImageView iv_level;
    protected TextView tv_video_title;
    private TextView tv_viewer_number;
    private View view_operate_viewer;
    private View view_add_viewer;
    private View view_minus_viewer;
    private SDRecyclerView hlv_viewer;
    private LinearLayout ll_ticket;
    private TextView tv_ticket;
    private TextView tv_user_number;
    private LinearLayout ll_follow;
    private TextView tv_creater_leave;
    private RoomSdkInfoView view_sdk_info;

    private LiveViewerListRecyclerAdapter adapter;
    private int hasFollow;
    private SDRunnableBlocker mScrollToStartRunnableBlocker;
    private ClickListener clickListener;

    private LinearLayout ll_guardian;

    private LinearLayout ll_middle;
    //守护布局
    private LinearLayout ll_guard;
    //守护人数
    private TextView tv_guard_num;

    public static final int MIN_CLICK_DELAY_TIME = 8000;
    private long lastGetDataTime = 0;
    /**
     * 跑马灯上下轮播
     */
    private EasyLayoutScroll easylayoutscroll;
    private List<View> views = new ArrayList<>();
    private List<BogoWishLisDetailtModel.ListBean> wishList = new ArrayList();

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_info;
    }

    protected void init() {
        ll_click_creater = find(R.id.ll_click_creater);
        iv_head_image = find(R.id.iv_head_image);
        iv_level = find(R.id.iv_level);
        tv_video_title = find(R.id.tv_video_title);
        tv_viewer_number = find(R.id.tv_viewer_number);
        view_operate_viewer = find(R.id.view_operate_viewer);
        view_add_viewer = find(R.id.view_add_viewer);
        view_minus_viewer = find(R.id.view_minus_viewer);
        hlv_viewer = find(R.id.hlv_viewer);
        ll_ticket = find(R.id.ll_ticket);
        tv_ticket = find(R.id.tv_ticket);
        tv_user_number = find(R.id.tv_user_number);
        ll_follow = find(R.id.ll_follow);
        tv_creater_leave = find(R.id.tv_creater_leave);
        view_sdk_info = find(R.id.view_sdk_info);
        //守护布局
        ll_guard = find(R.id.ll_guard);
        tv_guard_num = find(R.id.tv_guard_num);

        llWish = find(R.id.ll_wish);
        easylayoutscroll = find(R.id.upview1);

        view_add_viewer.setOnClickListener(this);
        view_minus_viewer.setOnClickListener(this);
        SDOnClickBlocker.setOnClickListener(ll_follow, 1000, this);
        ll_ticket.setOnClickListener(this);
        ll_click_creater.setOnClickListener(this);
        ll_guard.setOnClickListener(this);

        findViewById(R.id.live_noble_iv).setOnClickListener(v -> {
            bogoNobleListDialog = new BogoNobleListDialog(getActivity(), roomId);
            bogoNobleListDialog.show();
        });

        mScrollToStartRunnableBlocker = new SDRunnableBlocker();
        mScrollToStartRunnableBlocker.setMaxBlockCount(30);

        hlv_viewer.setLinearHorizontal();
        adapter = new LiveViewerListRecyclerAdapter(getActivity(), getLiveActivity());
        hlv_viewer.setAdapter(adapter);


        ll_guardian = find(R.id.ll_guardian);
        ll_guardian.setOnClickListener(this);

        ll_middle = find(R.id.ll_middle);

        initdata();
    }


    public void refreshWish() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastGetDataTime > MIN_CLICK_DELAY_TIME) {
            Log.e("RoomInfoView", "搞事情" + lastGetDataTime + "=" + currentTime);
            stopPlay();
            initdata();
        } else {
            Log.e("RoomInfoView", "操作频繁");
        }
        lastGetDataTime = currentTime;
    }

    private void initdata() {

        CommonInterface.getTodayWishList(new AppRequestCallback<BogoWishLisDetailtModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    wishList.clear();
                    wishList.addAll(actModel.getList());

                    if (wishList.size() > 0) {
                        llWish.setVisibility(VISIBLE);
                        setViews();
                    } else {
                        llWish.setVisibility(GONE);
                    }

                }
            }
        });
    }


    private void setViews() {
        views.clear();
        for (int i = 0; i < wishList.size(); i++) {
            RelativeLayout moreView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_view_single, null);
            TextView giftName = moreView.findViewById(R.id.top_wish_gift_name_tv);
            TextView giftProgress = moreView.findViewById(R.id.top_wish_gift_progress_tv);
            ZzHorizontalProgressBar giftProgressPb = moreView.findViewById(R.id.top_wish_gift_progress_pb);
            ImageView giftIcon = moreView.findViewById(R.id.top_wish_gift_icon_iv);

            GlideUtil.loadHeadImage(wishList.get(i).getG_icon()).into(giftIcon);
            giftName.setText(wishList.get(i).getG_name());
            giftProgress.setText(wishList.get(i).getG_now_num() + "/" + wishList.get(i).getG_num());

            double value = StringUtils.toDouble(wishList.get(i).getG_now_num() + "") / StringUtils.toDouble(wishList.get(i).getG_num() + "");
            int progress = (int) (value * 100);
            giftProgressPb.setProgress(progress);

            views.add(moreView);
        }
        //设置数据集
        easylayoutscroll.setEasyViews(views);
        //开始滚动
        easylayoutscroll.startScroll();

        easylayoutscroll.setOnItemClickListener((pos, view) -> Toast.makeText(getContext(), "您点击了第" + pos + "条索引", Toast.LENGTH_SHORT).show());
    }


    public void setTextVideoTitle(String text) {
        tv_video_title.setText(text);
    }

    public RoomSdkInfoView getSdkInfoView() {
        return view_sdk_info;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == view_add_viewer) {
            if (clickListener != null) {
                clickListener.onClickAddViewer(v);
            }
        } else if (v == view_minus_viewer) {
            if (clickListener != null) {
                clickListener.onClickMinusViewer(v);
            }
        } else if (v == ll_follow) {
            clickFollow();
        } else if (v == ll_ticket) {
            clickTicket();
        } else if (v == ll_click_creater) {
            String id = getLiveActivity().getCreaterId();
            clickHeadImage(id);
        } else if (v == ll_guardian) {
            clickGuardian();
        } else if (v == ll_guard) {
            //守护列表弹窗
            clickGuard();
        }
    }

    //点击守护，获取守护列表
    private void clickGuard() {
        LiveGuardTableDialog liveGuardianDialog = new LiveGuardTableDialog(getActivity());
        liveGuardianDialog.showBottom();
        liveGuardianDialog.setCancelable(true);
        liveGuardianDialog.setCanceledOnTouchOutside(true);
    }


    private void clickGuardian() {
        LiveGuardTableDialog liveGuardianDialog = new LiveGuardTableDialog(getActivity());
        liveGuardianDialog.showBottom();
    }

    /**
     * 显示隐藏私密直播的邀请观众view
     *
     * @param show
     */
    public void showOperateViewerView(boolean show) {
        if (show) {
            SDViewUtil.setVisible(view_operate_viewer);
        } else {
            SDViewUtil.setGone(view_operate_viewer);
        }
    }

    /**
     * 绑定数据
     *
     * @param actModel
     */
    public void bindData(App_get_videoActModel actModel) {
        if (actModel == null) {
            return;
        }

        if (!TextUtils.isEmpty(actModel.getVideo_title())) {
            setTextVideoTitle(actModel.getVideo_title());
        } else {
            if (getLiveActivity().isPlayback()) {
                setTextVideoTitle("精彩回放");
            } else {
                setTextVideoTitle("直播Live");
            }
        }

        RoomUserModel createrModel = actModel.getPodcast();
        if (createrModel != null) {
            UserModel creater = createrModel.getUser();
            if (!creater.equals(UserModelDao.query())) {
                bindHasFollow(createrModel.getHas_focus(), false);
            } else {
                SDViewUtil.setGone(ll_follow);
            }
        }

        updateGuard(StringUtils.toInt(actModel.getGuardian_num()));
    }

    protected void clickFollow() {
        CommonInterface.requestFollow(getLiveActivity().getCreaterId(), getLiveActivity().getRoomId(), new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    bindHasFollow(actModel.getHas_focus(), true);
                }
            }
        });
    }

    protected void clickTicket() {
        Intent intent = new Intent(getActivity(), LiveContActivity.class);
        intent.putExtra(RoomContributionView.EXTRA_ROOM_ID, getLiveActivity().getRoomId());
        intent.putExtra(RoomContributionView.EXTRA_USER_ID, getLiveActivity().getCreaterId());
        getActivity().startActivity(intent);
    }


    /**
     * 绑定主播数据
     *
     * @param user
     */
    public void bindCreaterData(UserModel user) {
        GlideUtil.loadHeadImage(user.getHead_image()).into(iv_head_image);
        if (TextUtils.isEmpty(user.getV_icon())) {
            SDViewUtil.setGone(iv_level);
        } else {
            SDViewUtil.setVisible(iv_level);
            GlideUtil.load(user.getV_icon()).into(iv_level);
        }
        SDViewBinder.setTextView(tv_user_number, String.valueOf(user.getShowId()));
        updateTicket(user.getTicket());

    }

    /**
     * 更新印票数量
     *
     * @param ticket
     */
    public void updateTicket(long ticket) {
        if (ticket < 0) {
            ticket = 0;
        }
        SDViewBinder.setTextView(tv_ticket, String.valueOf(ticket));
    }

    /**
     * 更新守护数量
     *
     * @param guardnum
     */
    public void updateGuard(int guardnum) {
        if (guardnum == 0) {
            ll_guard.setVisibility(VISIBLE);
            SDViewBinder.setTextView(tv_guard_num, "虚席以待");
        } else {
            ll_guard.setVisibility(VISIBLE);
            SDViewBinder.setTextView(tv_guard_num, String.valueOf(guardnum) + "人");
        }

    }


    /**
     * 更新观众列表
     *
     * @param listModel
     */
    public void onLiveRefreshViewerList(List<UserModel> listModel) {
        adapter.updateData(listModel);
    }

    /**
     * 移除观众
     *
     * @param model
     */
    public void onLiveRemoveViewer(UserModel model) {
        adapter.removeData(model);
    }

    /**
     * 插入观众
     *
     * @param position
     * @param model
     */
    public void onLiveInsertViewer(int position, UserModel model) {
        adapter.insertData(position, model);
        if (position == 0 && hlv_viewer.isIdle()) {
            mScrollToStartRunnableBlocker.postDelayed(mScrollToStartRunnable, 1000);
        }
    }

    private Runnable mScrollToStartRunnable = new Runnable() {
        @Override
        public void run() {
            hlv_viewer.scrollToStart();
        }
    };

    /**
     * 更新观众数量
     *
     * @param viewerNumber
     */
    public void updateViewerNumber(int viewerNumber) {
        if (viewerNumber < 0) {
            viewerNumber = 0;
        }
        SDViewBinder.setTextView(tv_viewer_number, String.valueOf(viewerNumber));
    }

    protected void clickHeadImage(String to_userid) {
        // 显示用户信息窗口
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(getActivity(), to_userid);
        dialog.showCenter();
    }

    /**
     * 显示隐藏主播离开
     *
     * @param show
     */
    public void showCreaterLeave(boolean show) {
        if (show) {
            SDViewUtil.setVisible(tv_creater_leave);
        } else {
            SDViewUtil.setGone(tv_creater_leave);
        }
    }

    public void onEventMainThread(ERequestFollowSuccess event) {
        if (event.userId.equals(getLiveActivity().getCreaterId())) {
            bindHasFollow(event.actModel.getHas_focus(), true);
        }
    }

    public int getHasFollow() {
        return hasFollow;
    }

    private void bindHasFollow(int hasFollow, boolean anim) {
        this.hasFollow = hasFollow;
        if (hasFollow == 1) {
            if (anim) {
                SDAnimSet.from(ll_follow)
                        .scaleX(1, 0).setDuration(200)
                        .withClone().scaleY(1, 0)
                        .addListener(new OnEndGone())
                        .addListener(new OnEndReset())
                        .start();
            } else {
                SDViewUtil.setGone(ll_follow);
            }
        } else {
            if (anim) {
                SDAnimSet.from(ll_follow)
                        .scaleX(0, 1).setDuration(200)
                        .withClone().scaleY(0, 1)
                        .addListener(new OnEndReset())
                        .start();
            } else {
                SDViewUtil.setVisible(ll_follow);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mScrollToStartRunnableBlocker.onDestroy();
    }

    public void stopPlay() {
        if (easylayoutscroll != null) {
            //停止滚动
            easylayoutscroll.stopScroll();
        }
    }

    /**
     * 关闭弹窗
     */
    public void dismissDialog() {
        if (bogoNobleListDialog != null && bogoNobleListDialog.isShowing()) {
            bogoNobleListDialog.dismiss();
        }
    }


    public interface ClickListener {
        void onClickAddViewer(View v);

        void onClickMinusViewer(View v);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
    }
}
