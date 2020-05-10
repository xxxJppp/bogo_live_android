package com.bogokj.dynamic.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bogokj.dynamic.modle.DynamicCommonInfoModel;
import com.bogokj.dynamic.utils.DynamicUtils;
import com.bogokj.dynamic.utils.TimeUtils;
import com.bogokj.hybrid.app.App;
import com.bogokj.live.R;

import java.util.List;

public class DynamicCommonAdapter extends BaseQuickAdapter<DynamicCommonInfoModel,BaseViewHolder> {
    public DynamicCommonAdapter(@Nullable List<DynamicCommonInfoModel> data) {
        super(R.layout.item_dynamic_common,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicCommonInfoModel item) {

        helper.setText(R.id.item_tv_name,item.getNick_name());
        helper.setText(R.id.item_tv_time, TimeUtils.getFriendlyTimeSpanByNow(item.getAddtime() * 1000));
        helper.setText(R.id.item_tv_content,item.getContent());
        DynamicUtils.loadHttpImg(App.getApplication(),item.getHead_image(), (ImageView) helper.getView(R.id.item_iv_avatar),0);
    }
}
