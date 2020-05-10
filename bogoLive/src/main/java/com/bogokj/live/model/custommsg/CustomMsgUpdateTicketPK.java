package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

public class CustomMsgUpdateTicketPK extends CustomMsg{

    private int pk_ticket1;
    private int pk_ticket2;
    private String emcee_user_id1;
    private String emcee_user_id2;

    public CustomMsgUpdateTicketPK() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_UPDATE_PK_TICKET);
    }

    public int getPk_ticket1() {
        return pk_ticket1;
    }

    public void setPk_ticket1(int pk_ticket1) {
        this.pk_ticket1 = pk_ticket1;
    }

    public int getPk_ticket2() {
        return pk_ticket2;
    }

    public void setPk_ticket2(int pk_ticket2) {
        this.pk_ticket2 = pk_ticket2;
    }

    public String getEmcee_user_id1() {
        return emcee_user_id1;
    }

    public void setEmcee_user_id1(String emcee_user_id1) {
        this.emcee_user_id1 = emcee_user_id1;
    }

    public String getEmcee_user_id2() {
        return emcee_user_id2;
    }

    public void setEmcee_user_id2(String emcee_user_id2) {
        this.emcee_user_id2 = emcee_user_id2;
    }
}
