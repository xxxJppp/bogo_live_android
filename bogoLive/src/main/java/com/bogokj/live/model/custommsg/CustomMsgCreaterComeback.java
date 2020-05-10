package com.bogokj.live.model.custommsg;

import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.LiveConstant.CustomMsgType;

public class CustomMsgCreaterComeback extends CustomMsg
{

	private String text = SDResourcesUtil.getString(R.string.live_creater_come_back);

	public CustomMsgCreaterComeback()
	{
		super();
		setType(CustomMsgType.MSG_CREATER_COME_BACK);
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
