package com.mrzhang;

import android.app.Application;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class ImageLoaderApplication extends Application {
    private static ImageLoaderApplication mBase;

    @Override
    public void onCreate() {
        super.onCreate();
        mBase = this;
    }

    public static ImageLoaderApplication getInstance() {
        return mBase;
    }
}
