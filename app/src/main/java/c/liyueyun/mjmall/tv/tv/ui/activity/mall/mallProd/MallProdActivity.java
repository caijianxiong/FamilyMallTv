package c.liyueyun.mjmall.tv.tv.ui.activity.mall.mallProd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyErrorMessage;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallProdResult;
import c.liyueyun.mjmall.tv.base.task.CreateQRcodeAysncTask;
import c.liyueyun.mjmall.tv.tv.manager.ImageHandler;
import c.liyueyun.mjmall.tv.tv.ui.activity.VideoActivity;
import c.liyueyun.mjmall.tv.tv.ui.base.BaseNormalActivity;


public class MallProdActivity extends BaseNormalActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private String prodId;
    private LayoutInflater inflater;

    private ImageView iv_mallprod_poster, iv_mallprod_qrcode;
    private TextView tv_mallprod_name, tv_mallprod_info, tv_mallprod_price, tv_mallprod_reprice, tv_mallprod_tel;
    private LinearLayout llay_mallprod_list;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_mall_prod;
    }

    @Override
    protected void initViews() {
        mContext = this;
        inflater = LayoutInflater.from(mContext);
        iv_mallprod_poster = (ImageView) findViewById(R.id.iv_mallprod_poster);
        iv_mallprod_qrcode = (ImageView) findViewById(R.id.iv_mallprod_qrcode);
        iv_mallprod_qrcode = (ImageView) findViewById(R.id.iv_mallprod_qrcode);
        tv_mallprod_name = (TextView) findViewById(R.id.tv_mallprod_name);
        tv_mallprod_info = (TextView) findViewById(R.id.tv_mallprod_info);
        tv_mallprod_price = (TextView) findViewById(R.id.tv_mallprod_price);
        tv_mallprod_reprice = (TextView) findViewById(R.id.tv_mallprod_reprice);
        tv_mallprod_tel = (TextView) findViewById(R.id.tv_mallprod_tel);
        llay_mallprod_list = (LinearLayout) findViewById(R.id.llay_mallprod_list);
        showLoading("加载中...");
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //加横线
        tv_mallprod_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //加上这个属性，字体更清晰一些
        prodId = getIntent().getStringExtra("prodId");
        MyApplication.getInstance().getmApi().getMallTemplate().getProdInfo(prodId, new MyCallback<MallProdResult>() {
            @Override
            public void onError(MyErrorMessage msg) {
                hideLoading();
            }

            @Override
            public void onSuccess(final MallProdResult response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        refreshData(response);
                    }
                });
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 刷新数据显示
     */
    private void refreshData(MallProdResult mallProd) {
        tv_mallprod_name.setText(Html.fromHtml(mallProd.getTitle()));
        tv_mallprod_info.setText(Html.fromHtml(mallProd.getDesc()));
        tv_mallprod_price.setText("原价: ¥" + mallProd.getOriPrice());
        tv_mallprod_reprice.setText(" ¥" + mallProd.getPrice());
        tv_mallprod_tel.setText("客服电话: " + mallProd.getTel());

        llay_mallprod_list.removeAllViews();
        boolean loadMaxImg = false;
        for (final MallProdResult.Gallery gallery : mallProd.getGallery()) {
            View itemView = inflater.inflate(R.layout.mall_prod_item, null);
            llay_mallprod_list.addView(itemView, new ViewGroup.LayoutParams(Tool.getDimenWidth(mContext, 120), Tool.getDimenhight(mContext, 120)));
            final ImageView imgView = (ImageView) itemView.findViewById(R.id.iv_mallproditem_img);
            if (gallery.getType().equals("image")) {
                logUtil.d_2(TAG, "详情图片：" + gallery.getUrl());
                Glide.with(mContext)
                        .load(gallery.getUrl())
                        .override(75, 75)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgView);
                if (!loadMaxImg) {
                    loadMaxImg = true;
                    Glide.with(mContext)
                            .load(ImageHandler.getFit640(gallery.getUrl()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(iv_mallprod_poster);
                    itemView.requestFocus();
                }
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap videoBit = Tool.getVideoThumb(gallery.getUrl());
                        createVideoImg(videoBit);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgView.setImageBitmap(videoBit);
                            }
                        });
                    }
                }).start();
            }
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && gallery.getType().equals("image")) {
                        Glide.with(mContext)
                                .load(gallery.getUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_mallprod_poster);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gallery.getType().contains("video")) {
                        Intent intent = new Intent();
                        intent.putExtra("url", gallery.getUrl());
                        intent.setClass(MyApplication.getAppContext(), VideoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(intent);
                    }
                }
            });
        }
        //二维码
        String str = mallProd.getUrl();
        CreateQRcodeAysncTask qRcodeAysncTask = new CreateQRcodeAysncTask(str, null, new CreateQRcodeAysncTask.OnCreateListener() {
            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onSuccess(final Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_mallprod_qrcode.setImageBitmap(bitmap);
                    }
                });
            }
        });
        qRcodeAysncTask.setLogoId(R.mipmap.logo_wx);
        qRcodeAysncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private Bitmap createVideoImg(Bitmap videoImgBit) {
        Bitmap videoIconBit = BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(), R.mipmap.video_play_icon);
        if (videoIconBit != null) {
            //获取图片的宽高
            int srcWidth = videoImgBit.getWidth();
            int srcHeight = videoImgBit.getHeight();
            int logoWidth = videoIconBit.getWidth();
            int logoHeight = videoIconBit.getHeight();
            //logo大小为二维码整体大小的1/5
            float scaleFactor = srcWidth * 1.0f / 3 / logoWidth;
            Canvas canvas = new Canvas(videoImgBit);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(videoIconBit, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        }
        return videoImgBit;
    }

}
