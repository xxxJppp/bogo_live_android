package com.bogokj.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.bogokj.hybrid.constant.ApkConstant;
import com.bogokj.hybrid.dao.InitActModelDao;
import com.bogokj.hybrid.model.InitActModel;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDCollectionUtil;
import com.bogokj.library.view.SDRecyclerView;
import com.bogokj.live.IMHelper;
import com.bogokj.live.LiveConstant;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveMsgRecyclerAdapter;
import com.bogokj.live.event.ERequestFollowSuccess;
import com.bogokj.live.model.custommsg.CustomMsg;
import com.bogokj.live.model.custommsg.CustomMsgLight;
import com.bogokj.live.model.custommsg.CustomMsgLiveMsg;
import com.bogokj.live.model.custommsg.CustomMsgText;
import com.bogokj.live.model.custommsg.MsgModel;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.openqq.protocol.imsdk.msg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 聊天消息
 */
public class RoomMsgView extends RoomLooperMainView<MsgModel> {
    public RoomMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomMsgView(Context context) {
        super(context);
        init();
    }

    /**
     * 列表显示的最大消息数量
     */
    private static final int MAX_MSG_NUMBER = 200;
    /**
     * 刷新每一页的最大消息数量
     */
    private static final int PAGE_MAX_SIZE = 20;
    /**
     * 刷新每一页消息的间隔
     */
    private static final int PAGE_LOOP_TIME = 500;

    private SDRecyclerView lv_content;
    private LiveMsgRecyclerAdapter adapter;
    private List<MsgModel> listPage = new ArrayList<>();


    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_msg;
    }

    protected void init() {
        lv_content = find(R.id.lv_content);

        lv_content.setItemAnimator(null);
        adapter = new LiveMsgRecyclerAdapter(getActivity());
        lv_content.setAdapter(adapter);


        initLiveMsg();
    }

    protected void initLiveMsg() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            List<CustomMsgLiveMsg> listMsg = model.getListmsg();
            if (!SDCollectionUtil.isEmpty(listMsg)) {
                for (CustomMsgLiveMsg msg : listMsg) {
                    MsgModel msgModel = msg.parseToMsgModel();
                    if (msgModel != null) {
                        adapter.getData().add(msgModel);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onEventMainThread(ERequestFollowSuccess event) {
        if (event.userId.equals(getLiveActivity().getCreaterId())) {
            if (event.actModel.getHas_focus() == 1) {
                String followMsg = event.actModel.getFollow_msg();
                if (!TextUtils.isEmpty(followMsg)) {
                    final String groupId = getLiveActivity().getGroupId();
                    final CustomMsgLiveMsg msg = new CustomMsgLiveMsg();
                    msg.setDesc(followMsg);
                    IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            IMHelper.postMsgLocal(msg, groupId);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected long getLooperPeriod() {
        return PAGE_LOOP_TIME;
    }

    @Override
    protected void onLooperWork(LinkedList<MsgModel> queue) {
        int size = queue.size();
        if (ApkConstant.DEBUG) {
            LogUtil.i("queue size:" + size);
        }
        if (size > 0) {
            int loopCount = size;
            if (size > PAGE_MAX_SIZE) {
                loopCount = PAGE_MAX_SIZE;
            }

            listPage.clear();
            for (int i = 0; i < loopCount; i++) {
                listPage.add(queue.poll());
            }
            if (ApkConstant.DEBUG) {
                LogUtil.i("-------------take:" + listPage.size());
            }
            adapter.appendData(listPage);
            removeBeyondModel();
            dealScrollToBottom();
        }
    }

    @Override
    protected void onAfterLooperWork() {

    }


    public void addModel(MsgModel model) {
//        CustomMsgText customMsg = (CustomMsgText) model.getCustomMsg();
        Log.e("addModel", adapter.getData().size() + "==" + adapter);


        offerModel(model);
        adapter.notifyDataSetChanged();
    }

    private void dealScrollToBottom() {
        if (lv_content.isIdle()) {
            lv_content.scrollToEnd();
        }
    }

    private void removeBeyondModel() {
        int size = adapter.getDataCount();
        int overSize = size - MAX_MSG_NUMBER;
        if (overSize > 0) {
            for (int i = 0; i < overSize; i++) {
                adapter.removeData(0);
            }
        }
    }
}
