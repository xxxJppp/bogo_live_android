package com.bogokj.live.model;

public class BogoNobleListModel {

    public BogoNobleListModel(String id, String uid, String g_id) {
        this.id = id;
        this.uid = uid;
        this.g_id = g_id;
    }

    private String id;
    private String uid;
    private String g_id;


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

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }
}
