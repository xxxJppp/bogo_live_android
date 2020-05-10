package com.bogokj.live.model;

import com.bogokj.dynamic.modle.DynamicModel;
import com.bogokj.hybrid.model.BaseActListModel;

import java.util.List;

/**
 * Created by luodong on 2016/10/17.
 */
public class App_DynamicListModel extends BaseActListModel
{
    private List<DynamicModel> list;

    public List<DynamicModel> getList() {
        return list;
    }

    public void setList(List<DynamicModel> list) {
        this.list = list;
    }

}
