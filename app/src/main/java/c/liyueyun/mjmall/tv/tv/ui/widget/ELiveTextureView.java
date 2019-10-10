package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.manage.PrefManage;
import c.liyueyun.mjmall.tv.tv.manager.HandlerManage;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class ELiveTextureView extends TextureView {
    private final String TAG = this.getClass().getSimpleName();
	private Context mContext;
	private Uri mUri;
	private AssetFileDescriptor fileDescriptor;
	private IjkMediaPlayer mMediaPlayer = null;
	private boolean mIsPrepared;
	private int mSurfaceWidth,mSurfaceHeight;
	private int mSeekPos;
	private int runState;
	private OnStartListener startListener;
	private boolean isLooping = false;// 是否循环播放

    private static int errorCount;
	private static Object mLockObject = new Object();
	
	private static final int INIT_PLAYER = 10000;
	private static final int CREAT_PLAY_VIDEO = 10001;
	private static final int START_PLAY_VIDEO = 10002;
	private static final int STOP_PLAY_VIDEO = 10003;
	private static final int UPDATA_PLAY_INFO = 10004;
	private Handler myHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what){
				case INIT_PLAYER:
					logUtil.d_3(TAG, " StartThread");
					runState = 2;
					synchronized(mLockObject) {
						if (mMediaPlayer != null) {
							logUtil.d_3(TAG, "StartThread  release player");
							mIsPrepared = false;
							try {
								mMediaPlayer.reset();
								mMediaPlayer.release();
								mMediaPlayer = null;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						myHandler.sendEmptyMessage(CREAT_PLAY_VIDEO);
						runState = 0;
					}
					break;
				case CREAT_PLAY_VIDEO:
					logUtil.d_3(TAG, " MyVideoHandler->CREAT_PLAY_VIDEO");
					try {
						synchronized(mLockObject) {
							if (mMediaPlayer == null) {
								mMediaPlayer = new IjkMediaPlayer();
								if(MyApplication.getInstance().getPrefManage().getBooleanValueByKey(PrefManage.BooleanKey.isIjkPlayHard)) {
									mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
									mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
									mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
									mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec_mpeg4", 1);
								}

								mMediaPlayer.setLooping(isLooping);
								mMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
								mMediaPlayer.setOnPreparedListener(mPreparedListener);
								mMediaPlayer.setOnCompletionListener(mCompletionListener);
								mMediaPlayer.setOnErrorListener(mMediaPlayerErrorListener);
								mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangeListener);
								mMediaPlayer.setOnInfoListener(mInfoListener);
								mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
								mMediaPlayer.setScreenOnWhilePlaying(true);
							}
							myHandler.removeMessages(START_PLAY_VIDEO);
							myHandler.sendEmptyMessage(START_PLAY_VIDEO);
						}
//						myHandler.removeMessages(UPDATA_PLAY_INFO);
//						myHandler.sendEmptyMessage(UPDATA_PLAY_INFO);
					} catch (Exception ex) {
						myHandler.sendEmptyMessage(STOP_PLAY_VIDEO);
						return false;
					}
					break;
				case START_PLAY_VIDEO:
					try {
						if(mUri != null) {
							mMediaPlayer.setDataSource(mContext, mUri);
						}else if(fileDescriptor != null){
							mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor());
						}
						if(mSurfaceTexture != null){
							mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
							mMediaPlayer.prepareAsync();
						}
					}catch (Exception ex){
						ex.printStackTrace();
					}
					break;
				case STOP_PLAY_VIDEO:
					synchronized(mLockObject) {
						runState = 1;
						if(mMediaPlayer != null) {
							logUtil.d_3(TAG, " release player");
							mIsPrepared = false;
							try {
								mMediaPlayer.stop();
								mMediaPlayer.reset();
								mMediaPlayer.release();
							}catch (Exception ex){
								ex.printStackTrace();
							}
						}
						runState = 0;
					}
					break;
				case UPDATA_PLAY_INFO:
					myHandler.sendEmptyMessageDelayed(UPDATA_PLAY_INFO,1000);
					break;
			}
			return false;
		}
	});
	
	public ELiveTextureView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ELiveTextureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initVideoView();
	}

	private SurfaceTexture mSurfaceTexture;
	private void initVideoView() {
		logUtil.d_3(TAG, " = initVideoView");
		setSurfaceTextureListener(new SurfaceTextureListener() {
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
				if (mSurfaceTexture == null) {
					mSurfaceTexture = surface;
//					if(mUri != null) {
//						myHandler.removeMessages(START_PLAY_VIDEO);
//						myHandler.sendEmptyMessage(START_PLAY_VIDEO);
//					}
				}
			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
				return false;
			}

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surface) {

			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 这个之前是默认的拉伸图像
		if (mSurfaceWidth != 0 && mSurfaceHeight != 0) {
			setMeasuredDimension(mSurfaceWidth, mSurfaceHeight);
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	/**
	 *  设置视频大小，需提前设定
     */
	public void setVideoScale(int width, int height) {
		mSurfaceHeight = height;
		mSurfaceWidth = width;
	}

	/**
	 * 开始播放视频
	 */
	public void setVideoURI(Uri uri) {
		logUtil.d_3(TAG, " setVideoURI->uri = "+uri.toString());
		mUri = uri;
		this.fileDescriptor = null;
		mSeekPos = 0;
        errorCount = 0;
		myHandler.sendEmptyMessage(INIT_PLAYER);
		invalidate();
	}
	/**
	 * 开始播放视频2
	 */
	public void setVideoURI(AssetFileDescriptor fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
		this.mUri = null;
		mSeekPos = 0;
        errorCount = 0;
		myHandler.sendEmptyMessage(INIT_PLAYER);
		invalidate();
	}

	/**
	 * 停止并释放资源
	 */
	public void stopPlayback() {
		logUtil.d_3(TAG, " stopPlay");
		myHandler.removeCallbacksAndMessages(null);
		myHandler.sendEmptyMessage(STOP_PLAY_VIDEO);
	}

	/**
	 * 获取视频长度
	 */
	public long getDuration() {
		long mDuration = 0;
		if (mMediaPlayer != null && mIsPrepared) {
			mDuration = mMediaPlayer.getDuration();
		}
		return mDuration;
	}

	/**
	 * 获取视频当前播放位置
	 */
	public int getCurrentPosition() {
		if (mMediaPlayer != null && mIsPrepared) {
			return (int)mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	/**
	 * 设置视频播放位置
	 */
	public Boolean seekTo(int msec) {
		if (mMediaPlayer != null && mIsPrepared) {
			mMediaPlayer.seekTo(msec);
			mSeekPos = msec;
			return true;
		}
		return false;
	}

	/**
	 * 恢复播放
	 */
	public void start(){
		if (mMediaPlayer != null && mIsPrepared) {
			mMediaPlayer.start();
			if(startListener!=null){
				startListener.onStart();
//				if (MyApplication.getInstance().getmTts() != null)
//					MyApplication.getInstance().getmTts().stopSpeaking();
			}
		}
	}

	/**
	 * 暂停播放
	 */
	public void pause(){
		if (mMediaPlayer != null && mIsPrepared) {
			mMediaPlayer.pause();
//			MyApplication.getInstance().getmTts().startSpeaking("是否联系视频客服", null);
		}
	}

	/**
	 * 获取播放状态
	 */
	public boolean isPlaying() {
		if (mMediaPlayer != null && mIsPrepared) {
			return mMediaPlayer.isPlaying();
		}
		return false;
	}

	/**
	 * 设置是否循环播放
	 */
	public void setLooping(boolean looping) {
		isLooping = looping;
	}

	public void setStartListener(OnStartListener startListener){
		this.startListener = startListener;
	}


	private IMediaPlayer.OnCompletionListener mCompletionListener;
	public void setCompletionListener(IMediaPlayer.OnCompletionListener mCompletionListener){
		this.mCompletionListener = mCompletionListener;
	}

	private IMediaPlayer.OnInfoListener mInfoListener;
	public void setOnInfoListener(IMediaPlayer.OnInfoListener mInfoListener){
		this.mInfoListener = mInfoListener;
	}

	IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(IMediaPlayer mp) {
			if(runState == 0) {
				synchronized(mLockObject) {
					logUtil.d_3(TAG, " mPreparedListener onPrepared");
					mIsPrepared = true;
					if (mSeekPos != 0) {
						mMediaPlayer.seekTo(0);
					}
					start();
				}
			}
		}
	};

	private IMediaPlayer.OnErrorListener mMediaPlayerErrorListener = new IMediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
			logUtil.d_3(TAG, " mMediaPlayerErrorListener : error1="+framework_err+" ::: error2="+impl_err);
//			Toast.makeText(mContext,errorStr,Toast.LENGTH_LONG).show();
            errorCount++;
            if(errorCount>1){
                Toast.makeText(mContext,"获取视频源失败,请重试!",Toast.LENGTH_LONG).show();
                //停止播放视频
                Handler handlerVideo = HandlerManage.getInstance().getHandler(HandlerManage.updateType.videoActivity);
                if (handlerVideo != null) {
                    handlerVideo.sendEmptyMessage(MyConstant.CLOSE_ACTIVITY);
                }
            }else if(runState == 0) {
            	myHandler.removeMessages(INIT_PLAYER);
				myHandler.sendEmptyMessage(INIT_PLAYER);
			}
			return true;
		}
	};

	private IMediaPlayer.OnVideoSizeChangedListener mSizeChangeListener = new IMediaPlayer.OnVideoSizeChangedListener() {
		@Override
		public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
			try {
				// 获取视频资源的宽度
				int mVideoWidth = mp.getVideoWidth();
				// 获取视频资源的高度
				int mVideoHeight = mp.getVideoHeight();
				// 获取屏幕的宽度
				DisplayMetrics display = MyApplication.getInstance().getResources().getDisplayMetrics();
				// 在资源尺寸可以播放观看时处理
				if (mVideoHeight > 0 && mVideoWidth > 0) {
					// 拉伸比例
					float scaleWidth = (float) display.widthPixels / (float) mVideoWidth;
					float scaleHeight = (float) display.heightPixels / (float) mVideoHeight;
					float scale = scaleWidth>scaleHeight?scaleHeight:scaleWidth;
					// 视频资源拉伸至屏幕宽度，横屏竖屏需结合传感器等特殊处理
					mVideoWidth = (int)(scale * mVideoWidth);
					mVideoHeight = (int)(scale * mVideoHeight);
					// 设置surfaceview画布大小
					setVideoScale(mVideoWidth, mVideoHeight);
					// 请求调整
					requestLayout();
//					getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				}
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	};

	public interface OnStartListener {
		void onStart();
	}
}
