package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant.CustomMsgType;

/**
 * 接收后台管理员发送给主播的警告消息
 */
public class CustomMsgRequestPK extends CustomMsg {


    public static final String MSG_REJECT = "主播拒绝了你的PK请求";
    public static final String MSG_MAX = "当前主播PK已上限，请稍候尝试";
    public static final String MSG_BUSY = "主播有未处理的PK请求，请稍候再试";
   private  String  pkid;

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public CustomMsgRequestPK() {
        super();
        setType(CustomMsgType.MSG_REQUEST_PK);
    }
}
