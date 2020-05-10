package com.bogokj.live.model.custommsg;

import com.bogokj.live.LiveConstant;

//幸运礼物消息
public class CustomMsgLuckGift extends CustomMsg {
    private String user_money;//中奖金额
    private String user_multiple; //中奖倍数
    private String num; //赠送礼物数量
    private String text;//中奖内容

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CustomMsgLuckGift() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_LUCK_GIFT);
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getUser_multiple() {
        return user_multiple;
    }

    public void setUser_multiple(String user_multiple) {
        this.user_multiple = user_multiple;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
