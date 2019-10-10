package c.liyueyun.mjmall.tv.tv.ui.base;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public interface IBasePresenter<V extends IBaseView> {

    /**
     * presenter和对应的view绑定
     * @param view  目标view
     */
    void attachView(V view);
    /**
     * presenter与view解绑
     */
    void detachView();

}
