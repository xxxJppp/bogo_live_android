package com.bogokj.live.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.activity.LiveFollowActivity;
import com.bogokj.live.activity.LiveMyFocusActivity;
import com.bogokj.live.activity.LiveUserHeadImageActivity;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.model.UserModel;

/**
 * Created by yhz on 2017/8/28.
 */

public class LiveUserHomeCommonView extends LiveUserInfoCommonView
{
    private ImageView iv_setup;//设置
    private TextView tv_title;//标题
    public LiveUserHomeCommonView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveUserHomeCommonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveUserHomeCommonView(Context context)
    {
        super(context);
    }

    protected void init()
    {
        super.init();
        iv_setup.setVisibility(GONE);
        tv_title.setVisibility(GONE);

        setIvShareVisible(false);
        setIvReMarkVisible(false);
    }
    protected void clickFlHead()
    {
        if (mUserModel == null)
        {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveUserHeadImageActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, mUserModel.getUser_id());
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, mUserModel.getHead_image());
        getActivity().startActivity(intent);
    }
}
