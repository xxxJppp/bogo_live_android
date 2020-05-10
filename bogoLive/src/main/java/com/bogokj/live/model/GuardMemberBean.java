package com.bogokj.live.model;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2019/1/30 0030.
 */

public class GuardMemberBean {
    private String uid;
    private String nick_name;
    private String head_image;
    private String sex;
    private String user_level;
    private String type;
    private String total_diamonds;
    private String endtime;

    public static GuardMemberBean objectFromData(String str) {

        return new Gson().fromJson(str, GuardMemberBean.class);
    }


    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal_diamonds() {
        return total_diamonds;
    }

    public void setTotal_diamonds(String total_diamonds) {
        this.total_diamonds = total_diamonds;
    }
}
