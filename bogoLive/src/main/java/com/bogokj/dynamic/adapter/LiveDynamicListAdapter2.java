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
import android.widget.TextView;

import com.bogokj.dynamic.audiorecord.entity.AudioEntity;
import com.bogokj.dynamic.audiorecord.view.CommonSoundItemView;
import com.bogokj.dynamic.modle.DynamicLikeModel;
import com.bogokj.dynamic.modle.DynamicModel;
import com.bogokj.dynamic.utils.DynamicUtils;
import com.bogokj.dynamic.utils.TimeUtils;
import com.bogokj.dynamic.view.CommentPopup;
import com.bogokj.dynamic.view.SampleCoverVideo;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.adapter.SDSimpleAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.xianrou.util.StringUtils;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;


//动态列表adapter 仿微信点赞效果
public class LiveDynamicListAdapter2 extends SDSimpleAdapter<DynamicModel> {

    public static final String TAG = "LiveDynamicListAdapter";

    private Activity mActivity;
    private String dynamicType;
    private OnDynamicClickListener onDynamicClickListener;
    private int shareCount = 0;
    private int like_count = 0;
    private int comment_count = 0;
    private int is_praise = 0;
    private ImageView item_iv_like_count;
    private TextView item_tv_like_count;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    protected boolean isFull;
    private StandardGSYVideoPlayer curPlayer;
//    private String dynamic_id;

    public LiveDynamicListAdapter2(List<DynamicModel> listModel, Activity activity) {
        super(listModel, activity);
        mActivity = activity;
    }

    public void setDynamicType(String rankingType) {
        this.dynamicType = dynamicType;
    }


    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_dynamic_2;
    }

    @Override
    public void bindData(final int position, final View convertView, ViewGroup parent, final DynamicModel model) {

//        dynamic_id = model.getId();
        LinearLayout ll_dynamic_item = convertView.findViewById(R.id.ll_dynamic_item);
        ll_dynamic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.clickItem(model.getId());
            }
        });

        TextView item_tv_content = convertView.findViewById(R.id.item_tv_content);
        item_tv_content.setText(model.getContent());


        TextView item_tv_name = convertView.findViewById(R.id.item_tv_name);
        item_tv_name.setText(model.getNick_name());

        TextView item_tv_time = convertView.findViewById(R.id.item_tv_time);
        item_tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(model.getAddtime() * 1000));

        TextView item_tv_common_count = convertView.findViewById(R.id.item_tv_common_count);
        comment_count = model.getComments();
        item_tv_common_count.setText(String.valueOf(comment_count));

        item_tv_like_count = convertView.findViewById(R.id.item_tv_like_count);
        like_count = model.getPraise();
        item_tv_like_count.setText(String.valueOf(like_count));

        final TextView item_tv_share_dynamic_count = convertView.findViewById(R.id.item_tv_share_dynamic_count);
        shareCount = model.getForwarding();
        item_tv_share_dynamic_count.setText(String.valueOf(shareCount));
        SDViewUtil.setGone(item_tv_share_dynamic_count);

        RecyclerView rv = convertView.findViewById(R.id.rv_photo_list);
        final SampleCoverVideo video = convertView.findViewById(R.id.videoplayer);

        //视频动态
        if (!TextUtils.isEmpty(model.getVideo())) {
            rv.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
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
                    video.startWindowFullscreen(mActivity, false, true);
                }
            });
            //防止错位设置
            video.setPlayTag(TAG);
            video.setPlayPosition(position);
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            video.setAutoFullWithSize(true);
            //音频焦点冲突时是否释放
            video.setReleaseWhenLossAudio(false);
            //全屏动画
            video.setShowFullAnimation(true);
            //小屏时不触摸滑动
            video.setIsTouchWiget(false);

        } else {
            video.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setLayoutManager(new GridLayoutManager(mActivity, 2));
//            rv.addItemDecoration(new CommonItemDecoration(0,0,SDViewUtil.dp2px(8),SDViewUtil.dp2px(4),SDViewUtil.dp2px(8),SDViewUtil.dp2px(4)));
            DynamicImgAdapter dynamicImgAdapter = new DynamicImgAdapter(mActivity, model);
            rv.setAdapter(dynamicImgAdapter);
        }

        if (TextUtils.isEmpty(model.getAudio())) {
            SDViewUtil.setGone(convertView.findViewById(R.id.pp_sound_item_view));
        } else {
            SDViewUtil.setVisible(convertView.findViewById(R.id.pp_sound_item_view));
        }

        CommonSoundItemView commonSoundItemView = convertView.findViewById(R.id.pp_sound_item_view);
        AudioEntity audioEntity = new AudioEntity();
        audioEntity.setDuration(model.getAudio_duration());
        audioEntity.setUrl(model.getAudio());
        commonSoundItemView.setSoundData(audioEntity);
        ImageView head_img = (ImageView) convertView.findViewById(R.id.item_iv_avatar);
        DynamicUtils.loadHttpIconImg(mActivity, model.getHead_image(), head_img, 0);
        head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.clickHeadImg(convertView, position, model);
            }
        });
        //点赞
