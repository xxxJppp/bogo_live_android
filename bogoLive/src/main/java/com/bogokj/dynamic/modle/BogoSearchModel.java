package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author kn
 * @description:
 * @date :2019/12/18 11:25
 */
public class BogoSearchModel extends BaseActModel {

    /**
     * status : 1
     * error :
     * list : [{"id":"7","uid":"164739","theme":"5","addtime":"0"}]
     * act : theme_log
     * ctl : dynamic
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
         * id : 7
         * uid : 164739
         * theme : 5
         * addtime : 0
         */

        private String id;
        private String uid;
        private String theme;
        private String addtime;

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

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
