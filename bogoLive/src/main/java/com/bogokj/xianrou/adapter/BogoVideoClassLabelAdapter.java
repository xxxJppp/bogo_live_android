package com.bogokj.xianrou.adapter;

import android.support.annotation.Nullable;

import com.bogokj.live.R;
import com.bogokj.xianrou.model.BogoVideoClassLabelModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoVideoClassLabelAdapter extends BaseQuickAdapter<BogoVideoClassLabelModel, BaseViewHolder> {
    public BogoVideoClassLabelAdapter(@Nullable List<BogoVideoClassLabelModel> data) {
        super(R.layout.item_video_class, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoVideoClassLabelModel item) {
        helper.setText(R.id.item_tv_class_name, item.getName());
    }
}
