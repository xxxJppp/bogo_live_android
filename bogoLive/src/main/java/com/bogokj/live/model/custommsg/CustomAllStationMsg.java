package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

//全站广播通知
public class CustomAllStationMsg extends CustomMsg {
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CustomAllStationMsg() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_All_STATION);
    }
}
