package com.baijiahulian.common.crop;

import android.os.Environment;

import java.io.File;

/**
 * Created by yanglei on 16/1/27.
 */
public class ImageCropProxy {

    public static final int REQUEST_CODE_TAKE_PHOTO = 1001;
    public static final int PERMISSIONS_CODE_GALLERY = 2001;

    private static BJCommonImageCropHelper.OnHandlerResultCallback mCallback;
    private static ThemeConfig mThemeConfig;
    private static FunctionConfig mFunctionConfig;

    public static ThemeConfig getThemeConfig() {
        return mThemeConfig;
    }

    public static FunctionConfig getFunctionConfig() {
        return mFunctionConfig;
    }

    public static BJCommonImageCropHelper.OnHandlerResultCallback getCallback() {
        return mCallback;
    }

    public static void setCallback(BJCommonImageCropHelper.OnHandlerResultCallback mCallback) {
        ImageCropProxy.mCallback = mCallback;
    }

    public static void setFunctionConfig(FunctionConfig mFunctionConfig) {
        ImageCropProxy.mFunctionConfig = mFunctionConfig;
    }

    public static void setThemeConfig(ThemeConfig mThemeConfig) {
        ImageCropProxy.mThemeConfig = mThemeConfig;
    }

    /**
     * 返回拍照后照片存储目录
     * @return
     */
    public static File getTakePhotoDir() {
        File takePhotoFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/");
        if (! takePhotoFile.exists()) {
            takePhotoFile.mkdirs();
        }
        return takePhotoFile;
    }
}
