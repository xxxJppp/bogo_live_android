package com.bogokj.dynamic.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bogokj.dynamic.adapter.BogoSystemMsgAdaper;
import com.bogokj.dynamic.adapter.BogoTopPicAdaper;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoSystemMsgModel;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveWebViewActivity;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_DynamicListModel;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description:
 * @time 2019/12/27
 */
public class BogoSystemMsgActivity extends BaseActivity implements BaseActivity.TitleButtonClickListener {


    @ViewInject(R.id.bogo_top_pic_rv)
    protected RecyclerView topPicRv;

    @ViewInject(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    private BogoSystemMsgAdaper bogoSyatemMsgAdaper;

    private List<BogoSystemMsgModel.ListBean> systemMsgList = new ArrayList<>();
    private int page = 1;

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bogo_system_msg);

        initView();
        initAdaper();
        requestData();

    }

    private void initAdaper() {
        topPicRv.setLayoutManager(new LinearLayoutManager(this));

        bogoSyatemMsgAdaper = new BogoSystemMsgAdaper(this, systemMsgList);
        topPicRv.setAdapter(bogoSyatemMsgAdaper);

        sw_refresh.setOnRefreshListener(() -> {
            page = 1;
            requestData();
        });

        bogoSyatemMsgAdaper.setOnLoadMoreListener(() -> {
            page++;
            requestData();
        }, topPicRv);

        bogoSyatemMsgAdaper.setEmptyView(R.layout.view_state_empty_content);

        bogoSyatemMsgAdaper.setOnItemClickListener((adapter, view, position) -> {
            if ("2".equals(systemMsgList.get(position).getType())) {

                Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
                intent.putExtra(LiveWebViewActivity.EXTRA_URL, systemMsgList.get(position).getLink_url());
                getActivity().startActivity(intent);
            }
        });

    }

    private void requestData() {
        CommonInterface.getSystemMsgData(page, new AppRequestCallback<BogoSystemMsgModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    sw_refresh.setRefreshing(false);

                    if (page == 1) {
                        systemMsgList.clear();
                    }

                    if (actModel.getList() != null && actModel.getList().size() > 0) {
                        systemMsgList.addAll(actModel.getList());
                        bogoSyatemMsgAdaper.notifyDataSetChanged();
                        bogoSyatemMsgAdaper.loadMoreComplete();
                    } else {
                        bogoSyatemMsgAdaper.loadMoreEnd();
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                dismissProgressDialog();
                super.onFinish(resp);
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void initView() {
        mTitle.setMiddleTextTop("系统消息");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
        setTitleButtonClickListener(this);


    }

    @Override
    public void onLeftTitleButtonClickListener() {
        finish();
    }

    @Override
    public void onRightTitleButtonClickListener() {

    }

    @Override
    public void onMiddleTitleButtonClickListener() {

    }
}
