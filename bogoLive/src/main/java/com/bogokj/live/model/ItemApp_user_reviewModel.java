package com.bogokj.live.model;

import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-12 下午8:08:06 类说明
 */
public class ItemApp_user_reviewModel
{
	private int id;
	private String title;
	private String begin_time_format;
	private String watch_number_format;
	private String user_id;
	private String live_image;

	public String getLive_image() {
		return live_image;
	}

	public void setLive_image(String live_image) {
		this.live_image = live_image;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getColor(int position)
	{
		int pos = position + 1;
		if (pos % 4 == 1)
		{
			return SDResourcesUtil.getColor(R.color.live_item_user_home_right_one);
		} else if (pos % 4 == 2)
		{
			return SDResourcesUtil.getColor(R.color.live_item_user_home_right_two);
		} else if (pos % 4 == 3)
		{
			return SDResourcesUtil.getColor(R.color.live_item_user_home_right_three);
		} else if (pos % 4 == 0)
		{
			return SDResourcesUtil.getColor(R.color.live_item_user_home_right_four);
		} else
		{
			return SDResourcesUtil.getColor(R.color.live_item_user_home_right_one);
		}
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}



	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBegin_time_format()
	{
		return begin_time_format;
	}

	public void setBegin_time_format(String begin_time_format)
	{
		this.begin_time_format = begin_time_format;
	}

	public String getWatch_number_format()
	{
		return watch_number_format;
	}

	public void setWatch_number_format(String watch_number_format)
	{
		this.watch_number_format = watch_number_format;
	}

}
