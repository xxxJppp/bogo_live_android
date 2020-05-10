package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.live.R;
import com.bogokj.live.adapter.BogoRecommendEmceeAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.BogoFollowRecommendModel;
import com.bogokj.live.model.BogoFollowRecommendModelApi;
import com.bogokj.live.model.BogoWishLisDetailtModel;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvfu
 * @description: 推荐主播
 * @time kn 2019/12/16
 */
public class BogoRecommendEmceeActivity extends BaseActivity {

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;


    /**
     * 推荐数据列表
     */
    private List<BogoFollowRecommendModel> mRecommendList = new ArrayList<>();
    private BogoRecommendEmceeAdapter bogoRecommendEmceeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recommend_emcee);
        init();
        setOnClick();

        //                            InitBusiness.startMainActivity(LiveDoUpdateActivity.this);
    }

    private void setOnClick() {
        find(R.id.recommend_liveer_jump_tv).setOnClickListener(this);
        find(R.id.recommend_liveer_enter_tv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.recommend_liveer_jump_tv:
                toMain();
            case R.id.recommend_liveer_enter_tv:
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mRecommendList.size(); i++) {
                    if (mRecommendList.get(i).isIs_select()) {
                        stringBuilder.append(mRecommendList.get(i).getId()).append(",");
                    }
                }

                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    CommonInterface.followAllUser(stringBuilder.toString(), new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (actModel.isOk()) {
                                toMain();
                            }
                        }

                        @Override
                        protected void onError(SDResponse resp) {
                            super.onError(resp);
                        }
                    });

                } else {
                    toMain();
                }


                break;
        }
    }

    private void toMain() {
        Intent intent = new Intent(BogoRecommendEmceeActivity.this, LiveMainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void init() {
        rv_content_list.setLayoutManager(new GridLayoutManager(this, 3));
        bogoRecommendEmceeAdapter = new BogoRecommendEmceeAdapter(mRecommendList);
        rv_content_list.setAdapter(bogoRecommendEmceeAdapter);
        requestGetRecommendList();

        //adaper点击事件
        bogoRecommendEmceeAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < mRecommendList.size(); i++) {
                if (position == i && mRecommendList.get(i).isIs_select()) {
                    mRecommendList.get(i).setIs_select(false);
                } else if (position == i && !mRecommendList.get(i).isIs_select()) {
                    mRecommendList.get(i).setIs_select(true);
                }
            }
            bogoRecommendEmceeAdapter.notifyDataSetChanged();
        });
    }

    private void requestGetRecommendList() {

        CommonInterface.requestGetFollowRecommendList(new AppRequestCallback<BogoFollowRecommendModelApi>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mRecommendList.clear();
                    mRecommendList.addAll(actModel.getData().getList());
                    bogoRecommendEmceeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }
}
