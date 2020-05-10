package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

//守护者购买成功
public class CustomMsgBuyGuardianSuccess extends CustomMsg {

    private String svga_url;
    private String svga_text;

    public CustomMsgBuyGuardianSuccess() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_BUY_GUARDIAN_SUCCESS);
    }

    public String getSvga_url() {
        return svga_url;
    }

    public void setSvga_url(String svga_url) {
        this.svga_url = svga_url;
    }

    public String getSvga_text() {
        return svga_text;
    }

    public void setSvga_text(String svga_text) {
        this.svga_text = svga_text;
    }
}
