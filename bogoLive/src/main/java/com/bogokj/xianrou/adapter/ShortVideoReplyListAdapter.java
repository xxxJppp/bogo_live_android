package com.bogokj.xianrou.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bogokj.live.activity.LiveUserHomeActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.xianrou.model.XRUserDynamicCommentModel;
import com.bogokj.xianrou.util.StringUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by weipeng on 2017/10/18.
 */

public class ShortVideoReplyListAdapter extends BaseQuickAdapter<XRUserDynamicCommentModel, BaseViewHolder> {

    private Context mContext;

    public ShortVideoReplyListAdapter(Context context, List<XRUserDynamicCommentModel> data) {
        super(R.layout.item_short_video_reply, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final XRUserDynamicCommentModel item) {

//        if(StringUtils.toInt(item.getReply_comment_count()) > 0){
//            helper.setVisible(R.id.item_short_video_reply_comment,true);
//            helper.setText(R.id.item_short_video_reply_comment, String.format(Locale.CHINA,mContext.getString(R.string.See_all_replies),item.getReply_comment_count()));
//        }else{

//            helper.setVisible(R.id.item_short_video_reply_comment,false);
//        }

        //查看所有评论
//        helper.setOnClickListener(R.id.item_short_video_reply_comment, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                UIHelper.showShortVideoReplyCommentListActivity(mContext,item.getId());
//
//            }
//        });

        if (!TextUtils.isEmpty(item.getTo_nick_name())) {
            helper.setText(R.id.item_short_video_reply_body, "回复" + item.getTo_nick_name() + ":" + item.getContent());
        } else {
            helper.setText(R.id.item_short_video_reply_body, item.getContent());
        }
        helper.setText(R.id.item_short_video_reply_name, item.getNick_name());
        helper.setText(R.id.item_short_video_reply_time, item.getLeft_time());
        GlideUtil.loadHeadImage(item.getHead_image()).into((ImageView) helper.getView(R.id.item_short_video_avatar));

        helper.getView(R.id.item_short_video_avatar).setOnClickListener(v -> {
            toHomePage(item);
        });
    }

    private void toHomePage(XRUserDynamicCommentModel item) {
        Intent intent = new Intent(mContext, LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, item.getUser_id());
        mContext.startActivity(intent);
    }
}
