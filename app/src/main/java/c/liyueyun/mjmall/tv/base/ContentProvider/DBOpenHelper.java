package c.liyueyun.mjmall.tv.base.ContentProvider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import c.liyueyun.mjmall.tv.base.base.logUtil;

/**
 * Created by SongJie on 05/25 0025.
 * 这个类继承SQLiteOpenHelper抽象类，用于创建数据库和表。创建数据库是调用它的父类构造方法创建。
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    // 在SQLiteOepnHelper的子类当中，必须有该构造函数，用来创建一个数据库；
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        // 必须通过super调用父类当中的构造函数
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    /**
     * 只有当数据库执行创建 的时候，才会执行这个方法。如果更改表名，也不会创建，只有当创建数据库的时候，才会创建改表名之后 的数据表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create table");
        //创建用户表格,保存登入用户和绑定用户
        db.execSQL("create table " + ContentData.UserTableData.TABLE_NAME
                + "(" + ContentData.UserTableData._ID
                + " INTEGER PRIMARY KEY autoincrement,"
                + ContentData.UserTableData.USER_ID + " TEXT,"
                + ContentData.UserTableData.STATE + " INTEGER NOT NULL,"
                + ContentData.UserTableData.INFO + " TEXT);");

        addFamilyGroupUpdateTimeTAble(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 9:
                addFamilyGroupUpdateTimeTAble(db);
                if (newVersion == 10) break;
            default:
                //删除所有表格,并重新创建
                dropDb(db);
                onCreate(db);
                break;
        }
    }

    private void addFamilyGroupUpdateTimeTAble(SQLiteDatabase db) {
        logUtil.d_2("DBHelper","创建表");
        db.execSQL("create table " + ContentData.FamilyGroupUpDateTsTableData.TABLE_NAME
                + "(" + ContentData.FamilyGroupUpDateTsTableData._ID
                + " INTEGER PRIMARY KEY autoincrement,"
                + ContentData.FamilyGroupUpDateTsTableData.SESSION_ID + " TEXT,"
                + ContentData.FamilyGroupUpDateTsTableData.MSG_TIME_STAMP + " TEXT,"
                + ContentData.FamilyGroupUpDateTsTableData.SAVE_CURRENT_DAY + " TEXT,"
                + ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_UPDATE_DIALOG + " TEXT,"
                + ContentData.FamilyGroupUpDateTsTableData.HAS_SHOW_MSG_COUNT + " TEXT,"
                + ContentData.FamilyGroupUpDateTsTableData.SESSION_TIME_STAMP + " TEXT);");
    }


    /**
     * 删除所有表格
     */
    private void dropDb(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                db.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }


}