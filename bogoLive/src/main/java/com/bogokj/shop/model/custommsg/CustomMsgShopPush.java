package com.bogokj.shop.model.custommsg;

import com.bogokj.live.LiveConstant;
import com.bogokj.shop.model.ShopGoodsDetailModel;


/**
 * Created by Administrator on 2016/9/20.
 */
public class CustomMsgShopPush extends CustomMsgBaseShop
{
    private ShopGoodsDetailModel goods;

    public CustomMsgShopPush()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_SHOP_GOODS_PUSH);
    }

    public ShopGoodsDetailModel getGoods() {
        return goods;
    }

    public void setGoods(ShopGoodsDetailModel goods) {
        this.goods = goods;
    }
}
