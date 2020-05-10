package com.bogokj.live.model;

import com.bogokj.xianrou.util.TimeUtil;

public class LiveGuardianRulesModel {
    private String id;    //7
    private String title;    //守护者特权类型名称
    private String img;    //守护者特权类型图片
    private String sort;    //7
    private String status;    //1
    private Long addtime;    //1535598672

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return TimeUtil.timeFormat(addtime, "yyyy-MM-dd HH:mm");
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }
}
