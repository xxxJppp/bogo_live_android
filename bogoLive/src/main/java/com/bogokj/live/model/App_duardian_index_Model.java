package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2019/2/1 0001.
 * 获取守护类型和守护特权等信息
 */

public class App_duardian_index_Model extends BaseActModel {

    private int status;
    private String diamonds;
    private String head_image;
    private String is_guartian;
    private String guartian_time;
    //守护类型
    private List<GuardTypeModel> data;
    //守护特权
    private List<GuardPrivilegeModel> guardian_type;

    public static App_duardian_index_Model objectFromData(String str) {
        return new Gson().fromJson(str, App_duardian_index_Model.class);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(String diamonds) {
        this.diamonds = diamonds;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getIs_guartian() {
        return is_guartian;
    }

    public void setIs_guartian(String is_guartian) {
        this.is_guartian = is_guartian;
    }

    public String getGuartian_time() {
        return guartian_time;
    }

    public void setGuartian_time(String guartian_time) {
        this.guartian_time = guartian_time;
    }

    public List<GuardTypeModel> getData() {
        return data;
    }

    public void setData(List<GuardTypeModel> data) {
        this.data = data;
    }

    public List<GuardPrivilegeModel> getGuardian_type() {
        return guardian_type;
    }

    public void setGuardian_type(List<GuardPrivilegeModel> guardian_type) {
        this.guardian_type = guardian_type;
    }

}
