package com.tencent.qcloud.xiaoshipin.videoeditor.bgm;

import android.database.Cursor;
import android.provider.MediaStore;

import com.bogokj.hybrid.app.App;
import com.tencent.qcloud.xiaoshipin.videoeditor.bgm.utils.TCBGMInfo;

import java.util.ArrayList;

/**
 * Created by vinsonswang on 2017/12/8.
 */

public class TCBGMManager {
    private static final String TAG = "TCBgmManager";

    private LoadBgmListener mLoadBgmListener;

    private static class TCBgmMgrHolder {
        private static TCBGMManager instance = new TCBGMManager();
    }

    public static TCBGMManager getInstance() {
        return TCBgmMgrHolder.instance;
    }

//    public void loadBgmList(){
//        if(isLoading){
//            TXCLog.e(TAG, "loadBgmList, is loading");
//            return;
//        }
//        isLoading = true;
//        TCHttpEngine.getInstance().get(TCConstants.SVR_BGM_GET_URL, new TCHttpEngine.Listener() {
//            @Override
//            public void onResponse(int retCode, String retMsg, JSONObject retData) {
//                TXCLog.i(TAG, "retData = " + retData);
//                try {
//                    JSONObject bgmObject = retData.getJSONObject("bgm");
//                    if(bgmObject == null && mLoadBgmListener != null){
//                        mLoadBgmListener.onBgmList(null);
//                        return;
//                    }
//                    JSONArray list = bgmObject.getJSONArray("list");
//                    Type listType = new TypeToken<ArrayList<TCBGMInfo>>(){}.getType();
//                    ArrayList<TCBGMInfo> bgmInfoList = new Gson().fromJson(list.toString(), listType);
//
//                    getLocalPath(bgmInfoList);
//                    if(mLoadBgmListener != null){
//                        mLoadBgmListener.onBgmList(bgmInfoList);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    isLoading = false;
//                }
//            }
//        });

//    }

    //定义一个集合，存放从本地读取到的内容
    public ArrayList<TCBGMInfo> list;

    public TCBGMInfo song;
    private static String name;
    private static String singer;
    private static String path;
    private static int duration;
    private static long size;
    private static long albumId;
    private static long id;
    //获取专辑封面的Uri

    public void loadBgmList() {

        list = new ArrayList<>();


        Cursor cursor = App.getApplication().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                song = new TCBGMInfo();
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //list.add(song);
                //把歌曲名字和歌手切割开
//                song.setName(name);
                song.setSinger(singer);
                song.setPath(path);
                song.setDuration(duration);
                song.setSize(size);
                song.setId(id);
                song.setAlbumId(albumId);
                if (size > 1000 * 800) {
                    if (name.contains("-")) {
                        String[] str = name.split("-");
                        singer = str[0];
                        song.setSinger(singer);
                        name = str[1];
                        song.setName(name);
                    } else {
                        song.setName(name);
                    }
                    list.add(song);
                }
            }
        }
        cursor.close();
        if (mLoadBgmListener != null) {
            mLoadBgmListener.onBgmList(list);
        }
    }

    //    转换歌曲时间的格式
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }
//    /**
//     * 根据bgmList，获取本地已保存过的路径
//     * @param bgmInfoList
//     */
//    private void getLocalPath(ArrayList<TCBGMInfo> bgmInfoList){
//        if(bgmInfoList == null || bgmInfoList.size() == 0){
//            return;
//        }
//        for(TCBGMInfo tcbgmInfo : bgmInfoList){
//            tcbgmInfo.localPath = mPrefs.getString(tcbgmInfo.name, "");
//        }
//        for(TCBGMInfo tcbgmInfo : bgmInfoList){
//            if(!tcbgmInfo.localPath.equals("")){
//                tcbgmInfo.status = TCBGMInfo.STATE_DOWNLOADED;
//            }
//        }
//    }
//
//    public void downloadBgmInfo(final String bgmName, final int position, String url){
//        TCBGMDownloadProgress tcbgmDownloadProgress = new TCBGMDownloadProgress(bgmName, position, url);
//        tcbgmDownloadProgress.start(new TCBGMDownloadProgress.Downloadlistener() {
//            @Override
//            public void onDownloadFail(String errorMsg) {
//                LoadBgmListener loadBgmListener = null;
//                synchronized (TCBGMManager.this){
//                    loadBgmListener = mLoadBgmListener;
//                }
//
//                if(loadBgmListener != null){
//                    loadBgmListener.onDownloadFail(position, errorMsg);
//                }
//            }
//
//            @Override
//            public void onDownloadProgress(int progress) {
//                TXCLog.i(TAG, "downloadBgmInfo, progress = " + progress);
//                LoadBgmListener loadBgmListener = null;
//                synchronized (TCBGMManager.this){
//                    loadBgmListener = mLoadBgmListener;
//                }
//                if(loadBgmListener != null){
//                    loadBgmListener.onDownloadProgress(position, progress);
//                }
//            }
//
//            @Override
//            public void onDownloadSuccess(String filePath) {
//                TXCLog.i(TAG, "onDownloadSuccess, filePath = " + filePath);
//                LoadBgmListener loadBgmListener = null;
//                synchronized (TCBGMManager.this){
//                    loadBgmListener = mLoadBgmListener;
//                }
//                if(loadBgmListener != null){
//                    loadBgmListener.onBgmDownloadSuccess(position, filePath);
//                }
//                // 本地保存，防止重复下载
//                synchronized (TCBGMManager.this) {
//                    mPrefs.edit().putString(bgmName, filePath).apply();
//                }
//            }
//        });
//
//    }

    public void setOnLoadBgmListener(LoadBgmListener loadBgmListener) {
        synchronized (this) {
            mLoadBgmListener = loadBgmListener;
        }
    }

    public interface LoadBgmListener {

        void onBgmList(final ArrayList<TCBGMInfo> tcBgmInfoList);

        void onBgmDownloadSuccess(int position, String filePath);

        void onDownloadFail(int position, String errorMsg);

        void onDownloadProgress(int position, int progress);
    }
}
