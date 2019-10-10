package c.liyueyun.mjmall.tv.tv.ui.anim;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by SongJie on 12/22 0022.
 */

public class ScalePageTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 1.00f;
    public static final float MIN_SCALE = 0.7f;
    /**核心就是实现transformPage(View page, float position)这个方法**/
    @Override
    public void transformPage(View page, float position) {

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}