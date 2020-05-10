package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActListModel;

import java.util.List;

/**
 * Created by luodong on 2016/10/17.
 */
public class NobleListModel extends BaseActListModel {


    /**
     * noble_list : [{"nobleid":"贵族id","uid":"用户id","nick_name":"用户昵称","noble_name":"贵族名称","user_level":"用户等级","is_noble_stealth":"是否隐身 1是 0否","noble_icon":"贵族图标","noble_avatar":"贵族头像图片","sort":"排序","head_image":"用户头像","thumb_head_image":"用户头像","noble_maybe":"是否又坐席功能1 是 0否"}]
     * noble_list_sum : 列表总数
     * state : 已购买的数量
     * ctl : video
     */

    private String noble_list_sum;
    private List<NobleListBean> noble_list;

    public String getNoble_list_sum() {
        return noble_list_sum;
    }

    public void setNoble_list_sum(String noble_list_sum) {
        this.noble_list_sum = noble_list_sum;
    }

    public List<NobleListBean> getNoble_list() {
        return noble_list;
    }

    public void setNoble_list(List<NobleListBean> noble_list) {
        this.noble_list = noble_list;
    }

    public static class NobleListBean {
        /**
         * nobleid : 贵族id
         * uid : 用户id
         * nick_name : 用户昵称
         * noble_name : 贵族名称
         * user_level : 用户等级
         * is_noble_stealth : 是否隐身 1是 0否
         * noble_icon : 贵族图标
         * noble_avatar : 贵族头像图片
         * sort : 排序
         * head_image : 用户头像
         * thumb_head_image : 用户头像
         * noble_maybe : 是否又坐席功能1 是 0否
         */

        private String nobleid;
        private String uid;
        private String nick_name;
        private String noble_name;
        private String user_level;
        private String is_noble_stealth;
        private String noble_icon;
        private String noble_avatar;
        private String sort;
        private String head_image;
        private String thumb_head_image;
        private String noble_maybe;

        public String getNobleid() {
            return nobleid;
        }

        public void setNobleid(String nobleid) {
            this.nobleid = nobleid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getNoble_name() {
            return noble_name;
        }

        public void setNoble_name(String noble_name) {
            this.noble_name = noble_name;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getIs_noble_stealth() {
            return is_noble_stealth;
        }

        public void setIs_noble_stealth(String is_noble_stealth) {
            this.is_noble_stealth = is_noble_stealth;
        }

        public String getNoble_icon() {
            return noble_icon;
        }

        public void setNoble_icon(String noble_icon) {
            this.noble_icon = noble_icon;
        }

        public String getNoble_avatar() {
            return noble_avatar;
        }

        public void setNoble_avatar(String noble_avatar) {
            this.noble_avatar = noble_avatar;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getHead_image() {
            return head_image;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }

        public String getThumb_head_image() {
            return thumb_head_image;
        }

        public void setThumb_head_image(String thumb_head_image) {
            this.thumb_head_image = thumb_head_image;
        }

        public String getNoble_maybe() {
            return noble_maybe;
        }

        public void setNoble_maybe(String noble_maybe) {
            this.noble_maybe = noble_maybe;
        }
    }
}
