package c.liyueyun.mjmall.tv.base.httpApi.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import c.liyueyun.mjmall.tv.base.base.logUtil;


/**
 * Created by SongJie on 09/12 0012.
 */
public class MyVolleyListener<T> implements Response.Listener<T>, Response.ErrorListener {
    private final String TAG = this.getClass().getSimpleName();
    private MyCallback<T> mCallback;
    public MyVolleyListener(MyCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError e) {
        MyErrorMessage errorMsg= new MyErrorMessage(e);
        logUtil.d_3(TAG,("https error = "+errorMsg.getMessage()).replace("\n"," ::: "));
        if (null != mCallback) {
            mCallback.onError(errorMsg);
            mCallback.onFinish();
        }
    }

    @Override
    public void onResponse(T response) {
        if (null != mCallback && response !=null) {
            mCallback.onSuccess(response);
            mCallback.onFinish();
        }
    }

}