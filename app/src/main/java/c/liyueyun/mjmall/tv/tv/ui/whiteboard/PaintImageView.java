package c.liyueyun.mjmall.tv.tv.ui.whiteboard;//package liyueyun.business.tv.ui.whiteboard;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PointF;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.ViewTreeObserver;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.y2w.uikit.utils.StringUtil;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import liyueyun.business.im.manage.Users;
//import liyueyun.business.im.model.MessageModel;
//import liyueyun.business.tv.R;
//import liyueyun.business.tv.ui.whiteboard.linebean.LineInfo;
//import liyueyun.business.tv.ui.whiteboard.linebean.PageInfo;
//import liyueyun.business.tv.ui.whiteboard.linebean.PointInfo;
//import liyueyun.business.tv.ui.whiteboard.linebean.TagInfo;
//
///**
// * 自定义图片批注控件
// */
//public class PaintImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener, ViewTreeObserver.OnGlobalLayoutListener, Runnable {
//    private String TAG = getClass().getSimpleName().toString();
//
//    private final int NONE = 0;
//    private final int HANDWRITE = 1;
//    private final int ERASER = 2;
//    private final int PEN_YINGGUANG = 3;
//    private final int ARROW = 4;
//    private final int RECTANGLE = 5;
//    private final int ELLIPSE = 6;
//
//    private Context mContext;
//    private Bitmap mCanvasBitmap;
//    private Canvas mCanvas;         //单缓存缓存历史线条
////    private Paint mPaint;
//
//    //    private PorterDuffXfermode mPorterDuffXfermode;
//    private Paint mEraserPaint;
//
//    private Paint mFloatBmPaint; //悬浮图标画笔
//    private ArrayList<LineInfo> mPaintLines;
//    private PageInfo pageInfo;
//    private ArrayList<LineInfo> mRedoPaintLines;
//    private int currntColor; //记录画笔颜色
//    private int currentPaintSize; //记录画笔粗细
//    private ScaleGestureDetector mScaleGestureDetector = null;  //缩放检查手势的类
//    private float mScale; // 缩放倍数, mScale
//    private Matrix mMatrix = new Matrix();     //
//    private float[] mMatrixValues = new float[9];
//    private float preX, preY = 0;   //上一个点的位置
//    private int mBitmapWidth, mBitmapHeight = -1;
//    private float mCentreTranX, mCentreTranY;// 图片初始化居中时的偏移
//    private int mTouchSlop;             //最小滑动距离
//    private int mCurrentRotate = 0;
//    private int mDrawState = NONE; //当前绘制状态
//    private boolean isDraw = false; //是否在一次触摸事件之中.
//    private TransPointF transPointF;
//    private LineInfo lineInfo;
//    private List<TagInfo> tagsList = new ArrayList<>();
//    private int currentClickIconIndex; //当前点击的会话icon
//    private Bitmap readBitmap;
//    private Bitmap unreadBitmap;
//    private int floatBmWidth, floatBmHeight;
//    private Bitmap defaultHeadBm;
//
//    private boolean isLongPressMode; // true 长按事件
//    private boolean isAddClick = false;//添加tag
//
//    private boolean hideTalk = true; //true隐藏话题图标
//
//    private boolean overParentBoundsEnable = true; //是否可以滑动超出界面
//
//
//    @Override
//    public void run() {
//        isLongPressMode = true;
//        //如果当前是画笔状态，则不处理
//        if (mDrawState == HANDWRITE)
//            return;
//        // 1.根据action_down的位置判断是否在tagsList当中存在
//        if (hasView(mLastX, mLastY)) {
//            // 1.1当前位置已添加会话icon，则将该icon状态修改为可滑动状态
//            if (currentClickIconIndex < tagsList.size()) {
//                tagsList.get(currentClickIconIndex).setState(TagInfo.STATE_TRANSLATE);
//            }
//        } else {
//            // 1.2当前位置未添加icon,则添加icon
//            addTag(mLastX, mLastY);
//            isLongPressMode = false;
//            isAddClick = true;
//            TagInfo info = tagsList.get(tagsList.size() - 1);
//            info.setClick(true);
//            //点击事件处理
//            Log.e("info", "被点击了 - " + currentClickIconIndex);
//            if (clickListener != null)
//                clickListener.onSessionIconClick(info);
//            invalidate();
//        }
//    }
//
//    public interface OnSessionIconClick {
//
//        void onSessionIconClick(TagInfo info);
//
//        void onLongClickUp(TagInfo info);
//    }
//
//    OnSessionIconClick clickListener;
//
//    public void setOnSessionClickListener(OnSessionIconClick listener) {
//        this.clickListener = listener;
//    }
//
//    public void setClickTagById(String msgId) {
//        for (TagInfo info : tagsList) {
//            if (msgId.equals(info.getMid())) {
//                info.setClick(true);
//                if (clickListener != null)
//                    clickListener.onSessionIconClick(info);
//                break;
//            }
//        }
//
//    }
//
//    public abstract static class ListenTouch extends Object {
//        public abstract boolean touchUp(MotionEvent event);
//    }
//
//    ListenTouch listenTouch;
//
//    public void setListenTouch(ListenTouch listenTouch) {
//        this.listenTouch = listenTouch;
//    }
//
//    public interface onUndoPaintListener {
//        void onUndoPaint(LineInfo lineInfo);
//    }
//
//    onUndoPaintListener onUndoPaintListener;
//
//    public void setOnUndoPaintListener(onUndoPaintListener onUndoPaintListener) {
//        this.onUndoPaintListener = onUndoPaintListener;
//    }
//
//    public interface onPaintListener {
//        void onPaintEnd(LineInfo lineInfo);
//
//        void onPaintStart();
//    }
//
//    onPaintListener onPaintListener;
//
//    public void setOnPaintListener(onPaintListener onPaintListener) {
//        this.onPaintListener = onPaintListener;
//    }
//
//    public interface onGlobalLayoutFinish {
//        void onGlobalLayout(ImageView loadedImage);
//    }
//
//    onGlobalLayoutFinish onGlobalLayoutFinish;
//
//    public void setOnGlobalLayoutFinish(onGlobalLayoutFinish onGlobalLayoutFinish) {
//        this.onGlobalLayoutFinish = onGlobalLayoutFinish;
//    }
//
//    @Override
//    public void onGlobalLayout() {
//        if (onGlobalLayoutFinish != null)
//            onGlobalLayoutFinish.onGlobalLayout(this);
//    }
//
//    public PaintImageView(Context context) {
//        this(context, null);
//    }
//
//    public PaintImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.mContext = context;
//        userId = Users.getInstance().getCurrentUser().getEntity().getId();
//        initialData();
//        ViewTreeObserver observer = getViewTreeObserver();
//        if (null != observer)
//            observer.addOnGlobalLayoutListener(this);
//    }
//
//    private void initialData() {
//        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//        mScale = 1f;    //手势放大倍数初始值为1
//        currntColor = DrawPathCong.mStartColor;
//        currentPaintSize = DrawPathCong.mStartSize;
////        paintInfo2Pc = new PaintInfo2Pc();
//        mPaintLines = new ArrayList<>();
//        mRedoPaintLines = new ArrayList<>();
//        Collections.synchronizedList(mRedoPaintLines);
//        Collections.synchronizedList(mPaintLines);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        initPaint();
////        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
//        initErasePaint();
//        mScaleGestureDetector = new ScaleGestureDetector(mContext, this);
//        readBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_tall_read_icon);
//        defaultHeadBm = BitmapFactory.decodeResource(getResources(), R.drawable.chat_default_icon);
//        floatBmWidth = readBitmap.getWidth();
//        floatBmHeight = readBitmap.getHeight();
//    }
//
//    /**
//     * 初始化画板,为简化逻辑,使用统一变量记录当前画笔颜色,粗细
//     */
//    private void initPaint() {
////        mPaint = new Paint();
////        mPaint.setAntiAlias(true);
////        mPaint.setDither(true);
////        mPaint.setColor(currntColor);
////        mPaint.setStyle(Paint.Style.STROKE);
////        mPaint.setStrokeJoin(Paint.Join.ROUND);
////        mPaint.setStrokeCap(Paint.Cap.ROUND);
////        mPaint.setStrokeWidth(currentPaintSize);
//        mFloatBmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mFloatBmPaint.setFilterBitmap(true);
//        mFloatBmPaint.setDither(true);
//    }
//
//    private void initErasePaint() {
//        //橡皮擦
//        mEraserPaint = new Paint();
//        mEraserPaint.setAlpha(0);
//        //这个属性是设置paint为橡皮擦重中之重
//        //这是重点
//        //下面这句代码是橡皮擦设置的重点
//        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
//        mEraserPaint.setAntiAlias(true);
//        mEraserPaint.setDither(true);
//        mEraserPaint.setStyle(Paint.Style.STROKE);
//        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
//        mEraserPaint.setStrokeWidth(30);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (mMatrix != null) {
//            canvas.setMatrix(mMatrix);
//        }
////        mCanvas = canvas;
//        super.onDraw(canvas);
//        // 提高速度
//        canvas.clipRect(getPaddingLeft(), getPaddingTop(), getRight(), getBottom());
//        if (mCanvasBitmap != null) {
//            if (mDrawState != NONE && isDraw) {
//                drawCurrentPointPath(mCanvas);
//            }
//            drawAllPointPath(mCanvas);
//            canvas.drawBitmap(mCanvasBitmap, 0, 0, null);
//            if (!hideTalk)
//                drawTags();
//        }
//
//    }
//
//    public void setTagsList(List<TagInfo> list) {
//        Log.i(TAG + "1", "setTagsList");
//        if (list != null && !list.isEmpty()) {
//            this.tagsList = list;
//        }
//        invalidate();
//    }
//
//    private void drawTags() {
//        for (final TagInfo tagInfo : tagsList) {
//            PointF f = new PointF();
//            f.x = tagInfo.getX();
//            f.y = tagInfo.getY();
//            PointF pointF = transPointF.logic2Display(f);
//            if (tagInfo.isClick()) {
//                if (unreadBitmap == null)
//                    unreadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_tall_unread_icon);
//                mCanvas.drawBitmap(unreadBitmap, pointF.x, pointF.y, mFloatBmPaint);
//            } else {
//                mCanvas.drawBitmap(readBitmap, pointF.x, pointF.y, mFloatBmPaint);
//            }
//            float offset = floatBmWidth * 1f / 6;
//            RectF rectF = new RectF(pointF.x + offset, pointF.y + offset,
//                    pointF.x + offset * 5, pointF.y + offset * 5);
//            tagInfo.setHeadRect(rectF);
//            if (tagInfo.getHeadBitmap() != null) {
//                Log.i(TAG + "1", "drawable head:" + tagInfo.getAvatarUrl() + ",SenderId:" + tagInfo.getSenderId());
//                mCanvas.drawBitmap(tagInfo.getHeadBitmap(), null, rectF, mFloatBmPaint);
//            } else {
//                Log.i(TAG + "1", "drawable default head:" + tagInfo.getAvatarUrl() + ",SenderId:" + tagInfo.getSenderId());
//                mCanvas.drawBitmap(defaultHeadBm, null, rectF, mFloatBmPaint);
//            }
//            if (tagInfo.getUpdataView() == null) {
//                tagInfo.setUpdataView(new TagInfo.updataView() {
//                    @Override
//                    public void head() {
//                        invalidate();
//                    }
//                });
//            }
//        }
//    }
//
//    //绘制集合中的最后一笔
//    private void drawCurrentPointPath(Canvas canvas) {
//        if (mPaintLines.size() < 1) {
//            return;
//        }
//        LineInfo lineInfo = mPaintLines.get(mPaintLines.size() - 1);
//        ArrayList<PointInfo> pointInfos = lineInfo.getCurrentPointLists();
//        if (pointInfos.size() < 2) {
//            return;
//        }
//        switch (lineInfo.getType()) {
//            case 0:
//            case 1:
//                Paint paint = new Paint();
//                paint.setColor(lineInfo.getColor());
//                paint.setStrokeWidth(lineInfo.getStrokeWidth());
//                paint.setStyle(Paint.Style.STROKE);
//                drawCurrentPoint(canvas, paint, lineInfo);
//                break;
//            case 2:
//                drawCurrentPoint(canvas, mEraserPaint, lineInfo);
//                break;
//        }
//    }
//
//    //向画板上画全部记录的全部点的轨迹,绘制过程比较慢...
//    private void drawAllPointPath(Canvas cavas) {
//        for (int i = 0; i < mPaintLines.size(); i++) {
//            LineInfo lineInfo = mPaintLines.get(i);
//            switch (lineInfo.getType()) {
//                case 0:
//                case 1:
//                    Paint paint = new Paint();
//                    paint.setColor(lineInfo.getColor());
//                    paint.setStrokeWidth(lineInfo.getStrokeWidth());
//                    paint.setStyle(Paint.Style.STROKE);
//                    drawCurrentPoint(cavas, paint, lineInfo);
//                    break;
//                case 2:
//                    drawCurrentPoint(cavas, mEraserPaint, lineInfo);
//                    break;
//            }
//        }
//    }
//
//    //绘制当前一笔直线
//    private void drawCurrentPoint(Canvas canvas, Paint paint, LineInfo lineInfo) {
//        if (transPointF == null)
//            return;
//        Path path = new Path();
//        ArrayList<PointInfo> pointInfos = lineInfo.getCurrentPointLists();
//        for (int j = 0; j < pointInfos.size(); j++) {
//            PointF pointF = transPointF.logic2Display(pointInfos.get(j).getCrtlPointF());
//            if (j == 0) {
//                path.moveTo(pointF.x, pointF.y);
//            } else {
//                PointF prePointF = transPointF.logic2Display(pointInfos.get(j).getStartPointF());
//                path.quadTo(prePointF.x, prePointF.y, pointF.x, pointF.y);     //绘制贝斯尔曲线
//            }
//        }
//        canvas.drawPath(path, paint);
//    }
//
//    // 根据上一个点和原点的坐标获得贝斯尔的控制点
//    private float getBesPoint(float prex, float x) {
//        return (prex + x) / 2;
//    }
//
//    private void touch_down(float downX, float downY) {
//        float[] tempF = {downX, downY};
//        Matrix tempMatrix = new Matrix();
//        mMatrix.invert(tempMatrix);
//        tempMatrix.mapPoints(tempF, new float[]{downX, downY});
//        lineInfo = new LineInfo();
//        lineInfo.setLineId(userId);
//        if (mDrawState == HANDWRITE || mDrawState == ERASER) {
//            if (mDrawState == HANDWRITE) {
//                lineInfo.setColor(currntColor);
//                lineInfo.setStrokeWidth(currentPaintSize);
//                lineInfo.getCurrentPointLists().add(new PointInfo(null, transPointF.display2Logic(tempF[0], tempF[1]), 0));
//            } else if (mDrawState == ERASER) {
//                lineInfo.setColor(Color.WHITE);
//                lineInfo.setStrokeWidth(DrawPathCong.mEraseSize);
//                lineInfo.getCurrentPointLists().add(new PointInfo(null, transPointF.display2Logic(tempF[0], tempF[1]), 0));
//            }
//            lineInfo.setType(mDrawState);
//            mPaintLines.add(lineInfo);
//            preX = tempF[0];
//            preY = tempF[1];
//        } else if (mDrawState == ARROW || mDrawState == ELLIPSE || mDrawState == RECTANGLE) {
//            lineInfo.setColor(currntColor);
//            lineInfo.setStrokeWidth(currentPaintSize);
//            lineInfo.getCurrentPointLists().add(new PointInfo(null, transPointF.display2Logic(tempF[0], tempF[1]), 0));
//            switch (mDrawState) {
//                case ARROW:
//                    lineInfo.setType(4);
//                    break;
//                case ELLIPSE:
//                    lineInfo.setType(3);
//                    break;
//                case RECTANGLE:
//                    lineInfo.setType(2);
//                    break;
//            }
//            mPaintLines.add(lineInfo);
//        }
//        invalidate();
//    }
//
//
//    private void touch_move(float moveX, float moveY) {
//        float dx = Math.abs(moveX - preX);
//        float dy = Math.abs(moveY - preY);
//        if (dx >= mTouchSlop || dy >= mTouchSlop) {   //对发送点的频率进行一定的限制
//            float[] tempF = {moveX, moveY};
//            Matrix tempMatrix = new Matrix();
//            mMatrix.invert(tempMatrix);
//            tempMatrix.mapPoints(tempF, new float[]{moveX, moveY});
//            if (lineInfo == null) {
////                Log.e("PaintImageView", "按压点还没有执行");
//                return;
//            }
//            if (mDrawState == HANDWRITE || mDrawState == ERASER) {
//                PointInfo prePointInfo = lineInfo.getCurrentPointLists().get(lineInfo.getCurrentPointLists().size() - 1);
//                float besX = getBesPoint(preX, tempF[0]);
//                float besY = getBesPoint(preY, tempF[1]);
//                lineInfo.getCurrentPointLists().add(new PointInfo(transPointF.display2Logic(preX, preY)
//                        , transPointF.display2Logic(besX, besY), prePointInfo.getIndex() + 1));
//                invalidate();
//                preX = tempF[0];
//                preY = tempF[1];
//            } else if (mDrawState == RECTANGLE || mDrawState == ARROW || mDrawState == ELLIPSE) {
//                if (lineInfo.getCurrentPointLists().size() > 1) {
//                    lineInfo.getCurrentPointLists().add(1, new PointInfo(null, transPointF.display2Logic(tempF[0], tempF[1]), 0));
//                } else {
//                    lineInfo.getCurrentPointLists().add(new PointInfo(null, transPointF.display2Logic(tempF[0], tempF[1]), 0));
//                }
//                invalidate();
//            }
//        }
//    }
//
//    private void touch_up() {
//        if (mCanvas == null) {
////            Log.e("PaintImageView", "绘制bitmapCanvas == null");
//            return;
//        }
//        drawCurrentPointPath(mCanvas);
//        if (onPaintListener != null) {
//            onPaintListener.onPaintEnd(lineInfo);
//        }
//        lineInfo = null;
//    }
//
//    private int moveMode;
//
//    private boolean isClick = true;
//
//    public void setViewClick(boolean isClick) {
//        this.isClick = isClick;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        mScaleGestureDetector.onTouchEvent(event);
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                isLongPressMode = false;
//                if (!hideTalk)
//                    resetAllTagState();
//                getParent().requestDisallowInterceptTouchEvent(true);
//                if (mDrawState == HANDWRITE || mDrawState == ERASER) {
//                    if (onPaintListener != null)
//                        onPaintListener.onPaintStart();
//                    isDraw = true;
//                    touch_down(x, y);
//                }
//                mLastX = x;
//                mLastY = y;
//                if (!hideTalk)
//                    postDelayed(this, 1000);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (Math.abs(mLastX - x) > mTouchSlop || Math.abs(mLastY - y) > mTouchSlop) {
//                    if (!hideTalk)
//                        removeCallbacks(this);
//                }
//                if (!hideTalk)
//                    if (isLongPressMode && tagsList.get(currentClickIconIndex).getState() == TagInfo.STATE_TRANSLATE) {
//                        moveTag(x, y);
//                        break;
//                    }
//                if (!isScale) {
//                    if (mDrawState == NONE || (mDrawState == HANDWRITE && event.getPointerCount() == 2)) {
//                        handleScaleMove(event);
//                    } else if (mDrawState == HANDWRITE || mDrawState == ERASER) {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                        if (moveMode != 1)
//                            touch_move(x, y);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("info", "info");
//                if (!hideTalk)
//                    if (mDrawState != HANDWRITE && mDrawState != ERASER) {
//                        if (isLongPressMode) {
//                            TagInfo info = tagsList.get(currentClickIconIndex);
//                            if (clickListener != null)
//                                clickListener.onLongClickUp(info);
//                            break;
//                        }
//                        if (isAddClick) {
//                            isAddClick = false;
//                            break;
//                        }
//                        if (hasView(x, y) && Math.abs(x - mLastX) < floatBmWidth / 2 && Math.abs(y - mLastY) < floatBmHeight / 2
//                                && !isLongPressMode) {
//                            TagInfo info = tagsList.get(currentClickIconIndex);
//                            info.setClick(true);
//                            //点击事件处理
//                            Log.e("info", "被点击了 - " + currentClickIconIndex);
//                            if (clickListener != null)
//                                clickListener.onSessionIconClick(info);
//                            invalidate();
//                        }
//                    }
//            case MotionEvent.ACTION_CANCEL:
//                if (!hideTalk)
//                    removeCallbacks(this);
//                if ((mDrawState == HANDWRITE || mDrawState == ERASER) && moveMode != 1) {
//                    isDraw = false;
//                    touch_up();
//                }
//                moveMode = 0;
//                lastPointerCount = 0;
//                if (listenTouch != null) {
//                    listenTouch.touchUp(event);
//                }
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                moveMode = 1;
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                break;
//        }
//        return isClick;
//    }
//
//    private boolean addTag(float x, float y) {
//        x = x - floatBmWidth / 2;
//        y = y - floatBmHeight / 2;
//        float[] tempF = {x, y};
//        Matrix tempMatrix = new Matrix();
//        mMatrix.invert(tempMatrix);
//        tempMatrix.mapPoints(tempF, new float[]{x, y});
//
//        float xLimitLeft = (getWidth() - getBitmapWidth()) / 2; //左侧限制区域
//        float xLimitRight = getWidth() - ((getWidth() - getBitmapWidth()) / 2) - (floatBmWidth / 2); //右侧限制区域
//        float yLimitTop = (getHeight() - getBitmapHeight()) / 2; //顶部限制区域
//        float yLimitBottom = getHeight() - (getHeight() - getBitmapHeight()) / 2 - floatBmHeight;  // 底部限制区域
//
//        Log.i("info", "X:" + tempF[0] + ", Y:" + tempF[1] + ", L:" + xLimitLeft + ", R:" + xLimitRight + ", T:" + yLimitTop + ", B:" + yLimitBottom);
//        Rect rect = new Rect((int) xLimitLeft, (int) yLimitTop, (int) xLimitRight, (int) yLimitBottom);
//        if (!rect.contains((int) tempF[0], (int) tempF[1])) {
//            return false;
//        }
//
//        PointF pointF = transPointF.display2Logic(tempF[0], tempF[1]);
//        TagInfo info = new TagInfo(getContext());
//        info.setX(pointF.x);
//        info.setY(pointF.y);
//        info.setSenderId(userId);
//        info.setNewTag(true);
//        tagsList.add(info);
//
//        return true;
//    }
//
//    private void moveTag(float x, float y) {
//        x = x - floatBmWidth / 2;
//        y = y - floatBmHeight / 2;
//        float[] tempF = {x, y};
//        Matrix tempMatrix = new Matrix();
//        mMatrix.invert(tempMatrix);
//        tempMatrix.mapPoints(tempF, new float[]{x, y});
//
//        float xLimitRight = getWidth() - ((getWidth() - getBitmapWidth()) / 2) - floatBmWidth; //右侧限制区域
//        float yLimitBottom = getHeight() - (getHeight() - getBitmapHeight()) / 2 - floatBmHeight;  // 底部限制区域
//
//        if (xLimitRight < tempF[0]) {
//            tempF[0] = xLimitRight;
//        }
//
//        if (yLimitBottom < tempF[1]) {
//            tempF[1] = yLimitBottom;
//        }
//
//        PointF pointF = transPointF.display2Logic(tempF[0], tempF[1]);
//        tagsList.get(currentClickIconIndex).setX(pointF.x);
//        tagsList.get(currentClickIconIndex).setY(pointF.y);
//        invalidate();
//    }
//
//    public void deleteTag(TagInfo info) {
//        tagsList.remove(info);
//        invalidate();
//    }
//
//    private void resetAllTagState() {
//        for (TagInfo tagInfo : tagsList) {
//            tagInfo.setState(TagInfo.STATE_INIT);
//        }
//    }
//
//    private int lastPointerCount;
//    private boolean isCanDrag;
//    private float mLastX;
//    private float mLastY;
//
//    private void handleScaleMove(MotionEvent event) {
//        float x = 0, y = 0;
//        final int pointerCount = event.getPointerCount();
//        for (int i = 0; i < pointerCount; i++) {
//            x += event.getX(i);
//            y += event.getY(i);
//        }
//        x = x / pointerCount;
//        y = y / pointerCount;
//        if (pointerCount != lastPointerCount) {
//            isCanDrag = false;
//            mLastX = x;
//            mLastY = y;
//        }
////        Log.i("PaintImageView", "--->Move:" + "-->x:" + x + ",y:" + y + ",LastX" + mLastX + ",LastY" + mLastY);
//        lastPointerCount = pointerCount;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                RectF rectF = getMatrixRectF();
//                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                float dx = x - mLastX;
//                float dy = y - mLastY;
//                if (!isCanDrag) {
//                    isCanDrag = isCanDrag(dx, dy);
//                }
//                if (isCanDrag && getDrawable() != null) {
////                    Log.i("PaintImageView", "--->isCanDrag:" + isCanDrag + ",dx:" + dx + ",dy:" + dy + ",mTouchSlop:" + mTouchSlop);
//
////                    Log.i("PaintImageView", "99999 rectF.left: " + rectF.left + " , rectF.right: " + rectF.right
////                            + " - width " + getWidth() + " , dx " + dx + " , rectF.width " + rectF.width());
//                    isCheckLeftAndRight = isCheckTopAndBottom = true;
//                    if (rectF.width() < getWidth()) { // 如果宽度小于屏幕宽度，则禁止左右移动
//                        dx = 0;
//                        isCheckLeftAndRight = false;
//                    }
//                    if (rectF.height() < getHeight()) {// 如果高度小于雨屏幕高度，则禁止上下移动
//                        dy = 0;
//                        isCheckTopAndBottom = false;
//                    }
////                    Log.i("PaintImageView", "------start dx:" + dx + ",dy:" + dy);
//                    if (dx > 0) {
//                        if (dx > Math.abs(rectF.left) - (mCentreTranX * mScale) && isCheckLeftAndRight) {
//                            dx = Math.abs(rectF.left) - (mCentreTranX * mScale);
//                        }
//                    } else {
////                        Log.i("PaintImageView", "right:" + rectF.right + ",rectF.width:" + rectF.width() + ",Width:" + getWidth() + ",BitmapWidth:" + getBitmapWidth());
//                        if (!overParentBoundsEnable) {
//                            if (dx <= getWidth() - rectF.right) {
//                                dx = getWidth() - rectF.right;
//                            }
//                        }
//                    }
//                    if (dy > 0) {
//                        if (dy > Math.abs(rectF.top) - (mCentreTranY * mScale) && isCheckTopAndBottom) {
//                            dy = Math.abs(rectF.top) - (mCentreTranY * mScale);
//                        }
//                    } else {
////                        Log.i("PaintImageView", "bottom:" + rectF.bottom + ",Height:" + getHeight() + ",BitmapHeight:" + getBitmapHeight());
//                        if (!overParentBoundsEnable) {
//                            if (dy <= getHeight() - rectF.bottom && isCheckTopAndBottom) {
//                                if (rectF.bottom >= getHeight()) {
//                                    dy = getHeight() - rectF.bottom;
//                                } else {
//                                    float[] values = new float[9];
//                                    mMatrix.getValues(values);
//                                    if (getDrawable().getBounds().height() * values[4] < getHeight())
//                                        dy = 0;
//                                }
//                            }
//                        }
//                    }
//
//                    if (getScale() <= 1f) {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    } else {
//                        if (dx >= 0) {//左滑
//                            float tempL = Math.abs(rectF.left) - (mCentreTranX * mScale);
//                            if (tempL <= 0) {
//                                getParent().requestDisallowInterceptTouchEvent(false);
//                            } else {
//                                getParent().requestDisallowInterceptTouchEvent(true);
//                            }
//                        } else {//右滑
//                            float tempR = Math.abs(rectF.right) + (mCentreTranX * mScale);
//                            if (tempR <= getWidth()) {
//                                getParent().requestDisallowInterceptTouchEvent(false);
//                            } else {
//                                getParent().requestDisallowInterceptTouchEvent(true);
//                            }
//                        }
//                    }
//
////                    if ((rectF.left >= 0 && dx > 0) || (rectF.right <= getWidth() && dx < 0)) {
////                        if (mDrawState == HANDWRITE) { //当前为编辑状态，并且已滑动到左边或右边的边界，则不可滑动
////                            dx = 0;
////                            getParent().requestDisallowInterceptTouchEvent(true);
////                        } else {
////                            if (!overParentBoundsEnable)
////                                dx = 0;
////                            getParent().requestDisallowInterceptTouchEvent(false);
////                            Log.i("PaintImageView", "---requestDisallowInterceptTouchEvent0---dx:" + dx + ",dy:" + dy);
////                        }
////                    } else {
////                        float f = getScale();
////                        Log.i("PaintImageView", "---requestDisallowInterceptTouchEvent1---dx:" + dx + ",dy:" + dy + ", Scale:" + f);
////                        if (f <= 1f) {
////                            getParent().requestDisallowInterceptTouchEvent(false);
////                        } else {
////                            if (dx > 0) {
////                                getParent().requestDisallowInterceptTouchEvent(false);
////                            } else {
////                                getParent().requestDisallowInterceptTouchEvent(true);
////                            }
////                        }
////                    }
////                    Log.i("PaintImageView", "------end dx:" + dx + ",dy:" +dy);
//                    if (pointerCount > 2) {
//                        dx = 0;
//                        dy = 0;
//                    }
////                    Log.i("PaintImageView", "------end dx:" + dx + ",dy:" + dy);
//                    if (dx != 0 || dy != 0) {
//                        mMatrix.postTranslate(dx, dy);
//                        invalidate();
//                    }
//                }
//                mLastX = x;
//                mLastY = y;
//                break;
//        }
//    }
//
//    private boolean isCanDrag(float dx, float dy) {
////        return Math.sqrt((dx * dx) + (dy * dy)) >= 0;
//        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
//    }
//
//    private boolean hasView(float x, float y) {
//        float[] tempF = {x, y};
//        Matrix tempMatrix = new Matrix();
//        mMatrix.invert(tempMatrix);
//        tempMatrix.mapPoints(tempF, new float[]{x, y});
//        boolean flag = false;
//        for (int i = 0; i < tagsList.size(); i++) {
//            TagInfo tagInfo = tagsList.get(i);
//            PointF pointF = transPointF.logic2Display(new PointF(tagInfo.getX(), tagInfo.getY()));
//            float lRect = pointF.x;
//            float tRect = pointF.y;
//            float rRect = pointF.x + floatBmWidth;
//            float bRect = pointF.y + floatBmHeight;
//
//            Rect rect = new Rect((int) lRect, (int) tRect, (int) rRect, (int) bRect);
//
//            if (rect.contains((int) tempF[0], (int) tempF[1])) {
//                flag = true;
//                currentClickIconIndex = i;
//                break;
//            }
//        }
//        return flag;
//    }
//
//    //根据id删除某条线
//    private void deleteId(String lineId) {
//        if (mPaintLines.size() > 0) {
////            Log.e("PaintImageView", "执行删除方法");
//            for (int i = 0; i < mPaintLines.size(); i++) {
//                if (TextUtils.equals(mPaintLines.get(i).getLineId(), lineId)) {
//                    mPaintLines.remove(i);
//                    break;
//                }
//            }
//            drawAllPointPath(mCanvas);
//            invalidate();
//        } else {
//            Toast.makeText(AppContext.getAppContext(), "没有信息要撤销哦", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    //根据当前图片的Matrix获得图片的范围
//    public RectF getMatrixRectF() {
//        Matrix matrix = mMatrix;
//        RectF rect = new RectF();
//        Drawable d = getDrawable();
//        if (null != d) {
//            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//            matrix.mapRect(rect);
//        }
//        return rect;
//    }
//
//    private boolean isCheckTopAndBottom = true;
//    private boolean isCheckLeftAndRight = true;
//
//    private boolean isScale = false;
//
//    @Override
//    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//        float scaleFactor = scaleGestureDetector.getScaleFactor();
//        mScale = getScale();
//        if (getScale() < DrawPathCong.MAX_SCALE || scaleFactor < 1.0f) {
//            mMatrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
//            checkMatrixBounds();
//        }
//        invalidate();
//        return true;
//    }
//
//    @Override
//    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
//        isScale = true;
//        return true;
//    }
//
//    @Override
//    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
//        if (mScale < 1f) {
//            mMatrix.setScale(1f, 1f);
//            invalidate();
//        }
//        isCheckLeftAndRight = true;
//        isScale = false;
//    }
//
//    /**
//     * 通过矩阵映射图片上的(0,0)与(0,test1)点,
//     * 获得图片的放大倍数
//     */
//    public float getScale() {
//        return (float) Math.sqrt((float) Math.pow(getValue(mMatrix, Matrix.MSCALE_X), 2) + (float) Math.pow(getValue(mMatrix, Matrix.MSKEW_Y), 2));
//    }
//
//    private float getValue(Matrix matrix, int whichValue) {
//        matrix.getValues(mMatrixValues);
//        return mMatrixValues[whichValue];
//    }
//
//    public Matrix getImageMatrix() {
//        return mMatrix;
//    }
//
//    public void setImageMatrix() {
//        if (mMatrix != null) {
//            mMatrix.setScale(1f, 1f);
//            invalidate();
//        }
//    }
//
//
//    /**
//     * 改变画笔信息,为简化逻辑,使用统一变量记录当前画笔颜色,粗细
//     */
////    private void changePaint() {
////        mPaint = new Paint();
////        mPaint.setAntiAlias(true);
////        mPaint.setDither(true);
////        mPaint.setColor(currntColor);
////        mPaint.setStyle(Paint.Style.STROKE);
////        mPaint.setStrokeJoin(Paint.Join.ROUND);
////        mPaint.setStrokeCap(Paint.Cap.ROUND);
////        mPaint.setStrokeWidth(currentPaintSize);
////    }
//    @Override
//    public void setImageDrawable(Drawable drawable) {
//        super.setImageDrawable(drawable);
//        if (drawable == null) {
//            return;
//        }
//        mBitmapWidth = drawable.getIntrinsicWidth();
//        mBitmapHeight = drawable.getIntrinsicHeight();
//        mCentreTranX = (getWidth() - drawable.getIntrinsicWidth()) / 2;
//        mCentreTranY = (getHeight() - drawable.getIntrinsicHeight()) / 2;
//        transPointF = new TransPointF(mBitmapWidth, mBitmapHeight, mCentreTranX, mCentreTranY);
//        if (mBitmapWidth > 0 || mBitmapHeight > 0) {
//            createBitmap();
//        }
//        if (mPaintLines != null || mPaintLines.size() != 0)
//            invalidate();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (mCanvasBitmap == null) {
//            createBitmap();
//        }
//    }
//
//    private void createBitmap() {
//        if (getWidth() > 0 && getHeight() > 0) {
//            mCanvasBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_4444);
//            mCanvas = new Canvas(mCanvasBitmap);
//        }
//    }
//
//    //####################供外界使用的Api#################################
//    //####################################################################
//
//    /**
//     * 清空
//     */
//    public void clear() { //调用初始化画布函数以清空画布
//        mPaintLines.clear();
//        mRedoPaintLines.clear();
//        invalidate();//刷新
//    }
//
//    private String userId;
//
//    /**
//     * 撤销的核心思想就是将画布清空，
//     * 将保存下来的Path路径最后一个移除掉，
//     * 重新将路径画在画布上面。
//     */
//    public void undo() {
//        if (mPaintLines.size() > 0) {
//            //判断是否是自己画的线
//            int i = mPaintLines.size() - 1;
//            for (; i >= 0; i--) {
//                if (!StringUtil.isEmpty(userId) && userId.equals(mPaintLines.get(i).getLineId())) {
//                    break;
//                }
//            }
//            if (i == -1) {
//                Toast.makeText(AppContext.getAppContext(), "没有信息要撤销了哦", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            LineInfo info = mPaintLines.get(i);
//            mRedoPaintLines.add(info);
//            if (onUndoPaintListener != null) {
//                onUndoPaintListener.onUndoPaint(info);
//            }
//            mPaintLines.remove(i);
//            if (mCanvas != null) {
//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            }
//            invalidate();
//        } else {
//            Toast.makeText(AppContext.getAppContext(), "没有信息要撤销了哦", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void setPaintMessageModel(MessageModel model) {
//        LineInfo info = mPaintLines.get(mPaintLines.size() - 1);
//        info.setLineId(Users.getInstance().getCurrentUser().getEntity().getId());
//        info.setMessageModel(model);
//    }
//
//    public void redo() {
//        if (mRedoPaintLines.size() > 0) {
//            mPaintLines.add(mRedoPaintLines.get(mRedoPaintLines.size() - 1));
//            mRedoPaintLines.remove(mRedoPaintLines.size() - 1);
//            drawAllPointPath(mCanvas);
//            invalidate();
//        } else {
//            Toast.makeText(AppContext.getAppContext(), "没有信息要恢复了哦", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 旋转90,会相叠加
//     */
//    public void rotate() {
//        mCurrentRotate = (mCurrentRotate + 90);
//        mMatrix.postRotate(90, getWidth() / 2, getHeight() / 2);
//        invalidate();
//    }
//
//    /**
//     * 指定旋转的角度
//     *
//     * @param rotate
//     */
//    public void setRotate(int rotate) {
//        mCurrentRotate = rotate;
//        mMatrix.postRotate(0, getWidth() / 2, getHeight() / 2);
//        mMatrix.postRotate(mCurrentRotate, getWidth() / 2, getHeight() / 2);
//        invalidate();
//    }
//
//    public void setDrawInfo(PageInfo pageInfo) {
//        this.pageInfo = pageInfo;
//        this.mPaintLines.clear();
//        this.mPaintLines.addAll(pageInfo.getLineInfos());
//        if (mCanvas != null) {
//            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            drawAllPointPath(mCanvas);
//        }
//        if (transPointF != null)
//            invalidate();
//    }
//
//    public int getBitmapWidth() {
//        return mBitmapWidth;
//    }
//
//    public int getBitmapHeight() {
//        return mBitmapHeight;
//    }
//
//    public float getCentreTranX() {
//        return mCentreTranX;
//    }
//
//    public float getCentreTranY() {
//        return mCentreTranY;
//    }
//
//    //设置为普通笔绘画
//    public void setDrawHandWrite(String choseClose) {
//        mDrawState = HANDWRITE;
//        currntColor = Color.parseColor(choseClose);
//        initPaint();
//    }
//
//    //进入橡皮擦状态...
//    public void setEraserState() {
//        mDrawState = ERASER;
//    }
//
//    /**
//     * 设置当前状态可放大
//     */
//    public void setScaleState() {
//        mDrawState = NONE;
//    }
//
//
//    /**
//     * 设置当前画笔的粗细
//     *
//     * @param strokeWidth
//     */
//    public void setStrokeWidth(int strokeWidth) {
//        this.currentPaintSize = strokeWidth;
////        changePaint();
//    }
//
//    private ScaleType mScaleType = ScaleType.FIT_CENTER;
//
//    @Override
//    public void setScaleType(ScaleType scaleType) {
//        super.setScaleType(scaleType);
//        mScaleType = scaleType;
//    }
//
//    private boolean checkMatrixBounds() {
//        final RectF rect = getMatrixRectF();
//        if (null == rect) {
//            return false;
//        }
//
//        final float height = rect.height(), width = rect.width();
//        float deltaX = 0, deltaY = 0;
//
//        final int viewHeight = getBitmapHeight();
//        if (height <= viewHeight) {
//            switch (mScaleType) {
//                case FIT_START:
//                    deltaY = -rect.top;
//                    break;
//                case FIT_END:
//                    deltaY = viewHeight - height - rect.top;
//                    break;
//                default:
//                    deltaY = (viewHeight - height) / 2 - rect.top;
//                    break;
//            }
//        } else if (rect.top > 0) {
//            deltaY = -rect.top;
//        } else if (rect.bottom < viewHeight) {
//            deltaY = viewHeight - rect.bottom;
//        }
//
//        final int viewWidth = getBitmapWidth();
//        if (width <= viewWidth) {
//            switch (mScaleType) {
//                case FIT_START:
//                    deltaX = -rect.left;
//                    break;
//                case FIT_END:
//                    deltaX = viewWidth - width - rect.left;
//                    break;
//                default:
//                    deltaX = (viewWidth - width) / 2 - rect.left;
//                    break;
//            }
//        } else if (rect.left > 0) {
//            deltaX = -rect.left;
//        } else if (rect.right < viewWidth) {
//            deltaX = viewWidth - rect.right;
//        } else {
//        }
//
//        if (deltaX < 0 || deltaY < 0)
//            mMatrix.postTranslate(deltaX, deltaY);
//        return true;
//    }
//
//    public void setOverParentBoundsEnable(boolean enable) {
//        this.overParentBoundsEnable = enable;
//    }
//
//}