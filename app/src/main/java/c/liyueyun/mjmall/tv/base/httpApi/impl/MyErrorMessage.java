package c.liyueyun.mjmall.tv.base.httpApi.impl;

import android.content.Intent;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.manage.UserManage;


/**
 * Created by SongJie on 11/07 0007.
 ** @Description: S 系统抛出错误
 */
public class MyErrorMessage extends VolleyError {
    private final String TAG = this.getClass().getSimpleName();
    public static final String ERROR_NO_CONNECTION = "S001";
    public static final String ERROR_NETWORK = "S002";
    public static final String ERROR_SERVER = "S003";
    public static final String ERROR_TIMEOUT = "S004";
    public static final String ERROR_PARSE = "S005";
    public static final String ERROR_UNKNOWN = "S006";

    private String code="";
    private String message = "";

    //用户自定义错误
    public MyErrorMessage(String code, String msg) {
        this.code = code;
        this.message = msg;
    }
    //volley错误
    public MyErrorMessage(VolleyError e) {
        if (e instanceof NoConnectionError) {
            code = MyErrorMessage.ERROR_NO_CONNECTION;
            message = "无网络，请检查网络！";
        } else if (e instanceof NetworkError) {
            code = MyErrorMessage.ERROR_NETWORK;
            message = "网络错误，请检查网络！";
        } else if (e instanceof TimeoutError) {
            code = MyErrorMessage.ERROR_TIMEOUT;
            message = "网络请求超时，请重试！";
        } else if (e instanceof ParseError) {
            code = MyErrorMessage.ERROR_PARSE;
            message = "数据解析错误，请重试！";
        } else if (e instanceof ServerError) {
            code = MyErrorMessage.ERROR_SERVER;
            message = "返回错误，请重试！";
        } else if (e instanceof MyErrorMessage){
            MyErrorMessage msg = (MyErrorMessage) e;
            code = msg.code;
            message = msg.message;
        } else {
            code = MyErrorMessage.ERROR_UNKNOWN;
        }
        if(e!=null && e.networkResponse!=null) {
            String msgData = new String(e.networkResponse.data);
            if (!Tool.isEmpty(msgData)) {
                message = message + "\n" + msgData;
            }
            if(e.networkResponse.statusCode == 401){
                UserManage.getInstance().setCurrentUser(null);
            }
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
