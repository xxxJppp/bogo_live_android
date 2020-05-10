package com.bogokj.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.adapter.LiveGuardListTableAdapter;
import com.bogokj.live.appview.LiveGuardTableHeaderView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.App_duardian_index_Model;
import com.bogokj.live.model.GuardMemberBean;
import com.bogokj.live.model.GuardTableListModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.library.view.SDRecyclerView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 守护列表弹窗
 */
public class LiveGuardTableDialog extends LiveBaseDialog {

    // private final boolean isCreater;
    private int mDefaultHeight;
    private SDRecyclerView rv_guard_list;
    private LiveGuardListTableAdapter liveGuardListTableAdapter;
    private GuardMemberBean guardMemberBean;

    private List<GuardMemberBean> list = new ArrayList<>();
    //守护人数
    private TextView tv_guard_num;

    //关闭布局
    private RelativeLayout rlshutdown;
    //开通守护
    private TextView tv_open_guard;

    //守护状态
    private TextView tv_guard_state;
    //开通守护布局
    private RelativeLayout rlOpenGuard;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private Activity activity;
    private LiveGuardTableHeaderView liveGuardTableHeaderView;

    public LiveGuardTableDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        initView();
        //获取守护信息


        getGuardInfo();
    }


    /**
     * 添加HeaderView
     */
    private void addHeaderView(GuardMemberBean guardMemberBean) {
        liveGuardTableHeaderView = new LiveGuardTableHeaderView(activity, guardMemberBean, getLiveActivity().getCreaterId());
        mHeaderAndFooterWrapper.addHeaderView(liveGuardTableHeaderView);
    }

    private void initView() {
        setContentView(R.layout.dialog_live_guardian_list);
//        mDefaultHeight = SDViewUtil.getScreenHeightPercent(0.5f);
//        setHeight(mDefaultHeight);
        rv_guard_list = (SDRecyclerView) findViewById(R.id.recycler_guard);

        rlshutdown = findViewById(R.id.rl_shutdown);
        tv_open_guard = findViewById(R.id.tv_open_guard);
        //守护人数
        tv_guard_num = findViewById(R.id.tv_guard_num);

        tv_guard_state = findViewById(R.id.tv_guard_state);

        rlOpenGuard = findViewById(R.id.rl_open_guard);
        //关闭点击
        rlshutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //开通守护
        tv_open_guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //获取守护类型
                CommonInterface.requestGuardType(getLiveActivity().getCreaterId(), new AppRequestCallback<App_duardian_index_Model>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        if (actModel.getStatus() == 1) {
                            LiveOpenGuardDialog liveOpenGuardDialog = new LiveOpenGuardDialog(activity, actModel);
                            liveOpenGuardDialog.showBottom();
                            liveOpenGuardDialog.setCancelable(true);
                            liveOpenGuardDialog.setCanceledOnTouchOutside(true);
                        } else {
                            SDToast.showToast(actModel.getError());
                        }
                    }
                });


            }
        });

        paddings(0);
        mDefaultHeight = SDViewUtil.getScreenHeightPercent(0.6f);

        setHeight(mDefaultHeight);
//        paddingRight(SDViewUtil.dp2px(40));
//        paddingTop(SDViewUtil.dp2px(105));
//        paddingBottom(SDViewUtil.dp2px(105));

        //判断是否是主播
        if (getLiveActivity().getCreaterId().equals(UserModelDao.getUserId())) {
            //隐藏开通守护布局
            rlOpenGuard.setVisibility(View.GONE);
            tv_open_guard.setVisibility(View.GONE);
        } else {
            rlOpenGuard.setVisibility(View.GONE);
            tv_open_guard.setVisibility(View.VISIBLE);
        }


    }

    private void getGuardInfo() {
        //""+164736
        CommonInterface.requestGuardTableList(getLiveActivity().getCreaterId(), new AppRequestCallback<GuardTableListModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    List<GuardMemberBean> listbeans = new ArrayList<>();
                    if (actModel.getIs_guartian().equals("1")) {
//                        tv_guard_state.setText("已开通");
                        tv_guard_state.setText("到期时间：" + actModel.getGuartian_time());
                        tv_open_guard.setText("续费守护");
                    } else {

                    }


                    if (actModel.getList().size() == 0) {
                        tv_guard_num.setText("守护（0人)");
                        guardMemberBean = null;
                        rv_guard_list.setLayoutManager(new LinearLayoutManager(getContext()));
                        liveGuardListTableAdapter = new LiveGuardListTableAdapter(listbeans, getOwnerActivity());
                        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(liveGuardListTableAdapter);
                        addHeaderView(guardMemberBean);
                        rv_guard_list.setAdapter(mHeaderAndFooterWrapper);
                        return;
                    }

                    for (int i = 0; i < actModel.getList().size(); i++) {
                        if (i == 0) {
                            guardMemberBean = actModel.getList().get(0);
                            tv_guard_num.setText("守护（" + actModel.getList().size() + "人)");
                            if (null != guardMemberBean.getTotal_diamonds() && !guardMemberBean.getTotal_diamonds().equals("") && !guardMemberBean.getTotal_diamonds().equals("0")) {

                            } else {
                                listbeans.add(actModel.getList().get(i));
                            }
                        }

                        if (i > 0) {
                            listbeans.add(actModel.getList().get(i));
                        }
                    }
                    //  UserModelDao.updateDiamonds(actModel.getDiamonds());

                    rv_guard_list.setLayoutManager(new LinearLayoutManager(getContext()));
                    liveGuardListTableAdapter = new LiveGuardListTableAdapter(listbeans, getOwnerActivity());
                    mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(liveGuardListTableAdapter);
                    addHeaderView(guardMemberBean);
                    rv_guard_list.setAdapter(mHeaderAndFooterWrapper);
                    //人员列表点击
                    liveGuardListTableAdapter.setItemClickCallback(new SDItemClickCallback<GuardMemberBean>() {
                        @Override
                        public void onItemClick(int position, GuardMemberBean item, View view) {
                            Intent intent = new Intent(getOwnerActivity(), LiveUserHomeActivity.class);
                            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, item.getUid());
                            getOwnerActivity().startActivity(intent);
                        }
                    });


                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

}
