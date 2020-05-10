package com.bogokj.live.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.constant.Constant;
import com.bogokj.library.handler.PhotoHandler;
import com.bogokj.library.handler.PhotoHandler.PhotoHandlerListener;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.ImageCropManage;
import com.bogokj.live.event.EUserImageUpLoadComplete;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.PermissionUtil;
import com.bogokj.live.utils.PermissionUtils;

import org.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-7-2 下午5:26:09 类说明
 */
public class LiveUserPhotoActivity extends BaseActivity
{
    public static final String EXTRA_USER_IMG_URL = "extra_user_img_url";

    @ViewInject(R.id.iv_back)
    private ImageView iv_back;
    @ViewInject(R.id.iv_head_img)
    private ImageView iv_head_img;
    @ViewInject(R.id.tv_photo_ablum)
    private TextView tv_photo_ablum;
    @ViewInject(R.id.tv_tabe_photo)
    private TextView tv_tabe_photo;

    private PhotoHandler photoHandler;
    private String url;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_user_photo);
        init();
    }

    private void init()
    {
        register();
        getIntentData();
        displayImage();
        initPhotoHandler();
    }

    private void getIntentData()
    {
        url = getIntent().getStringExtra(EXTRA_USER_IMG_URL);
    }

    private void register()
    {
        iv_back.setOnClickListener(this);
        tv_photo_ablum.setOnClickListener(this);
        tv_tabe_photo.setOnClickListener(this);
    }

    private void displayImage()
    {
        GlideUtil.loadHeadImage(url).into(iv_head_img);
    }

    private void initPhotoHandler()
    {
        photoHandler = new PhotoHandler(this);
        photoHandler.setListener(new PhotoHandlerListener()
        {

            @Override
            public void onResultFromCamera(File file)
            {
                if (file != null && file.exists())
                {
                    dealImageFile(file);
                }
            }

            @Override
            public void onResultFromAlbum(File file)
            {
                if (file != null && file.exists())
                {
                    dealImageFile(file);
                }
            }

            @Override
            public void onFailure(String msg)
            {
                SDToast.showToast(msg);
            }
        });
    }

    private void dealImageFile(File file)
    {
        if (AppRuntimeWorker.getOpen_sts() == 1)
        {
            ImageCropManage.startCropActivity(this, file.getAbsolutePath());
        } else
        {
            Intent intent = new Intent(this, LiveUploadUserImageActivity.class);
            intent.putExtra(LiveUploadUserImageActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_back:
                clickIvBack();
                break;
            case R.id.tv_photo_ablum:
                clickTvPhotoAblum();
                break;
            case R.id.tv_tabe_photo:
                clickTvTakePhoto();
//                PermissionUtils.checkAndRequestMorePermissions(LiveUserPhotoActivity.this, PERMISSIONS,1,
//                        new PermissionUtils.PermissionRequestSuccessCallBack() {
//                            @Override
//                            public void onHasPermission() {
//
//                                clickTvTakePhoto();
//                                // 权限已被授予
//
//                            }
//                        });

                break;
            default:
                break;
        }
    }

    private void clickIvBack()
    {
        finish();
    }

    private void clickTvPhotoAblum()
    {
        photoHandler.getPhotoFromAlbum();
    }

    private void clickTvTakePhoto()
    {
        photoHandler.getPhotoFromCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        photoHandler.onActivityResult(requestCode, resultCode, data);
        ImageCropManage.onActivityResultUserImage(this,requestCode, resultCode, data);
    }

    public void onEventMainThread(EUserImageUpLoadComplete event)
    {
        finish();
    }
}
