package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import c.liyueyun.mjmall.tv.R;

@SuppressLint("AppCompatCustomView")
public class AutoScaleTextView extends TextView {


    private static float DEFAULT_MIN_TEXT_SIZE = 8;
    private static float DEFAULT_MAX_TEXT_SIZE = 72;
    private Paint testPaint;
    private float minTextSize;
    private float maxTextSize;

    public AutoScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DEFAULT_MAX_TEXT_SIZE=context.getResources().getDimension(R.dimen.sp_1080p_72px);
        initialise();
    }

    private void initialise() {
        testPaint = new Paint();
        testPaint.set(this.getPaint());
        maxTextSize = this.getTextSize();//xml设置的字体
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MIN_TEXT_SIZE;
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }

    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);//TypedValue.COMPLEX_UNIT_PX不可少，将单位设置为像素
        }
    }
}
