package com.bogokj.live.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.App_ContModel;
import com.bogokj.live.model.App_followActModel;
import com.bogokj.live.model.RankUserItemModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDViewBinder;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.library.view.CircleImageView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜头部View
 * Created by LianCP on 2017/8/22.
 */
public class RankingListBaseHeaderView extends BaseAppView {
    public RankingListBaseHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RankingListBaseHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RankingListBaseHeaderView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.ll_root)
    private LinearLayout ll_root;

    @ViewInject(R.id.tv_ranking_num)
    private TextView tv_ranking_num;

    @ViewInject(R.id.ll_item_first)
    private LinearLayout ll_item_first;
    @ViewInject(R.id.civ_head_first)
    private CircleImageView civ_head_first;
    @ViewInject(R.id.tv_nickname_first)
    private TextView tv_nickname_first;
    @ViewInject(R.id.iv_global_male_first)
    private ImageView iv_global_male_first;
    @ViewInject(R.id.iv_rank_first)
    private ImageView iv_rank_first;
    @ViewInject(R.id.tv_ranking_type_first)
    private TextView tv_ranking_type_first;
    @ViewInject(R.id.tv_num_other_first)
    private TextView tv_num_other_first;
    @ViewInject(R.id.tv_ticket_name_first)
    private TextView tv_ticket_name_first;

    @ViewInject(R.id.ll_item_second)
    private LinearLayout ll_item_second;
    @ViewInject(R.id.civ_head_second)
    private CircleImageView civ_head_second;
    @ViewInject(R.id.tv_nickname_second)
    private TextView tv_nickname_second;
    @ViewInject(R.id.iv_global_male_second)
    private ImageView iv_global_male_second;
    @ViewInject(R.id.iv_rank_second)
    private ImageView iv_rank_second;
    @ViewInject(R.id.tv_ranking_type_second)
    private TextView tv_ranking_type_second;
    @ViewInject(R.id.tv_num_other_second)
    private TextView tv_num_other_second;
    @ViewInject(R.id.tv_ticket_name_second)
    private TextView tv_ticket_name_second;

    @ViewInject(R.id.ll_item_third)
    private LinearLayout ll_item_third;
    @ViewInject(R.id.civ_head_third)
    private CircleImageView civ_head_third;
    @ViewInject(R.id.tv_nickname_third)
    private TextView tv_nickname_third;
    @ViewInject(R.id.iv_global_male_third)
    private ImageView iv_global_male_third;
    @ViewInject(R.id.iv_rank_third)
    private ImageView iv_rank_third;
    @ViewInject(R.id.tv_ranking_type_third)
    private TextView tv_ranking_type_third;
    @ViewInject(R.id.tv_num_other_third)
    private TextView tv_num_other_third;
    @ViewInject(R.id.tv_ticket_name_third)
    private TextView tv_ticket_name_third;

    @ViewInject(R.id.ll_num_first)
    private LinearLayout ll_num_first;

    @ViewInject(R.id.ll_num_second)
    private LinearLayout ll_num_second;

    @ViewInject(R.id.ll_num_third)
    private LinearLayout ll_num_third;

    @ViewInject(R.id.im_follow_first)
    private TextView im_follow_first;

    @ViewInject(R.id.im_follow_second)
    private TextView im_follow_second;

    @ViewInject(R.id.im_follow_third)
    private TextView im_follow_third;

    private List<RankUserItemModel> listModel = new ArrayList<RankUserItemModel>();
    private List<App_ContModel> listContModel = new ArrayList<App_ContModel>();

    private void init() {
        setContentView(R.layout.view_live_ranking_list_header);

        setOnClick();
    }

    private void setOnClick() {
        ll_item_first.setOnClickListener(this);
        ll_item_second.setOnClickListener(this);
        ll_item_third.setOnClickListener(this);
        im_follow_first.setOnClickListener(this);
        im_follow_second.setOnClickListener(this);
        im_follow_third.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        RankUserItemModel model = null;
        App_ContModel contModel = null;
        switch (v.getId()) {
            case R.id.ll_item_first:
                if (listModel.size() > 0) {
                    model = listModel.get(0);
                }
                if (listContModel.size() > 0) {
                    contModel = listContModel.get(0);
                }
                break;
            case R.id.ll_item_second:
                if (listModel.size() > 1) {
                    model = listModel.get(1);
                }
                if (listContModel.size() > 1) {
                    contModel = listContModel.get(1);
                }
                break;
            case R.id.ll_item_third:
                if (listModel.size() > 2) {
                    model = listModel.get(2);
                }
                if (listContModel.size() > 2) {
                    contModel = listContModel.get(2);
                }
                break;
            default:
                break;
        }
        if (null != mHeaderOnClick) {
            mHeaderOnClick.onItemClick(v, model, contModel);
        }
    }

    public void setRankingType(String rankingType) {
        SDViewBinder.setTextView(tv_ranking_type_first, rankingType, "");
        SDViewBinder.setTextView(tv_ranking_type_second, rankingType, "");
        SDViewBinder.setTextView(tv_ranking_type_third, rankingType, "");
    }

    public void setTickName(String tickName) {
        if (!TextUtils.isEmpty(tickName)) {
            SDViewBinder.setTextView(tv_ticket_name_first, tickName);
            SDViewBinder.setTextView(tv_ticket_name_second, tickName);
            SDViewBinder.setTextView(tv_ticket_name_third, tickName);
        }

    }

    public void setRankingNum(int rankingNum) {
//        SDViewUtil.setHeight(ll_root, SDViewUtil.dp2px(208));
        tv_ranking_num.setVisibility(GONE);
        tv_ranking_num.setText(rankingNum + AppRuntimeWorker.getTicketName());
    }

    public void setContDatas(List<App_ContModel> listContModel) {
        this.listContModel.clear();
        this.listContModel.addAll(listContModel);
        if (null != listContModel) {
            if (listContModel.size() < 1) {//如果没数据的时候，隐藏头部视图
                ll_root.setVisibility(GONE);
            } else {
                ll_root.setVisibility(VISIBLE);
            }

            if (listContModel.size() > 0) {//有一条数据的时候
                listContModel.get(0).setTicket(listContModel.get(0).getUse_ticket());
                initViewsDatas(listContModel.get(0), civ_head_first, tv_nickname_first, iv_global_male_first,
                        iv_rank_first, tv_num_other_first, im_follow_first);
                ll_num_first.setVisibility(VISIBLE);
                im_follow_first.setVisibility(VISIBLE);
                setFollowImage(im_follow_first, listContModel.get(0).getIs_focus());
            }

            if (listContModel.size() > 1) {//有两条数据的时候
                listContModel.get(1).setTicket(listContModel.get(1).getUse_ticket());
                initViewsDatas(listContModel.get(1), civ_head_second, tv_nickname_second, iv_global_male_second,
                        iv_rank_second, tv_num_other_second, im_follow_second);
                ll_num_second.setVisibility(VISIBLE);
                im_follow_second.setVisibility(VISIBLE);
                setFollowImage(im_follow_second, listContModel.get(1).getIs_focus());
            }

            if (listContModel.size() > 2) {//有三条数据的时候
                listContModel.get(2).setTicket(listContModel.get(2).getUse_ticket());
                initViewsDatas(listContModel.get(2), civ_head_third, tv_nickname_third, iv_global_male_third,
                        iv_rank_third, tv_num_other_third, im_follow_third);
                ll_num_third.setVisibility(VISIBLE);
                im_follow_third.setVisibility(VISIBLE);
                setFollowImage(im_follow_third, listContModel.get(2).getIs_focus());
            }
        }
    }

    private void setFollowImage(TextView im_follow, int isFollow) {
        if (isFollow == 1) {
            im_follow.setText("已关注");
            im_follow.setBackgroundResource(R.drawable.self_side_purple_fifteen_cornor_bac);

        } else {
            im_follow.setText("+关注");
            im_follow.setBackgroundResource(R.drawable.self_side_purple_fifteen_cornor_bac);
        }
    }

    public void setDatas(List<RankUserItemModel> listModel) {
        this.listModel.clear();
        this.listModel.addAll(listModel);
        if (null != listModel) {
            if (listModel.size() < 1) {//如果没数据的时候，隐藏头部视图
                ll_root.setVisibility(GONE);
            } else {
                ll_root.setVisibility(VISIBLE);
            }

            if (listModel.size() > 0) {//有一条数据的时候
                initViewsDatas(listModel.get(0), civ_head_first, tv_nickname_first, iv_global_male_first,
                        iv_rank_first, tv_num_other_first, im_follow_first);
                ll_num_first.setVisibility(VISIBLE);
                im_follow_first.setVisibility(VISIBLE);
                setFollowImage(im_follow_first, listModel.get(0).getIs_focus());
            }

            if (listModel.size() > 1) {//有两条数据的时候
                initViewsDatas(listModel.get(1), civ_head_second, tv_nickname_second, iv_global_male_second,
                        iv_rank_second, tv_num_other_second, im_follow_second);
                ll_num_second.setVisibility(VISIBLE);
                im_follow_second.setVisibility(VISIBLE);
                setFollowImage(im_follow_second, listModel.get(1).getIs_focus());
            }

            if (listModel.size() > 2) {//有三条数据的时候
                initViewsDatas(listModel.get(2), civ_head_third, tv_nickname_third, iv_global_male_third,
                        iv_rank_third, tv_num_other_third, im_follow_third);
                ll_num_third.setVisibility(VISIBLE);
                im_follow_third.setVisibility(VISIBLE);
                setFollowImage(im_follow_third, listModel.get(2).getIs_focus());
            }
        }
    }

    private void initViewsDatas(final RankUserItemModel model, CircleImageView headImageView, TextView tvNickName,
                                ImageView ivGlobalMale, ImageView ivRank, TextView tvTicketNum, final TextView im_follow) {
        GlideUtil.loadHeadImage(model.getHead_image()).into(headImageView);
        SDViewBinder.setTextView(tvNickName, model.getNick_name(), "你未设置昵称");
        if (model.getSex() == 1) {
            SDViewUtil.setVisible(ivGlobalMale);
            ivGlobalMale.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2) {
            SDViewUtil.setVisible(ivGlobalMale);
            ivGlobalMale.setImageResource(R.drawable.ic_global_female);
        } else {
            SDViewUtil.setGone(ivGlobalMale);
        }

        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            if ((model.getUser_id() + "").equals(userModel.getUser_id())) {
                SDViewUtil.setInvisible(im_follow);
            } else {
                SDViewUtil.setVisible(im_follow);
            }
        }

        setFollowImage(im_follow, model.getIs_focus());

        im_follow.setOnClickListener(view -> requestFollow(im_follow, model));


        SDViewUtil.setVisible(ivRank);
        ivRank.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));
        SDViewBinder.setTextView(tvTicketNum, model.getTicket() + "");
    }

    private RankingHeaderOnClick mHeaderOnClick;

    public void setRankingHeaderOnClick(RankingHeaderOnClick mHeaderOnClick) {
        this.mHeaderOnClick = mHeaderOnClick;
    }

    public interface RankingHeaderOnClick {
        public void onItemClick(View view, RankUserItemModel model, App_ContModel contModel);
    }

    private void requestFollow(final TextView im_follow, final RankUserItemModel model) {
        showProgressDialog("");
        CommonInterface.requestFollow(model.getUser_id() + "", 0, new AppRequestCallback<App_followActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getHas_focus() == 1) {
                        model.setIs_focus(1);
                        setFollowImage(im_follow, 1);
                    } else {
                        model.setIs_focus(0);
                        setFollowImage(im_follow, 0);
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }
}
