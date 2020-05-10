package com.bogokj.dynamic.modle;

import com.bogokj.dynamic.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class DynamicModel {
    private String id;
    private String uid;
    private String content;
    private String audio;
    private String video;
    private String cover_url;
    private int praise; //点赞数
    private int comments;
    private int forwarding;//转发数
    private long addtime;
    private int audio_duration;

    private String nick_name;

    //    private UserModel userInfo;
    private int is_like;

    private String head_image;
    private int is_focus;
    private int is_top;

    private List<String> picUrls = new ArrayList<>();
//    private List<String> thumbnailPicUrls;


    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public int getIs_focus() {
        return is_focus;
    }

    public void setIs_focus(int is_focus) {
        this.is_focus = is_focus;
    }

    private String timing;

    private int sex; // 0-未知，1-男，2-女

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAudio_duration() {
        return audio_duration / 1000;
    }

    public void setAudio_duration(int audio_duration) {
        this.audio_duration = audio_duration;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getForwarding() {
        return forwarding;
    }

    public void setForwarding(int forwarding) {
        this.forwarding = forwarding;
    }

    public long getAddtime() {
        return addtime;
    }

    public String getTime() {
        return TimeUtils.getFriendlyTimeSpanByNow(addtime * 1000);
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }
}
