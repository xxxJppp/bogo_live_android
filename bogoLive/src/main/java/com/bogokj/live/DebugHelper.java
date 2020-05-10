package com.bogokj.live;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.constant.ApkConstant;
import com.bogokj.library.common.SDActivityManager;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.dialog.impl.SDDialogMenu;
import com.bogokj.library.utils.SDOtherUtil;
import com.bogokj.library.utils.SDPackageUtil;
import com.bogokj.library.utils.SDShakeListener;
import com.bogokj.live.activity.LiveLoginActivity;
import com.bogokj.live.activity.LiveMainActivity;
import com.bogokj.live.common.HostManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 仅用于bogokj内部开发调试帮助
 */
public class DebugHelper
{
    private static SDShakeListener shakeListener;

    private DebugHelper()
    {
    }

    public static void init(Application app)
    {
        if (!ApkConstant.DEBUG)
        {
            return;
        }

        if (shakeListener == null)
        {
            shakeListener = new SDShakeListener(app);
        }
        shakeListener.setShakeCallback(new SDShakeListener.ShakeCallback()
        {
            @Override
            public void onShake()
            {
                if (!SDPackageUtil.isBackground())
                {
                    DebugHelper.onShake();
                }
            }
        }).start();
    }

    public static void release()
    {
        if (shakeListener != null)
        {
            shakeListener.stop();
            shakeListener.setShakeCallback(null);
            shakeListener = null;
        }
    }

    private static List<Class<?>> getCanShakeClass()
    {
        List<Class<?>> listClass = new ArrayList<>();
        listClass.add(LiveMainActivity.class);
        listClass.add(LiveLoginActivity.class);
        return listClass;
    }

    private static void onShake()
    {
        if (!ApkConstant.DEBUG)
        {
            return;
        }
        Activity activity = SDActivityManager.getInstance().getLastActivity();
        if (activity == null)
        {
            return;
        }
        if (!getCanShakeClass().contains(activity.getClass()))
        {
            return;
        }

        String title = "当前站：" + SDOtherUtil.getHost(HostManager.getInstance().getApiUrl());
        Object[] arrItem = new String[]{
                "原始站：" + SDOtherUtil.getHost(ApkConstant.SERVER_URL),};

        SDDialogMenu dialog = new SDDialogMenu(activity);
        dialog.setTextTitle(title);
        dialog.setItems(arrItem);
        dialog.setCallback(new ISDDialogMenu.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {

            }

            @Override
            public void onClickItem(View v, int index, SDDialogBase dialog)
            {
                String url = null;
                switch (index)
                {
                    case 0:
                        url = ApkConstant.SERVER_URL;
                        break;
                    default:
                        url = ApkConstant.SERVER_URL;
                        break;
                }

                HostManager.getInstance().setServerUrl(url);
                App.getApplication().logout(false);
            }
        });
        dialog.showBottom();
    }
}
