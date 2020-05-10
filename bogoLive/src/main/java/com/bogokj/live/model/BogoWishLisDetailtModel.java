package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author kn
 * @description:
 * @date :2019/12/19 9:30
 */
public class BogoWishLisDetailtModel extends BaseActModel {


    /**
     * status : 1
     * error :
     * list : [{"id":"6","uid":"164739","g_id":"1","g_name":"香蕉","g_icon":"http://fw25live.oss-cn-beijing.aliyuncs.com/public/gift/a8.png","g_num":"10","txt":"哈哈哈","add_day":"1575907200","add_time":"1575942946","g_now_num":0,"top_user":[{"head_image":"http://www.baidu.com","nick_name":"aaa","gift_num":"10"}]}]
     * act : wish_list
     * ctl : user_wish
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 6
         * uid : 164739
         * g_id : 1
         * g_name : 香蕉
         * g_icon : http://fw25live.oss-cn-beijing.aliyuncs.com/public/gift/a8.png
         * g_num : 10
         * txt : 哈哈哈
         * add_day : 1575907200
         * add_time : 1575942946
         * g_now_num : 0
         * top_user : [{"head_image":"http://www.baidu.com","nick_name":"aaa","gift_num":"10"}]
         */

        private String id;
        private String uid;
        private String g_id;
        private String g_name;
        private String g_icon;
        private String g_num;
        private String txt;
        private String add_day;
        private String add_time;
        private int g_now_num;
        private List<TopUserBean> top_user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getG_id() {
            return g_id;
        }

        public void setG_id(String g_id) {
            this.g_id = g_id;
        }

        public String getG_name() {
            return g_name;
        }

        public void setG_name(String g_name) {
            this.g_name = g_name;
        }

        public String getG_icon() {
            return g_icon;
        }

        public void setG_icon(String g_icon) {
            this.g_icon = g_icon;
        }

        public String getG_num() {
            return g_num;
        }

        public void setG_num(String g_num) {
            this.g_num = g_num;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getAdd_day() {
            return add_day;
        }

        public void setAdd_day(String add_day) {
            this.add_day = add_day;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getG_now_num() {
            return g_now_num;
        }

        public void setG_now_num(int g_now_num) {
            this.g_now_num = g_now_num;
        }

        public List<TopUserBean> getTop_user() {
            return top_user;
        }

        public void setTop_user(List<TopUserBean> top_user) {
            this.top_user = top_user;
        }

        public static class TopUserBean {
            /**
             * head_image : http://www.baidu.com
             * nick_name : aaa
             * gift_num : 10
             */

            private String head_image;
            private String nick_name;
            private String gift_num;

            public String getHead_image() {
                return head_image;
            }

            public void setHead_image(String head_image) {
                this.head_image = head_image;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getGift_num() {
                return gift_num;
            }

            public void setGift_num(String gift_num) {
                this.gift_num = gift_num;
            }
        }
    }
}
