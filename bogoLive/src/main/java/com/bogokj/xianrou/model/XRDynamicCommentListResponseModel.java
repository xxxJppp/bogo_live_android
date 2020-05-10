package com.bogokj.xianrou.model;

import java.util.List;

/**
 * @包名 com.bogokj.xianrou.model
 * @描述 动态详情请求返回数据实体
 * @作者 Su
 * @创建时间 2017/3/23 10:33
 **/
public class XRDynamicCommentListResponseModel {

    /**
     * comment_list : [{"user_id":"100992","nick_name":"美丽世界的孤儿","head_image":"http://liveimage.bogokj.net/public/attachment/201609/01/09/origin/1472663010100992.jpg","content":"1","to_comment_id":"0","to_user_id":"0","create_time":"2017-03-20 19:00:10","left_time":"21小时前","is_to_comment":0,"to_nick_name":" ","is_authentication":"0"}]
     * has_next : 0
     * status : 1
     * page : 1
     * error :
     */


    private int has_next;
    private int status;
    private int page;
    private String error;
    private List<XRUserDynamicCommentModel> comment_list;

    public int getHas_next() {
        return has_next;
    }

    public void setHas_next(int has_next) {
        this.has_next = has_next;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public List<XRUserDynamicCommentModel> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<XRUserDynamicCommentModel> comment_list) {
        this.comment_list = comment_list;
    }

    public boolean hasNext() {
        return getHas_next() == 1;
    }

}
