package com.bogokj.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.map.tencent.SDTencentMapManager;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.common.QKCommonInterface;
import com.bogokj.xianrou.model.QKTabSmallVideoModel;

/**
 * Created by liangyuan on 2019/1/23.
 * 小视频附近
 */

public class QKSmallVideoNearbyListView extends QKBaseVideoListView
{

    public QKSmallVideoNearbyListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public QKSmallVideoNearbyListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QKSmallVideoNearbyListView(Context context)
    {
        super(context);
    }

    @Override
    protected void requestData(final boolean isLoadMore)
    {


       // Toast.makeText(getActivity(),"lat:"+SDTencentMapManager.getInstance().getLatitude()+"lng:"+SDTencentMapManager.getInstance().getLongitude(),Toast.LENGTH_LONG).show();
        QKCommonInterface.requestNearbySmallVideoList(page, "3",""+SDTencentMapManager.getInstance().getLatitude(),""+SDTencentMapManager.getInstance().getLongitude(),new AppRequestCallback<QKTabSmallVideoModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {

                    if(page==1&&actModel.getData().size()==0){
                        //Toast.makeText(getActivity(),"数据为空",Toast.LENGTH_SHORT).show();
                        onRefreshComplete();
                          return;
                    }

                    synchronized (QKSmallVideoNearbyListView.this)
                    {
                        fillData(isLoadMore, actModel.getData(), actModel.getHas_next());
                    }
                    mAdapter.setIsshowjuli(true);
                    mAdapter.setPage(page);
                    mAdapter.setType(QKTabSmallVideoAdapter.TYPE_LIST);
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
        return false;
    }

}
