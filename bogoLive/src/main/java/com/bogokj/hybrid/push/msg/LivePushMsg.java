package com.bogokj.hybrid.push.msg;

import com.bogokj.hybrid.push.msg.PushMsg;

public class LivePushMsg extends PushMsg
{
	private int room_id;

	public int getRoom_id()
	{
		return room_id;
	}

	public void setRoom_id(int room_id)
	{
		this.room_id = room_id;
	}

}
