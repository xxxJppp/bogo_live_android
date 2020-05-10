package com.bogokj.xianrou.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.bogokj.live.R;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.xianrou.model.XRUserDynamicCommentModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author kn create
 * @description: 动态评论弹窗
 * @date : 2020/1/3
 */
public class BogoCommentDialog extends SDDialogBase {
    @ViewInject(R.id.click_other_menu_ll)
    protected LinearLayout otherMenuLl;

    @ViewInject(R.id.click_self_menu_ll)
    protected LinearLayout selfMenuLl;

    private XRUserDynamicCommentModel data;

    public BogoCommentDialog(Activity activity) {
        super(activity);
        init();
    }

    public BogoCommentDialog(Activity activity, XRUserDynamicCommentModel data) {
        super(activity);
        this.data = data;
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_comment_menu_layout);
        paddingLeft(50);
        paddingRight(50);
        x.view().inject(this, getContentView());

        otherMenuLl.setVisibility(AppRuntimeWorker.getLoginUserID().equals(data.getUser_id()) ? View.GONE : View.VISIBLE);
        selfMenuLl.setVisibility(AppRuntimeWorker.getLoginUserID().equals(data.getUser_id()) ? View.VISIBLE : View.GONE);

        setOnClick();

    }

    private void setOnClick() {
        findViewById(R.id.other_menu_chat_tv).setOnClickListener(this);
        findViewById(R.id.other_menu_copy_tv).setOnClickListener(this);
        findViewById(R.id.other_menu_report_tv).setOnClickListener(this);
        findViewById(R.id.self_menu_copy_tv).setOnClickListener(this);
        findViewById(R.id.self_menu_delete_tv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.other_menu_chat_tv:
                if (menuCliclListener != null)
                    menuCliclListener.onMenuChatClickListener(data.getUser_id());
                dismiss();
                break;

            case R.id.other_menu_report_tv:
                if (menuCliclListener != null) menuCliclListener.onMenuReportClickListener();
                dismiss();
                break;

            case R.id.other_menu_copy_tv:
            case R.id.self_menu_copy_tv:
                if (menuCliclListener != null)
                    menuCliclListener.onMenuCopylickListener(data.getContent());
                dismiss();
                break;

            case R.id.self_menu_delete_tv:
                if (menuCliclListener != null) menuCliclListener.onMenuDeleteClickListener(data.getComment_id());
                dismiss();
                break;


        }
    }

    private MenuCliclListener menuCliclListener;

    public void setMenuCliclListener(MenuCliclListener menuCliclListener) {
        this.menuCliclListener = menuCliclListener;
    }

    public interface MenuCliclListener {
        void onMenuChatClickListener(String user_id);

        void onMenuReportClickListener();

        void onMenuCopylickListener(String content);

        void onMenuDeleteClickListener(String comment_id);
    }
}
