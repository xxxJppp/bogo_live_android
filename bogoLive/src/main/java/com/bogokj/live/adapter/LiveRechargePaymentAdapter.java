package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.live.R;
import com.bogokj.live.model.PayItemModel;
import com.bogokj.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LiveRechargePaymentAdapter extends SDSimpleAdapter<PayItemModel>
{
    public LiveRechargePaymentAdapter(List<PayItemModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_recharge_payment;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final PayItemModel model)
    {
        ImageView iv_image = get(R.id.iv_image, convertView);
        TextView tv_name = get(R.id.tv_name, convertView);
        ImageView iv_selected = get(R.id.iv_selected, convertView);

        GlideUtil.load(model.getLogo()).into(iv_image);
        SDViewBinder.setTextView(tv_name, model.getName());
        iv_selected.setSelected(model.isSelected());

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyItemClickCallback(position, model, v);
            }
        });
    }

}
