package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.utils.ViewHolder;
import com.bogokj.live.R;
import com.bogokj.live.fragment.LiveUserHomeLeftFragment;
import com.bogokj.live.fragment.LiveUserHomeLeftFragment.ItemUserModel;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-12 上午10:15:04 类说明
 */
public class LiveUserHomeLeftAdapter extends SDSimpleAdapter<LiveUserHomeLeftFragment.ItemUserModel>
{

    public LiveUserHomeLeftAdapter(List<ItemUserModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        return R.layout.item_frag_user_home;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, ItemUserModel model)
    {
        TextView tv_key = ViewHolder.get(R.id.tv_key, convertView);
        TextView tv_value = ViewHolder.get(R.id.tv_value, convertView);
        SDViewBinder.setTextView(tv_key, model.getKey());
        SDViewBinder.setTextView(tv_value, model.getValue());
    }

}
