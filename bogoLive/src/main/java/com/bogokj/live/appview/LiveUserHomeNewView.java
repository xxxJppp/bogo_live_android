package com.bogokj.live.appview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.hybrid.umeng.UmengSocialManager;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserEditActivity;
import com.bogokj.live.activity.LiveUserPhotoActivity;
import com.bogokj.live.activity.LiveUserSettingActivity;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.dialog.InviteRechargeDialog;
import com.bogokj.live.model.App_userinfoActModel;
import com.bogokj.live.model.RoomShareModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by yhz on 2017/8/23.
 */

public class LiveUserHomeNewView extends BaseAppView {
    private TextView tv_nick_name;
    private ImageView iv_vip;
    private ImageView iv_global_male;
    private ImageView iv_rank;

    private TextView tv_user_id;

    private LinearLayout ll_v_explain;
    private TextView tv_v_explain;

    private RoomShareModel mRoomShareModel;//分销分享数据
    private String mQrCode;//分销分享二维码图片路径

    protected App_userinfoActModel mApp_userinfoActModel;
    protected UserModel mUserModel;

    private ImageView im_follow;

    public LiveUserHomeNewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveUserHomeNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveUserHomeNewView(Context context) {
        super(context);
        init();
    }

    protected void init() {
        setContentView(R.layout.include_tab_me_info_new_black);
        initView();
    }

    private void initView() {
        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        iv_vip = (ImageView) findViewById(R.id.iv_vip);
        iv_global_male = (ImageView) findViewById(R.id.iv_global_male);
        iv_rank = (ImageView) findViewById(R.id.iv_rank);
        tv_user_id = (TextView) findViewById(R.id.tv_user_id);

        ll_v_explain = (LinearLayout) findViewById(R.id.ll_v_explain);
        tv_v_explain = (TextView) findViewById(R.id.tv_v_explain);
        im_follow = (ImageView) findViewById(R.id.im_follow);
    }


    public ImageView getFollowView(){
        return im_follow;
    }


    public void setUserData(UserModel user) {
        if (user == null) {
            return;
        }

        this.mUserModel = user;


        SDViewBinder.setTextView(tv_nick_name, user.getNick_name());

        if (user.getIs_vip() == 1) {
            SDViewUtil.setVisible(iv_vip);
        } else {
            SDViewUtil.setGone(iv_vip);
        }
        if (user.getSex() == 0) {
            iv_global_male.setVisibility(GONE);
        } else if (user.getSex() == 1) {
            iv_global_male.setImageResource(R.drawable.ic_global_male);
        } else if (user.getSex() == 2) {
            iv_global_male.setImageResource(R.drawable.ic_global_female);
        }


        iv_rank.setImageResource(user.getLevelImageResId());


        SDViewBinder.setTextView(tv_user_id, user.getShowId());

        if (!TextUtils.isEmpty(user.getV_explain())) {
            SDViewUtil.setVisible(ll_v_explain);
            SDViewBinder.setTextView(tv_v_explain, user.getV_explain());
        } else {
            SDViewUtil.setGone(ll_v_explain);
        }
    }

    public void setData(App_userinfoActModel app_userinfoActModel) {
        if (app_userinfoActModel == null) {
            return;
        }

        this.mApp_userinfoActModel = app_userinfoActModel;
        this.mRoomShareModel = app_userinfoActModel.getShare();
        this.mQrCode = app_userinfoActModel.getQr_code();


        setUserData(app_userinfoActModel.getUser());
    }



    /**
     * 设置
     */
    private void clickSetting() {
        Intent intent = new Intent(getActivity(), LiveUserSettingActivity.class);
        getActivity().startActivity(intent);
    }


    //编辑
    private void clickIvRemark() {
        Intent intent = new Intent(getActivity(), LiveUserEditActivity.class);
        getActivity().startActivity(intent);
    }


    /**
     * 显示邀请码窗口
     */
    private void showInviteRechargeDialog() {
        InviteRechargeDialog mDialog = new InviteRechargeDialog(getActivity());
        mDialog.setImgQrCode(mQrCode);
        mDialog.setInviteRechargeOnClick(new InviteRechargeDialog.InviteRechargeOnClick() {
            @Override
            public void shareOnClick() {
                openShareMessage();
            }
        });
        mDialog.show();
    }

    private void openShareMessage() {
        if (null == mRoomShareModel) {
            return;
        }

        String title = mRoomShareModel.getShare_title();
        String content = mRoomShareModel.getShare_content();
        String imageUrl = mRoomShareModel.getShare_imageUrl();
        String clickUrl = mRoomShareModel.getShare_url();

        UmengSocialManager.openShare(title, content, imageUrl, clickUrl, getActivity(), new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                SDToast.showToast(platform + " 分享成功啦");
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                SDToast.showToast(platform + " 分享失败啦");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                SDToast.showToast(platform + " 分享取消了");
            }
        });
    }
}
