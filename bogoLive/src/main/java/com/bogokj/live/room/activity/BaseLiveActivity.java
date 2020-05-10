package com.bogokj.live.room.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.room.ILiveInterface;
import com.bogokj.live.model.App_get_videoActModel;

/**
 * @author kn create
 * @description: 回播 主播 用户 activity父类
 * @date : 2020/2/24
 */
public class BaseLiveActivity extends BaseActivity implements
        ILiveInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //----------ILiveActivity implements start----------

    @Override
    public int getRoomId() {
        return LiveInformation.getInstance().getRoomId();
    }

    @Override
    public String getGroupId() {
        return LiveInformation.getInstance().getGroupId();
    }

    @Override
    public String getCreaterId() {
        return LiveInformation.getInstance().getCreaterId();
    }

    @Override
    public App_get_videoActModel getRoomInfo() {
        return LiveInformation.getInstance().getRoomInfo();
    }

    @Override
    public boolean isPrivate() {
        return LiveInformation.getInstance().isPrivate();
    }

    @Override
    public boolean isPlayback() {
        return LiveInformation.getInstance().isPlayback();
    }

    @Override
    public boolean isCreater() {
        return LiveInformation.getInstance().isCreater();
    }

    @Override
    public int getSdkType() {
        return LiveInformation.getInstance().getSdkType();
    }

    @Override
    public boolean isAuctioning() {
        return LiveInformation.getInstance().isAuctioning();
    }

    @Override
    public void openSendMsg(String content) {

    }


    public void addView(View view) {
        SDViewUtil.addView((ViewGroup) findViewById(android.R.id.content), view);
    }

    public void replaceView(int id, View mRoomInfoView) {
        SDViewUtil.replaceView((ViewGroup) findViewById(id), mRoomInfoView);
    }


}
