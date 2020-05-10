package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.appview.ItemLiveTabCategorySingle;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.dao.ToJoinLiveData;
import com.bogokj.live.model.LiveRoomModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class LiveTabCategoryAdapter extends SDSimpleAdapter<LiveRoomModel> {
    private int hei;
    private static int type;
    private static int page;


    public LiveTabCategoryAdapter(List<LiveRoomModel> listModel, Activity activity) {
        super(listModel, activity);
        hei = activity.getWindowManager().getDefaultDisplay().getWidth() / 2;
    }

    public void setPageAndType(int type, int page) {
        this.type = type;
        this.page = page;
    }


    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_live_tab_category;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, LiveRoomModel model) {


        ItemLiveTabCategorySingle item0 = get(R.id.item_view0, convertView);
        item0.setModel(model);
        item0.setOnClickListener(v -> {
            ItemLiveTabCategorySingle view = (ItemLiveTabCategorySingle) v;
            LiveRoomModel models = view.getModel();
            if (models == null) {
                SDToast.showToast("数据为空");
                return;
            }
            ToJoinLiveData toJoinLiveData = new ToJoinLiveData(type, page, position, 0, "");
            AppRuntimeWorker.joinRoom(toJoinLiveData, getData(), models, getActivity());
        });
    }


}
