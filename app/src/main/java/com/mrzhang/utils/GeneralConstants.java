package com.mrzhang.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mrzhang.ImageLoaderApplication;

/**
 * Created by zyhang on 2019/9/3
 * <p>
 * Description:
 */
public class GeneralConstants {

    private static final String APP_VERSION_NAME = "0.0.1";

    public static int getAppVersion() {
        try {
            PackageInfo info = ImageLoaderApplication.getInstance().getPackageManager().getPackageInfo(ImageLoaderApplication.getInstance().getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
