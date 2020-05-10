package com.bogokj.xianrou.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bogokj.live.common.AppRuntimeWorker;

/**
 * 包名: com.bogokj.qingke.widget
 * 描述:
 * 作者: Su
 * 日期: 2017/7/13 14:59
 **/
public class QKDiamondTextView extends ReplaceableTextView
{
    public QKDiamondTextView(Context context)
    {
        super(context);
    }

    public QKDiamondTextView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QKDiamondTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String getTargetString()
    {
        return AppRuntimeWorker.getDiamondName();
    }
}
