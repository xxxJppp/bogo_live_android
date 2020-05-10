package com.bogokj.pay;

import com.bogokj.live.LiveConstant;
import com.bogokj.live.room.ILiveInterface;
import com.bogokj.live.business.LiveBaseBusiness;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.pay.model.custommsg.CustomMsgStartPayMode;
import com.bogokj.pay.model.custommsg.CustomMsgStartScenePayMode;

/**
 * Created by Administrator on 2016/12/1.
 */

public abstract class LivePayBusiness extends LiveBaseBusiness
{
    public LivePayBusiness(ILiveInterface liveActivity)
    {
        super(liveActivity);
    }

    public void onMsgPayMode(MsgModel msg)
    {
        if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_START_PAY_MODE)
        {
            onMsgPayWillStart(msg.getCustomMsgStartPayMode());
        } else if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_START_SCENE_PAY_MODE)
        {
            onMsgScenePayWillStart(msg.getCustomMsgStartScenePayMode());
        }
    }

    protected void onMsgPayWillStart(CustomMsgStartPayMode customMsg)
    {

    }

    protected void onMsgScenePayWillStart(CustomMsgStartScenePayMode customMsg)
    {

    }
}
