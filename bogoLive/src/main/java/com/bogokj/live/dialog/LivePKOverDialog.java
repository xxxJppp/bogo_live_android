package com.bogokj.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.App_user_homeActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.view.CircleImageView;

/**
* 结束pk dialog
* */
public class LivePKOverDialog extends LiveBaseDialog {

     private UserModel userModel;
    private LiveCreaterBusiness mLiveViewerBusiness;
    //自己
   private UserModel myModel;
    private TextView   overpk;
    private CircleImageView myHead;
    private CircleImageView userHead;

    public LivePKOverDialog(Activity activity, LiveCreaterBusiness liveViewerBusiness)
    {
        super(activity);
        this.mLiveViewerBusiness = liveViewerBusiness;
       // userModel = mLiveViewerBusiness.getPkuserModel();
        myModel = UserModelDao.query();
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_live_stop_pk);
        paddings(0);
        setCanceledOnTouchOutside(true);
        overpk = findViewById(R.id.tv_over_pk);
        myHead = findViewById(R.id.iv_myhead);
        userHead = findViewById(R.id.iv_userhead);

        GlideUtil.loadHeadImage(myModel.getHead_image()).into(myHead);
        //获取PK用户信息
        getUserInfo( mLiveViewerBusiness.getmApplyPKUserId());

        overpk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                endPk();
            }
        });
     //   requestList();
    }

    //获取用户信息
    public void getUserInfo(String userid) {
        CommonInterface.requestUser_home(userid, new AppRequestCallback<App_user_homeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (!TextUtils.isEmpty(actModel.getUser().getHead_image())) {
                        GlideUtil.loadHeadImage(actModel.getUser().getHead_image()).into(userHead);
                    }
                }
            }
        });

    }

    //结束pk
    public void endPk() {
        CommonInterface.requestEndLivePk(new AppRequestCallback<String>() {
            @Override
            protected void onSuccess(SDResponse resp) {



//                if (actModel.getStatus() == 1) {
//                    if (!TextUtils.isEmpty(actModel.getUser().getHead_image())) {
//
//                    }
//                }
            }
        });

    }



//    @Override
//    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
//
//        AppDialogConfirm dialog = new AppDialogConfirm(getOwnerActivity());
//        dialog.setTextContent("是否发起PK请求？");
//        dialog.setCallback(new ISDDialogConfirm.Callback()
//        {
//            @Override
//            public void onClickCancel(View v, SDDialogBase dialog)
//            {
//
//            }
//
//            @Override
//            public void onClickConfirm(View v, SDDialogBase dialog)
//            {
////                mLiveViewerBusiness.requestPK(list.get(position).getUser_id());
//                dismiss();
//            }
//        }).show();
//    }


}
