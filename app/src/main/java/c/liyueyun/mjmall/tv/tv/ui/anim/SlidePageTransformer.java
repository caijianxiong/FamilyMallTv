package c.liyueyun.mjmall.tv.tv.ui.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by SongJie on 03/02 0002.
 */

public class SlidePageTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 0.75f;
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            float scaleFactor = MIN_SCALE+ (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setAlpha(0);
        }
    }
}