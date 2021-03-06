package com.bogokj.live.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.bogokj.library.utils.SDViewUtil;

/**
 * 直播间聊天列表礼物图标span
 */
public class LiveMsgGiftSpan extends SDNetImageSpan
{

    public LiveMsgGiftSpan(View view)
    {
        super(view);
        setWidth(SDViewUtil.dp2px(15));

    }

    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight=rect.bottom-rect.top;

            int top= drHeight/2 - fontHeight/4;
            int bottom=drHeight/2 + fontHeight/4;

            fm.ascent=-bottom;
            fm.top=-bottom;
            fm.bottom=top;
            fm.descent=top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        transY = ((bottom-top) - b.getBounds().bottom)/2+top;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }


}
