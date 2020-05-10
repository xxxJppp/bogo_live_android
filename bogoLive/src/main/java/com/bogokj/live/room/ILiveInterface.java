package com.bogokj.live.room;

import com.bogokj.live.ILiveInfo;

public interface ILiveInterface extends ILiveInfo {

    /**
     * 打开直播间输入框
     *
     * @param content
     */
    void openSendMsg(String content);

}
