package com.bogokj.live.model;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.library.common.SDSelectManager;
import com.bogokj.library.utils.SDTypeParseUtil;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.event.EUserLoginSuccess;
import com.bogokj.live.utils.LiveUtils;
import com.sunday.eventbus.SDEventManager;

import java.io.Serializable;
import java.util.Map;

public class UserModel extends BaseObservable implements SDSelectManager.Selectable, Serializable {
    static final long serialVersionUID = 0;

    private String user_id = ""; // 用户id
    private String luck_num = ""; // 靓号
    private String nick_name; // 昵称
    private String signature; // 签名
    private int sex; // 0-未知，1-男，2-女
    private int login_type; //0：微信；1：QQ；2：手机；3：微博 ;4 : 游客登录
    private String city; // 所在城市
    private String province;//所在省份
    private String emotional_state;//情感状态
    private String birthday;//生日
    private int is_authentication;// "0",//是否认证 0指未认证  1指待审核 2指认证 3指审核不通
    private String job;//职业
    private int is_edit_sex;//是否已编辑性别(只能编辑一次)
    private long focus_count; // 关注数量
    private String head_image; // 头像
    private long fans_count; // 粉丝数量
    private long ticket; // 钱票数量
    private long useable_ticket;//可用钱票数量
    private int user_level; // 用户等级
    private long use_diamonds; // 累计消费的钻石数量
    private long diamonds; // 钻石数量
    private String v_type;// 认证类型 0 未认证 1 普通 2企业
    private String v_icon;// 认证图标
    private String v_explain;// 认证说明
    private String home_url;// 主页
    private Map<String, String> item;// 用户其他信息列表
    private int follow_id; // 是否关注这个粉丝;0:未关注; >0：已关注
    private long video_count;// 直播数
    private int is_agree;//是否同意直播协议 0 表示不同意 1表示同意
    private int is_remind;//是否接收推送消息 0-不接收，1-接收
    // add
    private String nick_nameFormat;
    private int sort_num = -1; // 观众列表中的排序
    private int is_vip;//该用户是否为VIP 1:vip
    private String vip_expire_time;//用户vip到期时间，若非vip，则为 未开通或已过期

    private long coin; //游戏币

    //竞拍直播添加参数
    private int show_user_order;//是否显示【我的订单】 0否 1是
    private int user_order;//我的订单数(观众)
    private int show_user_pai;//我的订单数(观众)
    private int user_pai;//我的竞拍数（观众）
    private int show_podcast_order;//是否显示星店订单(主播) 0否 1是
    private int podcast_order;//星店订单数
    private int show_podcast_pai;//是否显示竞拍管理(主播) 0否 1是
    private int podcast_pai;//竞拍管理 数量(主播)
    private int show_podcast_goods;//是否显示 商品管理（主播） 0否 1是
    private int podcast_goods;//星店中的商品数量
    private int shopping_cart;//购物车个数
    private int show_shopping_cart;//是否显示购物车
    private int open_podcast_goods;//我的小店开关
    private int shop_goods;//我的小店数量

    private boolean selected;

    //家族定制添加参数
    private int family_id;//家族ID
    private int family_chieftain;//是否家族长 0：否、1：是

    //公会定制参数
    private int society_id;//公会ID
    private String society_name;//公会名称
    private int society_chieftain;//是否公会长；0：否、1：是

    //公会审核状态，0未审核，1审核通过，2拒绝通过',
    private int gh_status;

    private int show_svideo;//青稞小视频
    private int n_svideo_count;//青稞小视频数量
    //add


    private int noble_vip_type;  //0不是贵族1是贵族
    private int noble_is_avatar;  //0没有，1有 贵族头像
    private int noble_car;  //0没有，1有 贵族座驾
    private int noble_special_effects; //0没有，1有 贵族特效
    private int noble_medal;  //0没有，1有 贵族勋章
    private int noble_experience;  //0没有，1有 经验加成
    private int noble_barrage;  //0没有，1有 贵族弹幕
    private int noble_silence;  //0没有，1有 贵族防禁言
    private int noble_stealth;  //0没有，1有 贵族隐身权限

