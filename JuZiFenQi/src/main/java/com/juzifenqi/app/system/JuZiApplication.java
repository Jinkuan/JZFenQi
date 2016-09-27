package com.juzifenqi.app.system;

import android.app.Application;

import com.juzifenqi.app.BuildConfig;
import com.juzifenqi.app.common.Global;
import com.juzifenqi.app.common.model.UpdateTypeModel;
import com.juzifenqi.app.constant.Const;
import com.juzifenqi.app.http.HttpUtils;
import com.juzifenqi.app.other.BuildProperties;
import com.juzifenqi.app.readwrite.PreferManager;
import com.juzifenqi.app.readwrite.SDCardUtils;
import com.juzifenqi.app.util.Utils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * Desc:   主Application
 * Time:   2016-09-23 12:04
 * Author: chende
 */
public class JuZiApplication extends Application {

    private Global global;
    private EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        initConfig();
        initData();
    }

    private void initConfig() {//初始化一些必的服务配置或数据
        global = Global.getInstance(getApplicationContext());
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        PreferManager.init(this);//创建Sharedpreferences
        Utils.initScreenSize(this);//获取当前设备屏幕尺寸，并存入Sharedpreferences
        SDCardUtils.initPath(this);//创建文件存储目录
        HttpUtils.initAsyncHttp(this);//初始Http请求的一些基本配置
        Logger.init(Const.APP_NAME).hideThreadInfo().logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);//初始化日志配置
    }

    private void initData() {//初始化数据
        new Thread() {//初始化小米状态栏属性文件

            @Override
            public void run() {
                try {
                    global.setBuildProperties(BuildProperties.newInstance());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    @Subscribe
    public void onEvent(UpdateTypeModel updateTypeModel) {
        switch (updateTypeModel.updateType) {
            case LOGIN_STATUS_CHANGE://登录状态发生变更
                break;
        }
    }

    @Override
    public void onTerminate() {
        eventBus.unregister(this);
        super.onTerminate();
    }
}
