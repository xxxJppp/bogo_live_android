package com.bogokj.live.dialog;

import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.live.R;
import com.bogokj.library.utils.SDViewUtil;

/**
 * 守护特权详情弹窗
 */
public class LiveGuardSpecialDetailDialog extends LiveBaseDialog {

    // private final boolean isCreater;
    //守护特权内容
    private String content;
    private Activity activity;
    private WebView webView;
    private int mDefaultHeight;
    private String  guardid;
    public LiveGuardSpecialDetailDialog(Activity activity,String id) {
        super(activity);
        this.activity = activity;
        this.content = content;
        this.guardid = id;
        initView();
        //获取守护信息
       // getGuardInfo();
    }


    private class WebViewClientListener extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }
    }

    private void initView() {
        setContentView(R.layout.dialog_guard_special_detail);
        webView = findViewById(R.id.special_detail_webView);

        mDefaultHeight = SDViewUtil.getScreenHeightPercent(0.5f);
        setHeight(mDefaultHeight);
        paddingLeft(80);
        paddingRight(80);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        //加载需要显示的网页
        InitActModel initActModel = InitActModelDao.query();
        webView.loadUrl(initActModel.getH5_url().getGuartian_special_effects()+"&id="+guardid);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());

    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



}
