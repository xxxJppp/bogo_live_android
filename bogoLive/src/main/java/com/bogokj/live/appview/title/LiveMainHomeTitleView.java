package com.bogokj.live.appview.title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.activity.LiveMainActivity;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.dialog.StartLiveAndVideoDialog;

/**
 * 首页-主页标题栏view
 */
public class LiveMainHomeTitleView extends BaseAppView {
    public LiveMainHomeTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainHomeTitleView(Context context) {
        super(context);
        init();
    }

    private View view_search;
    private View view_select_live;
    private View view_new_msg;
    private View view_ranking;
    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void init() {
        setContentView(R.layout.view_live_main_home_title);
        view_search = findViewById(R.id.view_search);
        view_select_live = findViewById(R.id.view_select_live);
        view_new_msg = findViewById(R.id.view_new_msg);
        view_ranking = findViewById(R.id.view_ranking);
        view_search.setOnClickListener(this);
        view_select_live.setOnClickListener(this);
        view_new_msg.setOnClickListener(this);
        view_ranking.setOnClickListener(this);

        findViewById(R.id.iv_live).setOnClickListener(view -> {
            StartLiveAndVideoDialog startLiveAndVideoDialog = new StartLiveAndVideoDialog(getActivity());
            startLiveAndVideoDialog.setCanceledOnTouchOutside(true);
            startLiveAndVideoDialog.showBottom();
        });
    }

    public View getViewSelectLive() {
        return view_select_live;
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == view_search) {
            getCallback().onClickSearch(v);
        } else if (v == view_select_live) {
            getCallback().onClickSelectLive(v);
        } else if (v == view_new_msg) {
            getCallback().onClickNewMsg(v);
        } else if (v == view_ranking) {
            getCallback().onClickRanking(v);
        }
    }

    public Callback getCallback() {
        if (mCallback == null) {
            mCallback = new Callback() {
                @Override
                public void onClickSearch(View v) {

                }

                @Override
                public void onClickSelectLive(View v) {

                }

                @Override
                public void onClickNewMsg(View v) {

                }

                @Override
                public void onClickRanking(View v) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onClickSearch(View v);

        void onClickSelectLive(View v);

        void onClickNewMsg(View v);

        void onClickRanking(View v);
    }
}
