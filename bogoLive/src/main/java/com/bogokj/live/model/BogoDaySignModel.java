package com.bogokj.live.model;

public class BogoDaySignModel {
    private String day;
    private String num;
    private String id;
    private String is_sign;
    private String now;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }


    public String getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getDay() {
        return "第" + day + "天";
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
