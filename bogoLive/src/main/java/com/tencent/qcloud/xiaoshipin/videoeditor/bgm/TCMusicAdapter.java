package com.tencent.qcloud.xiaoshipin.videoeditor.bgm;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bogokj.live.R;
import com.tencent.qcloud.xiaoshipin.videoeditor.bgm.utils.BaseRecyclerAdapter;
import com.tencent.qcloud.xiaoshipin.videoeditor.bgm.utils.TCBGMInfo;

import java.util.List;

/**
 * Created by hanszhli on 2017/6/15.
 */

public class TCMusicAdapter extends BaseRecyclerAdapter<TCMusicAdapter.LinearMusicViewHolder> implements View.OnClickListener {
    private static final String TAG = "TCMusicAdapter";
    private Context mContext;
    private List<TCBGMInfo> mBGMList;

    private OnClickSubItemListener mOnClickSubItemListener;

    private SparseArray<LinearMusicViewHolder> mProgressButtonIndexMap = new SparseArray<LinearMusicViewHolder>();

    public void setOnClickSubItemListener(OnClickSubItemListener onClickSubItemListener) {
        mOnClickSubItemListener = onClickSubItemListener;
    }

    public TCMusicAdapter(Context context, List<TCBGMInfo> list) {
        mContext = context;
        mBGMList = list;
    }

    @Override
    public LinearMusicViewHolder onCreateVH(ViewGroup parent, int viewType) {
        LinearMusicViewHolder linearMusicViewHolder = new LinearMusicViewHolder(View.inflate(parent.getContext(), R.layout.item_editer_bgm, null));
        return linearMusicViewHolder;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindVH(LinearMusicViewHolder holder, int position) {
        TCBGMInfo info = mBGMList.get(position);


        Log.d(TAG, "onBindVH   info.name:" + info.name);

        holder.tvName.setText(info.name);
        holder.tvTime.setText(formatTime(info.duration));
        holder.tvSinger.setText(info.singer);
        holder.itemView.setTag(position);
        holder.setPosition(position);
        holder.setOnClickSubItemListener(mOnClickSubItemListener);
        holder.setOnItemClickListener(mOnItemClickListener);

        mProgressButtonIndexMap.put(position, holder);
    }

    @Override
    public void onBindViewHolder(LinearMusicViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mBGMList.size();
    }

    public void updateItem(int position, TCBGMInfo info) {

        LinearMusicViewHolder holder = mProgressButtonIndexMap.get(position);

        if (holder == null) {
            return;
        }


        Log.d(TAG, "onBindVH   info.name:" + info.name);

        holder.tvName.setText(info.name);
        holder.itemView.setTag(position);
        holder.tvTime.setText(formatTime(info.duration));
        holder.tvSinger.setText(info.singer);
        holder.setPosition(position);
        holder.setOnClickSubItemListener(mOnClickSubItemListener);
        holder.setOnItemClickListener(mOnItemClickListener);

    }

    //    转换歌曲时间的格式
    public String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }

    public static class LinearMusicViewHolder extends RecyclerView.ViewHolder {
        private Button btnUse;
        private TextView tvName;
        private TextView tvTime;
        private TextView tvSinger;
        private OnItemClickListener onItemClickListener;
        private int position;

        public LinearMusicViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.bgm_tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.bgm_tv_time);
            tvSinger = (TextView) itemView.findViewById(R.id.bgm_tv_singer);
            btnUse = (Button) itemView.findViewById(R.id.btn_use);
            btnUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickSubItemListener != null) {
                        mOnClickSubItemListener.onClickUseBtn(btnUse, position);
                    }
                }
            });

        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        private OnClickSubItemListener mOnClickSubItemListener;

        public void setOnClickSubItemListener(OnClickSubItemListener onClickSubItemListener) {
            mOnClickSubItemListener = onClickSubItemListener;
        }

    }

    public interface OnClickSubItemListener {
        void onClickUseBtn(Button button, int position);
    }

}
