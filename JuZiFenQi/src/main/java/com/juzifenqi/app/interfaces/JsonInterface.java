package com.juzifenqi.app.interfaces;

import org.json.JSONObject;

/**
 * Desc:   遍历Json数组返回索引和json对象
 * Time:   2016-09-23 12:04
 * Author: chende
 */
public interface JsonInterface {

    void parse(JSONObject jData, int index);
}
