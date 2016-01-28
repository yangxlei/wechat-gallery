package crop.common.baijiahulian.com.mygallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by yanglei on 16/1/23.
 */
public class ColorCheckBox extends CheckBox {
    public ColorCheckBox(Context context) {
        this(context, null);
    }

    public ColorCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    @Override
    public void setButtonDrawable(Drawable drawable) {
        super.setButtonDrawable(drawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
