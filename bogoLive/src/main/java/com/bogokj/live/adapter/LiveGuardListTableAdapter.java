package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.GuardMemberBean;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.live.utils.LiveUtils;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;

import java.util.List;

import static android.view.View.GONE;

/**
* 守护人员列表适配器
* */
public class LiveGuardListTableAdapter extends SDSimpleRecyclerAdapter<GuardMemberBean> {

//    private LiveCategoryClickCallback<LiveGuardianRulesModel> mItemClickCallback;

    public LiveGuardListTableAdapter(List<GuardMemberBean> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<GuardMemberBean> holder, final int position, final GuardMemberBean model) {

       ImageView ivSex;
       ImageView ivRank;
//        TextView name;
        ivSex = holder.get(R.id.iv_user_sex);
//        name = holder.get(R.id.category_name);
        ivRank = holder.get(R.id.iv_user_rank);
        //头像
        GlideUtil.loadHeadImage(model.getHead_image()).into((ImageView) holder.get(R.id.user_head));
        //昵称
         ((TextView)holder.get(R.id.user_nick_name)).setText(model.getNick_name());
        //性别
        if(model.getSex().equals("0")){
            ivSex.setVisibility(GONE);
        }else if(model.getSex().equals("1")){
            ivSex.setImageResource(R.drawable.ic_global_male);
        }else if(model.getSex().equals("2")){
            ivSex.setImageResource(R.drawable.ic_global_female);
        }
        //等级
        ivRank.setImageResource(LiveUtils.getLevelImageResId(Integer.parseInt(model.getUser_level().toString()) ));
        //钻石
        if(null!=model.getTotal_diamonds()&&!model.getTotal_diamonds().equals("")&& !model.getTotal_diamonds().equals("0")){
            ((TextView)holder.get(R.id.ticket_num)).setText("消费"+model.getTotal_diamonds() +"钻石");
        }else {
            ((TextView)holder.get(R.id.ticket_num)).setText("消费0钻石");
        }


        ((TextView)holder.findViewById(R.id.tv_end_time)).setText("剩余"+model.getEndtime());


    }

    @Override
    public int getLayoutId(ViewGroup viewGroup, int i) {
        return R.layout.item_guard_list_table;
    }

    public void updateData(List<GuardMemberBean> listModel) {
        this.setData(listModel);
        this.notifyDataSetChanged();
    }
}
