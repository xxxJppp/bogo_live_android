package com.bogokj.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.App_ContModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
import com.bogokj.library.adapter.SDSimpleAdapter;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-28 下午3:23:32 类说明
 */
public class LiveContAdapter extends SDSimpleAdapter<App_ContModel> {
    private String rankingType;
    private String ticketName;
    private OnRankingClickListener onRankingClickListener;

    public LiveContAdapter(List<App_ContModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    public void setRankingType(String rankingType) {
        this.rankingType = rankingType;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_ranking_list;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, App_ContModel model) {
        bindOtherData(position + 3, convertView, parent, model);
        binddefaultData(position, convertView, parent, model);
    }

    private void bindOtherData(int position, View convertView, ViewGroup parent, App_ContModel model) {
        TextView tv_position_other = get(R.id.tv_position_other, convertView);
        int number = position + 1;
        SDViewBinder.setTextView(tv_position_other, number + "");
    }


    private void binddefaultData(final int position, final View convertView, final ViewGroup parent, final App_ContModel model) {
        TextView tv_ranking_type = get(R.id.tv_ranking_type, convertView);
        TextView tv_is_follow = get(R.id.tv_is_follow, convertView);
        LinearLayout ll_item_content = get(R.id.ll_item_content, convertView);
        LinearLayout ll_is_follow = get(R.id.ll_is_follow, convertView);
        ImageView civ_head_other = get(R.id.civ_head_other, convertView);
        ImageView civ_level_other = get(R.id.civ_level_other, convertView);
        TextView tv_nickname_other = get(R.id.tv_nickname_other, convertView);
        ImageView iv_global_male_other = get(R.id.iv_global_male_other, convertView);
        ImageView iv_rank_other = get(R.id.iv_rank_other, convertView);
        TextView tv_num_other = get(R.id.tv_num_other, convertView);
        TextView tv_ticket_name = get(R.id.tv_ticket_name, convertView);

        SDViewBinder.setTextView(tv_ranking_type, rankingType, "");

        GlideUtil.loadHeadImage(model.getHead_image()).into(civ_head_other);
//
//        if (2 == model.getIs_authentication()) {
//            SDViewUtil.setVisible(civ_level_other);
//            GlideUtil.load(model.getV_icon()).into(civ_level_other);
//        } else {
//            SDViewUtil.setGone(civ_level_other);
//        }

        SDViewBinder.setTextView(tv_nickname_other, model.getNick_name(), "你未设置昵称");

        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            if ((model.getUser_id() + "").equals(userModel.getUser_id())) {
                SDViewUtil.setInvisible(ll_is_follow);
            } else {
                SDViewUtil.setVisible(ll_is_follow);
            }
        }

//        SDViewUtil.setInvisible(ll_is_follow);

        switch (model.getIs_focus()) {
            case 1:
                SDViewBinder.setTextView(tv_is_follow, "已关注", "你未设置昵称");
                tv_is_follow.setTextColor(getActivity().getResources().getColor(R.color.res_text_gray_m));
                //tv_is_follow.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_rank_followed));
                break;
            default:
                SDViewBinder.setTextView(tv_is_follow, "+关注", "关注");
                tv_is_follow.setTextColor(getActivity().getResources().getColor(R.color.white));
                //tv_is_follow.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_rank_follow));
                break;
        }

        ll_is_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRankingClickListener != null) {
                    onRankingClickListener.clickFollow(view, position, model);
                }
            }
        });

        ll_item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRankingClickListener != null) {
                    onRankingClickListener.clickItem(view, position, model);
                }
            }
        });


        if (model.getSex() == 1) {
            iv_global_male_other.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2) {
            iv_global_male_other.setImageResource(R.drawable.ic_global_female);
        } else {
            SDViewUtil.setGone(iv_global_male_other);
        }
        iv_rank_other.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));

        SDViewBinder.setTextView(tv_num_other, model.getUse_ticket() + "");

        if (!TextUtils.isEmpty(ticketName)) {
            SDViewBinder.setTextView(tv_ticket_name, ticketName);
        }
    }

    public void setOnRankingClickListener(OnRankingClickListener onRankingClickListener) {
        this.onRankingClickListener = onRankingClickListener;
    }

    public interface OnRankingClickListener {
        public void clickFollow(View view, int position, App_ContModel model);

        public void clickItem(View view, int position, App_ContModel model);
    }
}
