package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mrzhang.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class DiskCache implements ImageCache {
    private String TAG = DiskCache.class.getSimpleName();

    private static String cacheDir;

    public DiskCache() {
        cacheDir = FileUtils.getSDCachePath() + "/";
        Log.d(TAG, cacheDir);
    }

    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(cacheDir + getUrl(url));
    }

    public void put(String url, Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(cacheDir + getUrl(url)));
            //图片压缩 100 是不压缩  30 是压缩70%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getUrl(String url){
        return url.replaceAll("/","-");
    }
}
