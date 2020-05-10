package com.bogokj.xianrou.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 小视频列表数据模型
 * Created by LianCP on 2017/7/20.
 */
public class QKSmallVideoListModel implements Parcelable {

    private String user_id;
    private String weibo_id;
    private String head_image;//用户头像地址
    private String is_authentication;//是否认证
    private String v_icon;
    private String content;
    private String red_count;//红包数
    private String digg_count;//点赞数
    private String comment_count;//评论数
    private String video_count;//视频点击数
    private String nick_name;//昵称
    private String sort_num;
    private String photo_image;//写真封面图片
    private String city;
    private String is_top;//是否为置顶
    private String price;//售价
    private String type;//video 视频
    private String create_time;
    private String province;
    private String address;
    private String ticket_count;
    private int has_digg;
    private int has_black;
    private int is_show_weibo_report;//是否显示举报动态
    private int is_show_user_report;//是否显示举报用户
    private int is_show_user_black;//是否显示拉黑用户
    private int is_show_top;//是否显示置顶
    private int is_show_deal_weibo;//是否显示删除动态
    private String left_time;//距现在的时间（分钟）
    private String weibo_place;
    private int images_count;//图片数
    private Object images[];//	图片列表
    private String video_url;//视频连接地址
    private String juli;//距离

    public String getJuli() {
        return juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(String weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getIs_authentication() {
        return is_authentication;
    }

    public void setIs_authentication(String is_authentication) {
        this.is_authentication = is_authentication;
    }

    public String getV_icon() {
        return v_icon;
    }

    public void setV_icon(String v_icon) {
        this.v_icon = v_icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRed_count() {
        return red_count;
    }

    public void setRed_count(String red_count) {
        this.red_count = red_count;
    }

    public String getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(String digg_count) {
        this.digg_count = digg_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getVideo_count() {
        return video_count;
    }

    public void setVideo_count(String video_count) {
        this.video_count = video_count;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSort_num() {
        return sort_num;
    }

    public void setSort_num(String sort_num) {
        this.sort_num = sort_num;
    }

    public String getPhoto_image() {
        return photo_image;
    }

    public void setPhoto_image(String photo_image) {
        this.photo_image = photo_image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTicket_count() {
        return ticket_count;
    }

    public void setTicket_count(String ticket_count) {
        this.ticket_count = ticket_count;
    }

    public int getHas_digg() {
        return has_digg;
    }

    public void setHas_digg(int has_digg) {
        this.has_digg = has_digg;
    }

    public int getHas_black() {
        return has_black;
    }

    public void setHas_black(int has_black) {
        this.has_black = has_black;
    }

    public int getIs_show_weibo_report() {
        return is_show_weibo_report;
    }

    public void setIs_show_weibo_report(int is_show_weibo_report) {
        this.is_show_weibo_report = is_show_weibo_report;
    }

    public int getIs_show_user_report() {
        return is_show_user_report;
    }

    public void setIs_show_user_report(int is_show_user_report) {
        this.is_show_user_report = is_show_user_report;
    }

    public int getIs_show_user_black() {
        return is_show_user_black;
    }

    public void setIs_show_user_black(int is_show_user_black) {
        this.is_show_user_black = is_show_user_black;
    }

    public int getIs_show_top() {
        return is_show_top;
    }

    public void setIs_show_top(int is_show_top) {
        this.is_show_top = is_show_top;
    }

    public int getIs_show_deal_weibo() {
        return is_show_deal_weibo;
    }

    public void setIs_show_deal_weibo(int is_show_deal_weibo) {
        this.is_show_deal_weibo = is_show_deal_weibo;
    }

    public String getLeft_time() {
        return left_time;
    }

    public void setLeft_time(String left_time) {
        this.left_time = left_time;
    }

    public String getWeibo_place() {
        return weibo_place;
    }

    public void setWeibo_place(String weibo_place) {
        this.weibo_place = weibo_place;
    }

    public int getImages_count() {
        return images_count;
    }

    public void setImages_count(int images_count) {
        this.images_count = images_count;
    }

    public Object[] getImages() {
        return images;
    }

    public void setImages(Object[] images) {
        this.images = images;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.weibo_id);
        dest.writeString(this.head_image);
        dest.writeString(this.is_authentication);
        dest.writeString(this.v_icon);
        dest.writeString(this.content);
        dest.writeString(this.red_count);
        dest.writeString(this.digg_count);
        dest.writeString(this.comment_count);
        dest.writeString(this.video_count);
        dest.writeString(this.nick_name);
        dest.writeString(this.sort_num);
        dest.writeString(this.photo_image);
        dest.writeString(this.city);
        dest.writeString(this.is_top);
        dest.writeString(this.price);
        dest.writeString(this.type);
        dest.writeString(this.create_time);
        dest.writeString(this.province);
        dest.writeString(this.address);
        dest.writeString(this.ticket_count);
        dest.writeInt(this.has_digg);
        dest.writeInt(this.has_black);
        dest.writeInt(this.is_show_weibo_report);
        dest.writeInt(this.is_show_user_report);
        dest.writeInt(this.is_show_user_black);
        dest.writeInt(this.is_show_top);
        dest.writeInt(this.is_show_deal_weibo);
        dest.writeString(this.left_time);
        dest.writeString(this.weibo_place);
        dest.writeInt(this.images_count);
//        dest.writeParcelable(this.images, flags);
        dest.writeString(this.video_url);
    }

    public QKSmallVideoListModel() {
    }

    protected QKSmallVideoListModel(Parcel in) {
        this.user_id = in.readString();
        this.weibo_id = in.readString();
        this.head_image = in.readString();
        this.is_authentication = in.readString();
        this.v_icon = in.readString();
        this.content = in.readString();
        this.red_count = in.readString();
        this.digg_count = in.readString();
        this.comment_count = in.readString();
        this.video_count = in.readString();
        this.nick_name = in.readString();
        this.sort_num = in.readString();
        this.photo_image = in.readString();
        this.city = in.readString();
        this.is_top = in.readString();
        this.price = in.readString();
        this.type = in.readString();
        this.create_time = in.readString();
        this.province = in.readString();
        this.address = in.readString();
        this.ticket_count = in.readString();
        this.has_digg = in.readInt();
        this.has_black = in.readInt();
        this.is_show_weibo_report = in.readInt();
        this.is_show_user_report = in.readInt();
        this.is_show_user_black = in.readInt();
        this.is_show_top = in.readInt();
        this.is_show_deal_weibo = in.readInt();
        this.left_time = in.readString();
        this.weibo_place = in.readString();
        this.images_count = in.readInt();
//        this.images = in.readParcelable(Object[].class.getClassLoader());
        this.video_url = in.readString();
    }

    public static final Creator<QKSmallVideoListModel> CREATOR = new Creator<QKSmallVideoListModel>() {
        @Override
        public QKSmallVideoListModel createFromParcel(Parcel source) {
            return new QKSmallVideoListModel(source);
        }

        @Override
        public QKSmallVideoListModel[] newArray(int size) {
            return new QKSmallVideoListModel[size];
        }
    };
}
