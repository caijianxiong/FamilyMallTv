package c.liyueyun.mjmall.tv.base.manage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

import c.liyueyun.mjmall.tv.BuildConfig;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.httpApi.api.ApiTemplate;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyErrorMessage;
import c.liyueyun.mjmall.tv.base.httpApi.response.UpdateResult;
import c.liyueyun.mjmall.tv.base.task.DownloadAsyncTask;


/**
 * 应用程序更新工具包
 */
public class UpdateManager {
    private final String TAG = this.getClass().getSimpleName();
    private static UpdateManager INSTANCE;
    private onUpDateStatusListener onUpDateStatusListener;
    private Context mContext;
    private ApiTemplate mApi;
    private UpdateResult updateResult;
    private PrefManage prefManage;
    private String folderPath;
    private String apkName;
    private String apkPath;

    private String pattern = "yyyyMMdd";
    private boolean hasUpDate = false;

    private final int CHECK_FILE_EXIST = 10000;
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_FILE_EXIST:
                    checkApkExist();
                    break;
            }
            return false;
        }
    });
    private DownloadAsyncTask downloadAsyncTask;

    public static UpdateManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpdateManager();
        }
        return INSTANCE;
    }

    public UpdateManager() {
        mApi = MyApplication.getInstance().getmApi();
        prefManage = MyApplication.getInstance().getPrefManage();
        folderPath = Tool.getSavePath(MyConstant.folderNameApk);
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 更新APP,判断是否需要更新
     */
    public void checkAppUpdate(Context context) {
        mContext = context;
        mApi.getUpdateTemplate().getUpdate(new MyCallback<UpdateResult>() {
            @Override
            public void onError(MyErrorMessage msg) {
                logUtil.d_2(TAG, "获取更新数据失败");
                hasUpDate = false;
            }

            @Override
            public void onSuccess(UpdateResult response) {
                updateResult = response;
                if (Tool.getVersionCode(mContext) < response.getVersionCode()) {
                    hasUpDate = true;
                } else {
                    hasUpDate = false;
                }
            }

            @Override
            public void onFinish() {
                if (onUpDateStatusListener != null) {
                    onUpDateStatusListener.hasUpDate(updateResult);
                }
            }
        });
    }

    public void toUpdate() {
        if (hasUpDate)
            myHandler.sendEmptyMessage(CHECK_FILE_EXIST);
    }

    /**
     * 显示版本更新通知对话框
     */
    private void checkApkExist() {
        apkName = "mall_tv_" + updateResult.getVersionCode() + "_" + BuildConfig.FLAVOR + ".apk";
        apkPath = folderPath + apkName;
        File file = new File(apkPath);
        if (file.exists()) {
            logUtil.d_3(TAG, "APK已经存在");
            //直接安装apk
            installApk();
        } else {
            //下载apk
            logUtil.d_3(TAG, "下载APK");
            downloadApk();
        }
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        //清空文件夹
        File file = new File(folderPath);
        File[] allFiles = file.listFiles();
        if (allFiles != null) {
            for (int i = 0; i < allFiles.length; i++) {
                Tool.deleteFile(allFiles[i]);
            }
        }
        String url = null;
        if (BuildConfig.FLAVOR.equals("liyueyun")) {
            url = updateResult.getLiyueyun();
        } else if (BuildConfig.FLAVOR.equals("znds")) {
            url = updateResult.getZnds();
        } else if (BuildConfig.FLAVOR.equals("coocaa")) {
            url = updateResult.getCoocaa();
        } else if (BuildConfig.FLAVOR.equals("xiaomi")) {
            url = updateResult.getXiaomi();
        } else if (BuildConfig.FLAVOR.equals("lielanghr")) {
            url = updateResult.getLielanghr();
        } else if (BuildConfig.FLAVOR.equals("sharp")) {
            url = updateResult.getSharp();
        } else if (BuildConfig.FLAVOR.equals("tcl")) {
            url = updateResult.getTcl();
        }
        if (Tool.isEmpty(url)) {
            url = updateResult.getDownloadUrl();
        }
        //后台下载
        logUtil.d_3(TAG, "下载APK URL:" + url);
        downloadAsyncTask = new DownloadAsyncTask(url, MyConstant.folderNameApk, apkName, new DownloadAsyncTask.OnDownCompleteListener() {
            @Override
            public void onError(Throwable t) {
                logUtil.d_3(TAG, "下载更新APK失败，error");
                Toast.makeText(mContext, "下载更新APK失败,请到应用市场下载最新版本", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProcces(int current, int total) {
                if (onUpDateStatusListener != null) {
                    onUpDateStatusListener.onProccess(current, total);
                }
            }

            @Override
            public void onSuccess(String filePath) {
                logUtil.d_3(TAG, "下载成功,提示安装");
                installApk();
            }
        });
        downloadAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 安装apk
     */
    public void installApk() {
        if (apkPath != null) {
            logUtil.d_3(TAG, "开始执行安装: " + apkPath);
            try {
                File apkFile = new File(apkPath);
                //修改文件权限,外部可访问
                if (apkPath.contains("data/data")) {
                    Runtime.getRuntime().exec("chmod 755 " + apkFile.getParentFile().getParent()).waitFor();
                    Runtime.getRuntime().exec("chmod 755 " + apkFile.getParent()).waitFor();
                    Runtime.getRuntime().exec("chmod 755 " + apkFile).waitFor();
                }
                //执行安装
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(
                            mContext
                            , "c.liyueyun.mjmall.tv.fileprovider"
                            , apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                }
                MyApplication.getInstance().startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void cancelTask() {
        if (downloadAsyncTask != null && !downloadAsyncTask.isCancelled()) {
            downloadAsyncTask.cancelTask();
            downloadAsyncTask.cancel(true);
            downloadAsyncTask = null;
        }
        //清空apk文件夹
        File file = new File(folderPath);
        deleteFile(file);
    }

    //flie：要删除的文件夹的所在位置
    private void deleteFile(File file) {
        if (file == null) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
//            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }


    public void setOnUpDateStatusListener(UpdateManager.onUpDateStatusListener onUpDateStatusListener) {
        this.onUpDateStatusListener = onUpDateStatusListener;
    }

    public interface onUpDateStatusListener {
        void hasUpDate(UpdateResult updateResult);

        void onProccess(int current, int total);
    }


    public void release() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
