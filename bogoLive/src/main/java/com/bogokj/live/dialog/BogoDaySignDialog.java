package com.bogokj.live.dialog;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.adapter.BogoDaySignListAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_request_full_invite_codeModel;
import com.bogokj.live.model.BogoDayIsSignApiModel;
import com.bogokj.live.model.BogoDaySignApiModel;
import com.bogokj.live.model.BogoDaySignListApiModel;
import com.bogokj.live.model.BogoDaySignModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.umeng.socialize.view.BaseDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 签到弹窗
 * @time kn 2019/12/20
 */
public class BogoDaySignDialog extends SDDialogBase {

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private BogoDaySignListAdapter bogoDaySignListAdapter;
    private List<BogoDaySignModel> list = new ArrayList<>();

    public BogoDaySignDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_day_sign);
        paddings(0);
        x.view().inject(this, getContentView());

        findViewById(R.id.tv_sign).setOnClickListener(this);

        rv_content_list.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv_content_list.setAdapter(bogoDaySignListAdapter = new BogoDaySignListAdapter(list));
        requestGetDayList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign:
                clickSign();
                break;
        }
    }

    //点击签到
    private void clickSign() {

        CommonInterface.requestDaySign(new AppRequestCallback<BogoDaySignApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    dismiss();
                    new BogoSignDaySuccessDialog(getOwnerActivity()).showCenter();
                } else {
                    Toast.makeText(getContext(), actModel.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void requestGetDayList() {
        CommonInterface.requestGetDayList(new AppRequestCallback<BogoDaySignListApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    list.clear();
                    list.addAll(actModel.getList());
                    bogoDaySignListAdapter.notifyDataSetChanged();
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

    public static void check(final Activity activity) {
        if (activity == null) {
            return;
        }
        CommonInterface.requestCheckDayIsSign(new AppRequestCallback<BogoDayIsSignApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    if (actModel.getToday_signin() != 1) {
                        new BogoDaySignDialog(activity).showCenter();
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }
}
