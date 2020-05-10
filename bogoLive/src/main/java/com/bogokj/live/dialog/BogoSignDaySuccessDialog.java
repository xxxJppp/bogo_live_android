package com.bogokj.live.dialog;

import android.app.Activity;
import android.view.View;

import com.bogokj.live.R;
import com.fanwe.lib.dialog.impl.SDDialogBase;

import org.xutils.x;

public class BogoSignDaySuccessDialog extends SDDialogBase {


    public BogoSignDaySuccessDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_sign_day_success);
        paddings(0);
        x.view().inject(this, getContentView());

        findViewById(R.id.tv_sign_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_ok:
                dismiss();
                break;
        }
    }
}