package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class App_pk_getTime_Model extends BaseActModel {
    private List<TimeModel> list;

    public List<TimeModel> getList() {
        return list;
    }

    public void setList(List<TimeModel> list) {
        this.list = list;
    }
}
