package com.bogokj.live.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.common.CommonOpenSDK;
import com.bogokj.hybrid.constant.Constant;
import com.bogokj.hybrid.event.EPaySdk;
import com.bogokj.hybrid.event.ERefreshReload;
import com.bogokj.hybrid.http.AppHttpUtil;
import com.bogokj.hybrid.listner.PayResultListner;
import com.bogokj.hybrid.model.PaySdkModel;
import com.bogokj.library.utils.SDFileUtil;
import com.bogokj.library.utils.SDOtherUtil;
import com.bogokj.library.utils.SDSystemUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.webview.CustomWebView;
import com.bogokj.library.webview.DefaultWebViewClient;
import com.bogokj.live.R;
import com.bogokj.live.event.EWxPayResultCodeComplete;
import com.bogokj.live.wxapi.WXPayEntryActivity;
import com.bogokj.o2o.jshandler.O2OShoppingLiveJsHander;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;
import org.xutils.view.annotation.ViewInject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.bogokj.live.appview.H5AppViewWeb.no_network_url;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class LiveWebViewActivity extends BaseActivity {
    private static final int REQUEST_CODE_BAOFOO_SDK_RZ = 100;// 宝付支付

    public static final String EXTRA_URL = "extra_url";

    public static final String EXTRA_IS_SHOW_TITLE = "extra_is_show_title";

    private String mUrl;

    private boolean mIsShowTitle;//是否展示标题

    @ViewInject(R.id.rl_title)
    private RelativeLayout rl_title;
    @ViewInject(R.id.view_back)
    private View view_back;
    @ViewInject(R.id.tv_finish)
    private TextView tv_finish;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.cus_webview)
    protected CustomWebView customWebView;

    @ViewInject(R.id.svga_img)
    SVGAImageView svga_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_edit_data);
        init();
    }

    protected void init() {
        initIntent();
        initWebView();
        initListener();
    }

    private void initIntent() {
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mIsShowTitle = getIntent().getBooleanExtra(EXTRA_IS_SHOW_TITLE, true);
        if (!mIsShowTitle) {
            SDViewUtil.setGone(rl_title);
        }
    }

    private void initWebView() {
        customWebView.addJavascriptInterface(new O2OShoppingLiveJsHander(this, customWebView));
        customWebView.getSettings().setBuiltInZoomControls(true);

        customWebView.setWebViewClient(new DefaultWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("vipshop://") || url.contains("tbopen://")) {
                    return false;
                }

                if (url.contains("svgaurl")) {
                    SVGAParser parser = new SVGAParser(getActivity());
                    svga_img.setLoops(1);
                    try {
                        parser.parse(new URL(Uri.parse(url).getQueryParameter("svgaurl")), new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                                SVGADrawable drawable = new SVGADrawable(videoItem);
                                svga_img.setImageDrawable(drawable);
                                svga_img.startAnimation();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                if (url.contains("live://") && Uri.parse(url).getQueryParameter("action").equals("copy_share_url")) {

                    SDOtherUtil.copyText(Uri.parse(url).getQueryParameter("copy"));
                    SDToast.showToast("复制成功");
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                customWebView.loadUrl(no_network_url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                tv_title.setText(view.getTitle());
            }
        });

        customWebView.setWebChromeClientListener(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tv_title.setText(view.getTitle());
            }
        });

        AppHttpUtil.getInstance().synchronizeHttpCookieToWebView(mUrl);
        customWebView.get(mUrl);
    }

    private void initListener() {
        view_back.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        customWebView.reload();
    }

    @Override
    public void onBackPressed() {
        if (customWebView.canGoBack()) {
            customWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 网络刷新
     *
     * @param event
     */
    public void onEventMainThread(ERefreshReload event) {
        customWebView.loadUrl(mUrl);
        customWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //清除历史记录
                customWebView.clearHistory();
            }
        }, 1000);
    }


    /**
     * 支付SDK
     */
    public void onEventMainThread(EPaySdk event) {
        openSDKPAY(event.model);
    }

    public void openSDKPAY(PaySdkModel model) {
        String payCode = model.getPay_sdk_type();
        if (!TextUtils.isEmpty(payCode)) {
            if (Constant.PaymentType.UPAPP.equalsIgnoreCase(payCode)) {
                CommonOpenSDK.payUpApp(model, this, mPayResultListner);
            } else if (Constant.PaymentType.BAOFOO.equalsIgnoreCase(payCode)) {
                CommonOpenSDK.payBaofoo(model, this, REQUEST_CODE_BAOFOO_SDK_RZ, mPayResultListner);
            } else if (Constant.PaymentType.ALIPAY.equalsIgnoreCase(payCode)) {
                CommonOpenSDK.payAlipay(model, this, mPayResultListner);
            } else if (Constant.PaymentType.WXPAY.equals(payCode)) {
                CommonOpenSDK.payWxPay(model, this, mPayResultListner);
            }
        }
    }

    /*微信支付回调返回信息*/
    public void onEventMainThread(final EWxPayResultCodeComplete event) {
        switch (event.WxPayResultCode) {
            case WXPayEntryActivity.RespErrCode.CODE_CANCEL:
                mPayResultListner.onCancel();
                break;
            case WXPayEntryActivity.RespErrCode.CODE_FAIL:
                mPayResultListner.onFail();
                break;
            case WXPayEntryActivity.RespErrCode.CODE_SUCCESS:
                mPayResultListner.onSuccess();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == view_back) {
            if (customWebView.canGoBack()) {
                customWebView.goBack();
            } else {
                finish();
            }
        } else if (v == tv_finish) {
            finish();
        }
    }

    private PayResultListner mPayResultListner = new PayResultListner() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customWebView.loadJsFunction(Constant.JsFunctionName.JS_PAY_SDK, 1);
                }
            });
        }

        @Override
        public void onDealing() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customWebView.loadJsFunction(Constant.JsFunctionName.JS_PAY_SDK, 2);
                }
            });
        }

        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customWebView.loadJsFunction(Constant.JsFunctionName.JS_PAY_SDK, 3);
                }
            });
        }

        @Override
        public void onCancel() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customWebView.loadJsFunction(Constant.JsFunctionName.JS_PAY_SDK, 4);
                }
            });
        }

        @Override
        public void onNetWork() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customWebView.loadJsFunction(Constant.JsFunctionName.JS_PAY_SDK, 5);
                }
            });
        }

        @Override
        public void onOther() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customWebView.loadJsFunction(Constant.JsFunctionName.JS_PAY_SDK, 6);
                }
            });
        }
    };
}
