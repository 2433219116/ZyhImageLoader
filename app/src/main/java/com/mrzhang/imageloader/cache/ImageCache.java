package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public interface ImageCache {
    Bitmap get(String url);
    void put(String url, Bitmap bitmap);
    void delete();
}
