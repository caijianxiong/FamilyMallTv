package c.liyueyun.mjmall.tv.tv.ui.base;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public interface IBaseView {
    /**
     * 显示进度条
     * @param msg   进度条加载内容
     */
    void showLoading(String msg, boolean isCanelListener);
    void loadingCanelListener();
    /**
     * 隐藏进度条
     */
    void hideLoading();

    /**
     * 错误弹窗
     */
    void showErrorDialog(String errorStr, boolean isBtnListener);
    void errorBtnListener();
    /**
     * 弹toast
     */
    void showMsgToast(String msg);
}
