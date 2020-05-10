package com.bogokj.live.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bogokj.hybrid.activity.AppWebViewActivity;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.common.CommonOpenLoginSDK;
import com.bogokj.hybrid.constant.ApkConstant;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.event.ERetryInitSuccess;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.live.model.App_do_loginActModel;
import com.bogokj.live.model.App_send_mobile_verifyActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.blocker.SDDurationBlocker;
import com.bogokj.library.customview.SDSendValidateButton;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.business.InitBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.event.EFirstLoginNewLevel;
import com.bogokj.live.model.App_do_updateActModel;
import com.bogokj.live.model.UserModel;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.sunday.eventbus.SDEventManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveLoginActivity extends BaseActivity {
    //微信
    private ImageView iv_weixin;

    //QQ
    private ImageView iv_qq;

    //新浪
    private ImageView iv_sina;

    //手机
    private TextView ll_shouji;

    //游客
    private TextView tv_visitors;

    private TextView tv_agreement;

    private SDDurationBlocker blocker = new SDDurationBlocker(2000);
    private String inviteCode = "";
    private String channel = "";

    @ViewInject(R.id.et_mobile)
    private EditText et_mobile;
    @ViewInject(R.id.et_code)
    private EditText et_code;
    @ViewInject(R.id.btn_send_code)
    private SDSendValidateButton btn_send_code;

    @ViewInject(R.id.ll_other_login)
    private LinearLayout ll_other_login;

    private String strMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIsExitApp = true;
        setFullScreen(true);
        setContentView(R.layout.act_live_login);
        init();
    }

    private void init() {
        if (ApkConstant.AUTO_REGISTER) {
            clickLoginVisitors();
            return;
        }

        register();
        bindDefaultData();
        initLoginIcon();
        initSDSendValidateButton();
    }

    private void register() {
        iv_weixin = find(R.id.iv_weixin);
        iv_qq = find(R.id.iv_qq);
        iv_sina = find(R.id.iv_sina);
        ll_shouji = find(R.id.ll_shouji);
        tv_visitors = find(R.id.tv_visitors);
        tv_agreement = find(R.id.tv_agreement);

        iv_qq.setOnClickListener(this);
        iv_sina.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        ll_shouji.setOnClickListener(this);
        tv_visitors.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);

        tv_visitors.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
    }

    private void initSDSendValidateButton() {
        btn_send_code.setmTextColorEnable(getResources().getColor(R.color.color_rank_tv_CC45FF));
        btn_send_code.setmTextColorDisable(getResources().getColor(R.color.gray));
        btn_send_code.setmBackgroundEnableResId(R.drawable.bg_login_send_code);
        btn_send_code.setmBackgroundDisableResId(R.drawable.bg_login_send_code_disable);
        btn_send_code.updateViewState(true);
        btn_send_code.setmListener(new SDSendValidateButton.SDSendValidateButtonListener() {
            @Override
            public void onTick() {
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onClickSendValidateButton() {
                requestSendCode();
            }
        });
    }

    private void requestSendCode() {
        strMobile = et_mobile.getText().toString();

        if (TextUtils.isEmpty(strMobile)) {
            Toast.makeText(LiveLoginActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }


        CommonInterface.requestSendMobileVerify(0, strMobile, "", new AppRequestCallback<App_send_mobile_verifyActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    btn_send_code.setmDisableTime(actModel.getTime());
                    btn_send_code.startTickWork();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void clickTvLogin() {
        strMobile = et_mobile.getText().toString();
        if (TextUtils.isEmpty(strMobile)) {
            SDToast.showToast("请输入手机号");
            return;
        }
        final String code = et_code.getText().toString();
        if (TextUtils.isEmpty(code)) {
            SDToast.showToast("请输入验证码");
            return;
        }

        //获取OpenInstall安装数据
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                JSONObject data = JSON.parseObject(bindData);

                String inviteCode = "";
                String channel = "";

                if (!TextUtils.isEmpty(bindData)) {
                    inviteCode = data.getString("invite_code");
                    channel = data.getString("channel");
                }
                CommonInterface.requestDoLogin(strMobile, code, inviteCode, channel, new AppRequestCallback<App_do_loginActModel>() {
                    @Override
                    public void onStart() {
                        showProgressDialog("正在登录");
                    }

                    @Override
                    protected void onSuccess(SDResponse resp) {
                        if (actModel.getStatus() == 1) {
                            dealSuccess(actModel);
                            setFirstLoginAndNewLevel(actModel);
                        }
                    }

                    @Override
                    protected void onError(SDResponse resp) {
                        super.onError(resp);
                    }

                    @Override
                    protected void onFinish(SDResponse resp) {
                        super.onFinish(resp);
                        dismissProgressDialog();
                    }
                });

                Log.d("OpenInstall", "getInstall : installData = " + appData.toString());
            }
        });
    }

    private void setFirstLoginAndNewLevel(App_do_loginActModel actModel) {
        InitActModel initActModel = InitActModelDao.query();
        initActModel.setFirst_login(actModel.getFirst_login());
        initActModel.setNew_level(actModel.getNew_level());
        if (!InitActModelDao.insertOrUpdate(initActModel)) {
            SDToast.showToast("保存init信息失败");
        }
        //发送事件首次登陆升级
        EFirstLoginNewLevel event = new EFirstLoginNewLevel();
        SDEventManager.post(event);
    }

    private void dealSuccess(App_do_loginActModel actModel) {
        UserModel user = actModel.getUser_info();
        if (user != null) {

            //登陆完成后出现一次
            App.is_invite_code_dialog_showed = true;

            if (actModel.getIs_lack() == 1) {
                Intent intent = new Intent(this, LiveDoUpdateActivity.class);
                intent.putExtra(LiveDoUpdateActivity.EXTRA_USER_MODEL, user);
                startActivity(intent);
            } else {
                if (UserModel.dealLoginSuccess(user, true)) {
//                    InitBusiness.finishLoginActivity();
                    InitBusiness.startMainActivity(LiveLoginActivity.this);
                } else {
                    SDToast.showToast("保存用户信息失败");
                }
            }
        } else {
            SDToast.showToast("没有获取到用户信息");
        }
    }

    private void bindDefaultData() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_titile = initActModel.getPrivacy_title();
            SDViewBinder.setTextView(tv_agreement, privacy_titile);
        }
    }

    private void initLoginIcon() {
        int otherLoginSize = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            //微信
            int has_wx_login = model.getHas_wx_login();
            if (has_wx_login == 1) {
                SDViewUtil.setVisible(iv_weixin);
                otherLoginSize++;
            } else {
                SDViewUtil.setGone(iv_weixin);
            }

            //QQ
            int has_qq_login = model.getHas_qq_login();
            if (has_qq_login == 1) {
                SDViewUtil.setVisible(iv_qq);
                otherLoginSize++;
            } else {
                SDViewUtil.setGone(iv_qq);
            }

            //新浪
            int has_sina_login = model.getHas_sina_login();
            if (has_sina_login == 1) {
                SDViewUtil.setVisible(iv_sina);
                otherLoginSize++;
            } else {
                SDViewUtil.setGone(iv_sina);
            }

            //手机
            int has_mobile_login = model.getHas_mobile_login();
            if (has_mobile_login == 1) {
                SDViewUtil.setVisible(ll_shouji);
            } else {
                SDViewUtil.setGone(ll_shouji);
            }

//            //游客
//            int has_visitors_login = model.getHas_visitors_login();
//            if (has_visitors_login == 1) {
//                SDViewUtil.setVisible(tv_visitors);
//            } else {
//                SDViewUtil.setGone(tv_visitors);
//            }
        }

        if (otherLoginSize > 0) {
            ll_other_login.setVisibility(View.VISIBLE);
        } else {
            ll_other_login.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (blocker.block()) {
            return;
        }

        if (v == iv_weixin) {
            clickLoginWeiXing();
        } else if (v == iv_qq) {
            clickLoginQQ();
        } else if (v == iv_sina) {
            clickLoginSina();
        } else if (v == ll_shouji) {
            clickLoginShouJi();
        } else if (v == tv_visitors) {
            clickLoginVisitors();
        } else if (v == tv_agreement) {
            clickAgreement();
        }
    }

    private void enableClickLogin(boolean enable) {
        iv_weixin.setClickable(enable);
        iv_qq.setClickable(enable);
        iv_sina.setClickable(enable);
        tv_visitors.setClickable(enable);
    }

    private void clickAgreement() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_link = initActModel.getPrivacy_link();
            if (!TextUtils.isEmpty(privacy_link)) {
                Intent intent = new Intent(LiveLoginActivity.this, AppWebViewActivity.class);
                intent.putExtra(AppWebViewActivity.EXTRA_URL, privacy_link);
                intent.putExtra(AppWebViewActivity.EXTRA_IS_SCALE_TO_SHOW_ALL, false);
                startActivity(intent);
            }
        }
    }

    /*
     * 登录
     * */
    private void doLogin(final String wx, final String openid, final String access_token, final String s) {

        //获取OpenInstall安装数据
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                JSONObject data = JSON.parseObject(bindData);

                if (!TextUtils.isEmpty(bindData)) {
                    inviteCode = data.getString("invite_code");
                    channel = data.getString("channel");
                }

                switch (wx) {
                    case "wx":
                        requestWeiXinLogin(openid, access_token);
                        break;
                    case "qq":
                        requestQQ(openid, access_token);
                        break;
                    case "wb":
                        requestSinaLogin(access_token, s);
                        break;
                    default:
                        break;
                }
                Log.d("OpenInstall", "getInstall : installData = " + appData.toString());
            }
        });


    }

    private void clickLoginWeiXing() {
        CommonOpenLoginSDK.loginWx(this, wxListener);
    }

    /**
     * 微信授权监听
     */
    private UMAuthListener wxListener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            SDToast.showToast("授权成功");
            String openid = map.get("openid");
            String access_token = map.get("access_token");
            //requestWeiXinLogin(openid, access_token);
            doLogin("wx", openid, access_token, "");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    private void clickLoginQQ() {
        CommonOpenLoginSDK.umQQlogin(this, qqListener);
    }

    /**
     * qq授权监听
     */
    private UMAuthListener qqListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SDToast.showToast("授权成功");
            String openid = data.get("openid");
            String access_token = data.get("access_token");
            //requestQQ(openid, access_token);
            doLogin("qq", openid, access_token, "");
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SDToast.showToast("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SDToast.showToast("授权取消");
        }
    };

    private void clickLoginSina() {
        CommonOpenLoginSDK.umSinalogin(this, sinaListener);
    }

    /**
     * 新浪授权监听
     */
    private UMAuthListener sinaListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SDToast.showToast("授权成功");
            String access_token = data.get("access_token");
            String uid = data.get("uid");
            //requestSinaLogin(access_token, uid);
            doLogin("wb", "", access_token, uid);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SDToast.showToast("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SDToast.showToast("授权取消");
        }
    };

    private void clickLoginShouJi() {
//        Intent intent = new Intent(this, LiveMobielRegisterActivity.class);
//        startActivity(intent);
        clickTvLogin();
    }

    private void clickLoginVisitors() {
        CommonInterface.requestLoginVisitorsLogin(new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onCancel(SDResponse resp) {
                super.onCancel(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                dismissProgressDialog();
                if (actModel.isOk()) {
                    startMainActivity(actModel);
                }
            }
        });
    }

    private void requestWeiXinLogin(final String openid, final String access_token) {
        CommonInterface.requestWxLogin(openid, access_token, inviteCode,channel,new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                enableClickLogin(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                enableClickLogin(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getNeed_bind_mobile() == 1) {
                        startBindMobileActivity(LoginType.WX_LOGIN, openid, access_token);
                    } else {
                        startMainActivity(actModel);
                    }
                    setFirstLoginAndNewLevel(actModel);
                }
            }
        });
    }

    private void requestQQ(final String openid, final String access_token) {
        CommonInterface.requestQqLogin(openid, access_token,inviteCode,channel, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                enableClickLogin(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                enableClickLogin(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getNeed_bind_mobile() == 1) {
                        startBindMobileActivity(LoginType.QQ_LOGIN, openid, access_token);
                    } else {
                        startMainActivity(actModel);
                    }
                    setFirstLoginAndNewLevel(actModel);
                }
            }
        });
    }

    private void requestSinaLogin(final String access_token, final String uid) {
        CommonInterface.requestSinaLogin(access_token, uid,inviteCode,channel, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                enableClickLogin(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                enableClickLogin(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getNeed_bind_mobile() == 1) {
                        startBindMobileActivity(LoginType.SINA_LOGIN, uid, access_token);
                    } else {
                        startMainActivity(actModel);
                    }
                    setFirstLoginAndNewLevel(actModel);
                }
            }
        });
    }

    private void setFirstLoginAndNewLevel(App_do_updateActModel actModel) {
        InitActModel initActModel = InitActModelDao.query();
        initActModel.setFirst_login(actModel.getFirst_login());
        initActModel.setNew_level(actModel.getNew_level());
        if (!InitActModelDao.insertOrUpdate(initActModel)) {
            SDToast.showToast("保存init信息失败");
        }
        //发送事件首次登陆升级
        EFirstLoginNewLevel event = new EFirstLoginNewLevel();
        SDEventManager.post(event);
    }

    private void startMainActivity(App_do_updateActModel actModel) {
        UserModel user = actModel.getUser_info();
        if (user != null) {
            if (UserModel.dealLoginSuccess(user, true)) {
                InitBusiness.startMainActivity(LiveLoginActivity.this);
            } else {
                SDToast.showToast("保存用户信息失败");
            }
        } else {
            SDToast.showToast("没有获取到用户信息");
        }
    }

    public void onEventMainThread(ERetryInitSuccess event) {
        bindDefaultData();
        initLoginIcon();
    }

    private void startBindMobileActivity(String loginType, String openid, String access_token) {
        Intent intent = new Intent(getActivity(), LiveLoginBindMobileActivity.class);
        intent.putExtra(LiveLoginBindMobileActivity.EXTRA_LOGIN_TYPE, loginType);
        intent.putExtra(LiveLoginBindMobileActivity.EXTRA_OPEN_ID, openid);
        intent.putExtra(LiveLoginBindMobileActivity.EXTRA_ACCESS_TOKEN, access_token);
        startActivity(intent);
    }


    public static final class LoginType {
        private static final String QQ_LOGIN = "qq_login";
        private static final String WX_LOGIN = "wx_login";
        private static final String SINA_LOGIN = "sina_login";
    }
}
