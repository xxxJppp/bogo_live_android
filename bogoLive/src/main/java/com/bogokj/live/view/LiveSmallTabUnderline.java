package com.bogokj.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.fanwe.lib.select.config.SDSelectTextViewConfig;
import com.fanwe.lib.select.config.SDSelectViewConfig;
import com.fanwe.lib.select.view.SDSelectView;
import com.bogokj.live.R;

/**
 * 带标题文字和下划线的Tab
 */
public class LiveSmallTabUnderline extends SDSelectView
{
    public LiveSmallTabUnderline(Context context)
    {
        super(context);
        init();
    }

    public LiveSmallTabUnderline(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private TextView tv_title;
    private View view_underline;

    private void init()
    {
        setContentView(R.layout.item_live_tab_underline);
        tv_title = (TextView) findViewById(R.id.tv_title);
        view_underline = findViewById(R.id.iv_underline);

        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        configTextViewTitle()
                .setTextColorResIdNormal(R.color.res_text_gray_s)
                .setTextColorResIdSelected(R.color.res_main_color)
                .setSelected(false);

        configViewUnderline()
                .setVisibilityNormal(View.INVISIBLE)
                .setVisibilitySelected(View.INVISIBLE)
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
