package com.bogokj.pay.dialog;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.xianrou.widget.QKDiamondTextView;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;

/**
 * Created by Administrator on 2016/12/16.
 */

public class LiveImportPriceDialog extends SDDialogBase
{
    private EditText et_diamond;
    private int live_pay_max;
    private int live_pay_min;
    private ImageView close;
    private TextView tv_confirm;
    private ISDDialogConfirm.Callback callback;

    public int getLive_pay_min()
    {
        return live_pay_min;
    }

    public LiveImportPriceDialog(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
        setCancelable(false);
        setDismissAfterClick(false);
        setContentView(R.layout.dialog_pay_pay_live_content);
        paddings(0);
        findView();
        initInputLimit();
        register();
    }

    private void findView()
    {
        et_diamond = (EditText) findViewById(R.id.et_diamond);
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != callback) {
                    callback.onClickCancel(v, LiveImportPriceDialog.this);
                }
            }
        });

        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != callback) {
                    callback.onClickConfirm(v, LiveImportPriceDialog.this);
                }
            }
        });

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("按时收费");

        QKDiamondTextView tv_diamonds_name = findViewById(R.id.tv_diamonds_name);
        tv_diamonds_name.setText("$/分钟)");

        TextView tv_unit = findViewById(R.id.tv_unit);
        tv_unit.setText("/每分钟");
    }

    private void initInputLimit()
    {
        live_pay_max = AppRuntimeWorker.getLivePayMax();
        live_pay_min = AppRuntimeWorker.getLivePayMin();

        String text_center = live_pay_min + "<=价格";
        if (live_pay_max > 0)
        {
            text_center = text_center + "<=" + live_pay_max;
        }
        et_diamond.setHint(text_center);
    }

    private void register()
    {
        et_diamond.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = s.toString();
                if (!TextUtils.isEmpty(text))
                {
                    int int_text = Integer.valueOf(text);
                    if (int_text == 0)
                    {
                        et_diamond.setText("1");
                        et_diamond.setSelection(1);
                        return;
                    }

                    if (live_pay_max > 0)
                    {
                        if (int_text > live_pay_max)
                        {
                            String s_text = String.valueOf(live_pay_max);
                            et_diamond.setText(s_text);
                            et_diamond.setSelection(s_text.length());
                            return;
                        }
                    }
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    //获取输入框价格
    public int getImportPrice()
    {
        int price = 1;
        if (et_diamond != null)
        {
            String diamond = et_diamond.getText().toString();
            if (!TextUtils.isEmpty(diamond))
            {
                int int_diamond = Integer.valueOf(diamond);
                if (int_diamond > 0)
                {
                    return int_diamond;
                }
            }
        }
        return price;
    }

    /**
     * 重置最低价格
     */
    public void resetMinPrice()
    {
        et_diamond.setText(Integer.toString(live_pay_min));
        et_diamond.setSelection(Integer.toString(live_pay_min).length());
    }

    public void setCallback(ISDDialogConfirm.Callback callback) {
       this.callback = callback;
    }
}
