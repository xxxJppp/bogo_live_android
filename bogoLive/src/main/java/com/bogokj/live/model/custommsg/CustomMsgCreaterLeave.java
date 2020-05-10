package com.bogokj.live.model.custommsg;

import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.LiveConstant.CustomMsgType;

public class CustomMsgCreaterLeave extends CustomMsg
{

	private String text = SDResourcesUtil.getString(R.string.live_creater_leave);

	public CustomMsgCreaterLeave()
	{
		super();
		setType(CustomMsgType.MSG_CREATER_LEAVE);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

}
