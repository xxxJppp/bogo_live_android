package com.bogokj.live.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bogokj.hybrid.app.App;
import com.bogokj.live.R;


/**
 * Glide帮助类
 */
public class GlideUtil {
    /**
     * 默认调用方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param <T>
     * @return
     */
    public static <T> RequestBuilder<T> load(T model) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.bg_image_loading)
                .error(R.drawable.bg_image_loading)
                .dontAnimate();
        return (RequestBuilder<T>) Glide.with(App.getApplication()).load(model)
                .apply(options);
    }

    //---------以下为扩展方法------------

    /**
     * 加载用户头像方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param <T>
     * @return
     */
    public static <T> RequestBuilder<T> loadHeadImage(T model) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.bg_head_image_loading)
                .error(R.drawable.bg_head_image_loading)
                .dontAnimate();
        return (RequestBuilder<T>) load(model)
                .apply(options);
    }


    /**
     * 加载背景图片方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param view  tagert view
     * @return
     */
    /*public static <T> void loadBackground(T model,final View view)
    {
        Glide.with(App.getApplication()).load(model).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>()
        {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
            {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
    }*/
}
