package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveChatC2CNewView;
import com.bogokj.live.model.LiveConversationListModel;

/**
 * Created by Administrator on 2016/7/18.
 */
public class LiveChatC2CActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_chat_c2c);
        init();
    }

    /**
     * 加载聊天fragment
     */
    private void init() {
        LiveChatC2CNewView view = new LiveChatC2CNewView(this);
        view.setClickListener(new LiveChatC2CNewView.ClickListener() {

            @Override
            public void onClickBack() {
                finish();
            }
        });

        view.setOnChatItemClickListener(new LiveChatC2CNewView.OnChatItemClickListener() {
            @Override
            public void onChatItemClickListener(LiveConversationListModel itemLiveChatListModel) {
                Intent intent = new Intent(LiveChatC2CActivity.this, LivePrivateChatActivity.class);
                intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, itemLiveChatListModel.getPeer());
                startActivity(intent);
            }
        });
        replaceView(R.id.ll_content, view);
        //传入数据
        view.requestData();
    }
}
