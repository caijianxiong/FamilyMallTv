package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * @author caicai
 * @create 2019/3/11
 * @Describe
 */
public class NoFastViewPager extends ViewPager {
    private String TAG = "NoFastViewPager";

    private long mLastKeyDownTime=0;
    public NoFastViewPager(Context context) {
        super(context);
    }

    public NoFastViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {//设置长按，响应间隔为150
            long current = System.currentTimeMillis();
            boolean dispatch;
            if (current - mLastKeyDownTime < 150) {
                dispatch= true;
            } else {
                dispatch=super.dispatchKeyEvent(event);
                mLastKeyDownTime = current;
            }
            return dispatch;
    }

}
