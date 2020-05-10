package com.bogokj.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.fanwe.lib.looper.ISDLooper;
import com.fanwe.lib.looper.impl.SDSimpleLooper;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsgViewerJoin;
import com.bogokj.live.view.LiveSVGAPlayView;

import java.util.LinkedList;

/**
 * 座驾播放
 *
 * @author Administrator
 * @date 2016-5-16 下午1:16:27
 */
public class RoomCarsSVGAPlayView extends RoomLooperMainView<CustomMsgViewerJoin> {


    public RoomCarsSVGAPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomCarsSVGAPlayView(Context context) {
        super(context);
    }


    private static final long DURATION_LOOPER = 1000;

    private LiveSVGAPlayView<CustomMsgViewerJoin> svga_play_view;

    private ISDLooper mLooper;

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_svga_play;
    }

    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        svga_play_view = find(R.id.svga_play_view);

        mLooper = new SDSimpleLooper();

    }

    private boolean hasAnimatorPlaying() {
        if (svga_play_view != null) {
            return svga_play_view.isPlaying();
        }
        return false;
    }

    private void playAnimatorView(LiveSVGAPlayView<CustomMsgViewerJoin> view, CustomMsgViewerJoin msg) {
        if (view != null) {
            this.svga_play_view = view;
            svga_play_view.setAnimatorListener(new SVGAPlayLister() {
                @Override
                public void onFinished() {
                }
            });

            svga_play_view.setMsg(msg);
        }
    }

    private void removeAnimatorView() {
        if (svga_play_view != null) {
            svga_play_view.removeSelf();
            svga_play_view = null;
        }
    }

    /**
     * 是否所有View都处于空闲
     *
     * @return
     */
    private boolean isAllGifViewFree() {

        if (svga_play_view.isBusy()) {
            return false;
        }

        return true;
    }


    @Override
    protected void onLooperWork(LinkedList<CustomMsgViewerJoin> queue) {
        CustomMsgViewerJoin msg = queue.peek();
        if (msg == null) {
            return;
        }

        if (hasAnimatorPlaying()) {
            // 当前有gif或者动画在播放
            LogUtil.i("playing");
        } else {
            //动画
            queue.poll();
            playAnimatorView(svga_play_view, msg);
        }
    }

    @Override
    protected long getLooperPeriod() {
        return DURATION_LOOPER;
    }


    @Override
    public void onMsgViewerJoin(CustomMsgViewerJoin msg) {
        super.onMsgViewerJoin(msg);
        if (msg != null && msg.getSender() != null) {

            if (!TextUtils.isEmpty(msg.getSender().getNoble_car_url())) {
                offerModel(msg);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLooper.stop();
    }

}
