package c.liyueyun.mjmall.tv.base.httpApi.impl;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.httpApi.response.DeleteResult;


/**
 * Created by SongJie on 09/12 0012.
 */
public class MyRequest<T> extends Request<T> {
    private final String TAG = "MyRequest";
    private Gson mGson = MyApplication.getInstance().getmGson();
    private Class<T> mClazz;
    private MyVolleyListener<T> mListener;
    private Object mParam;
    private String mToken;//是否在tcp头发送token
    private String startTime;
    private String ContentType;
    private String url;
    private Type type;

    public MyRequest(int method, String url, Class<T> mClazz, MyVolleyListener<T> callback) {
        super(method, url, callback);
        this.mClazz = mClazz;
        this.url = url;
        mListener = callback;
        logUtil.d_3(TAG,"api接口访问:"+url);
    }
    public MyRequest(int method, String url, Type type, MyVolleyListener<T> callback) {
        super(method, url, callback);
        this.type = type;
        this.url = url;
        mListener = callback;
        logUtil.d_3(TAG,"api接口访问:"+url);
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }


    public void setStartTime(String time){
        startTime = time;
    }

    public void setmParam(Object mParam) {
        this.mParam = mParam;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    /**
     * 数据返回
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String strRue = new String(response.data, "UTF-8");
            logUtil.d_3(TAG,"#####api接口("+url+")返回:"+strRue);
            T result;
            if(mClazz!=null) {
                result = mGson.fromJson(strRue, mClazz);
            }else if(type != null){
                result = mGson.fromJson(strRue, type);
            }else{
                return Response.success((T) new DeleteResult(), HttpHeaderParser.parseCacheHeaders(response));
            }
            if (result == null) {
                return Response.error(new MyErrorMessage(MyErrorMessage.ERROR_PARSE, "数据解析错误\n" + strRue));
            } else {
                return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (Exception e) {
            return Response.error(new MyErrorMessage(MyErrorMessage.ERROR_PARSE,"数据解析错误"));
        }
    }

    /**
     * 回调
     */
    @Override
    protected void deliverResponse(T response) {
        if(mListener!=null) {
            mListener.onResponse(response);
        }
    }

    /**
     * 对像转成String字符串
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mParam != null) {
            String str = mGson.toJson(mParam);
            String body = null;
            try {
                JSONObject jsonObject = new JSONObject(str);
                Iterator<String> keyIter= jsonObject.keys();
                String key;
                while (keyIter.hasNext()) {
                    key = keyIter.next();
                    if(body == null) {
                        body = key+"="+jsonObject.get(key);
                    }else{
                        body = body+"&"+key+"="+jsonObject.get(key);
                    }
                }
                if(body != null) {
                    logUtil.d_2(TAG,"https send msg -->"+body);
                    return body.getBytes();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.getBody();
    }

    /**
     * 设置类型
     */
    @Override
    public String getBodyContentType() {
//        return "text/xml; charset=gb2312";
        return super.getBodyContentType();
    }

    /**
     * 设置头文件的鉴权
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> headers = new HashMap<String, String>();
//        headers.put("Charset", "UTF-8");
//        headers.put("Accept-Encoding", "identity");
//        headers.put("Connection","close"); //拿取数据后直接关闭连接
        if(mToken != null) {
            //Users.getInstance().getCurrentUser().getToken()
            headers.put("Authorization", MyConstant.Token_Prefix+ mToken);
        }
        if(ContentType != null){
            headers.put("Content-Type", ContentType);
        }
        if(!Tool.isEmpty(startTime)){
            headers.put("Client-Sync-Time", startTime);
            logUtil.d_2(TAG,"获取时间time : "+startTime);
        }
        return headers;
    }

}
