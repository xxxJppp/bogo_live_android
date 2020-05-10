package com.bogokj.auction.dialog;

import android.app.Activity;

import com.bogokj.auction.AuctionBusiness;
import com.fanwe.lib.dialog.impl.SDDialogBase;

/**
 * Created by Administrator on 2016/12/1.
 */

public class AuctionBaseDialog extends SDDialogBase
{
    protected AuctionBusiness auctionBusiness;


    public AuctionBaseDialog(Activity activity, AuctionBusiness auctionBusiness)
    {
        super(activity);
        setAuctionBusiness(auctionBusiness);
    }

    public void setAuctionBusiness(AuctionBusiness auctionBusiness)
    {
        this.auctionBusiness = auctionBusiness;
    }

    public AuctionBusiness getAuctionBusiness()
    {
        return auctionBusiness;
    }
}
