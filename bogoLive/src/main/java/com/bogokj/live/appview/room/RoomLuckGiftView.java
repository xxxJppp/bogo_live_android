package com.bogokj.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.live.R;
import com.bogokj.live.appview.LiveLargeGiftInfoView;
import com.bogokj.live.appview.LiveLuckGiftInfoView;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.custommsg.CustomMsgLargeGift;
import com.bogokj.live.model.custommsg.CustomMsgLuckGift;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.model.custommsg.CustomMsgRefreshWish;

import java.util.LinkedList;

public class RoomLuckGiftView extends RoomLooperMainView<CustomMsgLuckGift> {
    public RoomLuckGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomLuckGiftView(Context context) {
        super(context);
        init();
    }

    private static final long DURATION_LOOPER = 1000;

    private LiveLuckGiftInfoView view_gift_info;

    private LuckGiftInfoViewCallback mCallback;

    private void init() {
        setContentView(R.layout.view_room_luck_gift_info);
        view_gift_info = (LiveLuckGiftInfoView) findViewById(R.id.view_gift_info);

        view_gift_info.setOnClickListener(this);
    }

    public void setCallback(LuckGiftInfoViewCallback callback) {
        mCallback = callback;
    }

    @Override
    protected void onLooperWork(final LinkedList<CustomMsgLuckGift> queue) {
        if (!view_gift_info.isPlaying()) {
            view_gift_info.playMsg(queue.poll());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == view_gift_info) {
            if (mCallback != null) {
                mCallback.onClickInfoView(view_gift_info.getMsg());
            }
        }
    }

    @Override
    public void OnMsgLuckGift(CustomMsgLuckGift msg) {
        super.OnMsgLuckGift(msg);
        //给用户的中奖提示
        if (msg.getSender().getUser_id().equals(UserModelDao.getUserId())) {
            SDToast.showToast("恭喜您中奖" + msg.getUser_multiple() + "倍！");
        }
        offerModel(msg);
    }

    public interface LuckGiftInfoViewCallback {
        void onClickInfoView(CustomMsgLuckGift msg);
    }

    @Override
    public void OnMsgRefreshWish(CustomMsgRefreshWish msg) {
        //刷新心愿单
    }


    @Override
    protected long getLooperPeriod() {
        return DURATION_LOOPER;
    }

}
