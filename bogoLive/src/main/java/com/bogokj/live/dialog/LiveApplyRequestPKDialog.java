package com.bogokj.live.dialog;

import android.app.Activity;

import com.bogokj.live.dialog.common.AppDialogConfirm;

/**
 * 申请连麦窗口
 */
public class LiveApplyRequestPKDialog extends AppDialogConfirm
{
    public LiveApplyRequestPKDialog(Activity activity)
    {
        super(activity);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setTextContent("申请PK中，等待对方应答...").setTextConfirm(null).setTextCancel("取消PK");
    }
}
