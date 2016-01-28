package com.baijiahulian.common.crop;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.baijiahulian.common.crop.model.PhotoInfo;

import java.io.File;
import java.util.List;

/**
 * 图片裁剪编辑工具类
 * Created by yanglei on 16/1/22.
 */
public class BJCommonImageCropHelper {

    public interface OnHandlerResultCallback {
        void onHandlerSuccess(List<PhotoInfo> resultList);

        void onHandlerFailure(String errorMsg);
    }

    public enum PhotoCropType {
        /** 16:9 裁图*/Rectangle16To9, /** 1:1 裁图*/Square, /**不裁图*/None, /** 4:3 裁图*/Rectangle4To3, /** 自由裁剪  */Free;

        public static void ratioWithCropType(PhotoCropType cropType, int[] ratioResult) {
            switch (cropType) {
                case Rectangle16To9:
                    ratioResult[0] = 16;
                    ratioResult[1] = 9;
                    break;
                case Rectangle4To3:
                    ratioResult[0] = 4;
                    ratioResult[1] = 3;
                    break;
                case Square:
                    ratioResult[0] = 1;
                    ratioResult[1] = 1;
                    break;
                case Free:
                    ratioResult[0] = 0;
                    ratioResult[0] = 0;
                    break;
                case None:
                default:
                    ratioResult[0] = -1;
                    ratioResult[1] = -1;
                    break;
            }
        }
    }

    /**
     * 进入相册，选择多图
     * @param context
     * @param maxSize  最大选择图片数
     * @param themeConfig 样式配置
     * @param callback
     */
    public static void openImageMulti(Context context, int maxSize, ThemeConfig themeConfig, OnHandlerResultCallback callback) {
        if (maxSize <= 0) return;
        FunctionConfig config = new FunctionConfig(context, maxSize);

        themeConfig = themeConfig == null ? ThemeConfig.DEFAULT : themeConfig;

        ImageCropProxy.setThemeConfig(themeConfig);
        ImageCropProxy.setFunctionConfig(config);
        ImageCropProxy.setCallback(callback);

        PhotoSelectActivity.launch(context);
    }

    /**
     * 从相册选单张图片
     * @param context
     * @param cropType 裁图类型
     * @param callback
     * @param themeConfig 样式基本配置
     */
    public static void openImageSingleAblum(Context context, PhotoCropType cropType, ThemeConfig themeConfig, OnHandlerResultCallback callback) {
        int ratio[] = new int[2];
        PhotoCropType.ratioWithCropType(cropType, ratio);
        openImageSingleAblum(context, ratio[0], ratio[1], themeConfig, callback);
    }

    /**
     * 进入相册，单选图片
     * @param context
     * @param aspectWidth 裁剪图片的宽度比率
     * @param aspectHeight 裁剪图片的高度比率
     * @param themeConfig 样式基本配置
     * @param callback
     */
    public static void openImageSingleAblum(Context context, int aspectWidth, int aspectHeight, ThemeConfig themeConfig, OnHandlerResultCallback callback) {
        themeConfig = themeConfig == null ? ThemeConfig.DEFAULT : themeConfig;
        FunctionConfig config = new FunctionConfig(context, aspectWidth, aspectHeight);

        ImageCropProxy.setThemeConfig(themeConfig);
        ImageCropProxy.setFunctionConfig(config);
        ImageCropProxy.setCallback(callback);

        PhotoSelectActivity.launch(context);
    }

    /**
     * 拍照选择图片， 默认不裁剪
     * @param context
     * @param cropType 裁图类型
     * @param callback
     * @param themeConfig 样式基本配置
     */
    public static void openImageSingleCamera(Context context, PhotoCropType cropType, ThemeConfig themeConfig, OnHandlerResultCallback callback) {
        int ratio[] = new int[2];
        PhotoCropType.ratioWithCropType(cropType, ratio);
        openImageSingleCamera(context, ratio[0], ratio[1], themeConfig, callback);
    }

    /**
     * 拍照选择图片， 默认不裁剪
      * @param context
     * @param aspectWidth 裁剪图片的宽度比率
     * @param aspectHeight 裁剪图片的高度比率
     * @param themeConfig 样式基本配置
     * @param callback
     */
    public static void openImageSingleCamera(Context context, int aspectWidth, int aspectHeight, ThemeConfig themeConfig, OnHandlerResultCallback callback) {
        themeConfig = themeConfig == null ? ThemeConfig.DEFAULT : themeConfig;
        FunctionConfig config = new FunctionConfig(context, aspectWidth, aspectHeight);
        config.setIsSingleCamera(true);

        ImageCropProxy.setThemeConfig(themeConfig);
        ImageCropProxy.setFunctionConfig(config);
        ImageCropProxy.setCallback(callback);

        PhotoSelectActivity.launch(context);
    }

}
