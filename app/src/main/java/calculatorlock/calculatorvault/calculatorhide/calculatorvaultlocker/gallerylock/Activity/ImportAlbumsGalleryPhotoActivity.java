package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportAlbumEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.AlbumsImportGalleryPhotoAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.GalleryPhotoAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.PhotoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.OnItemClickListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImportAlbumsGalleryPhotoActivity extends BaseActivity implements OnItemClickListener {
    private final ArrayList<ImportEnt> photoImportEntList = new ArrayList<>();
    private final ArrayList<ImportAlbumEnt> importAlbumEnts = new ArrayList<>();
    private final List<List<ImportEnt>> photoImportEntListShowList = new ArrayList<>();
    private final ArrayList<String> spinnerValues = new ArrayList<>();
    private final ArrayList<String> selectPath = new ArrayList<>();
    public String dbFolderPath;
    public String folderName;
    public int folderId;
    public int image_column_index;
    public int selectCount;
    public int count;
    private boolean IsExceptionInImportPhotos = false;
    private boolean isAlbumClick = false;
    private boolean IsSelectAll = false;
    private ArrayList<ImportEnt> photoImportEntListShow = new ArrayList<>();
    private ProgressBar Progress;
    private GridView imagegrid;
    private RecyclerView album_import_ListView;
    private AppCompatImageView btnSelectAll;
    private TextView lbl_import_photo_album_topbaar;
    private AppCompatButton btnImport;
    private LinearLayout ll_photo_video_empty;
    private ProgressDialog myProgressDialog = null;
    private SensorManager sensorManager;
    private AlbumsImportGalleryPhotoAdapter adapter;
    private GalleryPhotoAdapter galleryImagesAdapter;

    Handler Progresshowhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Progress.setVisibility(View.GONE);
                galleryImagesAdapter = new GalleryPhotoAdapter(ImportAlbumsGalleryPhotoActivity.this, 1, photoImportEntListShow);
                imagegrid.setAdapter(galleryImagesAdapter);
                galleryImagesAdapter.notifyDataSetChanged();
                if (photoImportEntListShow.size() <= 0) {
                    album_import_ListView.setVisibility(View.INVISIBLE);
                    imagegrid.setVisibility(View.INVISIBLE);
                    btnSelectAll.setVisibility(View.INVISIBLE);
                    ll_photo_video_empty.setVisibility(View.VISIBLE);
                }
            }
            super.handleMessage(message);
        }
    };

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Intent intent;
            if (message.what == 1) {
                hideProgress();
                if (photoImportEntListShow.size() > 0) {
                    galleryImagesAdapter = new GalleryPhotoAdapter(ImportAlbumsGalleryPhotoActivity.this, 1, photoImportEntListShow);
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
                    if (IsExceptionInImportPhotos) {
                        IsExceptionInImportPhotos = false;
                    } else {
                        Toast.makeText(ImportAlbumsGalleryPhotoActivity.this, selectCount + " photo(s) imported successfully", Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Common.IsPhoneGalleryLoad = true;
                        if (Common.IsCameFromPhotoAlbum) {
                            if (isAlbumClick) {
                                intent = new Intent(ImportAlbumsGalleryPhotoActivity.this, Photos_Gallery_Actitvity.class);
                            } else {
                                intent = new Intent(ImportAlbumsGalleryPhotoActivity.this, PhotosAlbumActivty.class);
                            }
                        } else if (Common.IsCameFromGalleryFeature) {
                            Common.IsCameFromGalleryFeature = false;
                            intent = new Intent(ImportAlbumsGalleryPhotoActivity.this, GalleryActivity.class);
                        } else if (isAlbumClick) {
                            intent = new Intent(ImportAlbumsGalleryPhotoActivity.this, Photos_Gallery_Actitvity.class);
                        } else {
                            intent = new Intent(ImportAlbumsGalleryPhotoActivity.this, PhotosAlbumActivty.class);
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
        myProgressDialog = ProgressDialog.show(this, null, "Your data is being encrypted... this may take a few moments... ", true);
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
        Advertisement.showBanner(ImportAlbumsGalleryPhotoActivity.this, ll_banner);

        SecurityLocksCommon.IsAppDeactive = true;
        Common.IsWorkInProgress = false;
        Common.IsPhoneGalleryLoad = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Progress = findViewById(R.id.prbLoading);
        album_import_ListView = findViewById(R.id.album_import_ListView);
        imagegrid = findViewById(R.id.customGalleryGrid);
        lbl_import_photo_album_topbaar = findViewById(R.id.lbl_import_photo_album_topbaar);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnImport = findViewById(R.id.btnImport);
        ll_photo_video_empty = findViewById(R.id.ll_photo_video_empty);

        lbl_import_photo_album_topbaar.setText(R.string.lbl_import_photo_album_select_album_topbaar);

        album_import_ListView.setLayoutManager(new GridLayoutManager(ImportAlbumsGalleryPhotoActivity.this, 2));

        folderId = Common.FolderId;
        folderName = null;

        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(ImportAlbumsGalleryPhotoActivity.this);
        photoAlbumDAL.OpenRead();
        PhotoAlbum GetAlbumById = photoAlbumDAL.GetAlbumById(Integer.toString(Common.FolderId));
        photoAlbumDAL.close();

        folderName = GetAlbumById.getAlbumName();
        dbFolderPath = (String) getIntent().getSerializableExtra("FOLDER_PATH");

        new DataLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        btnImport.setOnClickListener(view -> {
            OnImportClick();
        });
    }

    @Override
    public void onItemClick(int position) {
        Progress.setVisibility(View.VISIBLE);
        isAlbumClick = true;
        lbl_import_photo_album_topbaar.setText(R.string.lbl_import_photo_album_select_photo_topbaar);
        adapter = new AlbumsImportGalleryPhotoAdapter(ImportAlbumsGalleryPhotoActivity.this, importAlbumEnts, false, false, this);
        album_import_ListView.setAdapter(adapter);
        ShowAlbumData(position);
        imagegrid.setVisibility(View.VISIBLE);
        btnSelectAll.setVisibility(View.VISIBLE);
        album_import_ListView.setVisibility(View.INVISIBLE);
    }

    public void ShowAlbumData(final int i) {
        new Thread() {
            @Override
            public void run() {
                try {
                    photoImportEntListShow.clear();
                    for (ImportEnt next : photoImportEntList) {
                        if (spinnerValues.get(i).equals(new File(next.GetPath()).getParent())) {
                            next.GetThumbnailSelection();
                            photoImportEntListShow.add(next);
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    Progresshowhandler.sendMessage(message);
                } catch (Exception unused) {
                    Message message2 = new Message();
                    message2.what = 1;
                    Progresshowhandler.sendMessage(message2);
                }
            }
        }.start();
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

    @SuppressLint("SetTextI18n")
    public void OnImportClick() {
        final StorageOptionSharedPreferences GetObject = StorageOptionSharedPreferences.GetObject(this);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_import, Toast.LENGTH_SHORT).show();
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
                        final Dialog dialog2 = new Dialog(ImportAlbumsGalleryPhotoActivity.this, R.style.FullHeightDialog);
                        dialog2.setContentView(R.layout.sdcard_permission_alert_msgbox);
                        dialog2.setCancelable(false);
                        dialog2.setCanceledOnTouchOutside(false);

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

                        ImportPhotos();
                        Message message = new Message();
                        message.what = 3;
                        handle.sendMessage(message);
                        Common.IsWorkInProgress = false;
                }
            }.start();
            return;
        }
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(ImportAlbumsGalleryPhotoActivity.this);
        photoAlbumDAL.OpenRead();
        photoAlbumDAL.close();

    }

    public void ImportPhotos() {
        if (isAlbumClick) {
            ImportOnlyPhotosSDCard();
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
                    File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + file.getName());
                    folderName = file.getName();
                    if (file2.exists()) {
                        int i3 = 1;
                        while (i3 < 100) {
                            folderName = file.getName() + "(" + i3 + ")";
                            String sb = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + folderName;
                            File file3 = new File(sb);
                            if (!file3.exists()) {
                                i3 = 100;
                            }
                            i3++;
                            file2 = file3;
                        }
                    }
                    AddAlbumToDatabase(folderName);
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this);
                    photoAlbumDAL.OpenRead();
                    int GetLastAlbumId = photoAlbumDAL.GetLastAlbumId();
                    folderId = GetLastAlbumId;
                    Common.FolderId = GetLastAlbumId;
                    photoAlbumDAL.close();
                    ImportAlbumsPhotosSDCard(i);
                    i++;
                }
            }
        }
    }

    public void ImportAlbumsPhotosSDCard(int i) {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        List<ImportEnt> list = photoImportEntListShowList.get(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).GetThumbnailSelection()) {
                File file = new File(list.get(i2).GetPath());
                try {
                    String NSHideFile = Utilities.NSHideFile(this, file, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + folderName + "/"));
                    Utilities.NSEncryption(new File(NSHideFile));
                    if (NSHideFile.length() > 0) {
                        AddPhotoToDatabase(FileName(list.get(i2).GetPath()), list.get(i2).GetPath(), NSHideFile);
                    }
                    if (Build.VERSION.SDK_INT < 23 && StorageOptionSharedPreferences.GetObject(this).GetSDCardUri().length() > 0) {
                        Utilities.DeleteSDcardImageLollipop(this, file.getAbsolutePath());
                    }
                    try {
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e2) {
                    IsExceptionInImportPhotos = true;
                    e2.printStackTrace();
                }
            }
        }
    }

    public void ImportOnlyPhotosSDCard() {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        int size = photoImportEntListShow.size();
        for (int i = 0; i < size; i++) {
            if (photoImportEntListShow.get(i).GetThumbnailSelection()) {
                File file = new File(photoImportEntListShow.get(i).GetPath());
                try {
                    String NSHideFile = Utilities.NSHideFile(this, file, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + folderName + "/"));
                    Utilities.NSEncryption(new File(NSHideFile));
                    if (Build.VERSION.SDK_INT < 23 && StorageOptionSharedPreferences.GetObject(this).GetSDCardUri().length() > 0) {
                        Utilities.DeleteSDcardImageLollipop(this, file.getAbsolutePath());
                    }
                    try {
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (NSHideFile.length() > 0) {
                        AddPhotoToDatabase(FileName(photoImportEntListShow.get(i).GetPath()), photoImportEntListShow.get(i).GetPath(), NSHideFile);
                    }
                } catch (IOException e2) {
                    IsExceptionInImportPhotos = true;
                    e2.printStackTrace();
                }
            }
        }
    }

    public void AddPhotoToDatabase(String str, String str2, String str3) {
        Photo photo = new Photo();
        photo.setPhotoName(str);
        photo.setFolderLockPhotoLocation(str3);
        photo.setOriginalPhotoLocation(str2);
        photo.setAlbumId(folderId);
        PhotoDAL photoDAL = new PhotoDAL(ImportAlbumsGalleryPhotoActivity.this);
        try {
            photoDAL.OpenWrite();
            photoDAL.AddPhotos(photo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoDAL.close();
            throw th;
        }
        photoDAL.close();
    }

    public void AddAlbumToDatabase(String str) {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumName(str);
        photoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + str);
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this);
        try {
            photoAlbumDAL.OpenWrite();
            photoAlbumDAL.AddPhotoAlbum(photoAlbum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoAlbumDAL.close();
            throw th;
        }
        photoAlbumDAL.close();
    }

    private boolean IsFileCheck() {
        for (int i = 0; i < importAlbumEnts.size(); i++) {
            if (importAlbumEnts.get(i).GetAlbumFileCheck()) {
                photoImportEntListShow = new ArrayList<>();
                for (ImportEnt next : photoImportEntList) {
                    if (spinnerValues.get(i).equals(new File(next.GetPath()).getParent())) {
                        photoImportEntListShow.add(next);
                    }
                    for (int i2 = 0; i2 < photoImportEntListShow.size(); i2++) {
                        photoImportEntListShow.get(i2).SetThumbnailSelection(true);
                    }
                }
                photoImportEntListShowList.add(photoImportEntListShow);
            }
        }
        selectPath.clear();
        for (int i3 = 0; i3 < photoImportEntListShow.size(); i3++) {
            if (photoImportEntListShow.get(i3).GetThumbnailSelection()) {
                selectPath.add(photoImportEntListShow.get(i3).GetPath());
                return true;
            }
        }
        return false;
    }

    public void GetAlbumsFromGallery() {
        adapter = new AlbumsImportGalleryPhotoAdapter(this, importAlbumEnts, false, false, this);
        album_import_ListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (importAlbumEnts.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
        }
    }

    public void SelectedCount() {
        selectCount = 0;
        for (int i = 0; i < photoImportEntListShow.size(); i++) {
            if (photoImportEntListShow.get(i).GetThumbnailSelection()) {
                selectCount++;
            }
        }
    }

    public void btnSelectAllonClick(View view) {
        SelectAllPhotos();
        galleryImagesAdapter = new GalleryPhotoAdapter(this, 1, photoImportEntListShow);
        imagegrid.setAdapter(galleryImagesAdapter);
        galleryImagesAdapter.notifyDataSetChanged();
    }

    public void btnBackonClick(View view) {
        Back();
    }

    private void SelectAllPhotos() {
        if (IsSelectAll) {
            for (int i = 0; i < photoImportEntListShow.size(); i++) {
                photoImportEntListShow.get(i).SetThumbnailSelection(false);
            }
            IsSelectAll = false;
            btnSelectAll.setImageResource(R.drawable.ic_unselectallicon);
            return;
        }
        for (int i2 = 0; i2 < photoImportEntListShow.size(); i2++) {
            photoImportEntListShow.get(i2).SetThumbnailSelection(true);
        }
        IsSelectAll = true;
        btnSelectAll.setImageResource(R.drawable.ic_selectallicon);
    }

    public void loadGallery() {
        Cursor cursor = null;
        ContentResolver contentResolver = getContentResolver();

        if (contentResolver != null) {
            cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, null, null, "_id");
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                image_column_index = cursor.getColumnIndex("_id");
                count = cursor.getCount();
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    int columnIndex = cursor.getColumnIndex("_data");
                    if (new File(cursor.getString(columnIndex)).exists()) {
                        ImportEnt importEnt = new ImportEnt();
                        importEnt.SetPath(cursor.getString(columnIndex));
                        importEnt.SetThumbnailSelection(false);
                        importEnt.SetThumbnail(null);
                        photoImportEntList.add(importEnt);
                        ImportAlbumEnt importAlbumEnt = new ImportAlbumEnt();
                        File file = new File(importEnt.GetPath());
                        if (spinnerValues.size() <= 0 || !spinnerValues.contains(file.getParent())) {
                            importAlbumEnt.SetAlbumName(file.getParent());
                            importAlbumEnt.SetPath(cursor.getString(columnIndex));
                            importAlbumEnts.add(importAlbumEnt);
                            spinnerValues.add(file.getParent());
                        }
                    }
                }
            }
        }
