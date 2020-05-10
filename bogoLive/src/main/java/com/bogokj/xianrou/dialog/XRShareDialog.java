package com.bogokj.xianrou.dialog;

import android.app.Activity;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.bogokj.live.R;
import com.bogokj.xianrou.appview.XRUserDynamicDetailShareView;
import com.bogokj.xianrou.interfaces.XRShareClickCallback;

/**
 * <p>文件描述：<p>
 * <p>作者：WYF<p>
 * <p>创建时间：2018/12/19 0019<p>
 * <p>更改时间：2018/12/19 0019<p>
 */
public class XRShareDialog extends SDDialogBase {

    private XRShareClickCallback shareClickCallback;
    XRUserDynamicDetailShareView view_share;
    public XRShareDialog(Activity activity,XRShareClickCallback clickCallback) {
        super(activity);
        this.shareClickCallback = clickCallback;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_xr_share);
        setCanceledOnTouchOutside(true);
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);
        view_share = (XRUserDynamicDetailShareView)findViewById(R.id.view_share);

        view_share.setCallback(shareClickCallback);

    }
}
