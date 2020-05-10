/**
 *
 */
package com.bogokj.live.model;

import java.util.List;

import com.bogokj.hybrid.model.BaseActModel;

/**
 *
 * @author Administrator
 * @date 2016-5-17 下午6:54:44
 */
public class App_propActModel extends BaseActModel
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<LiveGiftTypeModel> list;

	public List<LiveGiftTypeModel> getList()
	{
		return list;
	}

	public void setList(List<LiveGiftTypeModel> list)
	{
		this.list = list;
	}

}
