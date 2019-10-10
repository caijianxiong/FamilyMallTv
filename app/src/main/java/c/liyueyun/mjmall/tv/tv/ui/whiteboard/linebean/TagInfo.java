package c.liyueyun.mjmall.tv.tv.ui.whiteboard.linebean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public class TagInfo implements Serializable {
    private static final long serialVersionUID = 4714950430618697568L;

    public static final int STATE_INIT = 0;
    public static final int STATE_TRANSLATE = 1;

    private String mid;//消息Id
    private String avatarUrl;//发送者头像
    private String senderId;//发送者Id
    private String fileId;//文件id
    private int pageId;//页码
    private float x; //左上角的坐标
    private float y;
    private int state; //当前状态
    private boolean isClick = false;

    private RectF headRect;

    private Bitmap headBitmap;
    private boolean isNewTag = false;//是否是新创建标签
    private Context context;

    private Handler mHandler = new Handler();

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public TagInfo(Context context) {
        this.context = context;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Bitmap getHeadBitmap() {
        return headBitmap;
    }

    public RectF getHeadRect() {

        return headRect;
    }

    public void setHeadRect(RectF headRect) {
        this.headRect = headRect;
    }


    public boolean isNewTag() {
        return isNewTag;
    }

    public void setNewTag(boolean newTag) {
        isNewTag = newTag;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
        if (updataView != null)
            updataView.head();
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TagInfo.updataView getUpdataView() {
        return updataView;
    }

//    public void setUpdataView(TagInfo.updataView updataView) {
//        this.updataView = updataView;
//        if (!StringUtil.isEmpty(senderId)) {
//            Users.getInstance().getRemote().userGet(senderId, new Back.Result<User>() {
//                @Override
//                public void onSuccess(final User user) {
//                    avatarUrl = user.getEntity().getAvatarUrl();
//                    if (!StringUtil.isEmpty(avatarUrl) && !avatarUrl.contains("http://")) {
//                        avatarUrl = Urls.User_Messages_File_DownLoad + avatarUrl;
//                        String token = Users.getInstance().getCurrentUser().getToken();
//                        if (avatarUrl.indexOf("?") >= 0)
//                            avatarUrl += "&";
//                        else
//                            avatarUrl += "?";
//                        avatarUrl += "access_token=" + token;
//                    }
//                    if (TagInfo.this.updataView != null) {
//                        final int wh = (int) (headRect.right - headRect.left);
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Glide.with(context).load(avatarUrl)
//                                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).override(wh, wh).into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                        Log.e("PaintImageView1", "bitmap");
//                                        if (headBitmap != null)
//                                            return;
//                                        getCircleBm(resource);
//                                        if (headBitmap != null)
//                                            TagInfo.this.updataView.head();
//                                    }
//                                });
//
//                            }
//                        });
//
//
//                    }
//                }
//
//                @Override
//                public void onError(int Code, String error) {
//                }
//            });
//        } else {
//
//        }
//    }

    private void getCircleBm(Bitmap resource) {
        headBitmap = resource;
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(headBitmap.getWidth(), headBitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError om) {
            om.printStackTrace();
            headBitmap = null;
            return;
        }
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, headBitmap.getWidth(), headBitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(headBitmap, rect, rect, paint);
        headBitmap = output;
    }

    private updataView updataView;

    public interface updataView {
        void head();
    }
}
