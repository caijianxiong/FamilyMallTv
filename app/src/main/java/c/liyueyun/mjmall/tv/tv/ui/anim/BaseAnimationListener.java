package c.liyueyun.mjmall.tv.tv.ui.anim;

import android.view.animation.Animation;

/**
 * @author caicai
 * @create 2019/3/28
 * @Describe
 */
public  class BaseAnimationListener implements Animation.AnimationListener {

    private AnimationListener animationListener;
    public BaseAnimationListener(AnimationListener animationListener) {
        this.animationListener=animationListener;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animationListener.onAnimEnd();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface AnimationListener{
        void onAnimEnd();
    }

}
