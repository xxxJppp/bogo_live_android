package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant.CustomMsgType;

/**
 * 同意PK请求
 */
public class CustomMsgAcceptPK extends CustomMsg {

    private String pk_id;
    public CustomMsgAcceptPK() {
        super();
        setType(CustomMsgType.MSG_ACCEPT_PK);
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }
}