//        Cursor managedQuery = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, null, null, "_id");
//        image_column_index = managedQuery.getColumnIndex("_id");
//        count = managedQuery.getCount();
//        for (int i = 0; i < count; i++) {
//            managedQuery.moveToPosition(i);
//            int columnIndex = managedQuery.getColumnIndex("_data");
//            if (new File(managedQuery.getString(columnIndex)).exists()) {
//                ImportEnt importEnt = new ImportEnt();
//                importEnt.SetPath(managedQuery.getString(columnIndex));
//                importEnt.SetThumbnailSelection(false);
//                importEnt.SetThumbnail(null);
//                photoImportEntList.add(importEnt);
//                ImportAlbumEnt importAlbumEnt = new ImportAlbumEnt();
//                File file = new File(importEnt.GetPath());
//                if (spinnerValues.size() <= 0 || !spinnerValues.contains(file.getParent())) {
//                    importAlbumEnt.SetAlbumName(file.getParent());
//                    importAlbumEnt.SetPath(managedQuery.getString(columnIndex));
//                    importAlbumEnts.add(importAlbumEnt);
//                    spinnerValues.add(file.getParent());
//                }
//            }
//        }

        if (photoImportEntList.size() <= 0) {
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
            for (int i = 0; i < photoImportEntListShow.size(); i++) {
                photoImportEntListShow.get(i).SetThumbnailSelection(false);
            }
            IsSelectAll = false;
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        if (Common.IsCameFromPhotoAlbum) {
            Common.IsCameFromPhotoAlbum = false;
            intent = new Intent(this, PhotosAlbumActivty.class);
        } else if (Common.IsCameFromGalleryFeature) {
            Common.IsCameFromGalleryFeature = false;
            intent = new Intent(this, GalleryActivity.class);
        } else {
            intent = new Intent(this, Photos_Gallery_Actitvity.class);
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
                for (int i2 = 0; i2 < photoImportEntListShow.size(); i2++) {
                    photoImportEntListShow.get(i2).SetThumbnailSelection(false);
                }
                IsSelectAll = false;
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            if (Common.IsCameFromPhotoAlbum) {
                intent = new Intent(this, PhotosAlbumActivty.class);
            } else if (Common.IsCameFromGalleryFeature) {
                Common.IsCameFromGalleryFeature = false;
                intent = new Intent(this, GalleryActivity.class);
            } else {
                intent = new Intent(this, Photos_Gallery_Actitvity.class);
            }
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @SuppressLint("StaticFieldLeak")
    private class DataLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            Progress.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            loadGallery();
            for (ImportEnt next : photoImportEntList) {
                if (spinnerValues.get(0).contains(Objects.requireNonNull(new File(next.GetPath()).getParent()))) {
                    photoImportEntListShow.add(next);
                }
            }
            return null;
        }

        public void onPostExecute(Void r2) {
            super.onPostExecute(r2);
            Progress.setVisibility(View.GONE);
            GetAlbumsFromGallery();
        }
    }
}
