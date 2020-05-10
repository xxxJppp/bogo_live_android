package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.ExchangeModel;

import java.util.List;

/**
 * Created by shibx on 2016/7/19.
 */
public class LiveUserProfitExchangeAdapter extends SDSimpleAdapter<ExchangeModel>{

    public LiveUserProfitExchangeAdapter(List<ExchangeModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_live_exchange_rule;
    }

    @Override
    public void bindData(final int position, final View convertView, ViewGroup parent, final ExchangeModel model) {
        TextView tv_diamonds = get(R.id.tv_diamonds,convertView);
        TextView tv_currency = get(R.id.tv_currency,convertView);
        tv_diamonds.setText(String.valueOf(model.getDiamonds()));
        tv_currency.setText(String.valueOf(model.getTicket()) + AppRuntimeWorker.getTicketName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemClickCallback(position, model, v);
            }
        });
    }
}
