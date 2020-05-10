package com.bogokj.live.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bogokj.hybrid.activity.AppWebViewActivity;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.customview.SDSendValidateButton;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.business.InitBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.event.EFirstLoginNewLevel;
import com.bogokj.live.model.App_do_loginActModel;
import com.bogokj.live.model.App_is_user_verifyActModel;
import com.bogokj.live.model.App_send_mobile_verifyActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveMobielRegisterActivity extends BaseActivity {

    @ViewInject(R.id.ll_image_code)
    private LinearLayout ll_image_code;
    @ViewInject(R.id.et_image_code)
    private EditText et_image_code;
    @ViewInject(R.id.iv_image_code)
    private ImageView iv_image_code;

    @ViewInject(R.id.et_mobile)
    private EditText et_mobile;
    @ViewInject(R.id.et_code)
    private EditText et_code;
    @ViewInject(R.id.btn_send_code)
    private SDSendValidateButton btn_send_code;

    @ViewInject(R.id.iv_login)
    private TextView iv_login;
    @ViewInject(R.id.tv_agreement)
    private TextView tv_agreement;
    @ViewInject(R.id.iv_back)
    private ImageView iv_back;

    private String strMobile;
    private String strImageCode;
    //验证码按钮
    private boolean codebtn = false;
    //登录按钮状态
    private boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_mobile_register);
        init();


        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_titile = initActModel.getPrivacy_title();
            SDViewBinder.setTextView(tv_agreement, privacy_titile);
        }

        //手机号输入监听
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 11) {
                    codebtn = true;
                    //btn_send_code.setBackgroundResource(R.drawable.verify_code_back_focus);
                } else {
                    codebtn = false;
                    //btn_send_code.setBackgroundResource(R.drawable.verify_code_back_unfocus);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //验证码输入的监听
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    login = true;
                    //iv_login.setImageResource(R.drawable.mobile_login_focus);
                } else {
                    login = false;
                    //iv_login.setImageResource(R.drawable.mobile_login_unfocus);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void init() {
        register();
        //initTitle();
        //btn_send_code.setBackgroundResource(R.drawable.verify_code_back_unfocus);
        //iv_login.setImageResource(R.drawable.mobile_login_unfocus);
        reqeustIsUserVerify();
        initSDSendValidateButton();
    }

    private void register() {
        iv_login.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);
        iv_image_code.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initTitle() {
//        mTitle.setMiddleTextTop("手机登录");
    }


    private void initSDSendValidateButton() {
        btn_send_code.setmListener(new SDSendValidateButton.SDSendValidateButtonListener() {
            @Override
            public void onTick() {
            }

            @Override
            public void onStop() {
                //btn_send_code.setBackgroundResource(R.drawable.verify_code_back_focus);
            }

            @Override
            public void onClickSendValidateButton() {
                if (codebtn) {
                    //btn_send_code.setBackgroundResource(R.drawable.verify_code_back_unfocus);
                    requestSendCode();
                }

            }
        });
    }

    private void reqeustIsUserVerify() {
        CommonInterface.requestIsUserVerify(new AppRequestCallback<App_is_user_verifyActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    SDViewUtil.setVisible(ll_image_code);
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.bg_image_loading)
                            .error(R.drawable.bg_image_loading)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate();
                    Glide.with(App.getApplication())
                            .load(actModel.getVerify_url())
                            .apply(options)
                            .into(iv_image_code);
                } else {
                    SDViewUtil.setGone(ll_image_code);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }


    private void requestSendCode() {
        strMobile = et_mobile.getText().toString();
        strImageCode = et_image_code.getText().toString();

        if (TextUtils.isEmpty(strMobile)) {
            SDToast.showToast("请输入手机号码");
            return;
        }
        if (ll_image_code.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(strImageCode)) {
                SDToast.showToast("请输入图形验证码");
                return;
            }
        }


        CommonInterface.requestSendMobileVerify(0, strMobile, strImageCode, new AppRequestCallback<App_send_mobile_verifyActModel>() {
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_image_code:
                reqeustIsUserVerify();
                break;
            case R.id.iv_login:
                clickTvLogin();
                break;
            case R.id.tv_agreement:
                clickAgreement();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void clickAgreement() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_link = initActModel.getPrivacy_link();
            if (!TextUtils.isEmpty(privacy_link)) {
                Intent intent = new Intent(LiveMobielRegisterActivity.this, AppWebViewActivity.class);
                intent.putExtra(AppWebViewActivity.EXTRA_URL, privacy_link);
                intent.putExtra(AppWebViewActivity.EXTRA_IS_SCALE_TO_SHOW_ALL, false);
                startActivity(intent);
            }
        }
    }

    private void clickTvLogin() {
        strMobile = et_mobile.getText().toString();
        if (TextUtils.isEmpty(strMobile)) {
            SDToast.showToast("请输入手机号");
            return;
        }
        String code = et_code.getText().toString();
        if (TextUtils.isEmpty(code)) {
            SDToast.showToast("请输入验证码");
            return;
        }

        /*CommonInterface.requestDoLogin(strMobile, code, new AppRequestCallback<App_do_loginActModel>() {
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
        });*/
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
            if (actModel.getIs_lack() == 1) {
                Intent intent = new Intent(this, LiveDoUpdateActivity.class);
                intent.putExtra(LiveDoUpdateActivity.EXTRA_USER_MODEL, user);
                startActivity(intent);
            } else {
                if (UserModel.dealLoginSuccess(user, true)) {
                    InitBusiness.finishLoginActivity();
                    InitBusiness.startMainActivity(LiveMobielRegisterActivity.this);
                } else {
                    SDToast.showToast("保存用户信息失败");
                }
            }
        } else {
            SDToast.showToast("没有获取到用户信息");
        }
    }
}
