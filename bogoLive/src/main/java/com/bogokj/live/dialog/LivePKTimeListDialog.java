package com.bogokj.live.dialog;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LivePkTimeListAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_pk_getTime_Model;
import com.bogokj.live.model.TimeModel;
import com.fanwe.library.adapter.http.model.SDResponse;

import java.util.ArrayList;

//pk时间选择
public class LivePKTimeListDialog extends LiveBaseDialog implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRvContentList;
    private ArrayList<TimeModel> list = new ArrayList<>();
    private LivePkTimeListAdapter livePKTimeListAdapter;
    private Activity activity;

    private TimeSelectListener  timeSelectListener;


    public   interface TimeSelectListener {
       void  ontimeselect(TimeModel timeModel);
    }

    public LivePKTimeListDialog(Activity activity,TimeSelectListener timeSelectListener)
    {
        super(activity);
        this.activity = activity;
        this.timeSelectListener = timeSelectListener;
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_live_pk_time_list);
        paddings(0);
        setCanceledOnTouchOutside(true);

        mRvContentList = (RecyclerView) findViewById(R.id.rv_content);
        mRvContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        livePKTimeListAdapter = new LivePkTimeListAdapter(list);
        mRvContentList.setAdapter(livePKTimeListAdapter);
        livePKTimeListAdapter.setOnItemClickListener(this);
        requestList();
    }
    //获取时间列表
    private void requestList() {
        CommonInterface.requestGetPKTimeList(new AppRequestCallback<App_pk_getTime_Model>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(actModel.isOk()){
                    list.clear();
                    list.addAll(actModel.getList());
                    livePKTimeListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        timeSelectListener.ontimeselect(list.get(position));
        dismiss();
    }


}
