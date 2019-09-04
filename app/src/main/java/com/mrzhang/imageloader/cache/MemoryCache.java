package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.Map;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class MemoryCache implements ImageCache{
    //图片内存缓存
    private LruCache<String, Bitmap> mImageCache;

    public MemoryCache() {
        initImageCache();
    }

    private void initImageCache() {
        //获取系统可用最大内存，这是VM所能提供的最大内存使用数量，超过这个值将抛出OOM异常
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //获取四分之一可用内存作为缓存
        final int catchSize = maxMemory / 4;
        //设置缓存大小，同时设置缓存
        mImageCache = new LruCache<String, Bitmap>(catchSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //Bitmap所占用的内存空间数等于Bitmap的每一行所占用的空间数乘以Bitmap的行数
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public Bitmap get(String url) {
        return mImageCache.get(url);
    }

    public void put(String url, Bitmap bitmap) {
        mImageCache.put(url, bitmap);
    }

    @Override
    public void delete() {
        mImageCache.evictAll();
    }
}
