package com.bogokj.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bogokj.hybrid.dialog.SDProgressDialog;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.BaseActModel;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.activity.BogoAddWishListActivity;
import com.bogokj.live.activity.BogoWishListActivity;
import com.bogokj.live.adapter.BogoNobleListAdapter;
import com.bogokj.live.adapter.BogoWishListAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.event.EventSelectWishOk;
import com.bogokj.live.model.BogoDelWishApiModel;
import com.bogokj.live.model.BogoNobleListModel;
import com.bogokj.live.model.BogoWishListApiModel;
import com.bogokj.live.model.BogoWishListModel;
import com.bogokj.live.model.BogoWishListPushApiModel;
import com.bogokj.live.model.NobleListModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 贵族弹窗
 * @time kn 2019/12/23
 */
public class BogoNobleListDialog extends SDDialogBase {

    RecyclerView nobleRv;
    private int roomId;

    private List<BogoNobleListModel> list = new ArrayList<>();
    private BogoNobleListAdapter bogoNobleListAdapter;

    public BogoNobleListDialog(Activity activity, int roomId) {
        super(activity);
        this.roomId = roomId;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_boble_list);
        paddings(0);
        setGrativity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        nobleRv = findViewById(R.id.rv_noble_list);
        nobleRv.setLayoutManager(new LinearLayoutManager(getContext()));
        bogoNobleListAdapter = new BogoNobleListAdapter(list);
        nobleRv.setAdapter(bogoNobleListAdapter);

        requestGetWishList();
    }

    private void requestGetWishList() {
        Log.e("roomId", roomId + "");
        CommonInterface.getNobleList(roomId + "", new AppRequestCallback<NobleListModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                Log.e("getNobleList", sdResponse.getDecryptedResult());
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }


}
