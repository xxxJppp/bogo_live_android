package cn.tillusory.tiui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.List;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiMakeup;
import cn.tillusory.tiui.R;
import cn.tillusory.tiui.adapter.TiBlusherAdapter;
import cn.tillusory.tiui.adapter.TiBrowPencilAdapter;
import cn.tillusory.tiui.adapter.TiEyeShadowAdapter;
import cn.tillusory.tiui.adapter.TiLipGlossAdapter;
import cn.tillusory.tiui.model.RxBusAction;

/**
 * Created by Anko on 2019-09-06.
 * Copyright (c) 2019 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiMakeupView extends LinearLayout {

    private TiSDKManager tiSDKManager;

    private LinearLayout tiMakeupBackLL;
    private TextView tiMakeupTV;
    private RecyclerView tiMakeupRV;
    private TiLipGlossAdapter lipGlossAdapter;
    private TiEyeShadowAdapter eyeShadowAdapter;
    private TiBrowPencilAdapter browPencilAdapter;
    private TiBlusherAdapter blusherAdapter;
    private List<TiMakeup> lipGlossList = new ArrayList<>();
    private List<TiMakeup> eyeShadowList = new ArrayList<>();
    private List<TiMakeup> browPencilList = new ArrayList<>();
    private List<TiMakeup> blusherList = new ArrayList<>();

    public TiMakeupView(Context context) {
        super(context);
    }

    public TiMakeupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiMakeupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TiMakeupView init(TiSDKManager tiSDKManager) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_makeup, this);

        tiMakeupBackLL = findViewById(R.id.tiMakeupBackLL);
        tiMakeupTV = findViewById(R.id.tiMakeupTV);
        tiMakeupRV = findViewById(R.id.tiMakeupRV);
    }


    private void initData() {
        tiMakeupBackLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().post(RxBusAction.ACTION_MAKEUP_BACK);
            }
        });

        lipGlossList.clear();
        lipGlossList.add(TiMakeup.NO_MAKEUP);
        lipGlossList.addAll(TiMakeup.getAllLipGloss(getContext()));
        lipGlossAdapter = new TiLipGlossAdapter(lipGlossList, tiSDKManager);

        eyeShadowList.clear();
        eyeShadowList.add(TiMakeup.NO_MAKEUP);
        eyeShadowList.addAll(TiMakeup.getAllEyeShadow(getContext()));
        eyeShadowAdapter = new TiEyeShadowAdapter(eyeShadowList, tiSDKManager);

        browPencilList.clear();
        browPencilList.add(TiMakeup.NO_MAKEUP);
        browPencilList.addAll(TiMakeup.getAllBrowPencil(getContext()));
        browPencilAdapter = new TiBrowPencilAdapter(browPencilList, tiSDKManager);

        blusherList.clear();
        blusherList.add(TiMakeup.NO_MAKEUP);
        blusherList.addAll(TiMakeup.getAllBlusher(getContext()));
        blusherAdapter = new TiBlusherAdapter(blusherList, tiSDKManager);

        tiMakeupRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void selectMakeup(String action) {
        switch (action) {
            case RxBusAction.ACTION_MAKEUP_NO:
                lipGlossAdapter.setSelectedPosition(0);
                tiSDKManager.setLipGloss(TiMakeup.NO_MAKEUP.getName());
                eyeShadowAdapter.setSelectedPosition(0);
                tiSDKManager.setEyeShadow(TiMakeup.NO_MAKEUP.getName());
                browPencilAdapter.setSelectedPosition(0);
                tiSDKManager.setBrowPencil(TiMakeup.NO_MAKEUP.getName());
                blusherAdapter.setSelectedPosition(0);
                tiSDKManager.setBlusher(TiMakeup.NO_MAKEUP.getName());
                break;
            case RxBusAction.ACTION_LIP_GLOSS:
                tiMakeupTV.setText(R.string.makeup_lip_gloss);
                tiMakeupRV.setAdapter(lipGlossAdapter);
                break;
            case RxBusAction.ACTION_EYE_SHADOW:
                tiMakeupTV.setText(R.string.makeup_eye_shadow);
                tiMakeupRV.setAdapter(eyeShadowAdapter);
                break;
            case RxBusAction.ACTION_BROW_PENCIL:
                tiMakeupTV.setText(R.string.makeup_brow_pencil);
                tiMakeupRV.setAdapter(browPencilAdapter);
                break;
            case RxBusAction.ACTION_BLUSHER:
                tiMakeupTV.setText(R.string.makeup_blusher);
                tiMakeupRV.setAdapter(blusherAdapter);
                break;
        }
    }
}
