package com.bogokj.live.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.LiveGiftModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;

import java.util.List;

public class LiveGiftAdapter extends SDSimpleAdapter<LiveGiftModel> {
    private GradientDrawable selectedDrawable;

    public LiveGiftAdapter(List<LiveGiftModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_live_gift;
    }

    private Drawable getSelectedDrawable() {
        if (selectedDrawable == null) {
            selectedDrawable = new GradientDrawable();
            selectedDrawable.setShape(GradientDrawable.RECTANGLE);
            selectedDrawable.setCornerRadius(SDViewUtil.dp2px(5));
            selectedDrawable.setStroke(SDViewUtil.dp2px(1), SDResourcesUtil.getColor(R.color.gift_select_color));
        }
        return selectedDrawable;
    }

    @Override
    protected void onUpdateView(int position, View convertView, ViewGroup parent, LiveGiftModel model) {
        if (model.isSelected()) {
            SDViewUtil.setBackgroundDrawable(convertView, getSelectedDrawable());
        } else {
            SDViewUtil.setBackgroundDrawable(convertView, null);
        }
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, LiveGiftModel model) {
        ImageView iv_gift = get(R.id.iv_gift, convertView);

        TextView tv_is_much = get(R.id.tv_is_much, convertView);
        //幸运礼物图标
        ImageView iv_is_luck = get(R.id.tv_is_luck, convertView);
        TextView tv_gift_name = get(R.id.tv_gift_name, convertView);
        TextView tv_price = get(R.id.tv_price, convertView);

        TextView tv_gift_type = get(R.id.tv_gift_type, convertView);

        if (model.getGift_type() == 1) {
            SDViewUtil.setVisible(tv_gift_type);
            tv_gift_type.setText("星榜");
        } else if (model.getGift_type() == 2) {
            SDViewUtil.setVisible(tv_gift_type);
            tv_gift_type.setText("VIP");
        } else {
            SDViewUtil.setGone(tv_gift_type);
        }


        if (model.getIs_much() == 1) {
            SDViewUtil.setVisible(tv_is_much);
        } else {
            SDViewUtil.setGone(tv_is_much);
        }

        //判断是否为幸运礼物
        if (model.getIs_lucky() == 1) {
            SDViewUtil.setVisible(iv_is_luck);
        } else {
            SDViewUtil.setGone(iv_is_luck);
        }

        onUpdateView(position, convertView, parent, model);

        SDViewBinder.setTextView(tv_price, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(tv_gift_name, String.valueOf(model.getName()));

        GlideUtil.load(model.getIcon()).into(iv_gift);
        convertView.setOnClickListener(this);
        LogUtil.i(String.valueOf(position));
    }

}
