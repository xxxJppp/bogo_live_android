package com.bogokj.live.adapter.viewholder.privatechat;

import android.view.View;
import android.widget.ImageView;
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
public class MsgGiftLeftViewHolder extends PrivateChatViewHolder
{
    public ImageView iv_gift;
    public TextView tv_msg;

    public MsgGiftLeftViewHolder(View itemView)
    {
        super(itemView);
        iv_gift = find(R.id.iv_gift);
        tv_msg = find(R.id.tv_msg);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgPrivateGift msg = (CustomMsgPrivateGift) customMsg;

        // 图片
        GlideUtil.load(msg.getProp_icon()).into(iv_gift);
        SDViewBinder.setTextView(tv_msg, msg.getTo_msg());
    }
}
