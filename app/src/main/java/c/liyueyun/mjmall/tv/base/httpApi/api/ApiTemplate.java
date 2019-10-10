package c.liyueyun.mjmall.tv.base.httpApi.api;

import com.android.volley.RequestQueue;

import c.liyueyun.mjmall.tv.base.base.MyConstant;
import c.liyueyun.mjmall.tv.base.httpApi.impl.VolleyClient;


/**
 * Created by SongJie on 09/12 0012.
 */
public class ApiTemplate {
    private VolleyClient mClient;
    private DataTemplate dataTemplate;
    private TokenTemplate tokenTemplate;
    private UpdateTemplate updateTemplate;
    private UIManagerTemplate uiManagerTemplate;
    private MallTemplate mallTemplate;

    public ApiTemplate(RequestQueue queue) {
        mClient = new VolleyClient(queue);
        dataTemplate = new DataTemplate(mClient, MyConstant.DataUrl);
        tokenTemplate = new TokenTemplate(mClient, MyConstant.TokenUrl);
        updateTemplate = new UpdateTemplate(mClient, MyConstant.UpdateUrl);
        uiManagerTemplate = new UIManagerTemplate(mClient, MyConstant.ManagerUrl);
        mallTemplate = new MallTemplate(mClient, MyConstant.MallUrl);
    }

    public DataTemplate getDataTemplate() {
        return dataTemplate;
    }

    public TokenTemplate getTokenTemplate() {
        return tokenTemplate;
    }

    public UpdateTemplate getUpdateTemplate() {
        return updateTemplate;
    }

    public UIManagerTemplate getUiManagerTemplate() {
        return uiManagerTemplate;
    }

    public MallTemplate getMallTemplate() {
        return mallTemplate;
    }
}
