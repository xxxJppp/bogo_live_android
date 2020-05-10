package com.bogokj.dynamic.activity;

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
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/*
 * 动态话题选择
 * */
public class BogoDynamicTopicActivity extends BaseActivity {

    public static final String TOPIC_MODEL = "TOPICE_MODEL";
    public static final int TOPIC_CODE = 0x99;

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private List<BogoDynamicTopicListModel> list = new ArrayList<>();
    private BogoDynamicTopicAdapter bogoDynamicTopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dynamic_topic);
        init();
    }

    private void init() {
        initView();
        initData();

    }

    private void initData() {
        requestGetTopicData();
    }

    private void initView() {
        findViewById(R.id.tv_close).setOnClickListener(this);

        rv_content_list.setLayoutManager(new LinearLayoutManager(this));
        bogoDynamicTopicAdapter = new BogoDynamicTopicAdapter(list);
        rv_content_list.setAdapter(bogoDynamicTopicAdapter);

        bogoDynamicTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent();
                intent.putExtra(TOPIC_MODEL, list.get(position));

                setResult(TOPIC_CODE, intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                finish();
                break;
            default:
                break;

        }
    }


    private void requestGetTopicData() {
        CommonInterface.requestGetTopicList(1, 1, "", new AppRequestCallback<BogoDynamicTopicListApi>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    list.clear();
                    list.addAll(actModel.getData());
                    bogoDynamicTopicAdapter.notifyDataSetChanged();
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

}
