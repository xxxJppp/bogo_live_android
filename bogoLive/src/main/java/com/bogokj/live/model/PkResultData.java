package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class PkResultData extends BaseActModel {

    private int status;
    private String win_user_id;
    private int punishment_time;

    public int getPunishment_time() {
        return punishment_time;
    }

    public void setPunishment_time(int punishment_time) {
        this.punishment_time = punishment_time;
    }

    private String emcee_user_id1;
    private String emcee_user_id2;
    private String pk_ticket1;
    private String pk_ticket2;

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

    public String getPk_ticket1() {
        return pk_ticket1;
    }

    public void setPk_ticket1(String pk_ticket1) {
        this.pk_ticket1 = pk_ticket1;
    }

    public String getPk_ticket2() {
        return pk_ticket2;
    }

    public void setPk_ticket2(String pk_ticket2) {
        this.pk_ticket2 = pk_ticket2;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public String getWin_user_id() {
        return win_user_id;
    }

    public void setWin_user_id(String win_user_id) {
        this.win_user_id = win_user_id;
    }




}
