package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActModel;

public class DynamicLikeModel  extends BaseActModel{
    private int is_like	;//0未点赞1已点赞
    private int count;	//点赞数

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