    private int stealth; // 0不隐身1隐身
    private String noble_avatar;  //贵族头像图片
    private String noble_icon;  //贵族图标
    private String noble_name;  //贵族名称
    private String user_level_color;  //用户等级颜色值
    private String noble;  //购买贵族会员h5链接
    private String members_url;  //购买vip会员h5链接
    private String luck_num_url;//我的靓号地址链接
    private String noble_car_url; //座驾动画地址
    private String noble_car_name;//座驾名称


    private String pay_car; //购买座驾地址链接

    private String guardian_list_url;

    private String guardian_avatar;
    private String guardian_name;
    private String guardian_id;

    private int seconds;

    private int is_guardian; //0:不是守护者 1：守护者

    public int getIs_guardian() {
        return is_guardian;
    }

    public void setIs_guardian(int is_guardian) {
        this.is_guardian = is_guardian;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getNoble_vip_type() {
        return noble_vip_type;
    }

    public void setNoble_vip_type(int noble_vip_type) {
        this.noble_vip_type = noble_vip_type;
    }

    public int getNoble_is_avatar() {
        return noble_is_avatar;
    }

    public void setNoble_is_avatar(int noble_is_avatar) {
        this.noble_is_avatar = noble_is_avatar;
    }

    public int getNoble_car() {
        return noble_car;
    }

    public void setNoble_car(int noble_car) {
        this.noble_car = noble_car;
    }

    public int getNoble_special_effects() {
        return noble_special_effects;
    }

    public void setNoble_special_effects(int noble_special_effects) {
        this.noble_special_effects = noble_special_effects;
    }

    public int getNoble_medal() {
        return noble_medal;
    }

    public void setNoble_medal(int noble_medal) {
        this.noble_medal = noble_medal;
    }

    public int getNoble_experience() {
        return noble_experience;
    }

    public void setNoble_experience(int noble_experience) {
        this.noble_experience = noble_experience;
    }

    public int getNoble_barrage() {
        return noble_barrage;
    }

    public void setNoble_barrage(int noble_barrage) {
        this.noble_barrage = noble_barrage;
    }

    public int getNoble_silence() {
        return noble_silence;
    }

    public void setNoble_silence(int noble_silence) {
        this.noble_silence = noble_silence;
    }

    public int getNoble_stealth() {
        return noble_stealth;
    }

    public void setNoble_stealth(int noble_stealth) {
        this.noble_stealth = noble_stealth;
    }

    public int getStealth() {
        return stealth;
    }

    public void setStealth(int stealth) {
        this.stealth = stealth;
    }

    public String getNoble_avatar() {
        return noble_avatar;
    }

    public void setNoble_avatar(String noble_avatar) {
        this.noble_avatar = noble_avatar;
    }

    public String getNoble_icon() {
        return noble_icon;
    }

    public void setNoble_icon(String noble_icon) {
        this.noble_icon = noble_icon;
    }

    public String getNoble_name() {
        return noble_name;
    }

    public void setNoble_name(String noble_name) {
        this.noble_name = noble_name;
    }

    public String getUser_level_color() {
        return user_level_color;
    }

    public void setUser_level_color(String user_level_color) {
        this.user_level_color = user_level_color;
    }

    public String getNoble() {
        return noble;
    }

    public void setNoble(String noble) {
        this.noble = noble;
    }

    public String getMembers_url() {
        return members_url;
    }

    public void setMembers_url(String members_url) {
        this.members_url = members_url;
    }

    public String getLuck_num_url() {
        return luck_num_url;
    }

    public void setLuck_num_url(String luck_num_url) {
        this.luck_num_url = luck_num_url;
    }

    public String getNoble_car_url() {
        return noble_car_url;
    }

    public void setNoble_car_url(String noble_car_url) {
        this.noble_car_url = noble_car_url;
    }

    public String getPay_car() {
        return pay_car;
    }

    public void setPay_car(String pay_car) {
        this.pay_car = pay_car;
    }

    public String getGuardian_list_url() {
        return guardian_list_url;
    }

    public void setGuardian_list_url(String guardian_list_url) {
        this.guardian_list_url = guardian_list_url;
    }

    public String getGuardian_avatar() {
        return guardian_avatar;
    }

    public void setGuardian_avatar(String guardian_avatar) {
        this.guardian_avatar = guardian_avatar;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getGuardian_id() {
        return guardian_id;
    }

    public void setGuardian_id(String guardian_id) {
        this.guardian_id = guardian_id;
    }

    public String getNoble_car_name() {
        return noble_car_name;
    }

    public void setNoble_car_name(String noble_car_name) {
        this.noble_car_name = noble_car_name;
    }


    /**
     * 是否可以在直播间发言
     *
     * @return
     */
    public boolean canSendMsg() {
        int level = AppRuntimeWorker.getSend_msg_lv();
        return user_level >= level;
    }

    /**
     * 是否可以发私信
     *
     * @return
     */
    public boolean canSendPrivateLetter() {
        int level = AppRuntimeWorker.getPrivate_letter_lv();
        return this.user_level >= level;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }

    public int getShopping_cart() {
        return shopping_cart;
    }

    public void setShopping_cart(int shopping_cart) {
        this.shopping_cart = shopping_cart;
    }

    public int getShow_shopping_cart() {
        return show_shopping_cart;
    }

    public void setShow_shopping_cart(int show_shopping_cart) {
        this.show_shopping_cart = show_shopping_cart;
    }

    public String getVip_expire_time() {
        return vip_expire_time;
    }

    public void setVip_expire_time(String vip_expire_time) {
        this.vip_expire_time = vip_expire_time;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getLuck_num() {
        return luck_num;
    }

    public void setLuck_num(String luck_num) {
        this.luck_num = luck_num;
    }

    public int getSort_num() {
        return sort_num;
    }

    public void setSort_num(int sort_num) {
        this.sort_num = sort_num;
    }

    public long getUseable_ticket() {
        return useable_ticket;
    }

    public void setUseable_ticket(long useable_ticket) {
        this.useable_ticket = useable_ticket;
    }

    public int getIs_remind() {
        return is_remind;
    }

    public void setIs_remind(int is_remind) {
        this.is_remind = is_remind;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getEmotional_state() {
        return emotional_state;
    }

    public void setEmotional_state(String emotional_state) {
        this.emotional_state = emotional_state;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIs_authentication() {
        return is_authentication;
    }

    public void setIs_authentication(int is_authentication) {
        this.is_authentication = is_authentication;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getIs_edit_sex() {
        return is_edit_sex;
    }

    public void setIs_edit_sex(int is_edit_sex) {
        this.is_edit_sex = is_edit_sex;
    }

    /**
     * 仅登录成功后保存用户数据的时候调用，其他地方不要调用
     *
     * @param user
     * @param post
     * @return
     */
    public static boolean dealLoginSuccess(UserModel user, boolean post) {
        if (user == null) {
            return false;
        }

        boolean result = false;
        CommonInterface.requestStateChangeLogin(null);
        CommonInterface.requestUser_apns(null);
        result = UserModelDao.insertOrUpdate(user);
        if (post) {
            EUserLoginSuccess event = new EUserLoginSuccess();
            SDEventManager.post(event);
        }
        return result;
    }

    public int getIs_agree() {
        return is_agree;
    }

    public void setIs_agree(int is_agree) {
        this.is_agree = is_agree;
    }

    public int getSexResId() {
        return LiveUtils.getSexImageResId(sex);
    }

    public String getNick_nameFormat() {
        if (nick_nameFormat == null) {
            nick_nameFormat = "" + nick_name + "：";
        }
        return nick_nameFormat;
    }

    public int getLevelImageResId() {
        return LiveUtils.getLevelImageResId(user_level);
    }

    // add

    /**
     * 获得游戏币余额
     *
     * @return
     */
    public long getGameCurrency() {
        if (AppRuntimeWorker.isUseGameCurrency()) {
            return getCoin();
        } else {
            return getDiamonds();
        }
    }

    /**
     * 扣除游戏币
     */
    public void payGameCurrency(long value) {
        if (AppRuntimeWorker.isUseGameCurrency()) {
            payCoins(value);
        } else {
            payDiamonds(value);
        }
    }

    /**
     * 用户剩余的游戏币是否够支付
     *
     * @param value
     * @return
     */
    public boolean canGameCurrencyPay(long value) {
        if (AppRuntimeWorker.isUseGameCurrency()) {
            return canCoinsPay(value);
        } else {
            return canDiamondsPay(value);
        }
    }

    /**
     * 更新用户游戏币
     *
     * @param value
     */
    public void updateGameCurrency(long value) {
        if (AppRuntimeWorker.isUseGameCurrency()) {
            setCoin(value);
        } else {
            setDiamonds(value);
        }
    }

    /**
     * 扣除钻石
     *
     * @param price
     */
    public void payDiamonds(long price) {
        if (price > 0) {
            diamonds = diamonds - price;
            if (diamonds < 0) {
                diamonds = 0;
            }
        }
    }

    /**
     * 扣除游戏币
     *
     * @param price
     */
    public void payCoins(long price) {
        if (price > 0) {
            coin = coin - price;
            if (coin < 0) {
                coin = 0;
            }
        }
    }

    /**
     * 用户所剩的余额够不够支付价格
     *
     * @param price 价格
     * @return true-够支付
     */
    public boolean canDiamondsPay(long price) {
        return this.diamonds >= price;
    }

    /**
     * 用户所拥有的游戏币是否足够支付
     *
     * @param price
     * @return
     */
    public boolean canCoinsPay(long price) {
        return this.coin >= price;
    }

    /**
     * 是否高级用户
     *
     * @return
     */
    public boolean isProUser() {
        boolean result = false;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            if (user_level >= model.getJr_user_level()) {
                result = true;
            }
        }
        return result;
    }

    public String getCity() {
        return city;
    }

    public int getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(int follow_id) {
        this.follow_id = follow_id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getUse_diamonds() {
        return use_diamonds;
    }

    public void setUse_diamonds(long use_diamonds) {
        this.use_diamonds = use_diamonds;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getFocus_count() {
        return focus_count;
    }

    public void setFocus_count(long focus_count) {
        this.focus_count = focus_count;
    }

    public long getFans_count() {
        return fans_count;
    }

    public void setFans_count(long fans_count) {
        this.fans_count = fans_count;
    }

    public long getTicket() {
        return ticket;
    }

    public void setTicket(long ticket) {
        this.ticket = ticket;
    }

    public long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(long diamonds) {
        if (diamonds < 0) {
            diamonds = 0;
        }
        this.diamonds = diamonds;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getUser_id() {
        return user_id;
    }

    /**
     * 获得用户展示的id
     *
     * @return
     */
    public String getShowId() {
        String result = luck_num;

        int num = SDTypeParseUtil.getInt(result);
        if (num <= 0) {
            result = user_id;
        }

        return result;
    }

    public void setUser_id(String user_id) {
        if (user_id != null) {
            this.user_id = user_id;
        }
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof UserModel)) {
            return false;
        }

        if (TextUtils.isEmpty(user_id)) {
            return false;
        }

        UserModel model = (UserModel) o;
        if (!user_id.equals(model.getUser_id())) {
            return false;
        }

        return true;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHome_url() {
        return home_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getV_icon() {
        return v_icon;
    }

    public void setV_icon(String v_icon) {
        this.v_icon = v_icon;
    }

    public String getV_explain() {
        return v_explain;
    }

    public void setV_explain(String v_explain) {
        this.v_explain = v_explain;
    }

    public Map<String, String> getItem() {
        return item;
    }

    public void setItem(Map<String, String> item) {
        this.item = item;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getVideo_count() {
        return video_count;
    }

    public void setVideo_count(long video_count) {
        this.video_count = video_count;
    }

    public void setNick_nameFormat(String nick_nameFormat) {
        this.nick_nameFormat = nick_nameFormat;
    }

    public int getShow_user_order() {
        return show_user_order;
    }

    public void setShow_user_order(int show_user_order) {
        this.show_user_order = show_user_order;
    }

    public int getUser_order() {
        return user_order;
    }

    public void setUser_order(int user_order) {
        this.user_order = user_order;
    }

    public int getShow_user_pai() {
        return show_user_pai;
    }

    public void setShow_user_pai(int show_user_pai) {
        this.show_user_pai = show_user_pai;
    }

    public int getUser_pai() {
        return user_pai;
    }

    public void setUser_pai(int user_pai) {
        this.user_pai = user_pai;
    }

    public int getShow_podcast_order() {
        return show_podcast_order;
    }

    public void setShow_podcast_order(int show_podcast_order) {
        this.show_podcast_order = show_podcast_order;
    }

    public int getPodcast_order() {
        return podcast_order;
    }

    public void setPodcast_order(int podcast_order) {
        this.podcast_order = podcast_order;
    }

    public int getShow_podcast_pai() {
        return show_podcast_pai;
    }

    public void setShow_podcast_pai(int show_podcast_pai) {
        this.show_podcast_pai = show_podcast_pai;
    }

    public int getPodcast_pai() {
        return podcast_pai;
    }

    public void setPodcast_pai(int podcast_pai) {
        this.podcast_pai = podcast_pai;
    }

    public int getShow_podcast_goods() {
        return show_podcast_goods;
    }

    public void setShow_podcast_goods(int show_podcast_goods) {
        this.show_podcast_goods = show_podcast_goods;
    }

    public int getPodcast_goods() {
        return podcast_goods;
    }

    public void setPodcast_goods(int podcast_goods) {
        this.podcast_goods = podcast_goods;
    }

    public int getFamily_id() {
        return family_id;
    }

    public void setFamily_id(int family_id) {
        this.family_id = family_id;
    }

    public int getFamily_chieftain() {
        return family_chieftain;
    }

    public void setFamily_chieftain(int family_chieftain) {
        this.family_chieftain = family_chieftain;
    }

    public int getSociety_chieftain() {
        return society_chieftain;
    }

    public void setSociety_chieftain(int society_chieftain) {
        this.society_chieftain = society_chieftain;
    }

    public int getSociety_id() {
        return society_id;
    }

    public void setSociety_id(int society_id) {
        this.society_id = society_id;
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public int getOpen_podcast_goods() {
        return open_podcast_goods;
    }

    public void setOpen_podcast_goods(int open_podcast_goods) {
        this.open_podcast_goods = open_podcast_goods;
    }

    public int getShop_goods() {
        return shop_goods;
    }

    public void setShop_goods(int shop_goods) {
        this.shop_goods = shop_goods;
    }

    public int getGh_status() {
        return gh_status;
    }

    public void setGh_status(int gh_status) {
        this.gh_status = gh_status;
    }

    public int getShow_svideo() {
        return show_svideo;
    }

    public void setShow_svideo(int show_svideo) {
        this.show_svideo = show_svideo;
    }

    public int getN_svideo_count() {
        return n_svideo_count;
    }

    public void setN_svideo_count(int n_svideo_count) {
        this.n_svideo_count = n_svideo_count;
    }
}
