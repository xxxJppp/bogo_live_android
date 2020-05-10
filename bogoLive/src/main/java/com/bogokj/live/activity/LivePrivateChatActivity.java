package com.bogokj.live.activity;

import android.os.Bundle;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.library.utils.SDKeyboardListener;
import com.bogokj.live.R;
import com.bogokj.live.appview.LivePrivateChatView;
import com.bogokj.live.appview.LivePrivateChatView.ClickListener;

public class LivePrivateChatActivity extends BaseActivity {
    /**
     * 聊天对象user_id(String)
     */
    public static final String EXTRA_USER_ID = "extra_user_id";

    private LivePrivateChatView view_private_chat;

    private SDKeyboardListener mKeyboardListener = new SDKeyboardListener();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.act_live_private_chat);
        view_private_chat = (LivePrivateChatView) findViewById(R.id.view_private_chat);

        String user_id = getIntent().getStringExtra(EXTRA_USER_ID);

        view_private_chat.setLockHeightEnable(true);
        view_private_chat.setClickListener(() -> finish());
        view_private_chat.setUserId(user_id);

        mKeyboardListener.setActivity(this).setKeyboardVisibilityCallback((visible, height) -> view_private_chat.onKeyboardVisibilityChange(visible, height));
    }
}
