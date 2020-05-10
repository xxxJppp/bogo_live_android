package com.bogokj.live.dialog;

import android.app.Activity;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveMainActivity;

/**
 * 第一次登陆
 */
public class LevelLoginFirstDialog extends LevelBaseDialog {

    public LevelLoginFirstDialog(Activity activity) {
        super(activity);
        initSetView();
    }

    private void initSetView() {
        setImageResource(R.drawable.ic_level_login_first);
        setTitle("每日首次登录奖励");
        setContent("经验+10");
    }

    public static void check(Activity activity) {
        if (activity == null) {
            return;
        }
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            if (model.getOpen_login_send_score() == 1) {//首次登陆奖励开关是否开启
                if (model.getFirst_login() == 1) {//是否是每日首次登录
                    LevelLoginFirstDialog levelLoginFirstDialog = new LevelLoginFirstDialog(activity);
                    levelLoginFirstDialog.setContent("经验+" + model.getLogin_send_score());
                    levelLoginFirstDialog.show();
                    model.setFirst_login(0);
                    InitActModelDao.insertOrUpdate(model);
                }
            }
        }
    }
}
