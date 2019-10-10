package c.liyueyun.mjmall.tv.base.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;


/**
 * Created by songjie on 018 12/18.
 */

public class CreateQRcodeAysncTask extends AsyncTask<Void, Void, Throwable> {
    private final String TAG = this.getClass().getSimpleName();
    private OnCreateListener createListener;
    private String url;
    private String savePath;
    private Bitmap bitmap;
    private int logoId = -1;

    @Override
    protected Throwable doInBackground(Void... voids) {
        try {
            //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix matrix = new MultiFormatWriter().encode(
                    url,
                    BarcodeFormat.QR_CODE,
                    Tool.getDimenWidth(MyApplication.getAppContext(),1080),
                    Tool.getDimenhight(MyApplication.getAppContext(),1080),
                    hints);
            matrix = updateBit(matrix,Tool.getDimenhight(MyApplication.getAppContext(),40));
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) { // 有信息设置像素点成黑色
                        pixels[y * width + x] = 0xff000000;
                    } else { // 无信息设置像素点为白色
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //通过像素数组生成bitmap,具体参考api
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            Bitmap logoBit = null;
            if(logoId == -1){
                logoBit = BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(), R.mipmap.logo);
            }else if(logoId != 0){
                logoBit = BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),logoId);
            }
            if(logoBit!=null){
                //获取图片的宽高
                int srcWidth = bitmap.getWidth();
                int srcHeight = bitmap.getHeight();
                int logoWidth = logoBit.getWidth();
                int logoHeight = logoBit.getHeight();
                //logo大小为二维码整体大小的1/5
                float scaleFactor = srcWidth * 1.0f / 7 / logoWidth;
                Canvas canvas = new Canvas(bitmap);
                canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
                canvas.drawBitmap(logoBit, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();
            }
        }catch (Throwable t){
            t.printStackTrace();
            return t;
        }
        // 保存bitmap到本地
        if(!Tool.isEmpty(savePath)) {
            try {
                File filePic = new File(savePath);
                if (!filePic.exists()) {
                    filePic.getParentFile().mkdirs();
                    filePic.createNewFile();
                } else {
                    Tool.deleteFile(filePic);
                    filePic.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(filePic);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if(createListener!=null){
            if (t != null) {
                logUtil.d_3(TAG,"失败 url="+this.url);
                createListener.onError(t);
            } else {
                logUtil.d_3(TAG,"成功 url="+this.url);
                createListener.onSuccess(bitmap);
            }
        }
    }

    /**
     * 设置二维码白边
     */
    private BitMatrix updateBit(BitMatrix matrix, int margin){
        int tempM = margin*2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for(int i= margin; i < resWidth- margin; i++){   //循环，将二维码图案绘制到新的bitMatrix中
            for(int j=margin; j < resHeight-margin; j++){
                if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
                    resMatrix.set(i,j);
                }
            }
        }
        return resMatrix;
    }

    public void setLogoId(int logoId){
        this.logoId = logoId;
    }
    public CreateQRcodeAysncTask(String url, String savePath, OnCreateListener listener) {
        logUtil.d_3(TAG,"开始下载 url="+url);
        this.url = url;
        this.savePath = savePath;
        this.createListener = listener;

    }

    public interface OnCreateListener {
        void onError(Throwable t);
        void onSuccess(Bitmap bitmap);
    }

}
