package com.bogokj.live.adapter;

import android.support.annotation.Nullable;

import com.bogokj.live.R;
import com.bogokj.live.model.BogoLiveSoundModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BogoLiveSoundAdapter extends BaseQuickAdapter<BogoLiveSoundModel, BaseViewHolder> {
    public BogoLiveSoundAdapter(@Nullable List<BogoLiveSoundModel> data) {
        super(R.layout.item_live_sound, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoLiveSoundModel item) {
        helper.setText(R.id.item_tv_name, item.getName());
    }
}
