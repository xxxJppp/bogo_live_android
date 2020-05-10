package com.bogokj.live.adapter.viewholder;

import android.view.View;

import com.bogokj.auction.model.custommsg.CustomMsgAuctionOffer;
import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.MsgModel;

/**
 * 竞拍出价
 * Created by Administrator on 2016/9/5.
 */
public class MsgAuctionOfferViewHolder extends MsgViewHolder
{

    public MsgAuctionOfferViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {

        CustomMsgAuctionOffer msg = (CustomMsgAuctionOffer) customMsg;

        appendUserInfo(msg.getUser());

        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.res_second_color);
        appendContent(text, textColor);

        setUserInfoClickListener(tv_content, msg.getUser());
    }

}
