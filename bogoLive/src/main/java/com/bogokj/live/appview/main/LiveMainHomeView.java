package com.bogokj.live.appview.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.event.ERetryInitSuccess;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.live.view.ScaleTransitionPagerTitleView;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.bogokj.library.utils.SDViewUtil;
import com.fanwe.lib.viewpager.SDViewPager;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveChatC2CActivity;
import com.bogokj.live.activity.LiveRankingActivity;
import com.bogokj.live.activity.LiveSearchUserActivity;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.appview.title.LiveMainHomeTitleView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.dialog.LiveSelectLiveDialog;
import com.bogokj.live.event.EReSelectTabLiveBottom;
import com.bogokj.live.event.ESelectLiveFinish;
import com.bogokj.live.model.HomeTabTitleModel;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 首页主页view
 * @time kn 2019/12/19
 */
public class LiveMainHomeView extends BaseAppView {
    public LiveMainHomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainHomeView(Context context) {
        super(context);
        init();
    }

    private MagicIndicator tabPageIndicator;

    private LiveMainHomeTitleView view_title;

    private SDViewPager vpg_content;

    private List titleList = new ArrayList();

    private List<HomeTabTitleModel> mListModel = new ArrayList<>();


    private SparseArray<LiveTabBaseView> mArrContentView = new SparseArray<>();

    private HomeTabTitleModel mSelectTitleModel;

    protected void init() {
        setContentView(R.layout.view_live_main_home);
        view_title = (LiveMainHomeTitleView) findViewById(R.id.view_title);

        vpg_content = (SDViewPager) findViewById(R.id.vpg_content);
        tabPageIndicator = (MagicIndicator) findViewById(R.id.tab_page_indicator);
        vpg_content.addOnPageChangeListener(mOnPageChangeListener);

        initTitle();
        initTabsData();
        initViewPagerIndicator();
        initViewPager();
    }

    private void initTitle() {
        view_title.setCallback(new LiveMainHomeTitleView.Callback() {
            @Override
            public void onClickSearch(View v) {
                clickSearch();
            }

            @Override
            public void onClickSelectLive(View v) {
                LiveSelectLiveDialog dialog = new LiveSelectLiveDialog(getActivity());
                dialog.show();
            }

            @Override
            public void onClickNewMsg(View v) {
                clickChatList();
            }

            @Override
            public void onClickRanking(View v) {
                clickRanking();
            }
        });
    }

    public void onEventMainThread(ERetryInitSuccess event) {
        initTabsData();
        mPagerAdapter.notifyDataSetChanged();
        dealLastSelected();
    }

    private void dealLastSelected() {
        int index = mListModel.indexOf(mSelectTitleModel);


        if (index < 0) {
            index = 1;
        }

        vpg_content.setCurrentItem(index);
    }

