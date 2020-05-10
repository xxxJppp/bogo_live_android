package com.bogokj.hybrid.activity;

import android.view.View;

import com.bogokj.library.title.SDTitleItem;
import com.bogokj.library.title.SDTitleSimple;
import com.bogokj.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;

public class BaseTitleActivity extends BaseActivity implements SDTitleSimpleListener {
    protected SDTitleSimple mTitle;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

        findViewById(R.id.title_ll).setVisibility(View.VISIBLE);
        mTitle = (SDTitleSimple) findViewById(R.id.title);
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setmListener(this);
    }

    @Override
    protected int onCreateTitleViewResId() {
        return R.layout.include_title_simple;
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        finish();
    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {

    }

    protected void isShowTitle(boolean isShowTitle) {
        if (isShowTitle) {
            SDViewUtil.setVisible(mTitle);
        } else {
            SDViewUtil.setGone(mTitle);
        }
    }
}
