package com.bogokj.xianrou.dialog;

import android.app.Activity;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.live.dialog.LiveTipoffTypeDialog;
import com.bogokj.xianrou.common.XRCommonInterface;
import com.bogokj.xianrou.model.XRCommonActionRequestResponseModel;
import com.bogokj.xianrou.model.XRReportTypeModel;
import com.bogokj.xianrou.model.XRUserDynamicDetailResponseModel;

/**
 * Created by yhz on 2017/9/26.
 */

public class XRTipoffTypeDialog extends LiveTipoffTypeDialog
{
    private XRUserDynamicDetailResponseModel mXRUserDynamicDetailResponseModel;
    private boolean mForUser;

    public XRTipoffTypeDialog(Activity activity, XRUserDynamicDetailResponseModel entity, boolean forUser)
    {
        super(activity, String.valueOf(entity.getUser_id()));
        this.mXRUserDynamicDetailResponseModel = entity;
        this.mForUser = forUser;
    }

    protected void requestTipoff(final long id)
    {
        if (mForUser)
        {
            requestReportUser(mXRUserDynamicDetailResponseModel, id);
        } else
        {
            requestReportDynamic(mXRUserDynamicDetailResponseModel, id);
        }
    }

    private void requestReportDynamic(XRUserDynamicDetailResponseModel entity, final long id)
    {
        XRCommonInterface.requestReportUserDynamic(entity.getInfo().getUser_id(),
                entity.getInfo().getWeibo_id(),
                Long.toString(id), new AppRequestCallback<XRCommonActionRequestResponseModel>()
                {
                    @Override
                    protected void onSuccess(SDResponse sdResponse)
                    {
                        if (actModel.isOk())
                        {
                            dismiss();
                        }
                    }
                });
    }

    private void requestReportUser(XRUserDynamicDetailResponseModel entity, final long id)
    {
        XRCommonInterface.requestReportUser(entity.getInfo().getUser_id(),
                Long.toString(id),
                new AppRequestCallback<XRCommonActionRequestResponseModel>()
                {
                    @Override
                    protected void onSuccess(SDResponse sdResponse)
                    {
                        if (actModel.isOk())
                        {
                            dismiss();
                        }
                    }
                });
    }
}
