package com.bogokj.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bogokj.live.R;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.dialog.impl.SDDialogConfirm;

/**
 * @author kn create
 * @description:
 * @date : 2020/2/27
 */
public class SDDialogNew extends SDDialogBase implements ISDDialogConfirm {
    public TextView tv_title;
    public FrameLayout fl_content;
    public TextView tv_content;
    public TextView tv_confirm;
    public TextView tv_cancel;
    private Callback mCallback;

    public SDDialogNew(Activity activity) {
        super(activity);
        this.init();
    }

    private void init() {
        this.setContentView(R.layout.lib_dialog_dialog_confirm);
        this.tv_title = (TextView) this.findViewById(R.id.tv_title);
        this.fl_content = (FrameLayout) this.findViewById(R.id.fl_content);
        this.tv_content = (TextView) this.findViewById(R.id.tv_content);
        this.tv_confirm = (TextView) this.findViewById(R.id.tv_confirm);
        this.tv_cancel = (TextView) this.findViewById(R.id.tv_cancel);
        this.tv_confirm.setOnClickListener(this);
        this.tv_cancel.setOnClickListener(this);
    }

    public SDDialogNew setCustomView(int layoutId) {
        this.fl_content.removeAllViews();
        LayoutInflater.from(this.getContext()).inflate(layoutId, this.fl_content, true);
        return this;
    }

    public SDDialogNew setCustomView(View view) {
        this.fl_content.removeAllViews();
        this.fl_content.addView(view);
        return this;
    }

    public SDDialogNew setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    public SDDialogNew setTextTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            this.tv_title.setVisibility(View.GONE);
        } else {
            this.tv_title.setVisibility(View.VISIBLE);
            this.tv_title.setText(text);
        }

        return this;
    }

    public SDDialogNew setTextContent(String text) {
        if (TextUtils.isEmpty(text)) {
            this.tv_content.setVisibility(View.GONE);
        } else {
            this.tv_content.setVisibility(View.VISIBLE);
            this.tv_content.setText(text);
        }

        return this;
    }

    public SDDialogNew setTextConfirm(String text) {
        if (TextUtils.isEmpty(text)) {
            this.tv_confirm.setVisibility(View.GONE);
        } else {
            this.tv_confirm.setVisibility(View.VISIBLE);
            this.tv_confirm.setText(text);
        }

        this.changeBottomButtonIfNeed();
        return this;
    }

    public SDDialogNew setTextCancel(String text) {
        if (TextUtils.isEmpty(text)) {
            this.tv_cancel.setVisibility(View.GONE);
        } else {
            this.tv_cancel.setVisibility(View.VISIBLE);
            this.tv_cancel.setText(text);
        }

        this.changeBottomButtonIfNeed();
        return this;
    }

    public SDDialogNew setTextColorTitle(int color) {
        this.tv_title.setTextColor(color);
        return this;
    }

    public SDDialogNew setTextColorContent(int color) {
        this.tv_content.setTextColor(color);
        return this;
    }

    public SDDialogNew setTextColorConfirm(int color) {
        this.tv_confirm.setTextColor(color);
        return this;
    }

    public SDDialogNew setTextColorCancel(int color) {
        this.tv_cancel.setTextColor(color);
        return this;
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v == this.tv_confirm) {
            if (this.mCallback != null) {
                this.mCallback.onClickConfirm(v, this);
            }

            this.dismissAfterClickIfNeed();
        } else if (v == this.tv_cancel) {
            if (this.mCallback != null) {
                this.mCallback.onClickCancel(v, this);
            }

            this.dismissAfterClickIfNeed();
        }

    }

    protected void changeBottomButtonIfNeed() {
        if (this.tv_cancel.getVisibility() == View.VISIBLE && this.tv_confirm.getVisibility() == View.VISIBLE) {
            setBackgroundDrawable(this.tv_cancel, this.getContext().getResources().getDrawable(R.drawable.lib_dialog_sel_bg_button_bottom_left));
            setBackgroundDrawable(this.tv_confirm, this.getContext().getResources().getDrawable(R.drawable.lib_dialog_sel_bg_button_bottom_right));
        } else if (this.tv_cancel.getVisibility() == View.VISIBLE) {
            setBackgroundDrawable(this.tv_cancel, this.getContext().getResources().getDrawable(R.drawable.lib_dialog_sel_bg_button_bottom_single));
        } else if (this.tv_confirm.getVisibility() == View.VISIBLE) {
            setBackgroundDrawable(this.tv_confirm, this.getContext().getResources().getDrawable(R.drawable.lib_dialog_sel_bg_button_bottom_single));
        }

    }
}
