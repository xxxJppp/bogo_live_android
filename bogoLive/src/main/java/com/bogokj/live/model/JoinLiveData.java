package com.bogokj.live.model;

import java.io.Serializable;
import java.util.List;

public class JoinLiveData implements Serializable {

    public JoinLiveData() {
    }

    public JoinLiveData(int roomId, String groupId, String createrId, String loadingVideoImageUrl, String privateKey) {
        this.roomId = roomId;
        this.groupId = groupId;
        this.createrId = createrId;
        this.loadingVideoImageUrl = loadingVideoImageUrl;
        this.privateKey = privateKey;
    }

    private int roomId;
    private String groupId;
    private String createrId;
    private String loadingVideoImageUrl;
    private String privateKey;
    private int is_small_screen;//是否小屏
    private int sdkType; //0-腾讯sdk；1-金山sdk
    //add
    private int create_type; //直播类型 0：移动端；1:PC直播


    //自己添加传递数据用，解析数据不用管
    private int type;
    private int page;
    private List<LiveRoomModel> mData;
    private int position;
    private int sex;
    private String city;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<LiveRoomModel> getmData() {
        return mData;
    }

    public void setmData(List<LiveRoomModel> mData) {
        this.mData = mData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCreate_type() {
        return create_type;
    }

    public void setCreate_type(int create_type) {
        this.create_type = create_type;
    }

    public void setSdkType(int sdkType) {
        this.sdkType = sdkType;
    }

    public int getSdkType() {
        return sdkType;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getLoadingVideoImageUrl() {
        return loadingVideoImageUrl;
    }

    public void setLoadingVideoImageUrl(String loadingVideoImageUrl) {
        this.loadingVideoImageUrl = loadingVideoImageUrl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getIs_small_screen() {
        return is_small_screen;
    }

    public void setIs_small_screen(int is_small_screen) {
        this.is_small_screen = is_small_screen;
    }
}
