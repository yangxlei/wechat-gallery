package com.baijiahulian.common.crop;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baijiahulian.common.crop.model.PhotoFolderInfo;
import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.uikit.GFImageView;
import com.baijiahulian.common.crop.utils.DeviceUtils;

import java.util.ArrayList;

/**
 * Created by yanglei on 16/1/23.
 */
public class PhotoSelectViewModel implements View.OnClickListener {

    private ImageButton mButtonBack;
    private TextView mTitleView;
    private Button mRightButton;
    private RecyclerView mPhotosView;
    private RecyclerView mFoldersView;

    private TextView mToggleFolderTextView;
    private TextView mPreviewTextView;

    private PhotoFolderInfo mCurrentFolderInfo;
    private ArrayList<PhotoInfo> mCurPhotos;

    private PhotoSelectActivity mActivity;

    private PhotosAdapter mPhotosAdapter;

    private View mFolderViewPannel;

    private DisplayMetrics mDisplayMetrics;

    public void setUI(PhotoSelectActivity activity, PhotoSelectActivity.FoldersAdapter foldersAdapter) {
        mActivity = activity;
        mCurPhotos = new ArrayList<>();
        mDisplayMetrics = DeviceUtils.getScreenPix(activity);

        activity.setContentView(R.layout.activity_photo_selected);

        mButtonBack = (ImageButton) activity.findViewById(R.id.title_bar_back_button_ib);
        mTitleView = (TextView) activity.findViewById(R.id.title_bar_title_tv);
        mRightButton = (Button) activity.findViewById(R.id.title_bar_right_button_cb);

        mPhotosView = (RecyclerView) activity.findViewById(R.id.activity_photo_selected_photos_list);
        mFoldersView = (RecyclerView) activity.findViewById(R.id.activity_photo_selected_folders_list);

        mPhotosView.setLayoutManager(new GridLayoutManager(activity, 3));
        mFoldersView.setLayoutManager(new LinearLayoutManager(activity));

        mFoldersView.setAdapter(foldersAdapter);
        mPhotosAdapter = new PhotosAdapter();
        mPhotosView.setAdapter(mPhotosAdapter);

        mToggleFolderTextView = (TextView) activity.findViewById(R.id.activity_photo_selected_toggle_folders_tv);
        mPreviewTextView = (TextView) activity.findViewById(R.id.activity_photo_selected_preview_tv);

        mFolderViewPannel = activity.findViewById(R.id.activit_photo_selected_folders_container_fl);

        mButtonBack.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        activity.findViewById(R.id.activity_photo_selected_toggler_folder_container_ll).setOnClickListener(this);
        mPreviewTextView.setOnClickListener(this);
    }

    public void refreshUI() {
        String folderName = mCurrentFolderInfo == null ? mActivity.getString(R.string.common_crop_all_photo) :
                mCurrentFolderInfo.getFolderName() + "";
        mTitleView.setText(folderName);
        mToggleFolderTextView.setText(folderName);

        String rightText = mActivity.getString(ImageCropProxy.getThemeConfig().getTitleBarRightButtonText());
        FunctionConfig config = ImageCropProxy.getFunctionConfig();
        if (config.isMultiModel() && config.getSelectedList().size() > 0) {
            String text = "(" + config.getSelectedList().size() + "/" + config.getMaxSize() + ")";
            rightText += text;
        }

        if (config.getSelectedList().size() > 0) {
            mRightButton.setEnabled(true);
        } else {
            mRightButton.setEnabled(false);
        }

        mRightButton.setText(rightText);
        mRightButton.setBackgroundColor(ImageCropProxy.getThemeConfig().getMainElementsColor());

        if (! ImageCropProxy.getFunctionConfig().isMultiModel()) {
           mRightButton.setVisibility(View.GONE);
        }


        mPreviewTextView.setText(mActivity.getString(R.string.common_crop_preview, config.getSelectedList().size()));
        if (config.isMultiModel()) {
            mPreviewTextView.setEnabled(config.getSelectedList().size() > 0);
        } else {
            mPreviewTextView.setVisibility(View.GONE);
        }

    }

    public void refreshPhotos() {
        mPhotosAdapter.notifyDataSetChanged();
    }

