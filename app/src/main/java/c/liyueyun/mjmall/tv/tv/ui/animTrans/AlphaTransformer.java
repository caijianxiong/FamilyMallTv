package c.liyueyun.mjmall.tv.tv.ui.animTrans;

import android.os.Build;
import android.view.View;
/**
 * @author caicai
 * @create 2019/3/26
 * @Describe
 */
public class AlphaTransformer extends ABaseTransformer {
    private String TAG="AlphaTransformer";
    public static final float MAX_SCALE = 1.00f;
    public static final float MIN_SCALE = 0f;

    @Override
    protected void onTransform(View page, float position) {
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
//        logUtil.d_2(TAG,"scaleValue="+scaleValue);
        page.setAlpha(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }

}
