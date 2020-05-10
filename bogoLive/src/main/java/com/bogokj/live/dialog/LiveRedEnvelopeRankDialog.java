package com.bogokj.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveRedEnvelopeAdapter;
import com.bogokj.live.appview.LiveRedEnvelopeOpenView;
import com.bogokj.live.appview.LiveRedEnvelopeRankView;
import com.bogokj.live.appview.LiveRedEnvelopeView;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_red_envelopeActModel;
import com.bogokj.live.model.App_user_red_envelopeActModel;
import com.bogokj.live.model.App_user_red_envelopeModel;
import com.bogokj.live.model.custommsg.CustomMsgRedEnvelope;
import com.fanwe.library.adapter.http.model.SDResponse;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-27 下午7:27:15 类说明
 */
public class LiveRedEnvelopeRankDialog extends LiveBaseDialog
{
    private LiveRedEnvelopeAdapter adapter;
    private CustomMsgRedEnvelope customMsgRedEnvelope;

    private LiveRedEnvelopeRankView liveRedEnvelopeRankView;

    public LiveRedEnvelopeRankDialog(Activity activity, CustomMsgRedEnvelope msg)
    {
        super(activity);
        this.customMsgRedEnvelope = msg;
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_live_red_envelope_rank);
        setCanceledOnTouchOutside(true);
        paddings(0);
        liveRedEnvelopeRankView = findViewById(R.id.live_red_envelope_rank_view);
        liveRedEnvelopeRankView.setIvEnvelopeOpenHead(customMsgRedEnvelope.getSender().getHead_image());//头像
        liveRedEnvelopeRankView.setTvEnvelopeOpenName(customMsgRedEnvelope.getSender().getNick_name());//昵称
        liveRedEnvelopeRankView.setCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveRedEnvelopeRankDialog.this.dismiss();//关闭
            }
        });
        request_user_red_envelope();//查看手气
    }



    /**
     * 查看手气
     */
    private void request_user_red_envelope()
    {
        CommonInterface.requestUser_red_envelope(customMsgRedEnvelope.getUser_prop_id(), new AppRequestCallback<App_user_red_envelopeActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    List<App_user_red_envelopeModel> list = actModel.getList();
                    if (list != null && list.size() > 0)
                    {
                        bindData(list);
                    }
                }
            }

            private void bindData(List<App_user_red_envelopeModel> list)
            {
                adapter = new LiveRedEnvelopeAdapter(list, getOwnerActivity());
                //获取打开红包View里面的ListView,并设置adapter
                liveRedEnvelopeRankView.getListView().setAdapter(adapter);
            }
        });
    }

    @Override
    public void show()
    {
        super.show();
    }
}
