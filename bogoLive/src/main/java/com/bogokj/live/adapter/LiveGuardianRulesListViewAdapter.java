package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.LiveGuardianRulesModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDViewBinder;

import java.util.List;

public class LiveGuardianRulesListViewAdapter extends SDSimpleRecyclerAdapter<LiveGuardianRulesModel> {

//    private LiveCategoryClickCallback<LiveGuardianRulesModel> mItemClickCallback;

    public LiveGuardianRulesListViewAdapter(List<LiveGuardianRulesModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<LiveGuardianRulesModel> holder, final int position, final LiveGuardianRulesModel model) {

        ImageView img;
        TextView name;
        img = holder.get(R.id.img_live_category);
        name = holder.get(R.id.category_name);

        GlideUtil.loadHeadImage(model.getImg()).into(img);

        SDViewBinder.setTextView(name, model.getTitle());

    }

    @Override
    public int getLayoutId(ViewGroup viewGroup, int i) {
        return R.layout.item_image_title;
    }

    public void updateData(List<LiveGuardianRulesModel> listModel) {
        this.setData(listModel);
        this.notifyDataSetChanged();
    }
}
