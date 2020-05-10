package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

/**
 * Created by shibx on 2016/7/20.
 */
public class App_doExchangeActModel extends BaseActModel{

    private int useable_ticket;
    private int diamonds;
    private String des;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getUseable_ticket() {
        return useable_ticket;
    }

    public void setUseable_ticket(int useable_ticket) {
        this.useable_ticket = useable_ticket;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }
}
