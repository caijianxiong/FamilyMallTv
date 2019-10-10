package c.liyueyun.mjmall.tv.base.ContentProvider;

/**
 * Created by SongJie on 05/25 0025.
 * 提供ContentProvider对外的各种常量，当外部数据需要访问的时候，就可以参考这些常量操作数据。
 */

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContentData {
    public static final Object mLockObject = new Object();
    public static final String AUTHORITY = "c.liyueyun.mjmall.tv.ContentProvider";
    public static final String DATABASE_NAME = "user.db";
    //创建 数据库的时候，都必须加上版本信息,并且必须大于4
    public static final int DATABASE_VERSION = 10;

    // 用户表的 自定义匹配码
    public static final int USER_LIST = 1;
    public static final int USER_ITEM = 2;
    public static final int FILE_LIST = 3;
    public static final int FILE_ITEM = 4;
    public static final int CALL_CANTACTS_LIST = 7;
    public static final int CALL_CANTACTS_ITEM = 8;
    public static final int CONFERENCE_LIST = 9;
    public static final int CONFERENCE_ITEM = 10;
    public static final int FAMILY_GROUP_LIST = 11;
    public static final int FAMILY_GROUP_ITEM = 12;
    public static final int GROUP_SESSION_LIST = 13;
    public static final int GROUP_SESSION_ITEM = 14;
    public static final int GROUP_MEMBER_LIST = 15;
    public static final int GROUP_MEMBER_ITEN = 16;
    public static final int GROUP_MESSAGE_LIST = 17;
    public static final int GROUP_MESSAGE_ITEM = 18;
    public static final int FAMILY_ALL_PHOTO_LIST = 19;
    public static final int FAMILY_ALL_PHOTO_ITEM = 20;
    public static final int FAMILY_ALL_ALBUM_LIST = 21;
    public static final int FAMILY_ALL_ALBUM_ITEM = 22;
    public static final int FAMILY_GROUP_UPDATE_TIME_LIST = 23;
    public static final int FAMILY_GROUP_UPDATE_TIME_ITEM = 24;


    public static UriMatcher uriMatcher;

    static {
        // 常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //匹配用户表格
        uriMatcher.addURI(ContentData.AUTHORITY, UserTableData.TABLE_NAME, USER_LIST);
        uriMatcher.addURI(ContentData.AUTHORITY, UserTableData.TABLE_NAME + "/#", USER_ITEM);
        //匹配每个群的更新时间
        uriMatcher.addURI(ContentData.AUTHORITY, FamilyGroupUpDateTsTableData.TABLE_NAME, FAMILY_GROUP_UPDATE_TIME_LIST);
        uriMatcher.addURI(ContentData.AUTHORITY, FamilyGroupUpDateTsTableData.TABLE_NAME + "/#", FAMILY_GROUP_UPDATE_TIME_ITEM);

    }

    /*song 本地登录帐号和绑定者帐号,用于多进程间的通信*/
    public static final class UserTableData implements BaseColumns {
        public static final String TABLE_NAME = "user";
        //Uri，外部程序需要访问就是通过这个Uri访问的，这个Uri必须的唯一的。
        public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY;
        public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/" + AUTHORITY;

        public static final String USER_ID = "userId";//ID为0的为本机用户
        public static final String STATE = "state";//状态,true为登录或者绑定
        public static final String INFO = "info";
    }



    /*保存每一个群更新的时间戳*/
    public static final class FamilyGroupUpDateTsTableData implements BaseColumns {
        public static final String TABLE_NAME = "mallupdatets";
        //Uri，外部程序需要访问就是通过这个Uri访问的，这个Uri必须的唯一的。
        public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY;
        public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/" + AUTHORITY;

        public static final String SESSION_ID = "sessionId";//群ID
        public static final String MSG_TIME_STAMP = "msgTimeStamp";//消息更新时间
        public static final String SESSION_TIME_STAMP = "sessionTimeStamp";//消息更新时间
        public static final String HAS_SHOW_MSG_COUNT = "hasShowMsgCount";//已读图片数
        public static final String SAVE_CURRENT_DAY = "saveCurrentDay";//保存日期
        public static final String HAS_SHOW_UPDATE_DIALOG = "hasShowUpdate";//记录当天是否弹更新窗口没
    }



}