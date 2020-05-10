package com.bogokj.live.dialog;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LivePKEmceeListAdapter;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_pk_get_emcee_listActModel;
import com.bogokj.live.model.UserModel;
import com.fanwe.library.adapter.http.model.SDResponse;

import java.util.ArrayList;

public class LivePKEmceeListDialog extends LiveBaseDialog implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRvContentList;
    private ArrayList<UserModel> list = new ArrayList<>();
    private LivePKEmceeListAdapter livePKEmceeListAdapter;
    private LiveCreaterBusiness mLiveViewerBusiness;
    private Activity activity;
    public LivePKEmceeListDialog(Activity activity, LiveCreaterBusiness liveViewerBusiness)
    {
        super(activity);
        this.activity = activity;
        this.mLiveViewerBusiness = liveViewerBusiness;
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_live_pk_emcee_list);
        paddings(0);
        setCanceledOnTouchOutside(true);

        mRvContentList = (RecyclerView) findViewById(R.id.rv_content);
        mRvContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        livePKEmceeListAdapter = new LivePKEmceeListAdapter(list);
        mRvContentList.setAdapter(livePKEmceeListAdapter);
        livePKEmceeListAdapter.setOnItemClickListener(this);
        requestList();
    }

    private void requestList() {
        CommonInterface.requestGetPKEmceeList(new AppRequestCallback<App_pk_get_emcee_listActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(actModel.isOk()){
                    list.clear();
                    list.addAll(actModel.getList());
                    livePKEmceeListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        LivePKConfirmDialog livePKConfirmDialog = new LivePKConfirmDialog(activity, mLiveViewerBusiness,list.get(position).getUser_id());
        livePKConfirmDialog.showBottom();
        dismiss();
    }


}
