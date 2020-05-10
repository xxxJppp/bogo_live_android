package com.bogokj.live.activity;

import android.os.Bundle;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.jshandler.AppJsHandler;
import com.bogokj.library.webview.CustomWebView;
import com.bogokj.library.webview.DefaultWebChromeClient;
import com.bogokj.library.webview.DefaultWebViewClient;
import com.bogokj.live.R;
import com.bogokj.live.event.EPayWebViewClose;

import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

/**
 * Created by shibx on 2017/3/24.
 */

public class LivePayWebViewActivity extends BaseTitleActivity
{
    public static final String EXTRA_URL = "extra_url";

    @ViewInject(R.id.cus_webview)
    private CustomWebView cus_webview;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_pay_web);
        init();
        initIntent();
    }

    private void init()
    {
        initTitle();
        initIntent();
        initView();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("详情");
    }

    private void initIntent()
    {
        mUrl = getIntent().getStringExtra(EXTRA_URL);
    }

    private void initView()
    {
        AppJsHandler handler = new AppJsHandler(this);
        cus_webview.addJavascriptInterface(handler);
        cus_webview.setWebChromeClientListener(new DefaultWebChromeClient());
        cus_webview.get(mUrl);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //发送页面关闭事件
        //充值窗口刷新
        EventBus.getDefault().post(new EPayWebViewClose());
    }
}