    private void initTabsData() {
        mListModel.clear();

        HomeTabTitleModel tabFollow = new HomeTabTitleModel();
        tabFollow.setTitle("关注");

        HomeTabTitleModel tabHot = new HomeTabTitleModel();
        tabHot.setTitle("推荐");

        HomeTabTitleModel tabNearby = new HomeTabTitleModel();
        tabNearby.setTitle("附近");

        HomeTabTitleModel tabClub = new HomeTabTitleModel();
        tabClub.setTitle(AppRuntimeWorker.getSociatyNmae());

        mListModel.add(tabFollow);
        mListModel.add(tabHot);
        mListModel.add(tabNearby);
        if (AppRuntimeWorker.getOpen_sociaty_module() == 1
                && !TextUtils.isEmpty(AppRuntimeWorker.getSociatyNmae())) {
            mListModel.add(tabClub);
        }

        titleList.clear();
        titleList.add("关注");
        titleList.add("推荐");
        titleList.add("附近");

        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            List<HomeTabTitleModel> listTab = initActModel.getVideo_classified();
            if (listTab != null && !listTab.isEmpty()) {
                mListModel.addAll(listTab);
            }
            for (HomeTabTitleModel tab : listTab) {
                titleList.add(tab.getTitle());
            }
        }


        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView scaleTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
                scaleTransitionPagerTitleView.setNormalColor(Color.WHITE);
                scaleTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                scaleTransitionPagerTitleView.setText(titleList.get(index).toString());
                scaleTransitionPagerTitleView.setTextSize(UIUtil.dip2px(getContext(), 5));
                scaleTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vpg_content.setCurrentItem(index);
                    }
                });
                return scaleTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(0);
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 6.0f;
                } else if (index == 1) {
                    return 1.2f;
                } else {
                    return 1.0f;
                }
            }
        });
        tabPageIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabPageIndicator, vpg_content);
    }

    private void initViewPager() {

        vpg_content.setOffscreenPageLimit(2);
        vpg_content.setAdapter(mPagerAdapter);

        vpg_content.setCurrentItem(1);
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mSelectTitleModel = mListModel.get(position);
            if (position == 1) {
                SDViewUtil.setInvisible(view_title.getViewSelectLive());
            } else {
                SDViewUtil.setInvisible(view_title.getViewSelectLive());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private SDPagerAdapter mPagerAdapter = new SDPagerAdapter<HomeTabTitleModel>(mListModel, getActivity()) {
        @Override
        public View getView(ViewGroup container, int position) {
            LiveTabBaseView view = null;
            switch (position) {
                case 0:
                    view = new LiveTabFollowView(getActivity());
                    break;
                case 1:
                    view = new LiveTabRecommendView(getActivity());
                    break;
                case 2:
                    view = new LiveTabNearbyView(getActivity());
                    break;
                case 3:
                    if (AppRuntimeWorker.getOpen_sociaty_module() == 1 && !TextUtils.isEmpty(AppRuntimeWorker.getSociatyNmae())) {
                        view = new LiveTabClubView(getActivity());
                    } else {
                        LiveTabCategoryView tabView = new LiveTabCategoryView(getActivity());
                        tabView.setTabTitleModel(mListModel.get(position));
                        view = tabView;
                    }
                    break;
                default:
                    LiveTabCategoryView tabView = new LiveTabCategoryView(getActivity());
                    tabView.setTabTitleModel(mListModel.get(position));
                    view = tabView;
                    break;
            }
            if (view != null) {
                mArrContentView.put(position, view);
                view.setParentViewPager(vpg_content);
            }

            return view;
        }
    };

    private void initViewPagerIndicator() {
//        view_pager_indicator.setViewPager(vpg_content);
//        view_pager_indicator.setAdapter(new PagerIndicatorAdapter() {
//            @Override
//            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent) {
//                LiveHomeTitleTab item = new LiveHomeTitleTab(getActivity());
//                HomeTabTitleModel model = SDCollectionUtil.get(mListModel, position);
//                item.setData(model);
//                return item;
//            }
//        });
    }

    public void onEventMainThread(ESelectLiveFinish event) {
//        String text = event.model.getCity();
//
//        IPagerIndicatorItem item = view_pager_indicator.getPagerIndicatorItem(1);
//        if (item != null) {
//            LiveHomeTitleTab tab = (LiveHomeTitleTab) item;
//            HomeTabTitleModel model = tab.getData();
//            model.setTitle(text);
//            tab.setData(model);
//        }
    }

    public void onEventMainThread(EReSelectTabLiveBottom event) {
        if (event.index == 0) {
            int index = vpg_content.getCurrentItem();
            LiveTabBaseView view = mArrContentView.get(index);
            if (view != null) {
                view.scrollToTop();
            }
        }
    }

    /**
     * 排行
     */
    private void clickChatList() {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 搜索
     */
    private void clickSearch() {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        getActivity().startActivity(intent);
    }


    /**
     * 排行榜
     */
    private void clickRanking() {
        Intent intent = new Intent(getActivity(), LiveRankingActivity.class);
        getActivity().startActivity(intent);
    }
}
