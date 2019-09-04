package com.mrzhang.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.mrzhang.ImageLoaderApplication;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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


    public static String getSDCachePath(String uniqueName) {
        String cachePath = null;
        if (isSDCardMounted()) {
            if (ImageLoaderApplication.getInstance().getExternalCacheDir() != null) {
                cachePath = ImageLoaderApplication.getInstance().getExternalCacheDir().getPath();
            }
        } else {
            cachePath = ImageLoaderApplication.getInstance().getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    /**
     * 创建文件夹的路径
     *
     * @param path 路径
     */
    public static void createMkdir(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路径为空");
        }
        File file = new File(path);

        if (!file.exists() || !file.isDirectory()) {
            try {
                file.mkdirs();

            } catch (Exception e) {
                throw new RuntimeException("创建文件夹不成功");
            }
        }

    }

    /**
     * md5编码
     * 因为图片URL中可能包含一些特殊字符，这些字符有可能在命名文件时是不合法的
     */
    public static String getHashKeyByMD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
