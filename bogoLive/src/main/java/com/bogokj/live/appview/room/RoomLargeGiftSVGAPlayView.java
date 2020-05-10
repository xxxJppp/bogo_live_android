package com.bogokj.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.live.R;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.model.custommsg.CustomMsgGift;
import com.bogokj.live.view.LiveLargeGiftSVGAView;

/**
 * 礼物播放
 *
 * @author Administrator
 * @date 2016-5-16 下午1:16:27
 */
public class RoomLargeGiftSVGAPlayView extends BaseAppView {


    private LargeGiftPlayViewCallBack callBack;

    public RoomLargeGiftSVGAPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomLargeGiftSVGAPlayView(Context context) {
        super(context);
    }

    public RoomLargeGiftSVGAPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private CustomMsgGift msg;
//    protected TextView tv_gift_desc;


    private LiveLargeGiftSVGAView cars_play_view;


    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_large_gift_svga_play;
    }

    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        cars_play_view = find(R.id.large_gift_play_view);

//        tv_gift_desc = find(R.id.tv_gift_desc);

    }


    public void setMsg(CustomMsgGift msg) {
        this.msg = msg;
    }

    public CustomMsgGift getMsg() {
        return msg;
    }


    public final boolean isPlaying() {
        if (cars_play_view != null) {
            return cars_play_view.isPlaying();
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }



    public void play() {
        playAnimatorView(cars_play_view, msg);
    }


    private void playAnimatorView(LiveLargeGiftSVGAView view, CustomMsgGift msg) {
        if (view != null) {
            this.cars_play_view = view;
            cars_play_view.setAnimatorListener(new SVGAPlayLister() {
                @Override
                public void onFinished() {
                    callBack.onFinished();
                }
            });

            cars_play_view.setMsg(msg);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setAnimatorListener(LargeGiftPlayViewCallBack callBack) {
        this.callBack = callBack;
    }


}
