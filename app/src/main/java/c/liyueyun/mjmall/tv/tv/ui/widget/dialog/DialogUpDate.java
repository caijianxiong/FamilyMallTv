package c.liyueyun.mjmall.tv.tv.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.manage.PrefManage;


/**
 * Created by SongJie on 07/20 0020.
 */
public class DialogUpDate extends Dialog {
    private Button negaButton, positButton;
    private TextView tv_message;
    private PrefManage prefManage;
    private String message;
    private onUpdateDialogListener onUpdateDialogListener;

    public void setOnUpdateDialogListener(DialogUpDate.onUpdateDialogListener onUpdateDialogListener) {
        this.onUpdateDialogListener = onUpdateDialogListener;
    }

    public DialogUpDate(Context context) {
        super(context);
    }

    public DialogUpDate(Context context, int theme) {
        super(context, theme);
    }

    public void setMessage(String message, String posText, String negaText) {
        this.message = message;
        if (tv_message != null) {
            tv_message.setText(message);
        }
        if (negaButton != null) {
            negaButton.setText(negaText);
        }
        if (positButton != null) {
            positButton.setText(posText);
        }
    }

    public void discoverNewVersion() {
        negaButton.setVisibility(View.VISIBLE);
        positButton.setVisibility(View.VISIBLE);
    }


    public void downloading() {
        negaButton.setVisibility(View.GONE);
        positButton.setVisibility(View.VISIBLE);
    }


    public static class Builder {
        private Context mContext;
        private DialogUpDate dialog;

        public Builder(Context context) {
            this.mContext = context;
        }


        public DialogUpDate create() {
            dialog = new DialogUpDate(mContext, R.style.DialogScaleStyle);
            dialog.setCancelable(false);//返回键
            //设置全屏显示
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.dialog_update_layout);
            WindowManager.LayoutParams mParams = window.getAttributes();
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(mParams);
            dialog.prefManage = new PrefManage(mContext);
            dialog.positButton = (Button) window.getDecorView().findViewById(R.id.positiveButton);
            dialog.negaButton = (Button) window.getDecorView().findViewById(R.id.negativeButton);
            dialog.tv_message = (TextView) window.getDecorView().findViewById(R.id.message);

            dialog.positButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialog.onUpdateDialogListener != null) {
                        dialog.onUpdateDialogListener.onButtonListener(dialog.positButton.getText().toString());
                    }
                }
            });

            dialog.negaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialog.onUpdateDialogListener != null) {
                        dialog.onUpdateDialogListener.onButtonListener(dialog.negaButton.getText().toString());
                    }
                }
            });

            return dialog;
        }
    }

    public interface onUpdateDialogListener {
        void onButtonListener(String text);
    }
}
