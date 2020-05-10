package com.bogokj.live.utils;


import com.bogokj.live.activity.TCVideoFileInfo;

/**
 * Created by liyuejiao on 2018/1/11.
 */

public class ItemView {
    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public interface OnAddListener {
        void onAdd(TCVideoFileInfo fileInfo);
    }
}
