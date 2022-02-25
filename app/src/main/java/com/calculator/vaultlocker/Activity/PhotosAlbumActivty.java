package com.calculator.vaultlocker.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.calculator.vaultlocker.BuildConfig;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.XMLParser.AlbumsGalleryPhotosMethods;
import com.calculator.vaultlocker.Model.Photo;
import com.calculator.vaultlocker.Model.PhotoAlbum;
import com.calculator.vaultlocker.DB.PhotoAlbumDAL;
import com.calculator.vaultlocker.DB.PhotoDAL;
import com.calculator.vaultlocker.Adapter.PhotosAlbumsAdapter;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksSharedPreferences;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.calculator.vaultlocker.utilities.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PhotosAlbumActivty extends BaseActivity {
    private static final int RESULT_LOAD_CAMERA = 1;
    public static int albumPosition = 0;
    public static boolean isEditPhotoAlbum = false;

    private ProgressBar Progress;
    private PhotosAlbumsAdapter adapter;
    private GridView gridView;
    private ArrayList<PhotoAlbum> photoAlbums;
    private File cacheDir;
    private LinearLayout ll_EditAlbum;

    private String albumName = "";
    private SensorManager sensorManager;

    private int position;
    private int AlbumId = 0;
    private boolean IsMoreDropdown = false;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photos_videos_albums);

        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton btn_Add_Album = findViewById(R.id.btn_Add_Album);
        RelativeLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_rename_btn = findViewById(R.id.ll_rename_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        LinearLayout ll_import_from_gallery_btn = findViewById(R.id.ll_import_from_gallery_btn);
        LinearLayout ll_import_from_camera_btn = findViewById(R.id.ll_import_from_camera_btn);

        Progress = findViewById(R.id.prbLoading);
        ll_EditAlbum = findViewById(R.id.ll_EditAlbum);
        gridView = findViewById(R.id.AlbumsGalleryGrid);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lblFeature1);

        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        toolbar.setNavigationOnClickListener(view -> Back());

        Common.IsPhoneGalleryLoad = true;
        SecurityLocksCommon.IsAppDeactive = true;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsMoreDropdown) {
                IsMoreDropdown = false;
            }
            return false;
        });

        Progress.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            GetAlbumsFromDatabase();
            Message message = new Message();
            message.what = 1;
            Handler handler = new Handler(message1 -> {
                if (message1.what == 1) {
                    Progress.setVisibility(View.GONE);
                }
                return false;
            });
            handler.sendMessage(message);
        }, 300);

        btn_Add_Album.setOnClickListener(view -> {
            if (!PhotosAlbumActivty.isEditPhotoAlbum) {
                AddAlbumPopup();
            }
        });

        gridView.setOnItemClickListener((adapterView, view, i, j) -> {
            PhotosAlbumActivty.albumPosition = gridView.getFirstVisiblePosition();
            if (PhotosAlbumActivty.isEditPhotoAlbum) {
                PhotosAlbumActivty.isEditPhotoAlbum = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, i, PhotosAlbumActivty.isEditPhotoAlbum);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = photoAlbums.get(i).getId();
            startActivity(new Intent(PhotosAlbumActivty.this, Photos_Gallery_Actitvity.class));
            finish();
        });

        gridView.setOnItemLongClickListener((adapterView, view, i, j) -> {
            PhotosAlbumActivty.albumPosition = gridView.getFirstVisiblePosition();
            if (PhotosAlbumActivty.isEditPhotoAlbum) {
                PhotosAlbumActivty.isEditPhotoAlbum = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, i, PhotosAlbumActivty.isEditPhotoAlbum);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                PhotosAlbumActivty.isEditPhotoAlbum = true;
                ll_EditAlbum.setVisibility(View.VISIBLE);
                position = i;
                AlbumId = Common.FolderId;
                albumName = photoAlbums.get(position).getAlbumName();
                adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, i, PhotosAlbumActivty.isEditPhotoAlbum);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (PhotosAlbumActivty.albumPosition != 0) {
                gridView.setSelection(PhotosAlbumActivty.albumPosition);
            }
            return true;
        });

        ll_rename_btn.setOnClickListener(view -> {
            if (photoAlbums.get(position).getId() != 1) {
                EditAlbumPopup(photoAlbums.get(position).getId(), photoAlbums.get(position).getAlbumName(), photoAlbums.get(position).getAlbumLocation());
                return;
            }
            Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_default_album_notrenamed, Toast.LENGTH_SHORT).show();
            PhotosAlbumActivty.isEditPhotoAlbum = false;
            adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, 0, PhotosAlbumActivty.isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ll_EditAlbum.setVisibility(View.GONE);
        });

        ll_delete_btn.setOnClickListener(view -> {
            if (photoAlbums.get(position).getId() != 1) {
                DeleteALertDialog(photoAlbums.get(position).getId(), photoAlbums.get(position).getAlbumName(), photoAlbums.get(position).getAlbumLocation());
                return;
            }
            Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_default_album_notdeleted, Toast.LENGTH_SHORT).show();
            PhotosAlbumActivty.isEditPhotoAlbum = false;
            adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, 0, PhotosAlbumActivty.isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ll_EditAlbum.setVisibility(View.GONE);
        });

        ll_import_from_gallery_btn.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.IsCameFromPhotoAlbum = true;
            Common.FolderId = photoAlbums.get(position).getId();
            AlbumId = Common.FolderId;
            albumName = photoAlbums.get(position).getAlbumName();
            Intent intent = new Intent(PhotosAlbumActivty.this, ImportAlbumsGalleryPhotoActivity.class);
            intent.putExtra("ALBUM_ID", AlbumId);
            intent.putExtra("FOLDER_NAME", AlbumId);
            startActivity(intent);
            finish();
        });

        ll_import_from_camera_btn.setOnClickListener(view -> {
            Common.FolderId = photoAlbums.get(position).getId();
            AlbumId = Common.FolderId;
            albumName = photoAlbums.get(position).getAlbumName();
            openCameraIntent();
        });

        int i = albumPosition;
        if (i != 0) {
            gridView.setSelection(i);
            albumPosition = 0;
        }
    }

    public void GetAlbumsFromDatabase() {
        isEditPhotoAlbum = false;
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this);
        try {
            photoAlbumDAL.OpenRead();
            photoAlbums = (ArrayList<PhotoAlbum>) photoAlbumDAL.GetAlbums();
            for (PhotoAlbum next : photoAlbums) {
                next.setAlbumCoverLocation(GetCoverPhotoLocation(next.getId()));
            }
            adapter = new PhotosAlbumsAdapter(this, 17367043, photoAlbums, 0, isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoAlbumDAL.close();
            throw th;
        }
    }

    public void AddAlbumPopup() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.album_add_edit_popup);
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        ((TextView) dialog.findViewById(R.id.lbl_Create_Edit_Album)).setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lbl_Ok)).setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lbl_Cancel)).setTypeface(createFromAsset);
        final EditText editText = dialog.findViewById(R.id.txt_AlbumName);

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (editText.getEditableText().toString().length() <= 0 || editText.getText().toString().trim().isEmpty()) {
                Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                return;
            }
            albumName = editText.getEditableText().toString();
            File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.PHOTOS + albumName);
            if (file.exists()) {
                Toast.makeText(PhotosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
            } else if (file.mkdirs()) {
                AlbumsGalleryPhotosMethods albumsGalleryPhotosMethods = new AlbumsGalleryPhotosMethods();
                albumsGalleryPhotosMethods.AddAlbumToDatabase(PhotosAlbumActivty.this, albumName);
                Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success, Toast.LENGTH_SHORT).show();
                GetAlbumsFromDatabase();
                dialog.dismiss();
            } else {
                Toast.makeText(PhotosAlbumActivty.this, "ERROR! Some Error in creating album", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void EditAlbumPopup(final int i, final String str, final String str2) {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.album_add_edit_popup);
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        TextView textView = dialog.findViewById(R.id.lbl_Create_Edit_Album);
        textView.setTypeface(createFromAsset);
        textView.setText(R.string.lbl_Photos_Album_Rename_Album);
        ((TextView) dialog.findViewById(R.id.lbl_Ok)).setTypeface(createFromAsset);
        ((TextView) dialog.findViewById(R.id.lbl_Cancel)).setTypeface(createFromAsset);
        final EditText editText = dialog.findViewById(R.id.txt_AlbumName);
        if (str.length() > 0) {
            editText.setText(str);
        }

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (editText.getEditableText().toString().length() <= 0 || editText.getText().toString().trim().isEmpty()) {
                Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                return;
            }
            albumName = editText.getEditableText().toString();
            if (new File(str2).exists()) {
                File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + albumName);
                if (file.exists()) {
                    Toast.makeText(PhotosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + str);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file2.renameTo(file)) {
                    AlbumsGalleryPhotosMethods albumsGalleryPhotosMethods = new AlbumsGalleryPhotosMethods();
                    albumsGalleryPhotosMethods.UpdateAlbumInDatabase(PhotosAlbumActivty.this, i, albumName);
                    Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success_renamed, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase();
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            PhotosAlbumActivty.isEditPhotoAlbum = false;
            adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, position, PhotosAlbumActivty.isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void DeleteALertDialog(final int i, final String str, final String str2) {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        if (str.length() > 9) {
            textView.setText("Are you sure you want to delete " + str.substring(0, 8) + ".. including its data?");
        } else {
            textView.setText("Are you sure you want to delete " + str + " including its data?");
        }

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            DeleteAlbum(i, str, str2);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            PhotosAlbumActivty.isEditPhotoAlbum = false;
            adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, position, PhotosAlbumActivty.isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void DeleteAlbum(int i, String str, String str2) {
        File file = new File(str2);
        DeleteAlbumFromDatabase(i);
        try {
            Utilities.DeleteAlbum(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DeleteAlbumFromDatabase(int i) {
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this);
        try {
            photoAlbumDAL.OpenWrite();
            photoAlbumDAL.DeleteAlbumById(i);
            Toast.makeText(this, R.string.lbl_Photos_Album_delete_success, Toast.LENGTH_SHORT).show();
            GetAlbumsFromDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoAlbumDAL.close();
            throw th;
        }
        photoAlbumDAL.close();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        SecurityLocksCommon.IsAppDeactive = true;
        String str;
        if (i == RESULT_LOAD_CAMERA && i2 == -1 && cacheDir != null) {

            try {
                str = Utilities.NSHideFile(this, cacheDir, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + albumName));
                Utilities.NSEncryption(new File(str));
                if (!str.equals("")) {
                    new AlbumsGalleryPhotosMethods().AddPhotoToDatabase(this, Common.FolderId, cacheDir.getName(), str, cacheDir.getAbsolutePath());
                    Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetCoverPhotoLocation(int i) {
        String str;
        new Photo();
        PhotoDAL photoDAL = new PhotoDAL(this);
        try {
            photoDAL.OpenRead();
            str = photoDAL.GetCoverPhoto(i).getFolderLockPhotoLocation();
            photoDAL.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            photoDAL.close();
            str = null;
        } catch (Throwable th) {
            photoDAL.close();
            throw th;
        }
        return str;
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
        ll_EditAlbum.setVisibility(View.GONE);
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            SecurityLocksSharedPreferences.GetObject(this).SetIsCameraOpenFromInApp(false);
            Common.isOpenCameraorGalleryFromApp = false;
            finish();
            System.exit(0);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        ll_EditAlbum.setVisibility(View.GONE);
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (isEditPhotoAlbum) {
                SecurityLocksCommon.IsAppDeactive = false;
                isEditPhotoAlbum = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new PhotosAlbumsAdapter(this, 17367043, photoAlbums, 0, isEditPhotoAlbum);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void openCameraIntent() {
        SecurityLocksSharedPreferences.GetObject(this).SetIsCameraOpenFromInApp(true);
        Common.isOpenCameraorGalleryFromApp = true;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(getApplication().getPackageManager()) != null) {
            try {
                cacheDir = createImageFile();
                intent.putExtra("output", FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID, cacheDir));
                SecurityLocksCommon.IsAppDeactive = false;
                startActivityForResult(intent, RESULT_LOAD_CAMERA);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File createTempFile = File.createTempFile("IMG_" + format + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        createTempFile.getAbsolutePath();
        return createTempFile;
    }

    public void Back() {
        if (isEditPhotoAlbum) {
            SecurityLocksCommon.IsAppDeactive = false;
            isEditPhotoAlbum = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new PhotosAlbumsAdapter(this, 17367043, photoAlbums, 0, isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    public enum SortBy {
        Name,
        Time
    }
}