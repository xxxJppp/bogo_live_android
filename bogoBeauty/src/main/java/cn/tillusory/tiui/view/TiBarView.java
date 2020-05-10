package cn.tillusory.tiui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiFilterEnum;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.model.RxBusAction;

/**
 * Created by Anko on 2019-09-06.
 * Copyright (c) 2019 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiBarView extends LinearLayout {

    private TiSDKManager tiSDKManager;

    private TextView tiBubbleTV;
    private TextView tiNumberTV;
    private SeekBar tiSeekBar;
    private View tiProgressV;
    private View tiMiddleV;
    private ImageView tiRenderEnableIV;

    private String currentAction = RxBusAction.ACTION_SKIN_WHITENING;
    private String currentBeautyAction = RxBusAction.ACTION_SKIN_WHITENING;
    private String currentFaceTrimAction = RxBusAction.ACTION_EYE_MAGNIFYING;

    private TiFilterEnum tiFilterEnum = TiFilterEnum.NO_FILTER;

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (currentAction) {

                case RxBusAction.ACTION_SKIN_WHITENING:
                    styleNormal(progress);
                    tiSDKManager.setSkinWhitening(progress);
                    break;

                case RxBusAction.ACTION_SKIN_BLEMISH_REMOVAL:
                    styleNormal(progress);
                    tiSDKManager.setSkinBlemishRemoval(progress);
                    break;

                case RxBusAction.ACTION_SKIN_TENDERNESS:
                    styleNormal(progress);
                    tiSDKManager.setSkinTenderness(progress);
                    break;

                case RxBusAction.ACTION_SKIN_SATURATION:
                    styleTransform(progress);
                    tiSDKManager.setSkinSaturation(progress - 50);
                    break;

                case RxBusAction.ACTION_SKIN_BRIGHTNESS:
                    styleTransform(progress);
                    tiSDKManager.setSkinBrightness(progress - 50);
                    break;

                case RxBusAction.ACTION_EYE_MAGNIFYING:
                    styleNormal(progress);
                    tiSDKManager.setEyeMagnifying(progress);
                    break;

                case RxBusAction.ACTION_CHIN_SLIMMING:
                    styleNormal(progress);
                    tiSDKManager.setChinSlimming(progress);
                    break;

                case RxBusAction.ACTION_JAW_TRANSFORMING:
                    styleTransform(progress);
                    tiSDKManager.setJawTransforming(progress - 50);
                    break;

                case RxBusAction.ACTION_FOREHEAD_TRANSFORMING:
                    styleTransform(progress);
                    tiSDKManager.setForeheadTransforming(progress - 50);
                    break;

                case RxBusAction.ACTION_MOUTH_TRANSFORMING:
                    styleTransform(progress);
                    tiSDKManager.setMouthTransforming(progress - 50);
                    break;

                case RxBusAction.ACTION_NOSE_MINIFYING:
                    styleTransform(progress);
                    tiSDKManager.setNoseMinifying(progress - 50);
                    break;

                case RxBusAction.ACTION_TEETH_WHITENING:
                    styleNormal(progress);
                    tiSDKManager.setTeethWhitening(progress);
                    break;

                case RxBusAction.ACTION_FACE_NARROWING:
                    styleNormal(progress);
                    tiSDKManager.setFaceNarrowing(progress);
                    break;

                case RxBusAction.ACTION_FILTER:
                    styleNormal(progress);
                    tiSDKManager.setFilterEnum(tiFilterEnum, progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            tiBubbleTV.setVisibility(VISIBLE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            tiBubbleTV.setVisibility(GONE);
        }
    };

    public TiBarView(Context context) {
        super(context);
    }

    public TiBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TiBarView init(TiSDKManager tiSDKManager) {
        this.tiSDKManager = tiSDKManager;

        RxBus.get().register(this);

        initView();

        initData();

        return this;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        RxBus.get().unregister(this);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_bar, this);

        tiBubbleTV = findViewById(R.id.tiBubbleTV);
        tiNumberTV = findViewById(R.id.tiNumberTV);
        tiSeekBar = findViewById(R.id.tiSeekBar);
        tiProgressV = findViewById(R.id.tiProgressV);
        tiMiddleV = findViewById(R.id.tiMiddleV);
        tiRenderEnableIV = findViewById(R.id.tiRenderEnableIV);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initData() {

        tiSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        tiSeekBar.setProgress(0);

        tiRenderEnableIV.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tiSDKManager.renderEnable(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        tiSDKManager.renderEnable(true);
                        return true;
                }
                return false;
            }
        });

    }

    private void styleNormal(int progress) {

        tiSeekBar.setSecondaryProgress(0);
        tiMiddleV.setVisibility(GONE);

        CharSequence percent = new StringBuilder().append(progress).append("%");

        tiNumberTV.setText(percent);

        float width = tiSeekBar.getWidth() - (getContext().getResources().getDisplayMetrics().density * 32 + 0.5f);

        tiBubbleTV.setText(percent);
        tiBubbleTV.setX(width / 100 * progress + (getContext().getResources().getDisplayMetrics().density * 1f + 0.5f));

        tiProgressV.setVisibility(GONE);
    }

    private void styleTransform(int progress) {

        tiSeekBar.setSecondaryProgress(50);
        tiMiddleV.setVisibility((progress > 48 && progress < 52) ? GONE : VISIBLE);

        CharSequence percent = new StringBuilder().append(progress - 50).append("%");

        tiNumberTV.setText(percent);

        float width = tiSeekBar.getWidth() - (getContext().getResources().getDisplayMetrics().density * 32 + 0.5f);

        tiBubbleTV.setText(percent);
        tiBubbleTV.setX(width / 100 * progress + (getContext().getResources().getDisplayMetrics().density * 1f + 0.5f));

        tiProgressV.setVisibility(VISIBLE);
        ViewGroup.LayoutParams layoutParams = tiProgressV.getLayoutParams();
        layoutParams.width = (int) (progress < 51 ? width / 100 * progress - (getContext().getResources().getDisplayMetrics().density * 5f + 0.5f) : width / 2);
        tiProgressV.setLayoutParams(layoutParams);

    }

    public void selectBeauty() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(tiSDKManager.isBeautyEnable());

        busCurrentAction(currentBeautyAction);
    }

    public void selectFaceTrim() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(tiSDKManager.isFaceTrimEnable());

        busCurrentAction(currentFaceTrimAction);
    }

    public void selectFilter() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);

        tiSeekBar.setEnabled(true);

        busCurrentFilter(tiFilterEnum);
    }

    public void hideSeekBar() {
        tiNumberTV.setVisibility(INVISIBLE);
        tiSeekBar.setVisibility(INVISIBLE);
        tiProgressV.setVisibility(GONE);
        tiMiddleV.setVisibility(GONE);
    }

    public void showNormalSeekBar() {
        tiNumberTV.setVisibility(VISIBLE);
        tiSeekBar.setVisibility(VISIBLE);
        tiProgressV.setVisibility(GONE);
        tiMiddleV.setVisibility(GONE);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusAction.ACTION_FILTER))
    public void busCurrentFilter(TiFilterEnum filterEnum) {

        currentAction = RxBusAction.ACTION_FILTER;

        if (filterEnum == TiFilterEnum.NO_FILTER) {
            hideSeekBar();
        } else {
            showNormalSeekBar();
            int progress = tiSDKManager.getFilterProgress(filterEnum);
            this.tiFilterEnum = filterEnum;
            tiSeekBar.setProgress(progress);
            styleNormal(progress);
            tiSDKManager.setFilterEnum(tiFilterEnum, progress);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void busCurrentAction(String currentAction) {
        this.currentAction = currentAction;
        switch (currentAction) {

            case RxBusAction.ACTION_SKIN_WHITENING:
                tiSeekBar.setProgress(tiSDKManager.getSkinWhitening());
                styleNormal(tiSDKManager.getSkinWhitening());
                currentBeautyAction = RxBusAction.ACTION_SKIN_WHITENING;
                break;

            case RxBusAction.ACTION_SKIN_BLEMISH_REMOVAL:
                tiSeekBar.setProgress(tiSDKManager.getSkinBlemishRemoval());
                styleNormal(tiSDKManager.getSkinBlemishRemoval());
                currentBeautyAction = RxBusAction.ACTION_SKIN_BLEMISH_REMOVAL;
                break;

            case RxBusAction.ACTION_SKIN_TENDERNESS:
                tiSeekBar.setProgress(tiSDKManager.getSkinTenderness());
                styleNormal(tiSDKManager.getSkinTenderness());
                currentBeautyAction = RxBusAction.ACTION_SKIN_TENDERNESS;
                break;

            case RxBusAction.ACTION_SKIN_SATURATION:
                tiSeekBar.setProgress(tiSDKManager.getSkinSaturation() + 50);
                styleTransform(tiSDKManager.getSkinSaturation() + 50);
                currentBeautyAction = RxBusAction.ACTION_SKIN_SATURATION;
                break;

            case RxBusAction.ACTION_SKIN_BRIGHTNESS:
                tiSeekBar.setProgress(tiSDKManager.getSkinBrightness() + 50);
                styleTransform(tiSDKManager.getSkinBrightness() + 50);
                currentBeautyAction = RxBusAction.ACTION_SKIN_BRIGHTNESS;
                break;

            case RxBusAction.ACTION_EYE_MAGNIFYING:
                tiSeekBar.setProgress(tiSDKManager.getEyeMagnifying());
                styleNormal(tiSDKManager.getEyeMagnifying());
                currentFaceTrimAction = RxBusAction.ACTION_EYE_MAGNIFYING;
                break;

            case RxBusAction.ACTION_CHIN_SLIMMING:
                tiSeekBar.setProgress(tiSDKManager.getChinSlimming());
                styleNormal(tiSDKManager.getChinSlimming());
                currentFaceTrimAction = RxBusAction.ACTION_CHIN_SLIMMING;
                break;

            case RxBusAction.ACTION_JAW_TRANSFORMING:
                tiSeekBar.setProgress(tiSDKManager.getJawTransforming() + 50);
                styleTransform(tiSDKManager.getJawTransforming() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_JAW_TRANSFORMING;
                break;

            case RxBusAction.ACTION_FOREHEAD_TRANSFORMING:
                tiSeekBar.setProgress(tiSDKManager.getForeheadTransforming() + 50);
                styleTransform(tiSDKManager.getForeheadTransforming() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_FOREHEAD_TRANSFORMING;
                break;

            case RxBusAction.ACTION_MOUTH_TRANSFORMING:
                tiSeekBar.setProgress(tiSDKManager.getMouthTransforming() + 50);
                styleTransform(tiSDKManager.getMouthTransforming() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_MOUTH_TRANSFORMING;
                break;

            case RxBusAction.ACTION_NOSE_MINIFYING:
                tiSeekBar.setProgress(tiSDKManager.getNoseMinifying() + 50);
                styleTransform(tiSDKManager.getNoseMinifying() + 50);
                currentFaceTrimAction = RxBusAction.ACTION_NOSE_MINIFYING;
                break;

            case RxBusAction.ACTION_TEETH_WHITENING:
                tiSeekBar.setProgress(tiSDKManager.getTeethWhitening());
                styleNormal(tiSDKManager.getTeethWhitening());
                currentFaceTrimAction = RxBusAction.ACTION_TEETH_WHITENING;
                break;

            case RxBusAction.ACTION_FACE_NARROWING:
                tiSeekBar.setProgress(tiSDKManager.getFaceNarrowing());
                styleNormal(tiSDKManager.getFaceNarrowing());
                currentFaceTrimAction = RxBusAction.ACTION_FACE_NARROWING;
                break;
        }
    }
}

