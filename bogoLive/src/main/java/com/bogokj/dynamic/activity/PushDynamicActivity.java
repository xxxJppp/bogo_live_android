package com.bogokj.dynamic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bogokj.dynamic.adapter.FullyGridLayoutManager;
import com.bogokj.dynamic.adapter.GridImageAdapter;
import com.bogokj.dynamic.audiorecord.AudioPlaybackManager;
import com.bogokj.dynamic.audiorecord.AudioRecordJumpUtil;
import com.bogokj.dynamic.audiorecord.entity.AudioEntity;
import com.bogokj.dynamic.audiorecord.util.PaoPaoTips;
import com.bogokj.dynamic.audiorecord.view.CommonSoundItemView;
import com.bogokj.dynamic.event.EventBusConfig;
import com.bogokj.dynamic.event.RefreshMessageEvent;
import com.bogokj.dynamic.event.VoiceRecordEvent;
import com.bogokj.dynamic.modle.BogoDynamicTopicListModel;
import com.bogokj.dynamic.utils.ImageUtil;
import com.bogokj.dynamic.utils.KeyboardUtils;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.map.tencent.SDTencentMapManager;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.live.dialog.common.AppDialogMenu;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.common.AliyunOssManage;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_aliyun_stsActModel;
import com.bogokj.xianrou.activity.base.XRBaseActivity;
import com.bogokj.xianrou.dialog.XRTimeOutLoadingDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sunday.eventbus.SDEventManager;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PushDynamicActivity extends XRBaseActivity implements TencentLocationListener {

    @ViewInject(R.id.btn_voice_record)
    Button mBtnVoiceRecord;

    @ViewInject(R.id.btn_video_record)
    Button mBtnVideoRecord;

    @ViewInject(R.id.et_input)
    EditText mEtInput;

    @ViewInject(R.id.tv_mark)
    TextView mark;

    @ViewInject(R.id.recycler)
    RecyclerView recyclerView;

    @ViewInject(R.id.pp_sound_item_view)
    CommonSoundItemView soundItemView;

    @ViewInject(R.id.tv_position_text)
    TextView tv_position_text;

    @ViewInject(R.id.tv_topic_name)
    TextView tv_topic_name;

    @ViewInject(R.id.input_checkbox)
    CheckBox input_checkbox;


    private GridImageAdapter adapter;

    private boolean hasVoiceFile = false;
    private String voiceFilePath = "";
    private List<LocalMedia> selectList = new ArrayList<>();
    private int maxSelectNum = 9;

    private int selectType = PictureMimeType.ofImage();

    /**
     * 1 视频 0图片
     */
    private int fileType = 0;

    /*
     * 1 文本 2图片 3视频
     * */
    private int dynamicType = 1;

    public static final long UPLOAD_TIME_OUT = 180000;//上传超时毫秒数

    private String uploadVideoUrl = "";
    private String uploadImgUrl = "";
    private String uploadVideoThmbUrl = "";
    private String uploadAudoUrl = "";
    private List<String> uploadImgUrlList = new ArrayList<>();
    private String content;
    private OSS mOss;
    private App_aliyun_stsActModel app_aliyun_stsActModel;
    private String fileName;
    private String objectKey;
    private int duration = 0;
    private TextView tvPush;

    private int isLocate = 1;

    //话题ID
    private String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_dynamic);
        initView();
        requestInitParams();

        showLocation();
    }

    protected void initView() {

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);

        tvPush = (TextView) findViewById(R.id.tv_push);

        findViewById(R.id.rl_input).setOnClickListener(this);
        findViewById(R.id.btn_voice_record).setOnClickListener(this);
        tvPush.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.btn_video_record).setOnClickListener(this);
        findViewById(R.id.iv_push_video).setOnClickListener(this);
        findViewById(R.id.iv_push_img).setOnClickListener(this);
        findViewById(R.id.rl_select_topic).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_select_topic:
                clickSelectTopic();
                break;
            case R.id.iv_push_video:
                clickSelectVideo();
                break;
            case R.id.iv_push_img:
                clickSelectImg();
                break;
            case R.id.rl_input:
                KeyboardUtils.showSoftInput(mEtInput);
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_push:
                clickPushDynamic();
                break;
            case R.id.btn_voice_record:
                clickRecrodVoice();
                break;
            case R.id.btn_video_record:
                clickSelectVideo();
                break;

