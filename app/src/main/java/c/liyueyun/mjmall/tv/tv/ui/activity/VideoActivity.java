package c.liyueyun.mjmall.tv.tv.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.tendcloud.tenddata.TCAgent;
import java.util.Formatter;
import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.httpApi.response.UIMangerResult;
import c.liyueyun.mjmall.tv.base.task.CreateQRcodeAysncTask;
import c.liyueyun.mjmall.tv.base.widget.MyProgressDialog;
import c.liyueyun.mjmall.tv.tv.manager.HandlerManage;
import c.liyueyun.mjmall.tv.tv.manager.ImageHandler;
import c.liyueyun.mjmall.tv.tv.ui.widget.ELiveTextureView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class VideoActivity extends Activity implements View.OnClickListener,View.OnTouchListener{
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext = this;
    private MyProgressDialog ProDialog;
    private String videoUrl;
    private String imgSrc;
    private String audioUrl;
    private String name;
    private boolean alwaysShow = false;
    private Animation cdPlayAnimation;
    private long byteCount ;
    private Gson mGson = MyApplication.getInstance().getmGson();
    private AudioManager audioManager;
    private UIMangerResult.Emall.EmallPage.EmallPageItem pageItem;
    private String videoDur;
    private float startX,startY,offSetX,offSetY, moveOffsetLimit = 10;
    private int seek,dur;

    private TextView tv_video_pos,tv_video_dur;
    private ProgressBar sbar_video;
    private ELiveTextureView eLiveTextureView;
    private RelativeLayout rlay_video_bar;
    private ImageView iv_video_img;
    private RelativeLayout rlay_video_emallbar1,rlay_video_emallbar2,rlay_video_emalltime;
    private TextView tv_video_emalltime;
    private TextView tv_video_name;
    private RelativeLayout rlay_popvideo_main;
    private ImageView iv_popvideo_cd;
    private ImageView iv_video_emalllinksrc,iv_video_emallqrcode;
    private TextView tv_video_emallqrcode1,tv_video_emallqrcode2,tv_video_emallaction1,tv_video_emallaction2;
    private Button btn_video_emallpay,btn_video_emallcall,btn_video_emallplay,btn_video_emallreplay;

    private String showType;
    private static final String VIDEO = "video";
    private static final String IMAGE = "image";
    private static final String AUDIO = "audio";

    private static int addTime = 0;
    private static final int hideBarTime = 5000;
    private static int seekTime = 100;//每次seek的默认递增时间

    private static final int UPDATA_BAR_POS = 10001;
    private static final int SHOW_BAR = 10002;
    private static final int HIDE_BAR = 10003;
    private static final int SHOW_PROGRESS = 10004;
    private static final int HIDE_PROGRESS = 10005;
    private static final int SHOW_NET_SPEED = 10007;
    private boolean isActivityVisiable; //true表示当前可见，false表示跳转到了客服

    private Handler myHandler= new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_BAR_POS:
                    int pos = eLiveTextureView.getCurrentPosition();
                    sbar_video.setProgress(pos);
                    tv_video_pos.setText(stringForTimeAuto(pos / 1000));
                    myHandler.sendEmptyMessageDelayed(UPDATA_BAR_POS, 1000);
                    if(pageItem!=null){
                        tv_video_emalltime.setText(stringForTimeAuto(pos / 1000)+"    /    "+videoDur);
                    }
                    break;
                case SHOW_BAR:
                    if(pageItem == null) {
                        rlay_video_bar.setVisibility(View.VISIBLE);
                    }
                    break;
                case HIDE_BAR:
                    if (!alwaysShow) {
                        rlay_video_bar.setVisibility(View.INVISIBLE);
                    }
                    break;
                case SHOW_PROGRESS:
                    if (ProDialog == null) {
                        MyProgressDialog.Builder builder = new MyProgressDialog.Builder();
                        ProDialog = builder.CreateDialog(mContext);
                        ProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {//back键取消监听
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        });
                    }
                    ProDialog.show();
                    break;
                case HIDE_PROGRESS:
                    if (ProDialog != null) {
                        ProDialog.cleanAnim();
                        ProDialog.dismiss();
                        ProDialog = null;
                    }
                    if (eLiveTextureView != null) {
                        eLiveTextureView.setBackground(null);
                    }
                    byteCount = 0;
                    myHandler.removeMessages(SHOW_NET_SPEED);
                    break;
                case MyConstant.CLOSE_ACTIVITY:
                    finish();
                    break;
                case SHOW_NET_SPEED:
                    showNetSpeed();
                    myHandler.sendEmptyMessageDelayed(SHOW_NET_SPEED, 1000);
                    break;
                case MyConstant.VIDEO_CONTRL:
