package com.bogokj.live.model;

import java.util.List;

/**
 * Created by Administrator on 2019/2/13 0013.
 */

public class LiveGiftTypeModel {


    private String id;
    private String name;
    private boolean isSelect;
    private List<LiveGiftModel> list;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<LiveGiftModel> getList()
    {
        return list;
    }

    public void setList(List<LiveGiftModel> list)
    {
        this.list = list;
    }
}
