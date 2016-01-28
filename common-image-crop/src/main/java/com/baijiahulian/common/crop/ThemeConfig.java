package com.baijiahulian.common.crop;

import android.graphics.Color;
import android.os.Build;

/**
 * Created by yanglei on 16/1/22.
 * 自定义选图片样式
 */
public class ThemeConfig {

    public static final ThemeConfig DEFAULT = new ThemeConfig.Builder().build();

    private int titleBarRightButtonText ;
    private int mainElementsColor;

    private ThemeConfig(Builder builder) {
        this.titleBarRightButtonText = builder.getTitleBarRightButtonText();
        this.mainElementsColor = builder.getMainElementsColor();
    }

    public int getTitleBarRightButtonText() {
        return titleBarRightButtonText;
    }

    public int getMainElementsColor() {
        return mainElementsColor;
    }

    public static class Builder {
        private int titleBarRightButtonText = R.string.common_crop_completed;

        private int mainElementsColor = Color.WHITE;

        /**
         * 设置标题栏右侧按钮文字
         * @param text 默认为 “完成”
         * @return
         */
        public Builder setTitlebarRightButtonText(int text) {
            this.titleBarRightButtonText = text;
            return this;
        }

        /**
         * 设置主要元素的色值
         * @param color
         * @return
         */
        public Builder setMainElementsColor(int color) {
            this.mainElementsColor = color;
            return this;
        }

        public ThemeConfig build() {
            return new ThemeConfig(this);
        }

        public int getMainElementsColor() {
            return mainElementsColor;
        }

        public int getTitleBarRightButtonText() {
            return titleBarRightButtonText;
        }
    }
}
