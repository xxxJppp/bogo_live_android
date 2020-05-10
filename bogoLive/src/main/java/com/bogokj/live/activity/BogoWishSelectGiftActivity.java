package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.title.SDTitleItem;
import com.bogokj.live.R;
import com.bogokj.live.adapter.BogoWishSelectGiftAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_propActModel;
import com.bogokj.live.model.BogoGetWishListGiftListApiModel;
import com.bogokj.live.model.LiveGiftModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @dw 心愿单礼物选择列表
 */
public class BogoWishSelectGiftActivity extends BaseTitleActivity {
    public static final int SELECT_GIFT_RESULT_CODE = 10;
    public static final String SELECT_GIFT_MODEL = "SELECT_GIFT_MODEL";

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private List<LiveGiftModel> list = new ArrayList<>();
    private BogoWishSelectGiftAdapter bogoWishSelectGiftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_gift_wish_list);
        init();
    }


    private void init() {
        initTitle();
        bogoWishSelectGiftAdapter = new BogoWishSelectGiftAdapter(list);
        rv_content_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_content_list.setAdapter(bogoWishSelectGiftAdapter);
        bogoWishSelectGiftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(SELECT_GIFT_MODEL, list.get(position));

                setResult(SELECT_GIFT_RESULT_CODE, intent);
                finish();
            }
        });

        requestGetGiftList();
    }

    private void requestGetGiftList() {
        CommonInterface.requestGetWishGiftList(new AppRequestCallback<BogoGetWishListGiftListApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    list.clear();
                    list.addAll(actModel.getList());
                    bogoWishSelectGiftAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("选择礼物");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
    }

}
