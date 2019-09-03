package com.mrzhang.imageloader.cache;

import android.graphics.Bitmap;

import com.jakewharton.disklrucache.DiskLruCache;
import com.mrzhang.utils.CloseUtils;
import com.mrzhang.utils.FileUtils;
import com.mrzhang.utils.GeneralConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
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
            mDiskLruCache = DiskLruCache.open(new File(FileUtils.getSDCachePath()), GeneralConstants.getAppVersion(), 1, 50 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String url) {
        return null;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        DiskLruCache.Editor editor = null;

        try {
            editor = mDiskLruCache.edit(url);
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
}
