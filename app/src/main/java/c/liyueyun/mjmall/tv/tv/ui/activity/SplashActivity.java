package c.liyueyun.mjmall.tv.tv.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.tv.ui.activity.mall.MallHomeActivity;
import c.liyueyun.mjmall.tv.tv.ui.base.BaseNormalActivity;


public class SplashActivity extends BaseNormalActivity {
    private String TAG = "SplashActivity";
    private RelativeLayout rlay_splash;
    private boolean isCanGo;

    private final int CAN_GO = 10000;
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstant.IS_LOGIN:
                    gotoMain();
                    break;
                case CAN_GO:
                    isCanGo = true;
                    startActivity(new Intent(SplashActivity.this, MallHomeActivity.class));
                    finish();
                    break;
            }
            return false;
        }
    });
    private int isJoinFamily;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        rlay_splash = (RelativeLayout) findViewById(R.id.rlay_splash);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        isCanGo = false;
        isJoinFamily = -1;
        ImageView logoView = (ImageView) findViewById(R.id.iv_splash_logo);
        logoView.setVisibility(View.GONE);
        gotoMain();
        myHandler.sendEmptyMessageDelayed(CAN_GO, 1500);
    }

    private void gotoMain() {
    }

    private void goMain() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null)
            myHandler.removeCallbacksAndMessages(null);
    }
}
