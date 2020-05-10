package com.bogokj.live.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2019/2/1 0001.
 * 守护类型model
 */

public class GuardTypeModel {

    private String id;
    private String name;
    private String day;
    private String coin;
    private String type;
    private String source_type;
    private String sort;
    private String addtime;
    private List<String> type_name;

    public static GuardTypeModel objectFromData(String str) {

        return new Gson().fromJson(str, GuardTypeModel.class);
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
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

    public List<String> getType_name() {
        return type_name;
    }

    public void setType_name(List<String> type_name) {
        this.type_name = type_name;
    }
}
