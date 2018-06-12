package com.android.bitglobal.tool;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import java.io.File;
import java.math.BigDecimal;

/**
 * 创建时间: 2017/5/22
 * 创建人: junhai
 * 功能描述:图片工具类，用来加载本地、资源、asset、网络图片
 */

public class ImageUtils {
    private static final ImageUtils ourInstance = new ImageUtils();

    public static ImageUtils get() {
        return ourInstance;
    }

    private ImageUtils() {
    }

    /**
     * 显示本地资源
     *
     * @param context
     * @param view
     * @param res     要显示的资源
     * @param holder  占位图片
     * @param error   加载出错图片
     */
    public void intoImage(Context context, ImageView view, int res, int holder, int error) {
        Glide.with(context).load(res).placeholder(holder).error(error).into(view);
    }

    /**
     * 显示本地资源
     *
     * @param context
     * @param view
     * @param res     要显示的资源
     * @param holder  占位图片
     */
    public void intoImage(Context context, ImageView view, int res, int holder) {
        Glide.with(context).load(res).placeholder(holder).into(view);
    }

    /**
     * 显示本地资源
     *
     * @param context
     * @param view
     * @param res     要显示的资源
     */
    public void intoImage(Context context, ImageView view, int res) {
        Glide.with(context).load(res).into(view);
    }

    /**
     * 显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param error   加载出错图片
     */
    public void intoImage(Context context, ImageView view, String url, int holder, int error) {
        Glide.with(context).load(url).placeholder(holder).error(error).into(view);
    }

    /**
     * 显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     */
    public void intoImage(Context context, ImageView view, String url, int holder) {
        Glide.with(context).load(url).placeholder(holder).into(view);
    }

    /**
     * 显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     */
    public void intoImage(Context context, ImageView view, String url) {
        Glide.with(context).load(url).into(view);
    }

    /**
     * 带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     本地资源
     * @param holder  占位图片
     * @param error   加载出错图片
     */
    public void intoImageWithCache(Context context, ImageView view, int res, int holder, int error) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(holder).error(error).into(view);
    }

    /**
     * 带缓存策略显示本地图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     */
    public void intoImageWithCache(Context context, ImageView view, int res, int holder) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(holder).into(view);
    }

    /**
     * 带缓存策略显示本地图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     */
    public void intoImageWithCache(Context context, ImageView view, int res) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    /**
     * 带缓存策略显示本地图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param error   加载出错图片
     */
    public void intoImageWithCache(Context context, ImageView view, String url, int holder, int error) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(holder).error(error).into(view);
    }

    /**
     * 带缓存策略显示本地图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     */
    public void intoImageWithCache(Context context, ImageView view, String url, int holder) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(holder).into(view);
    }

    /**
     * 带缓存策略显示本地图片
     *
     * @param context
     * @param view
     * @param url     图片url
     */
    public void intoImageWithCache(Context context, ImageView view, String url) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    /**
     * 带动画显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimation(Context context, ImageView view, int res, int holder, int error, int anim) {
        Glide.with(context).load(res).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimation(Context context, ImageView view, int res, int holder, int anim) {
        Glide.with(context).load(res).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param anim    动画ID
     */
    public void intoImageWithAnimation(Context context, ImageView view, int res, int anim) {
        Glide.with(context).load(res).animate(anim).into(view);
    }

    /**
     * 带动画显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimation(Context context, ImageView view, String url, int holder, int error, int anim) {
        Glide.with(context).load(url).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimation(Context context, ImageView view, String url, int holder, int anim) {
        Glide.with(context).load(url).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param anim    动画ID
     */
    public void intoImageWithAnimation(Context context, ImageView view, String url, int anim) {
        Glide.with(context).load(url).animate(anim).into(view);
    }

    /**
     * 带动画显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画效果
     */
    public void intoImageWithAnimation(Context context, ImageView view, int res, int holder, int error, Animation anim) {
        Glide.with(context).load(res).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param anim    动画效果
     */
    public void intoImageWithAnimation(Context context, ImageView view, int res, int holder, Animation anim) {
        Glide.with(context).load(res).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param anim    动画效果
     */
    public void intoImageWithAnimation(Context context, ImageView view, int res, Animation anim) {
        Glide.with(context).load(res).animate(anim).into(view);
    }

    /**
     * 带动画显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画效果
     */
    public void intoImageWithAnimation(Context context, ImageView view, String url, int holder, int error, Animation anim) {
        Glide.with(context).load(url).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param anim    动画效果
     */
    public void intoImageWithAnimation(Context context, ImageView view, String url, int holder, Animation anim) {
        Glide.with(context).load(url).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param anim    动画效果
     */
    public void intoImageWithAnimation(Context context, ImageView view, String url, Animation anim) {
        Glide.with(context).load(url).animate(anim).into(view);
    }

    /**
     * 带动画带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, int res, int holder, int error, int anim) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, int res, int holder, int anim) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param anim    动画ID
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, int res, int anim) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).into(view);
    }

    /**
     * 带动画带缓存策略显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, String url, int holder, int error, int anim) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画带缓存策略显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param anim    动画ID
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, String url, int holder, int anim) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画带缓存策略显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param anim    动画ID
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, String url, int anim) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).into(view);
    }

    /**
     * 带动画带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, int res, int holder, int error, Animation anim) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param holder  占位图片
     * @param anim    动画
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, int res, int holder, Animation anim) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画带缓存策略显示资源图片
     *
     * @param context
     * @param view
     * @param res     资源ID
     * @param anim    动画
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, int res, Animation anim) {
        Glide.with(context).load(res).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).into(view);
    }

    /**
     * 带动画带缓存策略显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param error   加载出错图片
     * @param anim    动画
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, String url, int holder, int error, Animation anim) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).error(error).into(view);
    }

    /**
     * 带动画带缓存策略显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param holder  占位图片
     * @param anim    动画
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, String url, int holder, Animation anim) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).placeholder(holder).into(view);
    }

    /**
     * 带动画带缓存策略显示网络图片
     *
     * @param context
     * @param view
     * @param url     图片url
     * @param anim    动画
     */
    public void intoImageWithAnimationAndCache(Context context, ImageView view, String url, Animation anim) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).animate(anim).into(view);
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
//                        BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getCacheDir() + "/"
                + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/"
                    + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
