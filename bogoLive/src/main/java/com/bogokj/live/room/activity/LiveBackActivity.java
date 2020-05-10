package com.bogokj.live.room.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.bogokj.live.R;
import com.bogokj.live.room.LiveFragment;
import com.bogokj.live.room.back.LivePlaybackFragment;

/**
 * @author kn update
 * @description: 主播回播界面Activity
 * @time 2020/2/16
 */
public class LiveBackActivity extends BaseLiveActivity {
    private LivePlaybackFragment creatFragment;
    private String userId;
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_anchor_touch);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 不锁屏


        userId = getIntent().getStringExtra("user_id");
        roomId = getIntent().getIntExtra("extra_room_id", 0);

        initView(savedInstanceState);
    }

    public void initView(Bundle savedInstanceState) {
        //添加Fragment应以这种方式,当旋转屏幕和被系统杀掉重启时,才不会出问题
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager(); //获取Fragment管理器对象
            FragmentTransaction ft = fm.beginTransaction(); //开启事务

            if (fm.findFragmentById(R.id.fragment_container) == null) {
                creatFragment = new LivePlaybackFragment();
            } else {
                creatFragment = (LivePlaybackFragment) fm.findFragmentById(R.id.fragment_container);
                ft.remove(creatFragment);
                fm.popBackStack();
                ft.commit();
                ft = fm.beginTransaction();
            }
            //传入一些参数
            Bundle bundle = new Bundle();
            bundle.putString("user_id", userId);
            bundle.putInt(LiveFragment.EXTRA_ROOM_ID, roomId);
            creatFragment.setArguments(bundle);

            ft.add(R.id.fragment_container, creatFragment); //将fragment添加到布局当中
            ft.commit(); //千万别忘记提交事务
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    public void onBackPressed() {
        creatFragment.exitRoom();

    }


    @Override
    protected void onDestroy() {
        //销毁单例
        super.onDestroy();
    }
}
