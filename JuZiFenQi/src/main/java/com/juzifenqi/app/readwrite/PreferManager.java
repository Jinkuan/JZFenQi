package com.juzifenqi.app.readwrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.juzifenqi.app.constant.Const;

/**
 * Desc:   Sharedpreferences 管理类
 * Time:   2016-09-23 12:04
 * Author: chende
 */
public class PreferManager {

    public static final String SCREEN_WIDTH = "screen_width";//屏幕宽度
    public static final String SCREEN_HEIGHT = "screen_height";//屏幕高度
    public static final String STATUS_BAR_HEIGHT = "status_bar_height";//状态栏宽度
    public static final String TOKEN = "token";// 用户token
    public static final String USER_UID = "user_uid";// 用户UID
    public static final String HAS_LOGIN = "has_login";// 标识用户是否登录

    public static SharedPreferences sp;
    public static Editor edit;

    public static void init(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            edit = sp.edit();
            edit.commit();
        }
    }

    public static void setString(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }


    public static void setLong(String key, long value) {
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static void setFloat(String key, float value) {
        edit.putFloat(key, value);
        edit.commit();
    }

    public static float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }


}
