package com.bogokj.xianrou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.IMHelper;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveChatC2CActivity;
import com.bogokj.live.activity.LiveSearchUserActivity;
import com.bogokj.live.event.EIMLoginSuccess;
import com.bogokj.live.event.ERefreshMsgUnReaded;
import com.bogokj.live.model.TotalConversationUnreadMessageModel;
import com.bogokj.live.view.LiveUnReadNumTextView;
import com.bogokj.xianrou.appview.QKSmallVideoListView;
import com.bogokj.xianrou.appview.main.QKTabSmallVideoView;
import com.bogokj.xianrou.fragment.base.XRBaseFragment;

import org.xutils.view.annotation.ViewInject;

/**
 * 小视频列表
 * Created by LianCP on 2017/7/19.
 */
public class QKTabSmallVideoFragment extends XRBaseFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new QKTabSmallVideoView(container.getContext());
    }
}
