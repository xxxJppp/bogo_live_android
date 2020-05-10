package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.dao.ToJoinLiveData;
import com.bumptech.glide.Glide;
import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.appview.LiveUserHomeNewView;
import com.bogokj.live.appview.LiveUserHomeTabCommonView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.event.ECheckUserFollow;
import com.bogokj.live.fragment.LiveUserHomeBaseFragment;
import com.bogokj.live.fragment.LiveUserHomeLeftFragment;
import com.bogokj.live.fragment.LiveUserHomeRightFragment;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.App_user_homeActModel;
import com.bogokj.live.model.LiveRoomModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.User_set_blackActModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.view.LiveUserHomeTabUnderline;
import com.bogokj.live.view.SlideToBottomScrollView;
import com.bogokj.xianrou.fragment.QKOtherSmallVideoFragment;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.common.SDSelectManager;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.select.SDSelectViewManager;

import org.xutils.x;

import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author kn update
 * @description: 个人主页
 * @time 2019/12/31
 */
public class LiveUserHomeActivity extends BaseActivity {
    public static final String EXTRA_USER_IMG_URL = "extra_user_img_url";
    public static final String EXTRA_USER_ID = "extra_user_id";
    /**
     * 家族列表跳转隐藏关注，拉黑，私信栏
     */
    public static final String EXTRA_FAMILY = "extra_family";


    private SlideToBottomScrollView lsv;

    public SlideToBottomScrollView getLsv() {
        return lsv;
    }

    private ImageView iv_blur_head;
    private View ll_close;
    private LiveUserHomeTabUnderline tab_left;
    private LiveUserHomeTabUnderline tab_right;
    private LiveUserHomeTabUnderline tab_video;

    private LiveUserHomeNewView view_live_user_info;
    private LiveUserHomeTabCommonView view_live_user_home_tab;

    private LinearLayout ll_cont;

    private ImageView iv_first;
    private ImageView iv_second;
    private ImageView iv_third;


    private ImageView iv_follow;
//    private LinearLayout ll_follow;// 关注
//    private TextView tv_follow;

    private LinearLayout ll_letter;// 私信

    private LinearLayout ll_set_black;// 拉黑
    private TextView tv_set_black;

    private LinearLayout ll_function_layout;

    private View ll_broadcast_entrance;//正在直播入口
    private TextView tv_broadcast_entrance;

    private SDSelectViewManager<LiveUserHomeTabUnderline> mSelectManager = new SDSelectViewManager<LiveUserHomeTabUnderline>();

    private int mSelectTabIndex = 0;

    private String user_id;

    private App_user_homeActModel app_user_homeActModel;

    private String familyStr;

