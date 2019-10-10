package c.liyueyun.mjmall.tv.tv.ui.activity.mall;


import c.liyueyun.mjmall.tv.base.httpApi.response.MallHomeMenuResult;
import c.liyueyun.mjmall.tv.tv.ui.base.IBaseView;

/**
 * Created by songjie on 2019-03-27
 */
public interface MallHomeView extends IBaseView {

    public void refresh(MallHomeMenuResult newData);
}
