package com.bogokj.xianrou.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bogokj.dynamic.adapter.BogoDynamicTopicAdapter;
import com.bogokj.dynamic.modle.BogoDynamicTopicListApi;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.title.SDTitleItem;
import com.bogokj.library.title.SDTitleSimple;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.xianrou.adapter.BogoVideoClassLabelAdapter;
import com.bogokj.xianrou.model.BogoVideoClassLabelModel;
import com.bogokj.xianrou.model.BogoVideoClassLabelModelApi;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class BogoVideoClassLabelSelectActivity extends BaseActivity implements SDTitleSimple.SDTitleSimpleListener {

    public static final String VIDEO_LABEL = "VIDEO_LABEL";
    public static final int VIDEO_CLASS_CODE = 0x99;

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private List<BogoVideoClassLabelModel> list = new ArrayList<>();
    private BogoVideoClassLabelAdapter bogoVideoClassLabelAdapter;
    private SDTitleSimple mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_video_class_label);
        init();
    }

    private void init() {
        initView();
        initTopBars();
        initData();

    }

    private void initTopBars() {

        mTitle = (SDTitleSimple) findViewById(R.id.title);
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setmListener(this);
        mTitle.setMiddleTextTop("分类");
    }

    private void initData() {
        requestGetVideoClassData();
    }

    private void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_content_list.setLayoutManager(linearLayoutManager);
        bogoVideoClassLabelAdapter = new BogoVideoClassLabelAdapter(list);
        rv_content_list.setAdapter(bogoVideoClassLabelAdapter);

        bogoVideoClassLabelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(VIDEO_LABEL, list.get(position));

                setResult(VIDEO_CLASS_CODE, intent);
                finish();
            }
        });

    }


    private void requestGetVideoClassData() {
        CommonInterface.requestGetVideoClassList(new AppRequestCallback<BogoVideoClassLabelModelApi>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    list.clear();
                    list.addAll(actModel.getData());
                    bogoVideoClassLabelAdapter.notifyDataSetChanged();
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {

    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {

    }
}
