package com.bogokj.live.utils;

import android.text.TextUtils;

import com.bogokj.hybrid.constant.ApkConstant;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.library.listener.SDResultCallback;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LiveUtils {

    public static String formatUnreadNumber(long number) {
        if (number > 99) {
            return "99+";
        } else {
            return String.valueOf(number);
        }
    }

    public static int getLevelImageResId(int level) {
        int resId = 0;
        try {
            String imageName = String.valueOf("rank_" + level);
            resId = SDResourcesUtil.getIdentifierDrawable(imageName);
        } catch (Exception e) {
            resId = R.drawable.nopic_expression;
        }
        return resId;
    }

    public static int getSexImageResId(int sex) {
        int resId = 0;
        switch (sex) {
            case 0:

                break;
            case 1:
                resId = R.drawable.ic_global_male;
                break;
            case 2:
                resId = R.drawable.ic_global_female;
                break;
            default:
                break;
        }
        return resId;
    }

    public static String getFormatNumber(int number) {
        double result;
        DecimalFormat format = new DecimalFormat("#.00");
        if (number >= 10000) {
            result = number;
            result = result / 10000;
            return format.format(result) + "万";
        }
        return String.valueOf(number);
    }


    public static String getFormatNumber(long number) {
        double result;
        DecimalFormat format = new DecimalFormat("#.00");
        if (number >= 10000) {
            result = number;
            result = result / 10000;
            return format.format(result) + "万";
        }
        return String.valueOf(number);
    }

    private static long lastUpdateAeskey = 0;

    /**
     * 更新aeskey
     *
     * @param isCalledByDecryptFail
     * @param callback
     */
    public static void updateAeskey(boolean isCalledByDecryptFail, final SDResultCallback<String> callback) {
        if (isCalledByDecryptFail && System.currentTimeMillis() - lastUpdateAeskey < 5000) {
            LogUtil.e("updateAeskey too fast");
            if (callback != null) {
                callback.onError(-1, "updateAeskey too fast");
            }
            return;
        }
        InitActModel actModel = InitActModelDao.query();
        if (actModel == null) {
            LogUtil.e("updateAeskey InitActModel is null");
            if (callback != null) {
                callback.onError(-1, "updateAeskey InitActModel is null");
            }
            return;
        }

        final String groupId = actModel.getFull_group_id();
        if (TextUtils.isEmpty(groupId)) {
            LogUtil.e("updateAeskey groupId is empty");
            if (callback != null) {
                callback.onError(-1, "updateAeskey groupId is empty");
            }
            return;
        }

        final List<String> listGroup = new ArrayList<>();
        listGroup.add(groupId);
        lastUpdateAeskey = System.currentTimeMillis();
        TIMGroupManager.getInstance().getGroupInfo(listGroup, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(s);
                if (callback != null) {
                    callback.onError(i, s);
                }
            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfoResult> listInfo) {
                if (listInfo != null && !listInfo.isEmpty()) {
                    TIMGroupDetailInfo info = listInfo.get(0);
                    String aesKey = info.getGroupIntroduction();
                    if (!TextUtils.isEmpty(aesKey)) {
                        ApkConstant.setAeskeyHttp(aesKey);
                        if (callback != null) {
                            callback.onSuccess(aesKey);
                        }
                        LogUtil.i("updateAeskey success:" + aesKey);
                    } else {
                        LogUtil.e("updateAeskey GroupIntroduction is empty");
                        if (callback != null) {
                            callback.onError(-1, "updateAeskey GroupIntroduction is empty");
                        }
                    }
                } else {
                    LogUtil.e("updateAeskey TIMGroupDetailInfo is empty");
                    if (callback != null) {
                        callback.onError(-1, "updateAeskey TIMGroupDetailInfo is empty");
                    }
                }
            }
        });
    }
}
