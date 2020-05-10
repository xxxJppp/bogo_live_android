package com.bogokj.dynamic.audiorecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bogokj.dynamic.activity.AudioRecordActivity;
import com.bogokj.dynamic.audiorecord.entity.EnterRecordAudioEntity;


/**
 * Created by chenxf on 17-7-6.
 */

public class AudioRecordJumpUtil {

    /**
     * 跳转录制语音页面
     */
    public static void startRecordAudio(Context context) {
        EnterRecordAudioEntity entity = new EnterRecordAudioEntity();
        entity.setSourceType(EnterRecordAudioEntity.SourceType.AUDIO_FEED);

        Intent intent = new Intent(context, AudioRecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AudioRecordActivity.KEY_ENTER_RECORD_AUDIO_ENTITY, entity);
        intent.putExtra(AudioRecordActivity.KEY_AUDIO_BUNDLE, bundle);
        context.startActivity(intent);
    }
}
