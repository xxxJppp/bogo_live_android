package com.bogokj.o2o.jshandler;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.dialog.SDProgressDialog;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.common.SDActivityManager;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.webview.CustomWebView;
import com.bogokj.live.activity.LiveCreateRoomActivity;
import com.bogokj.live.common.AppDiskKey;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.library.view.AppDialogProgress;
import com.bogokj.live.model.JoinLiveData;
import com.bogokj.live.model.UserModel;
import com.bogokj.live.room.activity.LiveAnchorActivity;
import com.bogokj.live.room.activity.LiveAudienceActivity;
import com.bogokj.o2o.activity.O2OShoppingLiveMainActivity;
import com.bogokj.o2o.common.O2OShoppingCommonInterface;
import com.bogokj.o2o.constant.Constant;
import com.bogokj.o2o.model.App_shop_getappActModel;
import com.bogokj.o2o.model.App_shop_getvideoActModel;
import com.bogokj.o2o.model.O2OShoppingJsonLiveJsModel;
import com.bogokj.o2o.model.O2OShoppingStartLiveAppJsModel;
import com.bogokj.shop.jshandler.ShopJsHandler;
import com.fanwe.lib.blocker.SDDurationBlocker;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.library.adapter.http.model.SDResponse;


/**
 * Created by Administrator on 2016/9/24.
 */

public class O2OShoppingLiveJsHander extends ShopJsHandler {
    private SDDurationBlocker blocker = new SDDurationBlocker();
    private CustomWebView customWebView;

    public O2OShoppingLiveJsHander(Activity activity) {
        super(activity);
    }

    public O2OShoppingLiveJsHander(Activity activity, CustomWebView customWebView) {
        super(activity);
        this.customWebView = customWebView;
    }

    @JavascriptInterface
    @Override
    public void logout() {
        super.logout();
        App.getApplication().logout(true, false, false);
    }

    @JavascriptInterface
    public void shopping_join_live(final String json) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                main_shopping_join_live(json);
            }
        });
    }

    private void main_shopping_join_live(String json) {
        try {
            final O2OShoppingJsonLiveJsModel model = JSON.parseObject(json, O2OShoppingJsonLiveJsModel.class);

            SDDisk.open().putString(AppDiskKey.SHOPPING_SESSION_ID, model.getSession_id());

            O2OShoppingCommonInterface.requestGetVideo(model.getPodcast_id(), new AppRequestCallback<App_shop_getvideoActModel>() {
                private SDProgressDialog dialog = new SDProgressDialog(getActivity());

                @Override
                protected void onStart() {
                    super.onStart();
                    dialog.show();
                }

                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.getStatus() == 1) {
                        final JoinLiveData joinLiveData = new JoinLiveData();
                        int is_small_screen = model.getIs_small_screen();
                        String creater_id = actModel.getCreaterId();
                        String group_id = actModel.getGroupId();
                        int room_id = actModel.getRoomId();
                        String loadingVideoImageUrl = actModel.getLoadingVideoImageUrl();

                        if (actModel.getUser() != null && actModel.getUser().getUser_id().equals(creater_id)) {
                            SDToast.showToast("creater_id==user_id");
                        }

                        UserModel.dealLoginSuccess(actModel.getUser(), true);

                        joinLiveData.setIs_small_screen(is_small_screen);
                        joinLiveData.setCreaterId(creater_id);
                        joinLiveData.setGroupId(group_id);
                        joinLiveData.setRoomId(room_id);
                        joinLiveData.setLoadingVideoImageUrl(loadingVideoImageUrl);

                        AppRuntimeWorker.startContext();
                        AppRuntimeWorker.joinLive(joinLiveData, getActivity());
                    }
                    dialog.dismiss();
                }

                @Override
                protected void onError(SDResponse resp) {
                    super.onError(resp);
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    @JavascriptInterface
    public void shopping_start_live_app(final String json) {
        if (blocker.block()) {
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                main_shopping_start_live_app(json);
            }
        });
    }

    private void main_shopping_start_live_app(String json) {
        try {
            final O2OShoppingStartLiveAppJsModel model = JSON.parseObject(json, O2OShoppingStartLiveAppJsModel.class);

            SDDisk.open().putString(AppDiskKey.SHOPPING_SESSION_ID, model.getSession_id());

            O2OShoppingCommonInterface.requestGetApp(new AppRequestCallback<App_shop_getappActModel>() {

                @Override
                protected void onStart() {
                    super.onStart();
                    AppDialogProgress.showWaitTextDialog(getActivity(),"正在启动直播应用");
                }

                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.getStatus() == 1) {
                        if (UserModel.dealLoginSuccess(actModel.getUser(), true)) {
                            Intent intent = new Intent(getActivity(), O2OShoppingLiveMainActivity.class);
                            startActivity(intent);
                        } else {
                            SDToast.showToast("user保存失败");
                        }
                    }
                }

                @Override
                protected void onFinish(SDResponse resp) {
                    super.onFinish(resp);
                    AppDialogProgress.hideWaitDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    @JavascriptInterface
    public void shopping_create_live(final String json) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                main_shopping_create_live(json);
            }
        });
    }

    private void main_shopping_create_live(String json) {
        try {
            final O2OShoppingStartLiveAppJsModel model = JSON.parseObject(json, O2OShoppingStartLiveAppJsModel.class);

            SDDisk.open().putString(AppDiskKey.SHOPPING_SESSION_ID, model.getSession_id());

            O2OShoppingCommonInterface.requestOpenVideo(new AppRequestCallback<App_shop_getappActModel>() {
                private SDProgressDialog dialog = new SDProgressDialog(getActivity());

                @Override
                protected void onStart() {
                    super.onStart();
                    dialog.show();
                }

                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.getStatus() == 1) {
                        UserModel.dealLoginSuccess(actModel.getUser(), true);

                        AppRuntimeWorker.startContext();
                        Intent intent = new Intent(getActivity(), LiveCreateRoomActivity.class);
                        startActivity(intent);
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    private int state = 0;

    @JavascriptInterface
    public void js_get_live_type() {
        if (SDActivityManager.getInstance().containActivity(LiveAnchorActivity.class)) {
            state = 1;
        } else if (SDActivityManager.getInstance().containActivity(LiveAudienceActivity.class)) {
            state = 1;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                customWebView.loadJsFunction(Constant.JsFunctionName.JS_LIVE_STATE, state);
            }
        }, 1000);
    }

    @JavascriptInterface
    public void js_shopping_comeback_live_app() {
        getActivity().finish();
    }

 /*  js_shopping_join_live(json)
    {
        "status":"0" 失败 1成功
        "data":""
    }
    js_shopping_create_live_app
    {
        "status":"0" 失败 1成功
        "data":""
    }

    js_shopping_create_live
    {
        "status":"0" 失败 1成功
        "data":""
    }*/
}
