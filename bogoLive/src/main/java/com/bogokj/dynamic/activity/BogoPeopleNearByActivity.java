package com.bogokj.dynamic.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bogokj.dynamic.adapter.BogoPeopleNearByAdaper;
import com.bogokj.dynamic.modle.BogoPeopleNearByModel;
import com.bogokj.dynamic.modle.DynamicModel;
import com.bogokj.dynamic.view.LiveDynamicListBaseView;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_DynamicListModel;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 附近的人
 * @time kn 2019/12/17
 */
public class BogoPeopleNearByActivity extends BaseActivity implements BaseActivity.TitleButtonClickListener {

    @ViewInject(R.id.people_nearby_list_rv)
    protected RecyclerView peopleListRv;

    @ViewInject(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;


    private List<BogoPeopleNearByModel.ListBean> nearbyList = new ArrayList<>();
    protected int page = 1;
    private BogoPeopleNearByAdaper bogoPeopleNearByAdaper;


    @Override
    public boolean isShowTitle() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bogo_people_near_by);

        initView();

        requestData(true);
    }

    private void initView() {
        initTitle();

        setAdaper();
    }


    private void requestData(boolean isLoadMore) {
        CommonInterface.requestPeopleNearBy(page, new AppRequestCallback<BogoPeopleNearByModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    fillData(actModel.getData(), isLoadMore);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }


    protected void fillData(List<BogoPeopleNearByModel.ListBean> listModels, boolean isLoadMore) {
        sw_refresh.setRefreshing(false);

        if (page == 1) {
            nearbyList.clear();
        }

        if (listModels != null && listModels.size() > 0) {
            nearbyList.addAll(listModels);
            bogoPeopleNearByAdaper.notifyDataSetChanged();
        } else {
            bogoPeopleNearByAdaper.loadMoreEnd();
        }

    }


    private void initTitle() {
        mTitle.setMiddleTextTop("附近的人");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);

        setTitleButtonClickListener(this);
    }

    private void setAdaper() {
        peopleListRv.setLayoutManager(new LinearLayoutManager(this));

        bogoPeopleNearByAdaper = new BogoPeopleNearByAdaper(this, nearbyList);
        peopleListRv.setAdapter(bogoPeopleNearByAdaper);

        sw_refresh.setOnRefreshListener(() -> {
            page = 1;
            requestData(true);
        });

        bogoPeopleNearByAdaper.setOnLoadMoreListener(() -> {
            page++;
            requestData(true);
        }, peopleListRv);

        bogoPeopleNearByAdaper.setEmptyView(R.layout.view_state_empty_content);

    }


    protected void onRefreshComplete() {
        if (peopleListRv != null) {
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }

    @Override
    public void onLeftTitleButtonClickListener() {
        finish();
    }

    @Override
    public void onRightTitleButtonClickListener() {

    }

    @Override
    public void onMiddleTitleButtonClickListener() {

    }
}
