package com.bogokj.auction.appview.room;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.auction.activity.AuctionGoodsDetailActivity;
import com.bogokj.auction.common.AuctionCommonInterface;
import com.bogokj.auction.event.EDoPaiSuccess;
import com.bogokj.auction.event.EGinsengShootMarginSuccess;
import com.bogokj.auction.model.App_pai_user_dopaiActModel;
import com.bogokj.auction.model.App_pai_user_get_videoActModel;
import com.bogokj.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.bogokj.auction.model.custommsg.CustomMsgAuctionOffer;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.R;
import com.bogokj.live.appview.room.RoomView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.model.custommsg.MsgModel;
import com.sunday.eventbus.SDEventManager;

/**
 * Created by Administrator on 2016/8/19.
 */
public class RoomAuctionBtnView extends RoomView
{
    public RoomAuctionBtnView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomAuctionBtnView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomAuctionBtnView(Context context)
    {
        super(context);
    }

    private RoomOfferChangeView ll_rechange_view;
    private LinearLayout ll_parent_last_pai_diamonds;
    private TextView tv_last_pai_diamonds;

    private App_pai_user_get_videoActModel actModel;

    private int mLast_pai_diamonds;//最高价

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_room_auction_btn_view;
    }

    @Override
    protected void onBaseInit()
    {
        super.onBaseInit();
        register();
    }

    private void register()
    {
        ll_parent_last_pai_diamonds = (LinearLayout) findViewById(R.id.ll_parent_last_pai_diamonds);
        ll_rechange_view = (RoomOfferChangeView) findViewById(R.id.ll_rechange_view);
        ll_parent_last_pai_diamonds = (LinearLayout) findViewById(R.id.ll_parent_last_pai_diamonds);
        ll_parent_last_pai_diamonds.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ll_rechange_view.isCanClick())
                {
                    if (verifyRequestParams())
                    {
                        ll_rechange_view.offerClick();
                    }
                }
            }
        });
        tv_last_pai_diamonds = (TextView) findViewById(R.id.tv_last_pai_diamonds);
    }

    public void bindData(App_pai_user_get_videoActModel app_pai_user_goods_detailActModel)
    {
        this.actModel = app_pai_user_goods_detailActModel;
        PaiUserGoodsDetailDataInfoModel model = app_pai_user_goods_detailActModel.getDataInfo();
        if (model != null)
        {
            this.mLast_pai_diamonds = model.getLast_pai_diamonds();
            SDViewBinder.setTextView(tv_last_pai_diamonds, model.getJj_diamonds());
            if (model.getStatus() == 0)
            {
                SDViewUtil.setVisible(ll_parent_last_pai_diamonds);
            } else
            {
                SDViewUtil.setGone(ll_parent_last_pai_diamonds);
            }
        }
    }

    private boolean verifyRequestParams()
    {
        if (actModel == null)
        {
            return false;
        }

        PaiUserGoodsDetailDataInfoModel model = actModel.getDataInfo();
        if (model == null)
        {
            return false;
        }

        int has_join = actModel.getData().getHas_join();
        if (has_join == 0)
        {
            startGinsengShootMarginActivity();
            return false;
        }
        requestPaiUserDopai(model.getId());
        return true;
    }

    private void requestPaiUserDopai(final int id)
    {
        AuctionCommonInterface.requestPaiUserDopai(id, mLast_pai_diamonds, new AppRequestCallback<App_pai_user_dopaiActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {

                } else if (actModel.getStatus() == 10052)
                {
                    startGinsengShootMarginActivity();
                } else if (actModel.getStatus() == 10053)
                {
                    mLast_pai_diamonds = actModel.getPai_diamonds();
                    ;
                    EDoPaiSuccess eDoPaiSuccess = new EDoPaiSuccess();
                    eDoPaiSuccess.last_pai_diamonds = actModel.getPai_diamonds();
                    SDEventManager.post(eDoPaiSuccess);
                }
            }
        });
    }

    //未交保证金
    private void startGinsengShootMarginActivity()
    {
        if (actModel != null && actModel.getDataInfo() != null)
        {
            PaiUserGoodsDetailDataInfoModel model = actModel.getDataInfo();

            Intent intent = new Intent(getActivity(), AuctionGoodsDetailActivity.class);
            intent.putExtra(AuctionGoodsDetailActivity.EXTRA_IS_ANCHOR, getLiveActivity().isCreater());
            intent.putExtra(AuctionGoodsDetailActivity.EXTRA_ID, String.valueOf(model.getId()));
            intent.putExtra(AuctionGoodsDetailActivity.EXTRA_IS_SMALL_SCREEN, 1);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onMsgAuction(MsgModel msg)
    {
        super.onMsgAuction(msg);

        int type = msg.getCustomMsgType();
        if (LiveConstant.CustomMsgType.MSG_AUCTION_OFFER == type)
        {
            CustomMsgAuctionOffer customMsgAuctionOffer = msg.getCustomMsgAuctionOffer();
            this.mLast_pai_diamonds = customMsgAuctionOffer.getPai_diamonds();
        } else if (LiveConstant.CustomMsgType.MSG_AUCTION_NOTIFY_PAY == type)
        {
            SDViewUtil.setGone(ll_parent_last_pai_diamonds);
        } else if (LiveConstant.CustomMsgType.MSG_AUCTION_PAY_SUCCESS == type)
        {
            SDViewUtil.setGone(ll_parent_last_pai_diamonds);
        } else if (LiveConstant.CustomMsgType.MSG_AUCTION_FAIL == type)
        {
            SDViewUtil.setGone(ll_parent_last_pai_diamonds);
        } else if (LiveConstant.CustomMsgType.MSG_AUCTION_SUCCESS == type)
        {
            SDViewUtil.setGone(ll_parent_last_pai_diamonds);
        }
    }

    //保证金缴纳成功
    public void onEventMainThread(EGinsengShootMarginSuccess event)
    {
        if (actModel != null && actModel.getData() != null)
        {
            actModel.getData().setHas_join(1);
        }
    }

}