//            case R.id.iv_add_img:
//
//
//                PictureSelector.create(this)
//                        .openGallery(PictureMimeType.ofImage())
//                        .maxSelectNum(9)
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
//                break;

            default:
                break;
        }
    }

    //点击跳转选择话题
    private void clickSelectTopic() {
        Intent intent = new Intent(this, BogoDynamicTopicActivity.class);
        startActivityForResult(intent, BogoDynamicTopicActivity.TOPIC_CODE);
    }

    //点击录制音频
    private void clickRecrodVoice() {
        if (hasVoiceFile) {
            File tempFile = new File(voiceFilePath);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            hasVoiceFile = false;
            soundItemView.setVisibility(View.GONE);
            mBtnVoiceRecord.setText("录制音频");
            return;
        }

        AudioRecordJumpUtil.startRecordAudio(PushDynamicActivity.this);
    }

    //点击选择视频
    private void clickSelectVideo() {

        mBtnVideoRecord.setText("上传图片");
        mark.setText("添加视频不超过1个，文字备注不超过300字");
        selectType = PictureMimeType.ofVideo();
        selectList.clear();

        maxSelectNum = 1;

        adapter.setSelectMax(maxSelectNum);
        adapter.setList(selectList);
        adapter.notifyDataSetChanged();

        showImageOrVideo();
    }

    //点击选择图片
    private void clickSelectImg() {

        mBtnVideoRecord.setText("上传视频");
        mark.setText("添加图片不超过9张，文字备注不超过300字");
        selectType = PictureMimeType.ofImage();
        selectList.clear();

        maxSelectNum = 9;

        adapter.setSelectMax(maxSelectNum);
        adapter.setList(selectList);
        adapter.notifyDataSetChanged();

        showImageOrVideo();
    }

    //点击发布动态
    private void clickPushDynamic() {
        content = mEtInput.getText().toString();
        if (TextUtils.isEmpty(content)) {
            SDToast.showToast("内容不能为空！");
            return;
        }

        if (content.length() < 20) {
            SDToast.showToast("内容不能低于20个字！");
            return;
        }


        tvPush.setClickable(false);

        closeKeyboard();

        //上传视频 图片
        getUploadOssSign();
    }

    //初始化阿里云STS KEY
    private void requestInitParams() {
        CommonInterface.requestAliyunSts(new AppRequestCallback<App_aliyun_stsActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    app_aliyun_stsActModel = actModel;
                    mOss = AliyunOssManage.getInstance().init(actModel);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                SDToast.showToast("网络异常");
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            AppDialogMenu dialog = new AppDialogMenu(PushDynamicActivity.this);
            dialog.setItems("相册", "视频");
            dialog.setCallback(new ISDDialogMenu.Callback() {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog) {
                }

                @Override
                public void onClickItem(View v, int index, SDDialogBase dialog) {
                    switch (index) {
                        case 0: // 相册
                            mark.setText("添加图片不超过9张，文字备注不超过300字");
                            if (selectType == PictureMimeType.ofImage()) {

                            } else {
                                selectType = PictureMimeType.ofImage();
                                selectList.clear();
                            }
                            maxSelectNum = 9;

                            showImageOrVideo();
                            break;
                        case 1: // 视频
                            mark.setText("添加视频不超过1个，文字备注不超过300字");
                            selectType = PictureMimeType.ofVideo();
                            selectList.clear();

                            maxSelectNum = 1;
                            showImageOrVideo();
                            break;
                        default:
                            break;
                    }
                }
            });
            dialog.showBottom();


        }
    };

    //选择图片或者视频
    private void showImageOrVideo() {
        adapter.setSelectMax(maxSelectNum);
        adapter.setList(selectList);
        adapter.notifyDataSetChanged();
        PictureSelector.create(PushDynamicActivity.this)
                .openGallery(selectType)
                .maxSelectNum(maxSelectNum)
                .previewVideo(true)
                .recordVideoSecond(60)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    //发布动态
    private void toPush() {
        showProgressDialog("正在发布...");
        if (uploadImgUrlList != null && uploadImgUrlList.size() > 0) {
            for (int i = 0; i < uploadImgUrlList.size(); i++) {
                if (i == 0) {
                    uploadImgUrl = uploadImgUrlList.get(i);
                } else {
                    uploadImgUrl = uploadImgUrl + "," + uploadImgUrlList.get(i);
                }
            }
        }

        //根据动态类型赋值多媒体参数值
        String mediaUrl = "";
        if (!TextUtils.isEmpty(uploadVideoUrl)) {
            mediaUrl = uploadVideoUrl;
            dynamicType = 3;
        } else if (!TextUtils.isEmpty(uploadImgUrl)) {
            mediaUrl = uploadImgUrl;
            dynamicType = 2;
        }

        String city = "";
        if (input_checkbox.isChecked()) {
            city = tv_position_text.getText().toString();
        } else {
            city = "";
        }
        CommonInterface.doRequestPushDynamic(
                dynamicType,
                content,
                mediaUrl,
                duration,
                uploadVideoThmbUrl,
                topicId,
                city,
                new AppRequestCallback<BaseActModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        dismissProgressDialog();
                        tvPush.setClickable(true);
                        if (actModel.isOk()) {
                            SDToast.showToast("发布成功！");

                            RefreshMessageEvent event = new RefreshMessageEvent("refresh_dynamic_list");
                            SDEventManager.post(event);
                            finish();
                        } else {
                            Toast.makeText(getActivity(), actModel.getError(), Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    protected void onError(SDResponse resp) {
                        super.onError(resp);
                        dismissProgressDialog();
                        tvPush.setClickable(true);
                    }
                });
    }


    private void getUploadOssSign() {
        if (mOss == null || app_aliyun_stsActModel == null) {
            SDToast.showToast("网络异常");
            tvPush.setClickable(true);
            getActivity().finish();
            return;
        }
        //上传音频 如果有
        if (hasVoiceFile) {
            uploadVoiceFile(new File(voiceFilePath));
        } else {
            //上传视频 图片
            uploadImgAndVideo(true);
        }
    }

    private void uploadImgAndVideo(boolean showLoadingDialog) {

        if (showLoadingDialog) {
            showProgressDialog("正在上传");
        }
        uploadImgUrlList.clear();

        if (selectType == PictureMimeType.ofImage()) {
            fileType = 0;

            for (int i = 0; i < selectList.size(); i++) {
                uploadIdCardImg(new File(selectList.get(i).getPath()), fileType);
            }

            //如果没选择图片
            if (selectList.size() == 0) {
                dismissProgressDialog();
                //发布
                toPush();
            }

        } else {
            fileType = 1;

            //如果没选择视频
            if (selectList.size() == 0) {
                dismissProgressDialog();
                //发布
                toPush();
                return;
            }

            //获取封面
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(selectList.get(0).getPath());// videoPath 本地视频的路径
            Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            File saveFile = ImageUtil.getSaveFile(bitmap, String.valueOf(System.currentTimeMillis()));

            uploadIdCardImg(new File(saveFile.getPath()), fileType);

        }
    }

    //上传音频
    private void uploadVoiceFile(File file) {


        if (mOss == null || app_aliyun_stsActModel == null) {
            SDToast.showToast("网络异常");
            tvPush.setClickable(true);
            getActivity().finish();
            return;
        }

       showProgressDialog("正在上传");

        String uploadFilePath = file.getPath();
        int i = uploadFilePath.lastIndexOf(".");
        String pf = uploadFilePath.substring(i, uploadFilePath.length());

        fileName = System.currentTimeMillis() + pf;
        objectKey = app_aliyun_stsActModel.getDir() + fileName;

        PutObjectRequest put = new PutObjectRequest(app_aliyun_stsActModel.getBucket(), objectKey, uploadFilePath);
        mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String server_path = "./" + objectKey;
                uploadAudoUrl = server_path;
//                   upLoadPicture();

                uploadImgAndVideo(false);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                SDToast.showToast("上传失败");
                tvPush.setClickable(true);
                dismissProgressDialog();
            }
        });

    }

    //上传视频
    private void uploadVideo(File file) {

        String uploadFilePath = file.getPath();
        int i = uploadFilePath.lastIndexOf(".");
        String pf = uploadFilePath.substring(i, uploadFilePath.length());

        fileName = System.currentTimeMillis() + pf;
        objectKey = app_aliyun_stsActModel.getDir() + fileName;

        PutObjectRequest put = new PutObjectRequest(app_aliyun_stsActModel.getBucket(), objectKey, uploadFilePath);
        mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String server_path = "./" + objectKey;
                uploadVideoUrl = server_path;
                dismissProgressDialog();
                //发布
                toPush();
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                SDToast.showToast("上传失败");
                tvPush.setClickable(true);
                dismissProgressDialog();
            }
        });
    }

    //上传图片或封面 0图片
    private void uploadIdCardImg(File file, final int fileType) {

        String picfileName = System.currentTimeMillis() + ".png";
        final String picobjectKey = app_aliyun_stsActModel.getDir() + picfileName;

        PutObjectRequest put = new PutObjectRequest(app_aliyun_stsActModel.getBucket(), picobjectKey, file.getPath());
        mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String server_path = "./" + picobjectKey;

                if (fileType == 0) {
                    uploadImgUrlList.add(server_path);

                    if (uploadImgUrlList.size() == selectList.size()) {
                        //发布
                        dismissProgressDialog();
                        toPush();
                    }

                } else {
                    uploadVideoThmbUrl = server_path;

                    //上传视频
                    uploadVideo(new File(selectList.get(0).getPath()));
                }
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                SDToast.showToast("上传失败");
                tvPush.setClickable(true);
                dismissProgressDialog();
            }
        });

    }


    public void onEventMainThread(VoiceRecordEvent mainThreadEvent) {
        if (mainThreadEvent.getWhat() == EventBusConfig.SOUND_FEED_RECORD_FINISH) {
            Object soundPath = mainThreadEvent.getObj();
            if (soundPath != null && soundPath instanceof String) {
                String path = (String) soundPath;
                voiceFilePath = path;
                AudioEntity entity = new AudioEntity();
                entity.setUrl(path);
                duration = AudioPlaybackManager.getDuration(path);
                if (duration <= 0) {
                    //PPLog.d(TAG, "duration <= 0");
                    PaoPaoTips.showDefault(this, "无权限");

                    File tempFile = new File(path);
                    if (tempFile.exists()) {
                        tempFile.delete();
                        return;
                    }
                } else {
                    entity.setDuration(duration / 1000);
                    mBtnVoiceRecord.setText("删除音频");
                    soundItemView.setSoundData(entity);
                    soundItemView.setVisibility(View.VISIBLE);
                    hasVoiceFile = true;
                    //PPLog.d(TAG, "soundPath:" + path);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList.addAll(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;

            }
        } else if (BogoDynamicTopicActivity.TOPIC_CODE == resultCode) {

            BogoDynamicTopicListModel bogoDynamicTopicListModel = data.getParcelableExtra(BogoDynamicTopicActivity.TOPIC_MODEL);
            topicId = bogoDynamicTopicListModel.getT_id();
            tv_topic_name.setText(bogoDynamicTopicListModel.getName());

        }
    }


    /**
     * 初始化页面 默认定位，展示地址
     */
    private void showLocation() {
        String city = getCity();
        if (TextUtils.isEmpty(city))//假如本地未缓存有效的地址，切换定位图标
        {
            //开启定位
            startLocate();
        } else {
            tv_position_text.setText(city);
        }
    }

    /**
     * 开启定位
     */
    private void startLocate() {
        tv_position_text.setText("正在定位");
        SDTencentMapManager.getInstance().startLocation(false, this);
    }


    /**
     * 获取城市
     *
     * @return
     */
    private String getCity() {
        if (isLocate == 1) {
            String city = SDTencentMapManager.getInstance().getCity();
            return !TextUtils.isEmpty(city) ? city : "";
        }
        return "";
    }


    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        TencentLocation location = SDTencentMapManager.getInstance().getLocation();
        if (isLocate == 1) {
            if (location != null) {
                tv_position_text.setText(tencentLocation.getCity());
            } else {
                tv_position_text.setText("定位失败");
            }
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
