package com.bogokj.live.appview.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.live.model.LiveFinishUserActModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author kn
 * @description: 观众直播结束界面
 * @time kn 2019/12/23
 */
public class RoomViewerFinishView extends RoomView {
    private String roomId;

    public RoomViewerFinishView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomViewerFinishView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomViewerFinishView(Context context, int roomId) {
        super(context);
        this.roomId = roomId + "";
        Log.e("RoomViewerFinishView", roomId + "");

        initData();

    }

    private ImageView iv_bg;
    private TextView tv_viewer_number, tv_viewer_pay;
    private TextView tv_follow;

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_viewer_finish;
    }

    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        iv_bg = find(R.id.iv_bg);
        tv_viewer_number = find(R.id.tv_viewer_number);
        tv_viewer_pay = find(R.id.tv_viewer_pay);
        tv_follow = find(R.id.tv_follow);
        TextView tv_back_home = find(R.id.tv_back_home);
        TextView tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        CircleImageView civ_head_img = (CircleImageView) findViewById(R.id.civ_head_img);

        if (getLiveActivity().getRoomInfo().getPodcast() != null) {
            UserModel user = getLiveActivity().getRoomInfo().getPodcast().getUser();
            SDViewBinder.setTextView(tv_nick_name, user.getNick_name());
            GlideUtil.load(user.getHead_image()).into(civ_head_img);
            Glide.with(this)
                    .load(user.getHead_image())
                    .apply(bitmapTransform(new BlurTransformation(20)).placeholder(R.drawable.bg_image_loading).error(R.drawable.bg_image_loading).dontAnimate())
                    .into(iv_bg);
        }


//        GlideUtil.load(user.getHead_image()).bitmapTransform(new BlurTransformation(getActivity(), 20)).into(iv_bg);

        tv_follow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickFollow();
            }
        });
        tv_back_home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickBackHome();
            }
        });

    }

    private void initData() {
        CommonInterface.liveFinishUserData(roomId, new AppRequestCallback<LiveFinishUserActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    tv_viewer_pay.setText(actModel.getTotal_diamonds() + "");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    public void setViewerNumber(int number) {
        if (number < 0) {
            number = 0;
        }
        tv_viewer_number.setText(String.valueOf(number));
    }

    public void setHasFollow(int hasFollow) {
        String strFollow = null;
        if (hasFollow == 1) {
            strFollow = "已关注";
            //tv_follow.setImageResource(R.drawable.ic_end_live_followed);
        } else {
            strFollow = "关注";
            //tv_follow.setImageResource(R.drawable.ic_end_live_follow);
        }
//        SDViewBinder.setTextView(tv_follow, strFollow);
    }

    protected void clickFollow() {
        CommonInterface.requestFollow(getLiveActivity().getCreaterId(), 0, new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    setHasFollow(actModel.getHas_focus());
                }
            }
        });
    }

    protected void clickBackHome() {
        getActivity().finish();
    }


    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}
