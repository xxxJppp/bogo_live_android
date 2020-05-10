package com.bogokj.xianrou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.xianrou.activity.ShortVideoPlayerTouchActivity;
import com.bogokj.xianrou.model.QKSmallVideoListModel;

import java.util.ArrayList;

/**
 * 小视频列表适配器
 * Created by Administrator on 2017/7/20.
 */

public class QKTabSmallVideoAdapter extends SDSimpleRecyclerAdapter<QKSmallVideoListModel> {

    private ArrayList<QKSmallVideoListModel> list;
    private int page = 1;
    private int type = TYPE_LIST;

    public static final int TYPE_LIST = 0;
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    private String otherUserId;

    private boolean isshowjuli = false;

    public boolean isIsshowjuli() {
        return isshowjuli;
    }

    public void setIsshowjuli(boolean isshowjuli) {
        this.isshowjuli = isshowjuli;
    }

    public QKTabSmallVideoAdapter(ArrayList<QKSmallVideoListModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.qk_item_tab_small_video;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.otherUserId = userId;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<QKSmallVideoListModel> holder, final int position, final QKSmallVideoListModel model) {
        CardView fl_root = holder.get(R.id.fl_root);//根布局
        ImageView iv_video_cover = holder.get(R.id.iv_video_cover);//小视频图片
        ImageView iv_avatar = holder.get(R.id.iv_avatar);//用户头像
        TextView tv_name = holder.get(R.id.tv_name);//小视频名字
        TextView tv_play_count = holder.get(R.id.tv_play_count);//观看小视频人数
        LinearLayout lljuli = holder.get(R.id.ll_juli);//距离布局
        TextView tv_juli = holder.get(R.id.tv_juli);//距离


        GlideUtil.load(model.getPhoto_image()).into(iv_video_cover);
        GlideUtil.loadHeadImage(model.getHead_image()).into(iv_avatar);
        tv_name.setText(model.getContent());
        tv_play_count.setText(model.getVideo_count());

        if (isshowjuli && !TextUtils.isEmpty(model.getJuli())) {
            lljuli.setVisibility(View.VISIBLE);
            int distance = (int) (Double.parseDouble(model.getJuli()) * 1000);
            if (distance < 1000) {
                tv_juli.setText(distance + "m");
            } else {
                tv_juli.setText(distance / 1000 + "km");
            }
        } else {
            lljuli.setVisibility(View.GONE);
        }

        fl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                XRActivityLauncher.launchUserDynamicDetailVideo(getActivity(), model.getWeibo_id());
                list = (ArrayList<QKSmallVideoListModel>) getData();
                Intent intent = new Intent(getActivity(), ShortVideoPlayerTouchActivity.class);
                intent.putExtra(ShortVideoPlayerTouchActivity.VIDEO_LIST, list);
                intent.putExtra(ShortVideoPlayerTouchActivity.VIDEO_POS, position);
                intent.putExtra(ShortVideoPlayerTouchActivity.VIDEO_LIST_PAGE, page);
                intent.putExtra(ShortVideoPlayerTouchActivity.VIDEO_TYPE, type);
                intent.putExtra(ShortVideoPlayerTouchActivity.VIDEO_OTHER_USER_ID, otherUserId);
                getActivity().startActivity(intent);
            }
        });
//        civ_head_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                QKActivitylauncher.launchUserHome(getActivity(), model.getUser_id());
//            }
//        });

    }


}
