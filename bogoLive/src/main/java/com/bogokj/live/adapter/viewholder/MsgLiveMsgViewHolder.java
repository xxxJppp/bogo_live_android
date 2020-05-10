package com.bogokj.live.adapter.viewholder;

import android.view.View;

import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgLiveMsg;
import com.bogokj.live.model.custommsg.MsgModel;

/**
 * 直播消息
 */
public class MsgLiveMsgViewHolder extends MsgViewHolder
{
    public MsgLiveMsgViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgLiveMsg msg = (CustomMsgLiveMsg) customMsg;

        //title
        String title = SDResourcesUtil.getString(R.string.live_msg_title);
        int titleColor = SDResourcesUtil.getColor(R.color.live_msg_title);
        appendContent(title, titleColor);

        // 内容
        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.live_msg_content);
        appendContent(text, textColor);
    }
}
