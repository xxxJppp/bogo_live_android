package com.bogokj.live.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.JoinPlayBackData;
import com.bogokj.live.model.LivePlaybackModel;
import com.bogokj.live.model.LiveRoomModel;
import com.bogokj.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class LiveTabFollowAdapter extends SDSimpleRecyclerAdapter<Object> {
    private static final int TOTAL_NEED_FIND_TYPE_COUNT = 2;
    private static SparseArray<Integer> arrTypeFirstPositon = new SparseArray<>();


    public LiveTabFollowAdapter(List<Object> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        int type = getItemViewType(viewType);
        int id = 0;
        switch (type) {
            case 0:
                id = R.layout.item_live_tab_follow_room;
                break;
            case 1:
                id = R.layout.item_live_tab_follow_playback;
                break;
            default:

                break;
        }
        return id;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<Object> holder, int position, Object model) {

    }

    @Override
    public SDRecyclerViewHolder<Object> onCreateVHolder(ViewGroup parent, int viewType) {

        SDRecyclerViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                viewHolder = new ViewHolderLiveRoom(parent, R.layout.item_live_tab_follow_room, getActivity());
                break;
            case 1:
                viewHolder = new ViewHolderPlayback(parent, R.layout.item_live_tab_follow_playback, getActivity());
                break;
            default:

                break;
        }
        return viewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        Object model = getData(position);

        if (model instanceof LiveRoomModel) {
            return 0;
        } else if (model instanceof LivePlaybackModel) {
            return 1;
        }

        return 0;
    }

    @Override
    public void setData(List<Object> list) {
        arrTypeFirstPositon.clear();
        super.setData(list);
        findFirstPosition();
    }

    private void findFirstPosition() {
        int findTypeCount = 0;
        int i = 0;
        for (Object item : getData()) {
            int type = getItemViewType(i);
            boolean needFind = false;
            if (item instanceof LiveRoomModel) {
                needFind = true;
            } else if (item instanceof LivePlaybackModel) {
                needFind = true;
            }

            if (needFind) {
                Integer typePos = arrTypeFirstPositon.get(type);
                if (typePos == null) {
                    arrTypeFirstPositon.put(type, i);
                    findTypeCount++;
                }
            }

            if (TOTAL_NEED_FIND_TYPE_COUNT == findTypeCount) {
                break;
            }
            i++;
        }
    }


    /**
     * 回放
     */
    public static class ViewHolderPlayback extends SDRecyclerViewHolder<LivePlaybackModel> {

        Activity activity;

        LinearLayout ll_content;
        ImageView iv_room;
        ImageView iv_head;
        ImageView iv_head_small;
        TextView tv_nickname;
        TextView tv_create_time;
        TextView tv_watch_number;
        TextView tv_topic;

        public ViewHolderPlayback(ViewGroup parent, int layoutId, Activity activity) {
            super(parent, layoutId);
            this.activity = activity;
        }


        @Override
        public void onBindData(int position, final LivePlaybackModel model) {
            iv_room = find(R.id.iv_room_image);
            ll_content = find(R.id.ll_content);
            iv_head = find(R.id.iv_head);
            iv_head_small = find(R.id.iv_head_small);
            tv_nickname = find(R.id.tv_nickname);
            tv_create_time = find(R.id.tv_create_time);
            tv_watch_number = find(R.id.tv_watch_number);
            tv_topic = find(R.id.tv_topic);
            GlideUtil.loadHeadImage(model.getHead_image()).into(iv_room);
            GlideUtil.loadHeadImage(model.getHead_image()).into(iv_head);
            if (!TextUtils.isEmpty(model.getV_icon())) {
                SDViewUtil.setVisible(iv_head_small);
                GlideUtil.load(model.getV_icon()).into(iv_head_small);
                SDViewUtil.setGone(iv_head_small);
            } else {
                SDViewUtil.setGone(iv_head_small);
            }

            SDViewBinder.setTextView(tv_nickname, model.getNick_name());
            SDViewBinder.setTextView(tv_create_time, model.getBegin_time_format());
            SDViewBinder.setTextView(tv_watch_number, model.getWatch_number_format());
            SDViewBinder.setTextView(tv_topic, model.getTitle());

            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JoinPlayBackData data = new JoinPlayBackData();
                    data.setRoomId(model.getRoom_id());
                    AppRuntimeWorker.joinPlayback(data, activity);
                }
            });

        }
    }


    public static class ViewHolderLiveRoom extends SDRecyclerViewHolder<LiveRoomModel> {

        ImageView iv_head;
        ImageView iv_head_small;
        TextView tv_nickname;
        TextView tv_city;
        TextView tv_watch_number;
        ImageView iv_room_image;
        TextView tv_topic;
        TextView tv_live_state;
        TextView tv_live_fee;
        TextView tv_game_state;
        Activity activity;
        private ImageView iv_label;

        public ViewHolderLiveRoom(ViewGroup parent, int layoutId, Activity activity) {
            super(parent, layoutId);
            this.activity = activity;
        }


        @Override
        public void onBindData(int position, final LiveRoomModel model) {
            iv_head = find(R.id.iv_head);
            iv_head_small = find(R.id.iv_head_small);
            tv_nickname = find(R.id.tv_nickname);
            tv_city = find(R.id.tv_city);
            tv_watch_number = find(R.id.tv_watch_number);
            iv_room_image = find(R.id.iv_room_image);
            tv_topic = find(R.id.tv_topic);
            tv_live_state = find(R.id.tv_live_state);
            tv_live_fee = find(R.id.tv_live_fee);
            tv_game_state = find(R.id.tv_game_state);
            iv_label = find(R.id.item_iv_label);

            GlideUtil.loadHeadImage(model.getHead_image()).into(iv_head);
            SDViewUtil.setGone(iv_head);
            if (!TextUtils.isEmpty(model.getV_icon())) {
                SDViewUtil.setVisible(iv_head_small);
                GlideUtil.load(model.getV_icon()).into(iv_head_small);
            } else {
                SDViewUtil.setGone(iv_head_small);
            }

            if (!TextUtils.isEmpty(model.getLabels())) {
                GlideUtil.load(model.getLabels()).into(iv_label);
            }

            SDViewBinder.setTextViewVisibleOrGone(tv_live_state, model.getLive_state());
            SDViewBinder.setTextViewVisibleOrGone(tv_live_fee, model.getLivePayFee());

            SDViewBinder.setTextView(tv_nickname, model.getNick_name());
            SDViewBinder.setTextView(tv_city, model.getCity());
            SDViewBinder.setTextView(tv_watch_number, model.getWatch_number());
            GlideUtil.load(model.getLive_image()).into(iv_room_image);

            if (model.getCate_id() > 0) {
                SDViewBinder.setTextView(tv_topic, model.getTitle());
                SDViewUtil.setVisible(tv_topic);
            } else {
                SDViewUtil.setGone(tv_topic);
            }
//            SDViewBinder.setTextView(tv_topic, model.getTitle());

            if (model.getIs_gaming() == 1) {
                SDViewUtil.setVisible(tv_game_state);
                SDViewBinder.setTextView(tv_game_state, model.getGame_name());
            } else {
                SDViewUtil.setGone(tv_game_state);
            }

            iv_head.setOnClickListener(v -> {
                Intent intent = new Intent(activity, LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUser_id());
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
                activity.startActivity(intent);
            });
            iv_room_image.setOnClickListener(v -> {
                toLiveListener.ToLiveListener(position);
            });
        }
    }

    static ToLiveListener toLiveListener;

    public void setToLiveListener(ToLiveListener toLiveListener) {
        this.toLiveListener = toLiveListener;
    }

    public interface ToLiveListener {
        void ToLiveListener(int posi);
    }

//AppRuntimeWorker.joinRoom(position, model, activity));
}
