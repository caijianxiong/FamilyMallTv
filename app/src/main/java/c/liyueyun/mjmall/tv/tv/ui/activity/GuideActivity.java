package c.liyueyun.mjmall.tv.tv.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.base.logUtil;
import c.liyueyun.mjmall.tv.base.manage.UserManage;
import c.liyueyun.mjmall.tv.tv.ui.base.BaseNormalActivity;
import c.liyueyun.mjmall.tv.tv.ui.widget.dialog.DialogChoice;


public class GuideActivity extends BaseNormalActivity {

    private String TAG = "GuideActivity";
    private Context context = this;
    private ImageView img_invite_erw;
    private String myId;


    private String url;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (context == null) return false;
            switch (msg.what) {
                case MyConstant.BE_INVITED_SUCCESS:
                    logUtil.d_2(TAG, "准备跳转到首页");
                    finish();
                    break;
            }
            return false;
        }
    });


    @Override
    protected int setLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews() {
        img_invite_erw = (ImageView) findViewById(R.id.img_invite_erw);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }


    @Override
    public void onBackPressed() {
        DialogChoice.Builder builder = new DialogChoice.Builder(this);
        builder.setTitle("即将退出每家优选");
        builder.setPositiveButton("残忍离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstance().exit();
            }
        });
        builder.setNegativeButton("留下看看", null);
        builder.create().show();
    }
}
