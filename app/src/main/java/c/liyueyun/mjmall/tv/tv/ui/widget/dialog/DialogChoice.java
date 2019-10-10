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
import android.widget.RelativeLayout;
import android.widget.TextView;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;


/**
 * Created by SongJie on 07/20 0020.
 */
public class DialogChoice extends Dialog {
    private Button negaButton,positButton;

    public DialogChoice(Context context) { super(context); }

    public DialogChoice(Context context, int theme) { super(context, theme); }

    /**
     * 初始化焦点
     */
    public void initBtnFocus(boolean isNegaBtn){
        if(isNegaBtn){
            negaButton.requestFocus();
        }else{
            positButton.requestFocus();
        }
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPositiveButton(String positiveButtonText, OnClickListener listener) {
            if(!Tool.isEmpty(positiveButtonText)) {
                this.positiveButtonText = positiveButtonText;
            }
            this.positiveButtonClickListener = listener;
        }

        public void setNegativeButton(String negativeButtonText, OnClickListener listener) {
            if(!Tool.isEmpty(negativeButtonText)) {
                this.negativeButtonText = negativeButtonText;
            }
            this.negativeButtonClickListener = listener;
        }

        public DialogChoice create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final DialogChoice dialog = new DialogChoice(context, R.style.DialogScaleStyle);
            View layout = inflater.inflate(R.layout.dialog_choice, null);
            dialog.addContentView(layout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            //设置全屏
            Window mWindow = dialog.getWindow();
            //设置dialog动画
//            assert mWindow != null;
//            mWindow.setWindowAnimations(R.style.PopWindowAnimStyle);
            WindowManager.LayoutParams mParams = mWindow.getAttributes();
            mParams.alpha = 1f;
            mParams.gravity = Gravity.CENTER_HORIZONTAL;
            mParams.width = MyApplication.getAppContext().getResources().getDisplayMetrics().widthPixels/2;
            mParams.height = MyApplication.getAppContext().getResources().getDisplayMetrics().heightPixels*2/5;
            mParams.y = 0;
            mParams.x = 0;
            mWindow.setAttributes(mParams);
//            mWindow.getDecorView().setBackgroundColor(Color.MAGENTA);
            mWindow.getDecorView().setPadding(0,0,0,0);
            mWindow.getDecorView().setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels);

            // set the confirm button
            dialog.positButton = (Button) layout.findViewById(R.id.positiveButton);
            if (positiveButtonText != null) {
                dialog.positButton.setText(positiveButtonText);
            }
            dialog.positButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    if (positiveButtonClickListener != null) {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
            // set the cancel button
            dialog.negaButton = (Button) layout.findViewById(R.id.negativeButton);
            if (negativeButtonText != null) {
                dialog.negaButton.setText(negativeButtonText);
            }
            dialog.negaButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });
            // set the dialog title

            if (!Tool.isEmpty(title)){
                ((TextView) layout.findViewById(R.id.title)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }

            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
