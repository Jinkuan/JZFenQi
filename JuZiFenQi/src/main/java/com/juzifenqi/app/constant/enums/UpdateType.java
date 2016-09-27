package com.juzifenqi.app.constant.enums;

/**
 * 类描述：更新类型标识
 */
public enum UpdateType {

    LOGIN_STATUS_CHANGE("用户登录状态发生变化"),
    REQUEST_RE_QUERY_USER_INFO("请求重新查询用户信息"),
    RELOCATION("重新定位");

    private UpdateType(String text) {

    }

}