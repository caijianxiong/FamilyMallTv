package c.liyueyun.mjmall.tv.base.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tendcloud.tenddata.TCAgent;
import com.yun2win.utils.LogFileUtil;
import java.util.LinkedList;
import java.util.List;
import c.liyueyun.mjmall.tv.BuildConfig;
import c.liyueyun.mjmall.tv.base.httpApi.api.ApiTemplate;
import c.liyueyun.mjmall.tv.base.manage.PrefManage;
import c.liyueyun.mjmall.tv.base.manage.UserManage;

/**
 * Created by SongJie on 11/15 0015.
 */
public class MyApplication extends Application {
    private final String TAG = this.getClass().getSimpleName();
    private static Context appContext;
    private static MyApplication instance;
    private List<Activity> mList = new LinkedList<>();
    private PrefManage prefManage;
    private RequestQueue mRequestQueue;
    private ApiTemplate mApi;
    private ProcessKey processKey;
    private Gson mGson;

    public enum ProcessKey {
        UI, IM, browser, avcall
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logUtil.d_2(TAG, "Application oncreate");
        appContext = this;
        instance = this;
        //初始化LOG日志
        LogFileUtil.isWrite = false;
        logUtil.initLog();
        logUtil.setBugLevel(BuildConfig.DEBUG ? 3 : 1);
        //初始化异常铺货器
        Exception_Handler.getInstance(this);
        //X5浏览服务
//        QbSdk.initX5Environment(this,null);
        //初始化SharedPreferences,只有UI进程才可使用
        prefManage = new PrefManage(appContext);
        //volley
        mRequestQueue = Volley.newRequestQueue(appContext);
        mApi = new ApiTemplate(mRequestQueue);
        //初始化Gson
        mGson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        //song 分进程功能
        processKey = ProcessKey.UI;
        logUtil.d_3(TAG, "process name is " + processKey);
//                        SpeechUtility.createUtility(appContext, "appid=" + "5bc55ac8");
//                        VoiceSynthesisManager.getSingleton().init(appContext);
        //TalkingData
        TCAgent.init(this,"2EA70AC1CABE4956AE4D6F493A721C43",BuildConfig.FLAVOR);
        TCAgent.setReportUncaughtExceptions(true);
        //创建用户数据库
        UserManage.getInstance();
        //建立与IM的aidl连接
        //清空保存的状态,渠道屏蔽部分功能
        prefManage.setStringValueByKey(PrefManage.StringKey.busyUIStatus, null);
        if (BuildConfig.FLAVOR.equals("xiaomi")) {
            prefManage.setBooleanValueByKey(PrefManage.BooleanKey.isBackRun, false);
            prefManage.setBooleanValueByKey(PrefManage.BooleanKey.isAotoUpdate, false);
        } else if (BuildConfig.FLAVOR.equals("coocaa")) {
            prefManage.setBooleanValueByKey(PrefManage.BooleanKey.isAotoUpdate, false);
        }
        //后台自动更新APK
//                        if (prefManage.getBooleanValueByKey(PrefManage.BooleanKey.isAotoUpdate)) {
//                        UpdateManager.getInstance().checkAppUpdate(MyApplication.getAppContext());
//                        }
        //清楚图片缓冲
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Glide.get(appContext).clearDiskCache();
//                            }
//                        }).start();
        //song 推送子进程
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }


    public void removeActivity(Activity activity) {
        if (activity != null)
            mList.remove(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UserManage.getInstance().setCurrentUser(null);
            System.exit(0);
        }
    }


    public static Context getAppContext() {
        return appContext;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public ApiTemplate getmApi() {
        return mApi;
    }

    public ProcessKey getProcessKey() {
        return processKey;
    }

    public Gson getmGson() {
        return mGson;
    }

    /**
     * 此方法只允许UI进程调用
     */
    public PrefManage getPrefManage() {
        return prefManage;
    }
}