//        helper.addOnClickListener(R.id.item_iv_like_count);

        LinearLayout ll_reply = convertView.findViewById(R.id.ll_reply);
        ll_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.clickItem(model.getId());
            }
        });


        LinearLayout ll_share_dynamic = convertView.findViewById(R.id.ll_share_dynamic);
        ll_share_dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.shareDynamic( model.getId());
                shareCount = shareCount + 1;
                item_tv_share_dynamic_count.setText(String.valueOf(shareCount));
            }
        });

        item_iv_like_count = convertView.findViewById(R.id.item_iv_like_count);
        if (StringUtils.toInt(model.getIs_like()) == 1) {
            item_iv_like_count.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_s);
            is_praise = 2;
        } else {
            is_praise = 1;
            item_iv_like_count.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_n);
        }
        LinearLayout ll_like = convertView.findViewById(R.id.ll_like);
        ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onDynamicClickListener.clickLikeDynamic(convertView, position, model, isLikeed);
//                if (!isLikeed) {
//                    item_iv_like_count.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_s);
//                    isLikeed = true;
//                    like_count = like_count + 1;
//                    item_tv_like_count.setText(String.valueOf(like_count));
//                } else {
//                    isLikeed = false;
//                    item_iv_like_count.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_n);
//                    like_count = like_count - 1;
//                    item_tv_like_count.setText(String.valueOf(like_count));
//                }

                requestLikeDynamic(model.getId());
            }
        });

        TextView item_del = convertView.findViewById(R.id.item_del);
        if (StringUtils.toInt(model.getUid()) == StringUtils.toInt(AppRuntimeWorker.getLoginUserID())) {
            SDViewUtil.setVisible(item_del);
        } else {
            SDViewUtil.setGone(item_del);
        }

        item_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDynamicClickListener.deleteDynamic(convertView, position, model);
            }
        });

        final LinearLayout ll_dynamic_menu = convertView.findViewById(R.id.ll_dynamic_menu);
        ll_dynamic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onClickMenu(model.getId(),view,ll_dynamic_menu);
            }
        });

    }

    private void onClickMenu(final String id,View v,View ll_dynamic_menu){
        CommentPopup commentPopup = new CommentPopup(getActivity(),like_count,is_praise == 2,comment_count);
        commentPopup.setOnCommentPopupClickListener(new CommentPopup.OnCommentPopupClickListener() {
            @Override
            public void onLikeClick(View v, TextView likeText) {
                requestLikeDynamic(id);
            }

            @Override
            public void onCommentClick(View v) {
                onDynamicClickListener.clickItem(id);
            }

            @Override
            public void onShareClick(View v) {
                onDynamicClickListener.shareDynamic(id);
            }
        });

        commentPopup.linkTo(v);
        commentPopup.showPopupWindow(v);
    }


    private void requestLikeDynamic(String id) {
        CommonInterface.requestLikeDynamic(id, is_praise, new AppRequestCallback<DynamicLikeModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {

                if (actModel.isOk()) {
                    if (StringUtils.toInt(actModel.getIs_like()) == 1) {
                        item_iv_like_count.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_s);
                        is_praise = 2;
                    } else {
                        is_praise = 1;
                        item_iv_like_count.setBackgroundResource(R.drawable.ic_dynamic_thumbs_up_n);
                    }

                    like_count = actModel.getCount();
                    item_tv_like_count.setText(String.valueOf(like_count));
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);

            }
        });
    }

    public void setOnDynamicClickListener(OnDynamicClickListener onDynamicClickListener) {
        this.onDynamicClickListener = onDynamicClickListener;
    }

    public interface OnDynamicClickListener {
        public void clickHeadImg(View view, int position, DynamicModel model);

        public void clickItem(String dynamic_id);

        public void deleteDynamic(View view, int position, DynamicModel model);

        public void shareDynamic(String dynamic_id);
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
