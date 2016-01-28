package com.baijiahulian.common.crop.uikit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by yanglei on 16/1/23.
 */
public class CCButton extends Button {
    public CCButton(Context context) {
        this(context, null);
    }

    public CCButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CCButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBackgroundPaint = new Paint();
    }

    private Paint mBackgroundPaint;

    @Override
    public void setBackgroundColor(int color) {
//        super.setBackgroundColor(color);
        mBackgroundPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        RectF rectf = new RectF(0, 0, getWidth(), getHeight());

        canvas.drawRoundRect(rectf, 4, 4, mBackgroundPaint);


        super.onDraw(canvas);
    }
}
