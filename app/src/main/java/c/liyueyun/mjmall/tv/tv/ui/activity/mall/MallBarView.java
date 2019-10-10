package c.liyueyun.mjmall.tv.tv.ui.activity.mall;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import c.liyueyun.mjmall.tv.R;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallHomeMenuResult;


/**
 * Created by SongJie on 10/19 0019.
 */
public class MallBarView extends LinearLayout {
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private List<TextView> tvMenubarList;
    private OnItemSelectListener onItemSelectListener;
    private int selectPos;
    private Animation animationMax;
    private LayoutInflater inflater;
    private boolean isLoseFocus = false;
    private boolean isForceFocus = false;

    private final int LOSE_FOCUS = 10000;
    private final int FORCE_FOCUS = 10001;
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case LOSE_FOCUS:
                    isLoseFocus = true;
                    break;
                case FORCE_FOCUS:
                    isForceFocus = false;
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    //监听各item的focus的监听器
    private OnFocusChangeListener barFocusListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isHas) {
//            logUtil.d_3(TAG,"onFocusChange view = "+view+",isHas = "+isHas);
            int changePos = tvMenubarList.indexOf(view);
            if(isHas) {
//                logUtil.d_3(TAG,changePos+"获得焦点");
                for(int i=0;i<tvMenubarList.size();i++){
                    tvMenubarList.get(i).setSelected(false);
                }
                if(isLoseFocus){
                    isLoseFocus = false;
                    SelectRequestFocus(selectPos);
                }else {
                    for(int i=0;i<tvMenubarList.size();i++){
                        tvMenubarList.get(i).clearAnimation();
                    }
                    if (selectPos != changePos && changePos != -1) {
                        selectPos = changePos;
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(selectPos);
                            isForceFocus = true;
                            myHandler.removeMessages(FORCE_FOCUS);
                            myHandler.sendEmptyMessageDelayed(FORCE_FOCUS,200);
                        }
                    }
                    view.startAnimation(animationMax);
                }
                myHandler.removeMessages(LOSE_FOCUS);
            }else{
//                logUtil.d_3(TAG,changePos+"失去焦点");
                view.setSelected(true);
                view.clearAnimation();
                myHandler.sendEmptyMessageDelayed(LOSE_FOCUS,200);
            }
        }
    };

    //监听item的点击事件
    private OnClickListener barClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int i=0;i<tvMenubarList.size();i++){
                if(tvMenubarList.get(i).equals(v)){
                    if (onItemSelectListener != null) {
                        onItemSelectListener.onItemSelect(i);
                    }
                    break;
                }
            }
        }
    };
    public MallBarView(Context context) {
        this(context,null);
    }

    public MallBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MallBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        selectPos = -1;
        tvMenubarList = new ArrayList<>();
        animationMax = AnimationUtils.loadAnimation(mContext, R.anim.menubar_item_zoom);
        animationMax.setFillAfter(true);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.setOrientation(HORIZONTAL);
    }

    /**
     * 刷新数据
     */
    public void refreshData(List<MallHomeMenuResult.MenuItem> menuList){
        this.removeAllViews();
        tvMenubarList.clear();
        for(MallHomeMenuResult.MenuItem menuItem:menuList){
            TextView textViewItem = (TextView)inflater.inflate(R.layout.mall_bar_item, null);
            LayoutParams itemLayout =  new LayoutParams(Tool.getDimenWidth(mContext,210), Tool.getDimenhight(mContext,68));
            itemLayout.gravity = Gravity.CENTER_VERTICAL;
            itemLayout.leftMargin = Tool.getDimenWidth(mContext,20);
            itemLayout.rightMargin = Tool.getDimenWidth(mContext,20);
            this.addView(textViewItem,itemLayout);
            tvMenubarList.add(textViewItem);

            textViewItem.setText(menuItem.getName());
            textViewItem.setOnFocusChangeListener(barFocusListener);
            textViewItem.setOnClickListener(barClickListener);
            textViewItem.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if(isForceFocus){
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 设置选中item
     */
    public void initFocus(){
        if(tvMenubarList.size() > 0) {
            tvMenubarList.get(0).requestFocus();
            tvMenubarList.get(0).setSelected(true);
        }
    }

    /**
     * 选中item
     */
    public void selectItem(int pos){
        if(pos < tvMenubarList.size()) {
            for (int i = 0; i < tvMenubarList.size(); i++) {
                if (i != pos) {
                    tvMenubarList.get(i).setSelected(false);
                }else{
                    tvMenubarList.get(i).setSelected(true);
                }
            }
        }
    }
    /**
     * select的item重新获取焦点
     */
    private void SelectRequestFocus(int pos){
        if(pos < tvMenubarList.size()) {
            for (int i = 0; i < tvMenubarList.size(); i++) {
                if (i != pos) {
                    tvMenubarList.get(i).clearAnimation();
                }
            }
            View selectView = tvMenubarList.get(pos);
            if (selectView != null) {
//            logUtil.d_2(TAG,"强制焦点"+pos);
                if(selectView.hasFocus()){
                    selectView.startAnimation(animationMax);
                }else {
                    selectView.requestFocus();
                }
            }
        }
    }

    /**
     * 设置导航栏的焦点监听
     */
    public void setItemSelectListener(OnItemSelectListener listener){
        if(listener != null){
            onItemSelectListener = listener;
        }
    }

    public interface OnItemSelectListener {
        /**
         * @param pos 选中项的位置
         */
        void onItemSelect(int pos);
    }

}
