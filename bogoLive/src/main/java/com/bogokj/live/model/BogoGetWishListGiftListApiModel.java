package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoGetWishListGiftListApiModel extends BaseActModel {
    private List<LiveGiftModel> list;

    public List<LiveGiftModel> getList() {
        return list;
    }

    public void setList(List<LiveGiftModel> list) {
        this.list = list;
    }
}
