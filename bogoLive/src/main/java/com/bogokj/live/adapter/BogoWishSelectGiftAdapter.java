package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.model.LiveGiftModel;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoWishSelectGiftAdapter extends BaseQuickAdapter<LiveGiftModel, BaseViewHolder> {
    public BogoWishSelectGiftAdapter(@Nullable List<LiveGiftModel> data) {
        super(R.layout.item_select_wish_gift,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LiveGiftModel item) {
        helper.setText(R.id.item_tv_name,item.getName());
        GlideUtil.load(item.getIcon()).into((ImageView) helper.getView(R.id.item_iv_gift));
    }
}
