package com.bogokj.live.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.appview.ItemLiveTabNewSingle;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.dao.ToJoinLiveData;
import com.bogokj.live.model.LiveRoomModel;

import java.util.List;

public class LiveTabNearbyAdapter extends SDSimpleRecyclerAdapter<LiveRoomModel> {
    private int hei;
    private static int type;
    private static int page;

    public LiveTabNearbyAdapter(List<LiveRoomModel> listModel, Activity activity) {
        super(listModel, activity);
        hei = activity.getWindowManager().getDefaultDisplay().getWidth() / 2;
    }


    public void setPageAndType(int type, int page) {
        this.type = type;
        this.page = page;
    }


    @Override
    public void onBindData(SDRecyclerViewHolder<LiveRoomModel> holder, int position, LiveRoomModel model) {
        if (position % 2 == 0) {  //左边的列
            ((GridLayoutManager.LayoutParams) holder.findViewById(R.id.item_layout).getLayoutParams()).setMargins(0, 0, 4, 4);
            ((GridLayoutManager.LayoutParams) holder.findViewById(R.id.item_layout).getLayoutParams()).height = hei;
        } else {   // 右边的列设置左边的距离
            ((GridLayoutManager.LayoutParams) holder.findViewById(R.id.item_layout).getLayoutParams()).setMargins(0, 0, 0, 4);
            ((GridLayoutManager.LayoutParams) holder.findViewById(R.id.item_layout).getLayoutParams()).height = hei;
        }
        ItemLiveTabNewSingle item0 = holder.get(R.id.item_view0);
        item0.setModel(model);
        item0.setOnClickListener(v -> {
            ItemLiveTabNewSingle view = (ItemLiveTabNewSingle) v;
            LiveRoomModel model1 = view.getModel();
            if (model1 == null) {
                SDToast.showToast("数据为空");
                return;
            }
            ToJoinLiveData toJoinLiveData = new ToJoinLiveData(type, page, position, 0, "");
            AppRuntimeWorker.joinRoom(toJoinLiveData, getData(), model1, getActivity());
        });
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_tab_nearby;
    }


}
