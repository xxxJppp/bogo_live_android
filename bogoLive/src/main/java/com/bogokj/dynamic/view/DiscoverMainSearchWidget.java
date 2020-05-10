package com.bogokj.dynamic.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.R;

/**
 * 发现首页搜索框自定义控件
 */
public class DiscoverMainSearchWidget extends LinearLayout implements View.OnClickListener {
    private EditText keyInput;  //关键字输入框
    private ImageView keyDelete;    //清除输入框图标
    //private Button backBnt; //返回按钮
    //private Spinner customSearchType;
    //private ListView tipsList;  //提示列表
    private Context context;
    //private CustomSearchHintAdapter customSearchHintAdapter;//提示适配器
    //private CustomSearchCompleteAdapter customSearchCompleteAdapter;    //补全适配器
    private CustomSearchListener customSearchListener;  //回调接口

    public DiscoverMainSearchWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(this.context).inflate(R.layout.discover_search_widget, this);
        initViews();
    }

    public void setCustomSearchListener(CustomSearchListener listener) {
        this.customSearchListener = listener;
    }

    public void setKeyInput(String s) {
        keyInput.setText(s);
    }

    private void initViews() {
        keyInput = (EditText) findViewById(R.id.customSearchKey);
        keyDelete = (ImageView) findViewById(R.id.circle_search_delete);
        //backBnt = (Button) findViewById(R.id.customSearchRebnt);
        //customSearchType = (Spinner) findViewById(customSearchType);
        //tipsList = (ListView) findViewById(R.id.customSearchTips);
        keyDelete.setOnClickListener(this);
        keyDelete.setVisibility(View.GONE);
        //backBnt.setOnClickListener(this);
        keyInput.addTextChangedListener(new EditChangeListener());
        keyInput.setOnClickListener(this);


        keyInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //tipsList.setVisibility(GONE);
                    notifyStartSearching(keyInput.getText().toString());
                }
                return true;
            }
        });

    }

    public String getInputKey() {
        if (null != keyInput) {
            return keyInput.getText().toString();
        }
        return "";
    }

    /**
     * 通知监听，进行搜索
     *
     * @param key
     */
    private void notifyStartSearching(String key) {
        if (null != customSearchListener) {
            customSearchListener.onSearch(keyInput.getText().toString());
            //customSearchListener.onSearch(keyInput.getText().toString(), customSearchType.getSelectedItem().toString());
        }
        //如果输入法打开则关闭，如果没打开则打开
        InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 回调接口
     */
    public interface CustomSearchListener {
        //更新自动补全内容
        //void onRefreshAutoComplete(String key);
        //开始搜索
        void onSearch(String key);

        void onDelete();

        void onInputChange(String key);

    }

    /**
     * 字符变化监听
     */
    private class EditChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            customSearchListener.onInputChange(charSequence.toString());
//            if (!"".equals(charSequence.toString())) {
////                keyDelete.setVisibility(VISIBLE);
//                customSearchListener.onInputChange(charSequence.toString());
//
//            } else {
//                keyDelete.setVisibility(GONE);
//            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_search_delete:
                keyInput.setText("");
                keyDelete.setVisibility(GONE);
                customSearchListener.onDelete();
                break;
        }
    }

}
