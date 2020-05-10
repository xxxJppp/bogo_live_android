package com.bogokj.live.span;

import android.graphics.Bitmap;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bogokj.hybrid.app.App;
import com.bogokj.library.common.SDBitmapCache;
import com.fanwe.lib.span.SDDynamicDrawableSpan;
import com.bogokj.live.R;
import com.bogokj.live.utils.GlideUtil;

public class SDNetImageSpan extends SDDynamicDrawableSpan
{

    private String url;

    public SDNetImageSpan(View view)
    {
        super(view);
    }

    public SDNetImageSpan setImage(String url)
    {
        this.url = url;
        return this;
    }

    @Override
    protected int getDefaultDrawableResId()
    {
        return R.drawable.nopic_expression;
    }

    @Override
    protected Bitmap onGetBitmap()
    {
        Bitmap bitmap = SDBitmapCache.getInstance().get(url);

        if (bitmap == null)
        {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.bg_image_loading)
                    .error(R.drawable.bg_image_loading)
                    .dontAnimate();

            Glide.with(App.getApplication())
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            //加载成功，resource为加载到的bitmap
                            SDBitmapCache.getInstance().put(url, resource);
                            getView().postInvalidate();
                        }
                    });
        }
        return bitmap;
    }

}
