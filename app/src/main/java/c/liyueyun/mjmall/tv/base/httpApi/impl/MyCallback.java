package c.liyueyun.mjmall.tv.base.httpApi.impl;

/**
 * Created by SongJie on 09/12 0012.
 */
public interface MyCallback<T> {

    public void onError(MyErrorMessage msg);

    public void onSuccess(T response);

    public void onFinish();
}