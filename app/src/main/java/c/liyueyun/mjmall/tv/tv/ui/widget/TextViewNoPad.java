package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by songjie on 2018-11-03
 * textView.setIncludeFontPadding(false);
 */
public class TextViewNoPad extends android.support.v7.widget.AppCompatTextView {
    Paint.FontMetricsInt fontMetricsInt;
    public TextViewNoPad(Context context) {
        super(context);
    }

    public TextViewNoPad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (fontMetricsInt == null){
            fontMetricsInt = new Paint.FontMetricsInt();
            getPaint().getFontMetricsInt(fontMetricsInt);
        }
        canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        super.onDraw(canvas);



    }
}