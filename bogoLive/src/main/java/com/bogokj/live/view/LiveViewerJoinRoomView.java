package com.bogokj.live.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.hybrid.app.App;
import com.bogokj.library.common.SDHandlerManager;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.dialog.LiveUserInfoDialog;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsgViewerJoin;
import com.bogokj.live.utils.GlideUtil;

public class LiveViewerJoinRoomView extends LinearLayout
{
    private static final long DURATION_IN = 1200;
    private static final long DURATION_OUT = 200;
    private static final long DURATION_DELAY = 2 * 1000;

    private LinearLayout ll_user;
    private ImageView user_vip_head_img_back;
    private TextView tv_nickname;

    private LinearLayout live_viewer_join_back_ll;
    private TextView iv_level_num;

    private ImageView iv_guard;

    private AnimatorSet animatorSetIn;
    private AnimatorSet animatorSetOut;
    private boolean isPlaying;
    private CustomMsgViewerJoin msg;

    private RelativeLayout rl_vip;

    private CircleImageView iv_pic;
    //等级图片
    private ImageView iv_rank;
    private ImageView iv_vip;

    public LiveViewerJoinRoomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveViewerJoinRoomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveViewerJoinRoomView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_viewer_join_room, this, true);

        ll_user = (LinearLayout) findViewById(R.id.ll_user);
        user_vip_head_img_back = (ImageView) findViewById(R.id.user_vip_head_img_back);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        iv_guard= (ImageView) findViewById(R.id.iv_guard);
        live_viewer_join_back_ll = (LinearLayout) findViewById(R.id.live_viewer_join_back_ll);
        iv_level_num = (TextView) findViewById(R.id.iv_level_num);
        rl_vip = (RelativeLayout) findViewById(R.id.rl_vip);
        iv_pic = (CircleImageView) findViewById(R.id.iv_pic);
        iv_rank = (ImageView) findViewById(R.id.iv_rank);

        iv_vip = (ImageView) findViewById(R.id.iv_vip);


        ll_user.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (msg != null)
                {
                    LiveUserInfoDialog dialog = new LiveUserInfoDialog((Activity) getContext(), msg.getSender().getUser_id());
                    dialog.showCenter();
                }
            }
        });

        SDViewUtil.setInvisible(this);
    }

    public CustomMsgViewerJoin getMsg()
    {
        return msg;
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public boolean canPlay()
    {
        return !isPlaying;
    }

    private void setMsg(CustomMsgViewerJoin msg)
    {
        this.msg = msg;
    }

    public void playMsg(CustomMsgViewerJoin newMsg)
    {
        if (newMsg != null)
        {
            if (canPlay())
            {
                isPlaying = true;
                setMsg(newMsg);
                SDHandlerManager.getMainHandler().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        bindData();
                        playIn();
                    }
                });
            }
        }
    }

    private void bindData()
    {
        UserModel sender = msg.getSender();
        if (sender != null)
        {
//            int resId = sender.getLevelImageResId();
//            if (resId != 0)
//            {
//                iv_level.setImageResource(resId);
//            }


            GlideUtil.loadHeadImage(sender.getHead_image()).into(iv_pic);
            if (sender.getIs_vip() == 1){
                user_vip_head_img_back.setImageResource(R.drawable.kings_head_img);
                //Typeface mtypeface= Typeface.createFromAsset(App.getApplication().getAssets(),"huawenxingkai.ttf");
                //tv_nickname.setTypeface(mtypeface);
            }
            //判断是否是守护
            if(msg.getSender().getIs_guardian()==1){
                iv_guard.setVisibility(VISIBLE);
            }

            if (msg.getSender().getIs_vip() == 1){
                iv_vip.setVisibility(VISIBLE);
            }

            SDViewBinder.setTextView(iv_level_num, "" + sender.getUser_level());
            SDViewBinder.setTextView(tv_nickname, sender.getNick_name());
            iv_rank.setImageResource(sender.getLevelImageResId());
        }
    }

    private void playIn()
    {
        if (animatorSetIn == null)
        {
            ObjectAnimator inTranslationX = ObjectAnimator.ofFloat(this, "translationX", -SDViewUtil.getWidth(this), 0f);
            inTranslationX.setInterpolator(new DecelerateInterpolator());
            inTranslationX.setDuration(DURATION_IN);

            animatorSetIn = new AnimatorSet();
            animatorSetIn.playTogether(inTranslationX);
            animatorSetIn.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    SDViewUtil.setVisible(LiveViewerJoinRoomView.this);
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    SDHandlerManager.getMainHandler().postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            playOut();
                        }
                    }, DURATION_DELAY);
                }
            });
        }
        animatorSetIn.start();
    }

    private void playOut()
    {
        if (animatorSetOut == null)
        {
            ObjectAnimator outTranslationY = ObjectAnimator.ofFloat(this, "translationY", 0f, -SDViewUtil.getHeight(this));
            outTranslationY.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator outAlpha = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
            outAlpha.setInterpolator(new DecelerateInterpolator());

            animatorSetOut = new AnimatorSet();
            animatorSetOut.playTogether(outTranslationY, outAlpha);
            animatorSetOut.setDuration(DURATION_OUT);
            animatorSetOut.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    SDViewUtil.setInvisible(LiveViewerJoinRoomView.this);
                    SDViewUtil.resetView(LiveViewerJoinRoomView.this);
                    isPlaying = false;
                }
            });
        }
        animatorSetOut.start();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        stopAnimator(animatorSetIn);
        super.onDetachedFromWindow();
    }

    private void stopAnimator(Animator animator)
    {
        if (animator != null)
        {
            animator.cancel();
            animator.removeAllListeners();
        }
    }
}
