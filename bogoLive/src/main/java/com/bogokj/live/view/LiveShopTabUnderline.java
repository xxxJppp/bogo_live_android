package com.bogokj.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bogokj.live.R;
import com.fanwe.lib.select.config.SDSelectTextViewConfig;
import com.fanwe.lib.select.config.SDSelectViewConfig;
import com.fanwe.lib.select.view.SDSelectView;

/**
 * 带标题文字和下划线的Tab
 */
public class LiveShopTabUnderline extends SDSelectView
{
    public LiveShopTabUnderline(Context context)
    {
        super(context);
        init();
    }

    public LiveShopTabUnderline(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private TextView tv_title;
    private View view_underline;

    private void init()
    {
        setContentView(R.layout.item_shop_tab_underline);
        tv_title = (TextView) findViewById(R.id.tv_title);
        view_underline = findViewById(R.id.iv_underline);

        configTextViewTitle()
                .setTextColorResIdNormal(R.color.top_tab_text_gray)
                .setTextColorResIdSelected(R.color.white)
                .setSelected(false);

        configViewUnderline()
                .setVisibilityNormal(View.INVISIBLE)
                .setVisibilitySelected(View.VISIBLE)
                .setSelected(false);
    }

    public SDSelectTextViewConfig configTextViewTitle()
    {
        return configText(getTextViewTitle());
    }

    public SDSelectViewConfig configViewUnderline()
    {
        return config(getViewUnderline());
    }

    public TextView getTextViewTitle()
    {
        return tv_title;
    }

    public View getViewUnderline()
    {
        return view_underline;
    }
}
