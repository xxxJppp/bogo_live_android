package com.bogokj.live.model;

/**
 * Created by Administrator on 2016/8/8.
 */
public class JoinPlayBackData
{
    private String use_id;

    public String getUse_id() {
        return use_id;
    }

    public void setUse_id(String use_id) {
        this.use_id = use_id;
    }

    private int roomId;
    private String loadingVideoImageUrl;
    //add 回放视频创建类型 0：移动端直播； 1：PC端直播；
    private int create_type;

    public int getCreate_type() {
        return create_type;
    }

    public void setCreate_type(int create_type) {
        this.create_type = create_type;
    }

    public String getLoadingVideoImageUrl()
    {
        return loadingVideoImageUrl;
    }

    public JoinPlayBackData setLoadingVideoImageUrl(String loadingVideoImageUrl)
    {
        this.loadingVideoImageUrl = loadingVideoImageUrl;
        return this;
    }

    public int getRoomId()
    {
        return roomId;
    }

    public JoinPlayBackData setRoomId(int roomId)
    {
        this.roomId = roomId;
        return this;
    }
}
