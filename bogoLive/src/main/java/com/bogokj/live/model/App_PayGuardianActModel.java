/**
 *
 */
package com.bogokj.live.model;

import com.bogokj.hybrid.model.BaseActModel;

/**
 * @author Administrator
 * @date 2016-5-17 下午6:54:44
 */
public class App_PayGuardianActModel extends BaseActModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String head_image;
    private Long diamonds;

    private String car_img;
    private String car_name;

    public String getCar_img() {
        return car_img;
    }

    public void setCar_img(String car_img) {
        this.car_img = car_img;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public Long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(Long diamonds) {
        this.diamonds = diamonds;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }
}