//                    TvMessageForIm tvMsg = mGson.fromJson((String) msg.obj, TvMessageForIm.class);
//                    String url = tvMsg.getUrl();
//                    String type = tvMsg.getType();
//                    if (!Tool.isEmpty(url) && !Tool.isEmpty(videoUrl) && url.equals(videoUrl) && !Tool.isEmpty(type)) {
//                        if (type.equals("play")) {
//                            if (!eLiveTextureView.isPlaying()) {
//                                eLiveTextureView.start();
//                                myHandler.sendEmptyMessageDelayed(HIDE_BAR, hideBarTime);
//                                iv_video_img.setVisibility(View.GONE);
//                            }
//                        } else if (type.equals("pause")) {
//                            if (eLiveTextureView.isPlaying()) {
//                                eLiveTextureView.pause();
//                                myHandler.removeMessages(HIDE_BAR);
//                                myHandler.sendEmptyMessage(SHOW_BAR);
//                                iv_video_img.setVisibility(View.VISIBLE);
//                            }
//                        } else if (type.equals("end") || type.equals("exit")) {
//                            finish();
//                        }
//                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_video);

        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        cdPlayAnimation = AnimationUtils.loadAnimation(mContext, R.anim.audio_animation);
        HandlerManage.getInstance().addHandler(HandlerManage.updateType.videoActivity,myHandler);
        eLiveTextureView = (ELiveTextureView)findViewById(R.id.elive_popvideo);
        rlay_popvideo_main = (RelativeLayout) findViewById(R.id.rlay_popvideo_main);
        rlay_video_bar = (RelativeLayout) findViewById(R.id.rlay_video_bar);
        tv_video_pos = (TextView)findViewById(R.id.tv_video_pos);
        tv_video_dur = (TextView)findViewById(R.id.tv_video_dur);
        sbar_video = (ProgressBar)findViewById(R.id.video_mybar);
        iv_video_img = (ImageView) findViewById(R.id.iv_video_img);
        tv_video_name = (TextView)findViewById(R.id.tv_video_name);
        iv_popvideo_cd = (ImageView) findViewById(R.id.iv_popvideo_cd);
        rlay_video_emallbar1 = (RelativeLayout) findViewById(R.id.rlay_video_emallbar1);
        rlay_video_emallbar2 = (RelativeLayout) findViewById(R.id.rlay_video_emallbar2);
        rlay_video_emalltime = (RelativeLayout) findViewById(R.id.rlay_video_emalltime);
        tv_video_emalltime = (TextView) findViewById(R.id.tv_video_emalltime);

        iv_video_emalllinksrc = (ImageView)findViewById(R.id.iv_video_emalllinksrc);
        iv_video_emallqrcode = (ImageView)findViewById(R.id.iv_video_emallqrcode);
        tv_video_emallqrcode1 = (TextView)findViewById(R.id.tv_video_emallqrcode1);
        tv_video_emallqrcode2 = (TextView)findViewById(R.id.tv_video_emallqrcode2);
        tv_video_emallaction1 = (TextView)findViewById(R.id.tv_video_emallaction1);
        tv_video_emallaction2 = (TextView)findViewById(R.id.tv_video_emallaction2);
        btn_video_emallpay = (Button)findViewById(R.id.btn_video_emallpay);
        btn_video_emallcall = (Button)findViewById(R.id.btn_video_emallcall);
        btn_video_emallplay = (Button)findViewById(R.id.btn_video_emallplay);
        btn_video_emallreplay = (Button)findViewById(R.id.btn_video_emallreplay);

        eLiveTextureView.setOnClickListener(this);
        eLiveTextureView.setOnTouchListener(this);
        btn_video_emallpay.setOnClickListener(this);
        btn_video_emallplay.setOnClickListener(this);
        btn_video_emallreplay.setOnClickListener(this);
        btn_video_emallcall.setOnClickListener(this);

        eLiveTextureView.setStartListener(startListener);
        eLiveTextureView.setCompletionListener(mCompletionListener);
        eLiveTextureView.setOnInfoListener(mInfoListener);

        int resultAudio = audioManager.requestAudioFocus(afChangeListener,
                                        // Use the music stream.
                                        AudioManager.STREAM_MUSIC,
                                        // Request permanent focus.
                                        AudioManager.AUDIOFOCUS_GAIN);
        //加载ijkplayer播放器
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            Toast.makeText(MyApplication.getAppContext(),"初始化播放器失败!",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (resultAudio == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // 开始播放音乐文件
            getIntentData(getIntent());
        }else{
            Toast.makeText(MyApplication.getAppContext(),"初始化音频组件失败!",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //关闭互斥activity
//        BrowserCallback.getInstance().exit();
        Handler handlerSlide = HandlerManage.getInstance().getHandler(HandlerManage.updateType.slideActivity);
        if (handlerSlide != null) {
            handlerSlide.sendEmptyMessage(MyConstant.CLOSE_ACTIVITY);
        }

        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlay_video_emallbar2.getVisibility() == View.VISIBLE) {
                    rlay_video_emallbar1.setVisibility(View.VISIBLE);
                    rlay_video_emallbar2.setVisibility(View.GONE);
                    if (!eLiveTextureView.isPlaying()) {
                        eLiveTextureView.start();
                    }
                }else{
                    //发送退出消息
                    if(Tool.isEmpty(audioUrl)){
//                        UserInfoResult bindUserInfo = UserManage.getInstance().getBindUser();
//                        if(bindUserInfo!= null){
//                            TvMessageForIm tvMessageResponse = new TvMessageForIm();
//                            tvMessageResponse.setAction("videoCtrol");
//                            tvMessageResponse.setType("exit");
//                            PushData pushData = new PushData();
//                            pushData.setTv(tvMessageResponse);
//                            String sendStr = mGson.toJson(pushData);
//                            ImAidlManage.getInstance().sendMessage(bindUserInfo.getId(),sendStr);
//                        }
                    }
                    finish();
                }
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Glide.clear(iv_video_img);
        myHandler.removeMessages(UPDATA_BAR_POS);
        eLiveTextureView.stopPlayback();
        iv_popvideo_cd.clearAnimation();
        iv_popvideo_cd.setVisibility(View.GONE);
        tv_video_name.setText("");
        int resultAudio = audioManager.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
        if (resultAudio == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // 开始播放音乐文件
            getIntentData(intent);
        }else{
            Toast.makeText(MyApplication.getAppContext(),"初始化音频组件失败!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisiable = true;
        TCAgent.onPageStart(this, "播放视频");
    }

    private void showNetSpeed(){
        long byteSize = TrafficStats.getUidRxBytes(android.os.Process.myPid())==TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalRxBytes()/1024);
        if(byteCount != 0 && ProDialog!=null){
            ProDialog.tv_mydialog_text.setText("加载速度:"+(byteSize - byteCount)+"KB/S");
        }
        byteCount = byteSize;
    }
    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, "播放视频");
        isActivityVisiable = false;
        eLiveTextureView.stopPlayback();
        myHandler.removeMessages(HIDE_BAR);
        myHandler.sendEmptyMessage(SHOW_BAR);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        logUtil.d_2(TAG,event.getAction()+"::::"+event.getKeyCode());
        if (rlay_video_emallbar2.getVisibility() == View.VISIBLE) {
            return super.dispatchKeyEvent(event);
        }
        if(event.getAction() == KeyEvent.ACTION_DOWN ){//按下
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    onClickEnter();
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if(eLiveTextureView.isPlaying()) {
                        if (tv_video_name.getVisibility() != View.VISIBLE) {
                            tv_video_name.setVisibility(View.VISIBLE);
                        }
                        if (addTime == 0) {
                            addTime = eLiveTextureView.getCurrentPosition();
                            dur = (int) eLiveTextureView.getDuration();
                        }
                        addTime = addTime - seekTime;
                        addTime = addTime>0?addTime:-1;
                        tv_video_name.setText( stringForTimeAuto(addTime / 1000) + "/" + videoDur);
                        myHandler.sendEmptyMessage(SHOW_BAR);
                        myHandler.removeMessages(HIDE_BAR);
                    }
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if(eLiveTextureView.isPlaying()) {
                        if (tv_video_name.getVisibility() != View.VISIBLE) {
                            tv_video_name.setVisibility(View.VISIBLE);
                        }
                        if (addTime == 0) {
                            addTime = eLiveTextureView.getCurrentPosition();
                            dur = (int) eLiveTextureView.getDuration();
                        }
                        addTime = addTime + seekTime;
                        addTime = addTime>dur?dur:addTime;
                        tv_video_name.setText( stringForTimeAuto(addTime / 1000) + "/" + videoDur);
                        myHandler.sendEmptyMessage(SHOW_BAR);
                        myHandler.removeMessages(HIDE_BAR);
                    }
                    return true;
            }
        }else if(event.getAction() == KeyEvent.ACTION_UP){//弹起
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if(showType.equals(AUDIO) || showType.equals(VIDEO)) {
                        if(addTime != 0) {
                            if(Math.abs(addTime - eLiveTextureView.getCurrentPosition())> seekTime * 2) {
                                seekToPaly(addTime);
                                addTime = 0;
                            }
                        }
                        myHandler.sendEmptyMessageDelayed(HIDE_BAR, hideBarTime);
                        if (tv_video_name.getVisibility() == View.VISIBLE) {
                            tv_video_name.setVisibility(View.GONE);
                        }
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logUtil.d_3(TAG, " onDestroy");
        Handler handler = HandlerManage.getInstance().getHandler(HandlerManage.updateType.videoActivity);
        if(handler != null && myHandler.equals(handler)){
            HandlerManage.getInstance().removeHandler(HandlerManage.updateType.videoActivity);
        }
        iv_popvideo_cd.clearAnimation();
        if(ProDialog != null) {
            ProDialog.cleanAnim();
            ProDialog.dismiss();
            ProDialog = null;
        }
        myHandler.removeMessages(UPDATA_BAR_POS);
        audioManager.abandonAudioFocus(afChangeListener);
    }

    @Override
    public void onBackPressed() {
        if (rlay_video_emallbar2.getVisibility() == View.VISIBLE) {
            rlay_video_emallbar1.setVisibility(View.VISIBLE);
            rlay_video_emallbar2.setVisibility(View.GONE);
            if (!eLiveTextureView.isPlaying()) {
                eLiveTextureView.start();
            }
        }else{
            //发送退出消息
            if(Tool.isEmpty(audioUrl)){
//                UserInfoResult bindUserInfo = UserManage.getInstance().getBindUser();
//                if(bindUserInfo!= null){
//                    TvMessageForIm tvMessageResponse = new TvMessageForIm();
//                    tvMessageResponse.setAction("videoCtrol");
//                    tvMessageResponse.setType("exit");
//                    PushData pushData = new PushData();
//                    pushData.setTv(tvMessageResponse);
//                    String sendStr = mGson.toJson(pushData);
//                    ImAidlManage.getInstance().sendMessage(bindUserInfo.getId(),sendStr);
//                }
            }
            finish();
        }
    }

    /**
     * 初始化数据
     */
    private void getIntentData(Intent intent){
        byteCount = 0;
        videoUrl = intent.getStringExtra("url");
        imgSrc = intent.getStringExtra("imgSrc");
        audioUrl = intent.getStringExtra("audioUrl");
        name = intent.getStringExtra("name");
        alwaysShow = intent.getBooleanExtra("alwaysShow",false);
        String emallData = intent.getStringExtra("emallItem");
        if(!Tool.isEmpty(emallData)) {
            pageItem = MyApplication.getInstance().getmGson().fromJson(emallData, UIMangerResult.Emall.EmallPage.EmallPageItem.class);
            if(pageItem!=null && pageItem.isEnableLinkAction()){
                if(!pageItem.isEnableCall()) {
                    btn_video_emallcall.setVisibility(View.GONE);
                }
                rlay_video_emallbar1.setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.tv_video_eamllname)).setText(pageItem.getTitle1());
                ((TextView)findViewById(R.id.tv_video_eamlprice)).setText(pageItem.getTitle2()+"    |    按\"确定或OK键\"查看或购买");
                //加载图片
                String url = Tool.getYun2winImg(pageItem.getLinkActionSrc());
                String imgurl = url.contains("yun2win.com")? ImageHandler.getThumbnail720P(url):url;
                Glide.with(mContext)
                        .load(imgurl)
                        .dontAnimate()//解决展位图大小影响
                        .placeholder(R.mipmap.slideloading)
                        .error(R.mipmap.slideloading)
                        .into(iv_video_emalllinksrc);
                //二维码
                String actionUrl = pageItem.getLinkActionUrl();
                if(!Tool.isEmpty(actionUrl)) {
                    String qrcodePath = Tool.getSavePath(MyConstant.folderNameImage) + Tool.get32MD5(actionUrl);
                    CreateQRcodeAysncTask qRcodeAysncTask = new CreateQRcodeAysncTask(actionUrl, qrcodePath, new CreateQRcodeAysncTask.OnCreateListener() {
                        @Override
                        public void onError(Throwable t) {
                        }

                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            iv_video_emallqrcode.setImageBitmap(bitmap);
                        }
                    });
                    if (pageItem.getLinkActionScanType().equals("wx")) {
                        qRcodeAysncTask.setLogoId(R.mipmap.logo_wx);
                    } else if (pageItem.getLinkActionScanType().equals("tb")) {
                        qRcodeAysncTask.setLogoId(R.mipmap.logo_tb);
                    } else {
                        qRcodeAysncTask.setLogoId(0);
                    }
                    qRcodeAysncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    findViewById(R.id.rlay_video_emallqrcode).setVisibility(View.GONE);
                }
                //加载信息
                tv_video_emallqrcode1.setText(pageItem.getLinkUrlTitle1());
                tv_video_emallqrcode2.setText(pageItem.getLinkUrlTitle2());
                tv_video_emallaction1.setText(pageItem.getLinkTitle1());
                tv_video_emallaction2.setText(pageItem.getLinkTitle2());
            }
        }
        if(!Tool.isEmpty(videoUrl)) {
            showType = VIDEO;
            //播放视频
            eLiveTextureView.setVisibility(View.VISIBLE);
            iv_video_img.setVisibility(View.GONE);
            myHandler.sendEmptyMessage(SHOW_BAR);
            myHandler.sendEmptyMessage(SHOW_PROGRESS);
            myHandler.sendEmptyMessage(SHOW_NET_SPEED);
            //谈吧视频源改http,https低安卓版本不太支持
//            if(UserManage.getInstance().getBindUser()==null ||
//                    Tool.isEmpty(UserManage.getInstance().getBindUser().getAppName()) ||
//                    UserManage.getInstance().getBindUser().getAppName().equals("谈吧")){
//                eLiveTextureView.setVideoURI(Uri.parse(videoUrl.replace("https","http")));
//            }else{
                eLiveTextureView.setVideoURI(Uri.parse(videoUrl));
//            }
            //加载视频暂停图片
            Glide.with(mContext)
                    .load(R.mipmap.video_pause)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                    .skipMemoryCache( true )//跳过内存缓存
                    .into(iv_video_img);
        }else if(!Tool.isEmpty(imgSrc)){
            showType = IMAGE;
            //显示图片
            eLiveTextureView.setVisibility(View.GONE);
            iv_video_img.setVisibility(View.VISIBLE);
            myHandler.sendEmptyMessage(HIDE_BAR);
            myHandler.sendEmptyMessage(HIDE_PROGRESS);
            Glide.with(mContext)
                    .load(Tool.getYun2winImg(imgSrc))
                    .crossFade()
                    .dontAnimate()//解决展位图大小影响
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.slideloading)
                    .error(R.mipmap.slideloaderror)
                    .into(iv_video_img);
        }else if(!Tool.isEmpty(audioUrl)){
            showType = AUDIO;
            //播放音乐
            rlay_popvideo_main.setBackgroundResource(R.mipmap.video_audio_bg);
            iv_popvideo_cd.setVisibility(View.VISIBLE);
            eLiveTextureView.setVisibility(View.GONE);
            iv_popvideo_cd.startAnimation(cdPlayAnimation);
            myHandler.sendEmptyMessage(SHOW_BAR);
            eLiveTextureView.setVideoURI(Uri.parse(audioUrl));
            if(!Tool.isEmpty(name)){
                tv_video_name.setText(name);
                if(tv_video_name.getVisibility() != View.VISIBLE){
                    tv_video_name.setVisibility(View.VISIBLE);
                }
            }
        }else{
            finish();
        }
    }

    /**
     * 点击确定事件处理
     */
    private void onClickEnter(){
        if(pageItem == null) {
            if(showType != IMAGE) {
                if (eLiveTextureView.isPlaying()) {
                    eLiveTextureView.pause();
                    myHandler.removeMessages(HIDE_BAR);
                    myHandler.sendEmptyMessage(SHOW_BAR);
                    iv_video_img.setVisibility(View.VISIBLE);
                } else {
                    eLiveTextureView.start();
                    myHandler.sendEmptyMessageDelayed(HIDE_BAR, hideBarTime);
                    iv_video_img.setVisibility(View.GONE);
                }
            }
        }else{
            if (eLiveTextureView.isPlaying()) {
                eLiveTextureView.pause();
                if(pageItem.isEnableLinkAction()) {
                    rlay_video_emallbar1.setVisibility(View.GONE);
                    rlay_video_emallbar2.setVisibility(View.VISIBLE);
                    rlay_video_emallbar2.requestFocus();
                    if (showType.equals(IMAGE)) {
                        rlay_video_emalltime.setVisibility(View.GONE);
                    }
                }
            }else{
                eLiveTextureView.start();
                rlay_video_emallbar1.setVisibility(View.VISIBLE);
                rlay_video_emallbar2.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 执行跳转指定位置播放
     */
    private void seekToPaly(int msc){
        int dur = (int) eLiveTextureView.getDuration();
        if (msc < 0) {
            msc = 0;
        } else if (msc > dur) {
            msc = dur;
        }
        eLiveTextureView.seekTo(msc);
        if(!eLiveTextureView.isPlaying()){
            eLiveTextureView.start();
            rlay_video_emallbar1.setVisibility(View.VISIBLE);
            rlay_video_emallbar2.setVisibility(View.GONE);
        }
    }
    private ELiveTextureView.OnStartListener startListener = new ELiveTextureView.OnStartListener() {
        @Override
        public void onStart() {
            if(eLiveTextureView !=null) {
                long dur = eLiveTextureView.getDuration();
                if(sbar_video!=null) {
                    sbar_video.setMax((int) dur);
                }
                if(tv_video_dur!=null) {
                    videoDur = stringForTimeAuto(dur / 1000);
                    tv_video_dur.setText(videoDur);
                }
                seekTime = (int) (dur / 100.0);
                myHandler.removeMessages(UPDATA_BAR_POS);
                myHandler.sendEmptyMessage(UPDATA_BAR_POS);
                myHandler.sendEmptyMessage(HIDE_PROGRESS);
                myHandler.sendEmptyMessage(SHOW_BAR);
                myHandler.sendEmptyMessageDelayed(HIDE_BAR, hideBarTime);
            }
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            logUtil.d_3(TAG, " mCompletionListener onCompletion");
            if(!Tool.isEmpty(audioUrl)){
                finish();
            }else {
                if(pageItem == null || !pageItem.isEnableLinkAction()) {
                    myHandler.removeMessages(HIDE_BAR);
                    myHandler.sendEmptyMessage(SHOW_BAR);
                    iv_video_img.setVisibility(View.VISIBLE);
                }else{
                    rlay_video_emallbar1.setVisibility(View.GONE);
                    rlay_video_emallbar2.setVisibility(View.VISIBLE);
                    rlay_video_emallbar2.requestFocus();
                }
                //发送结束消息
//                UserInfoResult bindUserInfo = UserManage.getInstance().getBindUser();
//                if(bindUserInfo!= null){
//                    TvMessageForIm tvMessageResponse = new TvMessageForIm();
//                    tvMessageResponse.setAction("videoCtrol");
//                    tvMessageResponse.setType("end");
//                    PushData pushData = new PushData();
//                    pushData.setTv(tvMessageResponse);
//                    String sendStr = mGson.toJson(pushData);
//                    ImAidlManage.getInstance().sendMessage(bindUserInfo.getId(),sendStr);
//                }
            }
        }
    };

    private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mediaPlayer, int what, int i1) {
            switch (what){
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    myHandler.sendEmptyMessage(SHOW_PROGRESS);
                    myHandler.sendEmptyMessage(SHOW_NET_SPEED);
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    myHandler.sendEmptyMessage(HIDE_PROGRESS);
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    //这里返回了视频旋转的角度，根据角度旋转视频到正确的画面
                    if (eLiveTextureView != null)
                        eLiveTextureView.setRotation(i1);
            }
            return false;
        }
    };

    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
            // Pause playback
                if(eLiveTextureView !=null){
                    eLiveTextureView.pause();
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume playback
                if(eLiveTextureView !=null && isActivityVisiable){
                    eLiveTextureView.start();
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                audioManager.abandonAudioFocus(afChangeListener);
                // Stop playback
                finish();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_video_emallpay:
//                Intent intentMall = new Intent(VideoActivity.this, MallMoreActivity.class);
//                startActivity(intentMall);
                break;
            case R.id.btn_video_emallplay:
                rlay_video_emallbar1.setVisibility(View.VISIBLE);
                rlay_video_emallbar2.setVisibility(View.GONE);
                if (!eLiveTextureView.isPlaying()) {
                    eLiveTextureView.start();
                }
                break;
            case R.id.btn_video_emallreplay:
                rlay_video_emallbar1.setVisibility(View.VISIBLE);
                rlay_video_emallbar2.setVisibility(View.GONE);
                seekToPaly(0);
                break;
            case R.id.btn_video_emallcall:
//                if (pageItem != null && pageItem.isEnableCall()) {
//                    Intent intentCustomer = new Intent(VideoActivity.this, CustomerServiceActivity.class);
//                    intentCustomer.putExtra("callNumbers", pageItem.getCallNumbers());
//                    intentCustomer.putExtra("contactImageSrc", pageItem.getContactImageSrc());
//                    intentCustomer.putExtra("contactAvatarSrc", pageItem.getContactAvatarSrc());
//                    startActivity(intentCustomer);
//                    isActivityVisiable = false;
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.elive_popvideo:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //记录点下屏幕的位置
                        startX = event.getX();
                        startY = event.getY();
                        seek = eLiveTextureView.getCurrentPosition();
                        dur = (int) eLiveTextureView.getDuration();
                        addTime = 0;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offSetX = event.getX() - startX;
                        offSetY = event.getY() - startY;
                        startX = event.getX();
                        startY = event.getY();
                        logUtil.d_2(TAG, "MotionEvent.ACTION_MOVE"+offSetX);
                        if (Math.abs(offSetX) > Math.abs(offSetY)) {
                            if (tv_video_name.getVisibility() != View.VISIBLE) {
                                tv_video_name.setVisibility(View.VISIBLE);
                            }
                            addTime = addTime + (int) (offSetX * 50);
                            tv_video_name.setText(stringForTimeAuto((seek + addTime) / 1000) + "/" + videoDur);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //记录离开屏幕的位置
                        if (Math.abs(addTime) > 3 * 1000) {
                            seekToPaly(seek + addTime);
                            if (tv_video_name.getVisibility() == View.VISIBLE) {
                                tv_video_name.setVisibility(View.GONE);
                            }
                        }else {
                            onClickEnter();
                        }
                    default:
                        break;
                }
                break;
        }
        return false;
    }

    /**
     * 时间转换为00:00
     */
    public String stringForTimeAuto(long timeS) {
        long seconds = timeS % 60;
        long minutes = (timeS / 60) % 60;
        long hours = timeS / 3600;
        Formatter formatter = new Formatter();
        String time;
        if (hours > 0) {
            time =  formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            time =  formatter.format("%02d:%02d", minutes, seconds).toString();
        }
        formatter.close();
        return time;
    }
}
