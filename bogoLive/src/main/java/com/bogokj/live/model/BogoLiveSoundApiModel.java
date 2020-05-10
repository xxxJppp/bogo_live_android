package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoLiveSoundApiModel extends BaseActModel {
    private List<BogoLiveSoundModel> data;

    public List<BogoLiveSoundModel> getData() {
        return data;
    }

    public void setData(List<BogoLiveSoundModel> data) {
        this.data = data;
    }
}
