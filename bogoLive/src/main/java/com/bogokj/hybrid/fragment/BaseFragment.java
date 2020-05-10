package com.bogokj.hybrid.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.activity.MainActivity;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.bogokj.library.fragment.SDBaseFragment;
import com.bogokj.live.R;
import com.bogokj.library.view.AppDialogProgress;
import com.sunday.eventbus.SDBaseEvent;

import org.xutils.x;

/**
 * @author 作者 yhz
 * @version 创建时间：2015-2-5 上午10:27:37 类说明 基类Fragment
 */
public class BaseFragment extends SDBaseFragment {
    protected AppDialogProgress mDialog;
    private SDStateLayout mStateLayout;

    protected void dimissDialog() {
        AppDialogProgress.hideWaitDialog();
    }

    protected void showProgressDialog(String msg) {
        AppDialogProgress.showWaitTextDialog(getContext(),msg);
    }

    protected void dismissProgressDialog() {
        dimissDialog();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        x.view().inject(this, view);
        init();
        super.onViewCreated(view, savedInstanceState);
    }

    protected void init() {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {

    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public MainActivity getMainActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            if (activity instanceof MainActivity) {
                return ((MainActivity) activity);
            }
        }
        return null;
    }

    @Override
    protected int onCreateContentView() {
        // TODO Auto-generated method stub
        return 0;
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
}
