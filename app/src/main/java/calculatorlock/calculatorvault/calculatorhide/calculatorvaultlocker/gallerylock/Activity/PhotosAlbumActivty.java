package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.GoogleAds;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.BuildConfig;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.extra.AlbumFile;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.extra.AlbumFolder;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.AlbumsGalleryPhotosMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.PhotoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.PhotosAlbumsAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
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
    Map<String, AlbumFolder> albumFolderMap = new HashMap();
    private PhotoAlbumDAL photoAlbumDAL;
    int _SortBy = 0;



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

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(PhotosAlbumActivty.this, ll_banner);

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
            Common.FolderId = PhotosAlbumActivty.this.photoAlbums.get(PhotosAlbumActivty.this.position).getId();
            PhotosAlbumActivty.this.AlbumId = Common.FolderId;
            PhotosAlbumActivty photosAlbumActivty = PhotosAlbumActivty.this;
            photosAlbumActivty.albumName = photosAlbumActivty.photoAlbums.get(PhotosAlbumActivty.this.position).getAlbumName();
            PhotosAlbumActivty.this.openCameraIntent();
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

    public void GetAlbumsFromDatabase(int i) {
        isEditPhotoAlbum = false;
        PhotoAlbumDAL photoAlbumDAL2 = new PhotoAlbumDAL(this);
        this.photoAlbumDAL = photoAlbumDAL2;
        try {
            photoAlbumDAL2.OpenRead();
            ArrayList<PhotoAlbum> arrayList = (ArrayList) this.photoAlbumDAL.GetAlbums(i);
            this.photoAlbums = arrayList;
            Iterator<PhotoAlbum> it = arrayList.iterator();
            while (it.hasNext()) {
                PhotoAlbum next = it.next();
                next.setAlbumCoverLocation(GetCoverPhotoLocation(next.getId()));
            }
            PhotosAlbumsAdapter photosAlbumsAdapter = new PhotosAlbumsAdapter(this, 17367043, this.photoAlbums, 0, isEditPhotoAlbum);
            this.adapter = photosAlbumsAdapter;
            this.gridView.setAdapter((ListAdapter) photosAlbumsAdapter);
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            PhotoAlbumDAL photoAlbumDAL3 = this.photoAlbumDAL;
            if (photoAlbumDAL3 != null) {
                photoAlbumDAL3.close();
            }
            throw th;
        }

    }

    public void AddAlbumPopup() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.album_add_edit_popup, null);
        bottomSheetDialog.setContentView(inflate);


        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (GoogleAds.adsdisplay) {
                GoogleAds.showFullAds(PhotosAlbumActivty.this, () -> {
                    GoogleAds.allcount60.start();
                    if (editText.getEditableText().toString().length() <= 0 || editText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    albumName = editText.getEditableText().toString();
                    File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.PHOTOS + albumName);
                    if (file.exists()) {
                        Toast.makeText(PhotosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                    } else {
                        file.mkdirs();
                        AlbumsGalleryPhotosMethods albumsGalleryPhotosMethods = new AlbumsGalleryPhotosMethods();
                        albumsGalleryPhotosMethods.AddAlbumToDatabase(PhotosAlbumActivty.this, albumName);
                        Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success, Toast.LENGTH_SHORT).show();
                        GetAlbumsFromDatabase();
                        bottomSheetDialog.dismiss();
                    }
                });
            } else {
                if (editText.getEditableText().toString().length() <= 0 || editText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                    return;
                }
                albumName = editText.getEditableText().toString();
                File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.PHOTOS + albumName);
                if (file.exists()) {
                    Toast.makeText(PhotosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                } else {
                    file.mkdirs();
                    AlbumsGalleryPhotosMethods albumsGalleryPhotosMethods = new AlbumsGalleryPhotosMethods();
                    albumsGalleryPhotosMethods.AddAlbumToDatabase(PhotosAlbumActivty.this, albumName);
                    Toast.makeText(PhotosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase();
                    bottomSheetDialog.dismiss();
                }
            }

        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    public void EditAlbumPopup(final int i, final String str, final String str2) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.album_add_edit_popup, null);
        bottomSheetDialog.setContentView(inflate);
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        TextView textView = inflate.findViewById(R.id.lbl_Create_Edit_Album);
        textView.setTypeface(createFromAsset);
        textView.setText(R.string.lbl_Photos_Album_Rename_Album);
        ((TextView) inflate.findViewById(R.id.lbl_Ok)).setTypeface(createFromAsset);
        ((TextView) inflate.findViewById(R.id.lbl_Cancel)).setTypeface(createFromAsset);
        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);
        if (str.length() > 0) {
            editText.setText(str);
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
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
                    bottomSheetDialog.dismiss();
                }
            }
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            PhotosAlbumActivty.isEditPhotoAlbum = false;
            adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, position, PhotosAlbumActivty.isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void DeleteALertDialog(final int i, final String str, final String str2) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.confirmation_message_box, null);
        bottomSheetDialog.setContentView(inflate);


        TextView textView = inflate.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        if (str.length() > 9) {
            textView.setText("Are you sure you want to delete " + str.substring(0, 8) + ".. including its data?");
        } else {
            textView.setText("Are you sure you want to delete " + str + " including its data?");
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            DeleteAlbum(i, str, str2);
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            PhotosAlbumActivty.isEditPhotoAlbum = false;
            adapter = new PhotosAlbumsAdapter(PhotosAlbumActivty.this, 17367043, photoAlbums, position, PhotosAlbumActivty.isEditPhotoAlbum);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
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
        if (i == RESULT_LOAD_CAMERA && i2 == -1 && cacheDir != null) {
            String str=new String();
            try {
                str = Utilities.NSHideFile(this, cacheDir, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + albumName));
                Utilities.NSEncryption(new File(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!str.equals("")) {
                new AlbumsGalleryPhotosMethods().AddPhotoToDatabase(this, Common.FolderId, cacheDir.getName(), str, cacheDir.getAbsolutePath());
                Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
                // GetAlbumsFromDatabase();
                GetAlbumsFromDatabase(this._SortBy);
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
                Log.e("photoFile", this.cacheDir + "");
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