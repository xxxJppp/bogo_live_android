package com.bogokj.live.adapter.viewholder;

import android.view.View;

import com.bogokj.auction.model.custommsg.CustomMsgAuctionFail;
import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.MsgModel;

/**
 * 流拍
 * Created by Administrator on 2016/9/6.
 */
public class MsgAuctionFailViewHolder extends MsgViewHolder{

    public MsgAuctionFailViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg) {
        CustomMsgAuctionFail msg = (CustomMsgAuctionFail) customMsg;
//        appendUserInfo(msg.getUser());

        //title
        String title = SDResourcesUtil.getString(R.string.live_msg_auction_title);
        int titleColor = SDResourcesUtil.getColor(R.color.live_msg_title);
        appendContent(title, titleColor);

        // 内容
        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.res_second_color);
        appendContent(text, textColor);

        setUserInfoClickListener(tv_content, msg.getUser());
    }

}
