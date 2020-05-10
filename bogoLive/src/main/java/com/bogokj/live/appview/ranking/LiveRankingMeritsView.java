package com.bogokj.live.appview.ranking;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.live.view.LiveTabBgImageView;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.bogokj.library.common.SDSelectManager;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.select.SDSelectViewManager;
import com.fanwe.lib.viewpager.SDViewPager;
import com.bogokj.live.R;
import com.bogokj.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 功德排行榜
 *
 * @author luodong
 * @date 2016-10-10
 */
public class LiveRankingMeritsView extends LiveRankingBaseView {

    public LiveRankingMeritsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveRankingMeritsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveRankingMeritsView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.tab_rank_day)
    private LiveTabBgImageView tab_rank_day;
    @ViewInject(R.id.tab_rank_month)
    private LiveTabBgImageView tab_rank_month;
    @ViewInject(R.id.tab_rank_total)
    private LiveTabBgImageView tab_rank_total;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<LiveRankingListBaseView> arrFragment = new SparseArray<>();

    private SDSelectViewManager<LiveTabBgImageView> selectViewManager = new SDSelectViewManager<>();

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_ranking;
    }

    private void init() {
        initSDViewPager();
        initTabs();
    }

    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(2);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");
        listModel.add("");

        vpg_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (selectViewManager.getSelectedIndex() != position) {
                    selectViewManager.setSelected(position, true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        vpg_content.setAdapter(new LivePagerAdapter(listModel, getActivity()));

    }

    private class LivePagerAdapter extends SDPagerAdapter<String> {

        public LivePagerAdapter(List<String> listModel, Activity activity) {
            super(listModel, activity);
        }

        @Override
        public View getView(ViewGroup viewGroup, int position) {
            LiveRankingListMeritsView view = new LiveRankingListMeritsView(getActivity());
            switch (position) {
                case 1:
                    view.setRankName(LiveRankingListMeritsView.RANKING_NAME_DAY);
                    view.requestData(false);
                    break;
                case 2:
                    view.setRankName(LiveRankingListMeritsView.RANKING_NAME_MONTH);
                    view.requestData(false);
                    break;
                case 0:
                    view.setRankName(LiveRankingListMeritsView.RANKING_NAME_ALL);
                    view.requestData(false);
                    break;
                default:
                    break;
            }
            if (null != view) {
                arrFragment.put(position, view);
            }
            return view;
        }
    }

    private void changeLiveTabUnderline(LiveTabBgImageView tabUnderline, String title) {
        tabUnderline.getTextViewTitle().setText(title);
//        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(50)).setWidthSelected(SDViewUtil.dp2px(50));
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_14)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_16));
    }

    private void initTabs() {
        changeLiveTabUnderline(tab_rank_total, "总榜");
        changeLiveTabUnderline(tab_rank_day, "日榜");
        changeLiveTabUnderline(tab_rank_month, "月榜");


        LiveTabBgImageView[] items = new LiveTabBgImageView[]{tab_rank_total, tab_rank_day, tab_rank_month};
        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabBgImageView>() {

            @Override
            public void onNormal(int index, LiveTabBgImageView item) {
            }

            @Override
            public void onSelected(int index, LiveTabBgImageView item) {
                switch (index) {
                    case 1:
                        clickTabDay();
                        break;
                    case 2:
                        clickTabMonth();
                        break;
                    case 0:
                        clickTabTotal();
                        break;

                    default:
                        break;
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(0, true);
    }

    protected void clickTabDay() {
        vpg_content.setCurrentItem(1);
    }

    protected void clickTabMonth() {
        vpg_content.setCurrentItem(2);
    }

    private void clickTabTotal() {
        vpg_content.setCurrentItem(0);
    }

}
