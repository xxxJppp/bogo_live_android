package com.bogokj.live.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.appview.room.SVGAPlayLister;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LiveLargeGiftSVGAView extends BaseAppView {
    private SVGAImageView svga_img;
    private SVGAParser parser;

    /**
     * 是否处于忙碌
     */
    private boolean isBusy = false;
    /**
     * 是否正在播放gif
     */
    private boolean isPlaying = false;
    /**
     * 是否加载gif成功
     */
    private boolean isLoadGifSuccess = false;

    private CustomMsgGift msg;
    private SVGAPlayLister listener;

    private TextView tv_gift_desc;

    public LiveLargeGiftSVGAView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveLargeGiftSVGAView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveLargeGiftSVGAView(Context context) {
        super(context);
        init();
    }


    /**
     * 是否正在播放gif
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 是否忙碌
     *
     * @return
     */
    public boolean isBusy() {
        return isBusy;
    }

    /**
     * gif是否加载成功
     *
     * @return
     */
    public boolean isLoadGifSuccess() {
        return isLoadGifSuccess;
    }



    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_gift_svga_play, this, true);

        tv_gift_desc = (TextView) findViewById(R.id.tv_gift_desc);
        svga_img = (SVGAImageView) findViewById(R.id.svga_img);
        svga_img.setCallback(new SVGACallback() {

            @Override
            public void onStep(int i, double v) {

            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                stopAnimator();
                listener.onFinished();
            }
        });

        SDViewUtil.setInvisible(this);

    }

    public void setAnimatorListener(SVGAPlayLister listener) {
        this.listener = listener;
    }

    /**
     * 设置msg，调用此方法前要先调用setConfig方法设置gif配置
     *
     * @param msg
     * @return
     */
    public boolean setMsg(CustomMsgGift msg) {
        boolean isTarget = false;
        if (msg != null) {
            this.msg = msg;
            bindData(msg);
            isTarget = true;

        }
        return isTarget;
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimator();
        super.onDetachedFromWindow();
    }


    public void bindData(final CustomMsgGift msg) {
        if (msg != null) {

            try {
                isBusy = true;
                parser = new SVGAParser(getContext());
                resetDownloader(parser);
                parser.parse(new URL(msg.getAnimated_url()), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                        isLoadGifSuccess = true;
                        //动画
                        playAnimator(videoItem, msg.getTop_title());
                    }

                    @Override
                    public void onError() {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 你可以设置富文本到 ImageKey 相关的元素上
     * 富文本是会自动换行的，不要设置过长的文本
     *
     * @return
     */
    private SVGADynamicEntity requestDynamicItemWithSpannableText(String nickName) {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(nickName + getContext().getString(R.string.join));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28);
        dynamicEntity.setDynamicText(new StaticLayout(
                spannableStringBuilder,
                0,
                spannableStringBuilder.length(),
                textPaint,
                0,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                false
        ), "banner");
        dynamicEntity.setDynamicDrawer(new Function2<Canvas, Integer, Boolean>() {
            @Override
            public Boolean invoke(Canvas canvas, Integer frameIndex) {
                Paint aPaint = new Paint();
                aPaint.setColor(Color.WHITE);
                canvas.drawCircle(50, 54, frameIndex % 5, aPaint);
                return false;
            }
        }, "banner");
        return dynamicEntity;
    }

    /**
     * 设置下载器，这是一个可选的配置项。
     *
     * @param parser
     */
    private void resetDownloader(SVGAParser parser) {
        parser.setFileDownloader(new SVGAParser.FileDownloader() {
            @Override
            public void resume(final URL url, final Function1<? super InputStream, Unit> complete, final Function1<? super Exception, Unit> failure) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).get().build();
                        try {
                            Response response = client.newCall(request).execute();
                            complete.invoke(response.body().byteStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                            failure.invoke(e);
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 开始动画
     */
    public void playAnimator(SVGAVideoEntity videoItem, String text) {
        if (isPlaying) {
            return;
        }
        SDViewUtil.setVisible(this);
        SDViewBinder.setTextView(tv_gift_desc,text);

        isPlaying = true;
//        SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicItemWithSpannableText(nick_name));
        SVGADrawable drawable = new SVGADrawable(videoItem);
        svga_img.setImageDrawable(drawable);
        svga_img.setLoops(1);
        svga_img.setClearsAfterStop(true);
        svga_img.startAnimation();
    }


    public void stopAnimator() {
        if (svga_img != null) {
            svga_img.stopAnimation();
        }
        isBusy = false;
        isPlaying = false;
        isLoadGifSuccess = false;
        SDViewUtil.setInvisible(this);
    }


}


