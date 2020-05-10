package com.bogokj.live.adapter.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.live.R;
import com.bogokj.live.adapter.BogoWishRankAdaper;
import com.bogokj.live.model.BogoWishLisDetailtModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.view.ZzHorizontalProgressBar;
import com.bogokj.xianrou.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description:
 * @date :2019/12/19 9:29
 */
public class BogoWishListAdaper extends BaseQuickAdapter<BogoWishLisDetailtModel.ListBean, BaseViewHolder> {
    private Context context;

    public BogoWishListAdaper(Context context, @Nullable List<BogoWishLisDetailtModel.ListBean> data) {
        super(R.layout.item_bogo_wish_list_today_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoWishLisDetailtModel.ListBean item) {

        helper.setText(R.id.wish_list_today_wish_num_tv, "心愿 " + helper.getAdapterPosition() + 1);
        helper.setText(R.id.wish_list_today_giftname_tv, item.getG_name());

        ZzHorizontalProgressBar progressBar = helper.getView(R.id.wish_list_today_progressbar);
        double i = StringUtils.toDouble(item.getG_now_num()+"") / StringUtils.toDouble(item.getG_num()+"");
        int progress = (int)(i * 100);
        progressBar.setProgress(progress);
        Log.e("BogoWishListAdaper", progress + "");


        GlideUtil.loadHeadImage(item.getG_icon()).into((ImageView) helper.getView(R.id.wish_list_today_wish_gift_img_iv));

        helper.setText(R.id.wish_list_today_wish_gift_progress_tv, item.getG_now_num() + "/" + item.getG_num());

        List<BogoWishLisDetailtModel.ListBean.TopUserBean> top_user = item.getTop_user();
        RecyclerView todayRankRv = helper.getView(R.id.wish_list_today_bank_rv);
        todayRankRv.setLayoutManager(new GridLayoutManager(context, 3));
        BogoWishRankAdaper bogoWishRankAdaper = new BogoWishRankAdaper(top_user);
        todayRankRv.setAdapter(bogoWishRankAdaper);

    }
}
