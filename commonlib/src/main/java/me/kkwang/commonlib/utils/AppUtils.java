/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.kkwang.commonlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.DecimalFormat;

public class AppUtils {

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Should not happen.
            throw new RuntimeException(e);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = ViewUtils.dpToPxInt(25,context);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 转换文件大小
     *
     * @param fileLength file
     * @param pattern    匹配模板 "#.00","0.0"...
     * @return 格式化后的大小
     */
    public static String formatFileSize(long fileLength, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        String fileSizeString;
        if (fileLength < 1024) {
//            fileSizeString = df.format((double) fileLength) + "B";
            fileSizeString = "0KB";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;
    }


}
