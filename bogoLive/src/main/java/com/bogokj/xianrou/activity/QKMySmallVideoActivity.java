package com.bogokj.xianrou.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.live.R;
import com.bogokj.xianrou.appview.QKMySmallVideoView;
import com.fanwe.lib.statelayout.SDStateLayout;

/**
 * 我的小视频列表
 * Created by LianCP on 2017/7/21.
 */
public class QKMySmallVideoActivity extends BaseTitleActivity {
    private QKMySmallVideoView mSmallVideoView;
    private SDStateLayout view_state_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qk_act_my_small_video);

        initTitle();

        mSmallVideoView = new QKMySmallVideoView(this);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_my_small_video);
        frameLayout.addView(mSmallVideoView);
    }

    private void initTitle() {
        view_state_layout = (SDStateLayout) findViewById(R.id.view_state_layout);
        setStateLayout(view_state_layout);
        mTitle.setMiddleTextTop("我的小视频");
    }


}
