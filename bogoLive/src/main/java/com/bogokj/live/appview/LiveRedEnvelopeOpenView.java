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
public class LiveRedEnvelopeOpenView extends BaseAppView
{
    private TextView tv_info;
    private TextView tv_user_red_envelope;
    private View ll_open_close;

    public LiveRedEnvelopeOpenView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveRedEnvelopeOpenView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveRedEnvelopeOpenView(Context context)
    {
        super(context);
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.include_live_red_envelope_open;
    }

    @Override
    protected void onBaseInit()
    {
        super.onBaseInit();
        tv_info = find(R.id.tv_info);
        tv_user_red_envelope = find(R.id.tv_user_red_envelope);
        ll_open_close = find(R.id.ll_open_close);

    }

    /**
     * 设置视图的显示与隐藏
     */
    public void setViewShowOrHide()
    {
        SDViewUtil.setGone(tv_user_red_envelope);
    }

    /**
     * 信息
     */
    public void setTvInfo(String info)
    {
        if (!TextUtils.isEmpty(info))
        {
            SDViewBinder.setTextView(tv_info, info);
        }
    }

    /**
     * 查看手气点击监听
     *
     * @param onUserClickListener
     */
    public void setOnUserClickListener(OnClickListener onUserClickListener)
    {
        tv_user_red_envelope.setOnClickListener(onUserClickListener);
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
