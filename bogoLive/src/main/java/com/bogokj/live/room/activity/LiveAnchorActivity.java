package com.bogokj.live.room.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.bogokj.library.receiver.SDNetworkReceiver;
import com.bogokj.live.R;
import com.bogokj.live.room.anchor.LivePushCreaterFragment;
import com.bogokj.live.room.audience.LivePushViewerFragment;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;

import static com.bogokj.live.room.LiveFragment.EXTRA_ROOM_ID;
import static com.bogokj.live.room.anchor.LiveLayoutCreaterFragment.EXTRA_IS_CLOSED_BACK;

/**
 * @author kn update
 * @description: 主播直播界面Activity
 * @time 2020/2/16
 */
public class LiveAnchorActivity extends BaseLiveActivity {
    private LivePushCreaterFragment creatFragment;
    private int roomId;
    private int mIsClosedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_anchor_touch);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 不锁屏


        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        mIsClosedBack = getIntent().getIntExtra(EXTRA_IS_CLOSED_BACK, 0);

        initView(savedInstanceState);
    }

    public void initView(Bundle savedInstanceState) {
        //添加Fragment应以这种方式,当旋转屏幕和被系统杀掉重启时,才不会出问题
        if (savedInstanceState == null) {
            //获取Fragment管理器对象
            FragmentManager fm = getSupportFragmentManager();
            //开启事务
            FragmentTransaction ft = fm.beginTransaction();

            if (fm.findFragmentById(R.id.fragment_container) == null) {
                creatFragment = new LivePushCreaterFragment();
            } else {
                creatFragment = (LivePushCreaterFragment) fm.findFragmentById(R.id.fragment_container);
                ft.remove(creatFragment);
                fm.popBackStack();
                ft.commit();
                ft = fm.beginTransaction();
            }
            
            //传入一些参数
            Bundle bundle = new Bundle();
            bundle.putInt(LivePushViewerFragment.EXTRA_ROOM_ID, roomId);
            bundle.putInt(EXTRA_IS_CLOSED_BACK, mIsClosedBack);
            creatFragment.setArguments(bundle);

            //将fragment添加到布局当中
            ft.add(R.id.fragment_container, creatFragment);
            ft.commit(); //千万别忘记提交事务
        }
    }


    @Override
    public void onNetworkChanged(SDNetworkReceiver.NetworkType type) {
        if (type == SDNetworkReceiver.NetworkType.Mobile) {
            AppDialogConfirm dialog = new AppDialogConfirm(this);
            dialog.setTextContent("当前处于数据网络下，会耗费较多流量，是否继续？")
                    .setTextCancel("退出")
                    .setTextConfirm("继续")
                    .setCallback(new ISDDialogConfirm.Callback() {
                        @Override
                        public void onClickCancel(View v, SDDialogBase dialog) {
                            creatFragment.exitRoom(true);
                        }

                        @Override
                        public void onClickConfirm(View v, SDDialogBase dialog) {

                        }
                    }).show();
        }
        super.onNetworkChanged(type);
    }


    @Override
    public void onStop() {
        super.onStop();
        creatFragment.createrLeave();
    }

    @Override
    public void onBackPressed() {
        if (isAuctioning()) {
            creatFragment.showActionExitDialog();
        } else {
            creatFragment.showNormalExitDialog();
        }
    }


    @Override
    protected void onDestroy() {
        //销毁单例
        super.onDestroy();
    }
}
