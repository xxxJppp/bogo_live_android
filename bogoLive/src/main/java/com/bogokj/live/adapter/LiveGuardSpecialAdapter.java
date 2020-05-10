package com.bogokj.live.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.model.GuardPrivilegeModel;
import com.bogokj.live.utils.GlideUtil;
import com.bogokj.library.adapter.SDSimpleRecyclerAdapter;
import com.bogokj.library.adapter.viewholder.SDRecyclerViewHolder;
import com.bogokj.library.utils.SDViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
* 守护特权适配器
* */
public class LiveGuardSpecialAdapter extends SDSimpleRecyclerAdapter<GuardPrivilegeModel>
{
    private  int wid;
    //拥有的特权id集合
    private List<String>  specic = new ArrayList<>();

    public List<String> getSpecic() {
        return specic;
    }

    public void setSpecic(List<String> specic) {
        this.specic = specic;
    }

    public LiveGuardSpecialAdapter(List<GuardPrivilegeModel> listModel, List<String> sepecial, Activity activity)
    {
        super(listModel, activity);
        this.specic = sepecial;
        wid =(activity.getWindowManager().getDefaultDisplay().getWidth()- SDViewUtil.dp2px(40))/4;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<GuardPrivilegeModel> holder, final int position, final GuardPrivilegeModel model)
    {

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(wid, ViewGroup.LayoutParams.MATCH_PARENT);
        holder.findViewById(R.id.item_layout).setLayoutParams(params);
         //特权图片
        if(specic.contains(model.getId())){
            GlideUtil.load(model.getSelect_icon()).into((ImageView) holder.findViewById(R.id.iv_special));
        }else {
            GlideUtil.load(model.getDefault_icon()).into((ImageView) holder.findViewById(R.id.iv_special));
        }
        //特权名称
       ( (TextView) holder.findViewById(R.id.guard_special_name)).setText(model.getName());
//        holder.findViewById(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LiveGuardSpecialDetailDialog liveGuardSpecialDetailDialog = new LiveGuardSpecialDetailDialog(getActivity(),model.getCentent());
//                liveGuardSpecialDetailDialog.showCenter();
//                liveGuardSpecialDetailDialog.setCancelable(true);
//                liveGuardSpecialDetailDialog.setCanceledOnTouchOutside(true);
//            }
//        });

    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType)
    {
        return  R.layout.item_live_guard_special;

    }


}
