package com.bogokj.hybrid.jshandler;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.bogokj.hybrid.activity.AppWebViewActivity;
import com.bogokj.hybrid.activity.MyCaptureActivity;
import com.bogokj.hybrid.app.App;
import com.bogokj.hybrid.event.EApns;
import com.bogokj.hybrid.event.EClipBoardText;
import com.bogokj.hybrid.event.ECutPhoto;
import com.bogokj.hybrid.event.EIsExistInstalled;
import com.bogokj.hybrid.event.EJsLoginSuccess;
import com.bogokj.hybrid.event.ELoginSdk;
import com.bogokj.hybrid.event.EPaySdk;
import com.bogokj.hybrid.event.ERefreshReload;
import com.bogokj.hybrid.event.EShareSdk;
import com.bogokj.hybrid.event.ETencentLocationAddress;
import com.bogokj.hybrid.event.ETencentLocationMap;
import com.bogokj.hybrid.model.CutPhotoModel;
import com.bogokj.hybrid.model.OpenTypeModel;
import com.bogokj.hybrid.model.PaySdkModel;
import com.bogokj.hybrid.model.SdkShareModel;
import com.bogokj.hybrid.utils.IntentUtil;
import com.bogokj.library.common.SDActivityManager;
import com.bogokj.library.handler.js.AppJsWHandler;
import com.bogokj.library.utils.SDJsonUtil;
import com.bogokj.library.utils.SDToast;
import com.bogokj.live.dao.UserModelDao;
import com.bogokj.live.model.UserModel;
import com.bogokj.shop.jshandler.request.RequestHandler;
import com.sunday.eventbus.SDEventManager;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-18 上午9:49:34 类说明
 */
public class AppJsHandler extends AppJsWHandler
{
    public static final int REQUEST_CODE_QR = 99;// 二维码
    public static final int REQUEST_CODE_WEB_ACTIVITY = 2;// WEB回调

    private static final String DEFALUT_NAME = "App";

    public AppJsHandler(Activity activity)
    {
        super(DEFALUT_NAME, activity);
    }

    @JavascriptInterface
    public void check_network()
    {
        IntentUtil.openNetwork(getActivity());
    }

    @JavascriptInterface
    public void refresh_reload()
    {
        ERefreshReload event = new ERefreshReload();
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void sdk_share(String json)
    {
        try
        {
            SdkShareModel model = JSON.parseObject(json, SdkShareModel.class);
            EShareSdk event = new EShareSdk();
            event.model = model;
            event.json = json;
            SDEventManager.post(event);
        } catch (Exception e)
        {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    @JavascriptInterface
    public void pay_sdk(String json)
    {
        try
        {
            PaySdkModel model = JSON.parseObject(json, PaySdkModel.class);

            EPaySdk event = new EPaySdk();
            event.model = model;
            event.json = json;
            SDEventManager.post(event);
        } catch (Exception e)
        {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    @JavascriptInterface
    public void login_success(String json)
    {
        try
        {
            UserModel user = SDJsonUtil.json2Object(json, UserModel.class);
            UserModelDao.insertOrUpdate(user);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        EJsLoginSuccess event = new EJsLoginSuccess();
        event.json = json;
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void open_type(String json)
    {
        try
        {
            OpenTypeModel model = JSON.parseObject(json, OpenTypeModel.class);

            Intent intent_open_type = null;
            if (model.getOpen_url_type() == 0)
            {
                intent_open_type = new Intent(getActivity(), AppWebViewActivity.class);
                intent_open_type.putExtra(AppWebViewActivity.EXTRA_URL, model.getUrl());
            } else if (model.getOpen_url_type() == 2)
            {
                intent_open_type = new Intent(getActivity(), AppWebViewActivity.class);
                intent_open_type.putExtra(AppWebViewActivity.EXTRA_URL, model.getUrl());
                intent_open_type.putExtra(AppWebViewActivity.EXTRA_CODE, model.getOpen_url_type());
            } else
            {
                intent_open_type = IntentUtil.showHTML(model.getUrl());
            }
            getActivity().startActivityForResult(intent_open_type, REQUEST_CODE_WEB_ACTIVITY);
        } catch (Exception e)
        {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    @JavascriptInterface
    public void qr_code_scan()
    {
        Intent intent_qr_code_scan = new Intent(getActivity(), MyCaptureActivity.class);
        getActivity().startActivityForResult(intent_qr_code_scan, REQUEST_CODE_QR);
    }

    @JavascriptInterface
    public void getClipBoardText()
    {
        Activity activity = SDActivityManager.getInstance().getLastActivity();
        ClipboardManager cbm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text = cbm.getText().toString();
        if (!TextUtils.isEmpty(text))
        {
            EClipBoardText event = new EClipBoardText();
            event.text = text;
            SDEventManager.post(event);
        } else
        {
            SDToast.showToast("您还未复制内容哦");
        }
    }

    @JavascriptInterface
    public void CutPhoto(String json)
    {
        try
        {
            CutPhotoModel model = JSON.parseObject(json, CutPhotoModel.class);

            ECutPhoto event = new ECutPhoto();
            event.model = model;
            event.json = json;
            SDEventManager.post(event);
        } catch (Exception e)
        {
            e.printStackTrace();
            SDToast.showToast("数据解析异常");
        }
    }

    @JavascriptInterface
    public void restart()
    {
        Intent i = App.getApplication().getPackageManager().getLaunchIntentForPackage(App.getApplication().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SDActivityManager.getInstance().getLastActivity().startActivity(i);
    }

    @JavascriptInterface
    public void position()
    {
        ETencentLocationMap event = new ETencentLocationMap();
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void position2()
    {
        ETencentLocationAddress event = new ETencentLocationAddress();
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void apns()
    {
        EApns event = new EApns();
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void login_sdk(String login_sdk_type)
    {
        ELoginSdk event = new ELoginSdk();
        event.type = login_sdk_type;
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void is_exist_installed(String scheme)
    {
        EIsExistInstalled event = new EIsExistInstalled();
        event.packname = scheme;
        SDEventManager.post(event);
    }

    @JavascriptInterface
    public void setTextToClipBoard(String msg_str)
    {
        Activity activity = SDActivityManager.getInstance().getLastActivity();
        ClipboardManager cbm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(msg_str))
        {
            cbm.setText(msg_str);
        }
    }

    @JavascriptInterface
    public void savePhotoToLocal(String img_url_str)
    {
        RequestHandler handler = new RequestHandler(getActivity());
        handler.savePicture(img_url_str);
    }
}
