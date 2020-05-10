package com.bogokj.live.adapter.viewholder;

import android.view.View;

import com.bogokj.library.adapter.SDRecyclerAdapter;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.span.LiveHeartSpan;

/**
 * 点亮
 */
public class MsgLightViewHolder extends MsgTextViewHolder
{
    public MsgLightViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgLight msg = (CustomMsgLight) customMsg;

        appendUserInfo(msg.getSender());

        // 内容
        String text = SDResourcesUtil.getString(R.string.live_light);
        int textColor = SDResourcesUtil.getColor(R.color.live_msg_text_viewer);
        appendContent(text, textColor);

        // 心
        int id = SDResourcesUtil.getIdentifierDrawable(msg.getImageName());
        if (id == 0)
        {
            id = R.drawable.heart0;
        }
        LiveHeartSpan giftSpan = new LiveHeartSpan(getAdapter().getActivity(), id);
        sb.appendSpan(giftSpan, "heart");

        setUserInfoClickListener(tv_content);
    }
}
