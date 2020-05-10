package com.bogokj.live.dialog;

import android.app.Activity;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.bogokj.live.R;
import com.bogokj.live.room.ILiveInterface;

public class LiveBaseDialog extends SDDialogBase
{
    public LiveBaseDialog(Activity activity)
    {
        super(activity, R.style.dialogBase);
    }

    public ILiveInterface getLiveActivity()
    {
        if (getOwnerActivity() instanceof ILiveInterface)
        {
            return (ILiveInterface) getOwnerActivity();
        } else
        {
            return null;
        }
    }
}
