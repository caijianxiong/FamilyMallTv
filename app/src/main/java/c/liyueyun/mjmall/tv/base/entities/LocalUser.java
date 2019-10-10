package c.liyueyun.mjmall.tv.base.entities;


import c.liyueyun.mjmall.tv.base.httpApi.response.LoginResult;
import c.liyueyun.mjmall.tv.base.httpApi.response.TokenResult;

/**
 * Created by SongJie on 11/16 0016.
 */
public class LocalUser {
    private LoginResult loginResult;
    private TokenResult tokenResult;

    public LoginResult getLoginResult() {
        if(loginResult == null){
            loginResult = new LoginResult();
        }
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public TokenResult getTokenResult() {
        return tokenResult;
    }

    public void setTokenResult(TokenResult tokenResult) {
        this.tokenResult = tokenResult;
    }


    public interface OnTvNumberistener {
        void onResult(String number);
    }
}
