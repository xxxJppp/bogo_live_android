package com.bogokj.dynamic.modle;

public class DynamicCommonInfoModel {


    /**
     *  "id": "1",
     "uid": "2",
     "dynamic_id": "1",
     "dynamic_uid": "2",
     "reply_id": "0",
     "content": "bbbbbbbbbbbbbb",
     "addtime": null,
     "nick_name": "\u6c79\u6d8c\u7684\u6f8e\u6e43",
     "head_image": ".\/public\/attachment\/201904\/2\/1554461761501.png"
     */

    private String id;
    private String uid;
    private String dynamic_id;
    private String dynamic_uid;
    private String reply_id;
    private long addtime;
    private String content;

    private String nick_name;
    private String head_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getDynamic_uid() {
        return dynamic_uid;
    }

    public void setDynamic_uid(String dynamic_uid) {
        this.dynamic_uid = dynamic_uid;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }
}
