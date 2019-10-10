package c.liyueyun.mjmall.tv.base.httpApi.api;

import com.android.volley.Request;

import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyRequest;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyVolleyListener;
import c.liyueyun.mjmall.tv.base.httpApi.impl.VolleyClient;
import c.liyueyun.mjmall.tv.base.httpApi.response.UIMangerResult;


/**
 * Created by songjie on 003 1/3.
 */

public class UIManagerTemplate extends BaseTemplate{

    public UIManagerTemplate(VolleyClient client, String server) {
        super(client);
        setServer(server);
    }

    /**
     * 获取服务器管理数据
     */
    public Object getUIManagerData(String mToken, MyCallback<UIMangerResult> callback){
        String url = getUrl("binding");
        MyRequest<?> request = new MyRequest<>(
                Request.Method.GET,
                url,
                UIMangerResult.class,
                new MyVolleyListener<>(callback));
        request.setmToken(mToken);
        return postRequest(request);
    }
}
