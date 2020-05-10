package com.bogokj.live.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveDistributionAdatper;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_distribution_indexActModel;
import com.bogokj.live.model.DistributionItemModel;
import com.bogokj.live.model.PageModel;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhz on 2017/1/3.我的分销
 */

public class LiveDistributionActivity extends BaseTitleActivity
{
    @ViewInject(R.id.list)
    protected ListView list;
    private SDStateLayout view_state_layout;

    private List<DistributionItemModel> listModel = new ArrayList<DistributionItemModel>();
    private LiveDistributionAdatper liveDistributionAdatper;
    private PageModel pageModel;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_distribution);
        x.view().inject(this);
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        init();
    }

    private void init()
    {
        initView();
        initTitle();
        register();
    }

    private void initView()
    {
        view_state_layout = (SDStateLayout) findViewById(R.id.view_state_layout);
        setStateLayout(view_state_layout);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("分享收益");
    }

    protected void register()
    {
        liveDistributionAdatper = new LiveDistributionAdatper(listModel, this);
        getStateLayout().setAdapter(liveDistributionAdatper);
        list.setAdapter(liveDistributionAdatper);

        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                refreshViewer();
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }
        });

        refreshViewer();
    }

    private void loadMoreViewer()
    {
        if (pageModel != null)
        {
            if (pageModel.getHas_next() == 1)
            {
                page++;
                requestDistribution(true);
            } else
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                SDToast.showToast("没有更多数据");
            }
        } else
        {
            refreshViewer();
        }
    }

    private void refreshViewer()
    {
        page = 1;
        requestDistribution(false);
    }

    protected void requestDistribution(final boolean isLoadMore)
    {
        CommonInterface.requestDistribution(page, new AppRequestCallback<App_distribution_indexActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    pageModel = actModel.getPage();
                    SDViewUtil.updateAdapterByList(listModel, actModel.getData(), liveDistributionAdatper, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }
}
