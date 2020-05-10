package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.model.BogoFollowRecommendModel;
import com.bogokj.live.model.BogoFollowRecommendModelApi;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoRecommendEmceeAdapter extends BaseQuickAdapter<BogoFollowRecommendModel, BaseViewHolder> {
    public BogoRecommendEmceeAdapter(@Nullable List<BogoFollowRecommendModel> data) {
        super(R.layout.item_recommend_emcee, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoFollowRecommendModel item) {

        GlideUtil.load(item.getHead_image()).into((ImageView) helper.getView(R.id.item_iv_head));
        helper.setText(R.id.item_tv_name, item.getNick_name());
        if (item.getSex() == 1) {
            helper.setImageResource(R.id.item_iv_sex, R.mipmap.img_sex_man);
        } else {
            helper.setImageResource(R.id.item_iv_sex, R.mipmap.img_sex_woman);
        }

        if (item.isIs_select()) {
            helper.setImageResource(R.id.item_iv_select_status, R.drawable.ic_select_recommend_emcee);
        } else {
            helper.setImageResource(R.id.item_iv_select_status, R.drawable.ic_un_select_recommend_emcee);
        }

    }
}
