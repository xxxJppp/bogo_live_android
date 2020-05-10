package com.bogokj.dynamic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoSearchModel;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description: 搜索历史适配器
 * @date :2019/12/17 13:53
 */
public class BogoSearchHistoryAdaper extends BaseQuickAdapter<BogoSearchModel.ListBean, BaseViewHolder> {

    public BogoSearchHistoryAdaper(Context context, @Nullable List<BogoSearchModel.ListBean> data) {
        super(R.layout.item_search_history_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoSearchModel.ListBean item) {
        helper.setText(R.id.item_history_search_tiyle_tv, item.getTheme());
    }
}
