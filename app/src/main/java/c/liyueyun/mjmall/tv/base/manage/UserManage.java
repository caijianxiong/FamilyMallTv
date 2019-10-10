package c.liyueyun.mjmall.tv.base.manage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.tendcloud.tenddata.TCAgent;

import java.util.HashMap;
import java.util.Map;

import c.liyueyun.mjmall.tv.base.ContentProvider.ContentData;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.entities.LocalUser;
import c.liyueyun.mjmall.tv.base.httpApi.response.UserInfoResult;


/**
 * Created by SongJie on 05/25 0025.
 * 本地用户数据有IM进程管理
 * 绑定用户数据有UI进程管理
 */

public class UserManage {
    private final String TAG = this.getClass().getSimpleName();
    private static UserManage INSTANCE;
    private UserInfoResult bindUserinfo;
    private LocalUser localUser;
    private Uri uri;
    private Gson mGson = MyApplication.getInstance().getmGson();
    private ContentResolver cr;
    private static int time = 0;

    private final int STATIS = 10000;
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case STATIS:
                    time++;
                    myHandler.sendEmptyMessageDelayed(STATIS, 60 * 1000);
                    if (time > 9 && time % 10 == 1) {
                        Map kv = new HashMap();
                        kv.put("时间", Tool.getCurrentFormatDate(time - 1 + "分钟"));
                        TCAgent.onEvent(MyApplication.getAppContext(), "绑定时长", "每10分钟一次", kv);
                        logUtil.d_3(TAG, "发送绑定统计时长:" + (time - 1) + "分钟");
                    }
                    break;
            }
            return false;
        }
    });

    public LocalUser getLocalUser() {
        if (localUser == null) {
            if (!MyApplication.getInstance().getProcessKey().equals(MyApplication.ProcessKey.IM)) {
                getProviderLocalUser();
//                if (ImAidlManage.getInstance().isConnect()) {
//                }
            }
        }
        return localUser;
    }

    private ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            logUtil.d_3(TAG, "数据库发生变化,更新用户数据:" + selfChange);
            //song 分进程功能
            MyApplication.ProcessKey processName = MyApplication.getInstance().getProcessKey();
            if (processName.equals(MyApplication.ProcessKey.UI)) {
                //song 主进程
                if (localUser == null) {
                    getProviderLocalUser();
                    logUtil.d_3(TAG, "   =======   重新启动IM进程");
                    //建立与IM的aidl连接
//                    ImAidlManage.getInstance().attemptToBindService();
                } else {
//                    Handler contactHandler = HandlerManage.getInstance().getHandler(HandlerManage.updateType.splashActivity);
//                    if (contactHandler != null)
//                        contactHandler.sendEmptyMessage(MyConstant.IS_LOGIN);
                }
            } else if (processName.equals(MyApplication.ProcessKey.IM)) {
                //song 推送子进程
                getProviderBindUser(true);
            } else if (processName.equals(MyApplication.ProcessKey.browser)) {
                //song 浏览器子进程
                if (localUser == null)
                    getProviderLocalUser();
                getProviderBindUser(true);
            }
        }
    };

    public static UserManage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserManage();
        }
        return INSTANCE;
    }

    public UserManage() {
        time = 0;
        uri = ContentData.UserTableData.URI;
        cr = MyApplication.getAppContext().getContentResolver();
        cr.registerContentObserver(uri, true, contentObserver);
    }

    /**
     * 数据库所有的state初始化为false
     */
    public void init() {
        logUtil.d_3(TAG, "初始化用户数据库");
        ContentValues cv = new ContentValues();
        cv.put(ContentData.UserTableData.STATE, false);
        try {
            synchronized (ContentData.mLockObject) {
                cr.update(uri, cv, ContentData.UserTableData.STATE + "!=?", new String[]{"0"});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置本地登录用户
     */
    public void setCurrentUser(LocalUser user) {
        this.localUser = user;
        ContentValues cv = new ContentValues();
        cv.put(ContentData.UserTableData.USER_ID, "0");
        if (user != null) {
            cv.put(ContentData.UserTableData.INFO, mGson.toJson(user));
            cv.put(ContentData.UserTableData.STATE, true);
            logUtil.d_3(TAG, "本地用户登录");
        } else {
            cv.put(ContentData.UserTableData.STATE, false);
            logUtil.d_3(TAG, "本地用户退出");
        }
        try {
            synchronized (ContentData.mLockObject) {
                int result = cr.update(uri, cv, ContentData.UserTableData.USER_ID + "=?", new String[]{"0"});
                logUtil.d_3(TAG, "更新数据库:本地用户result = " + result);
                if (result == 0) {
                    cr.insert(uri, cv);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getProviderBindUser(boolean isChange) {
        logUtil.d_3(TAG, "从数据库查询绑定用户:start");
        try {
            synchronized (ContentData.mLockObject) {//同步锁
                UserInfoResult tempUser = null;
                if (bindUserinfo == null || isChange) {
                    // 查找id为0的数据,0为本机用户
                    Cursor c = cr.query(uri, null, ContentData.UserTableData.USER_ID + "!=?", new String[]{"0"}, null);
                    //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
                    if (c.moveToFirst()) {
                        do {
                            int state = c.getInt(c.getColumnIndex(ContentData.UserTableData.STATE));
                            if (state == 1) {
                                String info = c.getString(c.getColumnIndex(ContentData.UserTableData.INFO));
                                tempUser = mGson.fromJson(info, UserInfoResult.class);
                                if (tempUser != null) {
                                    logUtil.d_3(TAG, "从数据库获取到绑定用户:" + info);
                                    break;
                                }
                            }
                        } while (c.moveToNext());
                    }
                    c.close();
                    bindUserinfo = tempUser;
                } else {
                    logUtil.d_2(TAG, "已获取绑定用户,直接返回!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getProviderLocalUser() {
        LocalUser tempUser = null;
        try {
            synchronized (ContentData.mLockObject) {//同步锁
                // 查找id为0的数据,0为本机用户
                Cursor c = cr.query(uri, null, ContentData.UserTableData.USER_ID + "=?", new String[]{"0"}, null);
                //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
                if (c.moveToFirst()) {
                    int columnState = c.getColumnIndex(ContentData.UserTableData.STATE);
                    int state = c.getInt(columnState);
                    if (state == 1) {
                        String info = c.getString(c.getColumnIndex(ContentData.UserTableData.INFO));
                        tempUser = mGson.fromJson(info, LocalUser.class);
                    }
                }
                c.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (tempUser != null) {
            logUtil.d_3(TAG, "成功: 数据库获取本地用户"+mGson.toJson(tempUser));
        } else {
            logUtil.d_3(TAG, "失败: 数据库获取本地用户");
        }
        localUser = tempUser;
    }

    public boolean isContain(String userid) {
        try {
            synchronized (ContentData.mLockObject) {//同步锁
                // 查找id为0的数据,0为本机用户
                Cursor c = cr.query(uri, null, ContentData.UserTableData.USER_ID + "=?", new String[]{userid}, null);
                if (c.moveToFirst()) {
                    c.close();
                    return true;
                }
                c.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void remove(String userid) {
        try {
            synchronized (ContentData.mLockObject) {//同步锁
                // 查找id为0的数据,0为本机用户
                cr.delete(uri, ContentData.UserTableData.USER_ID + "=?", new String[]{userid});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onDesty() {
        cr.unregisterContentObserver(contentObserver);
    }
}
