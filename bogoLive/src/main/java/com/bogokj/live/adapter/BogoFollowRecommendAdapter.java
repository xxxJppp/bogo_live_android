package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.model.BogoFollowRecommendModel;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoFollowRecommendAdapter extends BaseQuickAdapter<BogoFollowRecommendModel, BaseViewHolder> {
    public BogoFollowRecommendAdapter(@Nullable List<BogoFollowRecommendModel> data) {
        super(R.layout.item_follow_recommend,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoFollowRecommendModel item) {
        helper.setText(R.id.item_tv_name,item.getNick_name());
        GlideUtil.loadHeadImage(item.getHead_image()).into((ImageView) helper.getView(R.id.item_iv_avatar));

        helper.addOnClickListener(R.id.item_iv_avatar);
        helper.addOnClickListener(R.id.item_tv_follow);
    }
}
