package com.bogokj.live.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.live.R;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;

import org.xutils.x;

import java.util.HashMap;

public class LiveGroupChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_chat_group);
        x.view().inject(this);


        // 从布局文件中获取聊天面板
        ChatLayout chatLayout = findViewById(R.id.chat_layout);
        // 单聊面板的默认 UI 和交互初始化
        chatLayout.initDefault();
        // 传入 ChatInfo 的实例，这个实例必须包含必要的聊天信息，一般从调用方传入
        chatLayout.getInputLayout().disableAudioInput(true);
        chatLayout.getInputLayout().disableMoreInput(true);
        chatLayout.getTitleBar().getRightGroup().setVisibility(View.GONE);

        ChatInfo chatInfo = (ChatInfo) getIntent().getSerializableExtra("CHAT_INFO");
        chatLayout.setChatInfo(chatInfo);
    }

}
