package com.bogokj.auction.model;

import com.bogokj.hybrid.model.BaseActModel;

/**
 * Created by Administrator on 2016/8/11.
 */
public class App_message_getlistActModel extends BaseActModel
{
    private MessageGetListDataModel data;

    public MessageGetListDataModel getData()
    {
        return data;
    }

    public void setData(MessageGetListDataModel data)
    {
        this.data = data;
    }
}
