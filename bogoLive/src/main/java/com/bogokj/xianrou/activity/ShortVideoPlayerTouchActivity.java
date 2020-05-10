package com.bogokj.xianrou.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.room.activity.BaseLiveActivity;
import com.bogokj.live.view.VerticalViewPager;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.live.R;
import com.bogokj.xianrou.adapter.QKTabSmallVideoAdapter;
import com.bogokj.xianrou.adapter.ShortVideoAdapter;
import com.bogokj.xianrou.common.QKCommonInterface;
import com.bogokj.xianrou.model.QKSmallVideoListModel;
import com.bogokj.xianrou.model.QKTabSmallVideoModel;
import com.bogokj.xianrou.util.Event;
import com.bogokj.xianrou.util.StringUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ShortVideoPlayerTouchActivity extends BaseLiveActivity {

    public static final String VIDEO_LIST = "VIDEO_LIST";
    public static final String VIDEO_POS = "VIDEO_POS";
    public static final String VIDEO_LIST_PAGE = "VIDEO_LIST_PAGE";
    public static final String VIDEO_TYPE = "VIDEO_TYPE";
    public static final String VIDEO_OTHER_USER_ID = "VIDEO_OTHER_USER_ID";

    @ViewInject(R.id.vertical_view_page)
    VerticalViewPager mVerticalViewPage;

    private List<QKSmallVideoListModel> mShortVideoList = new ArrayList<>();
    private ShortVideoAdapter dummyAdapter;

    public static int select_video_id = 0;
    private int videoPage = 1;
    private int videoType = 0;
    private String otherUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_short_video_player_touch);
        initView();
        initData();
    }


    public void initView() {

        dummyAdapter = new ShortVideoAdapter(getSupportFragmentManager(), mShortVideoList);
        mVerticalViewPage.setAdapter(dummyAdapter);
        mVerticalViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select_video_id = StringUtils.toInt(mShortVideoList.get(position).getWeibo_id());
                Event.OnTouchShortVideoPlayerPageChange event = new Event.OnTouchShortVideoPlayerPageChange();
                EventBus.getDefault().post(event);

                if (position == mShortVideoList.size() - 1) {
                    requestData(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //mVerticalViewPage.setPageMargin(getResources().getDimensionPixelSize(R.dimen.h5));
        //mVerticalViewPage.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
    }

    public void initData() {

        mShortVideoList.addAll(getIntent().<QKSmallVideoListModel>getParcelableArrayListExtra(VIDEO_LIST));
        int selectPos = getIntent().getIntExtra(VIDEO_POS, 0);
        videoPage = getIntent().getIntExtra(VIDEO_LIST_PAGE, 1);
        videoType = getIntent().getIntExtra(VIDEO_TYPE, QKTabSmallVideoAdapter.TYPE_LIST);
        otherUserId = getIntent().getStringExtra(VIDEO_OTHER_USER_ID);
        //默认第一个选中播放
        if (mShortVideoList.size() > 0) {
            select_video_id = StringUtils.toInt(mShortVideoList.get(selectPos).getWeibo_id());
        }
        mVerticalViewPage.setCurrentItem(selectPos);
        mVerticalViewPage.setOffscreenPageLimit(1);
        dummyAdapter.notifyDataSetChanged();


    }

    public void requestData(boolean isCache) {

        videoPage++;

        switch (videoType) {
            case QKTabSmallVideoAdapter.TYPE_LIST:
                requestVideoList();
                break;
            case QKTabSmallVideoAdapter.TYPE_MY:
                requestMyVideoList();
                break;
            case QKTabSmallVideoAdapter.TYPE_OTHER:
                requestOtherVideoList();
                break;
        }


    }

    private void requestOtherVideoList() {
        QKCommonInterface.requestOtherSmallVideoList(videoPage, otherUserId, new AppRequestCallback<QKTabSmallVideoModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mShortVideoList.addAll(actModel.getData());
                    dummyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void requestMyVideoList() {
        QKCommonInterface.requestMySmallVideoList(videoPage, new AppRequestCallback<QKTabSmallVideoModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mShortVideoList.addAll(actModel.getData());
                    dummyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void requestVideoList() {
        QKCommonInterface.requestSmallVideoList(videoPage, "1", new AppRequestCallback<QKTabSmallVideoModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mShortVideoList.addAll(actModel.getData());
                    dummyAdapter.notifyDataSetChanged();

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //销毁页面 发消息停止视频
        finish();
    }

    @Override
    protected void onDestroy() {
        Event.OnTouctVideoFinish event = new Event.OnTouctVideoFinish();
        EventBus.getDefault().post(event);
        super.onDestroy();
    }
}
