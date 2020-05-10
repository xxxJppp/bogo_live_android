package com.bogokj.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bogokj.dynamic.activity.BogoSystemMsgActivity;
import com.bogokj.dynamic.activity.BogoTopPicActivity;
import com.bogokj.dynamic.activity.BogoTopPicDetailActivity;
import com.bogokj.dynamic.activity.BogoTopPicSearchActivity;
import com.bogokj.dynamic.activity.PushDynamicActivity;
import com.bogokj.dynamic.adapter.BogoDynamicMainTopicAdapter;
import com.bogokj.dynamic.event.RefreshLiveVideoEvent;
import com.bogokj.dynamic.modle.BogoDynamicTopicListApi;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.view.LiveAttentionDynamicListView;
import com.bogokj.dynamic.view.LiveDynamicListBaseView;
import com.bogokj.dynamic.view.LiveMyDynamicListView;
import com.bogokj.dynamic.view.LiveNearByDynamicListView;
import com.bogokj.dynamic.view.LiveNewDynamicListView;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.common.CommonInterface;
import com.fanwe.lib.viewpager.SDViewPager;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.bogokj.library.common.SDSelectManager;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.view.select.SDSelectViewManager;
import com.bogokj.live.R;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.view.LiveTabUnderline;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 首页动态
 * @time kn 2019/12/17
 */
public class LiveMainDynamicView extends BaseAppView {
    public LiveMainDynamicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMainDynamicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainDynamicView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.ll_upload_dynamic)
    private RelativeLayout ll_upload_dynamic;

    @ViewInject(R.id.tab_dynamic_new)
    private LiveTabUnderline tab_dynamic_new;

    @ViewInject(R.id.tab_dynamic_attention)
    private LiveTabUnderline tab_dynamic_attention;

    @ViewInject(R.id.tab_dynamic_my)
    private LiveTabUnderline tab_dynamic_my;

    @ViewInject(R.id.tab_dynamic_near)
    private LiveTabUnderline tab_dynamic_near;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    @ViewInject(R.id.rv_topic_content_list)
    private RecyclerView rv_topic_content_list;

    private SparseArray<View> arrFragment = new SparseArray<>();

    private SDSelectViewManager<LiveTabUnderline> selectViewManager = new SDSelectViewManager<>();

    //话题列表
    private List<BogoDynamicTopicListModel> topicList = new ArrayList<>();
    private BogoDynamicMainTopicAdapter bogoDynamicMainTopicAdapter;

    private void init() {
        setContentView(R.layout.view_live_main_dynamic);

        //话题控件
        LinearLayoutManager linearLayoutManagerTopic = new LinearLayoutManager(getContext());
        linearLayoutManagerTopic.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_topic_content_list.setLayoutManager(linearLayoutManagerTopic);
        bogoDynamicMainTopicAdapter = new BogoDynamicMainTopicAdapter(topicList);
        rv_topic_content_list.setAdapter(bogoDynamicMainTopicAdapter);

        requestGetTopicData();

        setBtnOnClick();
        initSDViewPager();
        initTabs();
    }

    private void setBtnOnClick() {
        findViewById(R.id.dynamic_all_top_pic_tv).setOnClickListener(this);
        findViewById(R.id.system_msg_iv).setOnClickListener(this);
        findViewById(R.id.search_top_pic_tv).setOnClickListener(this);
        findViewById(R.id.dynamic_live_cv).setOnClickListener(this);
        findViewById(R.id.dynamic_short_video_cv).setOnClickListener(this);
        ll_upload_dynamic.setOnClickListener(this);


        bogoDynamicMainTopicAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), BogoTopPicDetailActivity.class);
            intent.putExtra("data", topicList.get(position));
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_upload_dynamic:
                clickLlUploadDynamic();
                break;

            case R.id.dynamic_all_top_pic_tv:
                getActivity().startActivity(new Intent(getActivity(), BogoTopPicActivity.class));
                break;

            case R.id.system_msg_iv:
                getActivity().startActivity(new Intent(getActivity(), BogoSystemMsgActivity.class));
                break;

            case R.id.search_top_pic_tv:
                getActivity().startActivity(new Intent(getActivity(), BogoTopPicSearchActivity.class));
                break;

            case R.id.dynamic_live_cv:
//                EventBus.getDefault().post(new RefreshLiveVideoEvent("live"));
                clickListener.onLiveClickListener();
                break;

            case R.id.dynamic_short_video_cv:
//                EventBus.getDefault().post(new RefreshLiveVideoEvent("video"));
                clickListener.onVideoClickListener();
                break;

            default:
                break;
        }
    }

    private void clickLlUploadDynamic() {
        Intent intent = new Intent(getActivity(), PushDynamicActivity.class);
        getActivity().startActivity(intent);
    }


    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(2);
        //vpg_content.setLockPull(true);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
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
            LiveDynamicListBaseView view = null;
            switch (position) {
                case 0:
                    view = new LiveAttentionDynamicListView(getActivity());
                    view.requestData(false);
                    break;
                case 1:
                    view = new LiveNewDynamicListView(getActivity());
                    view.requestData(false);
                    break;
                case 2:
                    view = new LiveNearByDynamicListView(getActivity());
                    view.requestData(false);
                    break;
                case 3:
                    view = new LiveMyDynamicListView(getActivity());
                    view.requestData(false);
                    break;

                default:
                    break;
            }
            if (view != null) {
                arrFragment.put(position, view);
            }
            return view;
        }

    }

    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title) {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_16)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_18));
        //tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(75)).setWidthSelected(SDViewUtil.dp2px(75));
    }

    private void initTabs() {
        changeLiveTabUnderline(tab_dynamic_attention, "关注");
        changeLiveTabUnderline(tab_dynamic_new, "推荐");
        changeLiveTabUnderline(tab_dynamic_near, "附近");
        changeLiveTabUnderline(tab_dynamic_my, "我的");
//        changeLiveTabUnderline(tab_dynamic_recomme, SDResourcesUtil.getString(R.string.live_ranking_tab_merits_text));
//        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_dynamic_recomme, tab_dynamic_attention};
        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_dynamic_attention, tab_dynamic_new, tab_dynamic_near, tab_dynamic_my};


        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>() {

            @Override
            public void onNormal(int index, LiveTabUnderline item) {
            }

            @Override
            public void onSelected(int index, LiveTabUnderline item) {
                GSYVideoManager.releaseAllVideos();
                switch (index) {
                    case 0:
                        clickTabAttention();
                        break;
                    case 1:
                        clickTabNew();
                        break;
                    case 2:
                        clickTabNear();
                        break;
                    case 3:
                        clickTabMy();
                        break;

                    default:
                        break;
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(1, true);

    }

    private void requestGetTopicData() {
        CommonInterface.requestGetTopicList(1, 1, "", new AppRequestCallback<BogoDynamicTopicListApi>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    topicList.clear();
                    topicList.addAll(actModel.getData());
                    bogoDynamicMainTopicAdapter.notifyDataSetChanged();
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    protected void clickTabAttention() {
        vpg_content.setCurrentItem(0);
    }

    protected void clickTabNew() {
        vpg_content.setCurrentItem(1);
    }

    protected void clickTabNear() {
        vpg_content.setCurrentItem(2);
    }

    protected void clickTabMy() {
        vpg_content.setCurrentItem(3);
    }


    ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onLiveClickListener();

        void onVideoClickListener();
    }
}
