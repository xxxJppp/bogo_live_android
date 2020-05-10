package com.bogokj.live.appview.pagerindicator;

import android.content.Context;
import android.util.AttributeSet;

import com.bogokj.live.model.HomeTabTitleModel;
import com.bogokj.live.view.LiveShopTabUnderline;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.model.PositionData;
import com.bogokj.library.utils.SDViewUtil;

/**
 * 道具商场标题Item
 */
public class LiveShopTitleTab extends LiveShopTabUnderline implements IPagerIndicatorItem
{
    public LiveShopTitleTab(Context context)
    {
        super(context);
        init();
    }

    public LiveShopTitleTab(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private HomeTabTitleModel mData;

    private void init()
    {
//        setMinimumWidth(SDViewUtil.dp2px(70));
//        setMinimumWidth(SDViewUtil.dp2px(90));
        int padding = SDViewUtil.dp2px(10);
        getTextViewTitle().setPadding(padding, 0, padding, 0);

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
