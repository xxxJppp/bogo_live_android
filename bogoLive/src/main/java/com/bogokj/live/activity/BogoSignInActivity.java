package com.bogokj.live.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bogokj.hybrid.activity.BaseActivity;
import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.R;
import com.bogokj.live.adapter.BogoDaySignListAdapter;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.dialog.BogoDaySignDialog;
import com.bogokj.live.dialog.BogoSignDaySuccessDialog;
import com.bogokj.live.model.BogoDayIsSignApiModel;
import com.bogokj.live.model.BogoDaySignApiModel;
import com.bogokj.live.model.BogoDaySignListApiModel;
import com.bogokj.live.model.BogoDaySignModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kn
 * @description: 签到页面
 * @time kn 2019/12/20
 */
public class BogoSignInActivity extends BaseActivity implements BaseActivity.TitleButtonClickListener {

    @ViewInject(R.id.sign_in_gift_rv)
    RecyclerView giftRv;

    @ViewInject(R.id.sign_in_time_first_tv)
    TextView timeFistTv;

    @ViewInject(R.id.sign_in_time_second_tv)
    TextView timeSecondTv;

    @ViewInject(R.id.sign_in_time_third_tv)
    TextView timeThirdTv;

    @ViewInject(R.id.sign_in_finish_tv)
    private TextView signInFinish;

    private BogoDaySignListAdapter bogoDaySignListAdapter;
    private List<BogoDaySignModel> list = new ArrayList<>();

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bogo_sign_in);
        initView();
    }

    private void initView() {
        mTitle.setMiddleTextTop("签到");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
        setTitleButtonClickListener(this);

        giftRv.setLayoutManager(new GridLayoutManager(this, 4));
        giftRv.setAdapter(bogoDaySignListAdapter = new BogoDaySignListAdapter(list));
        check();

        //点击事件
        signInFinish.setOnClickListener(this);

    }


    private void requestGetDayList() {
        CommonInterface.requestGetDayList(new AppRequestCallback<BogoDaySignListApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    String signin_count = actModel.getSignin_count();
                    String signCount = getSignCount(signin_count);

                    list.clear();
                    list.addAll(actModel.getList());
                    bogoDaySignListAdapter.notifyDataSetChanged();

                    try {
                        setTimeData(signCount);
                    } catch (Exception e) {
                        String str = getSignCount(list.size() + "");
                        setTimeData(str);
                    }

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

    private void setTimeData(String str) {
        timeFistTv.setText(str.charAt(0) + "");
        timeSecondTv.setText(str.charAt(1) + "");
        timeThirdTv.setText(str.charAt(2) + "");
    }

    public void check() {

        CommonInterface.requestCheckDayIsSign(new AppRequestCallback<BogoDayIsSignApiModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

                if (actModel.isOk()) {
                    if (actModel.getToday_signin() != 1) {
                        canSignIn();
                    } else {
                        cantSignIn();
                    }
                } else {
                    cantSignIn();
                }

                requestGetDayList();
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                cantSignIn();
            }
        });
    }


    public void canSignIn() {
        signInFinish.setEnabled(true);
        signInFinish.setBackgroundResource(R.drawable.bg_global_full_radius_gradual);
    }

    public void cantSignIn() {
        signInFinish.setEnabled(true);
        signInFinish.setBackgroundResource(R.drawable.self_side_gray_czero_twenty_cornor_bac);
    }

    @Override
    public void onLeftTitleButtonClickListener() {
        finish();
    }

    @Override
    public void onRightTitleButtonClickListener() {

    }

    @Override
    public void onMiddleTitleButtonClickListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_finish_tv:
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

                    check();
                } else {
                    ToastUtil.toastLongMessage(actModel.getError());
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }


    public String getSignCount(String count) {
        if (TextUtils.isEmpty(count)) return "000";

        switch (count.length()) {
            case 1:
                return "00" + count;


            case 2:
                return "0" + count;


            case 3:
                return count;


            default:
                return "999";

        }
    }
}
