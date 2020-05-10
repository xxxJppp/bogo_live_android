package com.bogokj.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.dao.ToJoinLiveData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bogokj.hybrid.app.App;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.appview.ItemLiveTabNewSingle;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.LiveRoomModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.xianrou.widget.varunest.sparkbutton.heplers.Utils;

import java.util.List;

public class LiveMainTabRecommendAdapter extends BaseQuickAdapter<LiveRoomModel, BaseViewHolder> {
    //    private int mVerticalSpace = 10;  // 分割线宽度
    private Activity context;

    private int type;
    private int page;
    private int sex;
    private String city;

    public LiveMainTabRecommendAdapter(@Nullable List<LiveRoomModel> data, Activity context) {
        super(R.layout.item_live_tab_hot, data);
//        mVerticalSpace = Utils.dpToPx(App.getApplication(), 4);
        this.context = context;
    }


    public void setPageAndType(int type, int page, int sex, String city) {
        this.type = type;
        this.page = page;
        this.sex = sex;
        this.city = city;
    }

    @Override
    protected void convert(BaseViewHolder helper, final LiveRoomModel model) {

        SDViewBinder.setTextViewVisibleOrGone((TextView) helper.getView(R.id.tv_live_state), model.getLive_state());

        SDViewBinder.setTextViewVisibleOrGone((TextView) helper.getView(R.id.tv_live_fee), model.getLivePayFee());

        SDViewBinder.setTextView((TextView) helper.getView(R.id.tv_nickname), model.getTitle());
        SDViewBinder.setTextView((TextView) helper.getView(R.id.tv_city), model.getCity());
        SDViewBinder.setTextView((TextView) helper.getView(R.id.tv_watch_number), model.getWatch_number());
        GlideUtil.load(model.getLive_image()).into((ImageView) helper.getView(R.id.iv_room_image));
        if (!TextUtils.isEmpty(model.getLabels())) {
            GlideUtil.load(model.getLabels()).into((ImageView) helper.getView(R.id.item_iv_label));
        }

        if (model.getIs_video_pk() == 1) {
            SDViewUtil.setVisible(helper.getView(R.id.im_is_pk));
        } else {
            SDViewUtil.setGone(helper.getView(R.id.im_is_pk));
        }

        if (model.getIs_gaming() == 1) {
            SDViewUtil.setVisible(helper.getView(R.id.tv_game_state));
            SDViewBinder.setTextView((TextView) helper.getView(R.id.tv_game_state), model.getGame_name());
        } else {
            SDViewUtil.setGone((TextView) helper.getView(R.id.tv_game_state));
        }

        helper.getView(R.id.iv_head).setOnClickListener(v -> {
            Intent intent = new Intent(context, LiveUserHomeActivity.class);
            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUser_id());
            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
            context.startActivity(intent);
        });
        helper.getView(R.id.iv_room_image).setOnClickListener(v -> {
            if (type == 2) {
                ToJoinLiveData toJoinLiveData = new ToJoinLiveData(type, page, helper.getAdapterPosition(), sex, city);
                AppRuntimeWorker.joinRoom(toJoinLiveData, mData, model, context);
            } else {
                ToJoinLiveData toJoinLiveData = new ToJoinLiveData(type, page, helper.getAdapterPosition() - 1, sex, city);
                AppRuntimeWorker.joinRoom(toJoinLiveData, mData, model, context);
            }

        });
    }

}
