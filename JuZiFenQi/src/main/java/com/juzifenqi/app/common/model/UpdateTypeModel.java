package com.juzifenqi.app.common.model;


import com.juzifenqi.app.constant.enums.UpdateType;

/**
 * 用于请求页面页面通知
 */
public class UpdateTypeModel {

    public String data;//数据
    public UpdateType updateType;// 更新类型

    public UpdateTypeModel(String data, UpdateType updateType) {
        this.data = data;
        this.updateType = updateType;
    }

    public UpdateTypeModel(UpdateType updateType) {
        this.updateType = updateType;
    }
}
