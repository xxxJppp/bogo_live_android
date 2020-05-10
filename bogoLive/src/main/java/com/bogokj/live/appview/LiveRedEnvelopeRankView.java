package com.bogokj.live.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;

/**
 * 打开的红包view
 * Created by luodong on 2016/9/2.
 */
public class LiveRedEnvelopeRankView extends BaseAppView
{
    private ImageView iv_envelope_open_head;
    private TextView tv_envelope_open_name;
    private ListView list_red;
    private View ll_open_close;

    public LiveRedEnvelopeRankView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveRedEnvelopeRankView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveRedEnvelopeRankView(Context context)
    {
        super(context);
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.include_live_red_envelope_rank;
    }

    @Override
    protected void onBaseInit()
    {
        super.onBaseInit();
        iv_envelope_open_head = find(R.id.iv_envelope_open_head);
        tv_envelope_open_name = find(R.id.tv_envelope_open_name);

        list_red = find(R.id.list_red);
        ll_open_close = find(R.id.ll_open_close);

    }



    /**
     * 获取ListView
     */
    public ListView getListView()
    {
        return list_red;
    }

    /**
     * 头像
     */
    public void setIvEnvelopeOpenHead(String envelopeOpenHead)
    {
        GlideUtil.loadHeadImage(envelopeOpenHead).into(iv_envelope_open_head);
    }

    /**
     * 名字
     */
    public void setTvEnvelopeOpenName(String envelopeOpenName)
    {
        SDViewBinder.setTextView(tv_envelope_open_name, envelopeOpenName);
    }



    /**
     * 头像点击监听
     *
     * @param onHeadClickListener
     */
    public void setOnHeadClickListener(OnClickListener onHeadClickListener)
    {
        iv_envelope_open_head.setOnClickListener(onHeadClickListener);
    }

    /**
     * 关闭点击监听
     *
     * @param closeClickListener
     */
    public void setCloseClickListener(OnClickListener closeClickListener)
    {
        ll_open_close.setOnClickListener(closeClickListener);
    }

}
