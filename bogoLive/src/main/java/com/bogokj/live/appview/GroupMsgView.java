package com.bogokj.live.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveGroupChatActivity;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;

public class GroupMsgView extends BaseAppView {
    public GroupMsgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GroupMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupMsgView(Context context) {
        super(context);
        init();
    }


    protected void init() {
        setContentView(R.layout.view_group_msg);

        // 从布局文件中获取会话列表面板
        ConversationLayout conversationLayout = findViewById(R.id.conversation_layout);
        // 初始化聊天面板
        conversationLayout.initDefault();
        conversationLayout.getTitleBar().setVisibility(GONE);

        conversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo messageInfo) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(messageInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
                chatInfo.setId(messageInfo.getId());
                chatInfo.setChatName(messageInfo.getTitle());
                Intent intent = new Intent(getActivity(), LiveGroupChatActivity.class);
                intent.putExtra("CHAT_INFO", chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        findViewById(R.id.rl_service).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(InitActModelDao.query().getService_id());
                chatInfo.setChatName("系统客服");
                Intent intent = new Intent(getActivity(), LiveGroupChatActivity.class);
                intent.putExtra("CHAT_INFO", chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });
    }
}
