package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;
import com.mrzhang.utils.CloseUtils;
import com.mrzhang.utils.FileUtils;
import com.mrzhang.utils.GeneralConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class ZDiskLruCache implements ImageCache {
    private DiskLruCache mDiskLruCache;

    public ZDiskLruCache() {
        try {
            mDiskLruCache = DiskLruCache.open(new File(FileUtils.getSDCachePath("bitmap")), GeneralConstants.getAppVersion(), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String url) {
        DiskLruCache.Snapshot snapshot;
        try {
            snapshot = mDiskLruCache.get(FileUtils.getHashKeyByMD5(url));
            if (snapshot != null) {
                InputStream is = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        DiskLruCache.Editor editor = null;

        try {
            editor = mDiskLruCache.edit(FileUtils.getHashKeyByMD5(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (editor != null) {
            try {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeBitmapToDisk(bitmap, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                CloseUtils.closeQuietly(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToDisk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            CloseUtils.closeQuietly(bos);
        }

        return result;
    }

    /**
     * flush()
     *
     * 这个方法用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。
     *
     * 频繁地调用并不会带来任何好处，只会额外增加同步journal文件的时间。
     *
     * 比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以了。
     */
}
