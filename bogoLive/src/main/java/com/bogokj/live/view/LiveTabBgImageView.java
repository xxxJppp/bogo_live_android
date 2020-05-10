package com.bogokj.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bogokj.live.R;
import com.fanwe.lib.select.config.SDSelectTextViewConfig;
import com.fanwe.lib.select.view.SDSelectView;

/**
 * 带标题文字和背景图的Tab
 */
public class LiveTabBgImageView extends SDSelectView
{
    public LiveTabBgImageView(Context context)
    {
        super(context);
        init();
    }

    public LiveTabBgImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private TextView tv_title;

    private void init()
    {
        setContentView(R.layout.item_live_tab_image_bg);
        tv_title = (TextView) findViewById(R.id.tv_title);

        configTextViewTitle()
                .setTextColorResIdNormal(R.color.text_user_home)
                .setTextColorResIdSelected(R.color.white)
                .setBackgroundResIdSelected(R.drawable.bg_btn_gradient_purple)
                .setSelected(false);
    }

    public SDSelectTextViewConfig configTextViewTitle()
    {
        return configText(getTextViewTitle());
    }

    public TextView getTextViewTitle()
    {
        return tv_title;
    }

}
