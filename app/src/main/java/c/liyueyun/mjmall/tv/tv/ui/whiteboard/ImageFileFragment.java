package c.liyueyun.mjmall.tv.tv.ui.whiteboard;//package liyueyun.business.tv.ui.whiteboard;
//
//import android.graphics.Color;
//import android.graphics.PointF;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.liyueyun.knocktv.entity.room.RoomWhiteBoardsEntity;
//import com.y2w.uikit.utils.StringUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import knocktv.model.Point;
//import knocktv.ui.activity.imagebrowse.paint.PaintImageView;
//import knocktv.ui.activity.imagebrowse.paint.bean.LineInfo;
//import knocktv.ui.activity.imagebrowse.paint.bean.PageInfo;
//import knocktv.ui.activity.imagebrowse.paint.bean.PointInfo;
//import knocktv.ui.activity.imagebrowse.paint.util.DrawPathCong;
//import knocktv.ui.base.BaseFragment;
//import knocktv.ui.dialog.PaintColorView;
//import liyueyun.tb.business.R;
//import uk.co.senab.photoview.PhotoView;
//
//public class ImageFileFragment extends BaseFragment<ImageFilePresenter, IImageFileView> implements IImageFileView {
//
//    private final static String TAG = ImageFileFragment.class.getSimpleName();
//
//    private final int HANDLER_SHOW_IMAGE = 1;
//
//    public static ImageFileFragment newInstance() {
//        ImageFileFragment newFragment = new ImageFileFragment();
//        Bundle bundle = new Bundle();
//        newFragment.setArguments(bundle);
//        return newFragment;
//    }
//
//    private PaintImageView mPhotoView;
////    private PhotoView mPhotoView;
//
//    private String mFilePath;
//
//    private boolean isClick = true;
//
//    private OnPaintListener mOnPaintListener;
//
//    private Handler vHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case HANDLER_SHOW_IMAGE:
//                    if (mPhotoView != null) {
//                        mPhotoView.setViewClick(isClick);
//                        mPhotoView.setClickable(isClick);
//                        mPhotoView.setEnabled(isClick);
//                        Glide.with(getActivity())
//                                .load(mFilePath)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .dontAnimate()//解决展位图大小影响
//                                .skipMemoryCache(true)
//                                .fitCenter()
//                                .placeholder(R.drawable.img_default)
//                                .error(R.drawable.img_default)
//                                .into(mPhotoView);
//                    }
//                    break;
//            }
//            return false;
//        }
//    });
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected ImageFilePresenter initPresenter() {
//        return new ImageFilePresenter(getActivity());
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_imagefile;
//    }
//
//    @Override
//    protected void initViews(View view) {
//        mPhotoView = (PaintImageView) view.findViewById(R.id.iv_photoview);
//        mPhotoView.setOnPaintListener(new PaintImageView.onPaintListener() {
//            @Override
//            public void onPaintEnd(LineInfo lineInfo) {
//                ArrayList<PointInfo> infos = lineInfo.getCurrentPointLists();
//                if (infos == null || infos.size() <= 1) {
//                    return;
//                }
//                List<Point> points = new ArrayList<>();
//                for (PointInfo info : infos) {
//                    PointF f = info.getCrtlPointF();
//                    points.add(new Point(f.x / 100, f.y / 100));
//                }
//                String type = "pencil";
//                JSONObject js = new JSONObject();
//                if (lineInfo.getType() == 2) {
//                    js.put("color", "#000000");
//                    type = "eraser";
//                } else {
//                    js.put("color", getColor(lineInfo.getColor()));
//                }
//                js.put("points", JSON.toJSON(points));
//                Log.i(TAG, "--->type:" + lineInfo.getType() + ", points:" + js.toString());
//                if (mOnPaintListener != null) {
//                    mOnPaintListener.onPaint(type, js.toString());
//                }
//            }
//
//            @Override
//            public void onPaintStart() {
//
//            }
//        });
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        if (!StringUtil.isEmpty(mFilePath)) {
//            vHandler.sendEmptyMessage(HANDLER_SHOW_IMAGE);
//        }
//    }
//
//    @Override
//    protected String getTagName() {
//        return ImageFileFragment.class.getSimpleName();
//    }
//
//    public void showImage(String url) {
//        mFilePath = url;
//        vHandler.sendEmptyMessage(HANDLER_SHOW_IMAGE);
//    }
//
//    public void showFileImage(String filePath) {
//        if (StringUtil.isEmpty(mFilePath)) {
//            mFilePath = filePath;
//            vHandler.sendEmptyMessage(HANDLER_SHOW_IMAGE);
//        }
//    }
//
//    public void setViewClick(boolean isClick) {
//        this.isClick = isClick;
//    }
//
//    public void setOnPaintListener(OnPaintListener listener) {
//        this.mOnPaintListener = listener;
//    }
//
//    public void setDrawHandWrite(boolean isEraser, String choseClose) {
//        if (mPhotoView != null) {
//            if (isEraser) {
//                mPhotoView.setEraserState();
//            } else if (!StringUtil.isEmpty(choseClose)) {
//                mPhotoView.setDrawHandWrite(choseClose);
//            } else {
//                mPhotoView.setScaleState();
//            }
//        }
//    }
//
//    public void setWhiteBoardsDraw(List<RoomWhiteBoardsEntity> entities) {
//        ArrayList<LineInfo> lineInfos = new ArrayList<>();
//        for (RoomWhiteBoardsEntity entity : entities) {
//            JSONObject js = JSON.parseObject(entity.getData());
//            if (js == null) {
//                continue;
//            }
//            String points = js.getString("points");
//            List<Point> lst = JSON.parseArray(points, Point.class);
//            ArrayList<PointInfo> pointInfos = new ArrayList<>();
//            float preX = 0;
//            float preY = 0;
//            for (int j = 0; j < lst.size(); j++) {
//                Point p = lst.get(j);
//                if (j == 0) {
//                    PointF f = new PointF();
//                    f.x = Float.valueOf(p.x) * 100;
//                    f.y = Float.valueOf(p.y) * 100;
//                    pointInfos.add(new PointInfo(null, f, j));
//                    preX = f.x;
//                    preY = f.y;
//                } else {
//                    PointF f1 = new PointF();
//                    f1.x = Float.valueOf(p.x) * 100;
//                    f1.y = Float.valueOf(p.y) * 100;
//                    PointF f2 = new PointF();
//                    f2.x = getBesPoint(preX, f1.x);
//                    f2.y = getBesPoint(preY, f1.y);
//                    pointInfos.add(new PointInfo(f2, f1, j));
//                    preX = f1.x;
//                    preY = f1.y;
//                }
//            }
//            String color = js.getString("color");
//            boolean isEraser = "eraser".equals(entity.getType());
//            int type = isEraser ? 2 : 1;
//            int strokeWidth = isEraser ? DrawPathCong.mEraseSize : DrawPathCong.mStartSize;
//            if (pointInfos.size() > 1) {
//                lineInfos.add(new LineInfo(entity.getFrom(), type, Color.parseColor(color), strokeWidth, pointInfos));
//            }
//        }
//        if (mPhotoView != null)
//            mPhotoView.setDrawInfo(new PageInfo(0, lineInfos, ""));
//    }
//
//    // 根据上一个点和原点的坐标获得贝斯尔的控制点
//    private float getBesPoint(float prex, float x) {
//        return (prex + x) / 2;
//    }
//
//    private String getColor(int color) {
//        for (String str : PaintColorView.paintColors) {
//            if (color == Color.parseColor(str)) {
//                return str;
//            }
//        }
//        return "";
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.i(TAG, "--->onDestroyView");
//        if (mPhotoView != null) {
//            Glide.clear(mPhotoView);
//        }
//    }
//
//    public interface OnPaintListener {
//        void onPaint(String type, String data);
//    }
//}
