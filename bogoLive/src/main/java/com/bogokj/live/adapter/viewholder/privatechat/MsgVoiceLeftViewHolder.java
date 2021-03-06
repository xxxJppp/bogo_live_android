package com.bogokj.live.adapter.viewholder.privatechat;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.hybrid.app.App;
import com.fanwe.lib.player.SDMediaPlayer;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgPrivateVoice;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MsgVoiceLeftViewHolder extends PrivateChatViewHolder
{
    private TextView tv_duration;
    private LinearLayout ll_voice;
    private ImageView iv_play_voice;
    private GifDrawable mGifDrawable;

    public MsgVoiceLeftViewHolder(View itemView)
    {
        super(itemView);
        tv_duration = find(R.id.tv_duration);
        ll_voice = find(R.id.ll_voice);
        iv_play_voice = find(R.id.iv_play_voice);
    }

    public int getVoiceGifRes()
    {
        return R.drawable.ic_play_voice_left_gif;
    }

    public int getVoiceStaticRes()
    {
        return R.drawable.ic_play_voice_left;
    }

    public GifDrawable getGifDrawable()
    {
        if (mGifDrawable == null)
        {
            try
            {
                mGifDrawable = new GifDrawable(App.getApplication().getResources(), getVoiceGifRes());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return mGifDrawable;
    }

    private void releaseGif()
    {
        if (mGifDrawable != null)
        {
            mGifDrawable.stop();
            mGifDrawable.recycle();
            mGifDrawable = null;
        }
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        final CustomMsgPrivateVoice msg = (CustomMsgPrivateVoice) customMsg;
        final String path = msg.getPath();

        if (SDMediaPlayer.getInstance().isPlaying()
                && !TextUtils.isEmpty(path) && path.equals(SDMediaPlayer.getInstance().getDataPath()))
        {
            if (getGifDrawable() != null)
            {
                iv_play_voice.setImageDrawable(getGifDrawable());
                getGifDrawable().start();
            }
        } else
        {
            releaseGif();
            iv_play_voice.setImageResource(getVoiceStaticRes());
        }

        tv_duration.setText(msg.getDurationFormat());
        SDViewUtil.setWidth(ll_voice, msg.getViewWidth());
        ll_voice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SDMediaPlayer.getInstance().setDataPath(path);
                SDMediaPlayer.getInstance().performPlayStop();
            }
        });
    }
}
