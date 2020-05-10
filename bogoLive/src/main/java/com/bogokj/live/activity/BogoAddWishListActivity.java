package com.bogokj.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.activity.BaseTitleActivity;
import com.bogokj.library.title.SDTitleItem;
import com.bogokj.library.title.SDTitleSimple;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.event.EventSelectWishOk;
import com.bogokj.live.model.BogoWishListModel;
import com.bogokj.live.model.LiveGiftModel;
import com.bogokj.xianrou.util.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import static com.bogokj.live.activity.BogoWishSelectGiftActivity.SELECT_GIFT_MODEL;

/**
 * @dw
 */
public class BogoAddWishListActivity extends BaseTitleActivity {
    public static final int SELECT_GIFT_REQUEST_CODE = 10;

    @ViewInject(R.id.tv_gift_name)
    TextView tv_gift_name;

    @ViewInject(R.id.et_count)
    EditText et_count;

    @ViewInject(R.id.et_repay)
    EditText et_repay;

    private LiveGiftModel liveGiftModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_wish_list);
        init();
    }


    private void init() {
        initTitle();
        findViewById(R.id.rl_select_gift).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_gift:
                clickSelectGift();
                break;
            case R.id.title_item_tv_bot:
                clickOk();
                break;
        }
    }

    private void clickOk() {
        String count = et_count.getText().toString();
        String repay = et_repay.getText().toString();

        if (liveGiftModel == null) {
            SDToast.showToast("请选择礼物");
            return;
        }
        if (StringUtils.toInt(count) == 0) {
            SDToast.showToast("请输入礼物数量");
            return;
        }

        BogoWishListModel bogoWishListModel = new BogoWishListModel();
        bogoWishListModel.setTxt(repay);
        bogoWishListModel.setG_id(String.valueOf(liveGiftModel.getId()));
        bogoWishListModel.setG_num(count);
        bogoWishListModel.setG_icon(liveGiftModel.getIcon());
        bogoWishListModel.setG_name(liveGiftModel.getName());

        EventSelectWishOk eventSelectWishOk = new EventSelectWishOk();
        eventSelectWishOk.setBogoWishListModel(bogoWishListModel);
        EventBus.getDefault().post(eventSelectWishOk);
        finish();

    }

    private void clickSelectGift() {
        Intent intent = new Intent(this, BogoWishSelectGiftActivity.class);
        startActivityForResult(intent, SELECT_GIFT_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_GIFT_REQUEST_CODE) {
            if (resultCode == BogoWishSelectGiftActivity.SELECT_GIFT_RESULT_CODE) {
                liveGiftModel = data.getParcelableExtra(SELECT_GIFT_MODEL);
                tv_gift_name.setText(liveGiftModel.getName());
            }
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("添加礼物和数量");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
        SDTitleItem right = mTitle.addItemRight();
        right.setTextBot("完成");
        right.mTvBot.setTextColor(getResources().getColor(R.color.global));
        right.mTvBot.setOnClickListener(this);
    }

}
