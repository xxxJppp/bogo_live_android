package com.bogokj.dynamic.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_DynamicListModel;

/**
 * @author kn
 * @description: 动态 推荐
 * @time kn 2019/12/17
 */
public class LiveNewDynamicListView extends LiveDynamicListBaseView {

    public LiveNewDynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LiveNewDynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveNewDynamicListView(Context context) {
        super(context);
    }

    @Override
    public void requestData(final boolean isLoadMore) {
        showProgressDialog("");
        CommonInterface.requestDynamicList(page, new AppRequestCallback<App_DynamicListModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    fillData(actModel.getHas_next(), actModel.getList(), isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                onRefreshComplete();
                dismissProgressDialog();
                super.onFinish(resp);
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    @Override
    protected int getDynamicType() {
        return 1;
    }

}
