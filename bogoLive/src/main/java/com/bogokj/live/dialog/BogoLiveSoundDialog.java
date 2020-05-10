package com.bogokj.live.dialog;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.adapter.BogoDaySignListAdapter;
import com.bogokj.live.adapter.BogoLiveSoundAdapter;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.BogoDaySignListApiModel;
import com.bogokj.live.model.BogoDaySignModel;
import com.bogokj.live.model.BogoLiveSoundApiModel;
import com.bogokj.live.model.BogoLiveSoundModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.umeng.socialize.view.BaseDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class BogoLiveSoundDialog extends SDDialogBase {

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private BogoLiveSoundAdapter bogoLiveSoundAdapter;

    private List<BogoLiveSoundModel> list = new ArrayList<>();

    private LiveCreaterBusiness liveCreaterBusiness;

    public BogoLiveSoundDialog(Activity activity, LiveCreaterBusiness liveCreaterBusiness) {
        super(activity);
        this.liveCreaterBusiness = liveCreaterBusiness;
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_live_sound);
        paddings(0);
        x.view().inject(this, getContentView());
        setCanceledOnTouchOutside(true);

        rv_content_list.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv_content_list.setAdapter(bogoLiveSoundAdapter = new BogoLiveSoundAdapter(list));
        bogoLiveSoundAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                liveCreaterBusiness.onClickBGSound(list.get(position).getUrl());
            }
        });
        requestGetDayList();
    }

    private void requestGetDayList() {
        CommonInterface.requestLiveSoundList(new AppRequestCallback<BogoLiveSoundApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    list.clear();
                    list.addAll(actModel.getData());
                    bogoLiveSoundAdapter.notifyDataSetChanged();
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

}
