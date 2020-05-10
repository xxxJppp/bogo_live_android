package com.bogokj.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;
import com.bogokj.live.R;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_del_videoActModel;
import com.bogokj.live.model.App_end_videoActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author kn
 * @description: 主播直播结束页面
 * @time kn 2019/12/23
 */
public class RoomCreaterFinishView extends RoomView {

    public RoomCreaterFinishView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomCreaterFinishView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomCreaterFinishView(Context context) {
        super(context);
    }

    private ImageView iv_bg;
    private TextView tv_viewer_number;
    private TextView tv_ticket;
    private TextView tv_back_home;
    private TextView tv_share;
    private ImageView tv_delete_video;

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        setContentView(R.layout.view_room_creater_finish);

        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        tv_viewer_number = (TextView) findViewById(R.id.tv_viewer_number);
        tv_ticket = (TextView) findViewById(R.id.tv_ticket);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_back_home = (TextView) findViewById(R.id.tv_back_home);
        tv_delete_video = (ImageView) findViewById(R.id.tv_delete_video);
        TextView tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        CircleImageView civ_head_img = (CircleImageView) findViewById(R.id.civ_head_img);

        UserModel user = getLiveActivity().getRoomInfo().getPodcast().getUser();
        SDViewBinder.setTextView(tv_nick_name, user.getNick_name());
        GlideUtil.load(user.getHead_image()).into(civ_head_img);
        Glide.with(this)
                .load(user.getHead_image())
                .apply(bitmapTransform(new BlurTransformation(20)).placeholder(R.drawable.bg_image_loading).error(R.drawable.bg_image_loading).dontAnimate())
                .into(iv_bg);

        tv_back_home.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_delete_video.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == tv_back_home) {
            clickBackHome();
        } else if (v == tv_share) {
            clickShare();
        } else if (v == tv_delete_video) {
            clickDeleteVideo();
        }
    }

    private void clickBackHome() {
        getActivity().finish();
    }

    private void clickShare() {
        if (clickListener != null) {
            clickListener.onClickShare();
        }
    }

    private void clickDeleteVideo() {
        CommonInterface.requestDeleteVideo(getLiveActivity().getRoomId(), new AppRequestCallback<App_del_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    getActivity().finish();
                }
            }
        });
    }

    /**
     * 关闭直播间数据
     *
     * @param actModel
     */
    public void onLiveCreaterRequestEndVideoSuccess(App_end_videoActModel actModel) {
        SDViewBinder.setTextView(tv_viewer_number, String.valueOf(actModel.getWatch_number()));
        SDViewBinder.setTextView(tv_ticket, String.valueOf(actModel.getVote_number()));
        if (actModel.getHas_delvideo() == 1) {
            SDViewUtil.setVisible(tv_delete_video);
        } else {
            SDViewUtil.setInvisible(tv_delete_video);
        }
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }

    public interface ClickListener {
        void onClickShare();
    }

}
