package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportAlbumEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Video;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.VideoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.OnItemClickListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.AlbumsImportGalleryVideoAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.GalleryVideoAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoDAL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImportAlbumsGalleryVideoActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener, OnItemClickListener {
    private final ArrayList<ImportEnt> videoImportEntList = new ArrayList<>();
    private final ArrayList<ImportAlbumEnt> importAlbumEnts = new ArrayList<>();
    private final ArrayList<String> spinnerValues = new ArrayList<>();
    private final List<List<ImportEnt>> videoImportEntListShowList = new ArrayList<>();
    private final ArrayList<String> selectPath = new ArrayList<>();
    private ArrayList<ImportEnt> videoImportEntListShow = new ArrayList<>();

    private String folderName;
    private GridView imagegrid;
    private TextView lbl_import_photo_album_topbaar;
    private TextView lbl_photo_video_empty;
    private AppCompatButton btnImport;
    private LinearLayout ll_photo_video_empty;
    private ImageView photo_video_empty_icon;
    private SensorManager sensorManager;
    private RecyclerView album_import_ListView;
    private AppCompatImageView btnSelectAll;
    private ProgressBar Progress;
    private AlbumsImportGalleryVideoAdapter adapter;
    private GalleryVideoAdapter galleryImagesAdapter;

    private ProgressDialog myProgressDialog = null;
    private int selectCount;
    private int folderId;
    private boolean isAlbumClick = false;
    private boolean IsExceptionInImportVideos = false;
    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Intent intent;
            if (message.what == 1) {
                hideProgress();
                if (videoImportEntListShow.size() > 0) {
                    galleryImagesAdapter = new GalleryVideoAdapter(ImportAlbumsGalleryVideoActivity.this, 1, videoImportEntListShow);
                    imagegrid.setAdapter(galleryImagesAdapter);
                } else {
                    btnSelectAll.setEnabled(false);
                    btnImport.setEnabled(false);
                }
            } else if (message.what == 4) {
                Progress.setVisibility(View.GONE);
                GetAlbumsFromGallery();
            } else if (message.what == 3) {
                if (Common.IsImporting) {
                    RefershGalleryforKitkat();
                    Common.IsImporting = false;
                    if (IsExceptionInImportVideos) {
                        IsExceptionInImportVideos = false;
                    } else {
                        Toast.makeText(ImportAlbumsGalleryVideoActivity.this, selectCount + " video(s) imported successfully", Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        if (Common.IsCameFromPhotoAlbum) {
                            if (isAlbumClick) {
                                intent = new Intent(ImportAlbumsGalleryVideoActivity.this, Videos_Gallery_Actitvity.class);
                            } else {
                                intent = new Intent(ImportAlbumsGalleryVideoActivity.this, VideosAlbumActivty.class);
                            }
                        } else if (Common.IsCameFromGalleryFeature) {
                            Common.IsCameFromGalleryFeature = false;
                            intent = new Intent(ImportAlbumsGalleryVideoActivity.this, GalleryActivity.class);
                        } else if (isAlbumClick) {
                            intent = new Intent(ImportAlbumsGalleryVideoActivity.this, Videos_Gallery_Actitvity.class);
                        } else {
                            intent = new Intent(ImportAlbumsGalleryVideoActivity.this, VideosAlbumActivty.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            } else if (message.what == 2) {
                hideProgress();
            }
            super.handleMessage(message);
        }
    };
    private boolean IsSelectAll = false;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void RefershGalleryforKitkat() {
        Uri fromFile = Uri.fromFile(new File(Constants.FILE + Environment.getExternalStorageDirectory()));
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(fromFile);
        sendBroadcast(intent);
    }

    public void ShowImportProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Your data is being encrypted... this may take a few moments...", true);
    }

    public void hideProgress() {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_import_album_list);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBanner(ImportAlbumsGalleryVideoActivity.this, ll_banner);


        Progress = findViewById(R.id.prbLoading);
        album_import_ListView = findViewById(R.id.album_import_ListView);
        imagegrid = findViewById(R.id.customGalleryGrid);
        lbl_import_photo_album_topbaar = findViewById(R.id.lbl_import_photo_album_topbaar);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnImport = findViewById(R.id.btnImport);
        ll_photo_video_empty = findViewById(R.id.ll_photo_video_empty);
        photo_video_empty_icon = findViewById(R.id.photo_video_empty_icon);
        lbl_photo_video_empty = findViewById(R.id.lbl_photo_video_empty);

        SecurityLocksCommon.IsAppDeactive = true;
        Common.IsWorkInProgress = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lbl_import_photo_album_topbaar.setText(R.string.lbl_import_photo_album_select_album_topbaar);

        album_import_ListView.setLayoutManager(new GridLayoutManager(ImportAlbumsGalleryVideoActivity.this,2));

        folderId = Common.FolderId;
        folderName = null;
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(getApplicationContext());
        videoAlbumDAL.OpenRead();
        VideoAlbum GetAlbumById = videoAlbumDAL.GetAlbumById(Common.FolderId);
        videoAlbumDAL.close();
        folderName = GetAlbumById.getAlbumName();
        String dbFolderPath = (String) getIntent().getSerializableExtra("FOLDER_PATH");

        LoadData();

        btnImport.setOnClickListener(view -> {
            Advertisement.getInstance((ImportAlbumsGalleryVideoActivity.this)).showFull(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    OnImportClick();
                }
            });
        });
    }

    @Override
    public void onItemClick(int position) {
        imagegrid.setVisibility(View.VISIBLE);
        isAlbumClick = true;
        lbl_import_photo_album_topbaar.setText(R.string.lbl_import_video_album_select_video_topbaar);
        album_import_ListView.setVisibility(View.INVISIBLE);
        btnSelectAll.setVisibility(View.VISIBLE);
        adapter = new AlbumsImportGalleryVideoAdapter(ImportAlbumsGalleryVideoActivity.this, importAlbumEnts, false, true, this);
        album_import_ListView.setAdapter(adapter);
        videoImportEntListShow.clear();
        for (ImportEnt next : videoImportEntList) {
            if (spinnerValues.get(position).equals(new File(next.GetPath()).getParent())) {
                next.GetThumbnailSelection();
                videoImportEntListShow.add(next);
            }
        }

        galleryImagesAdapter = new GalleryVideoAdapter(ImportAlbumsGalleryVideoActivity.this, 1, videoImportEntListShow);
        imagegrid.setAdapter(galleryImagesAdapter);
        galleryImagesAdapter.notifyDataSetChanged();
        if (videoImportEntListShow.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            photo_video_empty_icon.setBackgroundResource(R.drawable.ic_video_empty_icon);
            lbl_photo_video_empty.setText(R.string.no_videos);
        }
    }

    public int albumCheckCount() {
        int i = 0;
        for (int i2 = 0; i2 < importAlbumEnts.size(); i2++) {
            if (importAlbumEnts.get(i2).GetAlbumFileCheck()) {
                i++;
            }
        }
        return i;
    }

    public void LoadData() {
        Progress.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {
                    loadGallery();
                } catch (IOException e) {
                    try {
                        e.printStackTrace();
                    } catch (Exception unused) {
                        Message message = new Message();
                        message.what = 4;
                        handle.sendMessage(message);
                        return;
                    }
                }
                for (ImportEnt next : videoImportEntList) {
                    if (spinnerValues.get(0).contains(Objects.requireNonNull(new File(next.GetPath()).getParent()))) {
                        videoImportEntListShow.add(next);
                    }
                }
                Message message2 = new Message();
                message2.what = 4;
                handle.sendMessage(message2);
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    public void OnImportClick() {
        final StorageOptionSharedPreferences GetObject = StorageOptionSharedPreferences.GetObject(this);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectvideomsg_import, Toast.LENGTH_LONG).show();
        } else if (Common.GetFileSize(selectPath) < Common.GetTotalFree()) {
            int albumCheckCount = albumCheckCount();
            if (albumCheckCount >= 2) {
                final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.confirmation_message_box);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);

                textView.setText("Are you sure you want to import " + albumCheckCount + " albums? Importing may take time according to the size of your data.");

                dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
                    if (Build.VERSION.SDK_INT >= 23) {
                        Import();
                    } else if (GetObject.GetSDCardUri().length() > 0) {
                        Import();
                    } else if (!GetObject.GetISDAlertshow()) {
                        final Dialog dialog2 = new Dialog(ImportAlbumsGalleryVideoActivity.this, R.style.FullHeightDialog);
                        dialog2.setContentView(R.layout.sdcard_permission_alert_msgbox);
                        dialog2.setCancelable(false);
                        dialog2.setCanceledOnTouchOutside(false);
                        ((TextView) dialog2.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.lblSdCardAlertMsgVideo);

                        ((CheckBox) dialog2.findViewById(R.id.cbalertdialog)).setOnCheckedChangeListener((compoundButton, z) -> GetObject.SetISDAlertshow(z));

                        dialog2.findViewById(R.id.ll_Ok).setOnClickListener(view2 -> {
                            dialog2.dismiss();
                            Import();
                        });
                        dialog2.show();
                    } else {
                        Import();
                    }
                });

                dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
                    for (int i = 0; i < importAlbumEnts.size(); i++) {
                        importAlbumEnts.get(i).SetAlbumFileCheck(false);
                    }
                    GetAlbumsFromGallery();
                    dialog.dismiss();
                });
                dialog.show();
            } else if (Build.VERSION.SDK_INT >= 23) {
                Import();
            } else if (GetObject.GetSDCardUri().length() > 0) {
                Import();
            } else if (!GetObject.GetISDAlertshow()) {
                final Dialog dialog2 = new Dialog(this, R.style.FullHeightDialog);
                dialog2.setContentView(R.layout.sdcard_permission_alert_msgbox);
                dialog2.setCancelable(false);
                dialog2.setCanceledOnTouchOutside(false);
                ((TextView) dialog2.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.lblSdCardAlertMsgVideo);

                ((CheckBox) dialog2.findViewById(R.id.cbalertdialog)).setOnCheckedChangeListener((compoundButton, z) -> GetObject.SetISDAlertshow(z));

                dialog2.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
                    dialog2.dismiss();
                    Import();
                });

                dialog2.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog2.dismiss());
                dialog2.show();
            } else {
                Import();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void Import() {
        if (!Common.IsCameFromGalleryFeature || !isAlbumClick) {
            SelectedCount();
            ShowImportProgress();
            Common.IsWorkInProgress = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        ImportVideo();
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
            return;
        }
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(getApplicationContext());
        videoAlbumDAL.OpenRead();
        videoAlbumDAL.close();
    }

    public void ImportVideo() throws IOException {
        if (isAlbumClick) {
            ImportOnlyVideosSDCard();
        } else {
            importAlbum();
        }
    }

    public void importAlbum() {
        if (importAlbumEnts.size() > 0) {
            int i = 0;
            for (int i2 = 0; i2 < importAlbumEnts.size(); i2++) {
                if (importAlbumEnts.get(i2).GetAlbumFileCheck()) {
                    File file = new File(importAlbumEnts.get(i2).GetAlbumName());
                    File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + file.getName());
                    folderName = file.getName();
                    if (file2.exists()) {
                        int i3 = 1;
                        while (i3 < 100) {
                            folderName = file.getName() + "(" + i3 + ")";
                            String sb = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + folderName;
                            File file3 = new File(sb);
                            if (!file3.exists()) {
                                i3 = 100;
                            }
                            i3++;
                            file2 = file3;
                        }
                    }
                    AddAlbumToDatabase(folderName, file2.getAbsolutePath());
                    VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(getApplicationContext());
                    videoAlbumDAL.OpenRead();
                    folderId = videoAlbumDAL.GetLastAlbumId();
                    videoAlbumDAL.close();
                    try {
                        ImportAlbumsVideosSDCard(i);
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void ImportAlbumsVideosSDCard(int i) throws IOException {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        List<ImportEnt> list = videoImportEntListShowList.get(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).GetThumbnailSelection()) {
                File file = new File(list.get(i2).GetPath());
                String str = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + folderName + "/";
                File file2 = new File(str);
                new File(str + "VideoThumnails/").mkdirs();
                String FileName = FileName(list.get(i2).GetPath());
                String str2 = str + "VideoThumnails/thumbnil-" + FileName.substring(0, FileName.lastIndexOf(".")) + "#jpg";
                File file3 = new File(str2);
                file3.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                list.get(i2).SetThumbnail(MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), list.get(i2).GetId(), 3, null));
                list.get(i2).GetThumbnail().compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Utilities.NSEncryption(file3);
                try {
                    String NSHideFile = Utilities.NSHideFile(this, file, file2);
                    try {
                        Utilities.NSEncryption(new File(NSHideFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (NSHideFile.length() > 1) {
                        AddVideoToDatabase(FileName(list.get(i2).GetPath()), list.get(i2).GetPath(), str2, NSHideFile);
                        if (Build.VERSION.SDK_INT < 23 && StorageOptionSharedPreferences.GetObject(this).GetSDCardUri().length() > 0) {
                            Utilities.DeleteSDcardImageLollipop(this, file.getAbsolutePath());
                        }
                        try {
                            getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_data='" + file.getPath() + "'", null);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        IsExceptionInImportVideos = true;
                    }
                } catch (Exception e3) {
                    IsExceptionInImportVideos = true;
                    e3.printStackTrace();
                }
            }
        }
    }

    public void ImportOnlyVideosSDCard() throws IOException {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        int size = videoImportEntListShow.size();
        for (int i = 0; i < size; i++) {
            if (videoImportEntListShow.get(i).GetThumbnailSelection()) {
                String str = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + folderName;
                new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + folderName + "/VideoThumnails/").mkdirs();
                String FileName = FileName(videoImportEntListShow.get(i).GetPath());
                String str2 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + folderName + "/VideoThumnails/thumbnil-" + FileName.substring(0, FileName.lastIndexOf(".")) + "#jpg";
                File file = new File(str2);
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                videoImportEntListShow.get(i).GetThumbnail().compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Utilities.NSEncryption(file);
                try {
                    File file2 = new File(videoImportEntListShow.get(i).GetPath());
                    String NSHideFile = Utilities.NSHideFile(this, file2, new File(str));
                    try {
                        Utilities.NSEncryption(new File(NSHideFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (NSHideFile.length() > 1) {
                        AddVideoToDatabase(FileName(videoImportEntListShow.get(i).GetPath()), videoImportEntListShow.get(i).GetPath(), str2, NSHideFile);
                        if (Build.VERSION.SDK_INT < 23 && StorageOptionSharedPreferences.GetObject(this).GetSDCardUri().length() > 0) {
                            Utilities.DeleteSDcardImageLollipop(this, file2.getAbsolutePath());
                        }
                        try {
                            getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        IsExceptionInImportVideos = true;
                    }
                } catch (Exception e3) {
                    IsExceptionInImportVideos = true;
                    e3.printStackTrace();
                }
            }
        }
    }

    public void AddVideoToDatabase(String str, String str2, String str3, String str4) {
        Log.d("Path", str4);
        Video video = new Video();
        video.setVideoName(str);
        video.setFolderLockVideoLocation(str4);
        video.setOriginalVideoLocation(str2);
        video.setthumbnail_video_location(str3);
        video.setAlbumId(folderId);
        VideoDAL videoDAL = new VideoDAL(ImportAlbumsGalleryVideoActivity.this);
        try {
            videoDAL.OpenWrite();
            videoDAL.AddVideos(video);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoDAL.close();
            throw th;
        }
        videoDAL.close();
    }

    public void AddAlbumToDatabase(String str, String str2) {
        VideoAlbum videoAlbum = new VideoAlbum();
        videoAlbum.setAlbumName(str);
        videoAlbum.setAlbumLocation(str2);
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(ImportAlbumsGalleryVideoActivity.this);
        try {
            videoAlbumDAL.OpenWrite();
            videoAlbumDAL.AddVideoAlbum(videoAlbum);
            Common.FolderId = videoAlbumDAL.GetLastAlbumId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoAlbumDAL.close();
            throw th;
        }
        videoAlbumDAL.close();
    }

    private boolean IsFileCheck() {
        for (int i = 0; i < importAlbumEnts.size(); i++) {
            if (importAlbumEnts.get(i).GetAlbumFileCheck()) {
                videoImportEntListShow = new ArrayList<>();
                for (ImportEnt next : videoImportEntList) {
                    if (spinnerValues.get(i).equals(new File(next.GetPath()).getParent())) {
                        videoImportEntListShow.add(next);
                    }
                    for (int i2 = 0; i2 < videoImportEntListShow.size(); i2++) {
                        videoImportEntListShow.get(i2).SetThumbnailSelection(true);
                    }
                }
                videoImportEntListShowList.add(videoImportEntListShow);
            }
        }
        selectPath.clear();
        for (int i3 = 0; i3 < videoImportEntListShow.size(); i3++) {
            if (videoImportEntListShow.get(i3).GetThumbnailSelection()) {
                selectPath.add(videoImportEntListShow.get(i3).GetPath());
                return true;
            }
        }
        return false;
    }

    public void GetAlbumsFromGallery() {
        adapter = new AlbumsImportGalleryVideoAdapter(ImportAlbumsGalleryVideoActivity.this, importAlbumEnts, false, true, this);
        album_import_ListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (importAlbumEnts.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            photo_video_empty_icon.setBackgroundResource(R.drawable.ic_video_empty_icon);
            lbl_photo_video_empty.setText(R.string.no_videos);
        }
    }

    public void SelectedCount() {
        selectCount = 0;
        for (int i = 0; i < videoImportEntListShow.size(); i++) {
            if (videoImportEntListShow.get(i).GetThumbnailSelection()) {
                selectCount++;
            }
        }
    }

    public void btnSelectAllonClick(View view) {
        SelectAllPhotos();
        galleryImagesAdapter = new GalleryVideoAdapter(ImportAlbumsGalleryVideoActivity.this, 1, videoImportEntListShow);
        imagegrid.setAdapter(galleryImagesAdapter);
        galleryImagesAdapter.notifyDataSetChanged();
    }

    public void btnBackonClick(View view) {
        Back();
    }

    private void SelectAllPhotos() {
        if (IsSelectAll) {
            for (int i = 0; i < videoImportEntListShow.size(); i++) {
                videoImportEntListShow.get(i).SetThumbnailSelection(false);
            }
            IsSelectAll = false;
            btnSelectAll.setImageResource(R.drawable.ic_unselectallicon);
            return;
        }
        for (int i2 = 0; i2 < videoImportEntListShow.size(); i2++) {
            videoImportEntListShow.get(i2).SetThumbnailSelection(true);
        }
        IsSelectAll = true;
        btnSelectAll.setImageResource(R.drawable.ic_selectallicon);
    }

    public void loadGallery() throws IOException {
        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, (str, uri) -> {
        });
        Cursor managedQuery = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, null, null, "_id");
        int video_column_index = managedQuery.getColumnIndex("_id");
        int count = managedQuery.getCount();
        for (int i = 0; i < count; i++) {
            managedQuery.moveToPosition(i);
            int i2 = managedQuery.getInt(video_column_index);
            int columnIndex = managedQuery.getColumnIndex("_data");
            if (new File(managedQuery.getString(columnIndex)).exists()) {
                String string = managedQuery.getString(columnIndex);
                if (string.endsWith(".3gp") || string.endsWith(".mp4") || string.endsWith(".ts") || string.endsWith(".webm") || string.endsWith(".mkv") || string.endsWith(".wmv") || string.endsWith(".mov") || string.endsWith(".avi") || string.endsWith(".flv")) {
                    ImportEnt importEnt = new ImportEnt();
                    importEnt.SetId(i2);
                    importEnt.SetPath(managedQuery.getString(columnIndex));
                    importEnt.SetThumbnail(null);
                    importEnt.SetThumbnailSelection(false);
                    videoImportEntList.add(importEnt);
                    ImportAlbumEnt importAlbumEnt = new ImportAlbumEnt();
                    File file = new File(importEnt.GetPath());
                    if (spinnerValues.size() <= 0 || !spinnerValues.contains(file.getParent())) {
                        importAlbumEnt.SetId(i2);
                        importAlbumEnt.SetAlbumName(file.getParent());
                        importAlbumEnt.SetPath(managedQuery.getString(columnIndex));
                        importAlbumEnts.add(importAlbumEnt);
                        spinnerValues.add(file.getParent());
                    }
                }
            }
        }
        if (videoImportEntList.size() <= 0) {
            btnSelectAll.setEnabled(false);
            btnImport.setEnabled(false);
        }
    }

    public String FileName(String str) {
        for (int length = str.length() - 1; length > 0; length--) {
            if (str.charAt(length) == " /".charAt(1)) {
                return str.substring(length + 1);
            }
        }
        return "";
    }

    @SuppressLint("SetTextI18n")
    public void Back() {
        Intent intent;
        if (isAlbumClick) {
            isAlbumClick = false;
            lbl_import_photo_album_topbaar.setText("Import Albums");
            album_import_ListView.setVisibility(View.VISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            for (int i = 0; i < videoImportEntListShow.size(); i++) {
                videoImportEntListShow.get(i).SetThumbnailSelection(false);
            }
            IsSelectAll = false;
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        if (Common.IsCameFromPhotoAlbum) {
            Common.IsCameFromPhotoAlbum = false;
            intent = new Intent(this, VideosAlbumActivty.class);
        } else if (Common.IsCameFromGalleryFeature) {
            Common.IsCameFromGalleryFeature = false;
            intent = new Intent(this, GalleryActivity.class);
        } else {
            intent = new Intent(this, Videos_Gallery_Actitvity.class);
        }
        startActivity(intent);
        finish();
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
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive && !Common.IsWorkInProgress) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Intent intent;
        if (i == 4) {
            if (isAlbumClick) {
                isAlbumClick = false;
                lbl_import_photo_album_topbaar.setText("Import Albums");
                album_import_ListView.setVisibility(View.VISIBLE);
                imagegrid.setVisibility(View.INVISIBLE);
                btnSelectAll.setVisibility(View.INVISIBLE);
                for (int i2 = 0; i2 < videoImportEntListShow.size(); i2++) {
                    videoImportEntListShow.get(i2).SetThumbnailSelection(false);
                }
                IsSelectAll = false;
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            if (Common.IsCameFromPhotoAlbum) {
                Common.IsCameFromPhotoAlbum = false;
                intent = new Intent(this, VideosAlbumActivty.class);
            } else if (Common.IsCameFromGalleryFeature) {
                Common.IsCameFromGalleryFeature = false;
                intent = new Intent(this, GalleryActivity.class);
            } else {
                intent = new Intent(this, Videos_Gallery_Actitvity.class);
            }
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
