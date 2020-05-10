package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author kn
 * @description:
 * @date :2019/12/17 13:54
 */
public class BogoPeopleNearByModel extends BaseActModel {

    /**
     * list : [{"id":"3","nick_name":"靓仔","sex":"1","head_image":"http://fw25live.oss-cn-beijing.aliyuncs.com/public/attachment/201912/3/1575269889032.png?x-oss-process=image/resize,m_mfit,h_50,w_50","v_type":"0","logout_time":"2019-12-12 11:06:46","juli":"0"},{"id":"164742","nick_name":"164742","sex":"2","head_image":"http://fw25live.oss-cn-beijing.aliyuncs.com/public/attachment/201912/164742/201912070450202812.png?x-oss-process=image/resize,m_mfit,h_50,w_50","v_type":"0","logout_time":"2019-12-12 12:09:35","juli":"0.06"},{"id":"2","nick_name":"汹涌的澎湃","sex":"1","head_image":"http://fw25live.oss-cn-beijing.aliyuncs.com/public/attachment/201904/2/1554461761501.png?x-oss-process=image/resize,m_mfit,h_50,w_50","v_type":"0","logout_time":"2019-12-12 14:18:35","juli":"15.21"}]
     * ctl : dynamic
     */

    private List<ListBean> data;

    public List<ListBean> getData() {
        return data;
    }

    public void setData(List<ListBean> data) {
        this.data = data;
    }

    public static class ListBean {
        /**
         * id : 3
         * nick_name : 靓仔
         * sex : 1
         * head_image : http://fw25live.oss-cn-beijing.aliyuncs.com/public/attachment/201912/3/1575269889032.png?x-oss-process=image/resize,m_mfit,h_50,w_50
         * v_type : 0
         * logout_time : 2019-12-12 11:06:46
         * juli : 0
         */

        private String id;
        private String nick_name;
        private String sex;
        private String head_image;
        private String v_type;
        private String logout_time;
        private String juli;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHead_image() {
            return head_image;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }

        public String getV_type() {
            return v_type;
        }

        public void setV_type(String v_type) {
            this.v_type = v_type;
        }

        public String getLogout_time() {
            return logout_time;
        }

        public void setLogout_time(String logout_time) {
            this.logout_time = logout_time;
        }

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }
    }
}
