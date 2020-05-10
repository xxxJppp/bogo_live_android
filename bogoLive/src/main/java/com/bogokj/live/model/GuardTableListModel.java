package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2019/1/30 0030.
 * 获取守护的返回接收model
 */

public class GuardTableListModel  extends BaseActModel {

    private int status;
    private String guardian_sum;
    private String is_guartian;
    private String guartian_time;

    public String getGuartian_time() {
        return guartian_time;
    }

    public void setGuartian_time(String guartian_time) {
        this.guartian_time = guartian_time;
    }

    private List<GuardMemberBean> list;

    public static GuardTableListModel objectFromData(String str) {

        return new Gson().fromJson(str, GuardTableListModel.class);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGuardian_sum() {
        return guardian_sum;
    }

    public void setGuardian_sum(String guardian_sum) {
        this.guardian_sum = guardian_sum;
    }

    public String getIs_guartian() {
        return is_guartian;
    }

    public void setIs_guartian(String is_guartian) {
        this.is_guartian = is_guartian;
    }

    public List<GuardMemberBean> getList() {
        return list;
    }

    public void setList(List<GuardMemberBean> list) {
        this.list = list;
    }


}
