package com.tencent.qcloud.xiaoshipin.videorecord;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import com.bogokj.hybrid.app.App;
import com.bogokj.live.control.LiveBeautyType;
import com.bogokj.live.model.LiveBeautyConfig;
import com.bogokj.live.model.LiveBeautyFilterModel;
import com.bogokj.library.utils.LogUtil;
import com.tencent.ugc.TXUGCRecord;

public class VideoBeautyUtils {

    private static VideoBeautyUtils sInstance;

    private LiveBeautyConfig mBeautyConfig;
    private TXUGCRecord mTXCameraRecord;

    public VideoBeautyUtils(TXUGCRecord txugcRecord) {
        this.mTXCameraRecord = txugcRecord;
    }

    public static VideoBeautyUtils getInstance(TXUGCRecord txugcRecord) {
        if (sInstance == null) {
            synchronized (VideoBeautyUtils.class) {
                if (sInstance == null) {
                    sInstance = new VideoBeautyUtils(txugcRecord);
                }
            }
        }
        return sInstance;
    }


    //----------IPushSDK implements start----------

    public void initBeauty() {

        LiveBeautyConfig config = getBeautyConfig();

        int beauty = 0;
        int whiten = 0;
        beauty = config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress();
        whiten = config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress();


        updateBeautyProgress(LiveBeautyType.BEAUTY, beauty);
        updateBeautyProgress(LiveBeautyType.WHITEN, whiten);

    }

    public void enableBeauty(boolean enable) {
        LiveBeautyConfig config = getBeautyConfig();

        int beauty = 0;
        int whiten = 0;
        if (enable) {
            beauty = config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress();
            whiten = config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress();
        }

        updateBeautyProgress(LiveBeautyType.BEAUTY, beauty);
        updateBeautyProgress(LiveBeautyType.WHITEN, whiten);
    }

    /**
     * 更新美颜百分比
     *
     * @param type     {@link LiveBeautyType}
     * @param progress [0-100]
     */
    public void updateBeautyProgress(int type, int progress) {
        LiveBeautyConfig config = getBeautyConfig();

        int style = 0; //0 ：光滑 1：自然 2：朦胧
        int beauty = getRealProgress(config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress());
        int whiten = getRealProgress(config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress());
        int ruddy = 0;

        switch (type) {
            case LiveBeautyType.BEAUTY:
                beauty = getRealProgress(progress);
                mTXCameraRecord.setBeautyDepth(style, beauty, whiten, ruddy);
                break;
            case LiveBeautyType.WHITEN:
                whiten = getRealProgress(progress);
                mTXCameraRecord.setBeautyDepth(style, beauty, whiten, ruddy);
                break;
            default:
                break;
        }

        LogUtil.i("beauty updateBeautyProgress:" + config.getBeautyTypeModel(type).getName() + " " + progress);
    }

    /**
     * 更新美颜滤镜
     *
     * @param model
     */
    public void updateBeautyFilter(LiveBeautyFilterModel model) {
        setBeautyFilter(model.getImageResId());
        LogUtil.i("beauty updateBeautyFilter:" + model.getName());
        updateBeautyFilterProgress(model.getProgress());
    }

    /**
     * 更新美颜滤镜百分比
     *
     * @param progress [0-100]
     */
    public void updateBeautyFilterProgress(int progress) {
        float value = progress / 100f;
        mTXCameraRecord.setSpecialRatio(value);

        LogUtil.i("beauty updateBeautyFilterProgress:" + progress);
    }

    private void setBeautyFilter(int imgResId) {
        if (mTXCameraRecord == null) {
            return;
        }

        if (imgResId == 0) {
            mTXCameraRecord.setFilter(null);
        } else {
            Resources resources = App.getApplication().getResources();
            TypedValue value = new TypedValue();
            resources.openRawResource(imgResId, value);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTargetDensity = value.density;
            Bitmap bmp = BitmapFactory.decodeResource(resources, imgResId, opts);
            mTXCameraRecord.setFilter(bmp);
        }
    }

    public LiveBeautyConfig getBeautyConfig() {
        if (mBeautyConfig == null) {
            mBeautyConfig = LiveBeautyConfig.get();
        }
        return mBeautyConfig;
    }

    /**
     * 真实值转换
     *
     * @param progress [0-100]
     * @return [0-10]
     */
    public static int getRealProgress(int progress) {
        float value = ((float) progress / 100) * 10;
        return (int) value;
    }

}
