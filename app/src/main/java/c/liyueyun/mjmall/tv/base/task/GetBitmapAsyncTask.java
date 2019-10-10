package c.liyueyun.mjmall.tv.base.task;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;


public class GetBitmapAsyncTask extends AsyncTask<Void, Void, Throwable> {
    private final static String TAG = "GetBitmapAsyncTask";
    private boolean cancelled;

    private String width,height,index;
    private String folderPath;
    private String bitmapPath;
    private PdfiumCore pdfiumCore;
    private String url;
    private OnBitmapCompleteListener bitmapCompleteListener;

    public GetBitmapAsyncTask(String width, String height, String index, String url, OnBitmapCompleteListener listener) {
        this.width = width;
        this.height = height;
        this.index = index;
        this.url = url;
        this.cancelled = false;
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
            int getWidth = Integer.valueOf(width);
            int getHeight = Integer.valueOf(height);
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
                int pageWidth = pdfiumCore.getPageWidth(pdfDocument,pos);
                int pageHeight = pdfiumCore.getPageHeight(pdfDocument,pos);
                float scaleWidth = getWidth/(float)pageWidth;
                float scaleHeight = getHeight/(float)pageHeight;
                float scale = scaleWidth>scaleHeight?scaleHeight:scaleWidth;
                pageWidth = (int)(scale * pageWidth);
                pageHeight = (int)(scale * pageHeight);
                Bitmap render = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
                pdfiumCore.renderPageBitmap(pdfDocument, render, pos, 0, 0, pageWidth, pageHeight, true);
                pdfiumCore.closeDocument(pdfDocument);
//                Bitmap cpy = render.copy(Bitmap.Config.RGB_565, false);
//                render.recycle();
//                render = cpy;
                bitmapPath =  saveBitmap(render,imgName);
                logUtil.d_3(TAG,"获取图片成功，返回地址 ="+bitmapPath);
            }
            pfd.close();
            return null;
        } catch (Throwable t) {
            logUtil.d_3(TAG,"获取图片异常，t ="+t.getMessage());
            return t;
        }
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if(cancelled){
            return;
        }
        if (t != null || bitmapPath == null) {
            if(bitmapCompleteListener!=null){
                bitmapCompleteListener.onError(t,url,this.index);
            }
        }else {
            if(bitmapCompleteListener!=null){
                bitmapCompleteListener.onSuccess(bitmapPath,url,this.index);
            }
        }
    }

    @Override
    protected void onCancelled() {
        cancelled = true;
    }

    /**
     * 通过路径获取数据库的uri，PDF文件类型的
     */
    private Uri getPdfUri(String filePath){
        Uri mUri = Uri.parse("content://media/external/file");
        Uri pdfUri = null;

        Uri uri = MediaStore.Files.getContentUri("external");
        String selection =  MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] selectionArg = new String[1];
        selectionArg[0] = "application/pdf";
        Cursor cursor = MyApplication.getAppContext().getContentResolver().query(uri, null, selection,selectionArg, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            if (filePath.equals(data)) {
                int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                pdfUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        if(pdfUri == null){
            logUtil.d_3(TAG,"获取pdf的uri失败 filePath = "+filePath);
        }
        return pdfUri;
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
        void onError(Throwable t, String url, String bitmapIndex);
        void onSuccess(String filePath, String url, String bitmapIndex);
    }

}
