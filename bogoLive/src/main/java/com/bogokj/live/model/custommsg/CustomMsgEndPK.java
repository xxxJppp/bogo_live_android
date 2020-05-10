package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

public class CustomMsgEndPK extends CustomMsg{

    private String win_user_id;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomMsgEndPK()

    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_END_PK);
    }

    public String getWin_user_id() {
        return win_user_id;
    }

    public void setWin_user_id(String win_user_id) {
        this.win_user_id = win_user_id;
    }
}
