package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoDaySignListApiModel extends BaseActModel {
    private String signin_count;
    private String signin_continue;
    private List<BogoDaySignModel> list;

    public String getSignin_continue() {
        return signin_continue;
    }

    public void setSignin_continue(String signin_continue) {
        this.signin_continue = signin_continue;
    }

    public String getSignin_count() {
        return signin_count;
    }

    public void setSignin_count(String signin_count) {
        this.signin_count = signin_count;
    }


    public List<BogoDaySignModel> getList() {
        return list;
    }

    public void setList(List<BogoDaySignModel> list) {
        this.list = list;
    }
}
