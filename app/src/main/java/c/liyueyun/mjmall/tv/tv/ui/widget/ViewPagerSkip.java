package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by songjie on 2019-04-18
 * 解决跳转很远会加载不出画面问题
 */
public class ViewPagerSkip extends ViewPager {
    private ViewPagerScroller scroller;

    public ViewPagerSkip(Context context) {
        this(context, null);
    }

    public ViewPagerSkip(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (scroller == null)
            scroller = new ViewPagerScroller(context);
        scroller.initViewPagerScroll(this);
    }


    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (Math.abs(getCurrentItem() - item) > 1) {
            int durtion = scroller.getmDuration();
            scroller.setScrollDuration(0);
            super.setCurrentItem(item, smoothScroll);
            scroller.setScrollDuration(durtion);
        } else {
            super.setCurrentItem(item, smoothScroll);
        }
    }

    /**
     * 设置翻页的时间
     */
    public void setScrollDuration(int dur) {
        scroller.setScrollDuration(dur);
    }

}
