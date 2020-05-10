package com.bogokj.live.model;

import com.bogokj.live.LiveConstant;
import com.bogokj.live.model.custommsg.CustomMsg;

public class CustomMsgCancelPK extends CustomMsg{
    public CustomMsgCancelPK()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_CANCEL_PK);
    }
}
