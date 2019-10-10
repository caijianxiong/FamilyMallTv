package c.liyueyun.mjmall.tv.base.httpApi.api;

import com.android.volley.Request;

import c.liyueyun.mjmall.tv.BuildConfig;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyRequest;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyVolleyListener;
import c.liyueyun.mjmall.tv.base.httpApi.impl.VolleyClient;
import c.liyueyun.mjmall.tv.base.httpApi.response.UpdateResult;


/**
 * Created by SongJie on 12/09 0009.
 */

public class UpdateTemplate extends BaseTemplate{

    public UpdateTemplate(VolleyClient client, String server) {
        super(client);
        setServer(server);
    }

    /**
     * 获取服务token
     */
    public Object getUpdate(MyCallback<UpdateResult> callback) {
        String url;
        if(BuildConfig.DEBUG){
            url = getUrl("mallTV/malltv.json");
        }else{
            url = getUrl("mallTV/malltv.json");
        }
        MyRequest<?> request = new MyRequest<>(
                Request.Method.GET,
                url,
                UpdateResult.class,
                new MyVolleyListener<>(callback));
        return postRequest(request);
    }

}