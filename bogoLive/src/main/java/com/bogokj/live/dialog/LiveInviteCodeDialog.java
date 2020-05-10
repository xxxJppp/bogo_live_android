package com.bogokj.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.dialog.SDProgressDialog;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.InitActModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_request_check_is_full_invite_codeModel;
import com.bogokj.live.model.App_request_full_invite_codeModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LiveInviteCodeDialog extends SDDialogBase {


    @ViewInject(R.id.tv_cancel)
    protected TextView tv_cancel;
    @ViewInject(R.id.tv_confirm)
    protected TextView tv_confirm;
    @ViewInject(R.id.et_invite_code)
    protected EditText et_invite_code;

    public LiveInviteCodeDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {

        InitActModel initActModel = InitActModelDao.query();
        setCanceledOnTouchOutside(initActModel.getIs_invite_code() == 0);
        setCancelable(initActModel.getIs_invite_code() == 0);
        setContentView(R.layout.dialog_invite_code);
        paddings(0);
        x.view().inject(this, getContentView());

        if (initActModel.getIs_invite_code() == 1) {
            tv_cancel.setVisibility(View.GONE);
        }
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                clickConfirm();
                break;
        }
    }

    private void clickConfirm() {

        String inviteCode = et_invite_code.getText().toString();
        if (TextUtils.isEmpty(inviteCode)) {
            SDToast.showToast("请填写邀请码");
            return;
        }

        final SDProgressDialog progressDialog = new SDProgressDialog(getContext());
        progressDialog.setMessage("正在提交");
        progressDialog.show();
        CommonInterface.requestFullInviteCode(inviteCode, new AppRequestCallback<App_request_full_invite_codeModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                progressDialog.dismiss();
                if (actModel.isOk()) {
                    SDToast.showToast("填写成功");
                    dismiss();
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


    public static void check(final Activity activity) {

        CommonInterface.requestCheckIsFullInviteCode(new AppRequestCallback<App_request_check_is_full_invite_codeModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {

                    if (actModel.getState() == 0) {

                        if (!App.is_invite_code_dialog_showed) {
                            return;
                        }
                        if (activity == null) {
                            return;
                        }

                        LiveInviteCodeDialog inviteCodeDialog = new LiveInviteCodeDialog(activity);
                        inviteCodeDialog.show();

                        App.is_invite_code_dialog_showed = false;
                    }
                }
            }
        });
    }
}
