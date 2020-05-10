package com.bogokj.dynamic.adapter;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.dynamic.audiorecord.entity.AudioEntity;
import com.bogokj.dynamic.audiorecord.view.CommonSoundItemView;
import com.bogokj.dynamic.modle.DynamicCommonInfoModel;
import com.bogokj.dynamic.modle.DynamicLikeModel;
import com.bogokj.dynamic.modle.DynamicModel;
import com.bogokj.dynamic.utils.DynamicUtils;
import com.bogokj.dynamic.view.CommentPopup;
import com.bogokj.dynamic.view.SampleCoverVideo;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.event.ECheckUserFollow;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.UserModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.xianrou.util.StringUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author kn
 * @description: 动态列表适配器
 * @time kn 2019/12/17
 */
public class LiveDynamicListAdapter extends BaseQuickAdapter<DynamicModel, BaseViewHolder> {

    public static final String TAG = "LiveDynamicListAdapter";
//    private List<DynamicModel> mList;

    private Activity mActivity;
    private OnDynamicClickListener onDynamicClickListener;
    private int shareCount = 0;
    private int like_count = 0;
    private int comment_count = 0;
    private ImageView item_iv_like_count;
    private TextView item_tv_like_count;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    protected boolean isFull;
    private StandardGSYVideoPlayer curPlayer;
    private String localUserId;


    public LiveDynamicListAdapter(List<DynamicModel> listModel, Activity activity) {
        super(R.layout.item_dynamic, listModel);
        mActivity = activity;

        UserModel user = UserModelDao.query();
        if (user != null) {
            localUserId = user.getUser_id();
        }
    }


    @Override
    protected void convert(BaseViewHolder helper, DynamicModel item) {
        LinearLayout ll_dynamic_item = helper.getView(R.id.ll_dynamic_item);

        TextView item_tv_content = helper.getView(R.id.item_tv_content);
        item_tv_content.setText(item.getContent());

        ImageView im_user_sex = helper.getView(R.id.im_user_sex);

        if (item.getSex() == 1) {
            im_user_sex.setImageResource(R.drawable.ic_global_male);
        } else if (item.getSex() == 2) {
            im_user_sex.setImageResource(R.drawable.ic_global_female);
        } else {
            im_user_sex.setVisibility(View.INVISIBLE);
        }

        if (item.getIs_top() == 1) {
            helper.getView(R.id.item_tv_top).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.item_tv_top).setVisibility(View.GONE);
        }

