package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.model.BogoWishListModel;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoWishListAdapter extends BaseQuickAdapter<BogoWishListModel, BaseViewHolder> {
    public BogoWishListAdapter(@Nullable List<BogoWishListModel> data) {
        super(R.layout.item_wish_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoWishListModel item) {
        helper.setText(R.id.item_tv_wish_gift_count, item.getG_num() + "个");
        helper.setText(R.id.item_tv_wish_gift_name, item.getG_name());
        helper.setText(R.id.item_tv_wish_order, "心愿" + (helper.getPosition() + 1));
        helper.setText(R.id.item_tv_wish_repay, "报答方式：" + item.getTxt());
        GlideUtil.load(item.getG_icon()).into((ImageView) helper.getView(R.id.item_iv_wish_gift_icon));

        helper.addOnClickListener(R.id.item_iv_del);

        if(item.getIs_del() == 1){
            helper.getConvertView().setVisibility(View.GONE);
        }else{
            helper.getConvertView().setVisibility(View.VISIBLE);
        }
    }
}
