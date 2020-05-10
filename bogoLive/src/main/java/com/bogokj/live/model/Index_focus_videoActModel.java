package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class Index_focus_videoActModel extends BaseActModel
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<LiveRoomModel> data;
    private List<LivePlaybackModel> playback;

    public List<LiveRoomModel> getData() {
        return data;
    }

    public void setData(List<LiveRoomModel> data) {
        this.data = data;
    }

    public List<LivePlaybackModel> getPlayback()
    {
        return playback;
    }

    public void setPlayback(List<LivePlaybackModel> playback)
    {
        this.playback = playback;
    }
}
