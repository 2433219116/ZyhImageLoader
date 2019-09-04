package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mrzhang.utils.CloseUtils;
import com.mrzhang.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class DiskCache implements ImageCache {
    private String TAG = DiskCache.class.getSimpleName();

    private static String cacheDir;

    public DiskCache() {
        cacheDir = FileUtils.getSDCachePath("bitmap") + File.separator;
        Log.d(TAG, cacheDir);
    }

    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(cacheDir + FileUtils.getHashKeyByMD5(url));
    }

    public void put(String url, Bitmap bitmap) {
        File file = new File(FileUtils.getSDCachePath("bitmap"));

        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(cacheDir + FileUtils.getHashKeyByMD5(url)));
            //图片压缩 100 是不压缩  30 是压缩70%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(fileOutputStream);
        }
    }

    @Override
    public void delete() {
        File[] files = new File(FileUtils.getSDCachePath("bitmap")).listFiles();
        for (File file : files) {
            if (file.exists())
                file.delete();
        }
    }
}
