package com.bogokj.baimei.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.baimei.adapter.holder.BMDailyTaskViewHolder;
import com.bogokj.baimei.model.BMDailyTasksListModel;
import com.bogokj.live.R;
import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;

/**
 * 包名: com.bogokj.baimei.adapter
 * 描述: 每日任务列表适配器
 * 作者: Su
 * 创建时间: 2017/5/25 15:41
 **/
public class BMDailyTasksAdapter extends SDRecyclerAdapter<BMDailyTasksListModel> {

    public BMDailyTasksAdapter(Activity activity) {
        super(activity);
    }


    @Override
    public SDRecyclerViewHolder<BMDailyTasksListModel> onCreateVHolder(ViewGroup parent, int viewType) {
        return new BMDailyTaskViewHolder(parent, R.layout.bm_view_holder_daily_tasks);
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<BMDailyTasksListModel> holder, int position, BMDailyTasksListModel model) {
        holder.onBindData(position, model);
        BMDailyTaskViewHolder viewHolder = (BMDailyTaskViewHolder) holder;
        viewHolder.setBMDailyTaskViewHolderCallback(new BMDailyTasksAdapterCallback() {
            @Override
            public void onReceiveAwardClick(View view, BMDailyTasksListModel model, int position) {
                getBMDailyTasksAdapterCallback().onReceiveAwardClick(view, model, position);
            }
        });
    }

    private BMDailyTasksAdapterCallback mBMDailyTasksAdapterCallback;

    private BMDailyTasksAdapterCallback getBMDailyTasksAdapterCallback() {
        if (mBMDailyTasksAdapterCallback == null) {
            mBMDailyTasksAdapterCallback = new BMDailyTasksAdapterCallback() {
                @Override
                public void onReceiveAwardClick(View view, BMDailyTasksListModel model, int position) {

                }
            };
        }
        return mBMDailyTasksAdapterCallback;
    }

    public void setBMDailyTasksAdapterCallback(BMDailyTasksAdapterCallback callback) {
        this.mBMDailyTasksAdapterCallback = callback;
    }

    public interface BMDailyTasksAdapterCallback extends BMDailyTaskViewHolder.BMDailyTaskViewHolderCallback {

    }
}
