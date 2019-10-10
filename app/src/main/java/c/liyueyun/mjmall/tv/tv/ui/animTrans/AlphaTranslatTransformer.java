package c.liyueyun.mjmall.tv.tv.ui.animTrans;

import android.view.View;


/**
 * @author caicai
 * @create 2019/3/26
 * @Describe
 */
public class AlphaTranslatTransformer extends ABaseTransformer {
    private String TAG="AlphaTranslatTransformer";
    @Override
    protected void onTransform(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]  左侧item
            // Use the default slide transition when moving to the left page
            view.setAlpha(1+position);
            view.setTranslationX(0);
//            logUtil.d_2(TAG+"aaa","--->"+position);

        } else if (position <= 1) { // (0,1]  当前item
            // Fade the page out.
            view.setAlpha(1 - position);
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
//            logUtil.d_2(TAG+"bbb","<---"+position);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }

    }

}
