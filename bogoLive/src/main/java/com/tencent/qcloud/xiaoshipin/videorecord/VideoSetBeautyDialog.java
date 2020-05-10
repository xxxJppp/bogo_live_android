package com.tencent.qcloud.xiaoshipin.videorecord;

import android.app.Activity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bogokj.live.R;
import com.bogokj.live.adapter.LiveSetBeautyFilterAdapter;
import com.bogokj.live.control.LiveBeautyFilter;
import com.bogokj.live.control.LiveBeautyType;
import com.bogokj.live.model.LiveBeautyConfig;
import com.bogokj.live.model.LiveBeautyFilterModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.gridlayout.SDGridLayout;
import com.bogokj.library.listener.SDItemClickCallback;
import com.bogokj.library.utils.SDViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置美颜窗口
 */
public class VideoSetBeautyDialog extends SDDialogBase {

    //美颜
    private SeekBar sb_beauty;
    private TextView tv_beauty_percent;

    //美白
    private SeekBar sb_whiten;
    private TextView tv_whiten_percent;

    private SDGridLayout view_filters;
    private LiveSetBeautyFilterAdapter filterAdapter;
    private LiveBeautyConfig mBeautyConfig;
    //    private TXUGCRecord mTXCameraRecord;
    private VideoBeautyUtils videoBeautyUtils;

    public VideoSetBeautyDialog(Activity activity, VideoBeautyUtils videoBeautyUtils) {
        super(activity, R.style.dialogBase);
        init();
        this.videoBeautyUtils = videoBeautyUtils;
    }

    private void init() {
        setContentView(R.layout.dialog_live_set_beauty);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        paddings(0);

        sb_beauty = (SeekBar) getContentView().findViewById(R.id.sb_beauty);
        tv_beauty_percent = (TextView) getContentView().findViewById(R.id.tv_beauty_percent);

        sb_whiten = (SeekBar) getContentView().findViewById(R.id.sb_whiten);
        tv_whiten_percent = (TextView) getContentView().findViewById(R.id.tv_whiten_percent);

        view_filters = (SDGridLayout) findViewById(R.id.view_filters);

        float widthTvPercent = SDViewUtil.measureText(tv_beauty_percent, "100%");
        SDViewUtil.setWidth(tv_beauty_percent, (int) widthTvPercent);
        SDViewUtil.setWidth(tv_whiten_percent, (int) widthTvPercent);

        initSeekBar();
        initBeautyFilter();


    }

    public LiveBeautyConfig getBeautyConfig() {
        if (mBeautyConfig == null) {
            mBeautyConfig = LiveBeautyConfig.get();
        }
        return mBeautyConfig;
    }

    private void initSeekBar() {
        LiveBeautyConfig config = getBeautyConfig();

        //美颜
        sb_beauty.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoBeautyUtils.updateBeautyProgress(LiveBeautyType.BEAUTY, progress);
                    getBeautyConfig().getBeautyTypeModel(LiveBeautyType.BEAUTY).setProgress(progress);
                }
                updateTextPercent(tv_beauty_percent, progress);
            }
        });
        int beauty = config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress();
        sb_beauty.setProgress(beauty);
        updateTextPercent(tv_beauty_percent, beauty);

        //美白
        sb_whiten.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoBeautyUtils.updateBeautyProgress(LiveBeautyType.WHITEN, progress);
                    getBeautyConfig().getBeautyTypeModel(LiveBeautyType.WHITEN).setProgress(progress);
                }
                updateTextPercent(tv_whiten_percent, progress);
            }
        });
        int whiten = config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress();
        sb_whiten.setProgress(whiten);
        updateTextPercent(tv_whiten_percent, whiten);

    }

    private void updateTextPercent(TextView textView, int percent) {
        textView.setText(String.valueOf(percent) + "%");
    }

    private void initBeautyFilter() {
        view_filters.setSpanCount(3);

        LiveBeautyConfig config = getBeautyConfig();

        List<LiveBeautyFilterModel> listFilter = new ArrayList<>();

        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.NONE));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.LANG_MAN));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.QING_XIN));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.WEI_MEI));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.FEN_NEN));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.HUAI_JIU));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.LAN_DIAO));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.QING_LIANG));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.RI_XI));


        filterAdapter = new LiveSetBeautyFilterAdapter(listFilter, getOwnerActivity());
        filterAdapter.setItemClickCallback(new SDItemClickCallback<LiveBeautyFilterModel>() {
            @Override
            public void onItemClick(int position, LiveBeautyFilterModel item, View view) {
                videoBeautyUtils.updateBeautyFilter(item);
            }
        });
        view_filters.setAdapter(filterAdapter);

        filterAdapter.getSelectManager().setSelected(0, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getBeautyConfig().save();
    }
}
