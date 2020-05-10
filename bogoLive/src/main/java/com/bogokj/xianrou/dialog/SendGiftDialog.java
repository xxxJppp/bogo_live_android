package com.bogokj.xianrou.dialog;

import android.app.Activity;
import android.view.Gravity;

import com.bogokj.live.R;
import com.bogokj.live.appview.LiveSendGiftView;
import com.bogokj.live.model.LiveGiftModel;
import com.bogokj.xianrou.appview.XRUserDynamicDetailShareView;
import com.bogokj.xianrou.interfaces.XRShareClickCallback;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

/**
 * <p>文件描述：<p>
 * <p>作者：WYF<p>
 * <p>创建时间：2018/12/19 0019<p>
 * <p>更改时间：2018/12/19 0019<p>
 */
public class SendGiftDialog extends SDDialogBase {


    private LiveSendGiftView view_send_gift;

    public SendGiftDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_send_gift_layout);
        setCanceledOnTouchOutside(true);
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);

        setGrativity(Gravity.BOTTOM);

        view_send_gift = (LiveSendGiftView) findViewById(R.id.view_send_gift);
        view_send_gift.setCallback(new LiveSendGiftView.SendGiftViewCallback() {
            @Override
            public void onClickSend(LiveGiftModel model, int is_plus) {
                ToastUtil.toastLongMessage("送礼物");
            }
        });


    }
}
