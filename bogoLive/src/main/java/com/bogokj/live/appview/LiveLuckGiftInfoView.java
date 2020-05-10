package com.bogokj.live.appview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsgLargeGift;
import com.bogokj.live.model.custommsg.CustomMsgLuckGift;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
import com.fanwe.lib.animator.SDAnim;
import com.fanwe.lib.animator.listener.SDAnimatorListener;
import com.bogokj.library.model.SDDelayRunnable;
import com.bogokj.library.utils.SDViewUtil;

public class LiveLuckGiftInfoView extends BaseAppView{
    private ObjectAnimator animator;

    public LiveLuckGiftInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveLuckGiftInfoView(Context context)
    {
        super(context);
        init();
    }

    //中奖信息
    private TextView tv_content;
    private CustomMsgLuckGift mMsg;
    private ObjectAnimator mAnim;

    //幸运礼物
    private FrameLayout luck_gift_framelayut;
    //昵称
    private TextView tv_name;
    //头像
    private ImageView iv_head;
    //等级
    private ImageView iv_rank;

    private void init()
    {
        setContentView(R.layout.view_live_luck_gift_info);
        //幸运礼物
        luck_gift_framelayut = find(R.id.luck_gift_framelayut);
        //昵称
        tv_name = find(R.id.tv_name);
        //中奖信息
        tv_content = find(R.id.tv_content);
        //等级
        iv_rank = find(R.id.iv_rank);
        //头像
        iv_head = find(R.id.iv_head);

        SDViewUtil.setInvisible(this);
    }

    public CustomMsgLuckGift getMsg()
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

    public void playMsg(CustomMsgLuckGift msg)
    {
        if (isPlaying())
        {
            return;
        }
        mMsg = msg;

        tv_name.setText(msg.getSender().getNick_name());
        tv_content.setText(msg.getText());
        GlideUtil.loadHeadImage(msg.getSender().getHead_image()).into(iv_head);
        iv_rank.setImageResource(LiveUtils.getLevelImageResId(msg.getSender().getUser_level()));
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
            luck_gift_framelayut.setVisibility(VISIBLE);
            mAnim = ObjectAnimator.ofFloat(luck_gift_framelayut, "translationX", SDViewUtil.getScreenWidth(), -SDViewUtil.getScreenWidth());
            mAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    SDViewUtil.resetView(LiveLuckGiftInfoView.this);
                    SDViewUtil.setInvisible(LiveLuckGiftInfoView.this);
                    mMsg = null;
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    SDViewUtil.setVisible(LiveLuckGiftInfoView.this);
                }
            });
        }

        mAnim.setDuration(7000).start();
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
