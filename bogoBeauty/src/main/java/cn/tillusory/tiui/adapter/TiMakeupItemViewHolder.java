package cn.tillusory.tiui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2019-09-05.
 * Copyright (c) 2019 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiMakeupItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView thumbIV;
    public TextView nameTV;

    public TiMakeupItemViewHolder(View itemView) {
        super(itemView);
        thumbIV = itemView.findViewById(R.id.thumbIV);
        nameTV = itemView.findViewById(R.id.nameTV);
    }
}