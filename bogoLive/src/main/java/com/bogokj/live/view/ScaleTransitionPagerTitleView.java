package com.bogokj.live.view;

import android.content.Context;
import android.graphics.Typeface;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * 带颜色渐变和缩放的指示器标题以及选中字体样式改变
 */
public class ScaleTransitionPagerTitleView extends ColorTransitionPagerTitleView {
    private float mMinScale = 1.1f;

    public ScaleTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);    // 实现颜色渐变
        setScaleX(mMinScale + (1.4f - mMinScale) * enterPercent);
        setScaleY(mMinScale + (1.4f - mMinScale) * enterPercent);
        setTypeface(Typeface.DEFAULT_BOLD); //选中后的字体样式,根据需求自己做修改
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);    // 实现颜色渐变
        setScaleX(1.4f + (mMinScale - 1.4f) * leavePercent);
        setScaleY(1.4f + (mMinScale - 1.4f) * leavePercent);
        setTypeface(Typeface.DEFAULT);//未选中的字体样式,根据需求自己做修改
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }
}