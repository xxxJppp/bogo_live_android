package com.bogokj.live.dao;

/**
 * @author kn create
 * @description:
 * @date : 2020/2/27
 */
public class ToJoinLiveData {

    public ToJoinLiveData() {
    }

    public ToJoinLiveData(int type, int page, int position, int sex, String city) {
        this.type = type;
        this.page = page;
        this.position = position;
        this.sex = sex;
        this.city = city;
    }

    private int type;
    private int page;
    private int position;
    private int sex;
    private String city;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
