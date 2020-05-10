package com.bogokj.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.hybrid.constant.ApkConstant;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.blocker.SDDurationBlocker;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.live.IMHelper;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.view.HeartLayout;

import java.util.LinkedList;

/**
 * 心心
 */
public class RoomHeartView extends RoomLooperMainView<CustomMsgLight> {
    public RoomHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomHeartView(Context context) {
        super(context);
        init();
    }

    /**
     * 点击拦截间隔
     */
    private static final long DURATION_BLOCK_CLICK = 200;
    private static final long DURATION_LOOPER = 300;
    /**
     * 队列最大消息数量
     */
    private static final int MAX_MSG = 10;

    private HeartLayout view_heart;
    private SDDurationBlocker mDurationBlocker;

    private boolean mIsFirst = true;

    private int mIsNoLight; //是否不需要直播间点亮功能，1-不需要直播间点亮功能

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_heart;
    }

    protected void init() {
        view_heart = find(R.id.view_heart);
        mIsNoLight = AppRuntimeWorker.getIs_no_light();

        mDurationBlocker = new SDDurationBlocker(DURATION_BLOCK_CLICK);
    }

    @Override
    protected long getLooperPeriod() {
        return DURATION_LOOPER;
    }

    @Override
    protected void onLooperWork(LinkedList<CustomMsgLight> queue) {
        if (ApkConstant.DEBUG) {
            LogUtil.i("onLoop:" + queue.size());
        }
        CustomMsgLight msg = queue.poll();
        if (msg != null) {
            addHeartInside(msg.getImageName());
        }
    }

    public void addHeart() {
        if (mDurationBlocker.block()) {
            return;
        }
        if (mIsNoLight == 1) {
            //服务端未开启点亮功能
            return;
        }

        sendLightMsg();
    }

    private void sendLightMsg() {
        boolean sendImMsg = true;

        final CustomMsgLight msg = new CustomMsgLight();
        final String name = view_heart.randomImageName();
        msg.setImageName(name);

        if (mIsFirst) {
            mIsFirst = false;
            if (getLiveActivity().getRoomInfo() != null) {
                if (getLiveActivity().getRoomInfo().getJoin_room_prompt() == 0) {
                    UserModel user = UserModelDao.query();
                    if (user != null && !user.isProUser()) {
                        sendImMsg = false;
                    }
                }
            }

            CommonInterface.requestLike(getLiveActivity().getRoomId(), new AppRequestCallback<BaseActModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.isOk()) {
                        LogUtil.i("request like success");
                    }
                }
            });
            msg.setShowMsg(1);
        } else {
            msg.setShowMsg(0);
        }

        if (view_heart.getHeartCount() >= 7) {
            // 界面中已经有足够的星星，不发送im消息
            sendImMsg = false;
        }

        if (sendImMsg) {
            LogUtil.i("add heart im");
            String groupId = getLiveActivity().getGroupId();
            IMHelper.sendMsgGroup(groupId, msg, null);
            if (msg.getShowMsg() == 1) {
                IMHelper.postMsgLocal(msg, groupId);
            } else {
                addHeartInside(name);
            }
        } else {
            LogUtil.i("add heart local");
            addHeartInside(name);
        }
    }

    private void addHeartInside(String imageName) {
        if (mIsNoLight == 1) {
            //服务端未开启点亮功能
            return;
        }
        view_heart.addHeart(imageName);
    }

    @Override
    public void onMsgLight(CustomMsgLight msg) {
        super.onMsgLight(msg);
        if (getQueue().size() >= MAX_MSG) {
            return;
        }
        offerModel(msg);
    }
}
