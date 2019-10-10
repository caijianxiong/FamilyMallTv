package c.liyueyun.mjmall.tv.tv.ui.base;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    private V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public V getView() {
        return mView;
    }

    /**
     * 检查view和presenter是否连接
     */
    public boolean isAttachView() {
        return mView != null;
    }


}
