package com.bogokj.dynamic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.dynamic.modle.BogoPeopleNearByModel;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.xianrou.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description: 附近的人适配器
 * @date :2019/12/17 13:53
 */
public class BogoPeopleNearByAdaper extends BaseQuickAdapter<BogoPeopleNearByModel.ListBean, BaseViewHolder> {

    public BogoPeopleNearByAdaper(Context context, @Nullable List<BogoPeopleNearByModel.ListBean> data) {
        super(R.layout.item_people_nearby_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoPeopleNearByModel.ListBean item) {
        GlideUtil.loadHeadImage(item.getHead_image()).into((ImageView) helper.getView(R.id.item_people_nearby_head_civ));

        if (!TextUtils.isEmpty(item.getNick_name()))
            helper.setText(R.id.item_people_nearby_nickname_tv, item.getNick_name());

        ImageView sexIv = helper.getView(R.id.item_people_nearby_sex_iv);
        if (StringUtils.toInt(item.getSex()) == 1) {
            sexIv.setImageResource(R.mipmap.img_sex_man);
        } else {
            sexIv.setImageResource(R.mipmap.img_sex_woman);
        }

        if (!TextUtils.isEmpty(item.getLogout_time()))
            helper.setText(R.id.item_people_nearby_time_tv, item.getLogout_time());

        if (!TextUtils.isEmpty(item.getJuli()))
            helper.setText(R.id.item_people_nearby_location_tv, item.getJuli());

        TextView cartificationTv = helper.getView(R.id.item_people_nearby_certification_tv);
        switch (item.getV_type()) {
            case "0":
                cartificationTv.setVisibility(View.GONE);
                break;

            case "1":
                cartificationTv.setVisibility(View.VISIBLE);
                cartificationTv.setText("普通认证");
                break;

            case "2":
                cartificationTv.setVisibility(View.VISIBLE);
                cartificationTv.setText("企业认证");
                break;

            case "3":
                cartificationTv.setVisibility(View.VISIBLE);
                cartificationTv.setText("支付宝认证");
                break;
        }

    }
}
