package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.model.BogoNobleListModel;
import com.bogokj.live.model.BogoWishListModel;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoNobleListAdapter extends BaseQuickAdapter<BogoNobleListModel, BaseViewHolder> {
    public BogoNobleListAdapter(@Nullable List<BogoNobleListModel> data) {
        super(R.layout.item_noble_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoNobleListModel item) {
        helper.setText(R.id.item_noble_nickname_tv, item.getId());
    }
}
