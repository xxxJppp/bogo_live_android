package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29.
 */
public class User_baseinfoActModel extends BaseActModel
{
    private List<LiveConversationListModel> list;

    public List<LiveConversationListModel> getList()
    {
        return list;
    }

    public void setList(List<LiveConversationListModel> list)
    {
        this.list = list;
    }
}
