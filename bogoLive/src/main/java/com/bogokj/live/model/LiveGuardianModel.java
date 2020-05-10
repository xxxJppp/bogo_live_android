package com.bogokj.live.model;

import com.bogokj.live.utils.LiveUtils;

public class LiveGuardianModel {
    private String id;
    private String uid;   //守护人id
    private String host_id;   //主播id
    private String coin;   //消费金币
    private String addtime;   //添加时间
    private String endtime;   //到期时间
    private String nick_name;   //用户昵称
    private String head_image;   //用户头像

    private int user_level;
    private String signature;
    private int is_follower; // 0:未关注 1:已关注

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getIs_follower() {
        return is_follower;
    }

    public void setIs_follower(int is_follower) {
        this.is_follower = is_follower;
    }

    public int getLevelImageResId()
    {
        return LiveUtils.getLevelImageResId(user_level);
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

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
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
}
