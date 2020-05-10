package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.live.R;
import com.bogokj.live.model.RuleItemModel;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LiveRechrgeDiamondsPaymentRuleAdapter extends SDSimpleAdapter<RuleItemModel> {
    public LiveRechrgeDiamondsPaymentRuleAdapter(List<RuleItemModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_live_recharge_diamonds_payment_rule;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final RuleItemModel model) {
        TextView tv_diamonds = get(R.id.tv_diamonds, convertView);
        TextView tv_name = get(R.id.tv_name, convertView);
        TextView tv_money = get(R.id.tv_money, convertView);
        TextView tv_diamonds_name = get(R.id.tv_diamonds_name, convertView);

        ImageView im_charge_rule_select = get(R.id.im_charge_rule_select, convertView);

        tv_diamonds_name.setText(AppRuntimeWorker.getDiamondName());
        SDViewBinder.setTextView(tv_diamonds, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(tv_name, model.getName());
        SDViewBinder.setTextView(tv_money, "￥" + model.getMoney_name());

        if (model.isSelect()) {
            im_charge_rule_select.setVisibility(View.VISIBLE);
        } else {
            im_charge_rule_select.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemClickCallback(position, model, v);
            }
        });
    }
}
