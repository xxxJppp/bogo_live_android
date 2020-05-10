package com.bogokj.dynamic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoSystemMsgModel;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description: 全部话题适配器
 * @date :2019/12/17 13:53
 */
public class BogoSystemMsgAdaper extends BaseQuickAdapter<BogoSystemMsgModel.ListBean, BaseViewHolder> {

    public BogoSystemMsgAdaper(Context context, @Nullable List<BogoSystemMsgModel.ListBean> data) {
        super(R.layout.item_system_msg_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoSystemMsgModel.ListBean item) {
        helper.setText(R.id.item_system_msg_content_tv, item.getTitle());
        helper.setText(R.id.item_system_msg_time_tv, item.getAddtime());

        ImageView headIv = helper.getView(R.id.item_system_msg_head_tv);
        if (TextUtils.isEmpty(item.getUrl())) {
            headIv.setVisibility(View.GONE);
        } else {
            headIv.setVisibility(View.VISIBLE);
            GlideUtil.loadHeadImage(item.getUrl()).into(headIv);
        }


    }
}
