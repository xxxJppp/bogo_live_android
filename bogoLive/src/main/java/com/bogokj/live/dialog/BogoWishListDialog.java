package com.bogokj.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bogokj.hybrid.dialog.SDProgressDialog;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.activity.BogoAddWishListActivity;
import com.bogokj.live.activity.BogoWishListActivity;
import com.bogokj.live.adapter.BogoDaySignListAdapter;
import com.bogokj.live.adapter.BogoWishListAdapter;
import com.bogokj.live.business.LiveCreaterBusiness;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.event.EventSelectWishOk;
import com.bogokj.live.model.BogoDelWishApiModel;
import com.bogokj.live.model.BogoGetWishListGiftListApiModel;
import com.bogokj.live.model.BogoWishListApiModel;
import com.bogokj.live.model.BogoWishListModel;
import com.bogokj.live.model.BogoWishListPushApiModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
 * @description: 心愿单弹窗
 * @time kn 2019/12/25
 */
public class BogoWishListDialog extends SDDialogBase {

    @ViewInject(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private List<BogoWishListModel> list = new ArrayList<>();
    private BogoWishListAdapter bogoWishListAdapter;
    private int totalCount;
    private int completeCount;
    private int delCount;
    private int completeDelCount;

    public BogoWishListDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_wish_list);
        paddings(0);
        x.view().inject(this, getContentView());
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        findViewById(R.id.rl_add_wish).setOnClickListener(this);
        findViewById(R.id.tv_push_wish).setOnClickListener(this);
        findViewById(R.id.today_wish_list_tv).setOnClickListener(v -> getContext().startActivity(new Intent(getContext(), BogoWishListActivity.class)));

        rv_content_list.setLayoutManager(new LinearLayoutManager(getContext()));
        bogoWishListAdapter = new BogoWishListAdapter(list);
        bogoWishListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.item_iv_del:
                    list.get(position).setIs_del(1);
                    bogoWishListAdapter.notifyDataSetChanged();
                    break;
            }
        });
        rv_content_list.setAdapter(bogoWishListAdapter);

        requestGetWishList();
    }

    private void requestGetWishList() {
        CommonInterface.requestGetWishList(new AppRequestCallback<BogoWishListApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    list.clear();
                    list.addAll(actModel.getList());
                    bogoWishListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_wish:
                clickAddWish();
                break;
            case R.id.tv_push_wish:
                clickPushWish();
                break;
            default:
                break;
        }
    }

    private void clickPushWish() {
        final SDProgressDialog dialog = new SDProgressDialog(getOwnerActivity());
        dialog.show();

        totalCount = 0;
        completeCount = 0;
        delCount = 0;
        completeDelCount = 0;
        for (BogoWishListModel item : list) {
            if (item.getId() != null) {
                if (item.getIs_del() == 1) {
                    delCount++;
                }
                continue;
            }
            totalCount++;
        }

        for (BogoWishListModel item : list) {
            if (item.getId() != null) {
                //之前心愿请求删除接口
                if (item.getIs_del() == 1) {
                    CommonInterface.requestDelWish(item.getId(), new AppRequestCallback<BogoDelWishApiModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            completeDelCount++;
                            if (completeDelCount == delCount && completeCount == totalCount) {
                                requestGetWishList();
                                dialog.dismiss();
                            }

                        }

                        @Override
                        protected void onError(SDResponse resp) {
                            super.onError(resp);
                            completeDelCount++;
                            if (completeDelCount == delCount && completeCount == totalCount) {
                                requestGetWishList();
                                dialog.dismiss();
                            }
                        }
                    });
                }
                continue;
            }

            //删除的不用添加
            if (item.getIs_del() == 1) {
                continue;
            }

            CommonInterface.requestPushWishList(item.getG_id(), item.getG_num(), item.getTxt(), new AppRequestCallback<BogoWishListPushApiModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.isOk()) {
                        completeCount++;
                    } else {
                        totalCount--;
                        Toast.makeText(getContext(), actModel.getError(), Toast.LENGTH_LONG).show();
                    }
                    if (completeCount == totalCount && completeDelCount == delCount) {
                        SDToast.showToast("发布完成");
                        requestGetWishList();
                        dialog.dismiss();
                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    super.onError(resp);
                    totalCount--;

                    if (completeCount == totalCount && completeDelCount == delCount) {
                        requestGetWishList();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    //添加心愿
    private void clickAddWish() {
        Intent intent = new Intent(getContext(), BogoAddWishListActivity.class);
        getContext().startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventSelectWishOk event) {
        list.add(event.getBogoWishListModel());
        bogoWishListAdapter.notifyDataSetChanged();
    }
}
