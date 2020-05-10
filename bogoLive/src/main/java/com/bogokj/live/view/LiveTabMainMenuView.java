package com.bogokj.live.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.library.utils.SDViewUtil;
import com.fanwe.lib.select.config.SDSelectImageViewConfig;
import com.fanwe.lib.select.view.SDSelectView;
import com.bogokj.live.R;

/**
 * 首页底部菜单tab
 */
public class LiveTabMainMenuView extends SDSelectView {
    public ImageView iv_tab_image;
    public TextView tv_tab_name;

    public LiveTabMainMenuView(Context context) {
        super(context);
        init();
    }

    public LiveTabMainMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabMainMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_tab_main_menu, this, true);
        iv_tab_image = (ImageView) findViewById(R.id.iv_tab_image);
        tv_tab_name = (TextView) findViewById(R.id.tv_tab_name);
    }

    public void setTvTaName(String name) {
        if (!TextUtils.isEmpty(name)) {
            tv_tab_name.setVisibility(VISIBLE);
            tv_tab_name.setText(name);
        } else {
            tv_tab_name.setVisibility(GONE);
            ViewGroup.LayoutParams para1;
            para1 = iv_tab_image.getLayoutParams();
            para1.height = SDViewUtil.dp2px(46);
            para1.width = SDViewUtil.dp2px(46);
            iv_tab_image.setLayoutParams(para1);
        }

    }

    public SDSelectImageViewConfig configImage() {
        return configImage(iv_tab_image);
    }
}
