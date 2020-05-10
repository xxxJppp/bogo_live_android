package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.listener.SDItemLongClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveBlacklistAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.common.AppDialogMenu;
import com.bogokj.live.model.Settings_black_listActModel;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.model.User_set_blackActModel;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单列表
 */
public class LiveBlackListActivity extends BaseTitleActivity {
    private ListView lv_content;

    private LiveBlacklistAdapter adapter;
    private int page;
    private int has_next;
    private List<UserModel> listModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_black_list);
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        lv_content = (ListView) findViewById(R.id.lv_content);
        setStateLayout((SDStateLayout) findViewById(R.id.view_state_layout));

        register();
    }

    private void register() {
        initTitle();

        adapter = new LiveBlacklistAdapter(listModel, this);
        adapter.setItemClickCallback(new SDItemClickCallback<UserModel>() {
            @Override
            public void onItemClick(int position, UserModel item, View view) {
                Intent intent = new Intent(LiveBlackListActivity.this, LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, item.getUser_id());
                startActivity(intent);
            }
        });
        adapter.setItemLongClickCallback(new SDItemLongClickCallback<UserModel>() {
            @Override
            public void onItemLongClick(int position, UserModel item, View view) {
                showOperateMenu(item);
            }
        });

        lv_content.setAdapter(adapter);
        getStateLayout().setAdapter(adapter);

        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter() {
                requestData(true);
            }
        });
    }

    private void showOperateMenu(final UserModel user) {
        AppDialogMenu dialog = new AppDialogMenu(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setItems("解除拉黑");
        dialog.setCallback(new ISDDialogMenu.Callback() {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog) {

            }

            @Override
            public void onClickItem(View v, int index, SDDialogBase dialog) {
                switch (index) {
                    case 0:
                        clickCancelBlackUser(user);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.showBottom();
    }

    private void clickCancelBlackUser(UserModel user) {
        if (user != null) {
            CommonInterface.requestSet_black(user.getUser_id(), new AppRequestCallback<User_set_blackActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (actModel.isOk()) {
                        requestData(false);
                    }
                }
            });
        }
    }

    private void requestData(final boolean isLoadMore) {
        if (isLoadMore) {
            if (has_next == 1) {
                page++;
            } else {
                getPullToRefreshViewWrapper().stopRefreshing();
                SDToast.showToast("没有更多数据了");
                return;
            }
        } else {
            page = 1;
        }

        CommonInterface.requestBlackList(page, new AppRequestCallback<Settings_black_listActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    has_next = actModel.getHas_next();

                    SDViewUtil.updateAdapterByList(listModel, actModel.getUser(), adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }


    private void initTitle() {
        mTitle.setMiddleTextTop("黑名单");
    }

    @Override
    protected void onResume() {
        requestData(false);
        super.onResume();
    }
}
