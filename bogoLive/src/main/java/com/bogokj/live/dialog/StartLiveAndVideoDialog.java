package com.bogokj.live.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bogokj.dynamic.activity.PushDynamicActivity;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveCreateRoomActivity;
import com.bogokj.live.activity.LiveCreaterAgreementActivity;
import com.bogokj.live.activity.LiveMainActivity;
import com.bogokj.live.activity.VideoChooseActivity;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.UserModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.tencent.qcloud.xiaoshipin.videorecord.TCVideoRecordActivity;

import static com.bogokj.xianrou.activity.XRVideoListActivity.VIDEO_REQUEST_CODE;

/**
 * 开始直播和拍摄小视频弹窗
 * Created by Administrator on 2019/1/12 0012.
 */

public class StartLiveAndVideoDialog extends SDDialogBase {

    private Activity context;
    private RelativeLayout rlshutdown;
    private ImageView im_start_live;
    private ImageView im_start_video, im_start_dynamic;

    public StartLiveAndVideoDialog(Activity context) {
//        super(context, R.style.startlivedialog);
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_start_live_and_video);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        paddings(0);
        initLayoutParams();

        rlshutdown = findViewById(R.id.rl_shutdown);
        im_start_live = findViewById(R.id.im_start_live);
        im_start_video = findViewById(R.id.im_start_video);
        im_start_dynamic = findViewById(R.id.im_start_dynamic);
        rlshutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //开始直播
        im_start_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (AppRuntimeWorker.isLogin((LiveMainActivity) context)) {
                    final UserModel userModel = UserModelDao.query();
                    if (userModel.getIs_agree() == 1) {
                        Intent intent = new Intent(context, LiveCreateRoomActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, LiveCreaterAgreementActivity.class);
                        context.startActivity(intent);
                    }
                }


            }
        });
        //开始拍摄
        im_start_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (!TextUtils.isEmpty(AppRuntimeWorker.getTencent_video_sdk_key())) {
                    Intent intent = new Intent(context, TCVideoRecordActivity.class);
                    ((LiveMainActivity) context).startActivity(intent);
                } else {
                    Intent intent = new Intent(context, VideoChooseActivity.class);
                    ((LiveMainActivity) context).startActivityForResult(intent, VIDEO_REQUEST_CODE);
                }
            }
        });

        //
        im_start_dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent = new Intent(((LiveMainActivity) context), PushDynamicActivity.class);
                ((LiveMainActivity) context).startActivity(intent);
            }
        });

//        initLayoutParams();
    }


    /**
     * 初始化弹出窗宽高，显示位置
     */
    private void initLayoutParams() {
        //布局的参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setWindowAnimations(R.style.BottomToTopAnim);
        window.setAttributes(params);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        disMissListener.onDisMissListener();
    }


    private DisMissListener disMissListener;

    public void setDisMissListener(DisMissListener disMissListener) {
        this.disMissListener = disMissListener;
    }

    public interface DisMissListener {
        void onDisMissListener();
    }
}
