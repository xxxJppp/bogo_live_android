package com.bogokj.live.span;

import com.bogokj.library.utils.SDViewUtil;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class LiveMsgRedEnvelopeSpan extends SDNetImageSpan
{

	public LiveMsgRedEnvelopeSpan(View view)
	{
		super(view);
		setWidth(SDViewUtil.dp2px(30));
	}

}
