package c.liyueyun.mjmall.tv.base.base;

/**
 * Created by SongJie on 04/19 0019.
 */
public class MyConstant {

    public static final String DataUrl = "http://mjxc.yun2win.com";//家庭版个人数据服务器
    //    public static final String DataUrl = "http://121.40.70.11:80";//家庭版个人数据测试服务器
    public static final String MeetingUrl = "http://mjxcroom.yun2win.com";//家庭版会议室服务器
    public static final String FileUrl = "http://attachment.yun2win.com/v1/attachments/";//文件存储服务器
    public static final String TokenUrl = "http://console.yun2win.com";//token鉴权服务器 oauth/token
    //    public final static String UpdateUrl = "http://update.kowo.tv";//更新APK地址
    public final static String UpdateUrl = "https://update-tb.oss-cn-hangzhou.aliyuncs.com";//更新APK地址
    public final static String compayServerUrl = "http://enterprise.yun2win.com/";//企业服务数据,正式
    //    public final static String compayServerUrl = "http://test.yun2win.com/";//企业服务数据,测试微信图片
//    public final static String compayServerUrl = "http://121.40.70.11:8080/";//测试
    public final static String AvcallStatisUrl = "http://conference.yun2win.com";//统计服务器
    //    public final static String AvcallStatisUrl = "http://121.40.70.11:18082";//测试
    public final static String ToPdfUrl = "http://doc2pi.yun2win.com";//转档服务器
    public final static String SDKDataUrl = "http://api.yun2win.com";//第三方SDK,数据服务器
    //    public final static String ManagerUrl = "http://121.40.70.11:18083";//测试
    public final static String ManagerUrl = "http://tvmanager.yun2win.com";//界面数据管理,后台管理服务器

    public final static String MallUrl = "http://tbm.8843shop.com";//家庭版第三方商城数据服务器

    public static final String SHARED_PREFERENCES_KEY = "liyueyun.mjmalltv.prefs";
    public static final String KEY_JOIN_CONFERENCE = "joinConference";
    public static final String KEY_ADD_CONTACT = "addContact";

    public static final String Token_Prefix = "Bearer ";
    public static final String AGORA_MD5_KEY = "a1wuOo8YOvqXGqk";
    public static final String home_invite_QRcode = "home_invite.jpg";
    public static final String home_upFile_QRcode = "home_upFile.jpg";
    public static final String pay_QRcode = "payQRcode.jpg";
    public static final String mycloud_QRcode = "myCloudQRcode.jpg";

    public static final String folderAllFile = "mjMallTv";
    public static final String folderNameUUID = "mjMallTv/uuid";
    public static final String folderNameLog = "mjMallTv/log";
    public static final String folderNameApk = "mjMallTv/apk";
    public static final String folderNamePdf = "mjMallTv/pdf";
    public static final String folderNameRecord = "mjMallTv/record";
    public static final String folderNameImage = "mjMallTv/image";
    public static final String folderImageCache = "mjMallTv/Cache";
    public static final String folderNameAudio = "mjMallTv/msc";
    public static final String folderAgioaLog = "mjMallTv/alog";
    public static final String folderNameVideo = "mjMallTv/video";
    public static final String folderMscAsr = "mjMallTv/msc/asr";

    public final static int TV_DISCONNECT = 20000;
    public final static int TV_EXCHANGE = 20001;
    public final static int TV_END = 20002;
    public final static int TV_HIDE = 20003;
    //    public final static int TV_CAM_ADD = 20004;
//    public final static int TV_CAM_DEL = 20005;
    public final static int TV_RECONNECT = 20006;
    public final static int TV_INTO_SCRIPT = 20007;//注入脚本js
    public final static int TV_EXCUTE_SCRIPT = 20008;//注入脚本字符串
    public final static int TV_MY_SCRIPT = 20009;//注入脚本字符串
    //    public final static int TV_RESTART_URL = 20009;//打开另一个URL，并关闭当前
//    public final static int TV_SCROO_TO = 20010;//打开另一个URL，并关闭当前
    public static final int CLOSE_ACTIVITY = 20010;
    //画廊图片缩放
    public static final int ZOOM_IMAGE = 20012;
    //    //主界面公告
//    public static final int SHOW_NOTICE = 20013;
    //主界面显示绑定信息
    public static final int SHOW_BIND = 20014;
    //主界面文件小圆点显示
    public static final int SHOW_FILE_ADD = 20015;
    //视频会议界面,移动端控制声音指令
    public static final int AVCALL_CONTRL = 20016;
    //视频播放控制命令
    public static final int VIDEO_CONTRL = 20017;
    //生成首页二维码
    public static final int SHOW_QRCODE_VIEW = 20018;
    //修改主界面背景
    public static final int CHANGE_MAIN_BG = 20019;
    //本地帐号登录后刷新数据
    public static final int REFRESH_DATA = 20020;
    //通话界面显示电视号
    public static final int SHOW_CALL_NUM = 20022;
    //获取更新画笔信息
    public static final int UPDATA_PAINT = 20023;
    //切换到观众
    public static final int ROLE_TO_AUDIENCE = 20024;
    //切换到主播
    public static final int ROLE_TO_BROADCASTER = 20025;
    //更新通话区名字
    public static final int REFRESH_NAME = 20027;
    //通话请求拒绝
    public static final int REFUSE_CALL = 20028;
    //通话请求忙碌
    public static final int BUSY_CALL = 20029;
    //刷新主页logo
    public static final int REFRESH_LOGO = 20031;
    //刷新主页公告
    public static final int REFRESH_NOTICE = 20032;
    //刷新企业服务
    public static final int REFRESH_COMPANY_SERVICE = 20033;
    //视频中文件分享指令
    public static final int AVCALL_FILE_OPEN = 20034;
    public static final int AVCALL_FILE_PAGE = 20035;
    public static final int AVCALL_FILE_CLOSE = 20036;
    public static final int MSG_RECOGNIZE_TO_CALL = 20037;
    //打开摄像头失败
    public static final int OPEN_CAMERA_ERROR = 20038;

