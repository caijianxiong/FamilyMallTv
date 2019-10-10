package c.liyueyun.mjmall.tv.tv.manager;

/**
 * Created by hejie on 2017/4/26.
 */

public class ImageHandler {

    public static String getAvatar(String url){
        int width = 64;
        int height = 64;
        return _getThumbnail(url, width, height);
    }

    public static String getAvatarLarge(String url){
        int width = 256;
        int height = 256;
        return _getThumbnail(url, width, height);
    }
    public static String getFit240(String url){
        int width = 240;
        int height = 240;
        return _getThumbnail(url, width, height);
    }
    public static String getFit360(String url){
        int width = 360;
        int height = 360;
        return _getThumbnail(url, width, height);
    }
    public static String getFit640(String url){
        int width = 640;
        int height = 640;
        return _getThumbnail(url, width, height);
    }

    public static String getThumbnail144P(String url){
        int width = 192;
        int height = 144;
        return _getThumbnail(url, width, height);
    };
    public static String getThumbnail240P(String url){
        int width = 320;
        int height = 240;
        return _getThumbnail(url, width, height);
    };
    public static String getThumbnail360P(String url){
        int width = 480;
        int height = 360;
        return _getThumbnail(url, width, height);
    };
    public static String getThumbnail720P(String url){
        int width = 1280;
        int height = 720;
        return _getThumbnail(url, width, height);
    };
    public static String getThumbnail1080P(String url){
        int width = 1920;
        int height = 1080;
        return _getThumbnail(url, width, height);
    };
    public static String getThumbnail4K(String url){
        int width = 3840;
        int height = 2160;
        return _getThumbnail(url, width, height);
    };
    public static String _getAvatar(String url,int width,int height){
        String str1 = _formatUrl(url);
        String str2 = "x-oss-process=image/resize,m_fill,w_" + width + ",h_" + height + ",limit_1";
        return str1 + str2;
    };

    public static String _getThumbnail(String url,int width,int height){
        String str1 = _formatUrl(url);
        String str2 = "x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height + ",limit_1";
        return str1 + str2;
    };
    public static String _formatUrl(String url){
        String url1 = url.replace("im.yun2win.com", "attachment.yun2win.com");
        if(url1.indexOf("x-oss-process") > 0)
            return url1;
        if(url1.indexOf("?") >= 0)
            url1 += "&";
        else
            url1 += "?";
        return url1;
    };
}
