package com.mrzhang.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * PROJECT_NAME：AndroidStudioProjects
 * PACKAGE_NAME：com.mrzhang.imageloader
 * CLASS_NAME：ImageLoader
 * AUTHOR：efan.zyhang
 * CREATE_TIME：2018/10/8 14:03
 * DESCRIPTION： 图片缓存
 */
public class ImageLoader {
    //图片内存缓存
    LruCache<String, Bitmap> mImageCache;

    //线程池、线程数量为CPU数量
    //可重用固定数量线程池
    //线程池处理形式好处是 系统空闲时可插入辅助线程，保持cpu繁忙，保持线程数目
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //UI handler
    Handler mUIHandler = new Handler(Looper.getMainLooper());


    public ImageLoader() {
        initImageLoader();
    }

    /**
     * 初始化工作
     */
    private void initImageLoader() {
        //获取系统可用最大内存，这是VM所能提供的最大内存使用数量，超过这个值将抛出OOM异常
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //获取四分之一可用内存作为缓存
        final int catchSize = maxMemory / 8;
        //设置缓存大小，同时设置缓存
        mImageCache = new LruCache<String, Bitmap>(catchSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //Bitmap所占用的内存空间数等于Bitmap的每一行所占用的空间数乘以Bitmap的行数
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    /**
     * 显示图片
     *
     * @param url       下载地址
     * @param imageView 图片地址
     */
    public void displayImage(final String url, final ImageView imageView) {
        imageView.setTag(url);

        //线程池提交
        //开启新的线程
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downLoadImage(url);
                if (bitmap == null) {
                    Log.e("app", "failed");
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImageView(imageView, bitmap);
                    Log.e("app", "ok");
                }
                //存入内存缓存
                mImageCache.put(url, bitmap);
            }
        });
    }

    /**
     * 传入url，下载图片
     *
     * @param imageUrl 图片地址
     * @return Bitmap 图片资源
     */
    private Bitmap downLoadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 更新UI
     *
     * @param imageView 更新图片位置
     * @param bitmap    图片资源
     */
    private void updateImageView(final ImageView imageView, final Bitmap bitmap) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
