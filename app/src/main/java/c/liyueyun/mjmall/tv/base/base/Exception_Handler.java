package c.liyueyun.mjmall.tv.base.base;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.tendcloud.tenddata.TCAgent;

import java.lang.Thread.UncaughtExceptionHandler;

public class Exception_Handler implements UncaughtExceptionHandler {
	private String TAG = "Exception_Handler";
	private UncaughtExceptionHandler mDefaultHandler;
	private static Exception_Handler INSTANCE;
	private Context mContext;
	private Object mLockFile = new Object();

	private final String fileName = "exceptionFile.txt";

	private Exception_Handler(Context ctx) {
		mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	public static Exception_Handler getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Exception_Handler(ctx);
		}
		return INSTANCE;
    }

	@Override
    public void uncaughtException(Thread thread, Throwable ex) {
		logUtil.d_3(TAG, "co.tv caught Exception");
		logUtil.d_e(TAG,ex);
		TCAgent.onError(MyApplication.getAppContext(), ex);
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			logUtil.d_3(TAG, "caught Exception to system manager");
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//退出程序
			MyApplication.getInstance().exit();
		}
	}
	/**
	* 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	*/
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序出现错误,即将重启.", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		return true;
	}
}