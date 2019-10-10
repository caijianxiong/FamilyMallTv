package c.liyueyun.mjmall.tv.tv.ui.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;


/**
 * Created by SongJie on 12/26 0026.
 * 加载圆角图片
 */

public class RoundImageDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF rectF;
    private int radian;

    public RoundImageDrawable(Bitmap bitmap, int dp) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        this.radian = Tool.getDimenhight(MyApplication.getAppContext(),dp);
    }

    /**
     * 纯颜色圆角图片
     */
    public RoundImageDrawable(int width, int height, int color, int dp){
        mBitmap = Bitmap.createBitmap(Tool.getDimenhight(MyApplication.getAppContext(),width), Tool.getDimenhight(MyApplication.getAppContext(),height), Bitmap.Config.ARGB_4444);
        mBitmap.eraseColor(color);//填充颜色 Color.parseColor("#cccc00")
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        this.radian = Tool.getDimenhight(MyApplication.getAppContext(),dp);
    }
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(rectF, radian, radian, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight()
    {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha)
    {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }

}