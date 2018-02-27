package com.cl.baseapplication.update.util;

/**
 * com.blueooo.miao.module.update.util in blueooo
 * Created by zhangdonghai on 2017/8/15
 */

public class ParseUtil {

    public static int getCode(String source, String expStr) {
        try {
            String replaceStr = source.replaceAll(expStr, "");
            return Integer.parseInt(replaceStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getCode(String source, String expStr, String replacement) {
        try {
            String replaceStr = source.replaceAll(expStr, "");
            return Integer.parseInt(replaceStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getCode(String source) {
        try {
            String replaceStr = source.replaceAll("\\.", "");
            return Integer.parseInt(replaceStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
