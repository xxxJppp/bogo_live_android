package com.bogokj.library.adapter.iml;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogokj.library.R;
import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;

import java.util.List;

public class SDSimpleTextAdapter<T> extends SDSimpleAdapter<T>
{
    public SDSimpleTextAdapter(List<T> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_simple_text;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, T model)
    {
        TextView tvName = get(R.id.item_simple_text_tv_name, convertView);
        SDViewBinder.setTextView(tvName, model.toString());
    }
}
