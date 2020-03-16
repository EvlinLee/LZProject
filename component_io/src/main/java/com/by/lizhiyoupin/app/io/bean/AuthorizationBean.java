package com.by.lizhiyoupin.app.io.bean;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/28 19:35
 * Summary: 授权
 */
public class AuthorizationBean {

    private String authorizationUrl;

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    @Override
    public String toString() {
        return "AuthorizationBean{" +
                "authorizationUrl='" + authorizationUrl + '\'' +
                '}';
    }
}
