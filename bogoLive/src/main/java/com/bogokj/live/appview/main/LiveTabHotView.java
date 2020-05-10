package com.bogokj.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.InitActModel;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.utils.SDCollectionUtil;
import com.bogokj.library.view.SDRecyclerView;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveMainTabRecommendAdapter;
import com.bogokj.live.appview.LiveTabHotHeaderView;
import com.bogokj.live.appview.pagerindicator.LiveHomeTitleSmallTab;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.event.ESelectLiveFinish;
import com.bogokj.live.model.HomeTabTitleModel;
import com.bogokj.live.model.LiveBannerModel;
import com.bogokj.live.model.LiveFilterModel;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页热门直播列表
 */
public class LiveTabHotView extends LiveTabBaseView {
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;



    public LiveTabHotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabHotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabHotView(Context context) {
        super(context);
        init();
    }


    private SDRecyclerView sdRecyclerView;
    private LiveTabHotHeaderView mHeaderView;
    // private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveMainTabRecommendAdapter mAdapter;
    //
    private SDViewPager vpg_content;
    private PagerIndicator view_pager_indicator;

    private List<HomeTabTitleModel> mListModel = new ArrayList<>();
    private SparseArray<LiveTabBaseView> mArrContentView = new SparseArray<>();

    private HomeTabTitleModel mSelectTitleModel;


    private int mSex;
    private String mCity;

    private void init() {
        setContentView(R.layout.view_live_tab_hot);

        sdRecyclerView = (SDRecyclerView) findViewById(R.id.rv_content);
        vpg_content = (SDViewPager) findViewById(R.id.vpg_content);
        view_pager_indicator = (PagerIndicator) findViewById(R.id.view_pager_indicator);
        vpg_content.addOnPageChangeListener(mOnPageChangeListener);

        mListModel.clear();

        HomeTabTitleModel tabhot = new HomeTabTitleModel();
        tabhot.setTitle("热门");
        mListModel.add(tabhot);
        HomeTabTitleModel tabClub = new HomeTabTitleModel();
        tabClub.setTitle(AppRuntimeWorker.getSociatyNmae());
        if (AppRuntimeWorker.getOpen_sociaty_module() == 1
                && !TextUtils.isEmpty(AppRuntimeWorker.getSociatyNmae()))

        {
            mListModel.add(tabClub);
        }

        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null)
        {
            List<HomeTabTitleModel> listTab = initActModel.getVideo_classified();
            if (listTab != null && !listTab.isEmpty())
            {
                mListModel.addAll(listTab);
            }
        }

        updateParams();
        initViewPagerIndicator();
        initViewPager();
    }

    //初始化viewpager
    private void initViewPager()
    {
        vpg_content.setOffscreenPageLimit(2);
        vpg_content.setAdapter(mPagerAdapter);
        vpg_content.setCurrentItem(0);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
        }

        @Override
        public void onPageSelected(int position)
        {
            mSelectTitleModel = mListModel.get(position);
//            if (position == 1)
//            {
//                SDViewUtil.setVisible(view_title.getViewSelectLive());
//            } else
//            {
//                SDViewUtil.setInvisible(view_title.getViewSelectLive());
//            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {
        }
    };

    private SDPagerAdapter mPagerAdapter = new SDPagerAdapter<HomeTabTitleModel>(mListModel, getActivity())
    {
        @Override
        public View getView(ViewGroup container, int position)
        {
            LiveTabBaseView view = null;
            switch (position)
            {
                case 0:
                    view = new LiveTabRecommendView(getActivity());
                    break;
                case 1:
//                    view = new LiveTabHotView(getActivity());
//                    break;
                    //           case 2:
//                    view = new LiveTabNearbyView(getActivity());
//                    break;
                    //          case 3:
                    //  view = new LiveTabNewView(getActivity());
                    //  break;
                    if (AppRuntimeWorker.getOpen_sociaty_module() == 1 && !TextUtils.isEmpty(AppRuntimeWorker.getSociatyNmae()))
                    {
                        view = new LiveTabClubView(getActivity());
                    } else
                    {
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
            if (view != null)
            {
                mArrContentView.put(position, view);
                view.setParentViewPager(vpg_content);
            }

            return view;
        }
    };

    private void initViewPagerIndicator()
    {
        view_pager_indicator.setViewPager(vpg_content);
        view_pager_indicator.setAdapter(new PagerIndicatorAdapter()
        {
            @Override
            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent)
            {
                LiveHomeTitleSmallTab item = new LiveHomeTitleSmallTab(getActivity());
                HomeTabTitleModel model = SDCollectionUtil.get(mListModel, position);
                item.setData(model);
                return item;
            }
        });
    }

    /**
     * 添加HeaderView
     */
    private void addHeaderView() {
        mHeaderView = new LiveTabHotHeaderView(getActivity());
        mHeaderView.setBannerItemClickCallback(new SDItemClickCallback<LiveBannerModel>() {
            @Override
            public void onItemClick(int position, LiveBannerModel item, View view) {
                Intent intent = item.parseType(getActivity());
                if (intent != null) {
                    getActivity().startActivity(intent);
                }
            }
        });
//        SDViewPager viewPager = getParentViewPager();
//        if (viewPager != null) {
//            viewPager.addPullCondition(new IgnorePullCondition(mHeaderView.getSlidingView()));
//        }
        mHeaderAndFooterWrapper.addHeaderView(mHeaderView);
    }

    /**
     * 更新接口过滤条件
     */
    private void updateParams() {
        LiveFilterModel model = LiveFilterModel.get();

        mSex = model.getSex();
        mCity = model.getCity();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        // startLoopRunnable();
    }

    /**
     * 选择过滤条件完成
     *
     * @param event
     */
    public void onEventMainThread(ESelectLiveFinish event) {
        updateParams();
        startLoopRunnable();
    }

    @Override
    protected void onLoopRun() {
        requestData();
    }

    /**
     * 请求热门首页接口
     */
    private void requestData() {
//        CommonInterface.requestIndex(1, mSex, 0, mCity, new AppRequestCallback<Index_indexActModel>() {
//            @Override
//            protected void onSuccess(SDResponse resp) {
//                if (actModel.isOk()) {
//                    mHeaderView.setData(actModel);
//
//                    synchronized (LiveTabHotView.this) {
//
//                        mListModel = actModel.getList();
//                        mAdapter.setData(mListModel);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            protected void onError(SDResponse resp) {
//                super.onError(resp);
//            }
//
//            @Override
//            protected void onFinish(SDResponse resp) {
//                getPullToRefreshViewWrapper().stopRefreshing();
//                super.onFinish(resp);
//            }
//        });
    }

    @Override
    public void scrollToTop() {
        sdRecyclerView.scrollToStart();
    }

    @Override
    protected void onRoomClosed(final int roomId) {
//        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
//            @Override
//            public LiveRoomModel onBackground() {
//                synchronized (LiveTabHotView.this) {
//                    if (SDCollectionUtil.isEmpty(mListModel)) {
//                        return null;
//                    }
//                    Iterator<LiveRoomModel> it = mListModel.iterator();
//                    while (it.hasNext()) {
//                        LiveRoomModel item = it.next();
//                        if (roomId == item.getRoom_id()) {
//                            return item;
//                        }
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            public void onMainThread(LiveRoomModel result) {
//                if (result != null) {
//                    synchronized (LiveTabHotView.this) {
//                        mAdapter.removeData(result);
//                    }
//                }
//            }
//        });
    }
}
