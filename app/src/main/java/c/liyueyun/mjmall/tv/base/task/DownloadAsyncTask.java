package c.liyueyun.mjmall.tv.base.task;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;

/**
 * Created by SongJie on 02/22 0022.
 */

public class DownloadAsyncTask extends AsyncTask<Void, Void, Throwable> {
    private final static String TAG = "DownloadAsyncTask";
    private boolean cancelled;

    private String url;
    private String fileName;
    private String folderPath;
    private OnDownCompleteListener downCompleteListener;
    private boolean isOverwrite;
    private HttpURLConnection conn;

    public DownloadAsyncTask(String url, String foldName, String fileName, OnDownCompleteListener listener) {
        this.url = url;
        this.fileName = fileName;
        this.downCompleteListener = listener;
        folderPath = Tool.getSavePath(foldName);
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        isOverwrite = false;
        logUtil.d_3(TAG, "开始下载 url=" + url);
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            cancelled = false;
            File file = new File(folderPath + fileName);
            //是否已下载更新文件
            if (!isOverwrite && file.exists()) {
                return null;
            }
            File temp = new File(folderPath + fileName + "temp");
            if (temp.exists()) {
                temp.delete();
            }
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            //设置超时间
//            conn.setConnectTimeout(600 * 1000);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestProperty("Accept-Encoding", "identity");
            //得到输入流,获取自己数组
            //获取内容长度
            int contentLength = conn.getContentLength();
            readInputStream(contentLength, conn.getInputStream(), new FileOutputStream(temp));
            //重名字
            temp.renameTo(file);
            conn.disconnect();
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            return t;
        }
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if (cancelled) {
            return;
        }
        if (t != null) {
            logUtil.d_3(TAG, "下载失败 url=" + this.url);
            if (downCompleteListener != null) {
                downCompleteListener.onError(t);
            }
        } else {
            String filePath = folderPath + fileName;
            logUtil.d_3(TAG, "下载成功 filePath =" + filePath);
            if (downCompleteListener != null) {
                downCompleteListener.onSuccess(filePath);
            }
        }
    }

    @Override
    protected void onCancelled() {
        cancelled = true;
        logUtil.d_2(TAG, "onCancelled=" + cancelled);
        if (conn != null) {
            conn.disconnect();
        }
    }

    public void cancelTask() {
        this.cancelled = true;
    }

    /**
     * 从输入流中获取字节数组
     */
    private void readInputStream(int totalSize, InputStream inputStream, FileOutputStream fos) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int len = 0;
        int size = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            if (cancelled) {
                break;
            }
            fos.write(buffer, 0, len);
            size = size + len;
            if (downCompleteListener != null) {
                downCompleteListener.onProcces(size, totalSize);
            }
        }
        inputStream.close();
        fos.close();
    }

    /**
     * 设置是否覆盖源文件
     */
    public void setOverwrite(boolean isWrite) {
        isOverwrite = isWrite;
    }

    public interface OnDownCompleteListener {
        void onError(Throwable t);

        void onProcces(int current, int total);

        void onSuccess(String filePath);
    }
}