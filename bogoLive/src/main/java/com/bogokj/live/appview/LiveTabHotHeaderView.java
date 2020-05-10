package com.bogokj.live.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.live.R;
import com.bogokj.live.model.Index_indexActModel;
import com.bogokj.live.model.LiveBannerModel;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页热门列表HeaderView
 */
public class LiveTabHotHeaderView extends BaseAppView {

    private Banner banner;
    private List<LiveBannerModel> listModel = new ArrayList<>();

    public LiveTabHotHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabHotHeaderView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setContentView(R.layout.view_live_tab_hot_header);

        initSlidingView();
    }


    /**
     * 初始化轮播view
     */
    private void initSlidingView() {
        banner = (Banner) findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new SlideGlideImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = listModel.get(position).parseType(getActivity());
                if (intent != null) {
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置轮播图item点击回调
     *
     * @param callback
     */
    public void setBannerItemClickCallback(SDItemClickCallback<LiveBannerModel> callback) {

    }

    /**
     * 设置数据
     *
     * @param actModel
     */
    public void setData(Index_indexActModel actModel) {
        if (actModel == null) {
            return;
        }
        bindDataBanner(actModel.getBanner());
    }

    /**
     * 设置轮播图列表数据
     *
     * @param listModel
     */
    private void bindDataBanner(List<LiveBannerModel> listModel) {
        this.listModel.clear();
        this.listModel.addAll(listModel);
        //设置图片集合
        banner.setImages(this.listModel);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
