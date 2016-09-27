package com.juzifenqi.app.constant;

/**
 * Desc:   常量管理类
 * Time:   2016-09-23 12:04
 * Author: chende
 */
public class Const {

    public static String APP_NAME = "JuZiFenQi";// 应用名
    public static String SHARED_PREFERENCES_NAME = "JuZiFenQi";// SharePreferences文件名
    public static String SDCARD_FOLDER_NAME = "JuZiFenQi";// SDCard创建的文件目录名称
    public static String BASE_URL = "http://www.shishikuaipin.com/api/";
    public static int USER_PORTRAIT_SIZE = 500;//用户头像大小 500px
    public static int HTTP_TIME_OUT = 10000;//http请求超时时长

    public static class URL {// Http请求的Url
        public static final String LOGOUT_URL = "logout"; //注销接口
    }

    public static class BrocastAction {
        public static final String ACTION_CLICK_NOTIFICATION_FRIEND = "com.soft.zaipin.util.ACTION_CLICK_NOTIFICATION_FRIEND";// 点击通知栏（好友消息）
    }

    public static class RequestCode {// 请求码
        public static final int TAKE_PICTURE = 1;// 拍照
        public static final int PICK_PICTURE = 2;//选择照片
        public static final int DO_CROP = 3;// 图片裁剪
    }

    public static class ShareInfo {
        public static String shareUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.weebu.weibuyundong";
        public static String imageUrl = "http://b.hiphotos.baidu.com/image/h%3D360/sign=4cd34e043ff33a87816d061cf65d1018/8d5494eef01f3a29b41f18fa9c25bc315c607c2b.jpg";
        public static String title = "来找事";
        public static String content = "快来加入来找事吧";
    }

    public static class ThirdPlatform {
        public static String WEIXIN_APP_ID = "wx5bf8ffc284d7f50c";
        public static final String QQ_APP_ID = "1105583282";// 腾讯的AppId
    }


}
