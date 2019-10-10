package c.liyueyun.mjmall.tv.tv.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import c.liyueyun.mjmall.tv.base.ContentProvider.ContentData;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.httpApi.api.ApiTemplate;
import c.liyueyun.mjmall.tv.base.manage.PrefManage;


/**
 * @author caicai
 * @create 2019/4/4
 * @Describe
 */
public class FamilyGroupUpDateTimeManage {

    private static String TAG = "FamilyGroupUpDateTimeManage";
    private static FamilyGroupUpDateTimeManage INSTANCE;
    private final ApiTemplate mApi;
    private final PrefManage prefManage;
    private final ContentResolver cr;
    private final Uri updatTsUri;
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler myHandler;


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }

    public static FamilyGroupUpDateTimeManage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FamilyGroupUpDateTimeManage();
        }
        return INSTANCE;
    }

    private FamilyGroupUpDateTimeManage() {
        mApi = MyApplication.getInstance().getmApi();
        prefManage = MyApplication.getInstance().getPrefManage();
        updatTsUri = ContentData.FamilyGroupUpDateTsTableData.URI;
        cr = MyApplication.getAppContext().getContentResolver();
        HandlerThread thread = new HandlerThread("mall.tv", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        myHandler = new ServiceHandler(mServiceLooper);

    }


    public void saveUpdateTime(String sessionId, String msgTimeStamp, String sessionTimeStamp) {
        try {
            if (Tool.isEmpty(sessionId)) {
                logUtil.d_2(TAG, "存储的时间戳无效");
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put(ContentData.FamilyGroupUpDateTsTableData.SESSION_ID, sessionId);
            if (msgTimeStamp != null)
                cv.put(ContentData.FamilyGroupUpDateTsTableData.MSG_TIME_STAMP, msgTimeStamp);
            if (sessionTimeStamp != null)
                cv.put(ContentData.FamilyGroupUpDateTsTableData.SESSION_TIME_STAMP, sessionTimeStamp);
            synchronized (ContentData.mLockObject) {
                Cursor cursor = cr.query(updatTsUri, null, ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?", new String[]{sessionId}, null);
                if (cursor.moveToFirst()) {
                    logUtil.d_3(TAG, "保存时间戳,更新数据库");
                    cr.update(updatTsUri, cv, ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?", new String[]{sessionId});
                } else {
                    logUtil.d_3(TAG, "增加时间戳数据到数据库");
                    cr.insert(updatTsUri, cv);
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logUtil.d_3(TAG, "数据库内存储异常");
        }
    }

    public String[] getUpdateTimeStamp(String sessionId) {
        String[] time = null;
        try {
            synchronized (ContentData.mLockObject) {//同步锁
                // 会议ID查找数据库 DESC降序     ASC升序
                Cursor cursor = cr.query(updatTsUri, null,
                        ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?",
                        new String[]{sessionId}
                        , null);
                if (cursor.moveToFirst()) {
                    time = new String[2];
                    do {
                        time[0] = cursor.getString(cursor.getColumnIndex(ContentData.FamilyGroupUpDateTsTableData.MSG_TIME_STAMP));
                        time[1] = cursor.getString(cursor.getColumnIndex(ContentData.FamilyGroupUpDateTsTableData.SESSION_TIME_STAMP));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return time;
    }


    public void saveHasShowMsgCount(String sessionId, int showMsgCount) {
        try {
            if (Tool.isEmpty(sessionId)) {
                logUtil.d_2(TAG, "存储的时间戳无效");
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put(ContentData.FamilyGroupUpDateTsTableData.SESSION_ID, sessionId);
            cv.put(ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_MSG_COUNT, showMsgCount);
            synchronized (ContentData.mLockObject) {
                Cursor cursor = cr.query(updatTsUri, null, ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?", new String[]{sessionId}, null);
                if (cursor.moveToFirst()) {
                    logUtil.d_3(TAG, "保存时间戳,更新数据库");
                    cr.update(updatTsUri, cv, ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?", new String[]{sessionId});
                } else {
                    logUtil.d_3(TAG, "增加时间戳数据到数据库");
                    cr.insert(updatTsUri, cv);
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logUtil.d_3(TAG, "数据库内存储异常");
        }
    }

    public int getHasShowMsgCount(String sessionId) {
        int count = 0;
        try {
            synchronized (ContentData.mLockObject) {//同步锁
                // 会议ID查找数据库 DESC降序     ASC升序
                Cursor cursor = cr.query(updatTsUri, null,
                        ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?",
                        new String[]{sessionId}
                        , null);
                if (cursor.moveToFirst()) {
                    do {
                        count = cursor.getInt(cursor.getColumnIndex(ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_MSG_COUNT));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }


    public void saveCheckUpdateTime(String day, String update) {
        try {
            if (Tool.isEmpty(day)) {
                logUtil.d_2(TAG, "存储的时间戳无效");
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put(ContentData.FamilyGroupUpDateTsTableData.SAVE_CURRENT_DAY, day);
            cv.put(ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_UPDATE_DIALOG, update);
            logUtil.d_2(TAG, "存day=" + day + "----update=" + update);
            synchronized (ContentData.mLockObject) {
                Cursor cursor = cr.query(updatTsUri, null, ContentData.FamilyGroupUpDateTsTableData.SAVE_CURRENT_DAY + "=?", new String[]{day}, null);
                if (cursor.moveToFirst()) {
                    logUtil.d_3(TAG, "保存检查更新day,更新数据库");
                    cr.update(updatTsUri, cv, ContentData.FamilyGroupUpDateTsTableData.SAVE_CURRENT_DAY + "=?", new String[]{day});
                } else {
                    logUtil.d_3(TAG, "增加检查更新day数据到数据库");
                    cr.insert(updatTsUri, cv);
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logUtil.d_3(TAG, "数据库内存储异常");
        }
    }

    public String getToDayHasCheckUpdate(String day) {
        String hasCheckUpdate = null;
        try {

            synchronized (ContentData.mLockObject) {//同步锁
                // 会议ID查找数据库 DESC降序     ASC升序
                Cursor cursor = cr.query(updatTsUri, null,
                        ContentData.FamilyGroupUpDateTsTableData.SAVE_CURRENT_DAY + "=?",
                        new String[]{day}
                        , null);
                if (cursor.moveToFirst()) {
                    do {
                        logUtil.d_2(TAG, "day=" + day + "--取boolean=" + cursor.getString(cursor.getColumnIndex(ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_UPDATE_DIALOG)));
                        hasCheckUpdate = cursor.getString(cursor.getColumnIndex(ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_UPDATE_DIALOG));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hasCheckUpdate;

    }


    /**
     * 清楚表数据
     */
    public void delectAllGroupStampData() {
        cr.delete(updatTsUri, null, null);
    }

    /**
     * 清楚表数据
     */
    public void delectGroupStampData(String sessionId) {
        logUtil.d_2(TAG, "删除这个群的时间戳等" + sessionId);
        cr.delete(updatTsUri, ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + "=?", new String[]{sessionId});
    }
}
