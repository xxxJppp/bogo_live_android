package com.bogokj.live.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bogokj.dynamic.modle.BogoDynamicTopicListApi;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.adapter.viewholder.BogoWishListAdaper;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.BogoWishLisDetailtModel;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 心愿单列表
 * @time kn 2019/12/18
 */
public class BogoWishListActivity extends BaseActivity implements BaseActivity.TitleButtonClickListener {

    @ViewInject(R.id.wish_list_rv)
    RecyclerView wishListRv;

    private List<BogoWishLisDetailtModel.ListBean> wishList = new ArrayList();
    private BogoWishListAdaper bogoWishListAdaper;

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wish_list);
        initView();

        requestData();
    }

    private void initView() {
        mTitle.setMiddleTextTop("心愿单");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
        setTitleButtonClickListener(this);


        wishListRv.setLayoutManager(new LinearLayoutManager(this));
        bogoWishListAdaper = new BogoWishListAdaper(this, wishList);
        wishListRv.setAdapter(bogoWishListAdaper);
    }


    private void requestData() {
        CommonInterface.getTodayWishList(new AppRequestCallback<BogoWishLisDetailtModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    wishList.clear();
                    wishList.addAll(actModel.getList());
                    bogoWishListAdaper.notifyDataSetChanged();
                }
            }
        });
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
