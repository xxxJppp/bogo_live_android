package com.bogokj.live.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bogokj.library.common.SDSelectManager;

public class LiveGiftModel implements SDSelectManager.Selectable, Parcelable {

    private int id;
    private String name; // 礼物名字
    private int score; // 积分
    private int diamonds; // 钻石
    private String icon; // 图标
    private String animated_url; // 动画地址
    private String ticket; // 钱票
    private int is_much; // 1-可以连续发送多个，用于小金额礼物
    private int is_lucky;//1-是幸运礼物，
    private int sort;
    private int is_red_envelope;// 1-红包
    private String score_fromat; //主播增加的经验

    private int gift_type; //0:普通礼物 1:星榜礼物 2:VIP礼物
    private int more_multiple;//中奖的最高

    public int getMore_multiple() {
        return more_multiple;
    }

    public void setMore_multiple(int more_multiple) {
        this.more_multiple = more_multiple;
    }

    // add
    private boolean selected;

    public String getScore_fromat()
    {
        return score_fromat;
    }

    public void setScore_fromat(String score_fromat)
    {
        this.score_fromat = score_fromat;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getDiamonds()
    {
        return diamonds;
    }

    public void setDiamonds(int diamonds)
    {
        this.diamonds = diamonds;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getTicket()
    {
        return ticket;
    }

    public void setTicket(String ticket)
    {
        this.ticket = ticket;
    }

    public int getIs_much()
    {
        return is_much;
    }

    public void setIs_much(int is_much)
    {
        this.is_much = is_much;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    @Override
    public boolean isSelected()
    {
        return selected;
    }

    @Override
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public int getIs_red_envelope()
    {
        return is_red_envelope;
    }

    public void setIs_red_envelope(int is_red_envelope)
    {
        this.is_red_envelope = is_red_envelope;
    }

    public String getAnimated_url()
    {
        return animated_url;
    }

    public void setAnimated_url(String animated_url)
    {
        this.animated_url = animated_url;
    }

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
    }

    public int getIs_lucky() {
        return is_lucky;
    }

    public void setIs_lucky(int is_lucky) {
        this.is_lucky = is_lucky;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.score);
        dest.writeInt(this.diamonds);
        dest.writeString(this.icon);
        dest.writeString(this.animated_url);
        dest.writeString(this.ticket);
        dest.writeInt(this.is_much);
        dest.writeInt(this.is_lucky);
        dest.writeInt(this.sort);
        dest.writeInt(this.is_red_envelope);
        dest.writeString(this.score_fromat);
        dest.writeInt(this.gift_type);
        dest.writeInt(this.more_multiple);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public LiveGiftModel() {
    }

    protected LiveGiftModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.score = in.readInt();
        this.diamonds = in.readInt();
        this.icon = in.readString();
        this.animated_url = in.readString();
        this.ticket = in.readString();
        this.is_much = in.readInt();
        this.is_lucky = in.readInt();
        this.sort = in.readInt();
        this.is_red_envelope = in.readInt();
        this.score_fromat = in.readString();
        this.gift_type = in.readInt();
        this.more_multiple = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LiveGiftModel> CREATOR = new Parcelable.Creator<LiveGiftModel>() {
        @Override
        public LiveGiftModel createFromParcel(Parcel source) {
            return new LiveGiftModel(source);
        }

        @Override
        public LiveGiftModel[] newArray(int size) {
            return new LiveGiftModel[size];
        }
    };
}
