package com.bogokj.pay.fragment;

import com.bogokj.live.common.AppRuntimeWorker;

/**
 * Created by Administrator on 2017/1/9.按场付费消费记录
 */

public class PaySceneBalanceExpendFragment extends PayBalanceBaseFragment
{
    @Override
    protected void init()
    {
        this.type=0;
        this.live_pay_type=1;
        super.init();
        adapter.setText("消费" + AppRuntimeWorker.getDiamondName() + ":");
    }
}
