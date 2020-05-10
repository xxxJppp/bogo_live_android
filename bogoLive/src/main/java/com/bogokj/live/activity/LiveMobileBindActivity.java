package com.bogokj.live.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.customview.SDSendValidateButton;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_ProfitBindingActModel;
import com.bogokj.live.model.App_send_mobile_verifyActModel;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by HSH on 2016/7/19.
 */
public class LiveMobileBindActivity extends LiveMobileBindBaseActivity
{
    @ViewInject(R.id.et_mobile)
    private EditText et_mobile;
    @ViewInject(R.id.et_code)
    private EditText et_code;
    @ViewInject(R.id.btn_send_code)
    private SDSendValidateButton btn_send_code;

    @ViewInject(R.id.iv_mobile_bind)
    private ImageView iv_mobile_bind;

    protected String strMobile;

    //验证码按钮
    private boolean  codebtn = false;
    //提交按钮状态
    private boolean  submit =false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_mobile_bind);
        init();
        initSDSendValidateButton();
        register();

        //手机号输入监听
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==11){
                    codebtn = true;
                    btn_send_code.setBackgroundResource(R.drawable.verify_code_back_focus);
                }else {
                    codebtn = false;
                    btn_send_code.setBackgroundResource(R.drawable.verify_code_back_unfocus);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //验证码
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    submit = true;
                    iv_mobile_bind.setImageResource(R.drawable.mobile_bind_submit_focus);
                }else {
                    submit = false;
                    iv_mobile_bind.setImageResource(R.drawable.mobile_bind_submit_unfocus);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init()
    {
        initTitle();
        btn_send_code.setBackgroundResource(R.drawable.verify_code_back_unfocus);
        iv_mobile_bind.setBackgroundResource(R.drawable.mobile_login_unfocus);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("手机绑定");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
    }

    private void register()
    {
         //提交
        iv_mobile_bind.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(submit) {
                    clickMobileBind();
                }

            }
        });
    }

    private void clickMobileBind()
    {
        String code = et_code.getText().toString();
        if (TextUtils.isEmpty(code))
        {
            SDToast.showToast("请输入验证码");
            return;
        }
        requestMobileBind(code);
    }

    private void initSDSendValidateButton()
    {
        btn_send_code.setmListener(new SDSendValidateButton.SDSendValidateButtonListener()
        {
            @Override
            public void onTick()
            {
            }

            @Override
            public void onStop() {
                btn_send_code.setBackgroundResource(R.drawable.verify_code_back_focus);

            }

            @Override
            public void onClickSendValidateButton()
            {
                if(codebtn){
                    btn_send_code.setBackgroundResource(R.drawable.verify_code_back_unfocus);
                    requestSendCode();
                }

            }
        });
    }

    private void requestSendCode()
    {
        strMobile = et_mobile.getText().toString();

        if (TextUtils.isEmpty(strMobile))
        {
            SDToast.showToast("请输入手机号码");
            return;
        }

        CommonInterface.requestSendMobileVerify(1, strMobile, null, new AppRequestCallback<App_send_mobile_verifyActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    btn_send_code.setmDisableTime(actModel.getTime());
                    btn_send_code.startTickWork();
                }
            }
        });
    }

    @Override
    protected void requestMobileBind(String code)
    {
        CommonInterface.requestMobileBind(strMobile, code, new AppRequestCallback<App_ProfitBindingActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    requestOnSuccess(actModel);
                }
            }
        });
    }

    @Override
    protected void requestOnSuccess(App_ProfitBindingActModel actModel)
    {
        finish();
    }
}
