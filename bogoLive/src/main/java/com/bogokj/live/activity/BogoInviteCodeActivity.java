package com.bogokj.live.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.dialog.SDProgressDialog;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.databinding.ActivityBogoInviteCodeBinding;
import com.bogokj.live.model.App_request_full_invite_codeModel;
import com.fanwe.library.adapter.http.model.SDResponse;

/**
 * @author kn update
 * @description: 邀请码界面
 * @time 2020/1/4
 */
public class BogoInviteCodeActivity extends BaseTitleActivity {

    private ActivityBogoInviteCodeBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.activity_bogo_invite_code, null, false);
        dataBinding = DataBindingUtil.bind(rootView);
        setContentView(rootView);

        init();
    }

    private void init() {
        initTitle();
        dataBinding.setClickUtils(new ClickClass());
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("邀请码");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
    }


    public class ClickClass {

        //提交邀请码
        public void clickConfirm() {
            String inviteCode = dataBinding.inviteCodeEt.getText().toString();
            if (TextUtils.isEmpty(inviteCode)) {
                SDToast.showToast("请填写邀请码");
                return;
            }

            final SDProgressDialog progressDialog = new SDProgressDialog(BogoInviteCodeActivity.this);
            progressDialog.setMessage("正在提交");
            progressDialog.show();
            CommonInterface.requestFullInviteCode(inviteCode, new AppRequestCallback<App_request_full_invite_codeModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    progressDialog.dismiss();
                    if (actModel.isOk()) {
                        SDToast.showToast("填写成功");
                    } else {
                        SDToast.showToast(actModel.getError());
                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    super.onError(resp);
                    progressDialog.dismiss();
                }
            });
        }
    }

}
