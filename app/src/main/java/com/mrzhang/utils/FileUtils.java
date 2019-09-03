package com.mrzhang.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.mrzhang.ImageLoaderApplication;

import java.io.File;
import java.io.IOException;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class FileUtils {

    /**
     * 判断sdcrad是否已经安装
     *
     * @return boolean true安装 false 未安装
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 得到sdcard的路径
     */
    public static String getSDCardRoot() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    public static String getSDCachePath() {
        if (isSDCardMounted()) {
            if (ImageLoaderApplication.getInstance().getExternalCacheDir() != null) {
                return ImageLoaderApplication.getInstance().getExternalCacheDir().getAbsolutePath();
            }
        }
        return "";
    }

    /**
     * 创建文件的路径及文件
     */

    public static String createMkdirsAndFiles(String path, String filename) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路径为空");
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                throw new RuntimeException("创建文件夹不成功");
            }
        }
        File f = new File(file, filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("创建文件不成功");
            }
        }
        return f.getAbsolutePath();
    }

}
