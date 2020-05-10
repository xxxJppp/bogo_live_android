package com.bogokj.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.common.SDHandlerManager;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.model.SDTaskRunnable;
import com.bogokj.library.utils.SDCollectionUtil;
import com.bogokj.library.view.SDRecyclerView;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveMainTabRecommendAdapter;
import com.bogokj.live.appview.LiveTabHotHeaderView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.event.ESelectLiveFinish;
import com.bogokj.live.model.Index_indexActModel;
import com.bogokj.live.model.LiveBannerModel;
import com.bogokj.live.model.LiveFilterModel;
import com.bogokj.live.model.LiveRoomModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author kn update
 * @description: 首页推荐
 * @time 2020/2/16
 */
public class LiveTabRecommendView extends LiveTabBaseView {

    public LiveTabRecommendView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabRecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabRecommendView(Context context) {
        super(context);
        init();
    }


    private SwipeRefreshLayout view_pull_to_refresh;
    private SDRecyclerView sdRecyclerView;
    private LiveTabHotHeaderView mHeaderView;
    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveMainTabRecommendAdapter mAdapter;


    private int mSex;
    private String mCity;

    private int page = 1;

    private void init() {
        setContentView(R.layout.view_live_tab_hot_new);
        view_pull_to_refresh = findViewById(R.id.view_pull_to_refresh);
        sdRecyclerView = (SDRecyclerView) findViewById(R.id.rv_content);
        mAdapter = new LiveMainTabRecommendAdapter(mListModel, getActivity());
        sdRecyclerView.setGridVertical(2);
        sdRecyclerView.setAdapter(mAdapter);
        view_pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
        addHeaderView();
        updateParams();
        requestData();
    }


    /**
     * 添加HeaderView
     */
    private void addHeaderView() {
        mHeaderView = new LiveTabHotHeaderView(getActivity());
        mHeaderView.setBannerItemClickCallback((position, item, view) -> {
            Intent intent = item.parseType(getActivity());
            if (intent != null) {
                getActivity().startActivity(intent);
            }
        });
        mAdapter.addHeaderView(mHeaderView);

    }

    /**
     * 更新接口过滤条件
     */
    private void updateParams() {
        LiveFilterModel model = LiveFilterModel.get();

        mSex = model.getSex();
        mCity = model.getCity();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        startLoopRunnable();
    }

    /**
     * 选择过滤条件完成
     *
     * @param event
     */
    public void onEventMainThread(ESelectLiveFinish event) {
        updateParams();
        startLoopRunnable();
    }

    @Override
    protected void onLoopRun() {
        requestData();
    }

    /**
     * 请求热门首页接口
     */
    private void requestData() {
        CommonInterface.requestIndex(page, mSex, 0, mCity, new AppRequestCallback<Index_indexActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    if (page == 1) {
                        mListModel.clear();
                    }
                    mListModel.addAll(actModel.getList());
                    mHeaderView.setData(actModel);
                    mAdapter.setPageAndType(1, page, mSex, mCity);
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                view_pull_to_refresh.setRefreshing(false);
                super.onFinish(resp);
            }
        });
    }

    @Override
    public void scrollToTop() {
        sdRecyclerView.scrollToStart();
    }

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
            @Override
            public LiveRoomModel onBackground() {
                synchronized (LiveTabRecommendView.this) {
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
                    synchronized (LiveTabRecommendView.this) {
                        mListModel.remove(result);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