    //视频中文件分享指令
    public static final int AVCALL_FILE = 20039;
    //同步会议内部指令
    public static final int SYS_CONFERENCE_MEMBER = 20041;
    public static final int SYS_CONFERENCE_ROOM = 20042;
    public static final int SYS_CONFERENCE_FILE = 20043;
    //判断是否登录
    public static final int IS_LOGIN = 20044;
    //刷新企业名称
    public static final int REFRESH_COMPANE_NAME = 20045;
    //本地帐号登录后刷新数据
    public static final int REFRESH_BUSINESS_USER = 20046;
    //本地帐号登录后刷新数据
    public static final int REFRESH_BUSINESS_CLUSCOUNT = 20047;
    //本地帐号登录后刷新数据
    public static final int REFRESH_BUSINESS_CLUSMEMBER = 20048;
    //本地帐号登录后刷新数据
    public static final int REFRESH_BUSINESS_CLUSMESSAGE = 20049;
    //退出企业
    public static final int LEAVE_COMPANY = 20050;
    //刷新圈子名片
    public static final int REFRESH_BUSINESS_CLUSCARD = 20051;
    //本地帐号登录后刷新数据
    public static final int REFRESH_BUSINESS_CLUSPICTURE = 20052;

    //购买会议室支付回调
    public static final int REFRESH_BUSINESS_PAY = 20053;

    //活动中心会议室刷新
    public static final int REFRESH_BUSINESS_AVTIVITY_CENTER = 20054;
    //免费领取新会议室
    public static final int REFRESH_GET_NEW_CONFERENCE = 20055;
    //免费领取新会议室
    public static final int NOTICE_ADD_NEW_FILE = 20056;
    public static final int AVCALL_CONTROL_MIC = 20057;
    public static final int AVCALL_CONTROL_CAMERA = 20058;
    public static final int AVCALL_CONTROL_KICK = 20059;
    public static final int AVCALL_CHANGE_HOSE = 20060;
    public static final int AVCALL_MUTE_ROOM = 20061;
    public static final int CHANGE_ROLE_SUCCESS = 20062;
    public static final int AVCALL_CHAT_MESSAGE = 20063;
    public static final int AVCALL_ALL_FOCUS = 20064;
    public static final int AVCALL_AUDIENCE_STATE_CHANGE = 20065;
    public static final int AVCALL_AUDUENCE_SPEAKERS = 20066;
    public static final int AVCALL_CLOSE_ROOM = 20067;
    //刷新企业圈活动详情二维码显示
    public static final int REFRESH_QRCODE_SHOW = 20064;
    //主页加入群
    public static final int HOME_REFRESH_GROUP = 20066;
    //主页加入某个群，刷新群消息
    public static final int HOME_REFRESH_GROUP_MSG = 20067;
    //刷新家庭相册界面
    public static final int FAMILY_ALBUM_REFRESH = 20068;
    //刷新相册界面
    public static final int ALBUM_REFRESH = 20069;
    //刷新所有图片界面
    public static final int ALL_PHOTOS_REFRESH = 20070;
    //图片浏览界面刷新
    public static final int PHOTO_GRALLY_REFRESH = 20071;
    //图片浏览退出（该相册被删除）
    public static final int PHOTO_GRALLY_EXIT = 20072;
    //我的家切换家庭后，刷新数据
    public static final int REFRESH_MY_FAMILY_AFTER_CHANGE = 20073;
    //扫码加入群成功
    public static final int BE_INVITED_SUCCESS = 20074;
    //切换群更换二维码
    public static final int HOME_LOAD_UP_FILE_ERM = 20075;
    //刷新维度消息
    public static final int HOME_REFRESH_MSG_COUNT = 20076;

    public static final int AV_CALL_NO_JOIN_FAMILY = 20077;
    //通知同步群消息
    public static final int SYS_ONE_GROUP_DATA = 20078;
    //通知同步群数量
    public static final int SYS_GROUP_COUNT_DATA = 20079;


}
