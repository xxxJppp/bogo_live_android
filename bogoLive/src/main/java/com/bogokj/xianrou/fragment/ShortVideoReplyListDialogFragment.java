package com.bogokj.xianrou.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.activity.LivePrivateChatActivity;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.xianrou.dialog.BogoCommentDialog;
import com.bogokj.xianrou.model.XRCommonActionRequestResponseModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.library.view.AppDialogProgress;
import com.bogokj.xianrou.model.XRDynamicCommentResopnseModel;
import com.bogokj.xianrou.util.InputMethodUtils;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.live.R;
import com.bogokj.xianrou.adapter.ShortVideoReplyListAdapter;
import com.bogokj.xianrou.common.XRCommonInterface;
import com.bogokj.xianrou.fragment.base.BaseListDialogFragment;
import com.bogokj.xianrou.model.XRDynamicCommentListResponseModel;
import com.bogokj.xianrou.model.XRUserDynamicCommentModel;
import com.bogokj.library.utils.LogUtil;
import com.bogokj.library.utils.SDToast;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

/**
 * @author kn update
 * @description: 短视频评论弹窗
 * @time 2020/1/2
 */
public class ShortVideoReplyListDialogFragment extends BaseListDialogFragment<XRUserDynamicCommentModel> implements BogoCommentDialog.MenuCliclListener {

    public static final String VIDEO_ID = "VIDEO_ID";

    private TextView mTvReplyNum;
    private String vid;
    private ImageView mIvClose;
    private LinearLayout mLlInput;
    private EditText mEtSay;
    private XRUserDynamicCommentModel replyBean;
    private boolean isShowInput = false;
    protected AppDialogProgress mDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noCommitTv;

    @Override
    public void initData() {
        super.initData();

        vid = getArguments().getString(VIDEO_ID);
        mSwipeRefreshLayout.setRefreshing(true);

        requestData(false);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_short_video_reply_list;
    }

    @Override
    protected int getDialogHeight() {
        return (int) (SDViewUtil.getScreenHeight() / 2);
    }

