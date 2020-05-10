package com.bogokj.xianrou.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.hybrid.fragment.BaseFragment;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.umeng.UmengSocialManager;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.appview.room.RoomSendGiftView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.event.ECheckUserFollow;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.Is_user_followActModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.xianrou.activity.ShortVideoPlayerTouchActivity;
import com.bogokj.xianrou.appview.ScreenLoveLayout;
import com.bogokj.xianrou.common.XRCommonInterface;
import com.bogokj.xianrou.dialog.NewXRTipoffTypeDialog;
import com.bogokj.xianrou.dialog.XRShareDialog;
import com.bogokj.xianrou.event.EUserDynamicListItemRemovedEvent;
import com.bogokj.xianrou.interfaces.XRShareClickCallback;
import com.bogokj.xianrou.interfaces.XRSimpleCallback1;
import com.bogokj.xianrou.manager.XRPageRequestStateHelper;
import com.bogokj.xianrou.model.QKSmallVideoListModel;
import com.bogokj.xianrou.model.XRAddVideoPlayCountResponseModel;
import com.bogokj.xianrou.model.XRCommonActionRequestResponseModel;
import com.bogokj.xianrou.model.XRDynamicCommentResopnseModel;
import com.bogokj.xianrou.model.XRInviteModel;
import com.bogokj.xianrou.model.XRReportTypeResponseModel;
import com.bogokj.xianrou.model.XRRequestUserDynamicFavoriteResponseModel;
import com.bogokj.xianrou.model.XRUserDynamicCommentModel;
import com.bogokj.xianrou.model.XRUserDynamicDetailResponseModel;
import com.bogokj.xianrou.util.DialogUtil;
import com.bogokj.xianrou.util.Event;
import com.bogokj.xianrou.util.InputMethodUtils;
import com.bogokj.xianrou.util.PopupMenuUtil;
import com.bogokj.xianrou.util.SimpleUtils;
import com.bogokj.xianrou.util.StringUtils;
import com.bogokj.xianrou.util.ViewUtil;
import com.bogokj.xianrou.widget.varunest.sparkbutton.SparkButton;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.sunday.eventbus.SDEventManager;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;

/**
 * @author kn update
 * @description: 短视频播放页面
 * @time 2020/1/2
 */
public class ShortVideoPlayerFragment extends BaseFragment implements ITXLivePlayListener {

    public static final String VIDEO_DATA = "video_data";

    @ViewInject(R.id.ll_input)
    LinearLayout mLlInput;

    @ViewInject(R.id.tv_say)
    TextView tv_say;

    @ViewInject(R.id.et_say)
    EditText mEtSay;

    @ViewInject(R.id.tv_forward_num)
    TextView mTvForwardNum;

    @ViewInject(R.id.tv_follow_num)
    TextView mTvFollowNum;

    @ViewInject(R.id.tv_reply_list)
    TextView mTvReplyNum;

    @ViewInject(R.id.iv_avatar)
    CircleImageView mIvAvatar;

    @ViewInject(R.id.tv_user_nicename)
    TextView mTvUserNicename;

    @ViewInject(R.id.tv_short_video_title)
    TextView mTvTitle;

    @ViewInject(R.id.rl_content)
    ScreenLoveLayout mRlContent;

    @ViewInject(R.id.tv_player_num)
    TextView mTvPlayerNum;

    @ViewInject(R.id.cover)
    ImageView mImageViewBg;

    @ViewInject(R.id.video_view)
    TXCloudVideoView mTXCloudVideoView;

    @ViewInject(R.id.iv_avatar)
    CircleImageView iv_avatar;

    @ViewInject(R.id.im_video_add_follow)
    ImageView im_video_add_follow;

    @ViewInject(R.id.iv_back)
    ImageView iv_back;

    @ViewInject(R.id.iv_action)
    ImageView iv_action;

    @ViewInject(R.id.spark_button_favorite_xr_frag_user_dynamic_detail_info_video)
    SparkButton mFavoriteSparkButton;

    @ViewInject(R.id.ll_author_name_info)
    LinearLayout ll_author_name_info;

    @ViewInject(R.id.im_share)
    ImageView im_share;

    @ViewInject(R.id.im_comment)
    ImageView im_comment;

    @ViewInject(R.id.tv_send)
    TextView tv_send;

