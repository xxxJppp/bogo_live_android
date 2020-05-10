package com.bogokj.live.model;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2019/2/1 0001.
 * 守护特权model
 */

public class GuardPrivilegeModel {
    private String id;
    private String name;
    private String select_icon;
    private String default_icon;
    private String centent;
    private String sort;

    public static GuardPrivilegeModel objectFromData(String str) {

        return new Gson().fromJson(str, GuardPrivilegeModel.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelect_icon() {
        return select_icon;
    }

    public void setSelect_icon(String select_icon) {
        this.select_icon = select_icon;
    }

    public String getDefault_icon() {
        return default_icon;
    }

    public void setDefault_icon(String default_icon) {
        this.default_icon = default_icon;
    }

    public String getCentent() {
        return centent;
    }

    public void setCentent(String centent) {
        this.centent = centent;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