    @Override
    public void initView(Dialog view) {
        super.initView(view);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        noCommitTv = view.findViewById(R.id.no_commit_tv);

        mTvReplyNum = (TextView) view.findViewById(R.id.tv_reply_num);
        mIvClose = (ImageView) view.findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mLlInput = (LinearLayout) view.findViewById(R.id.ll_input);

        mEtSay = (EditText) view.findViewById(R.id.et_say);

        mBaseQuickAdapter.setOnItemClickListener((adapter, view1, position) -> {
            replyBean = mListData.get(position);
            if (AppRuntimeWorker.getLoginUserID().equals(replyBean.getUser_id())) {
                toHomePage(replyBean.getUser_id());
            } else {
                changeInputStatus(true);
                mEtSay.setText("");
                mEtSay.setHint("回复" + replyBean.getNick_name() + ":");
            }
        });

        mBaseQuickAdapter.setOnItemLongClickListener((adapter, view12, position) -> {
            XRUserDynamicCommentModel replyBean = mListData.get(position);
            BogoCommentDialog bogoCommentDialog = new BogoCommentDialog(getActivity(), replyBean);
            bogoCommentDialog.show();
            bogoCommentDialog.setMenuCliclListener(this);
            return true;
        });

        mEtSay.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (replyBean != null) {
                    requestReplyComment();
                } else {
                    requestReplyVideo();
                }

            }
            return true;
        });


    }


    private void changeInputStatus(boolean status) {

        if (status) {
            isShowInput = true;
            mEtSay.requestFocus();
            InputMethodUtils.showSoftInput(mActivity);
        } else {
            replyBean = null;
            mEtSay.setHint("说点什么吧...");
            InputMethodUtils.hideSoftInput(mActivity);
        }
    }


    @Override
    public BaseQuickAdapter getAdapter() {
        return new ShortVideoReplyListAdapter(getContext(), mListData);
    }

    @Override
    protected void requestData(boolean refresh) {

        XRCommonInterface.requestCommentList(vid, page, new AppRequestCallback<XRDynamicCommentListResponseModel>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1 && isAdded()) {
                    if (actModel.getComment_list() != null && actModel.getComment_list().size() == 0 && page == 1) {
                        noCommitTv.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    } else {
                        noCommitTv.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        onRefuseOk(actModel.getComment_list());
                    }

                }
            }

        });
    }

    //发表评论回复
    private void requestReplyComment() {

        if (replyBean == null) {
            return;
        }

        String body = mEtSay.getText().toString();
        if (TextUtils.isEmpty(body)) {
            SDToast.showToast("请输入回复内容");
            return;
        }

        if (AppRuntimeWorker.getLoginUserID().equals(replyBean.getUser_id())) {
            SDToast.showToast("不能回复自己。");
            return;
        }

        showProgressDialog("正在发表回复...");
        String content = "回复" + replyBean.getNick_name() + ":" + body;
        XRCommonInterface.requestDynamicComment(vid, content, true, replyBean.getTo_comment_id(), new AppRequestCallback<XRDynamicCommentResopnseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                dismissProgressDialog();

                if (actModel.getStatus() == 1) {

                    mEtSay.setText("");
                    InputMethodUtils.hideSoftInput(mEtSay);
                    mTvReplyNum.setText("评论(" + String.valueOf(actModel.getComment_count()) + ")");

                    changeInputStatus(false);
                    page = 1;
                    mListData.clear();
                    requestData(false);
                    mRecyclerView.scrollToPosition(0);
                    SDToast.showToast("回复成功");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }

    //发表评论
    private void requestReplyVideo() {

        String body = mEtSay.getText().toString();
        if (TextUtils.isEmpty(body)) {
            SDToast.showToast("请输入评论内容。");
            return;
        }

        showProgressDialog("请稍后...");
        XRCommonInterface.requestDynamicComment(vid, body, false, "", new AppRequestCallback<XRDynamicCommentResopnseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                dismissProgressDialog();

                if (actModel.getStatus() == 1) {

                    mTvReplyNum.setText("评论(" + String.valueOf(actModel.getComment_count()) + ")");
                    mEtSay.setText("");
                    InputMethodUtils.hideSoftInput(mEtSay);
                    changeInputStatus(false);

                    page = 1;
                    mListData.clear();
                    requestData(false);
                    mRecyclerView.scrollToPosition(0);
                    SDToast.showToast("评论成功");

                    if (sendCommonListener != null) {
                        sendCommonListener.onsendCommonListener();
                    }

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //正在播放
            //now it's visible to user
        } else {
            // now it's invisible to user
            if (isShowInput) {
                changeInputStatus(false);
            }
            if (mActivity != null && InputMethodUtils.isActive(mActivity)) {
                InputMethodUtils.hideSoftInput(mEtSay);
                LogUtil.d("隐藏键盘");
            }
        }
    }

    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    protected void dimissDialog() {
        AppDialogProgress.hideWaitDialog();
    }

    protected void showProgressDialog(String msg) {
        AppDialogProgress.showWaitTextDialog(getContext(),msg);
    }

    protected void dismissProgressDialog() {
        dimissDialog();
    }


    private SendCommonListener sendCommonListener;

    public void setSendCommonListener(SendCommonListener sendCommonListener) {
        this.sendCommonListener = sendCommonListener;
    }

    public interface SendCommonListener {
        void onsendCommonListener();
    }

    /**
     * 跳转个人主页
     *
     * @param uid
     */
    private void toHomePage(String uid) {
        Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, uid);
        getActivity().startActivity(intent);
    }


    @Override
    public void onMenuChatClickListener(String user_id) {
        Intent intent = new Intent(getActivity(), LivePrivateChatActivity.class);
        intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, user_id);
        startActivity(intent);
    }

    @Override
    public void onMenuReportClickListener() {
        ToastUtil.toastLongMessage("举报");
    }

    @Override
    public void onMenuCopylickListener(String content) {
        ToastUtil.toastLongMessage(copy(content) ? "复制成功" : "复制失败");
    }

    @Override
    public void onMenuDeleteClickListener(String comment_id) {
        XRCommonInterface.requestDeleteDynamicComment(comment_id, new AppRequestCallback<XRCommonActionRequestResponseModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    page = 1;
                    mListData.clear();
                    requestData(false);
                    mRecyclerView.scrollToPosition(0);
                }
            }
        });
    }


    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
