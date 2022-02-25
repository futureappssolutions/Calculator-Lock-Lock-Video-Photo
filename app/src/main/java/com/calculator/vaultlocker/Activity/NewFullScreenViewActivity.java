package com.calculator.vaultlocker.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.calculator.vaultlocker.DB.PhotoDAL;
import com.calculator.vaultlocker.Model.ImageData;
import com.calculator.vaultlocker.Model.Photo;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.common.TouchImageView;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class NewFullScreenViewActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private final List<ImageData> imList = new ArrayList<>();
    private DisplayImageOptions options;
    private SensorManager sensorManager;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fullscreen_view);

        Common.IsPhoneGalleryLoad = true;
        SecurityLocksCommon.IsAppDeactive = true;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_photo_empty_icon).showImageOnFail(R.drawable.ic_photo_empty_icon).resetViewBeforeLoading(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(HttpStatus.SC_MULTIPLE_CHOICES)).build();

        int i = 0;
        int m_imagePosition = getIntent().getIntExtra("IMAGE_POSITION", 0);
        int _SortType = getIntent().getIntExtra("_SortBy", 0);
        int albumId = getIntent().getIntExtra("ALBUM_ID", 0);
        ArrayList<String> mPhotosList = getIntent().getStringArrayListExtra("mPhotosList");

        if (Common.IsCameFromAppGallery || mPhotosList == null) {
            PhotoDAL photoDAL = new PhotoDAL(this);
            photoDAL.OpenRead();
            List<Photo> photo = photoDAL.GetPhotoByAlbumId(albumId, _SortType);
            photoDAL.close();
            while (i < photo.size()) {
                ImageData imageData = new ImageData();
                imageData.setImgPath(photo.get(i).getFolderLockPhotoLocation());
                imList.add(imageData);
                i++;
            }
        } else {
            while (i < mPhotosList.size()) {
                ImageData imageData2 = new ImageData();
                imageData2.setImgPath(mPhotosList.get(i));
                imList.add(imageData2);
                i++;
            }
        }
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new ImageAdapter());
        viewPager.setCurrentItem(m_imagePosition);
    }

    @Override
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (Common.IsCameFromAppGallery) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.IsCameFromAppGallery = false;
                startActivity(new Intent(this, Photos_Gallery_Actitvity.class));
                finish();
            } else if (Common.IsCameFromGalleryFeature) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.IsCameFromGalleryFeature = false;
                startActivity(new Intent(this, GalleryActivity.class));
                finish();
            } else {
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(this, ActivityMain.class));
                finish();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private class ImageAdapter extends PagerAdapter {
        private final LayoutInflater inflater;

        ImageAdapter() {
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int i, @NonNull Object obj) {
            viewGroup.removeView((View) obj);
        }

        @Override
        public int getCount() {
            return imList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
            View inflate = inflater.inflate(R.layout.item_pager_image, viewGroup, false);

            TouchImageView touchImageView = inflate.findViewById(R.id.image);
            final ProgressBar progressBar = inflate.findViewById(R.id.loading);
            touchImageView.setMaxZoom(6.0f);

            ImageLoader.getInstance().displayImage("file:///" + imList.get(i).getImgPath(), touchImageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String str, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String str, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            viewGroup.addView(inflate, 0);
            return inflate;
        }

        @Override
        public boolean isViewFromObject(View view, @NonNull Object obj) {
            return view.equals(obj);
        }
    }
}
