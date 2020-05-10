package com.bogokj.dynamic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoPeopleNearByModel;
import com.bogokj.dynamic.utils.ImageUtil;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.xianrou.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description: 全部话题适配器
 * @date :2019/12/17 13:53
 */
public class BogoTopPicAdaper extends BaseQuickAdapter<BogoDynamicTopicListModel, BaseViewHolder> {

    public BogoTopPicAdaper(Context context, @Nullable List<BogoDynamicTopicListModel> data) {
        super(R.layout.item_top_pic_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoDynamicTopicListModel item) {
        helper.setText(R.id.item_top_pic_title_tv, item.getName());
        helper.setText(R.id.item_top_pic_hot_num_tv, item.getNum() + "+ 人讨论");

        GlideUtil.loadHeadImage(item.getImg()).into((ImageView) helper.getView(R.id.item_top_pic_head_iv));
    }
}
