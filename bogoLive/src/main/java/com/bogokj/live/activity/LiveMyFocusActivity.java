package com.bogokj.live.activity;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_focus_follow_ActModel;

import android.os.Bundle;
import android.view.View;

/**
 * @author kn update
 * @description: 粉丝页面
 * @time 2020/1/2
 */
public class LiveMyFocusActivity extends LiveFocusFollowBaseActivity {
    public static final String TAG = "LiveMyFocusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("粉丝");
    }

    @Override
    protected void request(final boolean isLoadMore) {
        CommonInterface.requestMy_focus(page, to_user_id, new AppRequestCallback<App_focus_follow_ActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_my_focusActModel = actModel;
                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }
}
