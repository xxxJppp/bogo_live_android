package com.bogokj.games.model;

import com.bogokj.hybrid.model.BaseActModel;

/**
 * Created by shibx on 2016/11/25.
 */

public class Games_betActModel extends BaseActModel {

    private GameBetDataModel data;

    public GameBetDataModel getData() {
        return data;
    }

    public void setData(GameBetDataModel data) {
        this.data = data;
    }
}
