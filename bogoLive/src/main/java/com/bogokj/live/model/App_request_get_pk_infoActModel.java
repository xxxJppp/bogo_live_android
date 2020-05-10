package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

public class App_request_get_pk_infoActModel extends BaseActModel {
    private String emcee_user_id1;
    private String emcee_user_id2;
    private String group_id1;
    private String group_id2;
    private String play_url1;
    private String play_url2;
    private int pk_ticket1;
    private int pk_ticket2;
    private int pk_time; //倒计时

    private String nick_name1;
    private String nick_name2;

    private String head_image1;
    private String head_image2;
    private int has_focus1;
    private int has_focus2;

    private String win_user_id;

    private int pk_status;

    public int getPk_status() {
        return pk_status;
    }

    public void setPk_status(int pk_status) {
        this.pk_status = pk_status;
    }

    public String getWin_user_id() {
        return win_user_id;
    }

    public void setWin_user_id(String win_user_id) {
        this.win_user_id = win_user_id;
    }
    public String getNick_name1() {
        return nick_name1;
    }

    public void setNick_name1(String nick_name1) {
        this.nick_name1 = nick_name1;
    }

    public String getNick_name2() {
        return nick_name2;
    }

    public void setNick_name2(String nick_name2) {
        this.nick_name2 = nick_name2;
    }

    public String getHead_image1() {
        return head_image1;
    }

    public void setHead_image1(String head_image1) {
        this.head_image1 = head_image1;
    }

    public String getHead_image2() {
        return head_image2;
    }

    public void setHead_image2(String head_image2) {
        this.head_image2 = head_image2;
    }

    public int getHas_focus1() {
        return has_focus1;
    }

    public void setHas_focus1(int has_focus1) {
        this.has_focus1 = has_focus1;
    }

    public int getHas_focus2() {
        return has_focus2;
    }

    public void setHas_focus2(int has_focus2) {
        this.has_focus2 = has_focus2;
    }

    public int getPk_time() {
        return pk_time;
    }

    public void setPk_time(int pk_time) {
        this.pk_time = pk_time;
    }

    public int getPk_ticket1() {
        return pk_ticket1;
    }

    public void setPk_ticket1(int pk_ticket1) {
        this.pk_ticket1 = pk_ticket1;
    }

    public int getPk_ticket2() {
        return pk_ticket2;
    }

    public void setPk_ticket2(int pk_ticket2) {
        this.pk_ticket2 = pk_ticket2;
    }

    public String getEmcee_user_id1() {
        return emcee_user_id1;
    }

    public void setEmcee_user_id1(String emcee_user_id1) {
        this.emcee_user_id1 = emcee_user_id1;
    }

    public String getEmcee_user_id2() {
        return emcee_user_id2;
    }

    public void setEmcee_user_id2(String emcee_user_id2) {
        this.emcee_user_id2 = emcee_user_id2;
    }

    public String getGroup_id1() {
        return group_id1;
    }

    public void setGroup_id1(String group_id1) {
        this.group_id1 = group_id1;
    }

    public String getGroup_id2() {
        return group_id2;
    }

    public void setGroup_id2(String group_id2) {
        this.group_id2 = group_id2;
    }

    public String getPlay_url1() {
        return play_url1.replace("rtmp", "http") + ".flv";
    }

    public String getPlay_url1_def() {
        return play_url1;
    }

    public void setPlay_url1(String play_url1) {
        this.play_url1 = play_url1;
    }

    public String getPlay_url2() {
        return play_url2.replace("rtmp", "http") + ".flv";
    }

    public String getPlay_url2_def() {
        return play_url2;
    }


    public void setPlay_url2(String play_url2) {
        this.play_url2 = play_url2;
    }
}