        TextView item_tv_follow = helper.getView(R.id.item_tv_follow);
        item_tv_follow.setOnClickListener(view -> CommonInterface.requestFollow(item.getUid(), 0, new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    ECheckUserFollow event = new ECheckUserFollow();
                    EventBus.getDefault().post(event);
                    item.setIs_focus(1);
                    notifyDataSetChanged();
                }
            }
        }));


        if (localUserId.equals(item.getUid())) {
            item_tv_follow.setVisibility(View.GONE);
        } else {
            item_tv_follow.setVisibility(item.getIs_focus() == 1 ? View.GONE : View.VISIBLE);

        }

        TextView item_tv_name = helper.getView(R.id.item_tv_name);
        if (!TextUtils.isEmpty(item.getNick_name())) item_tv_name.setText(item.getNick_name());

        TextView item_tv_time = helper.getView(R.id.item_tv_time);
        if (!TextUtils.isEmpty(item.getTiming())) item_tv_time.setText(item.getTiming());

        TextView item_tv_common_count = helper.getView(R.id.item_tv_common_count);
        comment_count = item.getComments();
        item_tv_common_count.setText(String.valueOf(comment_count));

        item_tv_like_count = helper.getView(R.id.item_tv_like_count);
        like_count = item.getPraise();
        item_tv_like_count.setText(String.valueOf(like_count));

        final TextView item_tv_share_dynamic_count = helper.getView(R.id.item_tv_share_dynamic_count);
        shareCount = item.getForwarding();
        item_tv_share_dynamic_count.setText(String.valueOf(shareCount));
        SDViewUtil.setGone(item_tv_share_dynamic_count);

        RecyclerView rv = helper.getView(R.id.rv_photo_list);
        final SampleCoverVideo video = helper.getView(R.id.videoplayer);
        final RelativeLayout rl_video = helper.getView(R.id.rl_video);

        //视频动态
        if (!TextUtils.isEmpty(item.getVideo())) {
            rv.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            rl_video.setVisibility(View.VISIBLE);
            //系统内核模式
            PlayerFactory.setPlayManager(SystemPlayerManager.class);
            video.loadCoverImage(item.getCover_url(), R.drawable.nopic);
            video.setUpLazy(item.getVideo(), true, null, null, "关闭全屏");
            //增加title
            video.getTitleTextView().setVisibility(View.GONE);
            //设置返回键
            video.getBackButton().setVisibility(View.GONE);
            //设置全屏按键功能
            video.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video.startWindowFullscreen(mActivity, false, true);
                }
            });
            //防止错位设置
            video.setPlayTag(TAG);
            video.setPlayPosition(helper.getPosition());
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
                    if (!video.isIfCurrentIsFullscreen()) {
                        //静音
                        GSYVideoManager.instance().setNeedMute(true);
                    }

                }

                @Override
                public void onQuitFullscreen(String url, Object... objects) {
                    super.onQuitFullscreen(url, objects);
                    //全屏不静音
                    GSYVideoManager.instance().setNeedMute(true);
                }

                @Override
                public void onEnterFullscreen(String url, Object... objects) {
                    super.onEnterFullscreen(url, objects);
                    GSYVideoManager.instance().setNeedMute(false);
                }
            });

        } else {
            video.setVisibility(View.GONE);
            rl_video.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setLayoutManager(new GridLayoutManager(mActivity, 3));
//            rv.addItemDecoration(new CommonItemDecoration(0,0,SDViewUtil.dp2px(8),SDViewUtil.dp2px(4),SDViewUtil.dp2px(8),SDViewUtil.dp2px(4)));
            DynamicImgAdapter dynamicImgAdapter = new DynamicImgAdapter(mActivity, item);
            rv.setAdapter(dynamicImgAdapter);
        }


        //TODO 关闭语音
        /*if (TextUtils.isEmpty(model.getAudio())) {
            SDViewUtil.setGone(holder.get(R.id.pp_sound_item_view));
        } else {
            SDViewUtil.setVisible(holder.get(R.id.pp_sound_item_view));
        }

        CommonSoundItemView commonSoundItemView = holder.get(R.id.pp_sound_item_view);
        AudioEntity audioEntity = new AudioEntity();
        audioEntity.setDuration(model.getAudio_duration());
        audioEntity.setUrl(model.getAudio());
        commonSoundItemView.setSoundData(audioEntity);*/
        ImageView head_img = (ImageView) helper.getView(R.id.item_iv_avatar);
        DynamicUtils.loadHttpIconImg(mActivity, item.getHead_image(), head_img, 0);
        head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.clickHeadImg(helper.getPosition(), item);
            }
        });
        //点赞
