package com.bogokj.live.adapter.viewholder;

import android.view.View;

import com.bogokj.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.MsgModel;

/**
 * 竞拍成功
 * Created by Administrator on 2016/9/6.
 */
public class MsgAuctionSucViewHolder extends MsgViewHolder
{

    public MsgAuctionSucViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgAuctionSuccess msg = (CustomMsgAuctionSuccess) customMsg;
        appendUserInfo(msg.getUser());
        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.res_second_color);
        appendContent(text, textColor);
        setUserInfoClickListener(tv_content, msg.getUser());
    }

}
