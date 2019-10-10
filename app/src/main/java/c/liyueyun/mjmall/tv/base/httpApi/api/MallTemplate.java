package c.liyueyun.mjmall.tv.base.httpApi.api;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyRequest;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyVolleyListener;
import c.liyueyun.mjmall.tv.base.httpApi.impl.VolleyClient;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallEventProdsResult;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallHomeMenuResult;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallProdResult;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallRowItem;


/**
 * Created by songjie on 2019-03-27
 */
public class MallTemplate extends BaseTemplate{

    public MallTemplate(VolleyClient client, String server) {
        super(client);
        setServer(server);
    }

    /**
     * 获取主界面菜单数据
     */
    public Object getHomeMenu(MyCallback<MallHomeMenuResult> callback){
        String url = getUrl("index?source=tanba");//index?source=tanba&province=广东&city=深圳
        MyRequest<?> request = new MyRequest<>(
                Request.Method.GET,
                url,
                MallHomeMenuResult.class,
                new MyVolleyListener<>(callback));
        request.setContentType("application/x-www-form-urlencoded");
        return postRequest(request);
    }

    /**
     * 获取单个菜单的详细数据
     */
    public Object getMenuItem(String menuId, MyCallback<List<MallRowItem>> callback){
        String url = getUrl("menu/"+menuId);
        MyRequest<?> request = new MyRequest<>(
                Request.Method.GET,
                url,
                new TypeToken<List<MallRowItem>>(){}.getType(),
                new MyVolleyListener<>(callback));
        request.setContentType("application/x-www-form-urlencoded");
        return postRequest(request);
    }

    /**
     * 获取商品详情
     */
    public Object getProdInfo(String prodId, MyCallback<MallProdResult> callback){
        String url = getUrl("item/"+prodId);
        MyRequest<?> request = new MyRequest<>(
                Request.Method.GET,
                url,
                MallProdResult.class,
                new MyVolleyListener<>(callback));
        request.setContentType("application/x-www-form-urlencoded");
        return postRequest(request);
    }

    /**
     * 获取活动商品列表
     */
    public Object getEventProds(String eventId, MyCallback<MallEventProdsResult> callback){
        String url = getUrl("event/"+eventId);
        MyRequest<?> request = new MyRequest<>(
                Request.Method.GET,
                url,
                MallEventProdsResult.class,
                new MyVolleyListener<>(callback));
        request.setContentType("application/x-www-form-urlencoded");
        return postRequest(request);
    }
}
