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
import android.view.View;
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

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.BuildConfig;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.AlbumsGalleryPhotosMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.MoveAlbumAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.PhoneGalleryPhotoAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.PhotoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Photos_Gallery_Actitvity extends BaseActivity {
    private static final int RESULT_LOAD_CAMERA = 1;
    public static ProgressDialog myProgressDialog;
    private final ArrayList<String> files = new ArrayList<>();
    public PhoneGalleryPhotoAdapter galleryImagesAdapter;
    public String moveToFolderLocation;
    public String albumName;
    public List<Photo> photos;
    public List<String> _albumNameArrayForMove = null;
    public LinearLayout ll_EditPhotos;
    public LinearLayout ll_anchor;
    public LinearLayout ll_background;
    public LinearLayout ll_delete_btn;
    public LinearLayout ll_move_btn;
    public LinearLayout ll_photo_video_empty;
    public LinearLayout ll_photo_video_grid;
    public LinearLayout ll_share_btn;
    public LinearLayout ll_unhide_btn;
    public ImageView photo_video_empty_icon;
    public TextView lbl_photo_video_empty;
    public RelativeLayout.LayoutParams ll_GridviewParams;

    public int selectCount;
    public int _SortBy = 1;
    public int photoCount = 0;
    public boolean IsSortingDropdown = false;
    public boolean isAddbarvisible = false;
    public boolean isEditMode = false;
    public boolean IsSelectAll = false;
    protected String folderLocation;

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 2) {
                hideProgress();
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(Photos_Gallery_Actitvity.this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(Photos_Gallery_Actitvity.this, R.string.Move_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(Photos_Gallery_Actitvity.this, R.string.Delete_error, Toast.LENGTH_SHORT).show();
                }
            } else if (message.what == 4) {
                hideProgress();
                Toast.makeText(Photos_Gallery_Actitvity.this, R.string.toast_share, Toast.LENGTH_LONG).show();
            } else if (message.what == 3) {
                if (Common.isUnHide) {
                    RefershGalleryforKitkat();
                    Common.isUnHide = false;
                    Toast.makeText(Photos_Gallery_Actitvity.this, R.string.toast_unhide, Toast.LENGTH_LONG).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(Photos_Gallery_Actitvity.this, Photos_Gallery_Actitvity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(Photos_Gallery_Actitvity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent2 = new Intent(Photos_Gallery_Actitvity.this, Photos_Gallery_Actitvity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        finish();
                    }
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(Photos_Gallery_Actitvity.this, R.string.toast_move, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent3 = new Intent(Photos_Gallery_Actitvity.this, Photos_Gallery_Actitvity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        finish();
                    }
                }
            }
            super.handleMessage(message);
        }
    };

    private PhotoDAL photoDAL;
    private SensorManager sensorManager;
    private File cacheDir;
    private GridView imagegrid;

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

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(Photos_Gallery_Actitvity.this, ll_banner);

        Common.IsPhoneGalleryLoad = true;
        SecurityLocksCommon.IsAppDeactive = true;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fabImpCam = findViewById(R.id.btn_impCam);
        FloatingActionButton fabImpGallery = findViewById(R.id.btn_impGallery);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);
        toolbar.setNavigationOnClickListener(view -> Back());

        ll_anchor = findViewById(R.id.ll_anchor);
        ll_background = findViewById(R.id.ll_background);
        ll_EditPhotos = findViewById(R.id.ll_EditPhotos);
        imagegrid = findViewById(R.id.customGalleryGrid);
        ll_delete_btn = findViewById(R.id.ll_delete_btn);
        ll_unhide_btn = findViewById(R.id.ll_unhide_btn);
        ll_move_btn = findViewById(R.id.ll_move_btn);
        ll_share_btn = findViewById(R.id.ll_share_btn);
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
            startActivity(new Intent(Photos_Gallery_Actitvity.this, ImportAlbumsGalleryPhotoActivity.class));
            finish();
        });

        fabImpCam.setOnClickListener(view -> {
            Common.isOpenCameraorGalleryFromApp = true;
            openCameraIntent();
        });

        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this);
        photoAlbumDAL.OpenRead();
        PhotoAlbum GetAlbumById = photoAlbumDAL.GetAlbumById(Integer.toString(Common.FolderId));
        _SortBy = photoAlbumDAL.GetSortByAlbumId(Common.FolderId);
        photoAlbumDAL.close();
        albumName = GetAlbumById.getAlbumName();
        Common.PhotoFolderName = albumName;
        folderLocation = GetAlbumById.getAlbumLocation();

        Objects.requireNonNull(getSupportActionBar()).setTitle(albumName);

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsSortingDropdown) {
                IsSortingDropdown = false;
            }
            return false;
        });

        imagegrid.setOnItemClickListener((adapterView, view, i, j) -> {
            ll_EditPhotos.setVisibility(View.GONE);
            if (!isEditMode) {
                Common.PhotoThumbnailCurrentPosition = imagegrid.getFirstVisiblePosition();
                SecurityLocksCommon.IsAppDeactive = false;
                Common.IsCameFromAppGallery = true;
                Intent intent = new Intent(Photos_Gallery_Actitvity.this, NewFullScreenViewActivity.class);
                intent.putExtra("IMAGE_URL", photos.get(i).getFolderLockPhotoLocation());
                intent.putExtra("IMAGE_POSITION", i);
                intent.putExtra("ALBUM_ID", photos.get(i).getAlbumId());
                intent.putExtra("ALBUM_NAME", albumName);
                intent.putExtra("ALBUM_LOCATION", folderLocation);
                intent.putExtra("_SortBy", _SortBy);
                startActivity(intent);
                finish();
            }
        });

        imagegrid.setOnItemLongClickListener((adapterView, view, i, j) -> {
            Common.PhotoThumbnailCurrentPosition = imagegrid.getFirstVisiblePosition();
            isEditMode = true;
            ll_EditPhotos.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            photos.get(i).SetFileCheck(true);
            galleryImagesAdapter = new PhoneGalleryPhotoAdapter(Photos_Gallery_Actitvity.this, 1, photos, true);
            imagegrid.setAdapter(galleryImagesAdapter);
            galleryImagesAdapter.notifyDataSetChanged();
            if (Common.PhotoThumbnailCurrentPosition != 0) {
                imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
            }
            return true;
        });

        ll_delete_btn.setOnClickListener(view -> DeletePhotos());

        ll_unhide_btn.setOnClickListener(view -> UnhidePhotos());

        ll_move_btn.setOnClickListener(view -> MovePhotos());

        ll_share_btn.setOnClickListener(view -> {
            if (!IsFileCheck()) {
                Toast.makeText(Photos_Gallery_Actitvity.this, R.string.toast_unselectphotomsg_share, Toast.LENGTH_SHORT).show();
            } else {
                SharePhotos();
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
            LoadPhotosFromDB(_SortBy);
            if (Common.PhotoThumbnailCurrentPosition == 0) {
                imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
                Common.PhotoThumbnailCurrentPosition = 0;
            }
        }
    }

    private void SetcheckFlase() {
        for (int i = 0; i < photos.size(); i++) {
            photos.get(i).SetFileCheck(false);
        }
        galleryImagesAdapter = new PhoneGalleryPhotoAdapter(this, 1, photos, false);
        imagegrid.setAdapter(galleryImagesAdapter);
        galleryImagesAdapter.notifyDataSetChanged();
        if (Common.PhotoThumbnailCurrentPosition != 0) {
            imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
            Common.PhotoThumbnailCurrentPosition = 0;
        }
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
        } else if (isAddbarvisible) {
            isAddbarvisible = false;
            ll_EditPhotos.setVisibility(View.GONE);
            IsSortingDropdown = false;
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            Common.PhotoFolderName = "My Photos";
            startActivity(new Intent(this, PhotosAlbumActivty.class));
            finish();
        }
        DeleteTemporaryPhotos();
    }

    @SuppressLint("SetTextI18n")
    public void UnhidePhotos() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_unhide, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        if (Common.GetTotalFree() > Common.GetFileSize(files)) {
            final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.confirmation_message_box);
            dialog.setCancelable(true);
            TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
            textView.setText("Are you sure you want to restore (" + selectCount + ") photo(s)?");

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
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).GetFileCheck()) {
                if (Utilities.NSUnHideFile(this, photos.get(i).getFolderLockPhotoLocation(), photos.get(i).getOriginalPhotoLocation())) {
                    DeleteFromDatabase(photos.get(i).getId());
                } else {
                    Toast.makeText(this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void DeletePhotos() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_delete, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        textView.setText("Are you sure you want to delete (" + selectCount + ") photo(s)?");

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

    public void Delete() {
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).GetFileCheck()) {
                new File(photos.get(i).getFolderLockPhotoLocation()).delete();
                DeleteFromDatabase(photos.get(i).getId());
            }
        }
    }

    public void DeleteFromDatabase(int i) {
        photoDAL = new PhotoDAL(this);
        try {
            photoDAL.OpenWrite();
            photoDAL.DeletePhotoById(i);
            if (photoDAL == null) {
                return;
            }
        } catch (Exception e) {
            if (photoDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (photoDAL != null) {
                photoDAL.close();
            }
            throw th;
        }
        photoDAL.close();
    }

    public void SelectedCount() {
        files.clear();
        selectCount = 0;
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).GetFileCheck()) {
                files.add(photos.get(i).getFolderLockPhotoLocation());
                selectCount++;
            }
        }
    }

    public boolean IsFileCheck() {
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).GetFileCheck()) {
                return true;
            }
        }
        return false;
    }

    public void MovePhotos() {
        photoDAL = new PhotoDAL(this);
        photoDAL.OpenWrite();
        String[] _albumNameArray = photoDAL.GetAlbumNames(Common.FolderId);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_move, Toast.LENGTH_SHORT).show();
        } else if (_albumNameArray.length > 0) {
            GetAlbumNameFromDB();
        } else {
            Toast.makeText(this, R.string.toast_OneAlbum, Toast.LENGTH_SHORT).show();
        }
    }

    public void Move(String str, String str2, String str3) {
        String str4;
        PhotoAlbum GetAlbum = GetAlbum(str3);
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).GetFileCheck()) {
                if (photos.get(i).getPhotoName().contains("#")) {
                    str4 = photos.get(i).getPhotoName();
                } else {
                    str4 = Utilities.ChangeFileExtention(photos.get(i).getPhotoName());
                }
                String str5 = str2 + "/" + str4;
                try {
                    if (Utilities.MoveFileWithinDirectories(photos.get(i).getFolderLockPhotoLocation(), str5)) {
                        UpdatePhotoLocationInDatabase(photos.get(i), str5, GetAlbum.getId());
                        Common.FolderId = GetAlbum.getId();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void UpdatePhotoLocationInDatabase(Photo photo, String str, int i) {
        photo.setFolderLockPhotoLocation(str);
        photo.setAlbumId(i);
        try {
            PhotoDAL photoDAL2 = new PhotoDAL(this);
            photoDAL2.OpenWrite();
            photoDAL2.UpdatePhotoLocation(photo);
            if (photoDAL == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (photoDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (photoDAL != null) {
                photoDAL.close();
            }
            throw th;
        }
        photoDAL.close();
    }

    public PhotoAlbum GetAlbum(String str) {
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this);
        try {
            photoAlbumDAL.OpenRead();
            return photoAlbumDAL.GetAlbum(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Throwable th) {
            photoAlbumDAL.close();
            throw th;
        }
    }

    private void GetAlbumNameFromDB() {
        photoDAL = new PhotoDAL(this);
        try {
            photoDAL.OpenWrite();
            _albumNameArrayForMove = photoDAL.GetMoveAlbumNames(Common.FolderId);
            MovePhotoDialog();
            if (photoDAL == null) {
                return;
            }
        } catch (Exception e) {
            if (photoDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (photoDAL != null) {
                photoDAL.close();
            }
            throw th;
        }
        photoDAL.close();
    }

    public void MovePhotoDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.move_customlistview);
        ListView listView = dialog.findViewById(R.id.ListViewfolderslist);

        listView.setAdapter(new MoveAlbumAdapter(this, 17367043, _albumNameArrayForMove, R.drawable.empty_folder_album_icon));

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
                            moveToFolderLocation = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + _albumNameArrayForMove.get(i);
                            Move(folderLocation, moveToFolderLocation, _albumNameArrayForMove.get(i));
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
            }
        });
        dialog.show();
    }

    public void SharePhotos() {
        showCopyFilesProcessForShareProgress();
        new Thread() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void run() {
                try {
                    SecurityLocksCommon.IsAppDeactive = false;
                    ArrayList<Intent> arrayList = new ArrayList<>();
                    ArrayList<String> arrayList2 = new ArrayList<>();
                    ArrayList<Uri> arrayList3 = new ArrayList<>();
                    Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
                    intent.setType("image/*");
                    for (ResolveInfo resolveInfo : getPackageManager().queryIntentActivities(intent, 0)) {
                        String str = resolveInfo.activityInfo.packageName;
                        if (!str.equals(getPackageName()) && !str.equals("com.dropbox.android") && !str.equals("com.facebook.katana")) {
                            Intent intent2 = new Intent("android.intent.action.SEND_MULTIPLE");
                            intent2.setType("image/*");
                            intent2.setPackage(str);
                            arrayList.add(intent2);
                            String str2 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS;

                            for (int i = 0; i < photos.size(); i++) {
                                if (photos.get(i).GetFileCheck()) {
                                    try {
                                        str2 = Utilities.CopyTemporaryFile(Photos_Gallery_Actitvity.this, photos.get(i).getFolderLockPhotoLocation(), str2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    arrayList2.add(str2);
                                    arrayList3.add(FileProvider.getUriForFile(Photos_Gallery_Actitvity.this, BuildConfig.APPLICATION_ID, new File(str2)));
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

    public void DeleteTemporaryPhotos() {
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + "/");
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

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        SecurityLocksCommon.IsAppDeactive = true;
        if (i == RESULT_LOAD_CAMERA && i2 == -1 && cacheDir != null) {
            String str = null;
            try {
                str = Utilities.NSHideFile(this, cacheDir, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + albumName));
                Utilities.NSEncryption(new File(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!Objects.equals(str, "")) {
                new AlbumsGalleryPhotosMethods().AddPhotoToDatabase(this, Common.FolderId, cacheDir.getName(), str, cacheDir.getAbsolutePath());
                Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
                LoadPhotosFromDB(_SortBy);
            }
        }
    }

    public void LoadPhotosFromDB(int i) {
        photos = new ArrayList<>();
        PhotoDAL photoDAL = new PhotoDAL(this);
        photoDAL.OpenRead();
        photoCount = photoDAL.GetPhotoCountByAlbumId(Common.FolderId);
        photos = photoDAL.GetPhotoByAlbumId(Common.FolderId, i);
        photoDAL.close();
        galleryImagesAdapter = new PhoneGalleryPhotoAdapter(this, 1, photos, false);
        imagegrid.setAdapter(galleryImagesAdapter);
        galleryImagesAdapter.notifyDataSetChanged();
        if (photos.size() < 1) {
            ll_photo_video_grid.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            photo_video_empty_icon.setBackgroundResource(R.drawable.ic_photo_empty_icon);
            lbl_photo_video_empty.setText(R.string.lbl_No_Photos);
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
        super.onResume();
        SetcheckFlase();
        IsSortingDropdown = false;
        isEditMode = false;
        ll_EditPhotos.setVisibility(View.GONE);
        IsSelectAll = false;
        invalidateOptionsMenu();
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
                IsSortingDropdown = false;
                isEditMode = false;
                ll_EditPhotos.setVisibility(View.GONE);
                IsSelectAll = false;
                invalidateOptionsMenu();
                return true;
            } else if (isAddbarvisible) {
                isAddbarvisible = false;
                ll_EditPhotos.setVisibility(View.GONE);
                IsSortingDropdown = false;
                return true;
            } else {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.FolderId = 0;
                Common.PhotoFolderName = "My Photos";
                startActivity(new Intent(this, PhotosAlbumActivty.class));
                finish();
                DeleteTemporaryPhotos();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    @SuppressLint("QueryPermissionsNeeded")
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
        File createTempFile = File.createTempFile("IMG_" + format + "_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        createTempFile.getAbsolutePath();
        return createTempFile;
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

    public enum SortBy {
        Time,
        Name,
        Size
    }

    public enum ViewBy {
        LargeTiles,
        Tiles,
        List
    }
}