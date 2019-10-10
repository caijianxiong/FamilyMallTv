package c.liyueyun.mjmall.tv.tv.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.manage.PrefManage;


/**
 * Created by SongJie on 07/20 0020.
 */
public class DialogCommonChoice extends Dialog {
    private TextView negaButton, positButton;
    private TextView tv_message, tv_message02;
    private PrefManage prefManage;


    public DialogCommonChoice(Context context) {
        super(context);
    }

    public DialogCommonChoice(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 初始化焦点
     */
    public void initBtnFocus(boolean isNegaBtn) {
        if (isNegaBtn) {
            negaButton.requestFocus();
        } else {
            positButton.requestFocus();
        }
    }

    public void setMessage(String message) {
        if (tv_message != null) {
            tv_message.setText(message);
        }
    }

    public void setMessage02(String message) {
        if (tv_message02 != null && !Tool.isEmpty(message)) {
            tv_message02.setVisibility(View.VISIBLE);
            tv_message02.setText(message);
        } else {
            if (tv_message02 != null)
                tv_message02.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private Context mContext;
        private String positiveButtonText;
        private String negativeButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private DialogCommonChoice dialog;

        public Builder(Context context) {
            this.mContext = context;
        }

        public void setPositiveButton(String positiveButtonText, OnClickListener listener) {
            if (!Tool.isEmpty(positiveButtonText)) {
                this.positiveButtonText = positiveButtonText;
            }
            this.positiveButtonClickListener = listener;
        }

        public void setNegativeButton(String negativeButtonText, OnClickListener listener) {
            if (!Tool.isEmpty(negativeButtonText)) {
                this.negativeButtonText = negativeButtonText;
            }
            this.negativeButtonClickListener = listener;
        }

        public DialogCommonChoice create() {
            dialog = new DialogCommonChoice(mContext, R.style.DialogScaleStyle);
            dialog.setCancelable(true);//返回键
            //设置全屏显示
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.dialog_choice02);
            WindowManager.LayoutParams mParams = window.getAttributes();
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(mParams);
            dialog.prefManage = new PrefManage(mContext);
            dialog.positButton = (TextView) window.getDecorView().findViewById(R.id.positiveButton);
            dialog.negaButton = (TextView) window.getDecorView().findViewById(R.id.negativeButton);
            dialog.tv_message = (TextView) window.getDecorView().findViewById(R.id.message);
            dialog.tv_message02 = (TextView) window.getDecorView().findViewById(R.id.message02);
            dialog.positButton.setText(positiveButtonText);
            dialog.negaButton.setText(negativeButtonText);
            dialog.negaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });

            dialog.positButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (positiveButtonClickListener != null) {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });

            return dialog;
        }
    }
}
