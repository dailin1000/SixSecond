package com.sixmediaplayer.www.utils;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.sixmediaplayer.www.R;

/**
 * NetUtils             用于网上下载的工具类
 * httpUtilsDownJson()  下载json工具
 * bitmapUtilsDown      下载图片工具
 * dbUtilsSave
 */
public class NetUtils {
    private static HttpUtils httpUtils;
    private static BitmapUtils bitmapUtils;
    private static DbUtils dbUtils;

    //下载json工具
    public static HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    //下载图片工具
    public static BitmapUtils getBitmapUtils(Context context) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context,
                    context.getExternalCacheDir().getPath(),
                    0.5f, 20 * 1024 * 1024);
        }
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        //运行时缓存,内存缓存
        bitmapUtils.configMemoryCacheEnabled(true);
        //磁盘缓存
        bitmapUtils.configDiskCacheEnabled(true);
        //线程池数量
        bitmapUtils.configThreadPoolSize(4);
        return bitmapUtils;
    }

    public static DbUtils getDbUtils(Context context) {
        if (dbUtils == null) {
            dbUtils = DbUtils.create(context);
        }
        return dbUtils;
    }
}
