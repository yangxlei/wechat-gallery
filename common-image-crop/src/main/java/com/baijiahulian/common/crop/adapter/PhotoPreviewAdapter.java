package com.baijiahulian.common.crop.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.baijiahulian.common.crop.ImageCropProxy;
import com.baijiahulian.common.crop.PhotoPreviewActivity;
import com.baijiahulian.common.crop.R;
import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.uikit.zoonview.PhotoView;
import com.baijiahulian.common.crop.uikit.zoonview.PhotoViewAttacher;
import com.baijiahulian.common.crop.utils.DeviceUtils;

import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/29 0029 15:53
 */
public class PhotoPreviewAdapter extends ViewHolderRecyclingPagerAdapter<PhotoPreviewAdapter.PreviewViewHolder, PhotoInfo> {

    private Activity mActivity;
    private DisplayMetrics mDisplayMetrics;

    private PhotoView mCurrentPagePhotoView;

    public PhotoPreviewAdapter(Activity activity, List<PhotoInfo> list) {
        super(activity, list);
        this.mActivity = activity;
        this.mDisplayMetrics = DeviceUtils.getScreenPix(mActivity);
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = getLayoutInflater().inflate(R.layout.item_preview_photo, null);
        PreviewViewHolder holder = new PreviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        PhotoInfo photoInfo = getDatas().get(position);
        String path = "";
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }

        ImageCropProxy.getFunctionConfig().getImageLoader()
                .displayImage(mActivity,
                        path, holder.mImageView, mDisplayMetrics.widthPixels / 2, mDisplayMetrics.heightPixels / 2);

        mCurrentPagePhotoView = holder.mImageView;
        mCurrentPagePhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ((PhotoPreviewActivity)mActivity).toggleTitleBar();
            }
        });
    }

    public PhotoView getCurrentPagePhotoView() {
        return mCurrentPagePhotoView;
    }

    static class PreviewViewHolder extends ViewHolderRecyclingPagerAdapter.ViewHolder{
        PhotoView mImageView;
        public PreviewViewHolder(View view) {
            super(view);
            mImageView = (PhotoView) view;
        }
    }
}
