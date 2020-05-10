package com.bogokj.live.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveWebH5View;
import com.bogokj.live.appview.pagerindicator.LiveShopTitleTab;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.HomeTabTitleModel;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.bogokj.library.utils.SDCollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class LiveToolsShopActivity extends BaseTitleActivity {

    private SDViewPager vpg_content;
    private PagerIndicator view_pager_indicator;
    private List<HomeTabTitleModel> mListModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tools_shop);
        init();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("道具商城");
    }


    private void init() {

        initTitle();

        vpg_content = (SDViewPager) findViewById(R.id.vpg_content);
        view_pager_indicator = (PagerIndicator) findViewById(R.id.view_pager_indicator);


        HomeTabTitleModel tab_car = new HomeTabTitleModel();
        tab_car.setTitle("座驾商城");

        HomeTabTitleModel tab_vip = new HomeTabTitleModel();
        tab_vip.setTitle("VIP会员");

        HomeTabTitleModel tab_luck_num = new HomeTabTitleModel();
        tab_luck_num.setTitle("靓号");

        mListModel.add(tab_vip);
        mListModel.add(tab_car);
        mListModel.add(tab_luck_num);

        initViewPagerIndicator();

        vpg_content.setOffscreenPageLimit(1);
        vpg_content.setAdapter(mPagerAdapter);

        vpg_content.setCurrentItem(0);

    }


    private SDPagerAdapter mPagerAdapter = new SDPagerAdapter<HomeTabTitleModel>(mListModel, getActivity()) {
        @Override
        public View getView(ViewGroup container, int position) {
            LiveWebH5View view = null;
            switch (position) {
                case 0:
                    view = new LiveWebH5View(getActivity());
                    view.loadUrl(AppRuntimeWorker.getVip_url());
                    break;
                case 1:
                    view = new LiveWebH5View(getActivity());
                    view.loadUrl(AppRuntimeWorker.getNoble_car_url());
                    break;
                case 2:
                    view = new LiveWebH5View(getActivity());
                    view.loadUrl(AppRuntimeWorker.getGoodNumberUrl());
                    break;
                default:
                    break;
            }
            if (view != null) {
                view.setParentViewPager(vpg_content);
            }

            return view;
        }
    };

    private void initViewPagerIndicator() {
        view_pager_indicator.setViewPager(vpg_content);
        view_pager_indicator.setAdapter(new PagerIndicatorAdapter() {
            @Override
            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent) {
                LiveShopTitleTab item = new LiveShopTitleTab(getActivity());
                HomeTabTitleModel model = SDCollectionUtil.get(mListModel, position);
                item.setData(model);
                return item;
            }
        });
    }
}
