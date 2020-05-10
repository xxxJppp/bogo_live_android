package com.bogokj.pay.dialog;

import android.app.Activity;
import android.widget.LinearLayout;

import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.pay.appview.PayUserBalanceContentView;
import com.bogokj.pay.model.App_live_live_pay_deductActModel;

/**
 * Created by Administrator on 2016/12/2.
 */

public class PayUserBalanceDialog extends AppDialogConfirm
{
    private PayUserBalanceContentView payUserBalanceContentView;

    private App_live_live_pay_deductActModel app_live_live_pay_deductActModel;


    public PayUserBalanceDialog(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
//        setTextTitle("温馨提示");
        setCancelable(false);
        setTextConfirm("去充值");
        payUserBalanceContentView = new PayUserBalanceContentView(getOwnerActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        payUserBalanceContentView.setLayoutParams(lp);

        setCustomView(payUserBalanceContentView);
    }

    public void bindData(App_live_live_pay_deductActModel data)
    {
        this.app_live_live_pay_deductActModel = data;
        payUserBalanceContentView.bindData(data);
    }

    /**
     * 是否强制充值
     *
     * @return
     */
    public int getIs_recharge()
    {
        if (app_live_live_pay_deductActModel != null)
        {
            return app_live_live_pay_deductActModel.getIs_recharge();
        }
        return 0;
    }
}
