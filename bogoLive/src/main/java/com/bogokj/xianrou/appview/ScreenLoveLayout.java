package com.bogokj.xianrou.appview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bogokj.live.R;

import java.util.Random;

/**
 * Created by Administrator on 2019/1/7 0024.
 */

public class ScreenLoveLayout extends RelativeLayout {
    private static int timeout=400;//双击间四百毫秒延时
    private int clickCount = 0;//记录连续点击次数
    private Handler handler;
    private Context mContext;
    float[] num = {-30, -20, 0, 20, 30};//随机心形图片角度

    private MyClickCallBack myClickCallBack;
    public interface MyClickCallBack{
        void oneClick();//点击一次的回调
        void doubleClick();//连续点击两次的回调

    }

    public void setMyClickCallBack(MyClickCallBack myClickCallBack) {
        this.myClickCallBack = myClickCallBack;
    }

    public ScreenLoveLayout(Context context) {
        super(context);
        initView(context);
    }

    public ScreenLoveLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScreenLoveLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        handler = new Handler();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clickCount++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (clickCount == 1) {
                        myClickCallBack.oneClick();
                    }else if(clickCount==2){
                        final ImageView imageView = new ImageView(mContext);
                        LayoutParams params = new LayoutParams(300, 300);
                        params.leftMargin = (int) event.getX() - 150;
                        params.topMargin = (int) event.getY() - 300;
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.video_details_like_number_press));
                        imageView.setLayoutParams(params);
                        addView(imageView);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0))
                                .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0))
                                .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)]))
                                .with(alpha(imageView, 0, 1, 100, 0))
                                .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                                .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                                .with(translationY(imageView, 0, -600, 800, 400))
                                .with(alpha(imageView, 1, 0, 300, 400))
                                .with(scale(imageView, "scaleX", 1, 3f, 700, 400))
                                .with(scale(imageView, "scaleY", 1, 3f, 700, 400));

                        animatorSet.start();
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                removeViewInLayout(imageView);
                            }
                        });


                        myClickCallBack.doubleClick();


                    }
                    handler.removeCallbacksAndMessages(null);
                    //清空handler延时，并防内存泄漏
                    clickCount = 0;//计数清零
                }
            },timeout);//延时timeout后执行run方法中的代码
        }
        return false;



      //  return super.onTouchEvent(event);
    }

    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }

}
