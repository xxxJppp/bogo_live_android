package com.bogokj.hybrid.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bogokj.hybrid.event.ERetryInitSuccess;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.business.InitBusiness;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.PermissionUtil;
import com.bogokj.xianrou.activity.QKMySmallVideoActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * @author lvfu
 * @description: 启动页
 * @time kn 2019/12/16
 */
public class SplashActivity extends BaseActivity {
    private InitBusiness mInitBusiness;

    @ViewInject(R.id.iv_wel)
    ImageView iv_wel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen(true);
        setContentView(R.layout.act_init);

        //两个日历权限和一个数据读写权限
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//        PermissionsUtils.showSystemSetting = false;//是否支持显示系统设置权限设置窗口跳转
        PermissionUtil.getInstance().chekPermissions(this, permissions, permissionsResult);

        GlideUtil.load(R.drawable.wel).into(iv_wel);
    }

    //创建监听权限的接口对象
    PermissionUtil.IPermissionsResult permissionsResult = new PermissionUtil.IPermissionsResult() {
        @Override
        public void passPermissons() {
            mInitBusiness = new InitBusiness();
            mInitBusiness.init(SplashActivity.this);
        }

        @Override
        public void forbitPermissons() {
            SDToast.showToast("权限被阻止，可能会使你无法正常使用该软件的部分功能！");
            mInitBusiness = new InitBusiness();
            mInitBusiness.init(SplashActivity.this);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionUtil.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void onEventMainThread(ERetryInitSuccess event) {
        InitBusiness.dealInitLaunchBusiness(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mInitBusiness) {
            mInitBusiness.onDestroy();
        }
        mInitBusiness = null;
    }
}
