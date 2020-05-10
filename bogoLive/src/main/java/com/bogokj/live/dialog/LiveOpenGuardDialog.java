package com.bogokj.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveWebViewActivity;
import com.bogokj.live.adapter.LiveGuardSpecialAdapter;
import com.bogokj.live.adapter.LiveGuardTypeAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.model.App_duardian_index_Model;
import com.bogokj.live.model.GuardPrivilegeModel;
import com.bogokj.live.model.GuardTypeModel;
import com.bogokj.live.utils.GlideUtil;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.library.view.SDRecyclerView;

/**
 * 开通守护 dialog
 */
public class LiveOpenGuardDialog extends LiveBaseDialog {
    //守护规则
    private ImageView iv_rule;
    //头像
    private CircleImageView userhead;
    //他的守护
    private LinearLayout ll_ta_guard;
    //开通守护
    private TextView tv_open;
    //我的钻石
    private TextView tv_diamonds_num;

    private ImageView im_close;

    //守护类型recyclerview
    private SDRecyclerView guard_tppe_recyclerview;

    //守护特权recyclerview
    private SDRecyclerView guard_special_recyclerview;

    private App_duardian_index_Model app_duardian_index_model;

    private Activity activity;

    private LiveGuardTypeAdapter liveGuardTypeAdapter;

    private LiveGuardSpecialAdapter liveGuardSpecialAdapter;

    private LinearLayout ll_guard_bg;

    private String guardid;

    private String type;

    private ImageView ivguard;

    //到期时间布局
    private LinearLayout ll_guard_time;
    //到期时间
    private TextView tv_guard_time;

    private String guard_text;
    private int mDefaultHeight;

    public LiveOpenGuardDialog(Activity activity, App_duardian_index_Model app_duardian_index_model) {
        super(activity);
        this.activity = activity;
        this.app_duardian_index_model = app_duardian_index_model;
        guardid = app_duardian_index_model.getData().get(0).getId();
        type = app_duardian_index_model.getData().get(0).getType();
        init();
        initClick();
    }

    private void init() {
        setContentView(R.layout.dialog_open_guard);
        paddings(0);
        mDefaultHeight = SDViewUtil.getScreenHeightPercent(0.5f);

        setHeight(mDefaultHeight);
        setCanceledOnTouchOutside(true);

        iv_rule = findViewById(R.id.iv_guard_rule);
        userhead = findViewById(R.id.user_head);
        ll_ta_guard = findViewById(R.id.ll_ta_guard);
        tv_open = findViewById(R.id.tv_open_guard);
        tv_diamonds_num = findViewById(R.id.tv_diamonds_num);
        im_close = findViewById(R.id.im_close);
        //
        guard_tppe_recyclerview = findViewById(R.id.recycler_guard_type);
        //特权
        guard_special_recyclerview = findViewById(R.id.rv_special_recyclerview);

        ll_guard_bg = findViewById(R.id.ll_guard_bg);
        ivguard = (ImageView) findViewById(R.id.iv_guard);

        ll_guard_time = (LinearLayout) findViewById(R.id.ll_guard_time);

        tv_guard_time = (TextView) findViewById(R.id.tv_guard_time);
        //头像
        GlideUtil.loadHeadImage(app_duardian_index_model.getHead_image()).into(userhead);
        //我的钻石
        tv_diamonds_num.setText(app_duardian_index_model.getDiamonds());
        //守护类型适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        guard_tppe_recyclerview.setLayoutManager(layoutManager);
        liveGuardTypeAdapter = new LiveGuardTypeAdapter(app_duardian_index_model.getData(), activity);
        guard_tppe_recyclerview.setAdapter(liveGuardTypeAdapter);
        if (app_duardian_index_model.getIs_guartian().equals("1")) {
            ivguard.setVisibility(View.VISIBLE);
            ll_guard_time.setVisibility(View.VISIBLE);
            tv_guard_time.setText(app_duardian_index_model.getGuartian_time().toString().split(" ")[0]);
            tv_open.setText("续费守护");

            guard_text = "是否续费守护？";

        } else {
            ivguard.setVisibility(View.GONE);
            ll_guard_time.setVisibility(View.GONE);
            guard_text = "是否开通守护？";

        }
        im_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击
        liveGuardTypeAdapter.setItemClick(new LiveGuardTypeAdapter.ItemClick() {
            @Override
            public void onClick(GuardTypeModel model) {

                guardid = model.getId();
                liveGuardSpecialAdapter.setSpecic(model.getType_name());
                liveGuardSpecialAdapter.notifyDataSetChanged();
            }
        });

        //守护特权适配器
        LinearLayoutManager layoutManager_special = new LinearLayoutManager(activity);
        layoutManager_special.setOrientation(LinearLayoutManager.HORIZONTAL);
        guard_special_recyclerview.setLayoutManager(layoutManager_special);
        liveGuardSpecialAdapter = new LiveGuardSpecialAdapter(app_duardian_index_model.getGuardian_type(), app_duardian_index_model.getData().get(0).getType_name(), activity);
        guard_special_recyclerview.setAdapter(liveGuardSpecialAdapter);

        liveGuardSpecialAdapter.setItemClickCallback(new SDItemClickCallback<GuardPrivilegeModel>() {
            @Override
            public void onItemClick(int position, GuardPrivilegeModel item, View view) {
//                LiveGuardSpecialDetailDialog liveGuardSpecialDetailDialog = new LiveGuardSpecialDetailDialog(activity,item.getId());
//                liveGuardSpecialDetailDialog.showCenter();
//                liveGuardSpecialDetailDialog.setCancelable(true);
//                liveGuardSpecialDetailDialog.setCanceledOnTouchOutside(true);
            }
        });

    }

    //初始化化点击事件
    private void initClick() {
        //守护规则
        iv_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitActModel initActModel = InitActModelDao.query();
                Intent intent = new Intent(activity, LiveWebViewActivity.class);
                intent.putExtra("extra_url", initActModel.getH5_url().getGuartian_details());
                activity.startActivity(intent);

            }
        });
        //他的守护
        ll_ta_guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //他的守护
                LiveGuardTableDialog liveGuardianDialog = new LiveGuardTableDialog(activity);
                liveGuardianDialog.showBottom();
                liveGuardianDialog.setCancelable(true);
                liveGuardianDialog.setCanceledOnTouchOutside(true);
            }
        });
        //开通守护
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppDialogConfirm dialog = new AppDialogConfirm(activity);
                dialog.setTextContent(guard_text);
                dialog.setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog) {
                        dialog.dismiss();
                        openGuard(guardid);

                    }
                }).show();


            }
        });


    }


    //获取用户信息
    public void openGuard(String guardid) {
        CommonInterface.openGuard(getLiveActivity().getCreaterId(), guardid, new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    Toast.makeText(getOwnerActivity(), "开通守护成功", Toast.LENGTH_LONG).show();
                    //                   final String groupId = getLiveActivity().getGroupId();
//                    //发送开通成功的群组消息
//                    IMHelper.sendMsgGroup(groupId, new CustomMsgOpenGuardSuccess(), new TIMValueCallBack<TIMMessage>()
//                    {
//                        @Override
//                        public void onError(int i, String s)
//                        {
//                        }
//
//                        @Override
//                        public void onSuccess(TIMMessage timMessage)
//                        {
//                            SDToast.showToast("发送消息成功");
//
//                        }
//                    });
                    dismiss();
                }
            }
        });

    }


}
