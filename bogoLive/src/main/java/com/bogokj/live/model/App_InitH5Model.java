package com.bogokj.live.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/12.
 */
public class App_InitH5Model implements Serializable {
    static final long serialVersionUID = 0;

    private String url_my_grades;//我的等级
    private String url_about_we;//关于我们
    private String url_help_feedback;//帮助与反馈
    private String url_user_order;//我的订单
    private String url_user_pai;//我的竞拍
    private String url_podcast_order;//星店订单
    private String url_podcast_pai;//竞拍管理
    private String url_podcast_goods;//商品管理
    private String url_auction_agreement;//竞拍协议
    private String url_shopping_cart;//购物车

    private String url_star_index;//周星榜
    private String guartian_details;//守护规则
    private String guartian_special_effects;//守护特权url
    private String luck_num_url;
    private String members_url;
    private String pay_car;
    private String invite_rewards;

    //贵族
    private String pay_noble;

    public String getPay_noble() {
        return pay_noble;
    }

    public void setPay_noble(String pay_noble) {
        this.pay_noble = pay_noble;
    }

    public String getInvite_rewards() {
        return invite_rewards;
    }

    public void setInvite_rewards(String invite_rewards) {
        this.invite_rewards = invite_rewards;
    }

    public String getMembers_url() {
        return members_url;
    }

    public void setMembers_url(String members_url) {
        this.members_url = members_url;
    }

    public String getPay_car() {
        return pay_car;
    }

    public void setPay_car(String pay_car) {
        this.pay_car = pay_car;
    }

    public String getLuck_num_url() {
        return luck_num_url;
    }

    public void setLuck_num_url(String luck_num_url) {
        this.luck_num_url = luck_num_url;
    }

    public String getGuartian_special_effects() {
        return guartian_special_effects;
    }

    public void setGuartian_special_effects(String guartian_special_effects) {
        this.guartian_special_effects = guartian_special_effects;
    }

    public String getGuartian_details() {
        return guartian_details;
    }

    public void setGuartian_details(String guartian_details) {
        this.guartian_details = guartian_details;
    }

    public String getUrl_star_index() {
        return url_star_index;
    }

    public void setUrl_star_index(String url_star_index) {
        this.url_star_index = url_star_index;
    }

    public String getUrl_shopping_cart() {
        return url_shopping_cart;
    }

    public void setUrl_shopping_cart(String url_shopping_cart) {
        this.url_shopping_cart = url_shopping_cart;
    }

    public String getUrl_my_grades() {
        return url_my_grades;
    }

    public void setUrl_my_grades(String url_my_grades) {
        this.url_my_grades = url_my_grades;
    }

    public String getUrl_about_we() {
        return url_about_we;
    }

    public void setUrl_about_we(String url_about_we) {
        this.url_about_we = url_about_we;
    }

    public String getUrl_help_feedback() {
        return url_help_feedback;
    }

    public void setUrl_help_feedback(String url_help_feedback) {
        this.url_help_feedback = url_help_feedback;
    }

    public String getUrl_user_order() {
        return url_user_order;
    }

    public void setUrl_user_order(String url_user_order) {
        this.url_user_order = url_user_order;
    }

    public String getUrl_user_pai() {
        return url_user_pai;
    }

    public void setUrl_user_pai(String url_user_pai) {
        this.url_user_pai = url_user_pai;
    }

    public String getUrl_podcast_order() {
        return url_podcast_order;
    }

    public void setUrl_podcast_order(String url_podcast_order) {
        this.url_podcast_order = url_podcast_order;
    }

    public String getUrl_podcast_pai() {
        return url_podcast_pai;
    }

    public void setUrl_podcast_pai(String url_podcast_pai) {
        this.url_podcast_pai = url_podcast_pai;
    }

    public String getUrl_podcast_goods() {
        return url_podcast_goods;
    }

    public void setUrl_podcast_goods(String url_podcast_goods) {
        this.url_podcast_goods = url_podcast_goods;
    }

    public String getUrl_auction_agreement() {
        return url_auction_agreement;
    }

    public void setUrl_auction_agreement(String url_auction_agreement) {
        this.url_auction_agreement = url_auction_agreement;
    }


}
