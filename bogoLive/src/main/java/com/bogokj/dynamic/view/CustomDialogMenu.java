package com.bogokj.dynamic.view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;

import java.util.Arrays;
import java.util.List;

/**
 * <p>文件描述：<p>
 * <p>创建时间：2019/8/22 0022<p>
 * <p>更改时间：2019/8/22 0022<p>
 */
public class CustomDialogMenu extends SDDialogBase implements ISDDialogMenu {
    public TextView tv_title;
    public TextView tv_cancel;
    public ListView lv_content;
    private List<Object> mListModel;
    private Callback mCallback;
    private BaseAdapter mInternalAdapter = new BaseAdapter() {
        public int getCount() {
            return CustomDialogMenu.this.mListModel != null && !CustomDialogMenu.this.mListModel.isEmpty() ? CustomDialogMenu.this.mListModel.size() : 0;
        }

        public Object getItem(int position) {
            return CustomDialogMenu.this.getModel(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(CustomDialogMenu.this.getContext()).inflate(R.layout.custom_dialog_item_dialog_menu, parent, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.tv_content);
            Object object = this.getItem(position);
            if (object != null) {
                textView.setText(String.valueOf(object));
            }

            return convertView;
        }
    };

    public CustomDialogMenu(Activity activity) {
        super(activity);
        this.init();
    }

    private void init() {
        this.setContentView(R.layout.custom_dialog_dialog_menu);
        this.tv_title = (TextView) this.findViewById(R.id.tv_title);
        this.tv_cancel = (TextView) this.findViewById(R.id.tv_cancel);
        this.lv_content = (ListView) this.findViewById(R.id.lv_content);
        this.tv_cancel.setOnClickListener(this);
        this.setTextTitle((String) null);
        this.setCanceledOnTouchOutside(true);
    }

    public CustomDialogMenu setTextTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            this.tv_title.setVisibility(View.GONE);
        } else {
            this.tv_title.setVisibility(View.VISIBLE);
            this.tv_title.setText(text);
        }

        return this;
    }

    public CustomDialogMenu setTextCancel(String text) {
        if (TextUtils.isEmpty(text)) {
            this.tv_cancel.setVisibility(View.GONE);
        } else {
            this.tv_cancel.setVisibility(View.VISIBLE);
            this.tv_cancel.setText(text);
        }

        return this;
    }

    public CustomDialogMenu setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    public CustomDialogMenu setItems(Object... objects) {
        List<Object> listObject = null;
        if (objects != null) {
            listObject = Arrays.asList(objects);
        }

        this.setItems(listObject);
        return this;
    }

    public CustomDialogMenu setItems(List<Object> listObject) {
        this.mListModel = listObject;
        this.setAdapter(this.getAdapter());
        return this;
    }

    public CustomDialogMenu setAdapter(BaseAdapter adapter) {
        this.lv_content.setAdapter(adapter);
        this.lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CustomDialogMenu.this.mCallback != null) {
                    CustomDialogMenu.this.mCallback.onClickItem(view, (int) id, CustomDialogMenu.this);
                }

                CustomDialogMenu.this.dismissAfterClickIfNeed();
            }
        });
        return this;
    }

    protected BaseAdapter getAdapter() {
        return this.mInternalAdapter;
    }

    private Object getModel(int position) {
        return this.mListModel != null && !this.mListModel.isEmpty() && position >= 0 && position < this.mListModel.size() ? this.mListModel.get(position) : null;
    }

    public int getDefaultPadding() {
        return 0;
    }

    public void onClick(View v) {
        if (v == this.tv_cancel) {
            if (this.mCallback != null) {
                this.mCallback.onClickCancel(v, this);
            }

            this.dismissAfterClickIfNeed();
        }

    }
}