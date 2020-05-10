package com.bogokj.dynamic.event;

/**
 * Created by shibx on 2017/3/24.
 */

public class RefreshLiveVideoEvent {

    public RefreshLiveVideoEvent(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
