package com.bogokj.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.model.App_pk_getTime_Model;
import com.bogokj.live.model.TimeModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.view.PkTimePopup;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;

import java.util.ArrayList;

/**
 * 确认pk dialog
 */
public class LivePKConfirmDialog extends LiveBaseDialog {

    private UserModel userModel;
    private LiveCreaterBusiness mLiveViewerBusiness;
    private ArrayList<TimeModel> timelist = new ArrayList<>();
    //自己
    private UserModel myModel;

    private String uid;
    private RelativeLayout ll_pk_time;
    private TextView tv_pk_time;
    private TextView confirm_pk;
    private Activity activity;

    private TimeModel timeModel;
//    private CircleImageView  myHead;
//    private CircleImageView  userHead;

    public LivePKConfirmDialog(Activity activity, LiveCreaterBusiness liveViewerBusiness, String useid) {
        super(activity);
        this.activity = activity;
        this.uid = useid;
        this.mLiveViewerBusiness = liveViewerBusiness;
        // userModel = mLiveViewerBusiness.getPkuserModel();
        myModel = UserModelDao.query();
        init();
    }


    private void init() {

        setContentView(R.layout.dialog_live_confirm_pk);
        paddings(0);
        setCanceledOnTouchOutside(true);
        confirm_pk = findViewById(R.id.tv_confirm_pk);
        ll_pk_time = findViewById(R.id.ll_pk_time);
        tv_pk_time = findViewById(R.id.tv_pk_time);
        requestPkTimeList();
//        GlideUtil.loadHeadImage(myModel.getHead_image()).into(myHead);
        //获取PK用户信息
        //getUserInfo( mLiveViewerBusiness.getmApplyPKUserId());

        ll_pk_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LivePKTimeListDialog livePKTimeListDialog = new LivePKTimeListDialog(activity, new LivePKTimeListDialog.TimeSelectListener() {
//                    @Override
//                    public void ontimeselect(TimeModel model) {
//                        timeModel = model;
//                        tv_pk_time.setText(timeModel.getTime()+"分钟");
//                    }
//                });
//                livePKTimeListDialog.showBottom();

                PkTimePopup pkTimePopup = new PkTimePopup(activity, timelist);
                pkTimePopup.setOnPkTimePopupClickListener(new PkTimePopup.OnPkTimePopupClickListener() {
                    @Override
                    public void onTimeSelect(TimeModel model) {
                        timeModel = model;
                        tv_pk_time.setText(timeModel.getTime() + "分钟");
                    }
                });

                pkTimePopup.linkTo(v);
                pkTimePopup.showPopupWindow(v);
            }
        });
        //确认PK
        confirm_pk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDialogConfirm dialog = new AppDialogConfirm(getOwnerActivity());
                dialog.setTextContent("是否发起PK请求？");
                dialog.setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog) {
                        //mLiveViewerBusiness.requestPK(list.get(position).getUser_id());
                        //请求pk
                        if (null != timeModel) {
                            mLiveViewerBusiness.requestPK(uid, timeModel.getId());
                            mLiveViewerBusiness.setmApplyPKUserId(uid);
                        } else {
                            SDToast.showToast("pk时间未设置，无法进行pk");
                        }
                        dismiss();
                    }
                }).show();

            }
        });


//        overpk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
        //   requestList();
    }

    //获取时间列表
    private void requestPkTimeList() {
        CommonInterface.requestGetPKTimeList(new AppRequestCallback<App_pk_getTime_Model>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    timelist.clear();
                    if (actModel.getList().size() == 0) {
                        dismiss();
                        SDToast.showToast("pk时间列表为空，请先添加pk时间");
                    } else {
                        timelist.addAll(actModel.getList());
                        timeModel = timelist.get(0);
                        tv_pk_time.setText(timeModel.getTime() + "分钟");
                    }
                }
            }
        });
    }


}
