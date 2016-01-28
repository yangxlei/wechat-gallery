package crop.common.baijiahulian.com.mygallery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.baijiahulian.common.crop.BJCommonImageCropHelper;
import com.baijiahulian.common.crop.ThemeConfig;
import com.baijiahulian.common.crop.model.PhotoInfo;
import com.baijiahulian.common.crop.uikit.GFImageView;
import com.baijiahulian.common.crop.utils.FrescoImageLoader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BJCommonImageCropHelper.OnHandlerResultCallback {

    private Button button01, button02,button03,button04,button05;

    private ThemeConfig themeConfig;
    private RecyclerView mPictures;
    private PicturesAdapter mAdapter;

    FrescoImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ImageLoader.init(this, new File(Environment.getExternalStorageDirectory(), "/Fresco/"));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageLoader = new FrescoImageLoader(this);

        button01 = (Button) findViewById(R.id.button01);
        button02 = (Button) findViewById(R.id.button02);
        button03 = (Button) findViewById(R.id.button03);
        button04 = (Button) findViewById(R.id.button04);
        button05 = (Button) findViewById(R.id.button05);

        button01.setOnClickListener(this);
        button02.setOnClickListener(this);
        button03.setOnClickListener(this);
        button04.setOnClickListener(this);
        button05.setOnClickListener(this);

        themeConfig = new ThemeConfig.Builder().setMainElementsColor(Color.parseColor("#00ccff")).
            setTitlebarRightButtonText(R.string.user).build();
        mPictures = (RecyclerView) findViewById(R.id.pictures);
        mPictures.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new PicturesAdapter();
        mPictures.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ThemeConfig theme = new ThemeConfig.Builder().setMainElementsColor(Color.parseColor("#ff9100")).build();
            BJCommonImageCropHelper.openImageMulti(this, 4, theme, new BJCommonImageCropHelper.OnHandlerResultCallback() {
                @Override
                public void onHandlerSuccess(List<PhotoInfo> resultList) {

                }

                @Override
                public void onHandlerFailure(String errorMsg) {

                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button01:
                // 相册多选
                BJCommonImageCropHelper.openImageMulti(this, 5, themeConfig, this);
                break;
            case R.id.button02:
                //相册单选，不裁图
                BJCommonImageCropHelper.openImageSingleAblum(this, BJCommonImageCropHelper.PhotoCropType.None, themeConfig, this);
                break;
            case R.id.button03:
                //相册单选， 自由裁图
                BJCommonImageCropHelper.openImageSingleAblum(this, BJCommonImageCropHelper.PhotoCropType.Free, themeConfig, this);
                break;
            case R.id.button04:
                // 拍照，不裁图
                BJCommonImageCropHelper.openImageSingleCamera(this, BJCommonImageCropHelper.PhotoCropType.None, themeConfig, this);
                break;
            case R.id.button05:
                // 拍照， 16：9 裁图
                BJCommonImageCropHelper.openImageSingleCamera(this, BJCommonImageCropHelper.PhotoCropType.Rectangle16To9, themeConfig, this);
                break;
        }

    }

    @Override
    public void onHandlerSuccess(List<PhotoInfo> resultList) {
        System.out.println("onHandlerSuccess " + resultList);

        mAdapter.setPhotoList(resultList);
    }

    @Override
    public void onHandlerFailure(String errorMsg) {

    }

    private class PicturesAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<PhotoInfo> photoInfoList = null;
        public void setPhotoList(List<PhotoInfo> list) {
            photoInfoList = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pictures, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PhotoInfo photoInfo = photoInfoList.get(position);
//            ImageLoader.displayImage(new File(photoInfo.getPhotoPath()), holder.imageView,
//                    new ImageOptions.Builder().build());
            imageLoader.displayImage(MainActivity.this, photoInfo.getPhotoPath(), holder.imageView,
                    300, 300);
        }

        @Override
        public int getItemCount() {
            return  photoInfoList == null ? 0 : photoInfoList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private GFImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (GFImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
