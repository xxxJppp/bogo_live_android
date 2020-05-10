package com.bogokj.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.common.QKCommonInterface;
import com.bogokj.xianrou.model.QKTabSmallVideoModel;
import com.fanwe.library.adapter.http.model.SDResponse;

public class BogoVideoClassListView extends QKBaseVideoListView {

    private String videoCate;

    public BogoVideoClassListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BogoVideoClassListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BogoVideoClassListView(Context context, String cate) {
        super(context);
        this.videoCate = cate;
        requestData(false);
    }


    @Override
    protected void requestData(final boolean isLoadMore) {
        if (videoCate == null) {
            return;
        }

        QKCommonInterface.requestSmallVideoClassList(page, videoCate, new AppRequestCallback<QKTabSmallVideoModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    synchronized (BogoVideoClassListView.this) {
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
