package com.juzifenqi.app.util;

/**
 * Desc:   常用正则表达式工具类
 * Time:   2016-09-23 13:12
 * Author: chende
 */
public class RegularUtils {

    public static boolean isLegal(String IDNumber) {//判断是否是有效的身份证号码
        boolean result = IDNumber.matches("[0-9]{15}|[0-9]{17}[0-9X]");
        if (result) {
            int year, month, date;
            if (IDNumber.length() == 15) {
                year = Integer.parseInt(IDNumber.substring(6, 8));
                month = Integer.parseInt(IDNumber.substring(8, 10));
                date = Integer.parseInt(IDNumber.substring(10, 12));
            } else {
                year = Integer.parseInt(IDNumber.substring(6, 10));
                month = Integer.parseInt(IDNumber.substring(10, 12));
                date = Integer.parseInt(IDNumber.substring(12, 14));
            }
            switch (month) {
                case 2:
                    result = (date >= 1) && (year % 4 == 0 ? date <= 29 : date <= 28);
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    result = (date >= 1) && (date <= 31);
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    result = (date >= 1) && (date <= 31);
                    break;
                default:
                    result = false;
                    break;
            }
        }
        return result;
    }


}
