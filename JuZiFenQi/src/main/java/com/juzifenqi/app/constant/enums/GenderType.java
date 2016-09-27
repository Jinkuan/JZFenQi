package com.juzifenqi.app.constant.enums;


import com.juzifenqi.app.common.model.TextValueObj;

import java.util.ArrayList;
import java.util.List;

/**
 * 性别
 */
public enum GenderType {

    MALE(1, "男"),
    FEMALE(2, "女");

    private int value;
    private String text;
    private int resId;

    GenderType(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static GenderType getType(int value) {
        GenderType[] list = GenderType.values();
        if (list != null) {
            for (GenderType obj : list) {
                if (obj.getValue() == value) {
                    return obj;
                }
            }
        }
        return MALE;
    }

    public static List<TextValueObj> getList() {
        List<TextValueObj> list = new ArrayList<>();
        for (GenderType obj : GenderType.values()) {
            list.add(new TextValueObj(obj.getText(), obj.getValue()));
        }
        return list;
    }

}
