package com.bogokj.xianrou.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoVideoClassLabelModelApi extends BaseActModel {
    private List<BogoVideoClassLabelModel> data;

    public List<BogoVideoClassLabelModel> getData() {
        return data;
    }

    public void setData(List<BogoVideoClassLabelModel> data) {
        this.data = data;
    }
}
