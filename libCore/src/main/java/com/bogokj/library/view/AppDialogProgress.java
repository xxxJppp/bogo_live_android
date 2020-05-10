package com.bogokj.library.view;

import android.app.Activity;
import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * 加载中窗口
 */
public class AppDialogProgress {

    private static QMUITipDialog waitDialog;

    public static void showWaitDialog(Context context) {
        try {
            if (waitDialog == null) {

                waitDialog = new QMUITipDialog.Builder(context)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("加载中")
                        .create();
            }

            if (!((Activity) context).isFinishing()) {

                waitDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showWaitTextDialog(Context context, String text) {
        try {
            if (waitDialog == null) {

                waitDialog = new QMUITipDialog.Builder(context)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord(text)
                        .create();
            }


            if (!((Activity) context).isFinishing()) {

                waitDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideWaitDialog() {
        try {
            if (waitDialog != null) {
                waitDialog.dismiss();
                waitDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
