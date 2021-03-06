package com.bogokj.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bogokj.live.R;
import com.bogokj.live.appview.LivePlayMusicView;
import com.bogokj.live.control.IPushSDK;
import com.bogokj.live.control.LivePushSDK;
import com.bogokj.live.music.MusicEffectConfig;
import com.bogokj.live.music.PushMusicEffectDialog;

/**
 * 直播sdk播放音乐view
 */
public class RoomPushMusicView extends ARoomMusicView
{
    private PushMusicEffectDialog mMusicEffectDialog;

    public RoomPushMusicView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomPushMusicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomPushMusicView(Context context)
    {
        super(context);
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_room_push_music);
        setPlayMusicView((LivePlayMusicView) findViewById(R.id.view_play_music));

        getPushSDK().setBgmPlayerCallback(new IPushSDK.BGMPlayerCallback()
        {
            @Override
            public void onBGMComplete()
            {
                stopMusic();
            }
        });
        updateEffectValue(getMusicEffectConfig());
    }

    protected IPushSDK getPushSDK()
    {
        return LivePushSDK.getInstance();
    }

    @Override
    protected void updateEffectValue(MusicEffectConfig config)
    {
        getPushSDK().setBGMVolume(config.getMusicVolume());
        getPushSDK().setMicVolume(config.getMicVolume());
    }

    @Override
    public boolean isMusicStarted()
    {
        return getPushSDK().isBGMStarted();
    }

    @Override
    public boolean isMusicPlaying()
    {
        return getPushSDK().isBGMPlaying();
    }

    @Override
    public boolean playMusic(String path)
    {
        if (TextUtils.isEmpty(path))
        {
            return false;
        }

        boolean result = getPushSDK().playBGM(path);
        updateViewState();
        return result;
    }

    @Override
    public boolean pauseMusic()
    {
        boolean result = getPushSDK().pauseBGM();
        updateViewState();
        return result;
    }

    @Override
    public boolean resumeMusic()
    {
        boolean result = getPushSDK().resumeBGM();
        updateViewState();
        return result;
    }

    @Override
    public boolean stopMusic()
    {
        boolean result = getPushSDK().stopBGM();
        updateViewState();
        return result;
    }

    @Override
    public long getMusicPosition()
    {
        return getPushSDK().getBGMPosition();
    }

    @Override
    public void showEffectDialog()
    {
        if (mMusicEffectDialog == null)
        {
            mMusicEffectDialog = new PushMusicEffectDialog(getActivity());
            mMusicEffectDialog.setMusicEffectCallback(this);
        }
        mMusicEffectDialog.showBottom();
    }

    @Override
    public void hideEffectDialog()
    {
        if (mMusicEffectDialog != null)
        {
            mMusicEffectDialog.dismiss();
        }
    }
}
