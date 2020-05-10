package com.bogokj.live.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.GuardMemberBean;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
import com.bogokj.library.view.CircleImageView;

/**
 * 直播守护列表HeaderView
 */
public class LiveGuardTableHeaderView extends BaseAppView
{

    private GuardMemberBean guardMemberBean;
    //头像
    private CircleImageView userhead;
    //昵称
    private TextView  tvName;
    //砖石
    private  TextView tvDiamond;
    //等级
    private ImageView iv_rank;
    //性别
    private  ImageView iv_sex;
    //守护之星空布局
    private RelativeLayout rlGuardEmpty;
    //守护之星布局
    private RelativeLayout rlGuardView;
    //成为第一个开通守护的人
    private TextView  tvFirstGuard;
    //主播id
    private String creatid;

    private Context context;

    public LiveGuardTableHeaderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveGuardTableHeaderView(Context context, GuardMemberBean memberBean, String creatid)
    {
        super(context);
        this.context = context;
        this.guardMemberBean = memberBean;
        this.creatid = creatid;
        init();
    }

    private void init()
    {
        setContentView(R.layout.header_guard_table);
        userhead = findViewById(R.id.user_head);
        tvName = findViewById(R.id.tv_name);

        //性别
        iv_sex = findViewById(R.id.iv_sex);
        //等级
        iv_rank = findViewById(R.id.iv_rank);
        //钻石
        tvDiamond = findViewById(R.id.tv_diamonds);
        //
        rlGuardEmpty = findViewById(R.id.rl_empty_view);
        rlGuardView = findViewById(R.id.rl_guard_star_view);

        tvFirstGuard = findViewById(R.id.tv_first_guard);

        if(null==guardMemberBean){
            rlGuardView.setVisibility(GONE);
            rlGuardEmpty.setVisibility(View.VISIBLE);
            tvFirstGuard.setVisibility(View.VISIBLE);
            //判断是不是主播
            if(creatid.equals(UserModelDao.getUserId())){
                tvFirstGuard.setText("我的守护正在星月兼程的赶来");

            }else {
                tvFirstGuard.setText("主播还没有人守护哦，赶快守护TA吧");
            }

            return;
        }


        if( null!=guardMemberBean.getTotal_diamonds()&&!guardMemberBean.getTotal_diamonds().equals("")&&!guardMemberBean.getTotal_diamonds().equals("0")){
            rlGuardEmpty.setVisibility(View.GONE);
            rlGuardView.setVisibility(View.VISIBLE);

            //头像
            GlideUtil.loadHeadImage(guardMemberBean.getHead_image()).into(userhead);
            //昵称
            tvName.setText(guardMemberBean.getNick_name());
            //性别
            if(guardMemberBean.getSex().equals("0")){
                iv_sex.setVisibility(GONE);
            }else if(guardMemberBean.getSex().equals("1")){
                iv_sex.setImageResource(R.drawable.ic_global_male);
            }else if(guardMemberBean.getSex().equals("2")){
                iv_sex.setImageResource(R.drawable.ic_global_female);
            }
            //等级
            iv_rank.setImageResource(LiveUtils.getLevelImageResId(Integer.parseInt(guardMemberBean.getUser_level()) ));
            //钻石
            tvDiamond.setText("消费"+guardMemberBean.getTotal_diamonds() +"钻石");

            rlGuardView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到个人主页
                    Intent intent = new Intent(context, LiveUserHomeActivity.class);
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, guardMemberBean.getUid());
                    context.startActivity(intent);
                }
            });

        }else {
            rlGuardEmpty.setVisibility(View.VISIBLE);
            rlGuardView.setVisibility(View.GONE);

        }



    }









}
