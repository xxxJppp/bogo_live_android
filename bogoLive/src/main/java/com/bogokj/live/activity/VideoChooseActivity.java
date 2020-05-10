package com.bogokj.live.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.adapter.TCVideoEditerListAdapter;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.utils.ItemView;
import com.bogokj.xianrou.activity.XRVideoListActivity;
import com.bogokj.xianrou.util.VideoFileUtils;


import java.io.File;
import java.util.ArrayList;


public class VideoChooseActivity extends Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "VideoChooseActivity";
    public static final String VIDEO_PATH = "video_path";
    public static final int VIDEO_REQUEST_CODE = 340;
    public static final int VIDEO_RESULT_CODE = 341;

    public static final int TYPE_SINGLE_CHOOSE = 0;
    public static final int TYPE_MULTI_CHOOSE = 1;
    private static final int VIDEO_SPAN_COUNT = 4;
    private RecyclerView mRecyclerView;
    private int mType;

    private TCVideoEditerListAdapter mAdapter;
    private TCVideoEditerMgr  mTCVideoEditerMgr;
    private ArrayList<TCVideoFileInfo> mTCVideoFileInfoList;

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<TCVideoFileInfo> fileInfoArrayList = (ArrayList<TCVideoFileInfo>) msg.obj;
            mAdapter.addAll(fileInfoArrayList);
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_ugc_video_list);

        mTCVideoEditerMgr = TCVideoEditerMgr.getInstance(this);
        mHandlerThread = new HandlerThread("LoadList");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mType = getIntent().getIntExtra("CHOOSE_TYPE", TYPE_MULTI_CHOOSE);
        mTCVideoFileInfoList = new ArrayList<>();
        init();
        loadVideoList();
    }

    @Override
    protected void onDestroy() {
        mHandlerThread.getLooper().quit();
        mHandlerThread.quit();
        super.onDestroy();
    }

    private void loadVideoList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<TCVideoFileInfo> fileInfoArrayList = mTCVideoEditerMgr.getAllVideo();
                    TCVideoFileInfo tcVideoFileInfo = new TCVideoFileInfo();
                     tcVideoFileInfo.setFileType(100);
                    fileInfoArrayList.add(0,tcVideoFileInfo);

                    Message msg = new Message();
                    msg.obj = fileInfoArrayList;
                    mMainHandler.sendMessage(msg);
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadVideoList();
        }
    }

    private void init() {
        ImageView  ivback = (ImageView) findViewById(R.id.iv_back);
        ivback.setOnClickListener(this);

//        mBtnNext = (Button) findViewById(R.id.btn_next);
//        mBtnNext.setEnabled(false);
//        mBtnNext.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R .id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, VIDEO_SPAN_COUNT));

        mAdapter = new TCVideoEditerListAdapter(this);
        mAdapter.setOnItemAddListener(onItemAddListener);
        mRecyclerView.setAdapter(mAdapter);




    }

//    private MenuView.ItemView.OnDeleteListener onItemDeleteListener = new MenuView.ItemView.OnDeleteListener() {
//        @Override
//        public void onDelete(int position) {
//            mMenuAdapter.removeIndex(position);
//            if (mMenuAdapter.getItemCount() == 0) {
//                mBtnNext.setEnabled(false);
//            }
//        }
//    };
//
    private ItemView.OnAddListener onItemAddListener = new ItemView.OnAddListener() {

        @Override
        public void onAdd(TCVideoFileInfo fileInfo) {
            if(fileInfo.getFileType()==100){
                // 激活系统的照相机进行录像
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                int maxDuration = (int) AppRuntimeWorker.getVideo_max_duration();
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, maxDuration);
                  startActivityForResult(intent, VideoChooseActivity.VIDEO_REQUEST_CODE);
            }else {

                final long duration = Long.valueOf(fileInfo.getDuration());
                if (duration > ((long) AppRuntimeWorker.getVideo_max_duration()*1000))
                {
                    SDToast.showToast("亲!您选择的视频过大!");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra(XRVideoListActivity.VIDEO_PATH, fileInfo.getFilePath());
                setResult(XRVideoListActivity.VIDEO_RESULT_CODE, intent);
                finish();
            }

        }
    };

//    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
//        @Override
//        public boolean onItemMove(int fromPosition, int toPosition) {
//            // 当Item被拖拽的时候。
//            Collections.swap(mTCVideoFileInfoList, fromPosition, toPosition);
//            mMenuAdapter.notifyItemMoved(fromPosition, toPosition);
//            return true;
//        }
//
//        @Override
//        public void onItemDismiss(int position) {
//        }
//    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
//            case R.id.back_ll:
//                finish();
//                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    private void doSelect() {
//        int count = mMenuAdapter.getItemCount();
//        for (int i = 0; i < count; i++) {
//            TCVideoFileInfo fileInfo = mMenuAdapter.getItem(i);
//            if (isVideoDamaged(fileInfo)){
//                showErrorDialog("包含已经损坏的视频文件");
//                return;
//            }
//            File file = new File(fileInfo.getFilePath());
//            if (!file.exists()) {
//                showErrorDialog("选择的文件不存在");
//                return;
//            }
//        }
//        if (count < 2) {
//            Intent intent = new Intent(this, TCVideoCutterActivity.class);
//            TCVideoFileInfo fileInfo = mMenuAdapter.getItem(0);
//            intent.putExtra(TCConstants.VIDEO_EDITER_PATH, fileInfo.getFilePath());
//            startActivity(intent);
//            return;
//        }
//        Intent intent = new Intent(this, TCVideoJoinerActivity.class);
//        intent.putExtra(TCConstants.INTENT_KEY_MULTI_CHOOSE, mMenuAdapter.getAll());
//        startActivity(intent);
      //  finish();
    }

    /**
     * 检测视频是否损坏
     *
     * @param info
     * @return
     */
    private boolean isVideoDamaged(TCVideoFileInfo info) {
        if (info.getDuration() == 0) {
            //数据库获取到的时间为0，使用Retriever再次确认是否损坏
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(info.getFilePath());
            } catch (Exception e) {
                return true;//无法正常打开，也是错误
            }
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (TextUtils.isEmpty(duration))
                return true;
            return Integer.valueOf(duration) == 0;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            File file = VideoFileUtils.getFileByUri(uri, this);
            if (file != null && file.exists())
            {
                Intent intent = new Intent();
                intent.putExtra(VIDEO_PATH, file.getPath());
                setResult(VIDEO_RESULT_CODE, intent);
                finish();
            }
        }
    }

    private void showErrorDialog(String msg) {
//        AlertDialog.Builder normalDialog = new AlertDialog.Builder(VideoChooseActivity.this, R.style.ConfirmDialogStyle);
//        normalDialog.setMessage(msg);
//        normalDialog.setCancelable(false);
//        normalDialog.setPositiveButton("知道了", null);
//        normalDialog.show();
    }
}
