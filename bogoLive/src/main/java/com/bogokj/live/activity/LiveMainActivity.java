package com.bogokj.live.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.activity.BaseNoTitleActivity;
import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.service.AppUpgradeHelper;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveChatC2CNewView;
import com.bogokj.live.appview.main.LiveMainBottomNavigationView;
import com.bogokj.live.appview.main.LiveMainDynamicView;
import com.bogokj.live.appview.main.LiveMainHomeView;
import com.bogokj.live.appview.main.LiveMainMeView;
import com.bogokj.live.appview.main.LiveMainRankingView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.BogoDaySignDialog;
import com.bogokj.live.dialog.LevelLoginFirstDialog;
import com.bogokj.live.dialog.LevelUpgradeDialog;
import com.bogokj.live.dialog.LiveInviteCodeDialog;
import com.bogokj.live.dialog.StartLiveAndVideoDialog;
import com.bogokj.live.dialog.common.AppDialogConfirm;
import com.bogokj.live.event.EIMLoginError;
import com.bogokj.live.event.EImOnForceOffline;
import com.bogokj.live.event.EReSelectTabLiveBottom;
import com.bogokj.live.utils.LocationUtils;
import com.bogokj.xianrou.activity.XRPublishVideoActivity;
import com.bogokj.xianrou.activity.XRVideoListActivity;
import com.bogokj.xianrou.appview.main.QKTabSmallVideoView;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.maning.imagebrowserlibrary.utils.StatusBarUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.sunday.eventbus.SDEventManager;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.ugc.TXUGCBase;

import cn.tillusory.sdk.TiSDK;

/**
 * @author kn
 * @description: 项目主页
 * @time kn 2019/12/17
 */
public class LiveMainActivity extends BaseNoTitleActivity implements LiveMainDynamicView.ClickListener {
    private FrameLayout fl_main_content;
    private LiveMainHomeView mMainHomeView;
    private LiveMainRankingView mMainRankingView;
    private QKTabSmallVideoView mSmallVideoView;
    private LiveMainMeView mMainMeView;
    private LiveMainBottomNavigationView mBottomNavigationView;
    Location location;
    private LiveMainDynamicView mMainDynamicView;
    private LiveChatC2CNewView mMainMsgView;
    //当前所在的页面 直播弹窗消失后要重置到当前页面
    private int lastIndex = 0;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_main;
    }



    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
//        StatusBarUtil.setLightMode(this);

        location = LocationUtils.getInstance(LiveMainActivity.this).showLocation();
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        mIsExitApp = true;

        checkUpdate();
        //登陆腾讯云
        AppRuntimeWorker.startContext();
        CommonInterface.requestUser_apns(null);
        CommonInterface.requestMyUserInfo(null);
        checkVideo();

        initTabs();

        initUpgradeDialog();
        initLoginfirstDialog();
        initFullInviteCodeDialog();
        initDaySignDialog();

        // Toast.makeText(LiveMainActivity.this,  "---"+SDTencentMapManager.getInstance().getLongitude(),Toast.LENGTH_SHORT).show();;
        //取消严格模式  FileProvider(用于解决7.0以上的拍照闪退问题)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        initBoGoBeautySdk();
        initTencetSdk();

