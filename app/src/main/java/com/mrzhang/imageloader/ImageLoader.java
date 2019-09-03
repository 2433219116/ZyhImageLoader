package com.mrzhang.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.mrzhang.imageloader.cache.DiskCache;
import com.mrzhang.imageloader.cache.DoubleCache;
import com.mrzhang.imageloader.cache.MemoryCache;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * CLASS_NAME：ImageLoader
 * AUTHOR：efan.zyhang
 */

public class ImageLoader {

    private MemoryCache mMemoryCache = new MemoryCache();
    private DiskCache mDiskCache = new DiskCache();
    private DoubleCache mDoubleCache = new DoubleCache();

    private boolean isUseDiskCache = false;
    private boolean isUseDoubleCache = false;

    //线程池、线程数量为CPU数量
    //可重用固定数量线程池
    //线程池处理形式好处是 系统空闲时可插入辅助线程，保持cpu繁忙，保持线程数目
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //UI handler
    private Handler mUIHandler = new Handler(Looper.getMainLooper());


    public void useDiskCache(boolean useDiskCache) {
        isUseDiskCache = useDiskCache;
    }

    public void useDoubleCache(boolean useDoubleCache) {
        isUseDoubleCache = useDoubleCache;
    }

    /**
     * 显示图片
     */
    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = getBitmap(url);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        
        submitExecutor(url, imageView);

    }

    private void submitExecutor(final String url, final ImageView imageView) {
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

                putBitmap(bitmap, url);
            }
        });
    }

    private void putBitmap(Bitmap bitmap, String url) {
        if (isUseDoubleCache) {
            mDoubleCache.put(url, bitmap);
        } else if (isUseDiskCache) {
            mDiskCache.put(url, bitmap);
        } else {
            mMemoryCache.put(url, bitmap);
        }
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        if (isUseDoubleCache) {
            bitmap = mDoubleCache.get(url);
        } else if (isUseDiskCache) {
            bitmap = mDiskCache.get(url);
        } else {
            bitmap = mMemoryCache.get(url);
        }
        return bitmap;
    }

    /**
     * 下载图片
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
