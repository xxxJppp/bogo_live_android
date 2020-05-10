package com.bogokj.live.runnable;

import android.app.Activity;

import com.bogokj.library.common.SDActivityManager;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.JoinPlayBackData;
import com.bogokj.live.room.activity.LiveBackActivity;
import com.fanwe.lib.looper.impl.SDWaitRunner;

/**
 * Created by Administrator on 2017/7/11.
 */

public class JoinPlayBackRunnable implements Runnable
{
    private JoinPlayBackData mData;

    public JoinPlayBackRunnable(JoinPlayBackData data)
    {
        mData = data;
    }

    @Override
    public void run()
    {
        final Activity activity = SDActivityManager.getInstance().getLastActivity();
        if (activity == null)
        {
            return;
        }

        new SDWaitRunner().run(new Runnable()
        {
            @Override
            public void run()
            {
                AppRuntimeWorker.joinPlayback(mData, activity);
            }
        }).condition(new SDWaitRunner.Condition()
        {
            @Override
            public boolean canRun()
            {
                if (SDActivityManager.getInstance().containActivity(LiveBackActivity.class)
                        || LiveInformation.getInstance().getRoomId() > 0)
                {
                    return false;
                } else
                {
                    return true;
                }
            }
        }).setTimeout(10 * 1000).startWait(100);
    }
}
