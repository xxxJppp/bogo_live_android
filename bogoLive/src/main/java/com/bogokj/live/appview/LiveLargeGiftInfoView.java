package com.bogokj.live.appview;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.fanwe.lib.animator.SDAnim;
import com.fanwe.lib.animator.listener.SDAnimatorListener;
import com.bogokj.library.model.SDDelayRunnable;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsgLargeGift;

/**
 * 大型礼物通知view
 */
public class LiveLargeGiftInfoView extends BaseAppView
{
    public LiveLargeGiftInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveLargeGiftInfoView(Context context)
    {
        super(context);
        init();
    }

    private static final long DURATION_ANIM = 12 * 1000;
    /**
     * view在屏幕外的偏移量，为了让平移看上去更顺滑
     */
    private static final int DISTANCE_OFFSET = SDViewUtil.dp2px(30);

    private TextView tv_content;
    private CustomMsgLargeGift mMsg;
    private SDAnim mAnim;

    private void init()
    {
        setContentView(R.layout.view_live_large_gift_info);
        tv_content = (TextView) findViewById(R.id.tv_content);

        SDViewUtil.setInvisible(this);
    }

    public CustomMsgLargeGift getMsg()
    {
        return mMsg;
    }

    public boolean isPlaying()
    {
        if (mAnim != null)
        {
            return mAnim.isRunning();
        } else
        {
            return false;
        }
    }

    public void playMsg(CustomMsgLargeGift msg)
    {
        if (isPlaying())
        {
            return;
        }
        mMsg = msg;
        tv_content.setText(msg.getDesc());

        mPlayAnimRunnable.runDelay(500);
    }

    private SDDelayRunnable mPlayAnimRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            playAnim();
        }
    };

    private void playAnim()
    {
        if (mAnim == null)
        {
            mAnim = SDAnim.from(this)
                    .setDuration(DURATION_ANIM)
                    .setInterpolator(new LinearInterpolator())
                    .addListener(new SDAnimatorListener()
                    {
                        @Override
                        public void onAnimationStart(Animator animation)
                        {
                            super.onAnimationStart(animation);
                            SDViewUtil.setVisible(LiveLargeGiftInfoView.this);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            super.onAnimationEnd(animation);
                            SDViewUtil.resetView(LiveLargeGiftInfoView.this);
                            SDViewUtil.setInvisible(LiveLargeGiftInfoView.this);
                            mMsg = null;
                        }
                    });
        }

        int x1 = SDViewUtil.getScreenWidth() + DISTANCE_OFFSET;
        int x2 = -SDViewUtil.getWidth(this) - DISTANCE_OFFSET;
        mAnim = mAnim.moveToX(x1, x2);
        mAnim.start();
    }

    private void stopAnim()
    {
        if (mAnim != null)
        {
            mAnim.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mPlayAnimRunnable.removeDelay();
        stopAnim();
    }
}
