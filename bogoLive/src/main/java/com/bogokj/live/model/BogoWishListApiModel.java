package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoWishListApiModel extends BaseActModel {
    private List<BogoWishListModel> list;

    public List<BogoWishListModel> getList() {
        return list;
    }

    public void setList(List<BogoWishListModel> list) {
        this.list = list;
    }
}
