package com.bogokj.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;

import java.text.DecimalFormat;

/**
 * @author kn update
 * @description: pk页面
 * @time 2020/1/4
 */
public class LivePKView extends RelativeLayout {

    private RelativeLayout rl_left_emcee;
    private TextView tv_left_total;
    private TextView tv_right_total;
    private ImageView iv_left_result;
    private ImageView iv_right_result;
    private int leftPro = 0;
    private int rightPro = 0;

    private int screenWidth;
    Animation animation;

    public LivePKView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LivePKView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LivePKView(Context context) {
        super(context);
        init();
    }


    private void init() {

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.pk_result);


        screenWidth = SDViewUtil.getScreenWidth();

        LayoutInflater.from(getContext()).inflate(R.layout.view_live_pk, this, true);
        rl_left_emcee = (RelativeLayout) findViewById(R.id.rl_left_emcee);
        rl_left_emcee.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth / 2, SDViewUtil.dp2px(20)));

        tv_left_total = (TextView) findViewById(R.id.tv_left_total);
        tv_right_total = (TextView) findViewById(R.id.tv_right_total);
        iv_left_result = (ImageView) findViewById(R.id.iv_left_result);
        iv_right_result = (ImageView) findViewById(R.id.iv_right_result);
        iv_left_result.setVisibility(GONE);
        iv_right_result.setVisibility(GONE);


        int leftWidth = (screenWidth / 2);
        rl_left_emcee.setLayoutParams(new RelativeLayout.LayoutParams(leftWidth, SDViewUtil.dp2px(20)));

    }

    public void changeProgress(int leftProP, int rightProP) {
        leftPro += leftProP;
        rightPro += rightProP;

        int total = leftPro + rightPro;
        DecimalFormat df = new DecimalFormat("0.00");
        String ratio = df.format((float) leftPro / total);

        int leftWidth = (int) (screenWidth * Float.parseFloat(ratio));
        rl_left_emcee.setLayoutParams(new RelativeLayout.LayoutParams(leftWidth, SDViewUtil.dp2px(20)));

        tv_left_total.setText("我方：" + leftPro);
        tv_right_total.setText("对方：" + rightPro);

    }

    public void setProgress(int leftProP, int rightProP) {
        leftPro = leftProP;
        rightPro = rightProP;

        int total = leftPro + rightPro;
        DecimalFormat df = new DecimalFormat("0.00");
        String ratio = df.format((float) leftPro / total);
        if (leftProP == 0 && rightProP == 0) {
            ratio = df.format((float) 1 / 2);
        }

        int leftWidth = (int) (screenWidth * Float.parseFloat(ratio));
        rl_left_emcee.setLayoutParams(new RelativeLayout.LayoutParams(leftWidth, SDViewUtil.dp2px(20)));
        tv_left_total.setText("我方：" + leftPro);
        tv_right_total.setText("对方：" + rightPro);

    }


    public void setInit() {
        iv_left_result.setVisibility(GONE);
        iv_right_result.setVisibility(GONE);
    }

    public void setWin(int result) {
        iv_left_result.setVisibility(VISIBLE);
        iv_right_result.setVisibility(VISIBLE);
        if (result > 0) {
            iv_left_result.setImageResource(R.drawable.ic_live_pk_win);
            iv_right_result.setImageResource(R.drawable.ic_live_pk_lose);
        } else {
            if (result == 0) {
                iv_left_result.setImageResource(R.drawable.ic_live_pk_draw);
                iv_right_result.setImageResource(R.drawable.ic_live_pk_draw);
            } else {
                iv_left_result.setImageResource(R.drawable.ic_live_pk_lose);
                iv_right_result.setImageResource(R.drawable.ic_live_pk_win);
            }
        }

    }


    public void setResult(String id, String cid) {
        iv_left_result.setVisibility(VISIBLE);
        iv_right_result.setVisibility(VISIBLE);
        if (id.equals("0")) {
            iv_left_result.setImageResource(R.drawable.ic_live_pk_draw);
            iv_right_result.setImageResource(R.drawable.ic_live_pk_draw);
        } else {
            if (id.equals(cid)) {
                iv_left_result.setImageResource(R.drawable.ic_live_pk_win);
                iv_right_result.setImageResource(R.drawable.ic_live_pk_lose);
            } else {
                iv_left_result.setImageResource(R.drawable.ic_live_pk_lose);
                iv_right_result.setImageResource(R.drawable.ic_live_pk_win);
            }
        }

        iv_left_result.startAnimation(animation);

    }

    public void showPKResult() {
        iv_left_result.setVisibility(VISIBLE);
        iv_right_result.setVisibility(VISIBLE);
        if (leftPro == rightPro) {
            iv_left_result.setImageResource(R.drawable.ic_live_pk_draw);
            iv_right_result.setImageResource(R.drawable.ic_live_pk_draw);
        } else {
            if (leftPro > rightPro) {
                iv_left_result.setImageResource(R.drawable.ic_live_pk_win);
                iv_right_result.setImageResource(R.drawable.ic_live_pk_lose);
            } else {
                iv_left_result.setImageResource(R.drawable.ic_live_pk_lose);
                iv_right_result.setImageResource(R.drawable.ic_live_pk_win);
            }
        }

        iv_left_result.startAnimation(animation);

    }
}
