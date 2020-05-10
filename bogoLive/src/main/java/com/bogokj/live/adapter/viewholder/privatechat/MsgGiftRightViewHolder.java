package com.bogokj.live.adapter.viewholder.privatechat;

import android.view.View;
import android.widget.TextView;

import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgPrivateGift;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.utils.GlideUtil;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MsgGiftRightViewHolder extends MsgGiftLeftViewHolder
{
    public TextView tv_score;

    public MsgGiftRightViewHolder(View itemView)
    {
        super(itemView);
        tv_score = find(R.id.tv_score);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgPrivateGift msg = (CustomMsgPrivateGift) customMsg;

        // 图片
        GlideUtil.load(msg.getProp_icon()).into(iv_gift);
        SDViewBinder.setTextView(tv_msg, msg.getFrom_msg());
        SDViewBinder.setTextView(tv_score, msg.getFrom_score());
    }
}
