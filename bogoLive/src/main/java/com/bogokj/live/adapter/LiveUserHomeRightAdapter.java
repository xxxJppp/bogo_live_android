package com.bogokj.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.utils.ViewHolder;
import com.bogokj.live.R;
import com.bogokj.live.model.App_user_homeActModel;
import com.bogokj.live.model.ItemApp_user_reviewModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-13 下午5:34:09 类说明
 */
public class LiveUserHomeRightAdapter extends SDSimpleAdapter<ItemApp_user_reviewModel>
{
    public LiveUserHomeRightAdapter(List<ItemApp_user_reviewModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_frag_user_home_right_ios;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, ItemApp_user_reviewModel model)
    {
//        TextView tv_title = ViewHolder.get(R.id.tv_title, convertView);
        TextView tv_begin_time_format = ViewHolder.get(R.id.tv_begin_time_format, convertView);
        TextView tv_watch_number_format = ViewHolder.get(R.id.tv_watch_number_format, convertView);

//        SDViewBinder.setTextView(tv_title, model.getTitle());
        SDViewBinder.setTextView(tv_begin_time_format, model.getBegin_time_format());
        SDViewBinder.setTextView(tv_watch_number_format, model.getWatch_number_format());

        ImageView iv_room_image = ViewHolder.get(R.id.iv_room_image,convertView);

        GlideUtil.load(model.getLive_image()).into(iv_room_image);
    }
}
