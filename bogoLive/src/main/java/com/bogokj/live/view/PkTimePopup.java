package com.bogokj.live.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bogokj.live.R;
import com.bogokj.live.adapter.LivePkTimeListAdapter;
import com.bogokj.live.model.TimeModel;

import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

public class PkTimePopup extends BasePopupWindow implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRvContentList;
    private ArrayList<TimeModel> list = new ArrayList<>();
    private LivePkTimeListAdapter livePKTimeListAdapter;

    private OnPkTimePopupClickListener onPkTimePopupClickListener;

    public PkTimePopup(Context context, ArrayList<TimeModel> timelist) {
        super(context);

        setAllowDismissWhenTouchOutside(true);
        setPopupGravity(Gravity.TOP | Gravity.CENTER_VERTICAL);

        this.list = timelist;
        mRvContentList = (RecyclerView) findViewById(R.id.rv_content);
        mRvContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        livePKTimeListAdapter = new LivePkTimeListAdapter(timelist);
        mRvContentList.setAdapter(livePKTimeListAdapter);
        livePKTimeListAdapter.setOnItemClickListener(this);

    }

    @Override
    protected Animation onCreateShowAnimation() {
        Animation showAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        showAnima.setInterpolator(new DecelerateInterpolator());
        showAnima.setDuration(350);
        return showAnima;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation exitAnima = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0,
                Animation.RELATIVE_TO_PARENT,
                0);
        exitAnima.setInterpolator(new DecelerateInterpolator());
        exitAnima.setDuration(350);
        return exitAnima;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_pk_time);
    }
    //=============================================================Getter/Setter

    public OnPkTimePopupClickListener getOnPkTimePopupClickListener() {
        return onPkTimePopupClickListener;
    }

    public void setOnPkTimePopupClickListener(OnPkTimePopupClickListener onPkTimePopupClickListener) {
        this.onPkTimePopupClickListener = onPkTimePopupClickListener;
    }


    //=============================================================InterFace
    public interface OnPkTimePopupClickListener {

        void onTimeSelect(TimeModel timeModel);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        onPkTimePopupClickListener.onTimeSelect(list.get(position));
        dismiss();
    }
}