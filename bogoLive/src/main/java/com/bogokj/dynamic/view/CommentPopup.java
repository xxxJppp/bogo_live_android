package com.bogokj.dynamic.view;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/16.
 * 微信朋友圈评论弹窗
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView item_tv_common_count;
    private View ll_share_dynamic;
    private ImageView mLikeAnimaView;
    private TextView mLikeText;

    private View mLikeClikcLayout;
    private View mCommentClickLayout;


    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    private Handler mHandler;

    public CommentPopup(Context context,int likeCount,boolean isLike ,int comentCount) {
        super(context);
        mHandler = new Handler();

        mLikeAnimaView = (ImageView) findViewById(R.id.item_iv_like_count);
        mLikeText = (TextView) findViewById(R.id.item_tv_like_count);
        mLikeText.setText(String.valueOf(likeCount));
        if (isLike){
            mLikeAnimaView.setImageResource(R.drawable.ic_dynamic_thumbs_up_s);
        } else {
            mLikeAnimaView.setImageResource(R.drawable.ic_dynamic_thumbs_up_n);
        }

        item_tv_common_count = (TextView) findViewById(R.id.item_tv_common_count);
        item_tv_common_count.setText(String.valueOf(comentCount));
        mLikeClikcLayout = findViewById(R.id.ll_like);
        mCommentClickLayout = findViewById(R.id.ll_reply);
        ll_share_dynamic = findViewById(R.id.ll_share_dynamic);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);
        ll_share_dynamic.setOnClickListener(this);
//        setBackgroundColor(Color.TRANSPARENT);
//        buildAnima();
        setAllowDismissWhenTouchOutside(true);
        setPopupGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//        setBlurBackgroundEnable(true);
    }

    private AnimationSet mAnimationSet;

    private void buildAnima() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
        mAlphaAnimation.setDuration(400);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(400);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    protected Animation onCreateShowAnimation() {
        Animation showAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        showAnima.setInterpolator(new DecelerateInterpolator());
        showAnima.setDuration(350);
        return showAnima;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation exitAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        exitAnima.setInterpolator(new DecelerateInterpolator());
        exitAnima.setDuration(350);
        return exitAnima;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dynamic_menu);
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }

    //=============================================================clickEvent
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(v, mLikeText);
//                    mLikeAnimaView.clearAnimation();
//                    mLikeAnimaView.startAnimation(mAnimationSet);
                    dismiss();
                }
                break;
            case R.id.ll_reply:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(v);
                    dismiss();
                }
                break;

            case R.id.ll_share_dynamic:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onShareClick(v);
                    dismiss();
                }
        }
    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, TextView likeText);

        void onCommentClick(View v);

        void onShareClick(View v);
    }
}