package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;


/**
 * Created by SongJie on 03/15 0015.
 * 圆形图片
 */

public class GlideCircleTransform extends BitmapTransformation {
    private static boolean isShowShaw ;

    //isShaw 圆形图片是否描边
    public GlideCircleTransform(Context context,boolean isShaw) {
        super(context);
        isShowShaw = isShaw;
    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r-Tool.getDimenhight(MyApplication.getAppContext(),4), paint);
        if (isShowShaw) {
            Paint shadPaint = new Paint();
            //抗锯齿
            paint.setAntiAlias(true);
            // 让画出的图形是空心的
            shadPaint.setStyle(Paint.Style.STROKE);
            // 设置画出的线的 粗细程度
            shadPaint.setStrokeWidth(Tool.getDimenhight(MyApplication.getAppContext(),6));
            shadPaint.setColor(Color.WHITE);
            canvas.drawCircle(r, r, r-Tool.getDimenhight(MyApplication.getAppContext(),4), shadPaint);
        }
        return result;
    }

    @Override public String getId() {
        return getClass().getName();
    }
}