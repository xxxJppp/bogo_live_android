package com.bogokj.live.common;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.Api_linkModel;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.library.config.SDConfig;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDResourcesUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.IMHelper;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.LiveInformation;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveLoginActivity;
import com.bogokj.live.dao.ToJoinLiveData;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.App_InitH5Model;
import com.bogokj.live.model.App_RegionListActModel;
import com.bogokj.live.model.CreateLiveData;
import com.bogokj.live.model.HomeTabTitleModel;
import com.bogokj.live.model.JoinLiveData;
import com.bogokj.live.model.JoinPlayBackData;
import com.bogokj.live.model.LiveRoomModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.room.LiveFragment;
import com.bogokj.live.room.activity.LiveAnchorActivity;
import com.bogokj.live.room.activity.LiveAudienceActivity;
import com.bogokj.live.room.activity.LiveBackActivity;
import com.bogokj.live.room.anchor.LiveLayoutCreaterFragment;
import com.bogokj.live.small.LiveUtils;
import com.bogokj.live.utils.LiveNetChecker;
import com.bogokj.xianrou.model.BogoVideoClassLabelModel;
import com.fanwe.lib.cache.SDDisk;

import java.util.ArrayList;
import java.util.List;

import static com.bogokj.library.utils.SDResourcesUtil.getResources;

public class AppRuntimeWorker {

