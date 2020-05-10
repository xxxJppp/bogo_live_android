package com.bogokj.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bogokj.live.R;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
import com.bogokj.library.utils.SDViewUtil;

import java.util.List;

public class LivePKEmceeListAdapter extends BaseQuickAdapter<UserModel,BaseViewHolder>{
    public LivePKEmceeListAdapter(@Nullable List<UserModel> data) {
        super(R.layout.item_pk_emcee_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserModel item) {
        helper.setText(R.id.item_tv_nick_name,item.getNick_name());
        GlideUtil.load(item.getHead_image()).into((ImageView) helper.getView(R.id.item_tv_avatar));
        ImageView ivGlobalMale = helper.getView(R.id.iv_global_male);
        if (item.getSex() == 1)
        {
            ivGlobalMale.setImageResource(R.drawable.ic_global_male);
        } else if (item.getSex() == 2)
        {
            ivGlobalMale.setImageResource(R.drawable.ic_global_female);
        } else
        {
            SDViewUtil.setGone(ivGlobalMale);
        }

        ImageView iv_rank = helper.getView(R.id.iv_rank);
        iv_rank.setImageResource(LiveUtils.getLevelImageResId(item.getUser_level()));
    }
}
