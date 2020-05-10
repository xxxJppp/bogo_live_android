package com.bogokj.live.appview.pagerindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.model.PositionData;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.model.HomeTabTitleModel;
import com.bogokj.live.view.LiveTabUnderline;

/**
 * 首页分类标题Item
 */
public class LiveHomeTitleSmallTab extends LiveTabUnderline implements IPagerIndicatorItem
{
    public LiveHomeTitleSmallTab(Context context)
    {
        super(context);
        init();
    }

    public LiveHomeTitleSmallTab(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private HomeTabTitleModel mData;

    private void init()
    {
//        setMinimumWidth(SDViewUtil.dp2px(70));
        setMinimumWidth(SDViewUtil.dp2px(43));
        int padding = SDViewUtil.dp2px(10);
        getTextViewTitle().setPadding(padding, 0, padding, 0);
        getTextViewTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

    }

    public void setData(HomeTabTitleModel data)
    {
        mData = data;
        if (data != null)
        {
            getTextViewTitle().setText(data.getTitle());
        }
    }

    public HomeTabTitleModel getData()
    {
        return mData;
    }

    @Override
    public void onSelectedChanged(boolean selected)
    {
        setSelected(selected);
    }

    @Override
    public void onShowPercent(float showPercent, boolean isEnter, boolean isMoveLeft)
    {

    }

    @Override
    public PositionData getPositionData()
    {
        return null;
    }
}
