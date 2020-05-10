package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant.CustomMsgType;

/**
 * 同意PK请求
 */
public class CustomMsgRejectPK extends CustomMsg {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CustomMsgRejectPK() {
        super();
        setType(CustomMsgType.MSG_REJECT_PK);
    }

}
