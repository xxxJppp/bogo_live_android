package com.bogokj.shop.model;

import com.bogokj.hybrid.model.BaseActModel;


/**
 * Created by Administrator on 2016/8/23.
 */
public class App_shop_goodsActModel extends BaseActModel
{
    private ShopGoodsDetailModel info;

    public ShopGoodsDetailModel getData()
    {
        return info;
    }

    public void setData(ShopGoodsDetailModel info)
    {
        this.info = info;
    }

}
