package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

//开通守护成功
public class CustomMsgOpenGuardSuccess extends CustomMsg {

    public CustomMsgOpenGuardSuccess() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_OPEN_GUARD_SUCCESS);
    }
}
