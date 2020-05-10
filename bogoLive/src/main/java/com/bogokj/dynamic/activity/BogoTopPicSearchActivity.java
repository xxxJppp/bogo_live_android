package com.bogokj.dynamic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.dynamic.adapter.BogoSearchHistoryAdaper;
import com.bogokj.dynamic.adapter.BogoSearchResultAdaper;
import com.bogokj.dynamic.modle.BogoDynamicTopicListApi;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoPeopleNearByModel;
import com.bogokj.dynamic.modle.BogoSearchModel;
import com.bogokj.dynamic.view.DiscoverMainSearchWidget;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 话题搜索页面
 * @time kn 2019/12/18
 */
public class BogoTopPicSearchActivity extends BaseActivity implements DiscoverMainSearchWidget.CustomSearchListener {

    @ViewInject(R.id.rv_main_search_result_list)
    private RecyclerView searchResultRv;

    @ViewInject(R.id.rv_main_search_history_list)
    private RecyclerView searchHistoryRv;

    @ViewInject(R.id.emptying_search_history)
    private ImageView cleanHistoryIv;

    @ViewInject(R.id.search_view)
    DiscoverMainSearchWidget searchView;

    @ViewInject(R.id.search_history_rl)
    private RelativeLayout searchHistoryRl;

    @ViewInject(R.id.search_top_pic_left_back_iv)
    private ImageView leftBackIv;

    @ViewInject(R.id.search_top_pic_right_back_iv)
    private TextView rightStopSearchTv;

    private List<BogoSearchModel.ListBean> searchHistoryList = new ArrayList<>();
    private List<BogoDynamicTopicListModel> searchResultList = new ArrayList<>();
    private BogoSearchHistoryAdaper bogoSearchHistoryAdaper;
    private BogoSearchResultAdaper searchResultAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bogo_top_pic_search);

        initView();

        requestHistoryData();
    }

    private void initView() {
        initAdaper();
        setOnClick();
    }

    private void setOnClick() {
        cleanHistoryIv.setOnClickListener(this);
        leftBackIv.setOnClickListener(this);
        rightStopSearchTv.setOnClickListener(this);

        searchView.setFocusable(false);
        searchView.setCustomSearchListener(this);

    }

    private void initAdaper() {
        searchResultRv.setLayoutManager(new LinearLayoutManager(this));
        searchHistoryRv.setLayoutManager(new LinearLayoutManager(this));

        //搜索历史
        bogoSearchHistoryAdaper = new BogoSearchHistoryAdaper(this, searchHistoryList);
        searchHistoryRv.setAdapter(bogoSearchHistoryAdaper);
        bogoSearchHistoryAdaper.setOnItemClickListener((adapter, view, position) -> {
            searchView.setKeyInput(searchHistoryList.get(position).getTheme());
            toSearch(searchHistoryList.get(position).getTheme());
        });

        //搜索结果
        searchResultAdaper = new BogoSearchResultAdaper(this, searchResultList);
        searchResultRv.setAdapter(searchResultAdaper);

        //搜索结果页面
        searchResultAdaper.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, BogoTopPicDetailActivity.class);
            intent.putExtra("data", searchResultList.get(position));
            startActivity(intent);
        });
    }

    /**
     * 搜索历史
     */
    private void requestHistoryData() {
        CommonInterface.requestTopPicHistory(new AppRequestCallback<BogoSearchModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    searchHistoryList.clear();
                    searchHistoryList.addAll(actModel.getList());
                    bogoSearchHistoryAdaper.notifyDataSetChanged();
                    if (searchHistoryList.size() > 0) {
                        cleanHistoryIv.setVisibility(View.VISIBLE);
                    } else {
                        cleanHistoryIv.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.search_top_pic_left_back_iv:
                finish();
                break;

            case R.id.search_top_pic_right_back_iv:
                stopSearch();
                break;

            case R.id.emptying_search_history:
                CommonInterface.deleteTopPicHistory(new AppRequestCallback<BogoSearchModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.isOk()) {
                            searchHistoryList.clear();
                            bogoSearchHistoryAdaper.notifyDataSetChanged();
                            cleanHistoryIv.setVisibility(View.GONE);

                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onSearch(String key) {
        Log.e("Search", key);
        toSearch(key);
    }

    private void toSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            stopSearch();
        } else {
            starSearch();
            searchResultAdaper.setKeyWord(key);
            requestData(key);
        }
    }

    @Override
    public void onDelete() {
        Log.e("Search", "delete");
        stopSearch();
    }

    @Override
    public void onInputChange(String key) {
        Log.e("Search", key);
//        toSearch(key);
    }


    private void requestData(String keyWord) {
        if (TextUtils.isEmpty(keyWord)) return;
        CommonInterface.searchTopicList(keyWord, new AppRequestCallback<BogoDynamicTopicListApi>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    searchResultList.clear();
                    searchResultList.addAll(actModel.getData());
                    searchResultAdaper.notifyDataSetChanged();

                    Log.e("BogoTopPicSearch", keyWord + "==" + actModel.getData());
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    /**
     * 开始停止搜索 布局变动
     */
    private void starSearch() {
        if (leftBackIv.getVisibility() == View.GONE) return;
        leftBackIv.setVisibility(View.GONE);
        searchHistoryRl.setVisibility(View.GONE);
        searchResultRv.setVisibility(View.VISIBLE);
        rightStopSearchTv.setVisibility(View.VISIBLE);

    }

    private void stopSearch() {
        if (leftBackIv.getVisibility() == View.VISIBLE) return;
        leftBackIv.setVisibility(View.VISIBLE);
        searchHistoryRl.setVisibility(View.VISIBLE);
        searchResultRv.setVisibility(View.GONE);
        rightStopSearchTv.setVisibility(View.INVISIBLE);

        searchResultList.clear();
        searchView.setKeyInput("");
    }

    @Override
    public void onBackPressed() {
        if (leftBackIv.getVisibility() == View.GONE) {
            stopSearch();
        } else {
            super.onBackPressed();
        }

    }
}
