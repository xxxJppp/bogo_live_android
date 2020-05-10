package cn.tillusory.tiui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import cn.tillusory.sdk.bean.TiMakeupEnum;
import cn.tillusory.tiui.R;

/**
 * Created by Anko on 2019-09-05.
 * Copyright (c) 2019 拓幻科技 - tillusory.cn. All rights reserved.
 */
public enum TiMakeup {
    NO_MAKEUP(TiMakeupEnum.NO_MAKEUP, R.drawable.ic_ti_none),
    LIP_GLOSS_MAKEUP(TiMakeupEnum.LIP_GLOSS_MAKEUP, R.drawable.ic_ti_lip_gloss),
    EYE_SHADOW_MAKEUP(TiMakeupEnum.EYE_SHADOW_MAKEUP, R.drawable.ic_ti_eye_shadow),
    BLUSHER_MAKEUP(TiMakeupEnum.BLUSHER_MAKEUP, R.drawable.ic_ti_blusher),
    BROW_PENCIL_MAKEUP(TiMakeupEnum.BROW_PENCIL_MAKEUP, R.drawable.ic_ti_brow_pencil);

    private TiMakeupEnum makeupEnum;
    private int imageId;

    TiMakeup(TiMakeupEnum makeupEnum, @DrawableRes int imageId) {
        this.makeupEnum = makeupEnum;
        this.imageId = imageId;
    }

    public TiMakeupEnum getMakeupEnum() {
        return makeupEnum;
    }

    public String getString(@NonNull Context context) {
        return context.getResources().getString(makeupEnum.getStringId());
    }

    public Drawable getImageDrawable(@NonNull Context context) {
        return context.getResources().getDrawable(imageId);
    }
}
