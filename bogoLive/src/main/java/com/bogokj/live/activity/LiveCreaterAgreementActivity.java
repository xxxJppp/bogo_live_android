package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.webview.CustomWebView;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.UserModel;

/**
 * Created by L on 2016/7/9.
 */
public class LiveCreaterAgreementActivity extends BaseTitleActivity
{

    private CustomWebView webView;
    private TextView tv_agree;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.act_live_creater_agreement;
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);
        webView = find(R.id.webview);
        tv_agree = find(R.id.tv_agree);

        mTitle.setMiddleTextTop("主播协议");

        InitActModel initActModel = InitActModelDao.query();
        if (initActModel == null)
        {
            SDToast.showToast("未找到初始化参数");
            finish();
            return;
        }

        final String url = initActModel.getAgreement_link();
        if (TextUtils.isEmpty(url))
        {
            SDToast.showToast("主播协议地址为空");
            finish();
            return;
        }

        webView.setScaleToShowAll(false);
        webView.get(url);

        tv_agree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clickAgree();
            }
        });
    }

    private void clickAgree()
    {
        CommonInterface.requestAgree(new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    UserModel user = UserModelDao.query();
                    if (user != null)
                    {
                        user.setIs_agree(1);
                        UserModelDao.insertOrUpdate(user);

                        Intent intent = new Intent(LiveCreaterAgreementActivity.this, LiveCreateRoomActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
