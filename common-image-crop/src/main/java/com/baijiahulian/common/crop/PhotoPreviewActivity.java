package com.baijiahulian.common.crop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baijiahulian.common.crop.adapter.PhotoPreviewAdapter;
import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.uikit.CCButton;
import com.baijiahulian.common.crop.uikit.GFViewPager;
import com.baijiahulian.common.crop.uikit.zoonview.PhotoViewAttacher;
import com.baijiahulian.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanglei on 16/1/25.
 */
public class PhotoPreviewActivity extends PhotoBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {

    public static void launch(Context context, List<PhotoInfo> photoList) {
        launch(context, photoList, 0);
    }

    public static void launch(Context context, List<PhotoInfo> photoList, int initialiIndex) {
        if (photoList == null || photoList.size() == 0) return;
        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        intent.putExtra(PHOTO_LIST, (Serializable) photoList);
        intent.putExtra(PHOTO_LIST_INITIAL_INDEX, initialiIndex);
        context.startActivity(intent);
    }


    private static final String PHOTO_LIST = "photo_list";
    private static final String PHOTO_LIST_INITIAL_INDEX = "photo_list_initial_index";

    private GFViewPager viewPager;
    private TextView mTitleView;
    private ImageButton mBackButton;
    private CCButton mRightButton;

    private List<PhotoInfo> mPhotoLists;
    private int mInitialIndex;

    private PhotoPreviewAdapter mPreviewAdapter;
    private View mTitleBar;
    private CheckBox mCheckBox;
    private View mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photos);

        mPhotoLists = (List<PhotoInfo>) getIntent().getSerializableExtra(PHOTO_LIST);
        mInitialIndex = getIntent().getIntExtra(PHOTO_LIST_INITIAL_INDEX, 0);

        mTitleBar = findViewById(R.id.common_crop_title_bar);

        viewPager = (GFViewPager) findViewById(R.id.vp_pager);
        mTitleView = (TextView) findViewById(R.id.title_bar_title_tv);
        mBackButton = (ImageButton) findViewById(R.id.title_bar_back_button_ib);
        mRightButton = (CCButton) findViewById(R.id.title_bar_right_button_cb);

        mBackButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);

        mPreviewAdapter = new PhotoPreviewAdapter(this, mPhotoLists);
        viewPager.setAdapter(mPreviewAdapter);
        viewPager.setCurrentItem(mInitialIndex);
        viewPager.addOnPageChangeListener(this);


        mCheckBox = (CheckBox) findViewById(R.id.activity_preview_photos_checkbox);
        PhotoInfo photoInfo = mPhotoLists.get(mInitialIndex);
        mCheckBox.setChecked(ImageCropProxy.getFunctionConfig().getSelectedList().containsKey(photoInfo.getPhotoPath()));

        mRightButton.setBackgroundColor(ImageCropProxy.getThemeConfig().getMainElementsColor());
        mCheckBox.setOnCheckedChangeListener(this);
        setTitleText(mInitialIndex);

        mBottomBar = findViewById(R.id.activity_preview_bottom_bar);

        if (! ImageCropProxy.getFunctionConfig().isMultiModel()) {
            mBottomBar.setVisibility(View.GONE);
        }

        mBottomBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleTitleBar();
            }
        }, 1000);
    }

    public void toggleTitleBar() {
        boolean isSingle = !ImageCropProxy.getFunctionConfig().isMultiModel();
        if (mTitleBar.getVisibility() == View.VISIBLE) {
            mTitleBar.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.GONE);
            mTitleBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.common_crop_out_bottom_to_top));
            if (! isSingle)
                mBottomBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.common_crop_out_fade));
        } else {
            mTitleBar.setVisibility(View.VISIBLE);
            mTitleBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.common_crop_in_top_to_bottom));
            if (! isSingle) {
                mBottomBar.setVisibility(View.VISIBLE);
                mBottomBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.common_crop_in_fade));
            }
        }

        if (isSingle)
            mBottomBar.setVisibility(View.GONE);

    }

    private void setTitleText(int position) {
        mTitleView.setText(getString(R.string.common_crop_preview_p2, position + 1, mPhotoLists.size()));

        String rightText = getString(ImageCropProxy.getThemeConfig().getTitleBarRightButtonText());

        if (ImageCropProxy.getFunctionConfig().isMultiModel()) {
            if (ImageCropProxy.getFunctionConfig().getSelectedList().size() == 0) {
                mRightButton.setEnabled(false);
            } else {
                mRightButton.setEnabled(true);
                rightText += "(" + ImageCropProxy.getFunctionConfig().getSelectedList().size() + "/" +
                        ImageCropProxy.getFunctionConfig().getMaxSize() + ")";
            }
        }
        mRightButton.setText(rightText);
    }

    @Override
    protected void takeResult(PhotoInfo info) {
    }

    @Override
    public void onClick(View v) {
        if (v == mBackButton) {
            finish();
        } else if (v == mRightButton) {
            ImageCropProxy.getFunctionConfig().getSelectedList().clear();
            PhotoInfo photoInfo = mPhotoLists.get(viewPager.getCurrentItem());
            ImageCropProxy.getFunctionConfig().getSelectedList().put(photoInfo.getPhotoPath(), photoInfo);
            resultData();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPreviewAdapter.getCurrentPagePhotoView() != null) {
            mPreviewAdapter.getCurrentPagePhotoView().setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    toggleTitleBar();
                }
            });
        }
    }

    @Override
    public void onPageSelected(int position) {
        setTitleText(position);
        PhotoInfo photoInfo = mPhotoLists.get(position);
        mCheckBox.setChecked(ImageCropProxy.getFunctionConfig().getSelectedList().containsKey(photoInfo.getPhotoPath()));
        mCheckBox.setTag(photoInfo.getPhotoPath());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String tagPath = (String) buttonView.getTag();
        PhotoInfo photoInfo = mPhotoLists.get(viewPager.getCurrentItem());
        if (tagPath != null && !StringUtils.isEquals(tagPath, photoInfo.getPhotoPath())) {
            return;
        }

        if (isChecked && ImageCropProxy.getFunctionConfig().getSelectedList().size() <
                ImageCropProxy.getFunctionConfig().getMaxSize()) {
            ImageCropProxy.getFunctionConfig().getSelectedList().put(photoInfo.getPhotoPath(),
                    photoInfo);
        } else if (isChecked && !ImageCropProxy.getFunctionConfig().getSelectedList().containsKey(photoInfo.getPhotoPath())) {
            toast(getString(R.string.common_crop_tips_max_count, ImageCropProxy.getFunctionConfig().getMaxSize()));
            buttonView.setChecked(false);
        } else if (!isChecked) {
            ImageCropProxy.getFunctionConfig().getSelectedList().remove(photoInfo.getPhotoPath());
        }

        setTitleText(viewPager.getCurrentItem());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTO_LIST, (Serializable) mPhotoLists);
        outState.putInt(PHOTO_LIST_INITIAL_INDEX, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInitialIndex = savedInstanceState.getInt(PHOTO_LIST_INITIAL_INDEX);
        mPhotoLists = (List<PhotoInfo>) savedInstanceState.getSerializable(PHOTO_LIST);
    }
}
