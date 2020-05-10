package com.bogokj.live.model.custommsg;

import com.bogokj.library.utils.LogUtil;
import com.bogokj.live.LiveConstant.CustomMsgType;
import com.bogokj.live.common.AppRuntimeWorker;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;

public class CustomMsgText extends CustomMsg
{

    private String text;

    public CustomMsgText()
    {
        super();
        setType(CustomMsgType.MSG_TEXT);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }


    @Override
    public TIMMessage parseToTIMMessage()
    {
        TIMMessage timMessage = super.parseToTIMMessage();
        if (AppRuntimeWorker.getHas_dirty_words() == 1)
        {
            if (timMessage != null)
            {
                TIMTextElem textElem = new TIMTextElem();
                textElem.setText(text);
                int ret = timMessage.addElement(textElem);
                LogUtil.i("CustomMsgText add TIMTextElem:" + ret);
            }
        }
        return timMessage;
    }
}
