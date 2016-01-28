package com.baijiahulian.common.crop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.uikit.CCButton;
import com.baijiahulian.common.crop.uikit.crop.CropImageActivity;
import com.baijiahulian.common.crop.uikit.crop.CropImageView;
import com.baijiahulian.common.crop.utils.RecycleViewBitmapUtils;
import com.baijiahulian.common.crop.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yanglei on 16/1/26.
 */
public class PhotoEditActivity extends CropImageActivity implements View.OnClickListener{

    protected static void launch(Context context) {
        Intent intent = new Intent(context, PhotoEditActivity.class);
        context.startActivity(intent);
    }

    private ImageButton mBackButton;
    private TextView mTitleView;
    private CCButton mRightButton;
    private CropImageView mCropView;

    private PhotoInfo mPhotoInfo;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_photo_edit);

        if (ImageCropProxy.getFunctionConfig().isMultiModel()
                ||ImageCropProxy.getFunctionConfig().getSelectedList().size() != 1
                || !ImageCropProxy.getFunctionConfig().isEnableCrop()) {
            finish();
        }

        ArrayList<PhotoInfo> photoList = new ArrayList<>();
        photoList.addAll(ImageCropProxy.getFunctionConfig().getSelectedList().values());
        mPhotoInfo = photoList.get(0);

        initialUI();
        initialImageState();
    }

    private void initialUI() {
        mBackButton = (ImageButton) findViewById(R.id.title_bar_back_button_ib);
        mTitleView = (TextView) findViewById(R.id.title_bar_title_tv);
        mRightButton = (CCButton) findViewById(R.id.title_bar_right_button_cb);

        mCropView = (CropImageView) findViewById(R.id.activity_photo_edit_crop_photo);

        mBackButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mTitleView.setText(R.string.common_crop_picture);
        mRightButton.setBackgroundColor(ImageCropProxy.getThemeConfig().getMainElementsColor());
        mRightButton.setText(ImageCropProxy.getThemeConfig().getTitleBarRightButtonText());
    }

    private void initialImageState() {


        String path = "";
        if (mPhotoInfo != null) {
            path = mPhotoInfo.getPhotoPath();
        }

        initCrop(mCropView, ImageCropProxy.getFunctionConfig().getCropWidthRatio(),
                ImageCropProxy.getFunctionConfig().getCropHeightRatio());

        setSourceUri(Uri.fromFile(new File(path)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setCropSaveSuccess(File file) {
        mPhotoInfo.setPhotoPath(file.getAbsolutePath());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageCropProxy.getFunctionConfig().getSelectedList().clear();
                ImageCropProxy.getFunctionConfig().getSelectedList().put(mPhotoInfo.getPhotoPath(), mPhotoInfo);
                mCropView.setImageBitmap(null);
                resultData();
            }
        });
    }

    @Override
    public void setCropSaveException(Throwable throwable) {

    }

    @Override
    protected void takeResult(PhotoInfo info) {

    }

    @Override
    public void onClick(View v) {
        if (v == mBackButton) {
            finish();
        } else if (v == mRightButton) {
            System.gc();
            String ext = Utils.getFileExtension(mPhotoInfo.getPhotoPath());
            File toFile = new File(ImageCropProxy.getTakePhotoDir(), Utils.getFileName(mPhotoInfo.getPhotoPath()) + "_crop." + ext);
            onSaveClicked(toFile);
        }
    }
}
