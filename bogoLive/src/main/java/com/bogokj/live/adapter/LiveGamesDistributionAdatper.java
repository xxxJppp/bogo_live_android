package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.GameContributorModel;
import com.bogokj.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by shibx on 2017/0413
 */

public class LiveGamesDistributionAdatper extends SDSimpleAdapter<GameContributorModel>
{
    public LiveGamesDistributionAdatper(List<GameContributorModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_game_distridution;
    }

    @Override
    public void bindData(final int position, final View convertView, ViewGroup parent, final GameContributorModel model)
    {
        ImageView civ_v_icon = (ImageView) convertView.findViewById(R.id.civ_v_icon);
        ImageView iv_global_male = (ImageView) convertView.findViewById(R.id.iv_global_male);
        ImageView iv_rank = (ImageView) convertView.findViewById(R.id.iv_rank);

        SDViewUtil.setGone(civ_v_icon);
        SDViewUtil.setGone(iv_global_male);
        SDViewUtil.setGone(iv_rank);

        ImageView civ_head_image = (ImageView) convertView.findViewById(R.id.civ_head_image);
        TextView tv_text_sum = (TextView) convertView.findViewById(R.id.tv_text_sum);

        GlideUtil.load(model.getHead_image()).into(civ_head_image);
        SDViewBinder.setTextView(tv_text_sum, model.getSum());
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notifyItemClickCallback(position, model, convertView);
            }
        });
    }
}
