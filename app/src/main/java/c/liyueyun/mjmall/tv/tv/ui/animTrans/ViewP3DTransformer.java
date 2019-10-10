package c.liyueyun.mjmall.tv.tv.ui.animTrans;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;



/**
 * @author caicai
 * @create 2019/3/15
 * @Describe
 */
public class ViewP3DTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 1.00f;
    public static final float MIN_SCALE = 0.6f;
    private float mDensity = 0;

    @Override
    public void transformPage(View page, float position) {

        mDensity = page.getContext().getResources().getDisplayMetrics().density;

        if (position < 0 ) {//当前item左边的
            page.setPivotX(page.getMeasuredWidth() / 2);
        } else if (position > 0) {
            page.setPivotX(page.getMeasuredWidth() / 2);
        }
        page.setPivotY(page.getMeasuredHeight() * 0.5f);
        //pos赋值
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
//        logUtil.d_2("ViewP3DTransformer", "==" + mDensity);
        float tempScale = position < 0 ? 1 + position : 1 - position;
        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;

        page.setRotationY(-position * 60f);
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }

    }

}
