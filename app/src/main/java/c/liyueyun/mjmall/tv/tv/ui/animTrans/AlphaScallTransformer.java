package c.liyueyun.mjmall.tv.tv.ui.animTrans;

import android.view.View;
/**
 * @author caicai
 * @create 2019/3/26
 * @Describe
 */
public class AlphaScallTransformer extends ABaseTransformer {
    private String TAG="AlphaTransformer";
    private static final float MAX_SCALE = 1f;
    private static final float MIN_SCALE = 1.7f;


    @Override
    protected void onTransform(View view, float position) {

        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight()/2);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1+position);
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);


        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }

    }

}
