package com.bogokj.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.IMHelper;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveAdminActivity;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.business.LiveViewerBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.common.AppDialogMenu;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.App_forbid_send_msgActModel;
import com.bogokj.live.model.App_set_adminActModel;
import com.bogokj.live.model.App_user_homeActModel;
import com.bogokj.live.model.App_userinfoActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsgLiveMsg;
import com.bogokj.live.model.custommsg.CustomMsgStopLinkMic;
import com.bogokj.live.utils.GlideUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-25 下午3:12:49 类说明
 */
public class LiveHostRefuseDialog extends LiveBaseDialog {
    private LinearLayout ll_close;
    private CircleImageView circleImageView;
    private TextView name;
    private TextView tv_colse;


    private String to_user_id;
    //    private String   username;
//    private String headurl;
    private LiveViewerBusiness liveViewerBusiness;

    public LiveHostRefuseDialog(Activity activity, LiveViewerBusiness liveViewer, String to_user_id) {
        super(activity);
        this.liveViewerBusiness = liveViewer;
        init_id(to_user_id);
        init();
        getUserInfo(to_user_id);
    }
    //获取用户信息
    public void getUserInfo(String userid) {
        CommonInterface.requestUser_home(userid, new AppRequestCallback<App_user_homeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    name.setText(actModel.getUser().getNick_name());
                    if (!TextUtils.isEmpty(actModel.getUser().getHead_image())) {
                        GlideUtil.loadHeadImage(actModel.getUser().getHead_image()).into(circleImageView);
                    }
                }
            }
        });

    }


    private void init_id(String user_id) {
        this.to_user_id = user_id;

    }

    private void init() {

        setContentView(R.layout.dialog_host_refuse);
        ll_close = findViewById(R.id.ll_close);
        name = findViewById(R.id.close_lianmai_name);
        circleImageView = findViewById(R.id.iv_head);
        tv_colse = findViewById(R.id.tv_close_lianmai);


        setCanceledOnTouchOutside(true);
        paddingLeft(80);
        paddingRight(80);
        x.view().inject(this, getContentView());

        //关闭dialog
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //关闭连麦
        tv_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                liveViewerBusiness.stopLinkhost(to_user_id);
//            IMHelper.sendMsgC2C(to_user_id, new CustomMsgStopLinkMic(), null);
            }
        });


    }


}
