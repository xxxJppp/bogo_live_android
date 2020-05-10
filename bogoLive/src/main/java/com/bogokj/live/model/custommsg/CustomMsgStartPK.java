package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

public class CustomMsgStartPK extends CustomMsg{

    private String pk_id;

    public CustomMsgStartPK() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_START_PK);
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }
}