//        Intent intent = new Intent(this, BogoRecommendEmceeActivity.class);
//        startActivity(intent);
    }



    private void initDaySignDialog() {
        BogoDaySignDialog.check(this);
    }

    /*
     * 初始化SDK鉴权
     * */
    private void initTencetSdk() {
        String liveLicenceURL = AppRuntimeWorker.getTencent_live_sdk_licence(); // 获取到的 licence url
        String liveLicenceKey = AppRuntimeWorker.getTencent_live_sdk_key();
        String videoLicenceURL = AppRuntimeWorker.getTencent_video_sdk_licence(); // 获取到的 licence url
        String videoLicenceKey = AppRuntimeWorker.getTencent_video_sdk_key(); // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, liveLicenceURL, liveLicenceKey);
        TXUGCBase.getInstance().setLicence(this, videoLicenceURL, videoLicenceKey);
    }


    //初始化布谷科技美颜SDK
    private void initBoGoBeautySdk() {
        if (AppRuntimeWorker.get_is_open_bogo_beauty() == 1) {
            TiSDK.init(InitActModelDao.query().getBogo_beauty_key(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initUpgradeDialog();
        initLoginfirstDialog();
        initFullInviteCodeDialog();

    }


    private void initFullInviteCodeDialog() {
        //LiveInviteCodeDialog.check(this);
    }

    private void initUpgradeDialog() {
        LevelUpgradeDialog.check(this);
    }

    //首次登陆奖励和升级提示
    private void initLoginfirstDialog() {
        LevelLoginFirstDialog.check(this);
    }

    private void checkUpdate() {
        new AppUpgradeHelper(this).check(0);
    }


    private void initTabs() {
        mBottomNavigationView = (LiveMainBottomNavigationView) findViewById(R.id.view_bottom_navigation);
        mBottomNavigationView.setCallback(new LiveMainBottomNavigationView.Callback() {
            @Override
            public void onTabSelected(int index) {
                switch (index) {
                    case LiveMainBottomNavigationView.INDEX_HOME:
                        onSelectTabHome();
                        GSYVideoManager.releaseAllVideos();
                        lastIndex = index;
                        break;
//                    case LiveMainBottomNavigationView.INDEX_RANKING:
//                    case LiveMainBottomNavigationView.INDEX_MSG:
//                        onSelectTabMsg();
//                        break;
                    case LiveMainBottomNavigationView.INDEX_DYNAMIC:
                        onSelectTabRankingOrDynamic();
                        lastIndex = index;
                        break;

                    case LiveMainBottomNavigationView.INDEX_LIVE:
                        LiveMainActivity.this.onClickCreateLive();
                        break;

                    case LiveMainBottomNavigationView.INDEX_SMALL_VIDEO:
                        onSelectTabSmallVideo();
                        GSYVideoManager.releaseAllVideos();
                        lastIndex = index;
                        break;
                    case LiveMainBottomNavigationView.INDEX_ME:
                        onSelectTabMe();
                        GSYVideoManager.releaseAllVideos();
                        lastIndex = index;
                        break;

                }
            }

            @Override
            public void onTabReselected(int index) {
                EReSelectTabLiveBottom event = new EReSelectTabLiveBottom();
                event.index = index;
                SDEventManager.post(event);
            }

            @Override
            public void onClickCreateLive(View view) {


            }
        });

        mBottomNavigationView.selectTab(LiveMainBottomNavigationView.INDEX_HOME);
    }


    private LiveChatC2CNewView getMainMsgView() {
        if (mMainMsgView == null) {
            mMainMsgView = new LiveChatC2CNewView(this);
        }
        return mMainMsgView;
    }

    public LiveMainHomeView getMainHomeView() {
        if (mMainHomeView == null) {
            mMainHomeView = new LiveMainHomeView(this);
        }
        return mMainHomeView;
    }

    public LiveMainRankingView getMainRankingView() {
        if (mMainRankingView == null) {
            mMainRankingView = new LiveMainRankingView(this);
        }
        return mMainRankingView;
    }

    public QKTabSmallVideoView getSmallVideoView() {
        if (mSmallVideoView == null) {
            mSmallVideoView = new QKTabSmallVideoView(this);
        }
        return mSmallVideoView;
    }

    public LiveMainMeView getMainMeView() {
        if (mMainMeView == null) {
            mMainMeView = new LiveMainMeView(this);
        }
        return mMainMeView;
    }

    public LiveMainDynamicView getmMainDynamicView() {
        if (mMainDynamicView == null) {
            mMainDynamicView = new LiveMainDynamicView(this);
            mMainDynamicView.setClickListener(this);
        }
        return mMainDynamicView;
    }

    /**
     * 主页
     */
    protected void onSelectTabHome() {
        SDViewUtil.toggleView(fl_main_content, getMainHomeView());
    }

    /**
     * 动态
     */
    protected void onSelectTabRankingOrDynamic() {
//        SDViewUtil.toggleView(fl_main_content, getMainRankingView());
        SDViewUtil.toggleView(fl_main_content, getmMainDynamicView());
    }

    /**
     * 小视频
     */
    protected void onSelectTabSmallVideo() {
        SDViewUtil.toggleView(fl_main_content, getSmallVideoView());
    }


    private void onSelectTabMsg() {
        SDViewUtil.toggleView(fl_main_content, getMainMsgView());
    }

    /**
     * 个人中心
     */
    protected void onSelectTabMe() {
        SDViewUtil.toggleView(fl_main_content, getMainMeView());
    }

    //直播和小视频弹窗
    private void onClickCreateLive() {
        // startActivity(new Intent(LiveMainActivity.this, QKCreateEntranceActivity.class));
        StartLiveAndVideoDialog startLiveAndVideoDialog = new StartLiveAndVideoDialog(LiveMainActivity.this);
        startLiveAndVideoDialog.setCanceledOnTouchOutside(true);
        startLiveAndVideoDialog.showBottom();
        startLiveAndVideoDialog.setDisMissListener(() -> {
            mBottomNavigationView.selectTab(lastIndex);
        });

    }

    public void onEventMainThread(EIMLoginError event) {
        AppDialogConfirm dialog = new AppDialogConfirm(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        String content = "登录IM失败";
        if (!TextUtils.isEmpty(event.errMsg)) {
            content = content + (event.errCode + event.errMsg);
        }
        dialog.setTextContent(content).setTextCancel("退出").setTextConfirm("重试");
        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                App.getApplication().logout(false);
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                AppRuntimeWorker.startContext();
            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XRVideoListActivity.VIDEO_REQUEST_CODE && resultCode == XRVideoListActivity.VIDEO_RESULT_CODE) {
            //跳转到小视频发布页
            Intent intent = new Intent(this, XRPublishVideoActivity.class);
            intent.putExtra(XRPublishVideoActivity.EXTRA_VIDEO_URL, data.getStringExtra(XRVideoListActivity.VIDEO_PATH));
            startActivity(intent);
        }
    }

    /**
     * 异地登录
     *
     * @param event
     */
    public void onEventMainThread(EImOnForceOffline event) {
        AppDialogConfirm dialog = new AppDialogConfirm(this);
        dialog.setTextContent("你的帐号在另一台手机上登录");
        dialog.setTextCancel("退出");
        dialog.setTextConfirm("重新登录");
        dialog.setCallback(new ISDDialogConfirm.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {
                App.getApplication().logout(true);
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog) {
                AppRuntimeWorker.startContext();
            }
        }).show();
    }


    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onLiveClickListener() {
        mBottomNavigationView.selectTab(0);
    }

    @Override
    public void onVideoClickListener() {
        mBottomNavigationView.selectTab(3);
    }


}
