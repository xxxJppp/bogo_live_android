package com.bogokj.dynamic.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.dynamic.activity.DynamicDetailActivity;
import com.bogokj.dynamic.activity.PushDynamicActivity;
import com.bogokj.dynamic.adapter.LiveDynamicListAdapter;
import com.bogokj.dynamic.event.RefreshMessageEvent;
import com.bogokj.dynamic.modle.DynamicLikeModel;
import com.bogokj.dynamic.modle.DynamicModel;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.dynamic.activity.BogoPeopleNearByActivity;
import com.bogokj.xianrou.util.StringUtils;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.sunday.eventbus.SDEventManager;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @author kn
 * @description: 动态页面基类
 * @time kn 2019/12/17
 */
public abstract class LiveDynamicListBaseView extends BaseAppView {
    private static final int REFRESH = 333;

    public LiveDynamicListBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveDynamicListBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveDynamicListBaseView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.lv_content)
    protected RecyclerView lv_content;

    @ViewInject(R.id.data_empty_layout)
    protected PercentLinearLayout emptyLayout;

    @ViewInject(R.id.view_state_layout)
    protected SDStateLayout sdStateLayout;


    protected List<DynamicModel> listModel = new ArrayList<DynamicModel>();
    protected LiveDynamicListAdapter adapter;

    protected int page;
    protected int has_next;
    public int firstVisibleItem, lastVisibleItem, visibleCount;
    boolean scrollState = false;

    protected void init() {
        setContentView(R.layout.frag_live_dyanmic_list);
        setAdapter();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                page = 1;
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter() {
                loadMoreViewer();
            }
        });

        lv_content.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE: //滚动停止
                        scrollState = false;
                        autoPlayVideo(recyclerView);
                        break;
                    case SCROLL_STATE_DRAGGING: //手指拖动
                        scrollState = true;
                        break;
                    case SCROLL_STATE_SETTLING: //惯性滚动
                        scrollState = true;
                        break;
                }

            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    firstVisibleItem = linearManager.findFirstVisibleItemPosition();
                    lastVisibleItem = linearManager.findLastVisibleItemPosition();
                    visibleCount = lastVisibleItem - firstVisibleItem;
                }

                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(LiveDynamicListAdapter.TAG) && (position < firstVisibleItem || position > lastVisibleItem)) {
                        GSYVideoManager.onPause();
                    }
                }
            }

        });


        findViewById(R.id.send_dynamic_button).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PushDynamicActivity.class);
            getActivity().startActivity(intent);
        });
    }

    private void autoPlayVideo(RecyclerView view) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();

        for (int i = 0; i < visibleCount; i++) {
            if (layoutManager != null && layoutManager.getChildAt(i) != null && layoutManager.getChildAt(i).findViewById(R.id.videoplayer) != null) {
                SampleCoverVideo homeGSYVideoPlayer = (SampleCoverVideo) layoutManager.getChildAt(i).findViewById(R.id.videoplayer);
                Rect rect = new Rect();
                homeGSYVideoPlayer.getLocalVisibleRect(rect);
                int videoheight = homeGSYVideoPlayer.getHeight();
                if (rect.top == 0 && rect.bottom == videoheight) {
                    if (homeGSYVideoPlayer.getCurrentState() == homeGSYVideoPlayer.CURRENT_STATE_NORMAL || homeGSYVideoPlayer.getCurrentState() == homeGSYVideoPlayer.CURRENT_STATE_ERROR) {
                        homeGSYVideoPlayer.getStartButton().performClick();
                    }
                    return;
                }

            }
        }
        GSYVideoManager.releaseAllVideos();
    }

    public void onEventMainThread(RefreshMessageEvent event) {
        if ("refresh_dynamic_list".equals(event.getMessage())) {
            page = 1;
            requestData(false);
        }
    }

    protected void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_content.setLayoutManager(layoutManager);

        adapter = new LiveDynamicListAdapter(listModel, getActivity());

        if (getDynamicType() == 2) {
            View headView = inflate(getContext(), R.layout.view_dynamic_nearby_head, null);
            LinearLayout peopleNearByLl = headView.findViewById(R.id.people_nearby_ll);
            //附近的人页面
            peopleNearByLl.setOnClickListener(v -> getActivity().startActivity(new Intent(getActivity(), BogoPeopleNearByActivity.class)));
            adapter.addHeaderView(headView);
        }

        adapter.setOnDynamicClickListener(new LiveDynamicListAdapter.OnDynamicClickListener() {
            @Override
            public void clickHeadImg(int position, DynamicModel model) {
                jumpUserHomeActivity(model);
            }

            @Override
            public void clickItem(String dynamic_id) {
                jumpDynimicDetailActivity(dynamic_id);
            }

            @Override
            public void deleteDynamic(int position, DynamicModel model) {
                showDelDialog(model.getId(), position);
            }

            @Override
            public void shareDynamic(String dynamic_id) {
                requestShareDynamic(dynamic_id);
            }

            @Override
            public void clickLikeDynamic(DynamicModel model, int posi) {
                //点击喜欢
                requestLikeDynamic(model, posi);
            }
        });
        lv_content.setAdapter(adapter);
    }


    /**
     * 点击喜欢
     *
     * @param model
     */
    private void requestLikeDynamic(final DynamicModel model, int posi) {
        int is_praise;
        if (model.getIs_like() == 1) {
            is_praise = 2;
        } else {
            is_praise = 1;
        }
        CommonInterface.requestLikeDynamic(model.getId(), is_praise, new AppRequestCallback<DynamicLikeModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {

                if (actModel.isOk()) {

                    if (StringUtils.toInt(actModel.getIs_like()) == 1) {
                        model.setIs_like(1);
                    } else {
                        model.setIs_like(0);
                    }

                    int like_count = actModel.getCount();
                    model.setPraise(like_count);

                    listModel.set(posi, model);
                    adapter.notifyItemChanged(posi);
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);

            }
        });
    }


    private void jumpDynimicDetailActivity(String dynamic_id) {
        Intent intent = new Intent(getContext(), DynamicDetailActivity.class);
        intent.putExtra(DynamicDetailActivity.DYNAMIC_ID, dynamic_id);
        getActivity().startActivityForResult(intent, REFRESH);
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        super.onActivityResult(activity, requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case REFRESH:
                    //返回刷新
                    requestData(false);
                    break;
            }
    }

    private void requestShareDynamic(String id) {
        showProgressDialog("");
        CommonInterface.requestShareDynamic(id, new AppRequestCallback<BaseActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                dismissProgressDialog();
                if (actModel.isOk()) {
                    RefreshMessageEvent event = new RefreshMessageEvent("refresh_dynamic_list");
                    SDEventManager.post(event);
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });
    }

    /**
     * 跳转到用户详情界面
     *
     * @param model
     */
    private void jumpUserHomeActivity(DynamicModel model) {
        Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, String.valueOf(model.getUid()));
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
        getActivity().startActivity(intent);
    }

    private void loadMoreViewer() {
        if (has_next == 1) {
            page++;
            requestData(true);
        } else {
            SDToast.showToast("没有更多数据了");
            onRefreshComplete();
        }
    }

    protected void onRefreshComplete() {
        if (lv_content != null) {
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    /**
     * 填充数据
     *
     * @param has_next   是否有下一页数据
     * @param listModels 要是填充数据
     * @param isLoadMore 是否加载更多数据
     */
    protected void fillData(int has_next, List<DynamicModel> listModels, boolean isLoadMore) {
        emptyLayout.setVisibility(GONE);
        sdStateLayout.setVisibility(VISIBLE);
        this.has_next = has_next;
        synchronized (LiveDynamicListBaseView.this) {
            if (!isLoadMore) {
                listModel.clear();
                GSYVideoManager.releaseAllVideos();
            }

            SDViewUtil.updateList(listModel, listModels, isLoadMore);
            if (adapter != null && listModel != null) {
                adapter.setNewData(listModel);
            }
            if (listModel.size() < 1) {
                emptyLayout.setVisibility(VISIBLE);
                sdStateLayout.setVisibility(GONE);
            } else {
                getStateLayout().showContent();
            }
        }
    }


    private void showDelDialog(final String id, final int pos) {
        AppDialogConfirm dialog = new AppDialogConfirm(getSDBaseActivity());
        dialog.setTextContent("确定删除该动态？");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setTextCancel("取消");
        dialog.setTextConfirm("继续");
        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                requestDelDynamic(id, pos);
            }
        }).show();
    }

    private void requestDelDynamic(String id, final int position) {
        showProgressDialog("");
        CommonInterface.requestDelDynamic(id, new AppRequestCallback<BaseActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                dismissProgressDialog();
                if (actModel.isOk()) {
                    if (!listModel.isEmpty() && listModel.size() > position) {
                        listModel.remove(position);
                        if (listModel.size() == 0) {
                            emptyLayout.setVisibility(VISIBLE);
                            sdStateLayout.setVisibility(GONE);
                        } else {
                            getStateLayout().showContent();
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });
    }

    public abstract void requestData(boolean isLoadMore);

    protected abstract int getDynamicType();
}
