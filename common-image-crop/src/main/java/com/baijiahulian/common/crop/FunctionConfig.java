package com.baijiahulian.common.crop;

import android.content.Context;

import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.utils.FrescoImageLoader;

import java.util.LinkedHashMap;

/**
 * Created by yanglei on 16/1/22.
 */
public class FunctionConfig {
    private boolean enableCrop; // 能否裁剪
    private boolean isMultiModel; // 是否多选
    private int maxSize; //最大选择图片数

    private boolean isSingleCamera; //是否拍照单选

    private int cropWidthRatio; //裁剪目标尺寸宽度
    private int cropHeightRatio; //裁剪目标尺寸高度

    private FrescoImageLoader  imageLoader;

    private LinkedHashMap<String, PhotoInfo> selectedList = new LinkedHashMap<>();

    public FunctionConfig(Context context, int maxSize) {
        enableCrop = false;
        isMultiModel = true;
        this.maxSize = maxSize;
        imageLoader = new FrescoImageLoader(context);
    }

    public FunctionConfig(Context context, int widthRatio, int heightRatio) {
        isMultiModel = false;
        enableCrop = (widthRatio >= 0 && heightRatio >= 0);
        cropWidthRatio = widthRatio;
        cropHeightRatio = heightRatio;
        imageLoader = new FrescoImageLoader(context);
    }

    public LinkedHashMap<String, PhotoInfo> getSelectedList() {
        return selectedList;
    }
    public FrescoImageLoader getImageLoader() {
       return imageLoader;
    }

    public boolean isEnableCrop() {
        return enableCrop;
    }

    public boolean isMultiModel() {
        return isMultiModel;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getCropHeightRatio() {
        return cropHeightRatio;
    }

    public int getCropWidthRatio() {
        return cropWidthRatio;
    }

    public void setIsSingleCamera(boolean isSingleCamera) {
        this.isSingleCamera = isSingleCamera;
    }

    public boolean isSingleCamera() {
        return isSingleCamera;
    }
}
