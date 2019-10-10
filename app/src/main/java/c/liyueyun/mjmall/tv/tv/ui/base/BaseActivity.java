package c.liyueyun.mjmall.tv.tv.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.tendcloud.tenddata.TCAgent;
import java.util.ArrayList;
import java.util.List;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.widget.MyProgressDialog;
import c.liyueyun.mjmall.tv.tv.ui.widget.dialog.DialogError;


/**
 * Created by Administrator on 2018/6/29 0029.
 */

public abstract class BaseActivity<P extends IBasePresenter<V>, V extends IBaseView> extends AppCompatActivity implements IBaseView {
    protected Context mContext;
    private PermissionListener mlistener;
    protected P presenter;
    private MyProgressDialog progressDialog;
    private DialogError dialogError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().setBackgroundDrawable(null);

        MyApplication.getInstance().addActivity(this);
        mContext = this;
        setContentView(setLayoutId());
        presenter = initPresenter();
        presenter.attachView((V) this);
        initViews();
        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        if (progressDialog != null) {
            progressDialog.cleanAnim();
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (dialogError != null && dialogError.isShowing())
            dialogError.dismiss();
    }

    protected void showActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected abstract int setLayoutId();

    protected abstract P initPresenter();

    protected abstract void initViews();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this,mContext.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this,mContext.getClass().getSimpleName());
    }


    /**
     * 权限申请
     *
     * @param permissions 待申请的权限集合
     * @param listener    申请结果监听事件
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        this.mlistener = listener;

        if (Build.VERSION.SDK_INT < 23) {
            listener.onGranted();
            return;
        }
        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else {  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {  //用户拒绝权限为空
                        if (mlistener != null)
                            mlistener.onGranted();
                    } else {  //不为空
                        //回调授权失败的接口
                        if (mlistener != null)
                            mlistener.onDenied(deniedPermissions);
                        //回调授权成功的接口
                        if (mlistener != null)
                            mlistener.onGranted(grantedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading(final String msg, final boolean isCanelListener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    MyProgressDialog.Builder builder = new MyProgressDialog.Builder();
                    progressDialog = builder.CreateDialog(BaseActivity.this);
                }
                progressDialog.tv_mydialog_text.setText(msg);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        if (isCanelListener) {
                            loadingCanelListener();
                        }
                    }
                });
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void showErrorDialog(final String errorStr, final boolean isBtnListener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!Tool.isEmpty(errorStr)) {
                    if (dialogError == null) {
                        DialogError.Builder builder = new DialogError.Builder();
                        dialogError = builder.create(mContext);
                    }
                    dialogError.setTitle(errorStr);
                    dialogError.setOnBtnListener(new DialogError.OnBtnListener() {
                        @Override
                        public void onClick() {
                            dialogError.dismiss();
                            if (isBtnListener) {
                                errorBtnListener();
                            }
                        }
                    });
                    if (!dialogError.isShowing()) {
                        dialogError.show();
                    }
                }
            }
        });
    }

    @Override
    public void showMsgToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 权限回调接口
     */
    public interface PermissionListener {
        //授权成功
        void onGranted();

        //授权部分
        void onGranted(List<String> grantedPermission);

        //拒绝授权
        void onDenied(List<String> deniedPermission);
    }
}
