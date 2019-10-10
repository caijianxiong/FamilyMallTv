package c.liyueyun.mjmall.tv.base.httpApi.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;

import c.liyueyun.mjmall.tv.base.httpApi.impl.MyRequest;
import c.liyueyun.mjmall.tv.base.httpApi.impl.VolleyClient;


/**
 * Created by SongJie on 09/12 0012.
 */
public class BaseTemplate {
    protected VolleyClient mClient;
    private String mServer;

    public BaseTemplate(VolleyClient client) {
        mClient = client;
    }

    protected void setServer(String server){
        if (!server.endsWith("/"))
            mServer = server + "/";
        else
            mServer = server;
    }
    /**
     * 得到访问的具体url接口
     */
    public String getUrl(String method) {
        return mServer + method;
    }

    protected Object postRequest(MyRequest<?> request) {
        Object tag = System.currentTimeMillis();
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mClient.post(request);
        return tag;
    }

    protected Object getRequest(StringRequest request) {
        Object tag = System.currentTimeMillis();
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy());
        mClient.get(request);
        return tag;
    }

    public void cancelRequest(Object tag) {
        mClient.cancel(tag);
    }

}
