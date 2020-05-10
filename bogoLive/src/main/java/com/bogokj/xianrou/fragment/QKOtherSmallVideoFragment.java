package com.bogokj.xianrou.fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.bogokj.hybrid.fragment.BaseFragment;
import com.bogokj.live.R;
import com.bogokj.xianrou.appview.QKOtherSmallVideoView;

/**
 * 包名: com.bogokj.xianrou.fragment
 * 描述:
 * 作者: Su
 * 日期: 2017/8/24 10:37
 **/
public class QKOtherSmallVideoFragment extends BaseFragment
{
    public static final String EXTRA_USER_ID = "QKOtherSmallVideoFragment.EXTRA_USER_ID";

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_other_small_video;
    }

    @Override
    protected void init()
    {
        super.init();

        final String userId = getArguments().getString(EXTRA_USER_ID);
        QKOtherSmallVideoView videoView = new QKOtherSmallVideoView(getContext())
        {
            @Override
            public String getUserId()
            {
                return userId;
            }
        };

        FrameLayout container = (FrameLayout) findViewById(R.id.fl_container);
        container.addView(videoView);
    }

}
