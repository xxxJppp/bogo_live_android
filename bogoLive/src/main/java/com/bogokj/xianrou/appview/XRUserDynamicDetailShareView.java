package com.bogokj.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.umeng.UmengSocialManager;
import com.bogokj.library.utils.SDPackageUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.view.SDAppView;
import com.bogokj.live.R;
import com.bogokj.xianrou.interfaces.XRShareClickCallback;
import com.bogokj.xianrou.util.ViewUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @包名 com.bogokj.xianrou.appview
 * @描述
 * @作者 Su
 * @创建时间 2017/3/24 18:08
 **/
public class XRUserDynamicDetailShareView extends SDAppView
{
    private View mQQButton, mWechatButton, mFriendsCircleButton, mWeiboButton,mQZoneButton;
    private XRShareClickCallback mCallback;


    public XRUserDynamicDetailShareView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initXRUserDynamicDetailShareView();
    }

    public XRUserDynamicDetailShareView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initXRUserDynamicDetailShareView();
    }

    public XRUserDynamicDetailShareView(Context context)
    {
        super(context);
        initXRUserDynamicDetailShareView();
    }

    private void initXRUserDynamicDetailShareView()
    {
        setContentView(R.layout.xr_frag_user_dynamic_detail_share);

        initView();
        initData();
        initListener();
    }

    private void initView()
    {
        mQQButton = findViewById(R.id.fl_button_qq_xr_frag_user_dynamic_share);
        mWechatButton = findViewById(R.id.fl_button_wechat_xr_frag_user_dynamic_share);
        mFriendsCircleButton = findViewById(R.id.fl_button_friends_xr_frag_user_dynamic_share);
        mWeiboButton = findViewById(R.id.fl_button_weibo_xr_frag_user_dynamic_share);
        mQZoneButton = findViewById(R.id.fl_button_qzone_xr_frag_user_dynamic_share);
    }

    private void initData()
    {
        if (UmengSocialManager.isAllSocialDisable() )
        {
            ViewUtil.setViewGone(this);
        }else {
            ViewUtil.setViewVisibleOrGone(mQQButton, UmengSocialManager.isQQEnable());
            ViewUtil.setViewVisibleOrGone(mQZoneButton, UmengSocialManager.isQQEnable());
            ViewUtil.setViewVisibleOrGone(mWechatButton, UmengSocialManager.isWeixinEnable());
            ViewUtil.setViewVisibleOrGone(mFriendsCircleButton, UmengSocialManager.isWeixinEnable());
            ViewUtil.setViewVisibleOrGone(mWeiboButton, UmengSocialManager.isSinaEnable());
        }

    }

    private void initListener()
    {
        mQQButton.setOnClickListener(this);
        mWechatButton.setOnClickListener(this);
        mFriendsCircleButton.setOnClickListener(this);
        mWeiboButton.setOnClickListener(this);
        mQZoneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (ViewUtil.isFastClick())
        {
            return;
        }
        if (v == mQQButton)
        {
            if (!SDPackageUtil.isAppInstalled(App.getApplication(), "com.tencent.mobileqq"))
            {
                SDToast.showToast("你未安装QQ客户端。");
                return;
            }
            getCallback().onShareQQClick(v);
        } else if (v == mWechatButton)
        {
            if (!SDPackageUtil.isAppInstalled(App.getApplication(), "com.tencent.mm"))
            {
                SDToast.showToast("你未安装微信客户端。");
                return;
            }

            getCallback().onShareWechatClick(v);
        } else if (v == mFriendsCircleButton)
        {
            if (!SDPackageUtil.isAppInstalled(App.getApplication(), "com.tencent.mm"))
            {
                SDToast.showToast("你未安装微信客户端。");
                return;
            }
            getCallback().onShareFriendsCircleClick(v);
        } else if (v == mWeiboButton)
        {
            if (!SDPackageUtil.isAppInstalled(App.getApplication(), "com.sina.weibo"))
            {
                SDToast.showToast("你未安装新浪微博客户端。");
                return;
            }
            getCallback().onShareWeiboClick(v);
        }else if (v == mQZoneButton)
        {
            if (!SDPackageUtil.isAppInstalled(App.getApplication(), "com.tencent.mobileqq"))
            {
                SDToast.showToast("你未安装QQ客户端。");
                return;
            }
            getCallback().onShareQZoneClick(v);
        }
    }

    public XRShareClickCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new XRShareClickCallback()
            {
                @Override
                public void onShareQQClick(View view)
                {

                }

                @Override
                public void onShareWechatClick(View view)
                {

                }

                @Override
                public void onShareFriendsCircleClick(View view)
                {

                }

                @Override
                public void onShareWeiboClick(View view)
                {

                }

                @Override
                public void onShareQZoneClick(View view)
                {

                }
            };
        }
        return mCallback;
    }

    public void setCallback(XRShareClickCallback mCallback)
    {
        this.mCallback = mCallback;
    }
}
