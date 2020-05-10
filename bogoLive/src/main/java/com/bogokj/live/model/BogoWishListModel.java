package com.bogokj.live.model;

public class BogoWishListModel {

    /**
     * id : 6
     * uid : 164739
     * g_id : 1
     * g_name : 香蕉
     * g_icon : http://fw25live.oss-cn-beijing.aliyuncs.com/public/gift/a8.png
     * g_num : 10
     * txt : 哈哈哈
     * add_day : 1575907200
     * add_time : 1575942946
     * g_now_num : 0
     */

    private String id;
    private String uid;
    private String g_id;
    private String g_name;
    private String g_icon;
    private String g_num;
    private String txt;
    private String add_day;
    private String add_time;
    private int g_now_num;
    private int is_del;

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getG_icon() {
        return g_icon;
    }

    public void setG_icon(String g_icon) {
        this.g_icon = g_icon;
    }

    public String getG_num() {
        return g_num;
    }

    public void setG_num(String g_num) {
        this.g_num = g_num;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getAdd_day() {
        return add_day;
    }

    public void setAdd_day(String add_day) {
        this.add_day = add_day;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getG_now_num() {
        return g_now_num;
    }

    public void setG_now_num(int g_now_num) {
        this.g_now_num = g_now_num;
    }
}
