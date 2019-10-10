package c.liyueyun.mjmall.tv.base.httpApi.api;

import com.android.volley.Request;

import c.liyueyun.mjmall.tv.base.httpApi.impl.VolleyClient;


/**
 * Created by SongJie on 11/17 0017.
 */
public class TokenTemplate extends BaseTemplate{

    public TokenTemplate(VolleyClient client, String server) {
        super(client);
        setServer(server);
    }

}
