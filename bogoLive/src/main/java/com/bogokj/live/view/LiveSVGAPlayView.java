package com.bogokj.live.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.R;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.appview.room.SVGAPlayLister;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgBuyGuardianSuccess;
import com.bogokj.live.model.custommsg.CustomMsgViewerJoin;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
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

public class LiveSVGAPlayView<T> extends BaseAppView {
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


    private SVGAPlayLister listener;
    private SVGADrawable drawable;

    public LiveSVGAPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveSVGAPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveSVGAPlayView(Context context) {
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


    private TextView tv_desc;
    private  TextView tv_nickname;

    private CircleImageView circleImageView;

    private LinearLayout linearLayout;
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_svga_play, this, true);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_nickname= (TextView) findViewById(R.id.tv_nickname);
        circleImageView = (CircleImageView) findViewById(R.id.iv_pic);
        linearLayout = (LinearLayout)findViewById(R.id.ll_car_top);


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
    public boolean setMsg(T msg) {
        boolean isTarget = false;
        if (msg != null) {
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


    public void bindData(final T msg) {
        if (msg != null) {
            //购买守护者成功
            if (msg instanceof CustomMsgBuyGuardianSuccess){
                final CustomMsgBuyGuardianSuccess  customMsg = (CustomMsgBuyGuardianSuccess) msg;
                final UserModel sender = customMsg.getSender();
                if (sender != null) {
                    try {
                        isBusy = true;
                        parser = new SVGAParser(getContext());
                        resetDownloader(parser);
                        parser.parse(new URL(customMsg.getSvga_url()), new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                                //座驾动画
                                playAnimator(videoItem, customMsg.getSvga_text(),true ,sender.getHead_image());
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg instanceof CustomMsgViewerJoin){
                final CustomMsgViewerJoin  customMsg = (CustomMsgViewerJoin) msg;
                final UserModel sender = customMsg.getSender();
                if (sender != null) {
                    try {
                        isBusy = true;
                        parser = new SVGAParser(getContext());
                        resetDownloader(parser);
                        parser.parse(new URL(sender.getNoble_car_url()), new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                                //头像
                                GlideUtil.loadHeadImage(sender.getHead_image()).into(circleImageView);
                                tv_nickname.setText(sender.getNick_name());

                                ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationX", SDViewUtil.getScreenWidth(), 0);
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                    }
                                });
                                animator.setDuration(500).start();
                                //座驾动画sender.getNick_name() + "乘座驾进入了房间。"
                                playAnimator(videoItem,   "乘坐【"+sender.getNoble_car_name()+"】驾到",false ,sender.getHead_image());
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
    public void playAnimator(SVGAVideoEntity videoItem, String text,boolean isBuyGuardian,String headImg) {
        if (isPlaying) {
            return;
        }
        SDViewUtil.setVisible(this);
        isPlaying = true;
        SDViewBinder.setTextView(tv_desc, text);
//        SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicItemWithSpannableText(nick_name));

        if (isBuyGuardian) {
//            LogUtil.d("======>购买守护者成功动画");
            SVGADynamicEntity dynamicItem = requestDynamicItem(headImg,text);
            drawable = new SVGADrawable(videoItem, dynamicItem);
        } else {
            drawable = new SVGADrawable(videoItem);
        }

//        SVGADrawable drawable = new SVGADrawable(videoItem);
        svga_img.setImageDrawable(drawable);
        svga_img.setLoops(1);
        svga_img.setClearsAfterStop(true);
        svga_img.startAnimation();
    }
    private SVGADynamicEntity requestDynamicItem(String url,String name) {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28);
        dynamicEntity.setDynamicImage(url,"99");
        dynamicEntity.setDynamicText(name, textPaint, "banner");
        return dynamicEntity;
    }

    public void stopAnimator() {
        if (svga_img != null) {
            svga_img.stopAnimation();
        }
        isBusy = false;
        isPlaying = false;
        SDViewUtil.setInvisible(this);
    }


}


