package c.liyueyun.mjmall.tv.base.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;


/**
 * Created by SongJie on 06/20 0020.
 */

public class GetFileImgAsyncTask extends AsyncTask<Void, Void, Throwable> {
    private final static String TAG = "GetBitmapAsyncTask";

    private String index;
    private String folderPath;
    private String bitmapPath;
    private PdfiumCore pdfiumCore;
    private String url;
    private OnBitmapCompleteListener bitmapCompleteListener;

    public GetFileImgAsyncTask(String url, String index, OnBitmapCompleteListener listener) {
        this.index = index;
        this.url = url;
        this.bitmapCompleteListener = listener;
        logUtil.d_3(TAG,"开始获取图片url = "+url +" index = "+index);
        pdfiumCore = new PdfiumCore(MyApplication.getAppContext());
        folderPath = Tool.getSavePath(MyConstant.folderNamePdf);
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            String id = Tool.urlToMD5(url);
            String filePath = folderPath+id+".pdf";
            File file = new File(filePath);
            if(!file.exists()){
                logUtil.d_3(TAG,"获取bitmap失败，pdf未找到");
                return new Throwable();
            }
            int pos = Integer.valueOf(index);
            String imgName = id+"_" +pos;
            String imgPath = folderPath + imgName + ".jpg";
            File imgFile = new File(imgPath);
            if(imgFile.exists()){
                logUtil.d_3(TAG,"图片已经存在，直接返回地址 ="+imgPath);
                bitmapPath = imgPath;
                return null;
            }

            ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);
//            ParcelFileDescriptor pfd = mContext.getContentResolver().openFileDescriptor(getPdfUri(filePath), "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(pfd, null);
            if(pos<pdfiumCore.getPageCount(pdfDocument)){
                pdfiumCore.openPage(pdfDocument, pos);
                int pageWidth = pdfiumCore.getPageWidthPoint(pdfDocument,pos);
                int pageHeight = pdfiumCore.getPageHeightPoint(pdfDocument,pos);
                float pageScale = pageWidth/(float)pageHeight;
                float viewScale = 1920/1080.0f;
                if(pageScale>viewScale){//宽型图片
                    pageWidth = 1920;
                    pageHeight = (int)(1920/pageScale);
                }else{//竖型图片
                    pageHeight = 1080;
                    pageWidth = (int)(pageScale*1080);
                }
                Bitmap render = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_4444);
                pdfiumCore.renderPageBitmap(pdfDocument, render, pos, 0, 0, pageWidth, pageHeight, true);
                pdfiumCore.closeDocument(pdfDocument);
//                Bitmap cpy = render.copy(Bitmap.Config.RGB_565, false);
//                render.recycle();
//                render = cpy;
                bitmapPath =  saveBitmap(render,imgName);
                logUtil.d_3(TAG,"获取图片成功，返回地址 ="+bitmapPath +"\n pageWidth="+pageWidth+"\n pageHeight="+pageHeight);
            }
            pfd.close();
            return null;
        } catch (Throwable t) {
            logUtil.d_3(TAG,"获取图片异常");
            t.printStackTrace();
            return t;
        }
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if (t != null || bitmapPath == null) {
            if(bitmapCompleteListener!=null){
                bitmapCompleteListener.onError(t);
            }
        }else {
            if(bitmapCompleteListener!=null){
                bitmapCompleteListener.onSuccess(bitmapPath);
            }
        }
    }

    /**
     * 保存bitmap到本地
     */
    private String saveBitmap(Bitmap mBitmap, String imgName) {
        File filePic;
        try {
            filePic = new File(folderPath + imgName + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }else{
                Tool.deleteFile(filePic);
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            mBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }

    public interface OnBitmapCompleteListener {
        void onError(Throwable t);
        void onSuccess(String filePath);
    }

}
