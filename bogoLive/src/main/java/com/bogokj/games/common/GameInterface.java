package com.bogokj.games.common;

import com.bogokj.games.model.Games_autoStartActModel;
import com.bogokj.hybrid.http.AppHttpUtil;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.http.AppRequestParams;

/**
 * 游戏相关接口
 */
public class GameInterface
{

    /**
     * 请求设置自动开始游戏
     *
     * @param auto_start 1-自动开始；0-手动开始
     * @param callback
     */
    public static void requestAutoStartGame(int auto_start, AppRequestCallback<Games_autoStartActModel> callback)
    {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("autoStart");
        params.put("auto_start", auto_start);
        AppHttpUtil.getInstance().post(params, callback);
    }

}
