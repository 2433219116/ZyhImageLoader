package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class DoubleCache implements ImageCache{

    private MemoryCache mMemoryCache = new MemoryCache();
    private DiskCache mDiskCache = new DiskCache();

    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }
}
