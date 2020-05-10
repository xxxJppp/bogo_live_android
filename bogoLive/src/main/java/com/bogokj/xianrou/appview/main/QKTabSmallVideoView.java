package com.bogokj.xianrou.appview.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.BogoFollowRecommendModel;
import com.bogokj.live.view.ScaleTransitionPagerTitleView;
import com.bogokj.xianrou.appview.BogoVideoClassListView;
import com.bogokj.xianrou.appview.QKBaseVideoListView;
import com.bogokj.xianrou.model.BogoVideoClassLabelModel;
import com.fanwe.lib.viewpager.SDViewPager;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveChatC2CActivity;
import com.bogokj.live.activity.LiveSearchUserActivity;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.appview.main.LiveTabBaseView;
import com.bogokj.live.model.HomeTabTitleModel;
import com.bogokj.xianrou.appview.QKSmallVideoHotListView;
import com.bogokj.xianrou.appview.QKSmallVideoListView;
import com.bogokj.xianrou.appview.QKSmallVideoNearbyListView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 首页视频
 * @time kn 2019/12/24
 */
public class QKTabSmallVideoView extends BaseAppView {

    //viewpage
    private SDViewPager vpg_content;

    private SparseArray<LiveTabBaseView> mArrContentView = new SparseArray<>();
    private List<String> titleList;
    private List<BogoVideoClassLabelModel> videoClass;

    public QKTabSmallVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public QKTabSmallVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QKTabSmallVideoView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.ll_search)
    private RelativeLayout ll_search;// 搜索

    @ViewInject(R.id.ll_chat)
    private RelativeLayout ll_chat;

    private QKSmallVideoListView smallVideoListView;
    private QKSmallVideoHotListView smallVideoHotListView;
    private QKSmallVideoNearbyListView smallVideoNearbyListView;

    private List<HomeTabTitleModel> mListModel = new ArrayList<>();
    private HomeTabTitleModel mSelectTitleModel;

    private MagicIndicator tabPageIndicator;

    private List<BogoVideoClassListView> videoListViewList = new ArrayList<>();

    private void init() {
        setContentView(R.layout.qk_frag_tab_small_video);
        vpg_content = (SDViewPager) findViewById(R.id.vpg_content);
        tabPageIndicator = (MagicIndicator) findViewById(R.id.tab_page_indicator);
        initTabsData();
        initViewPager();
        initViewPagerIndicator();
        vpg_content.addOnPageChangeListener(mOnPageChangeListener);

        setBtnOnClick();
    }


    private void initViewPager() {
        vpg_content.setOffscreenPageLimit(2);
        vpg_content.setAdapter(mPagerAdapter);
        vpg_content.setCurrentItem(0);
    }


    private void setBtnOnClick() {
        ll_search.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
    }

    private void initTabsData() {
        mListModel.clear();
        HomeTabTitleModel tabHot = new HomeTabTitleModel();
        tabHot.setTitle("热门");
        HomeTabTitleModel tabNew = new HomeTabTitleModel();
        tabNew.setTitle("最新");
        HomeTabTitleModel tabNearby = new HomeTabTitleModel();
        tabNearby.setTitle("附近");
        mListModel.add(tabHot);
        mListModel.add(tabNew);
        mListModel.add(tabNearby);

        //视频分类
        videoClass = AppRuntimeWorker.getVideoListTags();
        videoListViewList.clear();
        for (BogoVideoClassLabelModel model : videoClass) {

            HomeTabTitleModel itemModel = new HomeTabTitleModel();
            itemModel.setTitle(model.getName());
            mListModel.add(itemModel);
            videoListViewList.add(new BogoVideoClassListView(getActivity(), model.getId()));
        }
    }


    private void initViewPagerIndicator() {

        titleList = new ArrayList<>();
        titleList.add("热门");
        titleList.add("最新");
        titleList.add("附近");

        for (BogoVideoClassLabelModel model : videoClass) {
            titleList.add(model.getName());
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
//                if (index == 0) {
//                    return 6.0f;
//                } else if (index == 1) {
//                    return 1.2f;
//                } else {
//                    return 1.0f;
//                }
                return 0f;
            }

        });
        tabPageIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabPageIndicator, vpg_content);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    //获取最新小视频
    private QKSmallVideoListView getSmallVideoListView() {
        if (null == smallVideoListView) {
            smallVideoListView = new QKSmallVideoListView(getActivity());
        }
        return smallVideoListView;
    }

    //获取热门小视频
    private QKSmallVideoHotListView getHotSmallVideoListView() {
        if (null == smallVideoHotListView) {
            smallVideoHotListView = new QKSmallVideoHotListView(getActivity());
        }
        return smallVideoHotListView;
    }

    //获取附近小视频
    private QKSmallVideoNearbyListView getNearbySmallVideoListView() {
        if (null == smallVideoNearbyListView) {
            smallVideoNearbyListView = new QKSmallVideoNearbyListView(getActivity());
        }
        return smallVideoNearbyListView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_search:
                clickLLSearch();
                break;
            case R.id.ll_chat:
                clickLlChat();
                break;
            default:
                break;
        }
    }


    private SDPagerAdapter mPagerAdapter = new SDPagerAdapter<HomeTabTitleModel>(mListModel, getActivity()) {
        @Override
        public View getView(ViewGroup container, int position) {
            QKBaseVideoListView view = null;
            if (position > 2) {
                view = videoListViewList.get(position - 3);
                return view;
            }
            switch (position) {
                case 0:
                    view = getHotSmallVideoListView();
                    break;
                case 1:
                    view = getSmallVideoListView();
                    break;
                case 2:
                    view = getNearbySmallVideoListView();
                    break;
                default:
                    break;
            }
            return view;
        }
    };

    // 搜索
    private void clickLLSearch() {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        getActivity().startActivity(intent);
    }

    //聊天
    private void clickLlChat() {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        getActivity().startActivity(intent);
    }

}