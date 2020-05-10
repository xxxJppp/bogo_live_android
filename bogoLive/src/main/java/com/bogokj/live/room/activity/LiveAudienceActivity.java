package com.bogokj.live.room.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.model.Index_focus_videoActModel;
import com.bogokj.live.model.Index_indexActModel;
import com.bogokj.live.model.Index_new_videoActModel;
import com.bogokj.live.model.JoinLiveData;
import com.bogokj.live.model.LiveRoomModel;
import com.bogokj.live.room.LiveAdapter;
import com.bogokj.live.small.LiveUtils;
import com.bogokj.live.utils.FloatPermissionManager;
import com.bogokj.live.view.VerticalViewPager;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.util.Event;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author kn update
 * @description: 用户观看界面Activity
 * @time 2020/2/16
 */
public class LiveAudienceActivity extends BaseLiveActivity {

    public static final String LIVE_LIST = "LIVE_LIST";
    public static final String LIVE_SELECT_POS = "LIVE_SELECT_POS";
    public static final String LIVE_LIST_PAGE = "LIVE_LIST_PAGE";
    public static final String LIVE_TYPE = "LIVE_TYPE";
    public static final String LIVE_SEX = "LIVE_SEX";
    public static final String LIVE_CITY = "LIVE_CITY";

    @ViewInject(R.id.vertical_view_page)
    VerticalViewPager mVerticalViewPage;

    private List<LiveRoomModel> mLiveList = new ArrayList<>();
    private LiveAdapter liveAdapter;

    private int videoPage = 1;
    private int videoType = 0;

    public static int room_id = 0;
    private int searchSex;
    private String searchCity;
    private int selectPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_video_player_touch);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 不锁屏

        initView();
    }


    public void initView() {

        mLiveList.addAll(getIntent().<LiveRoomModel>getParcelableArrayListExtra(LIVE_LIST));
        selectPos = getIntent().getIntExtra(LIVE_SELECT_POS, 0);
        videoPage = getIntent().getIntExtra(LIVE_LIST_PAGE, 1);
        videoType = getIntent().getIntExtra(LIVE_TYPE, QKTabSmallVideoAdapter.TYPE_LIST);
        searchSex = getIntent().getIntExtra(LIVE_SEX, 0);
        searchCity = getIntent().getStringExtra(LIVE_CITY);

        //默认第一个选中直播
        if (mLiveList.size() > 0) {
            room_id = mLiveList.get(selectPos).getRoom_id();
        }

        liveAdapter = new LiveAdapter(getSupportFragmentManager(), mLiveList);

        mVerticalViewPage.setAdapter(liveAdapter);
        mVerticalViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPos = position;
                room_id = mLiveList.get(position).getRoom_id();
                Event.OnTouchLivePlayerPageChange event = new Event.OnTouchLivePlayerPageChange();
                EventBus.getDefault().post(event);

                if (position == mLiveList.size() - 1) {
                    requestData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mVerticalViewPage.setOffscreenPageLimit(0);
        mVerticalViewPage.setCurrentItem(selectPos);
        liveAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AppDialogConfirm dialog = new AppDialogConfirm(getActivity());
        dialog.setTextContent("是否小窗口继续播放？");
        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                Event.OnTouchLiveFinish event = new Event.OnTouchLiveFinish();
                EventBus.getDefault().post(event);

            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                boolean requestFloatPermission = FloatPermissionManager.isRequestFloatPermission(LiveAudienceActivity.this);
                toOpenWindow(requestFloatPermission, false);


            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 17) {
            boolean requestFloatPermission = FloatPermissionManager.isRequestFloatPermission(LiveAudienceActivity.this);
            toOpenWindow(requestFloatPermission, true);
        }
    }


    private void toOpenWindow(boolean requestFloatPermission, boolean isFinish) {
        if (requestFloatPermission) {
            //保留下数据 直播间退出去就会销毁掉
            int roomId = LiveInformation.getInstance().getRoomId();
            String groupId = LiveInformation.getInstance().getGroupId();
            String createrId = LiveInformation.getInstance().getCreaterId();
            String privateKey = LiveInformation.getInstance().getPrivateKey();
            String loadingVideoImageUrl = LiveInformation.getInstance().getLoadingVideoImageUrl();

            JoinLiveData joinLiveData = new JoinLiveData(roomId, groupId, createrId, loadingVideoImageUrl, privateKey);

            //这些是上下滑动 分页所需要数据
            joinLiveData.setType(videoType);
            joinLiveData.setPage(videoPage);
            joinLiveData.setmData(mLiveList);
            joinLiveData.setPosition(selectPos);
            joinLiveData.setSex(searchSex);
            joinLiveData.setCity(searchCity);

            LiveUtils.init(App.getApplication(), LiveAudienceActivity.this, joinLiveData);
            finish();
        } else {
            if (!isFinish) {
                FloatPermissionManager.requestFloatPermission(LiveAudienceActivity.this);
            } else {
                finish();
            }

        }
    }


    @Override
    protected void onDestroy() {
        //销毁单例
        super.onDestroy();
    }


    public void requestData() {

        videoPage++;

        switch (videoType) {
            case 0:
                requestFllowData();
                break;
            case 1:
                requestHotData();
                break;
            case 2:
                requestNearData();
                break;

            default:
                requestOtherData();
                break;
        }


    }


    /**
     * 请求更多接口数据
     */
    private void requestFllowData() {
        CommonInterface.requestFocusVideo(new AppRequestCallback<Index_focus_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    //关注页无加载更多
//                    mLiveList.addAll(actModel.getData());
//                    liveAdapter.notifyDataSetChanged();


                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    /**
     * 请求推荐接口数据
     */
    private void requestHotData() {
        CommonInterface.requestIndex(videoPage, searchSex, 0, searchCity, new AppRequestCallback<Index_indexActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mLiveList.addAll(actModel.getList());
                    liveAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }

    /**
     * 请求附近接口数据
     */
    private void requestNearData() {

        CommonInterface.requestNewVideo(1, new AppRequestCallback<Index_new_videoActModel>() {

            @Override
            protected void onSuccess(final SDResponse resp) {
                if (actModel.isOk()) {
                    //附近页无加载更多
//                    mLiveList.addAll(actModel.getList());
//                    liveAdapter.notifyDataSetChanged();

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });

    }

    /**
     * 请求其它接口数据
     */
    private void requestOtherData() {
        CommonInterface.requestCategoryVideo(videoType, new AppRequestCallback<Index_new_videoActModel>() {

            @Override
            protected void onSuccess(final SDResponse resp) {
                if (actModel.isOk()) {
                    //其他页无加载更多
//                    mLiveList.addAll(actModel.getList());
//                    liveAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                //getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }
}