    /**
     * 活的短视频分类
     *
     * @return
     */
    public static List<BogoVideoClassLabelModel> getVideoListTags() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getVideo_cate();
        }
        return new ArrayList<>();
    }

    //获取腾讯云相关的key
    public static String getTencent_video_sdk_key() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            boolean isNull = TextUtils.isEmpty(model.getTencent_video_sdk_key());
            return isNull ? "" : model.getTencent_video_sdk_key();
        }
        return "";
    }

    public static String getTencent_video_sdk_licence() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            boolean isNull = TextUtils.isEmpty(model.getTencent_video_sdk_licence());
            return isNull ? "" : model.getTencent_video_sdk_licence();
        }
        return "";
    }

    public static String getTencent_live_sdk_key() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            boolean isNull = TextUtils.isEmpty(model.getTencent_live_sdk_key());
            return isNull ? "" : model.getTencent_live_sdk_key();
        }
        return "";
    }

    public static String getTencent_live_sdk_licence() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            boolean isNull = TextUtils.isEmpty(model.getTencent_live_sdk_licence());
            return isNull ? "" : model.getTencent_live_sdk_licence();
        }
        return "";
    }


    //获取声网ID
    public static String get_agora_app_id() {

        InitActModel model = InitActModelDao.query();
        if (model != null) {
            boolean isNull = TextUtils.isEmpty(model.getAgora_app_id());
            return isNull ? "" : model.getAgora_app_id();
        }
        return "";
    }

    //是否开启付费美颜
    public static int get_is_open_bogo_beauty() {

        InitActModel model = InitActModelDao.query();
        if (model != null) {
            boolean isNull = TextUtils.isEmpty(model.getBogo_beauty_key());
            return isNull ? 0 : 1;
        }
        return 0;
    }

    /**
     * 获取是否有pk权限
     *
     * @return
     */
    public static int getIs_pk() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getIs_pk();
        }
        return 0;
    }


    /**
     * 创建直播分类是否强制选择
     *
     * @return
     */
    public static int getIs_classify() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getIs_classify();
        }
        return 0;
    }

    /**
     * 获得创建直播分类
     *
     * @return
     */
    public static List<HomeTabTitleModel> getListTags() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getVideo_classified();
        }
        return null;
    }

    /**
     * 个人中心游戏分享收益1 显示 0隐藏
     *
     * @return
     */
    public static int getGame_distribution() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getGame_distribution();
        }
        return 0;
    }


    /**
     * 返回是否不需要直播间点亮功能
     *
     * @return 1-不需要直播间点亮功能
     */
    public static int getIs_no_light() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getIs_no_light();
        } else {
            return 0;
        }
    }

    /**
     * 返回可以在直播间发言的最低等级
     *
     * @return
     */
    public static int getSend_msg_lv() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSend_msg_lv();
        } else {
            return 0;
        }
    }

    /**
     * 返回可以发私信的最低等级
     *
     * @return
     */
    public static int getPrivate_letter_lv() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getPrivate_letter_lv();
        } else {
            return 0;
        }
    }

    /**
     * @return 是否展示星店 小店插件
     */
    public static int getIsShowViewerPlugs() {
        return getResources().getInteger(R.integer.is_viewer_show_plug);
    }

    /**
     * 获得最大连麦数量
     *
     * @return
     */
    public static int getMaxLinkMicCount() {
        int count = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            count = model.getMic_max_num();
        }

        if (count < 0) {
            count = 0;
        } else if (count > LiveConstant.MAX_LINK_MIC_COUNT) {
            count = LiveConstant.MAX_LINK_MIC_COUNT;
        }
        return count;
    }

    /**
     * 是否开启我的小店
     *
     * @return
     */
    public static int getOpen_podcast_goods() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getOpen_podcast_goods();
        }
        return 0;
    }

    /**
     * @return 是否用H5打开页面
     */
    public static boolean getIsOpenWebviewMain() {
        return getResources().getBoolean(R.bool.is_open_webview_main);
    }

    /**
     * @return 按场付费总开关
     */
    public static int getLive_pay_scene() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getLive_pay_scene();
        }
        return 0;
    }

    /**
     * @return 按时付费总开关
     */
    public static int getLive_pay_time() {
        InitActModel initModel = InitActModelDao.query();
        if (initModel != null) {
            return initModel.getLive_pay_time();
        }
        return 0;
    }

    /**
     * 获得视频分辨率
     *
     * @return
     */
    public static int getVideoResolutionType() {
        InitActModel actModel = InitActModelDao.query();
        if (actModel != null) {
            return actModel.getVideo_resolution_type();
        } else {
            return LiveConstant.VideoQualityType.VIDEO_QUALITY_STANDARD;
        }
    }

    /**
     * @return 登录ID
     */
    public static String getLoginUserID() {
        String user_id = "";
        UserModel user = UserModelDao.query();
        if (user != null) {
            user_id = user.getUser_id();
        }
        return user_id;
    }

    /**
     * @return 登录用户名
     */
    public static String getLoginUserName() {
        String user_name = "";
        UserModel user = UserModelDao.query();
        if (user != null) {
            user_name = user.getNick_name();
        }
        return user_name;
    }


    /**
     * 返回im的sdkappid
     *
     * @return
     */
    public static String getSdkappid() {
        InitActModel model = InitActModelDao.query();
        if (model == null) {
            return null;
        } else {
            return model.getSdkappid();
        }
    }

    /**
     * 是否隐藏购物功能
     *
     * @return
     */
    public static int getShopping_goods() {
        InitActModel init = InitActModelDao.query();
        if (init != null) {
            return init.getShopping_goods();
        }
        return 0;
    }

    /**
     * 是否开启私信功能，开关字段由服务器下发
     *
     * @return true-开启
     */
    public static boolean hasPrivateChat() {
        InitActModel actModel = InitActModelDao.query();
        if (actModel != null) {
            return actModel.getHas_private_chat() == 1;
        }
        return true;
    }

    /**
     * @return 按时付费输入最大值
     */
    public static int getLivePayMax() {
        InitActModel init = InitActModelDao.query();
        if (init != null) {
            return init.getLive_pay_max();
        }
        return 0;
    }

    /**
     * @return 按时付费输入最小值
     */
    public static int getLivePayMin() {
        InitActModel init = InitActModelDao.query();
        if (init != null) {
            return init.getLive_pay_min() <= 0 ? 1 : init.getLive_pay_min();
        }
        return 1;
    }

    /**
     * @return 按场付费输入最大值
     */
    public static int getLivePaySceneMax() {
        InitActModel init = InitActModelDao.query();
        if (init != null) {
            return init.getLive_pay_scene_max();
        }
        return 0;
    }

    /**
     * @return 按场付费输入最小值
     */
    public static int getLivePaySceneMin() {
        InitActModel init = InitActModelDao.query();
        if (init != null) {
            return init.getLive_pay_scene_min() <= 0 ? 1 : init.getLive_pay_scene_min();
        }
        return 1;
    }

    /**
     * 设置页面是否显示推荐按钮
     *
     * @return
     */
    public static int getDistribution_module() {
        InitActModel init = InitActModelDao.query();
        if (init != null) {
            return init.getDistribution_module();
        }
        return 0;
    }


    /**
     * @return 商品管理URL
     */
    public static String getUrl_podcast_goods() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model app_InitH5Model = initActModel.getH5_url();
            if (app_InitH5Model != null) {
                return app_InitH5Model.getUrl_podcast_goods();
            }
        }
        SDToast.showToast("url_podcast_goods为空");
        return "";
    }

    //是否开启分销
    public static int getDistribution() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getDistribution();
        } else {
            return 0;
        }
    }

    /**
     * 获得最大连麦数量
     *
     * @return
     */
    public static int getMic_max_num() {
        int result = 0;

        InitActModel actModel = InitActModelDao.query();
        if (actModel != null) {
            result = actModel.getMic_max_num();
        }
        if (result <= 0) {
            result = 3;
        }

        return result;
    }

    //是否付费模式
    public static int getLive_pay() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getLive_pay();
        } else {
            return 0;
        }
    }

    //贵族勋章图片地址
    public static String getNoble_icon() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_icon();
        } else {
            return "";
        }
    }

    //用户等级颜色值
    public static String getUser_level_color() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getUser_level_color();
        } else {
            return "";
        }
    }

    //贵族头像图片地址
    public static String getNoble_avatar() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_avatar();
        } else {
            return "";
        }
    }

    //贵族隐身权限的有无
    public static boolean isNoble_stealth() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_stealth() == 1;
        } else {
            return false;
        }
    }

    //贵族隐身
    public static boolean isStealth() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getStealth() == 1;
        } else {
            return false;
        }
    }


    //贵族防止禁言
    public static boolean isNoble_silence() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_silence() == 1;
        } else {
            return false;
        }
    }

    //贵族弹幕
    public static boolean isNoble_barrage() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_barrage() == 1;
        } else {
            return false;
        }
    }


    //贵族勋章
    public static boolean isNoble_medal() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_medal() == 1;
        } else {
            return false;
        }
    }


    //贵族特效
    public static boolean isNoble_special_effects() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_special_effects() == 1;
        } else {
            return false;
        }
    }

    //贵族座驾
    public static boolean isNoble_car() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_car() == 1;
        } else {
            return false;
        }
    }

    public static String getNoble_car_url() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getH5_url().getPay_car();
        }
        return "";

    }

    public static String getVip_url() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getH5_url().getMembers_url();
        }
        return "";

    }

    /**
     * 获得VIP状态
     *
     * @return
     */
    public static boolean is_vip_user() {

        UserModel userModel = UserModelDao.query();

        return userModel != null && userModel.getIs_vip() == 1;

    }


    public static int getVideo_max_duration() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            return initActModel.getSts_video_limit();
        }
        return 0;

    }

    //0不是贵族1是贵族
    public static boolean isNoble_vip_type() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            return userModel.getNoble_vip_type() == 1;
        } else {
            return false;
        }
    }

    /**
     * 是否开启vip模块
     *
     * @return
     */
    public static boolean isOpenVip() {
        InitActModel actModel = InitActModelDao.query();
        return actModel != null && actModel.getOpen_vip() == 1;
    }

    /**
     * 是否使用游戏币，true-游戏币，false-钻石
     *
     * @return
     */
    public static boolean isUseGameCurrency() {
        InitActModel actModel = InitActModelDao.query();
        return actModel != null && actModel.getOpen_game_module() == 1 && actModel.getOpen_diamond_game_module() == 0;
    }

    /**
     * 获得游戏币单位
     *
     * @return
     */
    public static String getGameCurrencyUnit() {
        if (isUseGameCurrency()) {
            return SDResourcesUtil.getString(R.string.game_currency);
        } else {
            return getDiamondName();
        }
    }

    /**
     * 是否打开私聊送游戏币
     *
     * @return
     */
    public static boolean isOpenSendCoinsModule() {
        InitActModel actModel = InitActModelDao.query();
        return actModel != null && actModel.getOpen_send_coins_module() == 1;
    }

    /**
     * 是否开启游戏上庄功能
     *
     * @return
     */
    public static boolean isOpenBankerModule() {
        InitActModel actModel = InitActModelDao.query();
        return actModel != null && actModel.getOpen_banker_module() == 1;
    }

    //竞拍协议
    public static String getUrl_auction_agreement() {
        String url = "";
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                url = h5Model.getUrl_auction_agreement();
            }
        }
        return url;
    }

    //帮助和反馈
    public static String getUrl_help_feedback() {
        String url = "";
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                url = h5Model.getUrl_help_feedback();
            }
        }
        return url;
    }

    //关于我们
    public static String getUrl_about_us() {
        String url = "";
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                url = h5Model.getUrl_about_we();
            }
        }
        return url;
    }


    //我的等级
    public static String getUrl_my_grades() {
        String url = "";
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                url = h5Model.getUrl_my_grades();
            }
        }
        return url;
    }

    //贵族
    public static String getUrl_noble() {
        String url = "";
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                url = h5Model.getPay_noble();
            }
        }
        return url;
    }


    /**
     * 是否开启Oss上传图片
     *
     * @return 0 不开启 1 开启
     */
    public static int getOpen_sts() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getOpen_sts();
        } else {
            return 0;
        }
    }

    /**
     * 是否隐藏我的家族
     *
     * @return 0 不显示 1 显示
     */
    public static int getOpen_family_module() {
        if (getFamily_btn() == 1)
            return 1;
        else
            return 0;
    }

    public static int getFamily_btn() {
        int family_btn = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            family_btn = model.getOpen_family_module();
        }
        return family_btn;
    }

    /**
     * 是否隐藏我的公会
     *
     * @return 0 不显示 1 显示
     */
    public static int getOpen_sociaty_module() {
        if (getSociaty_btn() == 1)
            return 1;
        else
            return 0;
    }

    public static String getSociatyNmae() {
        String sociatyName = "";
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            sociatyName = model.getSociety_list_name();
        }
        return sociatyName;
    }

    public static int getSociaty_btn() {
        int sociaty_btn = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            sociaty_btn = model.getOpen_society_module();
        }
        return sociaty_btn;
    }

    /**
     * 是否隐藏所有竞拍功能
     *
     * @return
     */
    public static int getShow_hide_pai_view() {
        boolean is_show = getResources().getBoolean(R.bool.show_hide_pai_view);
        if (is_show) {
            return 1;
        } else {
            InitActModel model = InitActModelDao.query();
            if (model == null) {
                return 0;
            }

            return model.getOpen_pai_module();
        }
    }

    /**
     * 直播间是否显示虚拟竞拍按钮
     *
     * @return
     */
    public static int getPai_virtual_btn() {
        int pai_virtual_btn = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            pai_virtual_btn = model.getPai_virtual_btn();
        } else {
            pai_virtual_btn = SDResourcesUtil.getResources().getInteger(R.integer.pai_virtual_btn);
        }
        return pai_virtual_btn;
    }

    /**
     * 直播间是否显示实物竞拍按钮
     *
     * @return
     */
    public static int getPai_real_btn() {
        int pai_real_btn = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            pai_real_btn = model.getPai_real_btn();
        }
        return pai_real_btn;
    }

    /**
     * 当为1时,启用脏子过滤;默认0时不过滤
     *
     * @return
     */
    public static int getHas_dirty_words() {
        int result = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            result = model.getHas_dirty_words();
        }
        return result;
    }

    /**
     * 获得当前城市
     *
     * @return
     */
    public static String getCity_name() {
        String cityname = null;

        InitActModel model = InitActModelDao.query();
        if (model != null) {
            cityname = model.getCity();
        }
        return cityname;
    }

    /**
     * 获得钱票字符串
     *
     * @return
     */
    public static String getTicketName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getTicket_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.live_ticket);
        }
        return result;
    }

    /**
     * 获得帐号字符串
     *
     * @return
     */
    public static String getAccountName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getAccount_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.live_account);
        }
        return result;
    }

    /**
     * 获得app短名称字符串
     *
     * @return
     */
    public static String getAppShortName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getShort_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = getAppName();
        }
        return result;
    }

    /**
     * 获得app名称字符串
     *
     * @return
     */
    public static String getAppName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getApp_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.app_name);
        }
        return result;
    }

    public static App_RegionListActModel getRegionListActModel() {
        App_RegionListActModel actModel = SDDisk.open().getSerializable(App_RegionListActModel.class);
        if (actModel != null) {
            InitActModel initActModel = InitActModelDao.query();
            if (initActModel != null && initActModel.getRegion_versions() != actModel.getRegion_versions()) {
                //需要升级区域列表数据
                actModel = null;
            }
        }
        return actModel;
    }

    public static boolean isLogin(Activity activity) {
        boolean result = false;
        UserModel user = UserModelDao.query();
        if (user != null && !TextUtils.isEmpty(user.getUser_id())) {
            result = true;
        } else {
            result = false;
            if (activity != null) {
                Intent intent = new Intent(activity, LiveLoginActivity.class);
                activity.startActivity(intent);
            }
        }
        return result;
    }

    public static String getApiUrl(String ctl, String act) {
        if (!TextUtils.isEmpty(ctl) && !TextUtils.isEmpty(act)) {
            InitActModel model = InitActModelDao.query();
            if (model != null) {
                String key = ctl + "_" + act;
                List<Api_linkModel> listApi = model.getApi_link();
                if (listApi != null) {
                    for (Api_linkModel api : listApi) {
                        if (key.equals(api.getCtl_act())) {
                            String url = api.getApi();
                            return url;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getLiveRoleCreater() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_live();
        } else {
            return "user";
        }
    }

    public static String getLiveRoleViewer() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_normal();
        } else {
            return "user";
        }
    }

    public static String getLiveRoleVideoViewer() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_interact();
        } else {
            return "user";
        }
    }

    public static void setUsersig(String usersig) {
        if (usersig == null) {
            usersig = "";
        }
        SDConfig.putString(AppConfigKey.LIVE_USERSIG, usersig);
    }

    public static String getUsersig() {
        return SDConfig.getString(AppConfigKey.LIVE_USERSIG, null);
    }

    /**
     * IM登录
     *
     * @return
     */
    public static boolean startContext() {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return false;
        }
        String identifier = user.getUser_id();
        if (TextUtils.isEmpty(identifier)) {
            SDToast.showToast("用户id为空");
            return false;
        }

        String usersig = getUsersig();
        if (TextUtils.isEmpty(usersig)) {
            CommonInterface.requestUsersig(null);
            return false;
        }

        IMHelper.loginIM(identifier, usersig);
        return true;
    }

    /**
     * IM退出登录
     */
    public static void logout() {
        IMHelper.logoutIM(null);
    }

    /**
     * 打开主播界面
     *
     * @param activity
     */
    public static void openLiveCreaterActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, LiveAnchorActivity.class);
            activity.startActivity(intent);

        }
    }

    /**
     * 启动回放界面
     *
     * @param data
     * @param activity
     */
    public static void joinPlayback(final JoinPlayBackData data, final Activity activity) {
        if (activity == null || data == null) {
            return;
        }
        if (LiveInformation.getInstance().getRoomId() > 0) {
            SDToast.showToast("当前有视频正在播放");
            return;
        }

        LiveNetChecker.check(activity, new LiveNetChecker.CheckResultListener() {
            @Override
            public void onAccepted() {
                joinPlaybackInside(data, activity);
            }

            @Override
            public void onRejected() {
            }
        });
    }

    private static void joinPlaybackInside(JoinPlayBackData data, Activity activity) {
        Intent intent = new Intent(activity, LiveBackActivity.class);
        intent.putExtra("user_id", data.getUse_id());
        intent.putExtra("extra_room_id", data.getRoomId());
        activity.startActivity(intent);
    }

    /**
     * 创建直播间
     */
    public static void createLive(final CreateLiveData data, final Activity activity) {
        if (!isLogin(activity)) {
            return;
        }
        if (activity == null && data == null) {
            return;
        }

        LiveNetChecker.check(activity, new LiveNetChecker.CheckResultListener() {
            @Override
            public void onAccepted() {
                createPushLiveInside(data, activity);
            }

            @Override
            public void onRejected() {
            }
        });
    }

    /**
     * 创建推流直播间
     */
    private static void createPushLiveInside(CreateLiveData data, Activity activity) {

//        LiveAnchorActivity  LivePushCreaterActivity
        LogUtil.i("sdkType : 腾讯云");
        Intent intent = new Intent(activity, LiveAnchorActivity.class);
        intent.putExtra(LiveFragment.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(LiveLayoutCreaterFragment.EXTRA_IS_CLOSED_BACK, data.getIsClosedBack());
        activity.startActivity(intent);
        activity.finish();

        LogUtil.i("createPushLiveInside:" + data);
    }




    /**
     * 进入直播间imp
     *
     * @param //type     -1 无 0 关注  1 推荐  2附近  还有自定义分类id
     * @param //page     0 默认  页数
     * @param mData      null无 第一页数据
     * @param //position 0 默认 选中位置
     * @param model
     * @param activity
     */
    public static void joinRoom(ToJoinLiveData toJoinLiveData, List<LiveRoomModel> mData, LiveRoomModel model, Activity activity) {
        //销毁悬浮窗
        LiveUtils.destoryWindow();

        if (model.getLive_in() == 1) {
            //直播
            JoinLiveData data = new JoinLiveData();
            data.setCreaterId(model.getUser_id());
            data.setGroupId(model.getGroup_id());
            data.setLoadingVideoImageUrl(model.getLive_image());
            data.setRoomId(model.getRoom_id());
            data.setSdkType(model.getSdk_type());
            data.setCreate_type(model.getCreate_type());

            //传递其他数据
            data.setType(toJoinLiveData.getType());
            data.setPage(toJoinLiveData.getPage());
            data.setmData(mData);
            data.setPosition(toJoinLiveData.getPosition());
            data.setSex(toJoinLiveData.getSex());
            data.setCity(toJoinLiveData.getCity());

            joinLive(data, activity);

        } else if (model.getLive_in() == 3) {
            //回放
            JoinPlayBackData data = new JoinPlayBackData();
            data.setUse_id(model.getUser_id());
            data.setRoomId(model.getRoom_id());
            data.setCreate_type(model.getCreate_type());
            joinPlayback(data, activity);
        }
    }

    /**
     * 加入直播间
     *
     * @param data
     * @param activity
     */
    public static void joinLive(final JoinLiveData data, final Activity activity) {
        if (!isLogin(activity)) {
            return;
        }
        if (activity == null && data == null) {
            return;
        }

        LiveNetChecker.check(activity, new LiveNetChecker.CheckResultListener() {
            @Override
            public void onAccepted() {
                joinPushLiveInside(data, activity);
            }

            @Override
            public void onRejected() {
            }
        });
    }

    /**
     * 进入直播间
     *
     * @param data
     * @param activity
     */
    private static void joinPushLiveInside(JoinLiveData data, Activity activity) {

        UserModel user = UserModelDao.query();
        int sdk_type = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            sdk_type = model.getSdk_type();
        }

        if (user != null && user.getUser_id().equals(data.getCreaterId())) {
            CreateLiveData createLiveData = new CreateLiveData();
            createLiveData.setRoomId(data.getRoomId());
            createLiveData.setIsClosedBack(1);
            createLiveData.setSdkType(sdk_type);
            createLive(createLiveData, activity);
            return;
        }

        Log.e("dealRequestRoomInfo", "进入直播间");
        Intent intent = new Intent(activity, LiveAudienceActivity.class);
        intent.putExtra(LiveAudienceActivity.LIVE_LIST, (ArrayList<LiveRoomModel>) data.getmData());
        intent.putExtra(LiveAudienceActivity.LIVE_SELECT_POS, data.getPosition());
        intent.putExtra(LiveAudienceActivity.LIVE_LIST_PAGE, data.getPage());
        intent.putExtra(LiveAudienceActivity.LIVE_TYPE, data.getType());
        intent.putExtra(LiveAudienceActivity.LIVE_SEX, data.getSex());
        intent.putExtra(LiveAudienceActivity.LIVE_CITY, data.getCity());
        activity.startActivity(intent);
    }

    /**
     * 获得钻石字符串
     *
     * @return
     */
    public static String getDiamondName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getDiamond_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.live_diamond);
        }
        return result;
    }

    /**
     * 获得周星榜链接
     *
     * @return
     */
    public static String getWeekGiftUrl() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getH5_url().getUrl_star_index();
        }
        return result;
    }

    public static String getGoodNumberUrl() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getH5_url().getLuck_num_url();
        }
        return result;
    }

    public static String getInviteUrl() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getH5_url().getInvite_rewards();
        }
        return result;
    }
}
