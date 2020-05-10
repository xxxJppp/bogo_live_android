package com.bogokj.live.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.library.view.SDDefaultStringTextView;
import com.bogokj.live.common.AppRuntimeWorker;

/**
 * 默认显示“帐号”的TextView
 */
public class LiveStringAccountTextView extends SDDefaultStringTextView
{
    public LiveStringAccountTextView(Context context)
    {
        super(context);
    }

    public LiveStringAccountTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveStringAccountTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String getDefaultText()
    {
        return AppRuntimeWorker.getAccountName();
    }
}
