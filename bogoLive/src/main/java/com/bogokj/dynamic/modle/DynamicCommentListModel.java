package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActListModel;

import java.util.ArrayList;
import java.util.List;

public class DynamicCommentListModel extends BaseActListModel {
    private List<DynamicCommonInfoModel> list = new ArrayList<>();

    public List<DynamicCommonInfoModel> getList() {
        return list;
    }

    public void setList(List<DynamicCommonInfoModel> list) {
        this.list = list;
    }
}
