package com.bogokj.dynamic.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_DynamicListModel;
import com.fanwe.library.adapter.http.model.SDResponse;

/**
 * @author kn
 * @description: 话题圈
 * @time kn 2019/12/17
 */
public class BogoTopPicCircleDynamicListView extends LiveDynamicListBaseView {

    private String themeId;

    public BogoTopPicCircleDynamicListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BogoTopPicCircleDynamicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BogoTopPicCircleDynamicListView(Context context, String id) {
        super(context);
        themeId = id;
    }

    @Override
    public void requestData(final boolean isLoadMore) {
        showProgressDialog("");
        CommonInterface.requestToppicList(page, themeId, new AppRequestCallback<App_DynamicListModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    if (actModel.isOk()) {
                        fillData(actModel.getHas_next(), actModel.getList(), isLoadMore);
                    }
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
        return 0;
    }

}
