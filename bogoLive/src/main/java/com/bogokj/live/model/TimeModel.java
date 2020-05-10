package com.bogokj.live.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/1/21 0021.
 */

public class TimeModel implements Serializable {
    private String id;
    private String time;
    private  String sort;
    private  String addtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