    private boolean isSelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_home);
        x.view().inject(this);
        init();
    }

    private void init() {
        initIntentExtra();
        initView();
        initListener();
        initTab();
        initData();
        requestUser_home(false);
    }

    private void initIntentExtra() {
        user_id = getIntent().getStringExtra(EXTRA_USER_ID);
        familyStr = getIntent().getStringExtra(EXTRA_FAMILY);
    }

    private void initView() {
        lsv = (SlideToBottomScrollView) findViewById(R.id.lsv);
        iv_blur_head = (ImageView) findViewById(R.id.iv_blur_head);
        ll_close = findViewById(R.id.ll_close);
        tab_left = (LiveUserHomeTabUnderline) findViewById(R.id.tab_left);
        tab_right = (LiveUserHomeTabUnderline) findViewById(R.id.tab_right);
        tab_video = (LiveUserHomeTabUnderline) findViewById(R.id.tab_video);
        view_live_user_info = (LiveUserHomeNewView) findViewById(R.id.view_live_user_info);
        view_live_user_home_tab = (LiveUserHomeTabCommonView) findViewById(R.id.view_live_user_home_tab);
        ll_cont = (LinearLayout) findViewById(R.id.ll_cont);
        iv_first = (ImageView) findViewById(R.id.iv_first);
        iv_second = (ImageView) findViewById(R.id.iv_second);
        iv_third = (ImageView) findViewById(R.id.iv_third);
        iv_follow = view_live_user_info.getFollowView();
//        ll_follow = (LinearLayout) findViewById(R.id.ll_follow);
//        tv_follow = (TextView) findViewById(R.id.tv_follow);

        ll_letter = (LinearLayout) findViewById(R.id.ll_letter);
        ll_set_black = (LinearLayout) findViewById(R.id.ll_set_black);
        tv_set_black = (TextView) findViewById(R.id.tv_set_black);

        ll_function_layout = (LinearLayout) findViewById(R.id.ll_function_layout);
        ll_broadcast_entrance = findViewById(R.id.ll_broadcast_entrance);
        tv_broadcast_entrance = (TextView) findViewById(R.id.tv_broadcast_entrance);
    }

    private void initListener() {
        ll_close.setOnClickListener(this);
        iv_follow.setOnClickListener(this);
        iv_blur_head.setOnClickListener(this);
        ll_letter.setOnClickListener(this);
        ll_set_black.setOnClickListener(this);
        ll_broadcast_entrance.setOnClickListener(this);
        ll_cont.setOnClickListener(this);
    }

    private void initData() {
        UserModel user = UserModelDao.query();
        if (user != null) {
            String localUserId = user.getUser_id();
            if (localUserId.equals(user_id)) {
                isSelf = true;
                SDViewUtil.setGone(ll_function_layout);
            }
        }

        if (EXTRA_FAMILY.equals(familyStr)) {
            SDViewUtil.setGone(ll_function_layout);
        }
    }

    private void initTab() {
        tab_left.getTextViewTitle().setText("主页");

        tab_right.getTextViewTitle().setText("直播");

        tab_video.getTextViewTitle().setText("小视频");

        mSelectManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveUserHomeTabUnderline>() {

            @Override
            public void onNormal(int index, LiveUserHomeTabUnderline item) {
            }

            @Override
            public void onSelected(int index, LiveUserHomeTabUnderline item) {
                switch (index) {
                    case 0:
                        click0();
                        break;
                    case 1:
                        click1();
                        break;
                    case 2:
                        click2();
                        break;
                    default:
                        break;
                }
            }
        });

        mSelectManager.setItems(new LiveUserHomeTabUnderline[]
                {tab_left, tab_right, tab_video});
    }

    private void joinRoom() {
        LiveRoomModel video = app_user_homeActModel.getVideo();
        if (video == null) {
            return;
        }
        ToJoinLiveData toJoinLiveData = new ToJoinLiveData(-1, 0, 0, 0, "");
        AppRuntimeWorker.joinRoom(toJoinLiveData, null, video, this);
    }

    /**
     * 主页
     */
    protected void click0() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putSerializable(LiveUserHomeBaseFragment.EXTRA_OBJ, app_user_homeActModel);
            getSDFragmentManager().toggle(R.id.ll_content, null, LiveUserHomeLeftFragment.class, b);
        }
    }

    /**
     * 直播
     */
    protected void click1() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putSerializable(LiveUserHomeBaseFragment.EXTRA_OBJ, app_user_homeActModel);
            getSDFragmentManager().toggle(R.id.ll_content, null, LiveUserHomeRightFragment.class, b);
        }
    }

    /**
     * 小视频
     */
    protected void click2() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putString(QKOtherSmallVideoFragment.EXTRA_USER_ID, user_id);
            getSDFragmentManager().toggle(R.id.ll_content, null, QKOtherSmallVideoFragment.class, b);
        }
    }

    private void requestFollow() {
        CommonInterface.requestFollow(user_id, 0, new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    requestUser_home(true);
                    setIsFollow(actModel.getHas_focus());
                    ECheckUserFollow event = new ECheckUserFollow();
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    private void requestSet_black() {
        CommonInterface.requestSet_black(user_id, new AppRequestCallback<User_set_blackActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    // 已拉黑则刷新接口
                    if (actModel.getHas_black() == 1) {
                        requestUser_home(true);
                    }
                    setIsSet_black(actModel.getHas_black());
                }
            }
        });
    }

    // 设置个人信息关注按钮
    private void setIsFollow(int has_focus) {
        if (has_focus == 1) {
//            tv_follow.setText("已关注");
            iv_follow.setImageResource(R.drawable.ic_user_home_followed);
        } else {
//            tv_follow.setText("关注");
            iv_follow.setImageResource(R.drawable.ic_user_home_follow);
        }
    }

    // 设置个人信息拉黑按钮
    private void setIsSet_black(int has_black) {
        if (has_black == 1) {
            tv_set_black.setText("解除拉黑");
        } else {
            tv_set_black.setText("拉黑");
        }
    }

    // isRefresh是刷新就不切换Fragment
    private void requestUser_home(final boolean isRefresh) {
        CommonInterface.requestUser_home(user_id, new AppRequestCallback<App_user_homeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_user_homeActModel = actModel;

                    if (!isRefresh) {
                        mSelectManager.performClick(mSelectTabIndex);
                    }

                    setIsFollow(actModel.getHas_focus());
                    setIsSet_black(actModel.getHas_black());
                    bindData(actModel);
                    bindLlCont(actModel);
                }
            }
        });
    }

    private void bindData(App_user_homeActModel actModel) {
        if (!isSelf) {
            if (actModel.getVideo() != null) {
                SDViewUtil.setVisible(ll_broadcast_entrance);
                if (actModel.getVideo().getLive_in() == 1) {
                    tv_broadcast_entrance.setText("直播中");
                } else if (actModel.getVideo().getLive_in() == 3) {
                    tv_broadcast_entrance.setText("回播中");
                }
            } else {
                SDViewUtil.setInvisible(ll_broadcast_entrance);
            }
        }

        UserModel user = actModel.getUser();
        if (user == null) {
            return;
        }

        Glide.with(App.getApplication()).load(user.getHead_image())
                .apply(bitmapTransform(new BlurTransformation(10)).placeholder(R.drawable.bg_image_loading).error(R.drawable.bg_image_loading).dontAnimate())
                .into(iv_blur_head);

        view_live_user_info.setUserData(user);
        view_live_user_home_tab.setData(user);
    }

    private void bindLlCont(App_user_homeActModel app_user_homeActModel) {
        SDViewUtil.setVisible(iv_first);
        SDViewUtil.setVisible(iv_second);
        SDViewUtil.setVisible(iv_third);
        List<UserModel> cuser_list = app_user_homeActModel.getCuser_list();
        if (cuser_list != null && cuser_list.size() > 0) {
            SDViewUtil.setVisible(iv_first);
            SDViewUtil.setVisible(iv_second);
            SDViewUtil.setVisible(iv_third);
            if (cuser_list.size() == 1) {
                UserModel model0 = cuser_list.get(0);
                GlideUtil.loadHeadImage(model0.getHead_image()).into(iv_first);
            } else if (cuser_list.size() == 2) {
                UserModel model0 = cuser_list.get(0);
                UserModel model1 = cuser_list.get(1);
                GlideUtil.loadHeadImage(model0.getHead_image()).into(iv_first);
                GlideUtil.loadHeadImage(model1.getHead_image()).into(iv_second);
            } else if (cuser_list.size() == 3) {
                UserModel model0 = cuser_list.get(0);
                UserModel model1 = cuser_list.get(1);
                UserModel model2 = cuser_list.get(2);
                GlideUtil.loadHeadImage(model0.getHead_image()).into(iv_first);
                GlideUtil.loadHeadImage(model1.getHead_image()).into(iv_second);
                GlideUtil.loadHeadImage(model2.getHead_image()).into(iv_third);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ll_close) {
            clickLlClose();
        } else if (v == iv_follow) {
            clickLlFollow();
        } else if (v == ll_letter) {
            clickLlLetter();
        } else if (v == ll_set_black) {
            clickLlSetBlack();
        } else if (v == ll_broadcast_entrance) {
            clickLlBroadcastEntrance();
        } else if (v == ll_cont) {
            clickLlCont();
        } else if (v == iv_blur_head) {
            clickFlHead();
        }
    }

    private void clickLlClose() {
        finish();
    }

    private void clickLlFollow() {
        requestFollow();
    }

    private void clickLlLetter() {
        if (app_user_homeActModel == null) {
            return;
        }
        UserModel to_user = app_user_homeActModel.getUser();
        if (to_user == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LivePrivateChatActivity.class);
        intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, to_user.getUser_id());
        startActivity(intent);
    }

    private void clickLlSetBlack() {
        requestSet_black();
    }

    private void clickLlBroadcastEntrance() {
        //加入直播间
        joinRoom();
    }

    private void clickLlCont() {
        if (app_user_homeActModel == null) {
            return;
        }

        UserModel user = app_user_homeActModel.getUser();
        if (user == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
        intent.putExtra(LiveMySelfContActivity.EXTRA_USER_ID, user.getUser_id());
        startActivity(intent);

    }

    // 头像
    protected void clickFlHead() {
        if (app_user_homeActModel == null) {
            return;
        }

        UserModel user = app_user_homeActModel.getUser();
        if (user == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), LiveUserHeadImageActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, user.getUser_id());
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, user.getHead_image());
        getActivity().startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUser_home(true);
    }
}
