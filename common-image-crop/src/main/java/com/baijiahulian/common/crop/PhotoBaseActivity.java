/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.baijiahulian.common.crop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.permissions.EasyPermissions;
import com.baijiahulian.common.crop.utils.ActivityManager;
import com.baijiahulian.common.crop.utils.DeviceUtils;
import com.baijiahulian.common.crop.utils.MediaScanner;
import com.baijiahulian.common.crop.utils.Utils;
import com.baijiahulian.common.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:46
 */
public abstract class PhotoBaseActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    protected static String mPhotoTargetFolder;

    private Uri mTakePhotoUri;
    private MediaScanner mMediaScanner;

    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("takePhotoUri", mTakePhotoUri);
        outState.putString("photoTargetFolder", mPhotoTargetFolder);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTakePhotoUri = savedInstanceState.getParcelable("takePhotoUri");
        mPhotoTargetFolder = savedInstanceState.getString("photoTargetFolder");
    }

    protected Handler mFinishHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finishGalleryFinalPage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mMediaScanner = new MediaScanner(this);
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaScanner != null) {
            mMediaScanner.unScanFile();
        }
        ActivityManager.getActivityManager().finishActivity(this);
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 拍照
     */
    protected void takePhotoAction() {
        if (!DeviceUtils.existSDCard()) {
            toast("没有SD卡不能拍照呢~");
            return;
        }

        File takePhotoFolder = null;
        if (StringUtils.isEmpty(mPhotoTargetFolder)) {
            takePhotoFolder = ImageCropProxy.getTakePhotoDir();
        } else {
            takePhotoFolder = new File(mPhotoTargetFolder);
        }

        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String name = f.format(new Date());
        File toFile = new File(takePhotoFolder, "IMG" + name + ".jpg");
        boolean suc = false;
        try {
            suc = toFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Logger.d("create folder=" + toFile.getAbsolutePath());
        if (suc) {
            mTakePhotoUri = Uri.fromFile(toFile);
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
            startActivityForResult(captureIntent, ImageCropProxy.REQUEST_CODE_TAKE_PHOTO);
        } else {
//            Logger.e("create file failure");
            toast(getString(R.string.common_crop_take_photo_fail));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == ImageCropProxy.REQUEST_CODE_TAKE_PHOTO) {
            if (resultCode == RESULT_OK && mTakePhotoUri != null) {
                final String path = mTakePhotoUri.getPath();
                final PhotoInfo info = new PhotoInfo();
                info.setPhotoId(Utils.getRandom(10000, 99999));
                info.setPhotoPath(path);
                updateGallery(path);
                takeResult(info);
            } else {
                toast(getString(R.string.common_crop_take_photo_fail));
            }
        }
    }

    /**
     * 更新相册
     */
    private void updateGallery(String filePath) {
        if (mMediaScanner != null) {
            mMediaScanner.scanFile(filePath, "image/jpeg");
        }
    }

    protected void resultData() {

        BJCommonImageCropHelper.OnHandlerResultCallback callback = ImageCropProxy.getCallback();

        if (callback != null) {
            ArrayList<PhotoInfo> photoList = new ArrayList<>();
            photoList.addAll(ImageCropProxy.getFunctionConfig().getSelectedList().values());

            if ( photoList != null && photoList.size() > 0 ) {
                callback.onHandlerSuccess(photoList);
            } else {
                callback.onHandlerFailure(getString(R.string.common_crop_photo_list_empty));
            }
        }
        finishGalleryFinalPage();
    }

    protected void resultFailure(String errormsg, boolean delayFinish) {
        BJCommonImageCropHelper.OnHandlerResultCallback callback = ImageCropProxy.getCallback();
        if ( callback != null ) {
            callback.onHandlerFailure(errormsg);
        }
        if(delayFinish) {
            mFinishHanlder.sendEmptyMessageDelayed(0, 500);
        } else {
            finishGalleryFinalPage();
        }
    }

    private void finishGalleryFinalPage() {
        ActivityManager.getActivityManager().finishActivity(PhotoEditActivity.class);
        ActivityManager.getActivityManager().finishActivity(PhotoSelectActivity.class);
        ActivityManager.getActivityManager().finishActivity(PhotoPreviewActivity.class);

        ImageCropProxy.setFunctionConfig(null);
        ImageCropProxy.setCallback(null);
        ImageCropProxy.setThemeConfig(null);
    }

    protected abstract void takeResult(PhotoInfo info);

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(List<String> list) {
    }

    @Override
    public void onPermissionsDenied(List<String> list) {
    }
}