//        helper.addOnClickListener(R.id.item_iv_like_count);

        LinearLayout ll_reply = helper.getView(R.id.ll_reply);
        ll_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.clickItem(item.getId());
            }
        });


        LinearLayout ll_share_dynamic = helper.getView(R.id.ll_share_dynamic);
        ll_share_dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.shareDynamic(item.getId());
                shareCount = shareCount + 1;
                item_tv_share_dynamic_count.setText(String.valueOf(shareCount));
            }
        });

        item_iv_like_count = helper.getView(R.id.item_iv_like_count);
        if (StringUtils.toInt(item.getIs_like()) == 1) {
            item_iv_like_count.setImageResource(R.drawable.ic_dynamic_thumbs_up_s);

        } else {
            item_iv_like_count.setImageResource(R.drawable.ic_dynamic_thumbs_up_n_gray);
        }
        LinearLayout ll_like = helper.getView(R.id.ll_like);
        ll_like.setOnClickListener(view -> {
//            requestLikeDynamic(item);
            onDynamicClickListener.clickLikeDynamic(item, helper.getPosition());
        });

        TextView item_del = helper.getView(R.id.item_del);
        if (StringUtils.toInt(item.getUid()) == StringUtils.toInt(AppRuntimeWorker.getLoginUserID())) {
            SDViewUtil.setVisible(item_del);
        } else {
            SDViewUtil.setGone(item_del);
        }

        ll_dynamic_item.setOnClickListener(view -> onDynamicClickListener.clickItem(item.getId()));


        item_del.setOnClickListener(view -> onDynamicClickListener.deleteDynamic(helper.getPosition(), item));

        final LinearLayout ll_dynamic_menu = helper.getView(R.id.ll_dynamic_menu);
        ll_dynamic_menu.setOnClickListener(view -> onClickMenu(item, view, helper.getPosition()));
    }


    private void onClickMenu(final DynamicModel model, View v, int posi) {
        CommentPopup commentPopup = new CommentPopup(mActivity, like_count, model.getIs_like() == 1, comment_count);
        commentPopup.setOnCommentPopupClickListener(new CommentPopup.OnCommentPopupClickListener() {
            @Override
            public void onLikeClick(View v, TextView likeText) {
//                requestLikeDynamic(model);
                onDynamicClickListener.clickLikeDynamic(model, posi);
            }

            @Override
            public void onCommentClick(View v) {
                onDynamicClickListener.clickItem(model.getId());
            }

            @Override
            public void onShareClick(View v) {
                onDynamicClickListener.shareDynamic(model.getId());
            }
        });

        commentPopup.linkTo(v);
        commentPopup.showPopupWindow(v);
    }


    public void setOnDynamicClickListener(OnDynamicClickListener onDynamicClickListener) {
        this.onDynamicClickListener = onDynamicClickListener;
    }


    public interface OnDynamicClickListener {
        void clickHeadImg(int position, DynamicModel model);

        void clickItem(String dynamic_id);

        void deleteDynamic(int position, DynamicModel model);

        void shareDynamic(String dynamic_id);

        void clickLikeDynamic(DynamicModel model, int posi);
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        if (getListNeedAutoLand() && orientationUtils != null) {
            resolveFull();
        }
        standardGSYVideoPlayer.startWindowFullscreen(mActivity, false, true);
    }


    public void clearCache() {
        if (curPlayer != null) {
            curPlayer.getCurrentPlayer().clearCurrentCache();
        }
    }

    public boolean isFull() {
        return isFull;
    }

    /**************************支持全屏重力全屏的部分**************************/

    /**
     * 列表时是否需要支持重力旋转
     *
     * @return 返回true为支持列表重力全屏
     */
    public boolean getListNeedAutoLand() {
        return false;
    }

    private void initOrientationUtils(StandardGSYVideoPlayer standardGSYVideoPlayer, boolean full) {
        orientationUtils = new OrientationUtils((Activity) mActivity, standardGSYVideoPlayer);
        //是否需要跟随系统旋转设置
        //orientationUtils.setRotateWithSystem(false);
        orientationUtils.setEnable(false);
        orientationUtils.setIsLand((full) ? 1 : 0);
    }

    private void resolveFull() {
        if (getListNeedAutoLand() && orientationUtils != null) {
            //直接横屏
            orientationUtils.resolveByClick();
        }
    }

    private void onQuitFullscreen() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    public void onAutoComplete() {
        if (orientationUtils != null) {
            orientationUtils.setEnable(false);
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
        isPlay = false;
    }

    public void onPrepared() {
        if (orientationUtils == null) {
            return;
        }
        //开始播放了才能旋转和全屏
        orientationUtils.setEnable(true);
    }

    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        //如果旋转了就全屏
        if (isPlay && curPlayer != null && orientationUtils != null) {
            curPlayer.onConfigurationChanged(activity, newConfig, orientationUtils, false, true);
        }
    }

    public OrientationUtils getOrientationUtils() {
        return orientationUtils;
    }


    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    public void onDestroy() {
        if (isPlay && curPlayer != null) {
            curPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
    }
}
