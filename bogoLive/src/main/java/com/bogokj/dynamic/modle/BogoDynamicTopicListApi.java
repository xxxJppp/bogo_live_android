package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoDynamicTopicListApi extends BaseActModel {
    private List<BogoDynamicTopicListModel> data;

    public List<BogoDynamicTopicListModel> getData() {
        return data;
    }

    public void setData(List<BogoDynamicTopicListModel> data) {
        this.data = data;
    }
}
