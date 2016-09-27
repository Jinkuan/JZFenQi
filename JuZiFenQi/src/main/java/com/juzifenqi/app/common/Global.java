package com.juzifenqi.app.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.juzifenqi.app.R;
import com.juzifenqi.app.other.BuildProperties;
import com.juzifenqi.app.readwrite.PreferManager;
import com.juzifenqi.app.util.Utils;

/**
 * Desc:   全局单例
 * Time:   2016-09-23 12:54
 * Author: chende
 */
public class Global {

    private static Global instance = null;
    private static Object LOCK = new Object();
    private Toast toast;
    private Boolean isLogin;
    private String userId;//用户id
    private String token;//当前用户token
    private int versionCode;//app当前版本号
    private String versionName;//app当前版本名
    private BuildProperties buildProperties;

    @SuppressLint("ShowToast")
    private Global(Context mContext) {
        this.versionCode = Utils.getVersionCode(mContext);
        this.versionName = Utils.getVersion(mContext);
        toast = Toast.makeText(mContext, R.string.net_work_disable, Toast.LENGTH_SHORT);
    }

    public static Global getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new Global(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public Toast getToast() {
        return toast;
    }

    public boolean isLogin() {
        if (isLogin == null) {
            isLogin = PreferManager.getBoolean(PreferManager.HAS_LOGIN, false);
        }
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        PreferManager.setBoolean(PreferManager.HAS_LOGIN, isLogin);
        this.isLogin = isLogin;
    }

    public String userId() {
        if (TextUtils.isEmpty(userId)) {
            userId = PreferManager.getString(PreferManager.USER_UID, "");
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        PreferManager.setString(PreferManager.USER_UID, userId);
    }

    public String token() {
        if (TextUtils.isEmpty(token)) {
            token = PreferManager.getString(PreferManager.TOKEN, "");
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        PreferManager.setString(PreferManager.TOKEN, token);
    }

    public int versionCode() {
        return versionCode;
    }

    public String versionName() {
        return versionName;
    }


    public BuildProperties getBuildProperties() {
        return buildProperties;
    }

    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }
}

