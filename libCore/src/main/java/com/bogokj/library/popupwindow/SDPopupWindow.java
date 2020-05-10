package com.bogokj.library.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.bogokj.library.SDLibrary;
import com.bogokj.library.utils.SDViewUtil;

public class SDPopupWindow extends PopupWindow implements View.OnClickListener
{

    private Context context;

    public SDPopupWindow()
    {
        this(SDLibrary.getInstance().getContext());
    }

    public SDPopupWindow(Context context)
    {
        super(context);
        this.context = context;
        baseInit();
    }

    public Activity getActivity()
    {
        Activity activity = null;
        if (context instanceof Activity)
        {
            activity = (Activity) context;
        }
        return activity;
    }

    private void baseInit()
    {
        SDViewUtil.wrapperPopupWindow(this);
    }

    @Override
    public void onClick(View v)
    {

    }

    public void setContentView(int resId)
    {
        View contentView = SDViewUtil.inflate(resId, null);
        setContentView(contentView);
    }
}
