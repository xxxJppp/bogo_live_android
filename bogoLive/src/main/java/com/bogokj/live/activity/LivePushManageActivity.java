package com.bogokj.live.activity;

import android.os.Bundle;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.fanwe.lib.switchbutton.ISDSwitchButton;
import com.fanwe.lib.switchbutton.SDSwitchButton;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.UserModel;

/**
 * 推送管理
 */
public class LivePushManageActivity extends BaseTitleActivity
{
    private SDSwitchButton sb_push;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_push_manage);
        init();
    }

    private void init()
    {
        sb_push = (SDSwitchButton) findViewById(R.id.sb_push);
        initTitle();
        initSDSlidingButton();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("推送管理");
    }

    private void initSDSlidingButton()
    {
        UserModel user = UserModelDao.query();
        if (user != null)
        {
            if (user.getIs_remind() == 1)
            {
                sb_push.setChecked(true, false, false);
            } else
            {
                sb_push.setChecked(false, false, false);
            }
        }
        sb_push.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback()
        {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view)
            {
                if (checked)
                {
                    CommonInterface.requestSet_push(1, null);
                } else
                {
                    CommonInterface.requestSet_push(0, null);
                }
            }
        });
    }
}
