package com.bogokj.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.common.QKCommonInterface;
import com.bogokj.xianrou.model.QKTabSmallVideoModel;

/**
 * Created by LianCP on 2017/7/20.
 */
public class QKSmallVideoHotListView extends QKBaseVideoListView {

    public QKSmallVideoHotListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public QKSmallVideoHotListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QKSmallVideoHotListView(Context context) {
        super(context);
    }

    @Override
    protected void requestData(final boolean isLoadMore) {
        QKCommonInterface.requestSmallVideoList(page, "1", new AppRequestCallback<QKTabSmallVideoModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    synchronized (QKSmallVideoHotListView.this) {
                        fillData(isLoadMore, actModel.getData(), actModel.getHas_next());
                    }
                    mAdapter.setPage(page);
                    mAdapter.setType(QKTabSmallVideoAdapter.TYPE_LIST);
                }
                onRefreshComplete();
            }

            @Override
            protected void onError(SDResponse resp) {
                onRefreshComplete();
                super.onError(resp);
            }
        });
    }

    @Override
    protected boolean subscribeVideoRemovedEvent() {
        return false;
    }

}
