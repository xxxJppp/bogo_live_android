package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.model.BogoWishLisDetailtModel;
import com.bogokj.live.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description: 心愿单 礼物排行
 * @date :2019/12/19 9:29
 */
public class BogoWishRankAdaper extends BaseQuickAdapter<BogoWishLisDetailtModel.ListBean.TopUserBean, BaseViewHolder> {
    public BogoWishRankAdaper(@Nullable List<BogoWishLisDetailtModel.ListBean.TopUserBean> data) {
        super(R.layout.item_bogo_wish_rank_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoWishLisDetailtModel.ListBean.TopUserBean item) {
        GlideUtil.load(item.getHead_image()).into((ImageView) helper.getView(R.id.wish_list_today_top_head_iv));
        helper.setText(R.id.wish_list_today_top_nickname_tv, item.getNick_name());
        helper.setText(R.id.wish_list_today_top_num_tv, item.getGift_num() + "个");

        ImageView rankIv = helper.getView(R.id.wish_list_today_top_rank_iv);
        switch (helper.getPosition()) {
            case 0:
                rankIv.setVisibility(View.VISIBLE);
                rankIv.setImageResource(R.mipmap.wish_today_first_iv);
                break;

            case 1:
                rankIv.setVisibility(View.VISIBLE);
                rankIv.setImageResource(R.mipmap.wish_today_second_iv);
                break;

            case 2:
                rankIv.setVisibility(View.VISIBLE);
                rankIv.setImageResource(R.mipmap.wish_today_third_iv);
                break;

            default:
                rankIv.setVisibility(View.GONE);
                break;
        }
    }
}
