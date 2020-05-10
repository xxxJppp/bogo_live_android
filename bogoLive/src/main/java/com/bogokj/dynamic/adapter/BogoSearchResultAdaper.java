package com.bogokj.dynamic.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.bogokj.dynamic.activity.BogoTopPicSearchActivity;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.modle.BogoSearchModel;
import com.bogokj.live.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author kn
 * @description: 搜索历史适配器
 * @date :2019/12/17 13:53
 */
public class BogoSearchResultAdaper extends BaseQuickAdapter<BogoDynamicTopicListModel, BaseViewHolder> {

    private Context context;
    private String keyWord;

    public BogoSearchResultAdaper(Context context, @Nullable List<BogoDynamicTopicListModel> data) {
        super(R.layout.item_search_result_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BogoDynamicTopicListModel item) {
        if (TextUtils.isEmpty(item.getName())) return;
        StringInterceptionChangeRed(helper.getView(R.id.item_history_search_tiyle_tv), item.getName(), keyWord, null);
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }


    public static void StringInterceptionChangeRed(TextView numtext,
                                                   String string, String string2, String string3) {
        int fstart = string.indexOf(string2);
        if (fstart == -1) {
            numtext.setText(string);
            return;
        }

        int fend = fstart + string2.length();
        SpannableStringBuilder style = new SpannableStringBuilder(string);
        if (!"".equals(string3) && string3 != null) {
            int bstart = string.indexOf(string3);
            int bend = bstart + string3.length();
            style.setSpan(new ForegroundColorSpan(Color.RED), bstart, bend,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        numtext.setText(style);
    }
}
