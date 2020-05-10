package com.bogokj.dynamic.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.dynamic.adapter.BogoPeopleNearByAdaper;
import com.bogokj.dynamic.adapter.BogoTopPicAdaper;
import com.bogokj.dynamic.modle.BogoDynamicTopicListApi;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoPeopleNearByModel;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 全部话题
 * @time kn 2019/12/18
 */
public class BogoTopPicActivity extends BaseActivity {

    private static final String TAG = "BogoTopPicActivity";

    @ViewInject(R.id.bogo_top_pic_rv)
    protected RecyclerView topPicRv;

    @ViewInject(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    private BogoTopPicAdaper bogoTopPicAdaper;

    private List<BogoDynamicTopicListModel> topPicList = new ArrayList<>();
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bogo_top_pic);

        initView();

        requestData();
    }

    private void initView() {
        initAdaper();

        findViewById(R.id.top_pic_left_iv).setOnClickListener(this);
        findViewById(R.id.top_pic_search_iv).setOnClickListener(this);
    }

    private void initAdaper() {
        topPicRv.setLayoutManager(new LinearLayoutManager(this));

        bogoTopPicAdaper = new BogoTopPicAdaper(this, topPicList);
        topPicRv.setAdapter(bogoTopPicAdaper);

        sw_refresh.setOnRefreshListener(() -> {
            Log.e(TAG, "setOnRefreshListener");
            page = 1;
            requestData();
        });

        bogoTopPicAdaper.setOnLoadMoreListener(() -> {
            Log.e(TAG, "setOnLoadMoreListener");
            page++;
            requestData();
        }, topPicRv);

        bogoTopPicAdaper.setEmptyView(R.layout.view_state_empty_content);

        bogoTopPicAdaper.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, BogoTopPicDetailActivity.class);
            intent.putExtra("data", topPicList.get(position));
            startActivity(intent);
        });

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.top_pic_left_iv:
                finish();
                break;

            case R.id.top_pic_search_iv:
                startActivity(new Intent(getActivity(), BogoTopPicSearchActivity.class));
                break;
        }
    }

    private void requestData() {

        CommonInterface.requestGetTopicList(page, 0, "", new AppRequestCallback<BogoDynamicTopicListApi>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    sw_refresh.setRefreshing(false);
                    Log.e(TAG, "PAGE=" + page + "=listsize" + actModel.getData().size());

                    if (page == 1) {
                        topPicList.clear();
                    }

                    if (actModel.getData() != null && actModel.getData().size() > 0) {
                        topPicList.addAll(actModel.getData());
                        bogoTopPicAdaper.notifyDataSetChanged();
                        bogoTopPicAdaper.loadMoreComplete();
                    } else {
                        bogoTopPicAdaper.loadMoreEnd();
                    }
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }


}
