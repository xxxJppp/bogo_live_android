package com.bogokj.dynamic.modle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BogoDynamicTopicListModel implements Parcelable {

    /**
     * id : 1
     * name : #话题1#
     * num : 10
     */

    private String t_id;
    private String name;
    private String num;
    private String img;

    private String covers;
    private String today;
    private List<TodeayListModel> today_list;

    public String getCovers() {
        return covers;
    }

    public void setCovers(String covers) {
        this.covers = covers;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public List<TodeayListModel> getToday_list() {
        return today_list;
    }

    public void setToday_list(List<TodeayListModel> today_list) {
        this.today_list = today_list;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BogoDynamicTopicListModel() {
    }

    public class TodeayListModel {

        /**
         * id : 1
         * cover_url : https://ss0.pg
         */

        private int id;
        private String cover_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.t_id);
        dest.writeString(this.name);
        dest.writeString(this.num);
        dest.writeString(this.img);
        dest.writeString(this.covers);
        dest.writeString(this.today);
        dest.writeList(this.today_list);
    }

    protected BogoDynamicTopicListModel(Parcel in) {
        this.t_id = in.readString();
        this.name = in.readString();
        this.num = in.readString();
        this.img = in.readString();
        this.covers = in.readString();
        this.today = in.readString();
        this.today_list = new ArrayList<TodeayListModel>();
        in.readList(this.today_list, TodeayListModel.class.getClassLoader());
    }

    public static final Creator<BogoDynamicTopicListModel> CREATOR = new Creator<BogoDynamicTopicListModel>() {
        @Override
        public BogoDynamicTopicListModel createFromParcel(Parcel source) {
            return new BogoDynamicTopicListModel(source);
        }

        @Override
        public BogoDynamicTopicListModel[] newArray(int size) {
            return new BogoDynamicTopicListModel[size];
        }
    };
}
