package com.bogokj.xianrou.dialog;

import android.app.Activity;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.live.dialog.LiveTipoffTypeDialog;
import com.bogokj.xianrou.common.XRCommonInterface;
import com.bogokj.xianrou.model.XRCommonActionRequestResponseModel;
import com.bogokj.xianrou.model.XRUserDynamicDetailResponseModel;

/**
 * Created by yhz on 2017/9/26.
 */

public class NewXRTipoffTypeDialog extends LiveTipoffTypeDialog {
    private boolean mForUser;
    private String userId;
    private String dynamicId;

    public NewXRTipoffTypeDialog(Activity activity, String userId, String dynamicId, boolean forUser) {
        super(activity, String.valueOf(userId));
        this.mForUser = forUser;
        this.userId = userId;
        this.dynamicId = dynamicId;
    }

    protected void requestTipoff(final long id) {
        if (mForUser) {
            requestReportUser(userId, id);
        } else {
            requestReportDynamic(userId, dynamicId, id);
        }
    }

    private void requestReportDynamic(String userId, String dynamicId, final long id) {
        XRCommonInterface.requestReportUserDynamic(userId,
                dynamicId,
                Long.toString(id), new AppRequestCallback<XRCommonActionRequestResponseModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.isOk()) {
                            dismiss();
                        }
                    }
                });
    }

    private void requestReportUser(String userId, final long id) {
        XRCommonInterface.requestReportUser(userId,
                Long.toString(id),
                new AppRequestCallback<XRCommonActionRequestResponseModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.isOk()) {
                            dismiss();
                        }
                    }
                });
    }
}
