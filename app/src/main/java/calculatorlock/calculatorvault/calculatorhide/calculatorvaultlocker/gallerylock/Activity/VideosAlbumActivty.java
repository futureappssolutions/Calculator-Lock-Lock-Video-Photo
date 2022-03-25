package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.GoogleAds;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.AppSettingsSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.AlbumsGalleryVideosMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Video;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.VideoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.VideosAlbumsAdapter;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class VideosAlbumActivty extends BaseActivity {
    public static int albumPosition = 0;
    public static boolean isEditMode = false;
    int AlbumId = 0;
    int Position = 0;
    int _SortBy = 0;
    boolean IsMoreDropdown = false;
    private ProgressBar Progress;
    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Progress.setVisibility(View.GONE);
            }
            super.handleMessage(message);
        }
    };
    private ArrayList<VideoAlbum> videoAlbum;
    private String albumName;
    private GridView gridView;
    private LinearLayout ll_EditAlbum;
    private SensorManager sensorManager;
    private VideosAlbumsAdapter adapter;

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
        GoogleAds.showBannerAds(VideosAlbumActivty.this, ll_banner);

        gridView = findViewById(R.id.AlbumsGalleryGrid);
        Progress = findViewById(R.id.prbLoading);
        ll_EditAlbum = findViewById(R.id.ll_EditAlbum);

        FloatingActionButton btn_Add_Album = findViewById(R.id.btn_Add_Album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        RelativeLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_rename_btn = findViewById(R.id.ll_rename_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        LinearLayout ll_import_from_gallery_btn = findViewById(R.id.ll_import_from_gallery_btn);
        LinearLayout ll_import_from_camera_btn = findViewById(R.id.ll_import_from_camera_btn);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lblFeature2);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        toolbar.setNavigationOnClickListener(view -> btnBackonClick());

        Common.IsPhoneGalleryLoad = true;
        SecurityLocksCommon.IsAppDeactive = true;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        AppSettingsSharedPreferences appSettingsSharedPreferences = AppSettingsSharedPreferences.GetObject(this);
        _SortBy = appSettingsSharedPreferences.GetPhotosAlbumsSortBy();

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsMoreDropdown) {
                IsMoreDropdown = false;
            }
            return false;
        });

        Progress.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            GetAlbumsFromDatabase(_SortBy);
            Message message = new Message();
            message.what = 1;
            handle.sendMessage(message);
        }, 300);

        btn_Add_Album.setOnClickListener(view -> {
            if (!VideosAlbumActivty.isEditMode) {
                AddAlbumPopup();
            }
        });

        gridView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (GoogleAds.adsdisplay) {
                GoogleAds.showFullAds(VideosAlbumActivty.this, () -> {
                    GoogleAds.allcount60.start();
                    VideosAlbumActivty.albumPosition = gridView.getFirstVisiblePosition();
                    if (VideosAlbumActivty.isEditMode) {
                        VideosAlbumActivty.isEditMode = false;
                        ll_EditAlbum.setVisibility(View.GONE);
                        adapter = new VideosAlbumsAdapter(VideosAlbumActivty.this, 17367043, videoAlbum, i, VideosAlbumActivty.isEditMode);
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    SecurityLocksCommon.IsAppDeactive = false;
                    Common.FolderId = videoAlbum.get(i).getId();
                    startActivity(new Intent(VideosAlbumActivty.this, Videos_Gallery_Actitvity.class));
                    finish();
                });
            } else {
                VideosAlbumActivty.albumPosition = gridView.getFirstVisiblePosition();
                if (VideosAlbumActivty.isEditMode) {
                    VideosAlbumActivty.isEditMode = false;
                    ll_EditAlbum.setVisibility(View.GONE);
                    adapter = new VideosAlbumsAdapter(VideosAlbumActivty.this, 17367043, videoAlbum, i, VideosAlbumActivty.isEditMode);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    return;
                }
                SecurityLocksCommon.IsAppDeactive = false;
                Common.FolderId = videoAlbum.get(i).getId();
                startActivity(new Intent(VideosAlbumActivty.this, Videos_Gallery_Actitvity.class));
                finish();
            }

        });

        gridView.setOnItemLongClickListener((adapterView, view, i, j) -> {
            VideosAlbumActivty.albumPosition = gridView.getFirstVisiblePosition();
            if (VideosAlbumActivty.isEditMode) {
                VideosAlbumActivty.isEditMode = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new VideosAlbumsAdapter(VideosAlbumActivty.this, 17367043, videoAlbum, i, VideosAlbumActivty.isEditMode);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                VideosAlbumActivty.isEditMode = true;
                ll_EditAlbum.setVisibility(View.VISIBLE);
                VideosAlbumActivty videosAlbumActivty2 = VideosAlbumActivty.this;
                videosAlbumActivty2.Position = i;
                videosAlbumActivty2.AlbumId = Common.FolderId;
                albumName = videoAlbum.get(i).getAlbumName();
                adapter = new VideosAlbumsAdapter(VideosAlbumActivty.this, 17367043, videoAlbum, i, VideosAlbumActivty.isEditMode);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (VideosAlbumActivty.albumPosition != 0) {
                gridView.setSelection(VideosAlbumActivty.albumPosition);
            }
            return true;
        });

        ll_rename_btn.setOnClickListener(view -> {
            if (videoAlbum.get(Position).getId() != 1) {
                EditAlbumPopup(videoAlbum.get(Position).getId(), videoAlbum.get(Position).getAlbumName(), videoAlbum.get(Position).getAlbumLocation());
                return;
            }
            Toast.makeText(VideosAlbumActivty.this, R.string.lbl_default_album_notrenamed, Toast.LENGTH_SHORT).show();
            ll_EditAlbum.setVisibility(View.GONE);
            VideosAlbumActivty.isEditMode = false;
            adapter = new VideosAlbumsAdapter(VideosAlbumActivty.this, 17367043, videoAlbum, 0, VideosAlbumActivty.isEditMode);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        ll_delete_btn.setOnClickListener(view -> {
            if (videoAlbum.get(Position).getId() != 1) {
                DeleteALertDialog(videoAlbum.get(Position).getId(), videoAlbum.get(Position).getAlbumName(), videoAlbum.get(Position).getAlbumLocation());
                return;
            }
            Toast.makeText(VideosAlbumActivty.this, R.string.lbl_default_album_notdeleted, Toast.LENGTH_SHORT).show();
            ll_EditAlbum.setVisibility(View.GONE);
            VideosAlbumActivty.isEditMode = false;
            adapter = new VideosAlbumsAdapter(VideosAlbumActivty.this, 17367043, videoAlbum, 0, VideosAlbumActivty.isEditMode);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        ll_import_from_gallery_btn.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.IsCameFromPhotoAlbum = true;
            Common.FolderId = videoAlbum.get(Position).getId();
            AlbumId = Common.FolderId;
            albumName = videoAlbum.get(Position).getAlbumName();
            startActivity(new Intent(VideosAlbumActivty.this, ImportAlbumsGalleryVideoActivity.class));
            finish();
        });

        ll_import_from_camera_btn.setOnClickListener(view -> {
            AlbumId = videoAlbum.get(Position).getId();
            albumName = videoAlbum.get(Position).getAlbumName();
            dispatchTakeVideoIntent();
        });

        int i = albumPosition;
        if (i != 0) {
            gridView.setSelection(i);
            albumPosition = 0;
        }
    }

    public void btnBackonClick() {
        if (isEditMode) {
            SecurityLocksCommon.IsAppDeactive = false;
            isEditMode = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new VideosAlbumsAdapter(this, 17367043, videoAlbum, 0, isEditMode);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    public void GetAlbumsFromDatabase(int i) {
        isEditMode = false;
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(this);
        try {
            videoAlbumDAL.OpenRead();
            videoAlbum = (ArrayList<VideoAlbum>) videoAlbumDAL.GetAlbums(i);
            for (VideoAlbum next : videoAlbum) {
                next.setAlbumCoverLocation(GetCoverPhotoLocation(next.getId()));
            }
            adapter = new VideosAlbumsAdapter(this, 17367043, videoAlbum, 0, isEditMode);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoAlbumDAL.close();
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
                GoogleAds.showFullAds(VideosAlbumActivty.this, () -> {
                    GoogleAds.allcount60.start();
                    if (editText.getEditableText().toString().length() <= 0 || editText.getEditableText().toString().trim().isEmpty()) {
                        Toast.makeText(VideosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    albumName = editText.getEditableText().toString();
                    File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.VIDEOS + albumName);
                    if (file.exists()) {
                        Toast.makeText(VideosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                    } else  {
                        file.mkdirs();
                        AlbumsGalleryVideosMethods albumsGalleryVideosMethods = new AlbumsGalleryVideosMethods();
                        albumsGalleryVideosMethods.AddAlbumToDatabase(VideosAlbumActivty.this, albumName);
                        Toast.makeText(VideosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success, Toast.LENGTH_SHORT).show();
                        GetAlbumsFromDatabase(_SortBy);
                        bottomSheetDialog.dismiss();
                    }
                });
            } else {
                if (editText.getEditableText().toString().length() <= 0 || editText.getEditableText().toString().trim().isEmpty()) {
                    Toast.makeText(VideosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                    return;
                }
                albumName = editText.getEditableText().toString();
                File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.VIDEOS + albumName);
                if (file.exists()) {
                    Toast.makeText(VideosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                } else  {
                    file.mkdirs();
                    AlbumsGalleryVideosMethods albumsGalleryVideosMethods = new AlbumsGalleryVideosMethods();
                    albumsGalleryVideosMethods.AddAlbumToDatabase(VideosAlbumActivty.this, albumName);
                    Toast.makeText(VideosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase(_SortBy);
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

        TextView textView = inflate.findViewById(R.id.lbl_Create_Edit_Album);
        textView.setText(R.string.lbl_Photos_Album_Rename_Album);

        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);
        if (str.length() > 0) {
            editText.setText(str);
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (editText.getEditableText().toString().length() <= 0 || editText.getEditableText().toString().trim().isEmpty()) {
                Toast.makeText(VideosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                return;
            }
            albumName = editText.getEditableText().toString();
            if (new File(str2).exists()) {
                File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + albumName);
                if (file.exists()) {
                    Toast.makeText(VideosAlbumActivty.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + str);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file2.renameTo(file)) {
                    AlbumsGalleryVideosMethods albumsGalleryVideosMethods = new AlbumsGalleryVideosMethods();
                    albumsGalleryVideosMethods.UpdateAlbumInDatabase(VideosAlbumActivty.this, i, albumName);
                    Toast.makeText(VideosAlbumActivty.this, R.string.lbl_Photos_Album_Create_Album_Success_renamed, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase(_SortBy);
                    bottomSheetDialog.dismiss();
                    ll_EditAlbum.setVisibility(View.GONE);
                }
            }
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            ll_EditAlbum.setVisibility(View.GONE);
            VideosAlbumActivty.isEditMode = false;
            GetAlbumsFromDatabase(_SortBy);
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

        if (str.length() > 9) {
            textView.setText("Are you sure you want to delete " + str.substring(0, 8) + ".. including its data?");
        } else {
            textView.setText("Are you sure you want to delete " + str + " including its data?");
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            DeleteAlbum(i, str, str2);
            bottomSheetDialog.dismiss();
            ll_EditAlbum.setVisibility(View.GONE);
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            ll_EditAlbum.setVisibility(View.GONE);
            VideosAlbumActivty.isEditMode = false;
            VideosAlbumActivty videosAlbumActivty = VideosAlbumActivty.this;
            videosAlbumActivty.GetAlbumsFromDatabase(videosAlbumActivty._SortBy);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    public void DeleteAlbum(int i, String str, String str2) {
        File file = new File(str2);
        DeleteAlbumFromDatabase(i);
        File file2 = new File(str2 + File.separator + "VideoThumnails");
        if (file2.exists()) {
            file2.delete();
        }
        try {
            Utilities.DeleteAlbum(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DeleteAlbumFromDatabase(int i) {
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(this);
        try {
            videoAlbumDAL.OpenWrite();
            videoAlbumDAL.DeleteAlbumById(i);
            Toast.makeText(this, R.string.lbl_Photos_Album_delete_success, Toast.LENGTH_SHORT).show();
            GetAlbumsFromDatabase(_SortBy);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoAlbumDAL.close();
            throw th;
        }
        videoAlbumDAL.close();
    }

    @SuppressLint("SimpleDateFormat")
    public void dispatchTakeVideoIntent() {
        SecurityLocksSharedPreferences.GetObject(this).SetIsCameraOpenFromInApp(true);
        Common.isOpenCameraorGalleryFromApp = true;
        SecurityLocksCommon.IsAppDeactive = false;
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", "VideoTitle");
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("_data", Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ("Video_" + new SimpleDateFormat("yyyymmddhhmmss").format(new Date()) + ".mp4"));
        intent.putExtra("output", getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues));
        startActivityForResult(intent, 2);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        FileOutputStream fileOutputStream;
        super.onActivityResult(i, i2, intent);
        SecurityLocksCommon.IsAppDeactive = true;
        if (i != 1) {
            if (i != 2) {
                return;
            }
        } else if (i2 == -1) {
            return;
        }
        if (i2 == -1) {
            Uri lastPhotoOrVideo = Utilities.getLastPhotoOrVideo(this);
            String encodedPath = lastPhotoOrVideo.getEncodedPath();
            new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
            String ChangeFileExtention = Utilities.ChangeFileExtention(Utilities.FileName(encodedPath));
            File file = new File(encodedPath);
            Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), 1);
            File file2;
            file2 = new File(StorageOptionsCommon.STORAGEPATH_1 + StorageOptionsCommon.VIDEOS + albumName + "/VideoThumnails/");
            file2.mkdirs();
            Utilities.FileName(encodedPath);
            String str;
            str = StorageOptionsCommon.STORAGEPATH_1 + StorageOptionsCommon.VIDEOS + albumName + "/VideoThumnails/thumbnil-" + ChangeFileExtention.substring(0, ChangeFileExtention.lastIndexOf("#")) + "#jpg";
            File file3 = new File(str);
            String str2 = null;
            try {
                fileOutputStream = new FileOutputStream(file3, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fileOutputStream = null;
            }
            createVideoThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            try {
                assert fileOutputStream != null;
                fileOutputStream.flush();
                fileOutputStream.close();
                Utilities.NSEncryption(file3);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            File file4 = new File(StorageOptionsCommon.STORAGEPATH_1 + StorageOptionsCommon.VIDEOS + albumName + "/" + ChangeFileExtention);
            try {
                str2 = Utilities.NSHideFile(this, file, new File(Objects.requireNonNull(file4.getParent())));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            AddVideoToDatabase(Utilities.ChangeFileExtentionToOrignal(ChangeFileExtention), encodedPath, str, str2);
            file.delete();
            try {
                Utilities.NSEncryption(new File(Objects.requireNonNull(str2)));
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            GetAlbumsFromDatabase(_SortBy);
            Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor managedQuery = managedQuery(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = managedQuery.getColumnIndexOrThrow("_data");
        managedQuery.moveToFirst();
        return managedQuery.getString(columnIndexOrThrow);
    }

    private void AddVideoToDatabase(String str, String str2, String str3, String str4) {
        Video video = new Video();
        video.setVideoName(str);
        video.setFolderLockVideoLocation(str4);
        video.setOriginalVideoLocation(str2);
        video.setthumbnail_video_location(str3);
        video.setAlbumId(AlbumId);
        VideoDAL videoDAL = new VideoDAL(this);
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

    public String GetCoverPhotoLocation(int i) {
        new Video();
        VideoDAL videoDAL = new VideoDAL(this);
        try {
            videoDAL.OpenRead();
            String str = videoDAL.GetCoverVideo(i).getthumbnail_video_location();
            videoDAL.close();
            return str;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            videoDAL.close();
            return null;
        } catch (Throwable th) {
            videoDAL.close();
            throw th;
        }
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
    public void onStop() {
        Common.imageLoader.clearMemoryCache();
        Common.imageLoader.clearDiskCache();
        super.onStop();
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
            if (isEditMode) {
                SecurityLocksCommon.IsAppDeactive = false;
                isEditMode = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new VideosAlbumsAdapter(this, 17367043, videoAlbum, 0, isEditMode);
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
}
