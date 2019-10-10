package c.liyueyun.mjmall.tv.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import c.liyueyun.mjmall.tv.R;


/**
 * Created by SongJie on 08/15 0015.
 */
public class MyProgressDialog extends Dialog {
    private Context mConext;
    public ImageView spaceshipImage;
    private Animation hyperspaceJumpAnimation;
    public TextView tv_mydialog_text;

    public MyProgressDialog(Context context) {
        super(context);
        mConext = context;
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mConext, R.anim.load_animation);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mConext, R.anim.load_animation);
    }

    @Override
    public void show() {
        // 加载动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        try {
            super.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void cleanAnim(){
        spaceshipImage.clearAnimation();
    }

    public static class Builder {
        public MyProgressDialog CreateDialog(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.dialog_view);// 加载布局
            MyProgressDialog loadingDialog = new MyProgressDialog(context, R.style.loading_dialog);// 创建自定义样式dialog
            loadingDialog.spaceshipImage = (ImageView) view.findViewById(R.id.backImage);
            loadingDialog.tv_mydialog_text = (TextView) view.findViewById(R.id.tv_mydialog_text);
            loadingDialog.setCancelable(true);
            loadingDialog.setContentView(layout, new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));// 设置布局
            return loadingDialog;
        }
    }

}