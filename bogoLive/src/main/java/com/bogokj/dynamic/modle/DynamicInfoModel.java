package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActModel;

public class DynamicInfoModel extends BaseActModel {
    private DynamicModel data;

    public DynamicModel getData() {
        return data;
    }

    public void setData(DynamicModel data) {
        this.data = data;
    }
}
