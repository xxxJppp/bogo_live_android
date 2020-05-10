package com.bogokj.dynamic.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.library.title.SDTitleItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bogokj.dynamic.adapter.DynamicCommonAdapter;
import com.bogokj.dynamic.adapter.DynamicImgAdapter;
import com.bogokj.dynamic.audiorecord.entity.AudioEntity;
import com.bogokj.dynamic.audiorecord.view.CommonSoundItemView;
import com.bogokj.dynamic.modle.DynamicCommentListModel;
import com.bogokj.dynamic.modle.DynamicCommonInfoModel;
import com.bogokj.dynamic.modle.DynamicInfoModel;
import com.bogokj.dynamic.modle.DynamicLikeModel;
import com.bogokj.dynamic.modle.DynamicModel;
import com.bogokj.dynamic.utils.DynamicUtils;
import com.bogokj.dynamic.utils.TimeUtils;
import com.bogokj.dynamic.view.SampleCoverVideo;
import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.xianrou.util.StringUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class DynamicDetailActivity extends BaseTitleActivity implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    public static final String DYNAMIC_ID = "DYNAMIC_ID";

    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_content;

    private CircleImageView iv_avatar;
    private RecyclerView rv_photo_list;
    private CommonSoundItemView pp_sound_item_view;

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_common_list;

    @ViewInject(R.id.et_input)
    EditText et_input;

    private DynamicCommonAdapter dynamicCommonAdapter;
    private String dynamic_id;
    private DynamicModel dynamicListModel;
    private int page = 1;
    private int has_next;
    private List<DynamicCommonInfoModel> list = new ArrayList<>();
    private View headView;
    private SampleCoverVideo video;
    private TextView tv_common_count;
    private TextView item_tv_like_count;
    private ImageView like_iv;
    private int is_praise;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;
    private RelativeLayout rl_video;
    private ImageView im_user_sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);

        initView();
        initData();
        initTitle();
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        onBackPressed();
    }

    private void initView() {
        headView = LayoutInflater.from(this).inflate(R.layout.dynamic_datail_layout, null);

        tv_name = headView.findViewById(R.id.item_tv_name);
        tv_content = headView.findViewById(R.id.item_tv_content);
        tv_time = headView.findViewById(R.id.item_tv_time);
        iv_avatar = headView.findViewById(R.id.item_iv_avatar);
        pp_sound_item_view = headView.findViewById(R.id.pp_sound_item_view);

        rv_photo_list = headView.findViewById(R.id.rv_photo_list);
        video = headView.findViewById(R.id.videoplayer);

        rl_video = headView.findViewById(R.id.rl_video);
        im_user_sex = headView.findViewById(R.id.im_user_sex);


        tv_common_count = headView.findViewById(R.id.item_tv_common_count);
        item_tv_like_count = headView.findViewById(R.id.item_tv_like_count);
        headView.findViewById(R.id.ll_like).setOnClickListener(this);
        like_iv = headView.findViewById(R.id.item_iv_like_count);

        initVideoPlayer();

    }

    private void initVideoPlayer() {
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, video);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        video.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                video.startWindowFullscreen(DynamicDetailActivity.this, true, true);
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("动态详情");
    }


    protected void initData() {

        dynamic_id = getIntent().getStringExtra(DYNAMIC_ID);

        requestDynamicDetail();

        findViewById(R.id.btn_publish_common).setOnClickListener(this);

        requestCommonListData();

    }

    private void requestDynamicDetail() {
        showProgressDialog("");
        CommonInterface.requestDynamicInfo(dynamic_id, new AppRequestCallback<DynamicInfoModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                dismissProgressDialog();
                if (actModel.isOk()) {

                    fillData(actModel.getData());
                } else {
                    SDToast.showToast(actModel.getError());
                    finish();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
                finish();
            }
        });
    }

    private void fillData(DynamicModel model) {
        dynamicListModel = model;
        tv_common_count.setText(String.valueOf(dynamicListModel.getComments()));
        item_tv_like_count.setText(String.valueOf(dynamicListModel.getPraise()));
        changeLikeStatus(dynamicListModel.getIs_like(), dynamicListModel.getPraise());

        tv_name.setText(dynamicListModel.getNick_name());
        tv_content.setText(dynamicListModel.getContent());

        tv_time.setText(model.getTiming());

        DynamicUtils.loadHttpIconImg(this, dynamicListModel.getHead_image(), iv_avatar, 0);

        AudioEntity audioEntity = new AudioEntity();
        audioEntity.setUrl(dynamicListModel.getAudio());
        audioEntity.setDuration(dynamicListModel.getAudio_duration());
        pp_sound_item_view.setSoundData(audioEntity);
        if (dynamicListModel.getSex() == 1) {
            im_user_sex.setImageResource(R.drawable.ic_global_male);
        } else if (dynamicListModel.getSex() == 2) {
            im_user_sex.setImageResource(R.drawable.ic_global_female);
        } else {
            im_user_sex.setVisibility(View.INVISIBLE);
        }

        //视频动态
        if (!TextUtils.isEmpty(dynamicListModel.getVideo())) {
            rv_photo_list.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            rl_video.setVisibility(View.VISIBLE);

            //系统内核模式
            PlayerFactory.setPlayManager(SystemPlayerManager.class);
            video.loadCoverImage(model.getCover_url(), R.drawable.nopic);
            video.setUpLazy(model.getVideo(), true, null, null, "关闭全屏");
            //增加title
            video.getTitleTextView().setVisibility(View.GONE);
            //设置返回键
            video.getBackButton().setVisibility(View.GONE);
            //设置全屏按键功能
            video.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video.startWindowFullscreen(DynamicDetailActivity.this, false, true);
                }
            });
            //防止错位设置
