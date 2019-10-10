package c.liyueyun.mjmall.tv.tv.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.Tool;


/**
 * Created by songjie on 028 11/28.
 */

public class DialogError extends Dialog {
    private Context mContext;
    private TextView tv_dialogerror_title;
    private Button positiveButton;
    private OnBtnListener onBtnListener;

    public DialogError(Context context) {
        super(context);
        mContext = context;
    }

    public DialogError(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    /**
     *  设置窗口提示文字，必须设置
     */
    public void setTitle(String title) {
        if(!Tool.isEmpty(title)){
            tv_dialogerror_title.setText(title);
        }
    }

    public void setButtonText(String text) {
        positiveButton.setText(text);
    }

    @Override
    public void show() {
        try {
            super.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 设置返回监听
     */
    public void setOnBtnListener(OnBtnListener listener){
        this.onBtnListener = listener;
    }

    private OnBtnListener getListener(){
        return this.onBtnListener;
    }
    public static class Builder {
        private DialogError dialog;

        public DialogError create(Context mContext) {
            dialog = new DialogError(mContext, R.style.DialogScaleStyle);
            dialog.setCancelable(false);//返回键无效
            //设置全屏显示
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.dialog_error);
            WindowManager.LayoutParams mParams = window.getAttributes();
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(mParams);
            //message
            dialog.tv_dialogerror_title = (TextView) window.getDecorView().findViewById(R.id.tv_dialogerror_msg);
            //确定键
            dialog.positiveButton = (Button) window.getDecorView().findViewById(R.id.btn_dialogerror_Button);
            dialog.positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(dialog.getListener()!=null) {
                        dialog.getListener().onClick();
                    }
                    dialog.dismiss();
                }
            });
            return dialog;
        }
    }

    public interface OnBtnListener {
        void onClick();
    }
}