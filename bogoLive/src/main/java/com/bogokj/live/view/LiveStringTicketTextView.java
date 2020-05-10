package com.bogokj.live.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.library.view.SDDefaultStringTextView;
import com.bogokj.live.common.AppRuntimeWorker;

/**
 * 默认显示“钱票”的TextView
 */
public class LiveStringTicketTextView extends SDDefaultStringTextView
{
    public LiveStringTicketTextView(Context context)
    {
        super(context);
    }

    public LiveStringTicketTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveStringTicketTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String getDefaultText()
    {
        return AppRuntimeWorker.getTicketName();
    }
}
