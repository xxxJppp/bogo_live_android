package com.bogokj.xianrou.interfaces;

import android.view.View;

import com.bogokj.xianrou.model.XRSimplePhotoModel;

/**
 * @包名 com.bogokj.xianrou.callback
 * @描述
 * @作者 Su
 * @创建时间 2017/3/21 14:46
 **/
public interface XRPhotoNormalViewHolderCallback
{
    void onPhotoDeleteClick(View view, XRSimplePhotoModel photo, int position);

    void onPhotoClick(View view, XRSimplePhotoModel photo, int position);
}
