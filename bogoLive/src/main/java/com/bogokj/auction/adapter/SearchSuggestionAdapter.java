package com.bogokj.auction.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.live.R;
import com.tencent.lbssearch.object.result.SuggestionResultObject;

import java.util.List;

public class SearchSuggestionAdapter extends SDSimpleAdapter<SuggestionResultObject.SuggestionData>
{

	public SearchSuggestionAdapter(List<SuggestionResultObject.SuggestionData> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		return R.layout.item_search_suggestion;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent, SuggestionResultObject.SuggestionData model)
	{
		TextView tv_title = get(R.id.tv_title, convertView);
		TextView tv_content = get(R.id.tv_content, convertView);

		SDViewBinder.setTextView(tv_title, model.title);
		SDViewBinder.setTextView(tv_content, model.district);
	}

}
