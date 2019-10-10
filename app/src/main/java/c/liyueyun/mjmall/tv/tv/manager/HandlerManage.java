package c.liyueyun.mjmall.tv.tv.manager;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import c.liyueyun.mjmall.tv.base.base.logUtil;


/**
 * ui界面通知类
 * Created by maa46 on 2016/3/10.
 */
public class HandlerManage {
    private final String TAG = this.getClass().getSimpleName();
    private static HandlerManage INSTANCE;
    private Map<String, Handler> handlerMap;

    public enum updateType{
        AVCallActivity,videoActivity,slideActivity,galleryActivity,tvfileactivity,
        filefragment,coursefragment,settingfragment,infoFragment,localLoginService,
        bindService,emallFragment,contactFragment,callFragment,splashActivity,
        /** 以下为企业版新增 */
        homeActivity,conferenceActivity, contactActivity, companyServiceActivity, showBindActivity,
        fileActivity,clubMessageActivity,clubMessageDetailActivity,leaveCompanyActivity,BusinessCardActivity,
        BuyRoomDetailActivity,CenterActivity,MyCloudActivity,clubHomeActivity,
        /**家庭版新增*/
        familyAlbumActivity,photoGrallyActivity,albumActivity,allPhotosActivity,myFamilyActivity,guideActivity,

        familyGroupMsgManagerHandler,familyGroupManager
    }

    public static HandlerManage getInstance() {
        if(INSTANCE == null){
            INSTANCE = new HandlerManage();
        }
        return INSTANCE;
    }

    public HandlerManage(){
        handlerMap = new HashMap<>();
    }

    public void addHandler(updateType type,Handler handler){
        if(handler!=null) {
            handlerMap.put(type.toString(), handler);
        }else{
            logUtil.d_2(TAG,type.toString()+":handler is null");
        }
    }

    public Handler getHandler(updateType type){
        return handlerMap.get(type.toString());
    }

    public void removeHandler(updateType type){
        handlerMap.remove(type.toString());
    }
}
