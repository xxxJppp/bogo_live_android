package com.bogokj.dynamic.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.bogokj.live.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * <p>文件描述：<p>
 * <p>创建时间：2019/4/20 0020<p>
 * <p>更改时间：2019/4/20 0020<p>
 */
public class DynamicMenuPopup extends BasePopupWindow {
    public DynamicMenuPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dynamic_menu);
    }

    // 返回作用于PopupWindow的show和dismiss动画
    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }
}