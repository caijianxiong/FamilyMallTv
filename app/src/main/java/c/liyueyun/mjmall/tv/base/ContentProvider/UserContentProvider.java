package c.liyueyun.mjmall.tv.base.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by SongJie on 05/25 0025.
 * 这个类给外部程序提供访问内部数据的一个接口
 */
public class UserContentProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper = null;
    private static SQLiteDatabase dbW;
    // UriMatcher类用来匹配Uri，使用match()方法匹配路径时返回匹配码

    /**
     * 是一个回调函数，在ContentProvider创建的时候，就会运行,第二个参数为指定数据库名称，如果不指定，就会找不到数据库；
     * 如果数据库存在的情况下是不会再创建一个数据库的。（当然首次调用 在这里也不会生成数据库必须调用SQLiteDatabase的 getWritableDatabase,getReadableDatabase两个方法中的一个才会创建数据库）
     */
    @Override
    public boolean onCreate() {
        //这里会调用 DBOpenHelper的构造函数创建一个数据库
        dbOpenHelper = new DBOpenHelper(this.getContext(), ContentData.DATABASE_NAME, ContentData.DATABASE_VERSION);
        //获得一个可写的数据库引用，如果数据库不存在，则根据onCreate的方法里创建
        dbW = dbOpenHelper.getWritableDatabase();
        return true;
    }
    /**
     * 当执行这个方法的时候，如果没有数据库，他会创建，同时也会创建表，但是如果没有表，下面在执行insert的时候就会出错
     * 这里的插入数据也完全可以用sql语句书写，然后调用 db.execSQL(sql)执行。
     */
    @Override
    public Uri insert(Uri uri, ContentValues values){
        Uri result ;
        long id ;
        int matchCode = ContentData.uriMatcher.match(uri);
        id = dbW.insert(getTableName(matchCode), null, values);    // 返回的是记录的行号，主键为int，实际上就是主键值
        if(matchCode%2 == 1){
            result = ContentUris.withAppendedId(uri, id);
        }else{
            String path = uri.toString();
            result = Uri.parse(path.substring(0, path.lastIndexOf("/"))+id); // 替换掉id
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String where ;
        int matchCode = ContentData.uriMatcher.match(uri);
        if(matchCode%2 == 1){
            where = selection;
        }else{
            where = "_ID=" + ContentUris.parseId(uri);    // 删除指定id的记录
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";    // 把其它条件附加上
        }
        int count = dbW.delete(getTableName(matchCode), where, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String where;
        int matchCode = ContentData.uriMatcher.match(uri);
        if(matchCode%2 == 1){
            where = selection;
        }else{
            where = "_ID=" + ContentUris.parseId(uri);// 获取指定id的记录
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
        }
        int count = dbW.update(getTableName(matchCode), values, where, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        String where;
        int matchCode = ContentData.uriMatcher.match(uri);
        if(matchCode%2 == 1){
            where = selection;
        }else{
            where = "_ID=" + ContentUris.parseId(uri);// 获取指定id的记录
            where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
        }
        SQLiteDatabase dbR = dbOpenHelper.getReadableDatabase();
        cursor = dbR.query(getTableName(matchCode), projection, where, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (ContentData.uriMatcher.match(uri)) {
            case ContentData.USER_LIST:
                return ContentData.UserTableData.CONTENT_TYPE;
            case ContentData.USER_ITEM:
                return ContentData.UserTableData.CONTENT_TYPE_ITME;
                //每个群更新时间
            case ContentData.FAMILY_GROUP_UPDATE_TIME_LIST:
                return ContentData.FamilyGroupUpDateTsTableData.CONTENT_TYPE;
            case ContentData.FAMILY_GROUP_UPDATE_TIME_ITEM:
                return ContentData.FamilyGroupUpDateTsTableData.CONTENT_TYPE_ITME;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * 通过匹配码获取表格名字
     */
    private String getTableName(int matchCode){
        switch (matchCode) {
            case ContentData.USER_LIST:
            case ContentData.USER_ITEM:
                return ContentData.UserTableData.TABLE_NAME;
            case ContentData.FILE_LIST:

            case ContentData.FAMILY_GROUP_UPDATE_TIME_ITEM:
            case ContentData.FAMILY_GROUP_UPDATE_TIME_LIST:
                return ContentData.FamilyGroupUpDateTsTableData.TABLE_NAME;
        }
        return null;
    }

    public static void close(){
        if(dbW != null)
            dbW.close();
    }
}