package com.bogokj.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.library.drawable.SDDrawable;
import com.fanwe.lib.pulltorefresh.pullcondition.SimpleViewPullCondition;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.SDRecyclerView;
import com.bogokj.live.R;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.event.EUserDynamicListItemChangedEvent;
import com.bogokj.xianrou.event.EUserDynamicListItemRemovedEvent;
import com.bogokj.xianrou.model.QKSmallVideoListModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名: com.bogokj.qingke.fragment
 * 描述:
 * 作者: Su
 * 日期: 2017/7/25 14:48
 **/
public abstract class QKBaseVideoListView extends BaseAppView {
    public QKBaseVideoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public QKBaseVideoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QKBaseVideoListView(Context context) {
        super(context);
        init();
    }

    protected abstract void requestData(final boolean isLoadMore);

    protected abstract boolean subscribeVideoRemovedEvent();

    @ViewInject(R.id.lv_content)
    private SDRecyclerView mListView;
    protected QKTabSmallVideoAdapter mAdapter;
    private ArrayList<QKSmallVideoListModel> mSmallVideoList = new ArrayList<QKSmallVideoListModel>();
    protected int page = 1;
    protected int hasNext = 1;

    protected void init() {
        setContentView(R.layout.qk_frag_video_list);
        x.view().inject(this, getRootView());
        initListView();
        requestData(false);
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                page = 1;
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter() {
                loadMoreViewer();
            }
        });
    }

    private void initListView() {
        mListView.setGridVertical(2);
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new QKTabSmallVideoAdapter(mSmallVideoList, getActivity());
        mListView.setAdapter(mAdapter);
        getPullToRefreshViewWrapper().getPullToRefreshView().setPullCondition(new SimpleViewPullCondition(provideScrollingView()));
        getStateLayout().setAdapter(mAdapter);
    }

    protected View provideScrollingView() {
        return null;
    }

    private void loadMoreViewer() {
        if (hasNext == 1) {
            page++;
            requestData(true);
        } else {
            SDToast.showToast("没有更多数据了");
            onRefreshComplete();
        }
    }

    /**
     * 填充列表数据
     *
     * @param loadMore
     * @param data
     * @param hasNext
     */
    protected void fillData(boolean loadMore, List<QKSmallVideoListModel> data, int hasNext) {
        if (loadMore) {
            appendData(data);
        } else {
            updateData(data);
        }
        this.hasNext = hasNext;
    }

    protected void appendData(List<QKSmallVideoListModel> data) {
        if (mAdapter != null) {
            mAdapter.appendData(data);
        }
    }

    protected void updateData(List<QKSmallVideoListModel> data) {
        if (mAdapter != null) {
            mAdapter.updateData(data);
        }
    }

    protected void onRefreshComplete() {
        if (mListView != null) {
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    public void onEventMainThread(EUserDynamicListItemChangedEvent event) {
        updateVideoPlayCount(event.dynamicId, String.valueOf(event.videoPlayCount));
    }

    public void onEventMainThread(EUserDynamicListItemRemovedEvent event) {
        if (subscribeVideoRemovedEvent()) {
            removeVideo(event.dynamicId);
        }
    }

    protected void updateVideoPlayCount(String weiboId, String count) {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        int n = mAdapter.getItemCount();
        for (int i = 0; i < n; i++) {
            QKSmallVideoListModel model = mAdapter.getData(i);

            if (weiboId.equals(model.getWeibo_id())) {
                model.setVideo_count(count);
                mAdapter.updateData(i, model);
                break;
            }
        }
    }

    protected void removeVideo(String weiboId) {
        int pos = getVideoIndex(weiboId);
        if (pos == -1) {
            return;
        }

        mAdapter.removeData(pos);
    }

    private int getVideoIndex(String weiboId) {
        int result = -1;

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return result;
        }

        int n = mAdapter.getItemCount();
        for (int i = 0; i < n; i++) {
            QKSmallVideoListModel model = mAdapter.getData(i);

            if (weiboId.equals(model.getWeibo_id())) {
                return i;
            }
        }
        return result;
    }

}
