package com.bogokj.live;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.constant.ApkConstant;
import com.bogokj.library.common.SDHandlerManager;
import com.fanwe.lib.player.SDMediaPlayer;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDCollectionUtil;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.event.EImOnForceOffline;
import com.bogokj.live.event.EImOnNewMessages;
import com.bogokj.live.event.EImOnRefresh;
import com.bogokj.live.event.ESDMediaPlayerStateChanged;
import com.bogokj.live.model.custommsg.MsgModel;
import com.bogokj.live.model.custommsg.TIMMsgModel;
import com.sunday.eventbus.SDEventManager;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;

import java.util.List;

public class LiveIniter {

    public void init(Application app) {
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {

            private void postNewMessage(MsgModel msgModel) {
                EImOnNewMessages event = new EImOnNewMessages();
                event.msg = msgModel;
                SDEventManager.post(event);

                if (msgModel.isPrivateMsg()) {
                    IMHelper.postERefreshMsgUnReaded();
                }
            }

            @Override
            public boolean onNewMessages(final List<TIMMessage> listMessage) {
                if (!SDCollectionUtil.isEmpty(listMessage)) {
                    SDHandlerManager.getBackgroundHandler().post(() -> {
                        for (TIMMessage msg : listMessage) {
                            if (ApkConstant.DEBUG) {
                                TIMConversation conversation = msg.getConversation();
                                LogUtil.i("--------receive msg:" + conversation.getType() + " " + conversation.getPeer() + "---" + msg.getSender());
                            }

                            boolean post = true;
                            final TIMMsgModel msgModel = new TIMMsgModel(msg, true);

                            if (msgModel.getConversationPeer().equals(LiveInformation.getInstance().getCurrentChatPeer())) {
                                IMHelper.setSingleC2CReadMessage(msgModel.getConversationPeer(), false);
                            }

                            boolean needDownloadSound = msgModel.checkSoundFile(new TIMValueCallBack<String>() {
                                @Override
                                public void onError(int i, String s) {
                                }

                                @Override
                                public void onSuccess(String path) {
                                    msgModel.getCustomMsgPrivateVoice().setPath(path);
                                    postNewMessage(msgModel);
                                }
                            });
                            if (needDownloadSound) {
                                post = false;
                            }


                            if (post) {
                                postNewMessage(msgModel);
                            }
                        }
                    });
                }
                return false;
            }
        });

        TIMUserConfig timUserConfig = new TIMUserConfig().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.e("LiveIniter","onForceOffline");
                SDEventManager.post(new EImOnForceOffline());
            }

            @Override
            public void onUserSigExpired() {
                CommonInterface.requestUsersig(null);
            }
        });

        timUserConfig.setRefreshListener(new TIMRefreshListener() {

            @Override
            public void onRefresh() {
                // 默认登陆后会异步获取离线消息以及同步资料数据（如果有开启ImSDK存储，可参见关系链资料章节），同步完成后会通过onRefresh回调通知更新界面，用户得到这个消息时，可以刷新界面，比如会话列表的未读等等：
                SDEventManager.post(new EImOnRefresh());
            }

            @Override
            public void onRefreshConversation(List<TIMConversation> list) {
            }
        });

        //将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(timUserConfig);

        int appId = Integer.parseInt(App.getApplication().getResources().getString(R.string.app_id_tencent_live));
        TIMSdkConfig config = new TIMSdkConfig(appId)
                //.enableCrashReport(false)  //接口已废弃
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");

        TIMManager.getInstance().init(app, config);
        //TIMManager.getInstance().setLogPrintEnable(false);
        LogUtil.i("imsdk version:" + TIMManager.getInstance().getVersion());

        SDMediaPlayer.getInstance().setOnStateChangeCallback(new SDMediaPlayer.OnStateChangeCallback() {
            @Override
            public void onStateChanged(SDMediaPlayer.State oldState, SDMediaPlayer.State newState, SDMediaPlayer player) {
                LogUtil.i("onStateChanged:" + newState);
                SDEventManager.post(new ESDMediaPlayerStateChanged(newState));
            }
        });


        // 配置 Config，请按需配置
//        TUIKitConfigs configs = TUIKit.getConfigs();
//        configs.setSdkConfig(config);
//        configs.setCustomFaceConfig(new CustomFaceConfig());
//        configs.setGeneralConfig(new GeneralConfig());
//        TUIKit.init(app, Integer.parseInt(App.getApplication().getResources().getString(R.string.app_id_tencent_live)), configs);
    }

}
