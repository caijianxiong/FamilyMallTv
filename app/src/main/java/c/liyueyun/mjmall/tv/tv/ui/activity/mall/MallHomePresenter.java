package c.liyueyun.mjmall.tv.tv.ui.activity.mall;

import android.content.Context;
import android.content.Intent;

import c.liyueyun.mjmall.tv.base.base.MyApplication;
import c.liyueyun.mjmall.tv.base.base.Tool;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyCallback;
import c.liyueyun.mjmall.tv.base.httpApi.impl.MyErrorMessage;
import c.liyueyun.mjmall.tv.base.httpApi.response.MallHomeMenuResult;
import c.liyueyun.mjmall.tv.base.manage.UpdateManager;
import c.liyueyun.mjmall.tv.tv.ui.base.BasePresenter;


/**
 * Created by songjie on 2019-03-27
 */
public class MallHomePresenter extends BasePresenter<MallHomeView> {
    private Context mContext;

    public MallHomePresenter(Context mContext) {
        this.mContext = mContext;
//        HandlerManage.getInstance().addHandler(HandlerManage.updateType.AVCallActivity, myHandler);
    }

    @Override
    public void detachView() {
        super.detachView();
//        HandlerManage.getInstance().removeHandler(HandlerManage.updateType.AVCallActivity);
    }

    /**
     * 初始化数据
     */
    public void initData(Intent intent) {
        MyApplication.getInstance().getmApi().getMallTemplate().getHomeMenu(new MyCallback<MallHomeMenuResult>() {
            @Override
            public void onError(MyErrorMessage msg) {
                getView().showErrorDialog(Tool.getApiErrorMsg(msg.getMessage()),false);
            }

            @Override
            public void onSuccess(MallHomeMenuResult response) {
                if(isAttachView()){
                    getView().refresh(response);
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
