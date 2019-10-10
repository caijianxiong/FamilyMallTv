package c.liyueyun.mjmall.tv.base.httpApi.impl;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by SongJie on 09/12 0012.
 */
public class VolleyClient {
    private RequestQueue mRequestQueue;

    public VolleyClient(RequestQueue queue) {
        mRequestQueue = queue;
    }

    public void post(MyRequest<?> request) {
        mRequestQueue.add(request);
    }

    public void cancel(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public void get(StringRequest request) {
        mRequestQueue.add(request);
    }

}
