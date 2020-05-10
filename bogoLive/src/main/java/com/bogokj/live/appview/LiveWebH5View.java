package com.bogokj.live.appview;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.http.AppHttpUtil;
import com.bogokj.live.R;
import com.bogokj.live.appview.main.LiveTabBaseView;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.o2o.jshandler.O2OShoppingLiveJsHander;
import com.bogokj.library.utils.SDOtherUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.webview.CustomWebView;
import com.bogokj.library.webview.DefaultWebViewClient;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public class LiveWebH5View  extends LiveTabBaseView {

    private CustomWebView customWebView;
    private SVGAImageView svga_img;

    public LiveWebH5View(Context context)
    {
        super(context);
        init();
    }

    public LiveWebH5View(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveWebH5View(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        setContentView(R.layout.view_web_h5);
        customWebView = find(R.id.web_view);
        svga_img = find(R.id.svga_img);

        customWebView.setWebViewClient(new DefaultWebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains("vipshop://")||url.contains("tbopen://"))
                {
                    return false;
                }

                if(url.contains("svgaurl")){
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

                if(url.contains("live://") && Uri.parse(url).getQueryParameter("action").equals("copy_share_url")){

                    SDOtherUtil.copyText(Uri.parse(url).getQueryParameter("copy"));
                    SDToast.showToast("复制成功");
                    return true;
                } else
                {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //customWebView.loadUrl(no_network_url);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
            }
        });

    }

    public void loadUrl(String url){
        //String url = InitActModelDao.query().getH5_url().getPay_car() + "&user_id="+ UserModelDao.query().getUser_id();
        AppHttpUtil.getInstance().synchronizeHttpCookieToWebView(url);
        customWebView.get(url);
        customWebView.addJavascriptInterface(new O2OShoppingLiveJsHander(getActivity(), customWebView));
        customWebView.getSettings().setBuiltInZoomControls(true);
    }

    @Override
    protected void onRoomClosed(int roomId) {

    }

    @Override
    public void scrollToTop() {

    }

    @Override
    protected void onLoopRun() {

    }
}
