package com.bogokj.live.utils;

import com.bogokj.hybrid.app.App;
import com.bogokj.library.utils.SDDateUtil;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ReporterUtil
{
    public static void reportError(String msg)
    {
        UserModel user = UserModelDao.query();
        if (user != null)
        {
            msg = "(user:" + user.getUser_id() + ") " + msg;
        }

        String time = SDDateUtil.getNow_yyyyMMddHHmmss();
        msg = "Android " + time + " " + msg;

        MobclickAgent.reportError(App.getApplication(), msg);
        CommonInterface.reportErrorLog(msg);
    }

}
