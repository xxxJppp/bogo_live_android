package com.bogokj.live.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bogokj.live.R;
import com.bogokj.live.model.TimeModel;

import java.util.List;

//pk时间列表适配器
public class LivePkTimeListAdapter extends BaseQuickAdapter<TimeModel,BaseViewHolder>{
   public LivePkTimeListAdapter(@Nullable List<TimeModel> data) {
       super(R.layout.item_pk_time_list,data);
   }

   @Override
   protected void convert(BaseViewHolder helper, TimeModel item) {
       helper.setText(R.id.item_tv_time,item.getTime()+"分钟");
   }
}
