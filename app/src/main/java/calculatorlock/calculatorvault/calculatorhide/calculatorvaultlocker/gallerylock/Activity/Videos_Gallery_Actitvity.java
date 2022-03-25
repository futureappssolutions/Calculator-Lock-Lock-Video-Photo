package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.BuildConfig;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.MoveAlbumAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.AppGalleryVideoAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Video;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.VideoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoDAL;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Videos_Gallery_Actitvity extends BaseActivity {
    public static ProgressDialog myProgressDialog;
    private final ArrayList<String> files = new ArrayList<>();
    protected String folderLocation;

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 2) {
                hideProgress();
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(Videos_Gallery_Actitvity.this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(Videos_Gallery_Actitvity.this, R.string.Move_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(Videos_Gallery_Actitvity.this, R.string.Delete_error, Toast.LENGTH_SHORT).show();
                }
            } else if (message.what == 4) {
                Toast.makeText(Videos_Gallery_Actitvity.this, R.string.toast_share, Toast.LENGTH_LONG).show();
            } else if (message.what == 3) {
                if (Common.isUnHide) {
                    RefershGalleryforKitkat();
                    Common.isUnHide = false;
                    Toast.makeText(Videos_Gallery_Actitvity.this, R.string.toast_unhide, Toast.LENGTH_LONG).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(Videos_Gallery_Actitvity.this, Videos_Gallery_Actitvity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(Videos_Gallery_Actitvity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent2 = new Intent(Videos_Gallery_Actitvity.this, Videos_Gallery_Actitvity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        finish();
                    }
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(Videos_Gallery_Actitvity.this, R.string.toast_move, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent3 = new Intent(Videos_Gallery_Actitvity.this, Videos_Gallery_Actitvity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        finish();
                    }
                }
            }
            super.handleMessage(message);
        }
    };
    private List<Video> videos;
    private List<String> _albumNameArrayForMove = null;
    private String albumName;
    private String moveToFolderLocation;
    private LinearLayout ll_EditPhotos;
    private LinearLayout ll_photo_video_empty;
    private LinearLayout ll_photo_video_grid;
    private ImageView photo_video_empty_icon;
    private TextView lbl_photo_video_empty;
    private GridView imagegrid;
    private RelativeLayout.LayoutParams ll_GridviewParams;

    private SensorManager sensorManager;
    private VideoDAL videoDAL;
    private AppGalleryVideoAdapter galleryVideosAdapter;
    private int videoCount = 0;
    private int selectCount;
    private int _SortBy = 1;
    private boolean isEditMode = false;
    private boolean IsSelectAll = false;
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

    public void showMoveProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Please be patient... this may take a few moments...", true);
    }

    private void showIsImportingProgress() {
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photos_videos_gallery);

        SecurityLocksCommon.IsAppDeactive = true;
        Common.IsPhoneGalleryLoad = true;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fabImpCam = findViewById(R.id.btn_impCam);
        FloatingActionButton fabImpGallery = findViewById(R.id.btn_impGallery);
        LinearLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        LinearLayout ll_unhide_btn = findViewById(R.id.ll_unhide_btn);
        LinearLayout ll_move_btn = findViewById(R.id.ll_move_btn);
        LinearLayout ll_share_btn = findViewById(R.id.ll_share_btn);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        toolbar.setNavigationOnClickListener(view -> Back());

        ll_EditPhotos = findViewById(R.id.ll_EditPhotos);
        imagegrid = findViewById(R.id.customGalleryGrid);
        ll_photo_video_empty = findViewById(R.id.ll_photo_video_empty);
        ll_photo_video_grid = findViewById(R.id.ll_photo_video_grid);
        photo_video_empty_icon = findViewById(R.id.photo_video_empty_icon);
        lbl_photo_video_empty = findViewById(R.id.lbl_photo_video_empty);

        ll_GridviewParams = new RelativeLayout.LayoutParams(-1, -1);

        ll_photo_video_grid.setVisibility(View.VISIBLE);
        ll_photo_video_empty.setVisibility(View.INVISIBLE);

        fabImpGallery.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.IsCameFromPhotoAlbum = false;
            startActivity(new Intent(Videos_Gallery_Actitvity.this, ImportAlbumsGalleryVideoActivity.class));
            finish();
        });

        fabImpCam.setOnClickListener(view -> dispatchTakeVideoIntent());

        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(this);
        videoAlbumDAL.OpenRead();
        VideoAlbum GetAlbumById = videoAlbumDAL.GetAlbumById(Integer.toString(Common.FolderId));
        _SortBy = videoAlbumDAL.GetSortByAlbumId(Common.FolderId);
        videoAlbumDAL.close();
        albumName = GetAlbumById.getAlbumName();
        Common.VideoFolderName = albumName;
        folderLocation = GetAlbumById.getAlbumLocation();

        Objects.requireNonNull(getSupportActionBar()).setTitle(albumName);

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsSortingDropdown) {
                IsSortingDropdown = false;
            }
            return false;
        });

        imagegrid.setOnItemClickListener((adapterView, view, i, j) -> {
            String str;
            if (!isEditMode) {
                Common.VideoThumbnailCurrentPosition = imagegrid.getFirstVisiblePosition();
                File file = new File(videos.get(i).getFolderLockVideoLocation());
                SecurityLocksCommon.IsAppDeactive = false;
                if (file.exists()) {
                    try {
                        str = Utilities.NSVideoDecryptionDuringPlay(new File(videos.get(i).getFolderLockVideoLocation()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        str = "";
                    }
                } else {
                    str = file.getParent() + File.separator + Utilities.ChangeFileExtentionToOrignal(Utilities.FileName(file.getAbsolutePath()));
                }
                String substring = str.substring(str.lastIndexOf(".") + 1);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(FileProvider.getUriForFile(Videos_Gallery_Actitvity.this, BuildConfig.APPLICATION_ID, new File(str)), MimeTypeMap.getSingleton().getMimeTypeFromExtension(substring));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        imagegrid.setOnItemLongClickListener((adapterView, view, i, j) -> {
            Common.VideoThumbnailCurrentPosition = imagegrid.getFirstVisiblePosition();
            isEditMode = true;
            ll_EditPhotos.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            videos.get(i).SetFileCheck(true);
            galleryVideosAdapter = new AppGalleryVideoAdapter(Videos_Gallery_Actitvity.this, 1, videos, true);
            imagegrid.setAdapter(galleryVideosAdapter);
            galleryVideosAdapter.notifyDataSetChanged();
            if (Common.VideoThumbnailCurrentPosition != 0) {
                imagegrid.setSelection(Common.VideoThumbnailCurrentPosition);
            }
            return true;
        });

        ll_delete_btn.setOnClickListener(view -> DeleteVideos());

        ll_unhide_btn.setOnClickListener(view -> UnhideVideos());

        ll_move_btn.setOnClickListener(view -> MoveVideos());

        ll_share_btn.setOnClickListener(view -> {
            if (!IsFileCheck()) {
                Toast.makeText(Videos_Gallery_Actitvity.this, R.string.toast_unselectvideomsg_share, Toast.LENGTH_SHORT).show();
            } else {
                ShareVideos();
            }
        });

        if (Common.IsImporting) {
            showIsImportingProgress();
        } else if (Common.isUnHide) {
            showUnhideProgress();
        } else if (Common.isDelete) {
            showDeleteProgress();
        } else if (Common.isMove) {
            showMoveProgress();
        } else {
            LoadVideosFromDB(_SortBy);
            if (Common.VideoThumbnailCurrentPosition == 0) {
                imagegrid.setSelection(Common.VideoThumbnailCurrentPosition);
                Common.VideoThumbnailCurrentPosition = 0;
            }
        }
    }

    public void setUIforlistView() {
        imagegrid.setVerticalSpacing(Utilities.convertDptoPix(getApplicationContext(), 1));
        imagegrid.setHorizontalSpacing(Utilities.convertDptoPix(getApplicationContext(), 0));
        ll_GridviewParams.setMargins(0, 0, 0, 0);
        ll_photo_video_grid.setLayoutParams(ll_GridviewParams);
        imagegrid.setNumColumns(1);
    }

    public void btnBackonClick(View view) {
        Back();
    }

    public void Back() {
        if (isEditMode) {
            SetcheckFlase();
            isEditMode = false;
            IsSortingDropdown = false;
            ll_EditPhotos.setVisibility(View.GONE);
            IsSelectAll = false;
            invalidateOptionsMenu();
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            Common.VideoFolderName = StorageOptionsCommon.VIDEOS_DEFAULT_ALBUM;
            startActivity(new Intent(this, VideosAlbumActivty.class));
            finish();
        }
        DeleteTemporaryVideos();
    }

    @SuppressLint("SetTextI18n")
    public void UnhideVideos() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectvideomsg_unhide, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        if (Common.GetTotalFree() > Common.GetFileSize(files)) {
            final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.confirmation_message_box);
            dialog.setCancelable(true);
            TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
            textView.setText("Are you sure you want to restore (" + selectCount + ") video(s)?");

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
                        } catch (Exception unused) {
                            Message message2 = new Message();
                            message2.what = 2;
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
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).GetFileCheck()) {
                if (Utilities.NSUnHideFile(this, videos.get(i).getFolderLockVideoLocation(), videos.get(i).getOriginalVideoLocation())) {
                    new File(videos.get(i).getthumbnail_video_location()).delete();
                    DeleteFromDatabase(videos.get(i).getId());
                } else {
                    Toast.makeText(this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void DeleteVideos() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectvideomsg_delete, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        textView.setText("Are you sure you want to delete (" + selectCount + ") video(s)?");

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
                        message2.what = 2;
                        handle.sendMessage(message2);
                    }
                }
            }.start();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void Delete() {
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).GetFileCheck()) {
                new File(videos.get(i).getFolderLockVideoLocation()).delete();
                new File(videos.get(i).getthumbnail_video_location()).delete();
                DeleteFromDatabase(videos.get(i).getId());
            }
        }
    }

    public void DeleteFromDatabase(int i) {
        videoDAL = new VideoDAL(this);
        try {
            videoDAL.OpenWrite();
            videoDAL.DeleteVideoById(i);
            if (videoDAL == null) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (videoDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (videoDAL != null) {
                videoDAL.close();
            }
            throw th;
        }
        videoDAL.close();
    }

    public void SelectedCount() {
        files.clear();
        selectCount = 0;
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).GetFileCheck()) {
                files.add(videos.get(i).getFolderLockVideoLocation());
                selectCount++;
            }
        }
    }

    public boolean IsFileCheck() {
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).GetFileCheck()) {
                return true;
            }
        }
        return false;
    }

    public void MoveVideos() {
        videoDAL = new VideoDAL(this);
        videoDAL.OpenWrite();
        String[] _albumNameArray = videoDAL.GetAlbumNames(Common.FolderId);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectvideomsg_move, Toast.LENGTH_SHORT).show();
        } else if (_albumNameArray.length > 0) {
            GetAlbumNameFromDB();
        } else {
            Toast.makeText(this, R.string.toast_OneAlbum, Toast.LENGTH_SHORT).show();
        }
    }

    public void Move(String str, String str2, String str3) throws IOException {
        String str4;
        VideoAlbum GetAlbum = GetAlbum(str3);
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).GetFileCheck()) {
                if (videos.get(i).getVideoName().contains("#")) {
                    str4 = videos.get(i).getVideoName();
                } else {
                    str4 = Utilities.ChangeFileExtention(videos.get(i).getVideoName());
                }
                String str5 = str2 + "/" + str4;
                if (Utilities.MoveFileWithinDirectories(videos.get(i).getFolderLockVideoLocation(), str5)) {
                    String str6 = videos.get(i).getthumbnail_video_location();
                    String FileName = Utilities.FileName(videos.get(i).getthumbnail_video_location());
                    if (!FileName.contains("#")) {
                        FileName = Utilities.ChangeFileExtention(FileName);
                    }
                    String str7 = str2 + "/VideoThumnails/" + FileName;
                    if (Utilities.MoveFileWithinDirectories(str6, str7)) {
                        UpdateVideoLocationInDatabase(videos.get(i), str5, GetAlbum.getId(), str7);
                        Common.FolderId = GetAlbum.getId();
                    }
                }
            }
        }
    }

    public void UpdateVideoLocationInDatabase(Video video, String str, int i, String str2) {
        video.setFolderLockVideoLocation(str);
        video.setthumbnail_video_location(str2);
        video.setAlbumId(i);
        try {
            videoDAL.OpenWrite();
            videoDAL.UpdateVideoLocationById(video);
            if (videoDAL == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (videoDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (videoDAL != null) {
                videoDAL.close();
            }
            throw th;
        }
        videoDAL.close();
    }

    public VideoAlbum GetAlbum(String str) {
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(this);
        try {
            videoAlbumDAL.OpenRead();
            return videoAlbumDAL.GetAlbum(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Throwable th) {
            videoAlbumDAL.close();
            throw th;
        }
    }

    private void GetAlbumNameFromDB() {
        videoDAL = new VideoDAL(this);
        try {
            videoDAL.OpenWrite();
            _albumNameArrayForMove = videoDAL.GetMoveAlbumNames(Common.FolderId);
            MoveVideoDialog();
            if (videoDAL == null) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (videoDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (videoDAL != null) {
                videoDAL.close();
            }
            throw th;
        }
        videoDAL.close();
    }

    public void MoveVideoDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.move_customlistview);
        ListView listView = dialog.findViewById(R.id.ListViewfolderslist);
        listView.setAdapter(new MoveAlbumAdapter(this, 17367043, _albumNameArrayForMove, R.drawable.empty_folder_video_icon));

        listView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (_albumNameArrayForMove != null) {
                SelectedCount();
                showMoveProgress();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Common.isMove = true;
                            dialog.dismiss();
                            moveToFolderLocation = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + _albumNameArrayForMove.get(i);
                            Move(folderLocation, moveToFolderLocation, _albumNameArrayForMove.get(i));
                            Common.IsWorkInProgress = true;
                            Message message = new Message();
                            message.what = 3;
                            handle.sendMessage(message);
                            Common.IsWorkInProgress = false;
                        } catch (Exception unused) {
                            Message message2 = new Message();
                            message2.what = 2;
                            handle.sendMessage(message2);
                        }
                    }
                }.start();
            }
        });
        dialog.show();
    }

    public void ShareVideos() {
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
                            String str2 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS;
                            ArrayList<String> arrayList2 = new ArrayList<>();
                            ArrayList<Uri> arrayList3 = new ArrayList<>();
                            for (int i = 0; i < videos.size(); i++) {
                                if (videos.get(i).GetFileCheck()) {
                                    try {
                                        str2 = Utilities.CopyTemporaryFile(Videos_Gallery_Actitvity.this, videos.get(i).getFolderLockVideoLocation(), str2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    arrayList2.add(str2);
                                    arrayList3.add(FileProvider.getUriForFile(Videos_Gallery_Actitvity.this, BuildConfig.APPLICATION_ID, new File(str2)));
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

    public void DeleteTemporaryVideos() {
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + "/");
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
            Uri mVideoUri = Utilities.getLastPhotoOrVideo(this);
            String encodedPath = mVideoUri.getEncodedPath();
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
            File file4;
            file4 = new File(StorageOptionsCommon.STORAGEPATH_1 + StorageOptionsCommon.VIDEOS + albumName + "/" + ChangeFileExtention);
            try {
                str2 = Utilities.NSHideFile(this, file, new File(Objects.requireNonNull(file4.getParent())));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            AddVideoToDatabase(Utilities.ChangeFileExtentionToOrignal(ChangeFileExtention), encodedPath, str, str2);
            file.delete();
            try {
                assert str2 != null;
                Utilities.NSEncryption(new File(str2));
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            LoadVideosFromDB(_SortBy);
            Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_LONG).show();
        }
    }

    private void AddVideoToDatabase(String str, String str2, String str3, String str4) {
        Video video = new Video();
        video.setVideoName(str);
        video.setFolderLockVideoLocation(str4);
        video.setOriginalVideoLocation(str2);
        video.setthumbnail_video_location(str3);
        video.setAlbumId(Common.FolderId);
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

    private void SetcheckFlase() {
        for (int i = 0; i < videos.size(); i++) {
            videos.get(i).SetFileCheck(false);
        }
        galleryVideosAdapter = new AppGalleryVideoAdapter(this, 1, videos, false);
        imagegrid.setAdapter(galleryVideosAdapter);
        galleryVideosAdapter.notifyDataSetChanged();
        if (Common.VideoThumbnailCurrentPosition != 0) {
            imagegrid.setSelection(Common.VideoThumbnailCurrentPosition);
            Common.VideoThumbnailCurrentPosition = 0;
        }
    }

    public void LoadVideosFromDB(int i) {
        videos = new ArrayList<>();
        VideoDAL videoDAL = new VideoDAL(this);
        videoDAL.OpenRead();
        videoCount = videoDAL.GetVideoCountByAlbumId(Common.FolderId);
        videos = videoDAL.GetVideoByAlbumId(Common.FolderId, i);
        videoDAL.close();
        galleryVideosAdapter = new AppGalleryVideoAdapter(this, 1, videos, false);
        imagegrid.setAdapter(galleryVideosAdapter);
        galleryVideosAdapter.notifyDataSetChanged();
        if (videos.size() < 1) {
            ll_photo_video_grid.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            photo_video_empty_icon.setBackgroundResource(R.drawable.ic_video_empty_icon);
            lbl_photo_video_empty.setText(R.string.lbl_No_Video);
            return;
        }
        ll_photo_video_grid.setVisibility(View.VISIBLE);
        ll_photo_video_empty.setVisibility(View.INVISIBLE);
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
        Common.IsWorkInProgress = true;
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
        handle.removeCallbacksAndMessages(null);
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
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        SetcheckFlase();
        Utilities.changeFileExtention(StorageOptionsCommon.VIDEOS);
        isEditMode = false;
        ll_EditPhotos.setVisibility(View.GONE);
        IsSelectAll = false;
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
            if (isEditMode) {
                SetcheckFlase();
                isEditMode = false;
                IsSortingDropdown = false;
                ll_EditPhotos.setVisibility(View.GONE);
                IsSelectAll = false;
                invalidateOptionsMenu();
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            Common.VideoFolderName = StorageOptionsCommon.VIDEOS_DEFAULT_ALBUM;
            startActivity(new Intent(this, VideosAlbumActivty.class));
            finish();
            DeleteTemporaryVideos();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        menu.findItem(R.id.action_more);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isEditMode) {
            menu.findItem(R.id.action_more).setVisible(false);
            getMenuInflater().inflate(R.menu.menu_selection, menu);
        } else {
            menu.findItem(R.id.action_more).setVisible(false);
            if (IsSelectAll && isEditMode) {
                menu.findItem(R.id.action_select).setIcon(R.drawable.ic_unselectallicon);
            } else if (!IsSelectAll && isEditMode) {
                menu.findItem(R.id.action_select).setIcon(R.drawable.ic_selectallicon);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
