package com.bogokj.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.common.QKCommonInterface;
import com.bogokj.xianrou.model.QKTabSmallVideoModel;

/**
 * Created by Administrator on 2017/7/25.
 */
public class QKMySmallVideoView extends QKBaseVideoListView
{

    public QKMySmallVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public QKMySmallVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QKMySmallVideoView(Context context)
    {
        super(context);
    }

    @Override
    protected void requestData(final boolean isLoadMore)
    {
        QKCommonInterface.requestMySmallVideoList(page, new AppRequestCallback<QKTabSmallVideoModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    synchronized (QKMySmallVideoView.this)
                    {
                        fillData(isLoadMore, actModel.getData(), actModel.getHas_next());
                    }
                    mAdapter.setPage(page);
                    mAdapter.setType(QKTabSmallVideoAdapter.TYPE_MY);
                }
                onRefreshComplete();
            }

            @Override
            protected void onError(SDResponse resp)
            {
                onRefreshComplete();
                super.onError(resp);
            }
        });
    }

    @Override
    protected boolean subscribeVideoRemovedEvent()
    {
        return true;
    }


}
