package com.juzifenqi.app.util;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Desc:   布局动态计算工具类
 * Time:   2016-09-23 12:53
 * Author: chende
 */
public class LayoutUtils {

    //(通用布局方法)============================================================(通用布局方法)=================================================================(通用布局方法)

    public static FrameLayout.LayoutParams getParamF(View view, int screen_width, double rate_width) {//帧式布局设定宽度
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        return params;
    }

    public static LinearLayout.LayoutParams getParamL_WHT(View view, int screen_width, double rate_width, double rate_height, double rate_top) {//线性布局设定，宽，高，上边距
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        params.topMargin = (int) (screen_width * rate_top);
        return params;
    }


    public static LinearLayout.LayoutParams getParamL_TB(View view, int screen_width, double rate_top, double rate_bottom) {//线性布局上下边距
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (rate_top > 0) {
            params.topMargin = (int) (screen_width * rate_top);
        }
        if (rate_bottom > 0) {
            params.bottomMargin = (int) (screen_width * rate_bottom);
        }
        return params;
    }


    public static LinearLayout.LayoutParams getParamL_WH(View view, int screen_width, double rate_width, double rate_height) {//线性布局设定宽，高
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        return params;
    }

    public static LinearLayout.LayoutParams getParamL_W(View view, int screen_width, double rate_width) {//线性布局设定宽
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        return params;
    }

    public static LinearLayout.LayoutParams getParamL_H(View view, int screen_width, double rate_height) {//线性布局设定高
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (screen_width * rate_height);
        return params;
    }

    public static LinearLayout.LayoutParams getParamL_H(View view, int height) {//线性布局设定高
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = height;
        return params;
    }


    public static RelativeLayout.LayoutParams getParamR_WH(View view, int screen_width, double rate_width, double rate_height) {//相对布局设定高宽
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        return params;
    }

    public static RelativeLayout.LayoutParams getParamR_H(View view, int screen_width, double rate_height) {//相对布局设定高
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (screen_width * rate_height);
        return params;
    }

    public static RelativeLayout.LayoutParams getParamR_H(View view, double rate_height) {//相对布局设定高
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (params.width * rate_height);
        return params;
    }

    public static RelativeLayout.LayoutParams getParamR_W(View view, int screen_width, double rate_width) {//相对布局设定宽
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (screen_width * rate_width);
        return params;
    }

    public static RelativeLayout.LayoutParams getParamR_HT(View view, int screen_width, double rate_height, double top_offset) {//相对布局设定高、上边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (screen_width * rate_height);
        params.topMargin = (int) (screen_width * top_offset);
        return params;
    }

    public static RelativeLayout.LayoutParams getParamR_WHT(View view, int screen_width, double rate_width, double rate_height, double top_offset) {//相对布局设定高、上边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        params.topMargin = (int) (screen_width * top_offset);
        return params;
    }


    public static RelativeLayout.LayoutParams getParamR_T(View view, int screen_width, double rate_top) {//关系布局设定上边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = (int) (screen_width * rate_top);
        return params;
    }


    public static RelativeLayout.LayoutParams getParamR_WHLR(View view, int screen_width, double rate_width, double rate_height, double rate_left, double rate_right) {//关系布局设定宽，高，左边距，右边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        if (rate_left != 0) {
            params.leftMargin = (int) (screen_width * rate_left);
        }
        if (rate_right != 0) {
            params.rightMargin = (int) (screen_width * rate_right);
        }
        return params;
    }


    public static RelativeLayout.LayoutParams getParamR_WHLBT(View view, int screen_width, double rate_width, double rate_height, double rate_left, double margin_bottom_rate, double margin_top_rate) {//关系布局设定宽，高，左边距，下边距,上中
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        if (rate_left > 0) {
            params.leftMargin = (int) (screen_width * rate_left);
        }
        if (margin_bottom_rate > 0) {
            params.bottomMargin = (int) (screen_width * margin_bottom_rate);
        }
        if (margin_top_rate > 0) {
            params.topMargin = (int) (screen_width * margin_top_rate);
        }
        return params;
    }

    public static FrameLayout.LayoutParams getParamF_T(View view, int screen_width, double rate_top) {//帧布局设定上边距
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = (int) (screen_width * rate_top);
        return params;
    }


    public static FrameLayout.LayoutParams getParamF_H(View view, int screen_width, double rate_height) {//相对布局设定高
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (screen_width * rate_height);
        return params;
    }

    public static FrameLayout.LayoutParams getParamF_H(View view, double rate_height) {//相对布局设定高
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) (params.width * rate_height);
        return params;
    }

    public static FrameLayout.LayoutParams getParamF_WH(View view, int screen_width, double rate_width, double rate_height) {//帧布局设定高宽
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (screen_width * rate_width);
        params.height = (int) (params.width * rate_height);
        return params;
    }

}
