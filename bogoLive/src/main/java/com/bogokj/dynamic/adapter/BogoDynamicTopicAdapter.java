package com.bogokj.dynamic.adapter;

import android.support.annotation.Nullable;

import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.live.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoDynamicTopicAdapter extends BaseQuickAdapter<BogoDynamicTopicListModel, BaseViewHolder> {

    public BogoDynamicTopicAdapter(@Nullable List<BogoDynamicTopicListModel> data) {
        super(R.layout.item_dynamic_topic,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoDynamicTopicListModel item) {
        helper.setText(R.id.item_tv_topic_name, item.getName());
        helper.setText(R.id.item_tv_topic_discuss, item.getNum() + "人参与讨论");
    }
}
