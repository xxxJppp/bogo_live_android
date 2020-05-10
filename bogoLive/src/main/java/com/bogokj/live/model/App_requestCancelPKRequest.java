package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

public class App_requestCancelPKRequest extends BaseActModel {
    private String to_user_id;

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }
}