    @ViewInject(R.id.send_gift_iv)
    ImageView sendGiftIv;

    private TXLivePlayer mTXLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig = null;


    boolean mVideoPlay = false;

    boolean isplay = false;
    private QKSmallVideoListModel shortVideoBean;

    private boolean mStartSeek = false;
    private long lastClickTime = 0;

    private boolean islike = false;

    private ScreenLoveLayout screenLoveLayout;

    private String videoUrl = "";

    private String mUserId;
    private String mUserToken;
    private XRPageRequestStateHelper mRequestPageStateHelper;
    private XRUserDynamicDetailResponseModel.InfoBean mInfoBean;
    private XRInviteModel mInvite_info;
    private UMShareListener mShareListener;

    private XRUserDynamicCommentModel replyBean;

    private ShortVideoReplyListDialogFragment mShortVideoReplyDialog;
    private boolean isShowInput = false;
    private RoomSendGiftView mRoomSendGiftView;

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_short_video_player;
    }

    @Override
    protected void init() {
        super.init();
        initView();
        initData();
        initClickLister();
    }

    private void initClickLister() {
        iv_avatar.setOnClickListener(this);
        ll_author_name_info.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_action.setOnClickListener(this);
        mFavoriteSparkButton.setOnClickListener(this);
        im_share.setOnClickListener(this);
        tv_say.setOnClickListener(this);
        im_comment.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        mTXCloudVideoView.setOnClickListener(this);
        im_video_add_follow.setOnClickListener(this);
        sendGiftIv.setOnClickListener(this);
    }


    public void initData() {

        mUserId = AppRuntimeWorker.getLoginUserID();
//        mUserToken = AppContext.getInstance().getToken();
        shortVideoBean = getArguments().getParcelable(VIDEO_DATA);
        GlideUtil.load(shortVideoBean.getPhoto_image()).into(mImageViewBg);

        GlideUtil.loadHeadImage(shortVideoBean.getHead_image()).into(mIvAvatar);

        mTvTitle.setText(shortVideoBean.getContent());
        requestGetShortVideoDetailInfo();

    }


    @SuppressLint("ClickableViewAccessibility")
    public void initView() {


        mTXLivePlayer = new TXLivePlayer(getContext());
        mTXPlayConfig = new TXLivePlayConfig();

        mTXCloudVideoView.disableLog(true);
        screenLoveLayout = (ScreenLoveLayout) findViewById(R.id.rl_loveLayout);

        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.enableHardwareDecode(false);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);//RENDER_MODE_ADJUST_RESOLUTION
        mTXLivePlayer.setConfig(mTXPlayConfig);
        mTXLivePlayer.setAutoPlay(true);

        mTXCloudVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    long time = System.currentTimeMillis() - lastClickTime;
                    //TLog.log(String.valueOf(time));
                    if (time < 250) {
                        addFollowAnimation(motionEvent.getX(), motionEvent.getY());
                    }
                    lastClickTime = System.currentTimeMillis();
                }

                return false;
            }
        });

        screenLoveLayout.setMyClickCallBack(new ScreenLoveLayout.MyClickCallBack() {
            @Override
            public void oneClick() {
                if (isplay) {
                    mTXLivePlayer.pause();
                    isplay = false;
                } else {
                    mTXLivePlayer.resume();
                    isplay = true;
                }
            }

            @Override
            public void doubleClick() {
                if (islike) {

                } else {
                    requestFavorite(mInfoBean, null);
                    islike = true;
                }

            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_avatar:
            case R.id.ll_author_name_info:
                Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, shortVideoBean.getUser_id());
                startActivity(intent);
                break;
            case R.id.iv_action:

                popMoreMenu(view, shortVideoBean.getWeibo_id());
                break;

            case R.id.iv_back:

                getActivity().finish();
                break;
            case R.id.tv_say:
                changeInputStatus(true);
                break;

            case R.id.video_view:

                changeInputStatus(false);
                break;

            case R.id.tv_send:
                if (replyBean != null) {
                    requestReplyComment();
                    return;
                }
                requestReplyVideo();
                break;

//            case R.id.tv_reply_list:
            case R.id.im_comment:

                if (mShortVideoReplyDialog == null) {
                    mShortVideoReplyDialog = new ShortVideoReplyListDialogFragment();
                }

                Bundle bundle = new Bundle();
                bundle.putString(ShortVideoReplyListDialogFragment.VIDEO_ID, shortVideoBean.getWeibo_id());
                mShortVideoReplyDialog.setArguments(bundle);
                mShortVideoReplyDialog.show(getFragmentManager(), "ShortVideoReplyListDialogFragment");

                mShortVideoReplyDialog.setSendCommonListener(new ShortVideoReplyListDialogFragment.SendCommonListener() {
                    @Override
                    public void onsendCommonListener() {
                        requestGetCommonNumInfo();
                    }
                });
                break;

            case R.id.im_share:

                showShareDialog();
                break;

            case R.id.im_video_add_follow:

                requestFollow();
                break;

            case R.id.spark_button_favorite_xr_frag_user_dynamic_detail_info_video:
                if (ViewUtil.isFastClick()) {
                    return;
                }

                requestFavorite(mInfoBean, null);
                break;

            case R.id.send_gift_iv:
                break;

            default:
                break;
        }

    }


    private void requestFollow() {
        CommonInterface.requestFollow(shortVideoBean.getUser_id(), 0, new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    requestIsUserFollow();
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //正在播放
            isplay = true;
            //now it's visible to user
        } else {
            // now it's invisible to user
            if (isShowInput) {
                changeInputStatus(false);
            }
            if (mActivity != null && InputMethodUtils.isActive(mActivity)) {
                InputMethodUtils.hideSoftInput(mEtSay);
                LogUtil.d("隐藏键盘");
            }
        }
    }

    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    /**
     * @dw 视频点赞
     */
    protected void requestFavorite(final XRUserDynamicDetailResponseModel.InfoBean infoBean,
                                   @Nullable final XRSimpleCallback1<Integer> extraAction) {
        XRCommonInterface.requestDynamicFavorite(infoBean.getWeibo_id(), new AppRequestCallback<XRRequestUserDynamicFavoriteResponseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    if (actModel.isDigg()) {
                        infoBean.incFollow_num(1);
                        islike = true;
                    } else {
                        infoBean.decFollow_num(1);
                        islike = false;
                    }
                    changeFollowState(actModel.getHas_digg(), infoBean.getDigg_count());
//                    getInfoView().setInfoBean(infoBean, true);

                    notifyDynamicListItemChanged();

                    if (extraAction != null) {
                        extraAction.onCallback(actModel.getHas_digg());
                    }
                }
            }
        });
    }

    private void changeFollowState(int has_digg, String followNum) {

        boolean newState = (has_digg == 1);
        mFavoriteSparkButton.setChecked(newState);
        if (newState) {
            mFavoriteSparkButton.playAnimation();
        }
        mTvFollowNum.setText(SimpleUtils.simplifyString(followNum));

    }

    protected void notifyDynamicListItemChanged() {
        //通知动态列表
//        EUserDynamicListItemChangedEvent event = new EUserDynamicListItemChangedEvent();
//        event.fromDetail = true;
//        event.dynamicId = shortVideoBean.getWeibo_id();
//        event.has_digg = getInfoView().getInfoBean().getHas_digg();
//        event.favoriteCount = Integer.valueOf(getInfoView().getInfoBean().getDigg_count());
//        event.commentCount = Integer.valueOf(getInfoView().getInfoBean().getComment_count());
//        event.videoPlayCount = Integer.valueOf(getInfoView().getInfoBean().getVideo_count());
//
//        SDEventManager.post(event);
    }

    //发表评论回复
    private void requestReplyComment() {

        if (replyBean == null) {
            return;
        }

        String body = mEtSay.getText().toString();
        if (TextUtils.isEmpty(body)) {
            SDToast.showToast("请输入回复内容");
            return;
        }

        if (AppRuntimeWorker.getLoginUserID().equals(replyBean.getUser_id())) {
            SDToast.showToast("不能回复自己。");
            return;
        }

        showProgressDialog("正在发表回复...");
//        String content = "回复" + replyBean.getNick_name() + ":" + body;
        XRCommonInterface.requestDynamicComment(mInfoBean.getWeibo_id(), body, true, replyBean.getTo_comment_id(), new AppRequestCallback<XRDynamicCommentResopnseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                dismissProgressDialog();

                if (actModel.getStatus() == 1) {

                    mEtSay.setText("");
                    InputMethodUtils.hideSoftInput(mEtSay);
                    mTvReplyNum.setText(String.valueOf(actModel.getComment_count()));
                    mLlInput.setVisibility(View.GONE);
                    changeInputStatus(false);


                    SDToast.showToast("回复成功");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }

    protected XRPageRequestStateHelper getRequestPageStateHelper() {
        if (mRequestPageStateHelper == null) {
            mRequestPageStateHelper = new XRPageRequestStateHelper();
        }
        return mRequestPageStateHelper;
    }

    //获取视频详情
    private void requestGetShortVideoDetailInfo() {

        XRCommonInterface.requestDynamicDetail(shortVideoBean.getWeibo_id(), getRequestPageStateHelper().getCurrentPage(), new AppRequestCallback<XRUserDynamicDetailResponseModel>() {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {

                    mInfoBean = actModel.getInfo();
                    playVideo(mInfoBean);
                    mInvite_info = actModel.getInvite_info();
                    mFavoriteSparkButton.setChecked(mInfoBean.getHas_digg() == 1);
                    if (mInfoBean.getHas_digg() == 1) {
                        islike = true;
                    }

                    SDViewBinder.setTextView(mTvUserNicename, "@" + mInfoBean.getNick_name());
                    SDViewBinder.setTextView(mTvFollowNum, SimpleUtils.simplifyString(mInfoBean.getDigg_count()));
                    SDViewBinder.setTextView(mTvReplyNum, SimpleUtils.simplifyString(mInfoBean.getComment_count()));
                    videoUrl = actModel.getInfo().getVideo_url();

                    if (ShortVideoPlayerTouchActivity.select_video_id == StringUtils.toInt(shortVideoBean.getWeibo_id())
//                            ||
//                            ShortVideoTouchPlayerFragment.select_video_id == StringUtils.toInt(shortVideoBean.getWeibo_id())
                    ) {
                        startPlay();

                    }
                    if (actModel.hasNext()) {
                        getRequestPageStateHelper().turnToNextPage();
                    } else {
                        getRequestPageStateHelper().setLastPage();
                    }

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
//                getCommentDisplayFragment().stopRefreshing();
            }
        });

    }


    private void requestGetCommonNumInfo() {

        XRCommonInterface.requestDynamicDetail(shortVideoBean.getWeibo_id(), getRequestPageStateHelper().getCurrentPage(), new AppRequestCallback<XRUserDynamicDetailResponseModel>() {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    mInfoBean = actModel.getInfo();
                    SDViewBinder.setTextView(mTvUserNicename, "@" + mInfoBean.getNick_name());
                    SDViewBinder.setTextView(mTvFollowNum, SimpleUtils.simplifyString(mInfoBean.getDigg_count()));
                    SDViewBinder.setTextView(mTvReplyNum, SimpleUtils.simplifyString(mInfoBean.getComment_count()));
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });

    }

    private void playVideo(final XRUserDynamicDetailResponseModel.InfoBean infoBean) {


        XRCommonInterface.requestAddVideoPlayCount(infoBean.getWeibo_id(), new AppRequestCallback<XRAddVideoPlayCountResponseModel>() {
            @Override
            protected void onStart() {
                super.onStart();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    infoBean.setVideo_count(actModel.getVideo_count());

                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }


    //发表评论
    private void requestReplyVideo() {

        String body = mEtSay.getText().toString();
        if (TextUtils.isEmpty(body)) {
            SDToast.showToast("请输入评论内容。");
            return;
        }

        showProgressDialog("请稍后...");
        XRCommonInterface.requestDynamicComment(mInfoBean.getWeibo_id(), body, false, "", new AppRequestCallback<XRDynamicCommentResopnseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                dismissProgressDialog();

                if (actModel.getStatus() == 1) {

                    mTvReplyNum.setText(String.valueOf(actModel.getComment_count()));
                    mEtSay.setText("");
                    InputMethodUtils.hideSoftInput(mEtSay);
                    changeInputStatus(false);
                    mLlInput.setVisibility(View.GONE);

                    SDToast.showToast("评论成功");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }

    private void changeInputStatus(boolean status) {

        if (status) {
            isShowInput = true;
            mLlInput.setVisibility(View.VISIBLE);
            mEtSay.requestFocus();
            InputMethodUtils.showSoftInput(getActivity());
        } else {
            replyBean = null;
            mEtSay.setHint("");
            InputMethodUtils.hideSoftInput(getActivity());
            mLlInput.setVisibility(View.GONE);
        }
    }

    private void showShareDialog() {

        XRShareDialog dialog = new XRShareDialog(getActivity(), shareClickCallback);
        dialog.showBottom();
    }

    XRShareClickCallback shareClickCallback = new XRShareClickCallback() {
        @Override
        public void onShareQQClick(View view) {
            if (mInvite_info == null) {
                return;
            }

            UmengSocialManager.shareQQ(mInvite_info.getTitle(),
                    mInvite_info.getContent(),
                    mInvite_info.getImageUrl(),
                    mInvite_info.getClickUrl(),
                    getActivity(),
                    getShareListener());
        }

        @Override
        public void onShareWechatClick(View view) {
            if (mInvite_info == null) {
                return;
            }

            UmengSocialManager.shareWeixin(mInvite_info.getTitle(),
                    mInvite_info.getContent(),
                    mInvite_info.getImageUrl(),
                    mInvite_info.getClickUrl(),
                    getActivity(),
                    getShareListener());
        }

        @Override
        public void onShareFriendsCircleClick(View view) {
            if (mInvite_info == null) {
                return;
            }

            UmengSocialManager.shareWeixinCircle(mInvite_info.getTitle(),
                    mInvite_info.getContent(),
                    mInvite_info.getImageUrl(),
                    mInvite_info.getClickUrl(),
                    getActivity(),
                    getShareListener());
        }

        @Override
        public void onShareWeiboClick(View view) {
            if (mInvite_info == null) {
                return;
            }
            UmengSocialManager.shareSina(mInvite_info.getTitle(),
                    mInvite_info.getContent(),
                    mInvite_info.getImageUrl(),
                    mInvite_info.getClickUrl(),
                    getActivity(),
                    getShareListener());
        }

        @Override
        public void onShareQZoneClick(View view) {
            if (mInvite_info == null) {
                return;
            }

            UmengSocialManager.shareQzone(mInvite_info.getTitle(),
                    mInvite_info.getContent(),
                    mInvite_info.getImageUrl(),
                    mInvite_info.getClickUrl(),
                    getActivity(),
                    getShareListener());
        }
    };

    private UMShareListener getShareListener() {
        if (mShareListener == null) {
            mShareListener = new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    showProgressDialog("请稍候..");
                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    dismissProgressDialog();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    dismissProgressDialog();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    dismissProgressDialog();
                }
            };
        }
        return mShareListener;
    }

    /**
     * 判断当前登录用户与动态发布用户是否相同。
     *
     * @return
     */
    protected boolean isUserSelf(String dynamicUserId) {
        return UserModelDao.getUserIdInt() == Integer.valueOf(dynamicUserId);
    }

    protected void popMoreMenu(View view, final String dynamicId) {
        if (isUserSelf(shortVideoBean.getUser_id())) {
            PopupMenuUtil.popMenu(getActivity(), new int[]{1}, new String[]{getString(R.string.delete_user_dynamic)}, view,
                    new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == 1) {
                                deleteDynamic(getActivity(), dynamicId, new AppRequestCallback<XRCommonActionRequestResponseModel>() {
                                    @Override
                                    protected void onSuccess(SDResponse sdResponse) {
                                        if (actModel.isOk()) {
                                            EUserDynamicListItemRemovedEvent event = new EUserDynamicListItemRemovedEvent();
                                            event.dynamicId = dynamicId;
                                            SDEventManager.post(event);
                                            getActivity().onBackPressed();
                                        }
                                    }
                                });
                            }
                            return true;
                        }
                    });
        } else {
//            PopupMenuUtil.popMenu(getActivity(), new int[]{1, 2}, new String[]{getString(R.string.report_dynamic), getString(R.string.report_user)}, view,
            PopupMenuUtil.popMenu(getActivity(), new int[]{1}, new String[]{getString(R.string.report_user)}, view,
                    new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == 1) {
                                reportDynamic(shortVideoBean.getUser_id(), shortVideoBean.getWeibo_id(), false);
                            } else if (item.getItemId() == 2) {
                                reportDynamic(shortVideoBean.getUser_id(), shortVideoBean.getWeibo_id(), true);
                            }
                            return true;
                        }
                    });
        }
    }

    /**
     * 请求删除动态入口
     *
     * @param activity
     * @param dynamicId
     * @param callback
     */
    protected void deleteDynamic(@NonNull Activity activity, final String dynamicId, @NonNull final AppRequestCallback<XRCommonActionRequestResponseModel> callback) {
        DialogUtil.showDialog(activity, null, getString(R.string.confirm_delete_dynamic), null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                XRCommonInterface.requestDeleteUserDynamic(dynamicId, callback);
            }
        }, null, null);
    }

    /**
     * 举报动态入口
     */
    protected void reportDynamic(final String userId, final String dynamicId, final boolean forUser) {
        XRCommonInterface.requestReportType(new AppRequestCallback<XRReportTypeResponseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    popReportTypeSelectionDialog(userId, dynamicId, forUser);
                }
            }
        });
    }


    private void popReportTypeSelectionDialog(String mUserId, String dynamicId,
                                              final boolean forUser) {
        new NewXRTipoffTypeDialog(getActivity(), mUserId, dynamicId, forUser).show();
    }


    /**
     * 关注接口
     */
    private void requestIsUserFollow() {
        CommonInterface.requestIsUserFollow(shortVideoBean.getUser_id(), new AppRequestCallback<Is_user_followActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    if (actModel.getIs_focus() == 1) {
                        im_video_add_follow.setVisibility(View.GONE);
                    } else {
                        im_video_add_follow.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(null, param, event);
        }

        if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            if (mImageViewBg.isShown()) {
            }
        }
        if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS); //进度（秒数）
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION); //时间（秒数）
            Log.e("TXLiveConstantsprogress", progress + "---" + duration);
            if (progress >= duration) {
                //播放结束  2006回调不走 很迷
                stopPlay(false);
                mImageViewBg.setVisibility(View.VISIBLE);
                mTXLivePlayer.setPlayListener(this);
                startPlay();
            }

            return;

        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {


        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlay(false);
            mImageViewBg.setVisibility(View.VISIBLE);
            mTXLivePlayer.setPlayListener(this);
            startPlay();
        }


    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }


    private boolean startPlay() {
        //隐藏封面
        mImageViewBg.setVisibility(View.GONE);
        //判断是否关注
        requestIsUserFollow();

        Log.e("TXLiveConstants", videoUrl + "");
        int result = mTXLivePlayer.startPlay(videoUrl, TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result != 0) {
            //mStartPreview.setBackgroundResource(R.drawable.icon_record_start);
            return false;
        }

        mVideoPlay = true;
        return true;
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mVideoPlay = false;
        }
    }


    public void onEventMainThread(ECheckUserFollow event) {
        requestIsUserFollow();
    }

    //视频页面滑动切换
    public void onEventMainThread(Event.OnTouchShortVideoPlayerPageChange event) {
        if ((isVisible() && ShortVideoPlayerTouchActivity.select_video_id == StringUtils.toInt(shortVideoBean.getWeibo_id()))) {
            if (mTXCloudVideoView != null) {

                startPlay();
            }
        } else {
            stopPlay(true);
            if (mTXCloudVideoView != null) {
                mTXCloudVideoView.onDestroy();
            }
        }
    }


    /**
     * 点返回关闭页面后传递的消息
     *
     * @param event
     */
    public void onEventMainThread(Event.OnTouctVideoFinish event) {
        stopPlay(true);
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onDestroy();
        }
    }

    public void onEventMainThread(Event.LeftVideoTouchChangeEvent event) {
        mTXLivePlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTXLivePlayer.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTXLivePlayer.pause();
    }


    private void addFollowAnimation(float x, float y) {

//        if (StringUtils.toInt(mShortVideoDetailInfo.getFollow_state()) == 0) {
//            mShortVideoDetailInfo.setFollow_state("1");
//            requestShortVideoFollow();
//        }
//        final ImageView follow = new ImageView(getContext());
//        follow.setImageResource(R.drawable.ic_double_follow);
//        follow.setX(x);
//        follow.setY(y);
//        mRlContent.addView(follow);
//
//        ObjectAnimator animator = ObjectAnimator.ofFloat(follow, "translationY", y - 200);
//        animator.setDuration(500);
//        animator.start();
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//
//                mRlContent.removeView(follow);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });

    }

}
