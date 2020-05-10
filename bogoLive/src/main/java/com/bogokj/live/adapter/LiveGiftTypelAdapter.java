package com.bogokj.live.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.LiveGiftTypeModel;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDViewUtil;

import java.util.List;

/**
 * 礼物类型适配器
 */
public class LiveGiftTypelAdapter extends SDSimpleRecyclerAdapter<LiveGiftTypeModel> {
    private int wid;
    private int pos = 0;
    private Activity activity;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public LiveGiftTypelAdapter(List<LiveGiftTypeModel> listModel, Activity activity) {
        super(listModel, activity);
        this.activity = activity;
        int size = listModel.size();
        if (size > 0) {
            wid = SDViewUtil.getScreenWidth() / size;
        } else {
            wid = SDViewUtil.getScreenWidth();
        }
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<LiveGiftTypeModel> holder, final int position, final LiveGiftTypeModel model) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wid, ViewGroup.LayoutParams.MATCH_PARENT);
        holder.findViewById(R.id.item_layout).setLayoutParams(params);
        //特权名称
        ((TextView) holder.findViewById(R.id.gift_type_name)).setText(model.getName());
        if (pos == position) {
            ((TextView) holder.findViewById(R.id.gift_type_name)).setTextColor(Color.parseColor("#9D21FE"));
        } else {
            ((TextView) holder.findViewById(R.id.gift_type_name)).setTextColor(activity.getResources().getColor(R.color.white));
        }


    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_gift_type;

    }


}
