package c.liyueyun.mjmall.tv.base.manage;

import android.content.Context;
import android.content.SharedPreferences;

import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.logUtil;


/**
 * Created by SongJie on 11/17 0017.
 * 文件操作加同步锁,非跨进程
 */
public class PrefManage {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private SharedPreferences mSharedPrefs;

    public enum BooleanKey{
        isShowScreen,isShowMemory,isBackRun,isAotoUpdate,isFloatConnect,isOuttimeConnect,isRelateTV, isIjkPlayHard,
        isShowDetailQRcode,isShowDetailNew,isAVCallCamera
    }

    //fileUpdataTime 文件区更新并保存后,从服务器拿下的最后更新时间
    //busyUIStatus 数字表示会议,bind标识绑定弹窗中,avcall标识通话弹窗中, null和""标识空闲
    public enum StringKey{
        versionCode,showInstallDay,videoPauseImgId,fileUpdataTime,tvNumber, busyUIStatus,uiManagerData,
        sysConferenceMts,loginTime,sysFamilyMsgMts,sysFamilyMts,lastSession,currentSession,hasShowPhotosCount
    }

    public PrefManage(Context context){
        mContext = context;
        mSharedPrefs = mContext.getSharedPreferences(MyConstant.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        logUtil.d_2(TAG,"PrefManage onCreate");
    }

    /**
     * 设置boolean类型的value
     */
    public void setBooleanValueByKey(BooleanKey key, boolean mark){
        synchronized (this){
            SharedPreferences.Editor editor1 = mSharedPrefs.edit();
            editor1.putBoolean(key.toString(), mark);
            editor1.commit();
        }
    }

    /**
     * 获取boolean类型的value
     */
    public boolean getBooleanValueByKey(BooleanKey key){
        synchronized(this) {
            if(key.equals(BooleanKey.isBackRun) ||
                    key.equals(BooleanKey.isAotoUpdate) ||
                    key.equals(BooleanKey.isFloatConnect)||
                    key.equals(BooleanKey.isIjkPlayHard)||
                    key.equals(BooleanKey.isShowDetailQRcode)||
                    key.equals(BooleanKey.isShowDetailNew)||
                    key.equals(BooleanKey.isOuttimeConnect)){
                return mSharedPrefs.getBoolean(key.toString(),true);
            }else{
                return mSharedPrefs.getBoolean(key.toString(),false);
            }
        }
    }
    /**
     * 设置boolean类型的value
     */
    public void setStringValueByKey(StringKey key,String value){
        synchronized (this){
            SharedPreferences.Editor editor1 = mSharedPrefs.edit();
            editor1.putString(key.toString(), value);
            editor1.commit();
        }
    }

    /**
     * 获取String类型的value
     */
    public String getStringValueByKey(StringKey key){
        synchronized(this) {
            return mSharedPrefs.getString(key.toString(),"");
        }
    }

}
