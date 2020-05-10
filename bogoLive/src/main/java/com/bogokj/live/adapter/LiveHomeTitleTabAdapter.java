package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.common.SDSelectManager;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.HomeTabTitleModel;

import java.util.List;

/**
 * 用{@link com.bogokj.live.appview.pagerindicator.LiveHomeTitleTab}替代
 */
@Deprecated
public class LiveHomeTitleTabAdapter extends SDSimpleAdapter<HomeTabTitleModel>
{
    public LiveHomeTitleTabAdapter(List<HomeTabTitleModel> listModel, Activity activity)
    {
        super(listModel, activity);
        getSelectManager().setMode(SDSelectManager.Mode.SINGLE_MUST_ONE_SELECTED);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_tab_underline;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, HomeTabTitleModel model)
    {
        TextView tv_name = get(R.id.tv_name, convertView);
        View iv_underline = get(R.id.iv_underline, convertView);

        tv_name.setText(model.getTitle());
        SDViewBinder.setTextView(tv_name, model.getTitle());

        if (model.isSelected())
        {
            SDViewUtil.setVisible(iv_underline);
            tv_name.setTextColor(SDResourcesUtil.getColor(R.color.res_main_color));
        } else
        {
            SDViewUtil.setInvisible(iv_underline);
            tv_name.setTextColor(SDResourcesUtil.getColor(R.color.res_text_gray_s));
        }
    }
}
