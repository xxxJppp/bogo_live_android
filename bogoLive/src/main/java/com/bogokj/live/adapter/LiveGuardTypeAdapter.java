package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.GuardTypeModel;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDViewUtil;

import java.util.List;

/**
* 守护类型适配器
* */
public class LiveGuardTypeAdapter extends SDSimpleRecyclerAdapter<GuardTypeModel>
{
    private  int wid;

    private  int  selectpos;
    private  ItemClick itemClick;

    public ItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public int getSelectpos() {
        return selectpos;
    }

    public interface ItemClick{
        void onClick(GuardTypeModel model);
}

    public void setSelectpos(int selectpos) {
        this.selectpos = selectpos;
    }

    public LiveGuardTypeAdapter(List<GuardTypeModel> listModel, Activity activity)
    {
        super(listModel, activity);
        selectpos = 0;
        wid =(activity.getWindowManager().getDefaultDisplay().getWidth() - SDViewUtil.dp2px(24)) /3;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<GuardTypeModel> holder, final int position, final GuardTypeModel model)
    {

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(wid, ViewGroup.LayoutParams.MATCH_PARENT);
        holder.findViewById(R.id.item_layout).setLayoutParams(params);
        //
        ((TextView)holder.findViewById(R.id.guard_name)).setText(model.getName());
        ((TextView)holder.findViewById(R.id.tv_diamonds)).setText(model.getCoin()+"钻石");

         //类型  "type": "类型：1推荐2尊贵 0没有",
        if(model.getType().equals("0")){
            ((ImageView)holder.findViewById(R.id.iv_command)).setVisibility(View.GONE);
            ((ImageView)holder.findViewById(R.id.iv_respected)).setVisibility(View.GONE);
        }else if(model.getType().equals("1")){
            ((ImageView)holder.findViewById(R.id.iv_command)).setVisibility(View.VISIBLE);
            ((ImageView)holder.findViewById(R.id.iv_respected)).setVisibility(View.GONE);
        }else if(model.getType().equals("2")){
            ((ImageView)holder.findViewById(R.id.iv_command)).setVisibility(View.GONE);
            ((ImageView)holder.findViewById(R.id.iv_respected)).setVisibility(View.VISIBLE);
        }
         //位置是选中的位置
        if (selectpos == position){
            ((TextView)holder.findViewById(R.id.guard_name)).setTextColor(getActivity().getResources().getColor(R.color.orange));
            ((TextView)holder.findViewById(R.id.tv_diamonds)).setTextColor(getActivity().getResources().getColor(R.color.orange));
            ((ImageView)holder.findViewById(R.id.iv_command)).setImageResource(R.drawable.recommand_icon);
            ((ImageView)holder.findViewById(R.id.iv_respected)).setImageResource(R.drawable.respected_icon);
            ((ImageView)holder.findViewById(R.id.iv_up_arrow)).setVisibility(View.GONE);
            holder.findViewById(R.id.ll_item).setBackground(getActivity().getResources().getDrawable(R.drawable.res_layer_transparent_stroke_m_orange_corner));
        }else {
            ((TextView)holder.findViewById(R.id.guard_name)).setTextColor(getActivity().getResources().getColor(R.color.text_color_AAAAAA));
            ((ImageView)holder.findViewById(R.id.iv_up_arrow)).setVisibility(View.GONE);
            ((TextView)holder.findViewById(R.id.tv_diamonds)).setTextColor(   getActivity().getResources().getColor(R.color.text_color_AAAAAA));
            ((ImageView)holder.findViewById(R.id.iv_command)).setImageResource(R.drawable.recommand_grey_icon);
            ((ImageView)holder.findViewById(R.id.iv_respected)).setImageResource(R.drawable.respected_grey_icon);
            holder.findViewById(R.id.ll_item).setBackground(getActivity().getResources().getDrawable(R.drawable.res_layer_transparent_stroke_m_gray_corner));
        }

        //布局点击
        holder.findViewById(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(model);
                setSelectpos(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType)
    {
        return  R.layout.item_live_guard_type;

    }


}
