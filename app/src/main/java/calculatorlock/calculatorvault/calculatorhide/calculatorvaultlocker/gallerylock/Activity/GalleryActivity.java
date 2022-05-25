package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.GalleryFeatureAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.BuildConfig;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.GalleryEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Video;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

public class GalleryActivity extends BaseActivity {
    private final List<GalleryEnt> mGalleryGirdList = new ArrayList<>();
    private final ArrayList<String> mPhotosList = new ArrayList<>();
    private final ArrayList<String> files = new ArrayList<>();

    private GalleryFeatureAdapter galleryFeatureAdapter;

    private LinearLayout ll_Edit;
    private LinearLayout ll_photo_video_empty;
    private LinearLayout ll_photo_video_grid;

    private ImageView photo_video_empty_icon;
    private SensorManager sensorManager;
    private ProgressDialog myProgressDialog;

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 2) {
                hideProgress();
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(GalleryActivity.this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(GalleryActivity.this, R.string.Delete_error, Toast.LENGTH_SHORT).show();
                }
            } else if (message.what == 4) {
                Toast.makeText(GalleryActivity.this, R.string.toast_share, Toast.LENGTH_LONG).show();
            } else if (message.what == 3) {
                if (Common.isUnHide) {
                    RefershGalleryforKitkat();
                    Common.isUnHide = false;
                    Toast.makeText(GalleryActivity.this, R.string.toast_unhide, Toast.LENGTH_LONG).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(GalleryActivity.this, GalleryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(GalleryActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent2 = new Intent(GalleryActivity.this, GalleryActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        finish();
                    }
                }
            }
            super.handleMessage(message);
        }
    };

    private GridView galleryGrid;
    private TextView lbl_photo_video_empty;
    private int selectCount;
    private boolean IsSelectAll = false;
    private boolean isAddbarvisible = false;
    private boolean isEditMode = false;
    private boolean IsSortingDropdown = false;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void showUnhideProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Please be patient... this may take a few moments...", true);
    }

    public void showDeleteProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Please be patient... this may take a few moments...", true);
    }

    public void hideProgress() {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    private void showCopyFilesProcessForShareProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Please be patient... this may take a few moments...", true);
    }

    public void RefershGalleryforKitkat() {
        Uri fromFile = Uri.fromFile(new File(Constants.FILE + Environment.getExternalStorageDirectory()));
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(fromFile);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery);

        SecurityLocksCommon.IsAppDeactive = true;

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(GalleryActivity.this, ll_banner);

        LinearLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        LinearLayout ll_unhide_btn = findViewById(R.id.ll_unhide_btn);
        LinearLayout ll_share_btn = findViewById(R.id.ll_share_btn);
        Toolbar toolbar = findViewById(R.id.toolbar);

        this.galleryGrid = findViewById(R.id.customGalleryGrid);

        this.ll_Edit = findViewById(R.id.ll_Edit);
        this.ll_photo_video_empty = findViewById(R.id.ll_photo_video_empty);
        this.ll_photo_video_grid = findViewById(R.id.ll_photo_video_grid);
        this.photo_video_empty_icon = findViewById(R.id.photo_video_empty_icon);
        this.lbl_photo_video_empty = findViewById(R.id.lbl_photo_video_empty);

        setSupportActionBar(toolbar);
        getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        toolbar.setNavigationOnClickListener(view -> Back());

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.ll_photo_video_grid.setVisibility(View.VISIBLE);
        this.ll_photo_video_empty.setVisibility(View.INVISIBLE);

        this.galleryGrid.setOnItemClickListener(new galleryListners());
        this.galleryGrid.setOnItemLongClickListener(new galleryListners());

        ll_delete_btn.setOnClickListener(new galleryListners());
        ll_unhide_btn.setOnClickListener(new galleryListners());
        ll_share_btn.setOnClickListener(new galleryListners());
        ll_background.setOnTouchListener(new galleryListners());

        LoadGalleryFilesFromDB();
    }

    private void LoadGalleryFilesFromDB() {
        new ArrayList<>();
        PhotoDAL photoDAL = new PhotoDAL(this);
        photoDAL.OpenRead();
        List<Photo> GetPhotos = photoDAL.GetPhotos();
        photoDAL.close();
        for (Photo photo : GetPhotos) {
            GalleryEnt galleryEnt = new GalleryEnt();
            galleryEnt.set_albumId(photo.getAlbumId());
            galleryEnt.set_folderLockgalleryfileLocation(photo.getFolderLockPhotoLocation());
            galleryEnt.set_galleryfileName(photo.getPhotoName());
            galleryEnt.set_id(photo.getId());
            galleryEnt.set_isCheck(false);
            galleryEnt.set_isVideo(false);
            galleryEnt.set_modifiedDateTime(photo.get_modifiedDateTime());
            galleryEnt.set_originalgalleryfileLocation(photo.getOriginalPhotoLocation());
            galleryEnt.set_thumbnail_video_location("");
            this.mGalleryGirdList.add(galleryEnt);
        }
        new ArrayList<>();
        VideoDAL videoDAL = new VideoDAL(this);
        videoDAL.OpenRead();
        List<Video> GetVideos = videoDAL.GetVideos();
        videoDAL.close();
        for (Video video : GetVideos) {
            GalleryEnt galleryEnt2 = new GalleryEnt();
            galleryEnt2.set_albumId(video.getAlbumId());
            galleryEnt2.set_folderLockgalleryfileLocation(video.getFolderLockVideoLocation());
            galleryEnt2.set_galleryfileName(video.getVideoName());
            galleryEnt2.set_id(video.getId());
            galleryEnt2.set_isCheck(false);
            galleryEnt2.set_isVideo(true);
            galleryEnt2.set_modifiedDateTime(video.get_modifiedDateTime());
            galleryEnt2.set_originalgalleryfileLocation(video.getOriginalVideoLocation());
            galleryEnt2.set_thumbnail_video_location(video.getthumbnail_video_location());
            this.mGalleryGirdList.add(galleryEnt2);
        }

        Collections.reverse(this.mGalleryGirdList);
        for (GalleryEnt galleryEnt3 : this.mGalleryGirdList) {
            if (!galleryEnt3.get_isVideo()) {
                this.mPhotosList.add(galleryEnt3.get_folderLockgalleryfileLocation());
            }
        }
        galleryFeatureAdapter = new GalleryFeatureAdapter(this, 1, this.mGalleryGirdList, false);
        this.galleryGrid.setAdapter(galleryFeatureAdapter);
        this.galleryFeatureAdapter.notifyDataSetChanged();
        if (this.mGalleryGirdList.size() < 1) {
            this.ll_photo_video_grid.setVisibility(View.INVISIBLE);
            this.ll_photo_video_empty.setVisibility(View.VISIBLE);
            this.photo_video_empty_icon.setBackgroundResource(R.drawable.ic_gallery_empty_icon);
            this.lbl_photo_video_empty.setText(R.string.lbl_No_Gallery_File);
            return;
        }
        this.ll_photo_video_grid.setVisibility(View.VISIBLE);
        this.ll_photo_video_empty.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void UnhideGalleryFiles() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_unhide, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        if (Common.GetTotalFree() > Common.GetFileSize(this.files)) {
            final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.confirmation_message_box);
            dialog.setCancelable(true);
            TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
            textView.setText("Are you sure you want to restore (" + this.selectCount + ") photo(s) or video(s)?");

            dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
                showUnhideProgress();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            dialog.dismiss();
                            Common.isUnHide = true;
                            Unhide();
                            Common.IsWorkInProgress = true;
                            Message message = new Message();
                            message.what = 3;
                            handle.sendMessage(message);
                            Common.IsWorkInProgress = false;
                        } catch (Exception e) {
                            Message message2 = new Message();
                            message2.what = 3;
                            handle.sendMessage(message2);
                        }
                    }
                }.start();
                dialog.dismiss();
            });

            dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    public void Unhide() throws IOException {
        for (int i = 0; i < this.mGalleryGirdList.size(); i++) {
            if (this.mGalleryGirdList.get(i).get_isCheck()) {
                if (!Utilities.NSUnHideFile(this, this.mGalleryGirdList.get(i).get_folderLockgalleryfileLocation(), this.mGalleryGirdList.get(i).get_originalgalleryfileLocation())) {
                    Toast.makeText(this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                } else if (this.mGalleryGirdList.get(i).get_isVideo()) {
                    DeleteVideosFromDatabase(this.mGalleryGirdList.get(i).get_id());
                } else {
                    DeletePhotosFromDatabase(this.mGalleryGirdList.get(i).get_id());
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void DeleteGalleryFiles() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_delete, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setText("Are you sure you want to delete (" + this.selectCount + ") photo(s) or video(s)?");

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            showDeleteProgress();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Common.isDelete = true;
                        dialog.dismiss();
                        Delete();
                        Common.IsWorkInProgress = true;
                        Message message = new Message();
                        message.what = 3;
                        handle.sendMessage(message);
                        Common.IsWorkInProgress = false;
                    } catch (Exception unused) {
                        Message message2 = new Message();
                        message2.what = 3;
                        handle.sendMessage(message2);
                    }
                }
            }.start();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void SelectedCount() {
        this.files.clear();
        this.selectCount = 0;
        for (int i = 0; i < this.mGalleryGirdList.size(); i++) {
            if (this.mGalleryGirdList.get(i).get_isCheck()) {
                this.files.add(this.mGalleryGirdList.get(i).get_folderLockgalleryfileLocation());
                this.selectCount++;
            }
        }
    }

    public boolean IsFileCheck() {
        for (int i = 0; i < this.mGalleryGirdList.size(); i++) {
            if (this.mGalleryGirdList.get(i).get_isCheck()) {
                return true;
            }
        }
        return false;
    }

    public void Delete() {
        for (int i = 0; i < this.mGalleryGirdList.size(); i++) {
            if (this.mGalleryGirdList.get(i).get_isCheck()) {
                new File(this.mGalleryGirdList.get(i).get_folderLockgalleryfileLocation()).delete();
                new File(this.mGalleryGirdList.get(i).get_thumbnail_video_location()).delete();
                if (this.mGalleryGirdList.get(i).get_isVideo()) {
                    DeleteVideosFromDatabase(this.mGalleryGirdList.get(i).get_id());
                } else {
                    DeletePhotosFromDatabase(this.mGalleryGirdList.get(i).get_id());
                }
            }
        }
    }

    public void DeletePhotosFromDatabase(int i) {
        PhotoDAL photoDAL = new PhotoDAL(this);
        try {
            photoDAL.OpenWrite();
            photoDAL.DeletePhotoById(i);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoDAL.close();
            throw th;
        }
        photoDAL.close();
    }

    public void DeleteVideosFromDatabase(int i) {
        VideoDAL videoDAL = new VideoDAL(this);
        try {
            videoDAL.OpenWrite();
            videoDAL.DeleteVideoById(i);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoDAL.close();
            throw th;
        }
        videoDAL.close();
    }

    public void ShareGalleryFiles() {
        showCopyFilesProcessForShareProgress();
        new Thread() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void run() {
                try {
                    SecurityLocksCommon.IsAppDeactive = false;
                    ArrayList<Intent> arrayList = new ArrayList<>();
                    Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
                    intent.setType("image/*");
                    for (ResolveInfo resolveInfo : getPackageManager().queryIntentActivities(intent, 0)) {
                        String str = resolveInfo.activityInfo.packageName;
                        if (!str.equals(getPackageName()) && !str.equals("com.dropbox.android") && !str.equals("com.facebook.katana")) {
                            Intent intent2 = new Intent("android.intent.action.SEND_MULTIPLE");
                            intent2.setType("image/*");
                            intent2.setPackage(str);
                            arrayList.add(intent2);
                            String str2 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TEMPFILES;
                            ArrayList<String> arrayList2 = new ArrayList<>();
                            ArrayList<Uri> arrayList3 = new ArrayList<>();
                            for (int i = 0; i < mGalleryGirdList.size(); i++) {
                                if (mGalleryGirdList.get(i).get_isCheck()) {
                                    try {
                                        str2 = Utilities.CopyTemporaryFile(GalleryActivity.this, mGalleryGirdList.get(i).get_folderLockgalleryfileLocation(), str2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    arrayList2.add(str2);
                                    arrayList3.add(FileProvider.getUriForFile(GalleryActivity.this, BuildConfig.APPLICATION_ID, new File(str2)));
                                }
                            }
                            intent2.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList3);
                        }
                    }
                    Intent createChooser = Intent.createChooser(arrayList.remove(0), "Share Via");
                    createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", arrayList.toArray(new Parcelable[0]));
                    startActivity(createChooser);
                    Message message = new Message();
                    message.what = 4;
                    handle.sendMessage(message);
                } catch (Exception unused) {
                    Message message2 = new Message();
                    message2.what = 4;
                    handle.sendMessage(message2);
                }
            }
        }.start();
    }

    public void DeleteTemporaryGalleryFiles() {
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TEMPFILES + "/");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            assert listFiles != null;
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    file2.delete();
                }
            }
        }
    }

    private void SetcheckFlase() {
        for (int i = 0; i < this.mGalleryGirdList.size(); i++) {
            this.mGalleryGirdList.get(i).set_isCheck(false);
        }
        galleryFeatureAdapter = new GalleryFeatureAdapter(this, 1, this.mGalleryGirdList, false);
        this.galleryGrid.setAdapter(galleryFeatureAdapter);
        this.galleryFeatureAdapter.notifyDataSetChanged();
        if (Common.GalleryThumbnailCurrentPosition != 0) {
            this.galleryGrid.setSelection(Common.GalleryThumbnailCurrentPosition);
            Common.GalleryThumbnailCurrentPosition = 0;
        }
    }

    public void SelectOrUnSelectAll() {
        if (this.IsSelectAll) {
            for (int i = 0; i < this.mGalleryGirdList.size(); i++) {
                this.mGalleryGirdList.get(i).set_isCheck(false);
            }
            this.IsSelectAll = false;
        } else {
            for (int i2 = 0; i2 < this.mGalleryGirdList.size(); i2++) {
                this.mGalleryGirdList.get(i2).set_isCheck(true);
            }
            this.IsSelectAll = true;
        }
        galleryFeatureAdapter = new GalleryFeatureAdapter(this, 1, this.mGalleryGirdList, true);
        this.galleryGrid.setAdapter(galleryFeatureAdapter);
        this.galleryFeatureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void btnBackonClick(View view) {
        Back();
    }

    public void Back() {
        if (this.isEditMode) {
            SetcheckFlase();
            this.isEditMode = false;
            this.IsSortingDropdown = false;
            this.ll_Edit.setVisibility(View.GONE);
            this.IsSelectAll = false;
            invalidateOptionsMenu();
        } else if (this.isAddbarvisible) {
            this.isAddbarvisible = false;
            this.ll_Edit.setVisibility(View.GONE);
            this.IsSortingDropdown = false;
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            Common.PhotoFolderName = "My Photos";
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
        DeleteTemporaryGalleryFiles();
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
        Common.IsWorkInProgress = true;
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
        this.handle.removeCallbacksAndMessages(null);
        this.sensorManager.unregisterListener(this);
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
        SetcheckFlase();
        Utilities.changeFileExtention(StorageOptionsCommon.VIDEOS);
        this.isEditMode = false;
        this.ll_Edit.setVisibility(View.GONE);
        this.IsSelectAll = false;
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void onStop() {
        Common.imageLoader.clearMemoryCache();
        Common.imageLoader.clearDiskCache();
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            Common.isOpenCameraorGalleryFromApp = false;
            if (this.isEditMode) {
                SetcheckFlase();
                this.IsSortingDropdown = false;
                this.isEditMode = false;
                this.ll_Edit.setVisibility(View.GONE);
                this.IsSelectAll = false;
                invalidateOptionsMenu();
                return true;
            } else if (this.isAddbarvisible) {
                this.isAddbarvisible = false;
                this.ll_Edit.setVisibility(View.GONE);
                this.IsSortingDropdown = false;
                return true;
            } else {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.FolderId = 0;
                Common.PhotoFolderName = "My Photos";
                startActivity(new Intent(this, ActivityMain.class));
                finish();
                DeleteTemporaryGalleryFiles();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void PlayVideo(String str) {
        String str2;
        File file = new File(str);
        SecurityLocksCommon.IsAppDeactive = false;
        if (file.exists()) {
            try {
                str2 = Utilities.NSVideoDecryptionDuringPlay(new File(str));
            } catch (IOException e) {
                e.printStackTrace();
                str2 = "";
            }
        } else {
            str2 = file.getParent() + File.separator + Utilities.ChangeFileExtentionToOrignal(Utilities.FileName(file.getAbsolutePath()));
        }
        String substring = str2.substring(str2.lastIndexOf(".") + 1);

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, new File(str2)), MimeTypeMap.getSingleton().getMimeTypeFromExtension(substring));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        menu.findItem(R.id.action_more);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.isEditMode) {
            menu.findItem(R.id.action_more).setVisible(false);
            getMenuInflater().inflate(R.menu.menu_selection, menu);
        } else {
            menu.findItem(R.id.action_more).setVisible(false);
            if (this.IsSelectAll && this.isEditMode) {
                menu.findItem(R.id.action_select).setIcon(R.drawable.ic_unselectallicon);
            } else if (!this.IsSelectAll && this.isEditMode) {
                menu.findItem(R.id.action_select).setIcon(R.drawable.ic_selectallicon);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private class galleryListners implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnTouchListener {
        private galleryListners() {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnSelectAll:
                    SelectOrUnSelectAll();
                    return;
                case R.id.ll_delete_btn:
                    DeleteGalleryFiles();
                    return;
                case R.id.ll_share_btn:
                    if (!IsFileCheck()) {
                        Toast.makeText(GalleryActivity.this, R.string.toast_unselectphotovideomsg_share, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        ShareGalleryFiles();
                        return;
                    }
                case R.id.ll_unhide_btn:
                    UnhideGalleryFiles();
                    return;
                default:
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            Common.GalleryThumbnailCurrentPosition = galleryGrid.getFirstVisiblePosition();
            isEditMode = true;
            ll_Edit.setVisibility(View.VISIBLE);
            findViewById(R.id.ll_move_btn).setVisibility(View.GONE);
            invalidateOptionsMenu();
            mGalleryGirdList.get(i).set_isCheck(true);
            galleryFeatureAdapter = new GalleryFeatureAdapter(GalleryActivity.this, 1, mGalleryGirdList, true);
            galleryGrid.setAdapter(galleryFeatureAdapter);
            galleryFeatureAdapter.notifyDataSetChanged();
            if (Common.GalleryThumbnailCurrentPosition != 0) {
                galleryGrid.setSelection(Common.GalleryThumbnailCurrentPosition);
            }
            return true;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            Common.GalleryThumbnailCurrentPosition = galleryGrid.getFirstVisiblePosition();
            if (!isEditMode) {
                if (mGalleryGirdList.get(i).get_isVideo()) {
                    PlayVideo(mGalleryGirdList.get(i).get_folderLockgalleryfileLocation());
                    return;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= mPhotosList.size()) {
                        i2 = 0;
                        break;
                    } else if (mGalleryGirdList.get(i).get_folderLockgalleryfileLocation().endsWith(mPhotosList.get(i2))) {
                        break;
                    } else {
                        i2++;
                    }
                }
                SecurityLocksCommon.IsAppDeactive = false;
                Common.IsCameFromGalleryFeature = true;
                Intent intent = new Intent(GalleryActivity.this, NewFullScreenViewActivity.class);
                intent.putExtra("IMAGE_URL", mGalleryGirdList.get(i).get_folderLockgalleryfileLocation());
                intent.putExtra("IMAGE_POSITION", i2);
                intent.putStringArrayListExtra("mPhotosList", mPhotosList);
                startActivity(intent);
                finish();
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == R.id.ll_background && IsSortingDropdown) {
                IsSortingDropdown = false;
            }
            return false;
        }
    }
}
