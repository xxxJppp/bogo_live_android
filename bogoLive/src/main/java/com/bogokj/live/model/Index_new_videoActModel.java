package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActListModel;

import java.util.List;

public class Index_new_videoActModel extends BaseActListModel
{

    private static final long serialVersionUID = 1L;

    private List<LiveRoomModel> list;

    public List<LiveRoomModel> getList()
    {
        return list;
    }

    public void setList(List<LiveRoomModel> list)
    {
        this.list = list;
    }
}
