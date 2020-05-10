package com.bogokj.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.common.SDHandlerManager;
import com.bogokj.library.model.SDTaskRunnable;
import com.bogokj.library.utils.SDCollectionUtil;
import com.bogokj.library.view.SDRecyclerView;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.adapter.BogoFollowRecommendAdapter;
import com.bogokj.live.adapter.LiveMainTabRecommendAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.BogoFollowRecommendModel;
import com.bogokj.live.model.BogoFollowRecommendModelApi;
import com.bogokj.live.model.Index_focus_videoActModel;
import com.bogokj.live.model.LiveRoomModel;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.bogokj.xianrou.widget.recyclerviewheader.RecyclerViewUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 首页关注直播列表
 */
public class LiveTabFollowView extends LiveTabBaseView {

    private RecyclerView recommendListView;
    private BogoFollowRecommendAdapter bogoFollowRecommendAdapter;

    public LiveTabFollowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabFollowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabFollowView(Context context) {
        super(context);
        init();
    }

    private SDRecyclerView contentRv;

    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveMainTabRecommendAdapter mAdapter;


    /**
     * 推荐数据列表
     */
    private List<BogoFollowRecommendModel> mRecommendList = new ArrayList<>();

    private void init() {
        setContentView(R.layout.view_live_tab_follow);
        contentRv = (SDRecyclerView) findViewById(R.id.lv_content);


        //头部布局列表适配器
        View headView = inflate(getContext(), R.layout.view_follow_head, null);
        headView.findViewById(R.id.tv_refresh_recommend).setOnClickListener(this);
        recommendListView = headView.findViewById(R.id.rv_content_list);
        recommendListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bogoFollowRecommendAdapter = new BogoFollowRecommendAdapter(mRecommendList);
        recommendListView.setAdapter(bogoFollowRecommendAdapter);

        bogoFollowRecommendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.item_tv_follow:
                        //点击关注主播
                        CommonInterface.requestFollow(mRecommendList.get(position).getId(), 0, new AppRequestCallback<App_followActModel>() {

                            @Override
                            protected void onSuccess(SDResponse resp) {
                                if (actModel.getStatus() == 1) {
                                    mRecommendList.remove(position);
                                    bogoFollowRecommendAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                        break;
                    case R.id.item_iv_avatar:
                        Intent intent = new Intent(getContext(), LiveUserHomeActivity.class);
                        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, mRecommendList.get(position).getId());
                        getActivity().startActivity(intent);
                        break;
                }

            }
        });


        //列表adaper
        mAdapter = new LiveMainTabRecommendAdapter(mListModel, getActivity());
        mAdapter.addHeaderView(headView);
        contentRv.setGridVertical(2);
        contentRv.setAdapter(mAdapter);
        //关乎空数据页面布局
        getStateLayout().setAdapter(mAdapter);


        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                requestData();
            }

            @Override
            public void onRefreshingFromFooter() {
            }
        });


        requestData();
        requestGetRecommendList();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        requestData();
        super.onActivityResumed(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refresh_recommend:
                requestGetRecommendList();
                break;
            default:
                break;
        }
    }

    /**
     * 请求接口数据
     */
    private void requestData() {
        CommonInterface.requestFocusVideo(new AppRequestCallback<Index_focus_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    synchronized (LiveTabFollowView.this) {
                        mListModel.clear();
                        mListModel.addAll(actModel.getData());
                        mAdapter.setPageAndType(0, 1, 0, "");
                        mAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    private void requestGetRecommendList() {

        CommonInterface.requestGetFollowRecommendList(new AppRequestCallback<BogoFollowRecommendModelApi>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    synchronized (LiveTabFollowView.this) {
                        mRecommendList.clear();
                        mRecommendList.addAll(actModel.getData().getList());
                        bogoFollowRecommendAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    @Override
    public void scrollToTop() {
        contentRv.scrollToStart();
    }

    @Override
    protected void onLoopRun() {
        //关注页面执行定时刷新任务
    }

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
            @Override
            public LiveRoomModel onBackground() {
                synchronized (LiveTabFollowView.this) {
                    if (SDCollectionUtil.isEmpty(mListModel)) {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext()) {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id()) {
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result) {
                if (result != null) {
                    synchronized (LiveTabFollowView.this) {
                        mListModel.remove(result);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
