package com.bogokj.hybrid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.event.EExitApp;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.hybrid.umeng.UmengPushManager;
import com.bogokj.library.activity.SDBaseActivity;
import com.bogokj.library.receiver.SDNetworkReceiver;
import com.bogokj.library.title.SDTitleItem;
import com.bogokj.library.title.SDTitleSimple;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDOtherUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.library.view.AppDialogProgress;
import com.bogokj.live.event.EOnBackground;
import com.bogokj.live.event.EOnResumeFromBackground;
import com.bogokj.live.utils.LiveVideoChecker;
import com.bogokj.live.view.pulltorefresh.PullToRefreshViewWrapper;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import de.greenrobot.event.EventBus;

public class BaseNoTitleActivity extends SDBaseActivity implements SDNetworkReceiver.SDNetworkCallback, SDTitleSimple.SDTitleSimpleListener {


    /**
     * 触摸返回键是否退出App
     */
    protected boolean mIsExitApp = false;
    protected long mExitTime = 0;
    protected SDTitleSimple mTitle;
    private AppDialogProgress mProgressDialog;

    private PullToRefreshViewWrapper mPullToRefreshViewWrapper;
    private SDStateLayout mStateLayout;

    @Override
    protected void init(Bundle savedInstanceState) {
        UmengPushManager.getPushAgent().onAppStart();
        SDNetworkReceiver.addCallback(this);
        if (App.getApplication().isPushStartActivity(getClass())) {
            App.getApplication().startPushRunnable();
        }
    }

    @Override
    public void setContentView(View view) {

//        //先这样优化沉浸状态栏
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent), 0);

        super.setContentView(view);
        x.view().inject(this);

    }

    @Override
    protected int onCreateTitleViewResId() {
        return 0;
    }

    /**
     * 是否显示标题
     * <p>
     * 不想要标题别复写这方法
     * <p>
     * 想要标题 还要点击事件 点击监听  setTitleButtonClickListener
     *
     * @return
     */
    public boolean isShowTitle() {
        return false;
    }

    public void setTitleButtonClickListener(TitleButtonClickListener listener) {
        titleButtonClickListener = listener;
    }


    /**
     * 返回下拉刷新包裹对象
     *
     * @return
     */
    public final PullToRefreshViewWrapper getPullToRefreshViewWrapper() {
        if (mPullToRefreshViewWrapper == null) {
            mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();
        }
        return mPullToRefreshViewWrapper;
    }

    public void setStateLayout(SDStateLayout stateLayout) {
        if (mStateLayout != stateLayout) {
            mStateLayout = stateLayout;
            if (stateLayout != null) {
                stateLayout.getEmptyView().setContentView(R.layout.view_state_empty_content);
                stateLayout.getErrorView().setContentView(R.layout.view_state_error_net);
            }
        }
    }

    public SDStateLayout getStateLayout() {
        return mStateLayout;
    }


    public void showProgressDialog(String msg) {
        AppDialogProgress.showWaitTextDialog(this,msg);
    }

    public void dismissProgressDialog() {
        AppDialogProgress.hideWaitDialog();
    }

    public void exitApp() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            SDToast.showToast("再按一次退出!");
        } else {
            App.getApplication().exitApp(true);
        }
        mExitTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onBackground() {
        EOnBackground event = new EOnBackground();
        EventBus.getDefault().post(event);

        CommonInterface.requestStateChangeLogout(new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    LogUtil.i("requestStateChangeLogout");
                }
            }
        });
        super.onBackground();
    }

    @Override
    protected void onResumeFromBackground() {
        EOnResumeFromBackground event = new EOnResumeFromBackground();
        EventBus.getDefault().post(event);

        CommonInterface.requestStateChangeLogin(new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    LogUtil.i("requestStateChangeLogin");
                }
            }
        });
        if (getClass() != BaseNoTitleActivity.class) {
            checkVideo();
        }
        super.onResumeFromBackground();
    }

    protected void checkVideo() {
        LiveVideoChecker checker = new LiveVideoChecker(this);
        CharSequence copyContent = SDOtherUtil.pasteText();
        checker.check(String.valueOf(copyContent));
    }

    @Override
    public void onBackPressed() {
        if (mIsExitApp) {
            exitApp();
        } else {
            super.onBackPressed();
        }
    }

    public void onEventMainThread(EExitApp event) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getApplicationContext()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        SDNetworkReceiver.removeCallback(this);
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onNetworkChanged(SDNetworkReceiver.NetworkType type) {

    }


    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        //左边按钮
        if (titleButtonClickListener != null)
            titleButtonClickListener.onLeftTitleButtonClickListener();
    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {
        //中间按钮
        if (titleButtonClickListener != null)
            titleButtonClickListener.onMiddleTitleButtonClickListener();
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        //右边按钮
        if (titleButtonClickListener != null)
            titleButtonClickListener.onRightTitleButtonClickListener();
    }


    /**
     * 标题按钮点击监听
     */

    private TitleButtonClickListener titleButtonClickListener;

    public interface TitleButtonClickListener {
        void onLeftTitleButtonClickListener();

        void onRightTitleButtonClickListener();

        void onMiddleTitleButtonClickListener();
    }
}
