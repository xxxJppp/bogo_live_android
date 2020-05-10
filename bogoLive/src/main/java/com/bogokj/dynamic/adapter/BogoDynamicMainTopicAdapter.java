package com.bogokj.dynamic.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoDynamicMainTopicAdapter extends BaseQuickAdapter<BogoDynamicTopicListModel, BaseViewHolder> {
    public BogoDynamicMainTopicAdapter(@Nullable List<BogoDynamicTopicListModel> data) {
        super(R.layout.item_dynamic_top_topic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoDynamicTopicListModel item) {
        helper.setText(R.id.item_tv_topic_name, "#" + item.getName());
        helper.setText(R.id.item_tv_num, item.getNum() + "人参与了该话题");
        GlideUtil.load(item.getImg()).into((ImageView) helper.getView(R.id.item_iv_bg));

    }
}
