package com.bogokj.dynamic.modle;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author kn create
 * @description:
 * @date : 2019/12/27
 */
public class BogoSystemMsgModel extends BaseActModel {

    /**
     * list : [{"id":"7","title":"","content":"","link_url":"","url":"https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3008142408,2229729459&fm=26&gp=0.jpg","users":"all","type":"2","pub_detail":"","addtime":"1970-01-01 08:00:00"},{"id":"8","title":"164741","content":"","link_url":"","url":"","users":"all","type":"1","pub_detail":"","addtime":"1970-01-01 08:00:00"},{"id":"15","title":"","content":"","link_url":"","url":"https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3008142408,2229729459&fm=26&gp=0.jpg","users":"all","type":"2","pub_detail":"","addtime":"1970-01-01 08:00:00"},{"id":"54","title":"test","content":"test内容","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-16 17:02:47"},{"id":"55","title":"test","content":"test内容","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-16 17:26:25"},{"id":"56","title":"test","content":"test内容","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-16 17:26:50"},{"id":"57","title":"test","content":"test内容","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-16 17:32:34"},{"id":"58","title":"test","content":"test内容","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-16 17:32:45"},{"id":"60","title":"test内容","content":"test","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-17 10:12:30"},{"id":"61","title":"test内容","content":"test","link_url":"","url":"","users":"164743","type":"1","pub_detail":"","addtime":"2019-12-17 10:12:40"}]
     * ctl : index
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
         * title :
         * content :
         * link_url :
         * url : https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3008142408,2229729459&fm=26&gp=0.jpg
         * users : all
         * type : 2
         * pub_detail :
         * addtime : 1970-01-01 08:00:00
         */

        private String id;
        private String title;
        private String content;
        private String link_url;
        private String url;
        private String users;
        private String type;
        private String pub_detail;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsers() {
            return users;
        }

        public void setUsers(String users) {
            this.users = users;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPub_detail() {
            return pub_detail;
        }

        public void setPub_detail(String pub_detail) {
            this.pub_detail = pub_detail;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