//            video.setPlayTag(TAG);
//            video.setPlayPosition(position);
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            video.setAutoFullWithSize(true);
            //音频焦点冲突时是否释放
            video.setReleaseWhenLossAudio(false);
            //全屏动画
            video.setShowFullAnimation(true);
            //小屏时不触摸滑动
            video.setIsTouchWiget(false);

            video.setVideoAllCallBack(new GSYSampleCallBack() {
                @Override
                public void onPrepared(String url, Object... objects) {
                    super.onPrepared(url, objects);
                    //开始播放了才能旋转和全屏
                    orientationUtils.setEnable(true);
                    isPlay = true;
                }

                @Override
                public void onQuitFullscreen(String url, Object... objects) {
                    super.onQuitFullscreen(url, objects);
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                    if (orientationUtils != null) {
                        orientationUtils.backToProtVideo();
                    }
                }
            });

        } else {
            video.setVisibility(View.GONE);
            rl_video.setVisibility(View.GONE);
            rv_photo_list.setVisibility(View.VISIBLE);

            rv_photo_list.setLayoutManager(new GridLayoutManager(this, 3));
            rv_photo_list.setAdapter(new DynamicImgAdapter(this, dynamicListModel));
        }

        //TODO 关闭语音
    /*    if (!TextUtils.isEmpty(dynamicListModel.getAudio())) {
            pp_sound_item_view.setVisibility(View.VISIBLE);
        } else {
            pp_sound_item_view.setVisibility(View.GONE);
        }*/
        rv_common_list.setLayoutManager(new LinearLayoutManager(this));
        dynamicCommonAdapter = new DynamicCommonAdapter(list);
        dynamicCommonAdapter.addHeaderView(headView);
        dynamicCommonAdapter.setOnLoadMoreListener(this, rv_common_list);
        dynamicCommonAdapter.disableLoadMoreIfNotFullPage();
        dynamicCommonAdapter.notifyDataSetChanged();
        rv_common_list.setAdapter(dynamicCommonAdapter);
    }


    private void requestCommonListData() {


        showProgressDialog("");
        CommonInterface.requestDynamicCommentList(dynamic_id, page, new AppRequestCallback<DynamicCommentListModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                dismissProgressDialog();
                if (actModel.isOk()) {
                    has_next = actModel.getHas_next();
                    if (actModel.getPage() == 1) {
                        list.clear();
                    }
                    if (dynamicCommonAdapter == null) {
                        dynamicCommonAdapter = new DynamicCommonAdapter(list);
                    }
                    list.addAll(actModel.getList());

                    if (has_next == 1) {
                        dynamicCommonAdapter.loadMoreComplete();
                    } else {
                        dynamicCommonAdapter.loadMoreEnd();
//                        dynamicCommonAdapter.setEnableLoadMore(false);
                    }

                    rv_common_list.setAdapter(dynamicCommonAdapter);

                    dynamicCommonAdapter.notifyDataSetChanged();
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_publish_common:

                clickPublishCommon();
                break;

            case R.id.ll_like:
                clickLike();
                break;
            default:
                break;
        }
    }

    private void changeLikeStatus(int isLike, int like_count) {

        if (StringUtils.toInt(isLike) == 1) {
            like_iv.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_s);
            is_praise = 2;
        } else {
            like_iv.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_n_gray);
            is_praise = 1;
        }

        item_tv_like_count.setText(String.valueOf(like_count));
    }

    private void clickLike() {
        showProgressDialog("");
        CommonInterface.requestLikeDynamic(dynamicListModel.getId(), is_praise, new AppRequestCallback<DynamicLikeModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {

                dismissProgressDialog();
                if (actModel.isOk()) {
                    changeLikeStatus(actModel.getIs_like(), actModel.getCount());
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }


    private void clickPublishCommon() {

        String msgContent = et_input.getText().toString();
        if (TextUtils.isEmpty(msgContent)) {
            SDToast.showToast("请输入内容！");
            return;
        }
        showProgressDialog("");
        CommonInterface.requestSendComment(dynamic_id, msgContent, new AppRequestCallback<BaseActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                dismissProgressDialog();
                if (actModel.isOk()) {

                    et_input.setText("");
                    page = 1;
                    requestCommonListData();
                } else {
                    SDToast.showToast(actModel.getError());

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        if (has_next == 1) {
            page++;
            requestCommonListData();
        } else {
            SDToast.showToast("没有更多数据了");

        }

    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }

        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    protected void onPause() {
        video.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        video.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            video.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            video.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

}
