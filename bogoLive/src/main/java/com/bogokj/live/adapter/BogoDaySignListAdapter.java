package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.BogoDaySignModel;
import com.bogokj.live.view.RoundRectLayout;
import com.bogokj.xianrou.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoDaySignListAdapter extends BaseQuickAdapter<BogoDaySignModel, BaseViewHolder> {

    public BogoDaySignListAdapter(@Nullable List<BogoDaySignModel> data) {
        super(R.layout.item_day_sign, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoDaySignModel item) {

        helper.setText(R.id.item_tv_day, item.getDay());
        helper.setText(R.id.item_tv_reward, "+" + item.getNum());

        if (StringUtils.toInt(item.getIs_sign()) == 1) {
            helper.setBackgroundRes(R.id.item_iv_sign_success, R.drawable.bg_sign_success_new);
        } else {
            if (StringUtils.toInt(item.getNow()) == 1) {
                helper.setBackgroundRes(R.id.item_iv_sign_success, R.drawable.bg_sign_success);
            } else {
                helper.setBackgroundRes(R.id.item_iv_sign_success, 0);
            }

        }

    }
}
