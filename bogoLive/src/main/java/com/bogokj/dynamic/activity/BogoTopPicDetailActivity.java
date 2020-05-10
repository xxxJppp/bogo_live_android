package com.bogokj.dynamic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.view.BogoTopPicCircleDynamicListView;
import com.bogokj.dynamic.view.LiveAttentionDynamicListView;
import com.bogokj.dynamic.view.LiveDynamicListBaseView;
import com.bogokj.dynamic.view.LiveMyDynamicListView;
import com.bogokj.dynamic.view.LiveNearByDynamicListView;
import com.bogokj.dynamic.view.LiveNewDynamicListView;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.library.common.SDSelectManager;
import com.bogokj.library.view.select.SDSelectViewManager;
import com.bogokj.live.R;
import com.bogokj.live.appview.main.LiveMainDynamicView;
import com.bogokj.live.view.LiveTabUnderline;
import com.fanwe.lib.viewpager.SDViewPager;
import com.bogokj.library.adapter.SDPagerAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 话题详情
 * @time kn 2019/12/23
 */
public class BogoTopPicDetailActivity extends BaseActivity implements BaseActivity.TitleButtonClickListener {

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<View> arrFragment = new SparseArray<>();
    private BogoDynamicTopicListModel data;

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bogo_top_pic_detail);

        data = getIntent().getParcelableExtra("data");

        mTitle.setMiddleTextTop("#" + data.getName() + "#");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
        setTitleButtonClickListener(this);

        findViewById(R.id.push_dynamic_rl).setOnClickListener(this);

        initView();
    }

    private void initView() {
        initSDViewPager();
    }


    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(0);
        //vpg_content.setLockPull(true);
        List<String> listModel = new ArrayList<>();
        listModel.add("");

        vpg_content.setAdapter(new LivePagerAdapter(listModel, getActivity()));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.push_dynamic_rl:
                clickLlUploadDynamic();
                break;
        }
    }

    private void clickLlUploadDynamic() {
        Intent intent = new Intent(getActivity(), PushDynamicActivity.class);
        getActivity().startActivity(intent);
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


    private class LivePagerAdapter extends SDPagerAdapter<String> {

        public LivePagerAdapter(List<String> listModel, Activity activity) {
            super(listModel, activity);
        }

        @Override
        public View getView(ViewGroup viewGroup, int position) {
            LiveDynamicListBaseView view = null;
            switch (position) {
                case 0:
                    view = new BogoTopPicCircleDynamicListView(getActivity(), data.getT_id());
                    view.requestData(false);
                    break;

                default:
                    break;
            }
            if (view != null) {
                arrFragment.put(position, view);
            }
            return view;
        }

    }


}
