package com.bogokj.live.dialog;

import android.app.Activity;
import android.view.View;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.model.custommsg.CustomMsgApplyLinkMic;
import com.bogokj.live.model.custommsg.CustomMsgRequestPK;

public class LiveCreaterReceiveApplyPKDialog extends AppDialogConfirm {

    private CustomMsgRequestPK customMsgRequestPK;
    private LiveCreaterReceiveApplyLinkMicDialog.ClickListener clickListener;

    public void setClickListener(LiveCreaterReceiveApplyLinkMicDialog.ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public LiveCreaterReceiveApplyPKDialog(Activity activity, CustomMsgRequestPK msg)
    {
        super(activity);
        this.customMsgRequestPK = msg;

        setTextContent(msg.getSender().getNick_name() + "向你发PK请求").setTextCancel("拒绝").setTextConfirm("接受");
        setCallback(new Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {
                if (clickListener != null)
                {
                    clickListener.onClickReject();
                }
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                if (clickListener != null)
                {
                    clickListener.onClickAccept();
                }
            }
        });
    }

    @Override
    public void show()
    {
        startDismissRunnable(10 * 1000);
        super.show();
    }

    public interface ClickListener
    {
        void onClickAccept();

        void onClickReject();
    }
}
