package com.bogokj.live.adapter.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgForbidSendMsg;
import com.bogokj.live.model.custommsg.MsgModel;

/**
 * 禁言
 */
public class MsgForbidSendMsgViewHolder extends MsgTextViewHolder
{
    public MsgForbidSendMsgViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgForbidSendMsg msg = (CustomMsgForbidSendMsg) customMsg;

        // 标题
        String title = SDResourcesUtil.getString(R.string.live_msg_title);
        int titleColor = SDResourcesUtil.getColor(R.color.live_msg_title);
        appendContent(title, titleColor);

        // 内容
        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.live_msg_content);
        String color = msg.getFonts_color();
        if (!TextUtils.isEmpty(color))
        {
            textColor = Color.parseColor(color);
        }
        appendContent(text, textColor);
    }
}
