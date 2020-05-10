package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

import java.util.List;

public class BogoFollowRecommendModelApi extends BaseActModel {


    /**
     * msg :
     * data : {"max_page":1,"list":[{"id":"3","nick_name":"靓仔","head_image":"http://fw25live.oss-cn-beijing.aliyuncs.com/public/attachment/201912/3/1575269889032.png"}]}
     */

    private String msg;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * max_page : 1
         * list : [{"id":"3","nick_name":"靓仔","head_image":"http://fw25live.oss-cn-beijing.aliyuncs.com/public/attachment/201912/3/1575269889032.png"}]
         */

        private int max_page;
        private List<BogoFollowRecommendModel> list;

        public int getMax_page() {
            return max_page;
        }

        public void setMax_page(int max_page) {
            this.max_page = max_page;
        }

        public List<BogoFollowRecommendModel> getList() {
            return list;
        }

        public void setList(List<BogoFollowRecommendModel> list) {
            this.list = list;
        }


    }
}