    public void setCurrentFolder(PhotoFolderInfo folderInfo) {
        mCurrentFolderInfo = folderInfo;
        mCurPhotos.clear();
        mCurPhotos.addAll(folderInfo.getPhotoList());
        mPhotosAdapter.notifyDataSetChanged();
        refreshUI();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFolderViewPannel.getVisibility() == View.VISIBLE) {
                toggleFolderPannel();
                return true;
            }
        }
        return false;
    }

    public void toggleFolderPannel() {
        if (mFolderViewPannel.getVisibility() == View.VISIBLE) {
            mFolderViewPannel.setVisibility(View.GONE);
            mFolderViewPannel.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.common_crop_out_up_to_bottom));
        } else {
            mFolderViewPannel.setVisibility(View.VISIBLE);
            mFolderViewPannel.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.common_crop_in_bottom_to_up));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonBack) {
            if (mFolderViewPannel.getVisibility() == View.VISIBLE) {
                toggleFolderPannel();
            } else {
                mActivity.finish();
            }
        } else if (v == mRightButton) {
            mActivity.resultData();
        } else if (v.getId() == R.id.activity_photo_selected_toggler_folder_container_ll) {
            toggleFolderPannel();
        } else if (v == mPreviewTextView) {
            if (mFolderViewPannel.getVisibility() == View.VISIBLE) {
                toggleFolderPannel();
            }
            if (ImageCropProxy.getFunctionConfig().getSelectedList().size() > 0) {
                ArrayList<PhotoInfo> list = new ArrayList<>();
                list.addAll(ImageCropProxy.getFunctionConfig().getSelectedList().values());
                PhotoPreviewActivity.launch(mActivity, list);
            }
        }
    }

    private class PhotosAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return 0;
            return 1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder;
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_list_camera, parent, false);
                holder = new ViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_list_photo, parent, false);
                holder = new ViewHolder(view);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (position == 0) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.takePhotoAction();
                    }
                });
            } else {
                GFImageView imageView = (GFImageView) holder.itemView.findViewById(R.id.item_photo_list_photo_ci);
                final ImageButton checkBox = (ImageButton) holder.itemView.findViewById(R.id.item_photo_list_photo_checkbox);

                final PhotoInfo photoInfo = mCurPhotos.get(position - 1);

                ImageCropProxy.getFunctionConfig().getImageLoader()
                        .displayImage(mActivity, photoInfo.getPhotoPath(),
                                imageView, (int) (mDisplayMetrics.density * 120), (int) (mDisplayMetrics.density * 120));

                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ImageCropProxy.getFunctionConfig().isMultiModel()) {
                            PhotoPreviewActivity.launch(mActivity, mCurPhotos, position - 1);
                        } else {
                            ImageCropProxy.getFunctionConfig().getSelectedList().clear();
                            ImageCropProxy.getFunctionConfig().getSelectedList().put(photoInfo.getPhotoPath(), photoInfo);
                            if (! ImageCropProxy.getFunctionConfig().isEnableCrop()) {
                                PhotoPreviewActivity.launch(mActivity, mCurPhotos, position - 1);
                            } else {
                                PhotoEditActivity.launch(mActivity);
                            }
                        }
                    }
                });

                if (ImageCropProxy.getFunctionConfig().isMultiModel()) {
                    checkBox.setVisibility(View.VISIBLE);
                    boolean checked = ImageCropProxy.getFunctionConfig().getSelectedList().containsKey(photoInfo.getPhotoPath());
                    checkBox.setImageResource(checked?R.drawable.common_crop_ic_checkbox_checked:R.drawable.common_crop_ic_checkbox_unchecked);

                    checkBox.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            boolean checked = ImageCropProxy.getFunctionConfig().getSelectedList().containsKey(photoInfo.getPhotoPath());

                            if (! checked &&  ImageCropProxy.getFunctionConfig().getSelectedList().size() <
                                        ImageCropProxy.getFunctionConfig().getMaxSize()) {
                                    ImageCropProxy.getFunctionConfig().getSelectedList().put(photoInfo.getPhotoPath(), photoInfo);
                                    checkBox.setImageResource(R.drawable.common_crop_ic_checkbox_checked);
                            } else if (! checked) {
                                //错误提示
                                mActivity.toast(mActivity.getString(R.string.common_crop_tips_max_count,
                                        ImageCropProxy.getFunctionConfig().getMaxSize()));
                            } else {
                                ImageCropProxy.getFunctionConfig().getSelectedList().remove(photoInfo.getPhotoPath());
                                checkBox.setImageResource(R.drawable.common_crop_ic_checkbox_unchecked);
                            }

                            refreshUI();
                        }
                    });
                } else {
                    checkBox.setVisibility(View.GONE);
                    checkBox.setOnClickListener(null);
                }
            }

        }

        @Override
        public int getItemCount() {
            return mCurPhotos.size() + 1;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
