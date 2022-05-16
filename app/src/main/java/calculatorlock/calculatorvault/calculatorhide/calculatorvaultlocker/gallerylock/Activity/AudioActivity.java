package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
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
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Flaes;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.AudioDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.AudioFileAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.AudioPlayListDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioPlayListEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.MoveAlbumAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.AppSettingsSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AudioActivity extends BaseActivity implements View.OnClickListener {
    public static int _ViewBy;
    public static ProgressDialog myProgressDialog;
    public final Context context = this;
    private final ArrayList<String> files = new ArrayList<>();
    public List<String> _folderNameArrayForMove = null;
    public AudioFileAdapter audioFileAdapter;
    public String moveToFolderLocation;
    protected String folderLocation;

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 2) {
                hideProgress();
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(AudioActivity.this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isMove) {
                    Common.isMove = false;
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                }
            } else if (message.what == 4) {
                Toast.makeText(AudioActivity.this, R.string.toast_share, Toast.LENGTH_LONG).show();
            } else if (message.what == 3) {
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(AudioActivity.this, R.string.toast_unhide, Toast.LENGTH_LONG).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(AudioActivity.this, AudioActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(AudioActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent2 = new Intent(AudioActivity.this, AudioActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        finish();
                    }
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(AudioActivity.this, R.string.toast_move, Toast.LENGTH_SHORT).show();
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent3 = new Intent(AudioActivity.this, AudioActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        finish();
                    }
                }
            }
            super.handleMessage(message);
        }
    };
    private AppSettingsSharedPreferences appSettingsSharedPreferences;
    private List<AudioEnt> audioEntList;
    private FloatingActionsMenu fabMenu;
    private ImageView file_empty_icon;

    private GridView imagegrid;
    private TextView lbl_file_empty;
    private LinearLayout ll_EditAlbum;
    private LinearLayout ll_file_empty;
    private LinearLayout ll_file_grid;
    private AudioDAL audioDAL;
    private int selectCount;
    private int _SortBy = 1;
    private boolean IsSelectAll = false;
    private boolean IsSortingDropdown = false;
    private boolean isEditMode = false;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_delete_btn) {
            DeleteFiles();
        } else if (id == R.id.ll_move_btn) {
            MoveFiles();
        } else if (id != R.id.ll_share_btn) {
            if (id == R.id.ll_unhide_btn) {
                UnhideFiles();
            }
        } else if (IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectaudiomsg_share, Toast.LENGTH_SHORT).show();
        } else {
            ShareAudio();
        }
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_audio);

        SecurityLocksCommon.IsAppDeactive = true;

        Toolbar toolbar = findViewById(R.id.toolbar);
        LinearLayout ll_unhide_btn = findViewById(R.id.ll_unhide_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        LinearLayout ll_move_btn = findViewById(R.id.ll_move_btn);
        LinearLayout ll_share_btn = findViewById(R.id.ll_share_btn);
        RelativeLayout ll_background = findViewById(R.id.ll_background);
        FloatingActionButton fabImpGallery = findViewById(R.id.btn_impGallery);


        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBanner(AudioActivity.this, ll_banner);

        ll_EditAlbum = findViewById(R.id.ll_EditAlbum);
        imagegrid = findViewById(R.id.customGalleryGrid);
        fabMenu = findViewById(R.id.fabMenu);
        ll_file_empty = findViewById(R.id.ll_photo_video_empty);
        ll_file_grid = findViewById(R.id.ll_photo_video_grid);
        file_empty_icon = findViewById(R.id.photo_video_empty_icon);
        lbl_file_empty = findViewById(R.id.lbl_photo_video_empty);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        ll_file_grid.setVisibility(View.VISIBLE);
        ll_file_empty.setVisibility(View.INVISIBLE);

        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
        audioPlayListDAL.OpenRead();
        AudioPlayListEnt GetPlayListById = audioPlayListDAL.GetPlayListById(Common.FolderId);
        _SortBy = audioPlayListDAL.GetSortByPlaylistId(Common.FolderId);
        audioPlayListDAL.close();
        String folderName = GetPlayListById.getPlayListName();

        Objects.requireNonNull(getSupportActionBar()).setTitle(folderName);

        folderLocation = GetPlayListById.getPlayListLocation();
        appSettingsSharedPreferences = AppSettingsSharedPreferences.GetObject(this);
        _ViewBy = appSettingsSharedPreferences.GetAudioViewBy();

        ll_delete_btn.setOnClickListener(this);
        ll_unhide_btn.setOnClickListener(this);
        ll_move_btn.setOnClickListener(this);
        ll_share_btn.setOnClickListener(this);

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsSortingDropdown) {
                IsSortingDropdown = false;
            }
            return false;
        });

        fabImpGallery.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.IsCameFromPhotoAlbum = false;
            startActivity(new Intent(AudioActivity.this, AudiosImportActivity.class));
            finish();
        });

        imagegrid.setOnItemClickListener((adapterView, view, i, j) -> {
            if (!isEditMode) {
                int id = audioEntList.get(i).getId();
                AudioDAL audioDAL = new AudioDAL(AudioActivity.this);
                audioDAL.OpenRead();
                String folderLockAudioLocation = audioDAL.GetAudio(Integer.toString(id)).getFolderLockAudioLocation();
                audioDAL.close();
                String FileName = Utilities.FileName(folderLockAudioLocation);
                if (FileName.contains("#")) {
                    FileName = Utilities.ChangeFileExtentionToOrignal(FileName);
                }
                File file = new File(folderLockAudioLocation);
                File file2 = new File(file.getParent() + "/" + FileName);
                file.renameTo(file2);
                CopyTempFile(file2.getAbsolutePath());
            }
        });

        imagegrid.setOnItemLongClickListener((adapterView, view, i, j) -> {
            Common.PhotoThumbnailCurrentPosition = imagegrid.getFirstVisiblePosition();
            isEditMode = true;
            audioEntList.get(i).SetFileCheck(true);
            ll_EditAlbum.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            audioFileAdapter = new AudioFileAdapter(AudioActivity.this, AudioActivity.this, 1, audioEntList, true, AudioActivity._ViewBy);
            imagegrid.setAdapter(audioFileAdapter);
            audioFileAdapter.notifyDataSetChanged();
            if (Common.PhotoThumbnailCurrentPosition != 0) {
                imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
            }
            return true;
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
            LoadFilesFromDB(_SortBy);
            if (Common.PhotoThumbnailCurrentPosition != 0) {
                imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
                Common.PhotoThumbnailCurrentPosition = 0;
            }

            imagegrid.setOnItemClickListener((adapterView, view, i, j) -> {
                if (new File(audioEntList.get(i).getFolderLockAudioLocation()).exists()) {
                    SecurityLocksCommon.IsAppDeactive = false;
                    Common.CurrentTrackId = audioEntList.get(i).getId();
                    Intent intent = new Intent(AudioActivity.this, AudioPlayerActivity.class);
                    Common.CurrentTrackNextIndex = i;
                    startActivity(intent);
                    finish();
                    return;
                }
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(getIntent());
                finish();
            });
        }

        imagegrid.setOnItemClickListener((adapterView, view, i, j) -> {
            if (new File(audioEntList.get(i).getFolderLockAudioLocation()).exists()) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.CurrentTrackId = audioEntList.get(i).getId();
                Intent intent = new Intent(AudioActivity.this, AudioPlayerActivity.class);
                Common.CurrentTrackNextIndex = i;
                startActivity(intent);
                finish();
                return;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(getIntent());
            finish();
        });
    }

    private void SetcheckFlase() {
        for (int i = 0; i < audioEntList.size(); i++) {
            audioEntList.get(i).SetFileCheck(false);
        }
        audioFileAdapter = new AudioFileAdapter(this, this, 1, audioEntList, false, _ViewBy);
        imagegrid.setAdapter(audioFileAdapter);
        audioFileAdapter.notifyDataSetChanged();
        if (Common.PhotoThumbnailCurrentPosition != 0) {
            imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
            Common.PhotoThumbnailCurrentPosition = 0;
        }
    }

    public void Back() {
        Common.SelectedCount = 0;
        if (isEditMode) {
            SetcheckFlase();
            isEditMode = false;
            IsSortingDropdown = false;
            IsSelectAll = false;
            Common.IsSelectAll = false;
            ll_EditAlbum.setVisibility(View.GONE);
            invalidateOptionsMenu();
        } else if (fabMenu.isExpanded()) {
            fabMenu.collapse();
            IsSortingDropdown = false;
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            startActivity(new Intent(this, AudioPlayListActivity.class));
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    public void UnhideFiles() {
        if (IsFileCheck()) {
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
            textView.setText("Are you sure you want to restore (" + selectCount + ") audio(s)?");

            dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
            dialog.show();

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
            dialog.show();
        }
    }

    public void Unhide() throws IOException {
        AudioDAL audioDAL = new AudioDAL(this);
        audioDAL.OpenWrite();
        for (AudioEnt audioEnt : audioEntList) {
            if (audioEnt.GetFileCheck()) {
                File file = new File(audioEnt.getFolderLockAudioLocation());
                File file2 = new File(Environment.getExternalStorageDirectory().getPath() + Common.UnhideKitkatAlbumName + audioEnt.getAudioName());
                File file3 = new File(Objects.requireNonNull(file2.getParent()));
                if (!file3.exists() && !file3.mkdirs() && file2.exists()) {
                    file2 = Utilities.GetDesFileNameForUnHide(file2.getAbsolutePath(), file2.getName(), file2);
                }
                if (file.exists()) {
                    file2.createNewFile();
                    Flaes.decryptUsingCipherStream_AES128(file, file2);
                    if (file.exists() && file2.exists()) {
                        file.delete();
                        audioDAL.DeleteAudio(audioEnt);
                    }
                }
            }
        }
        audioDAL.close();
    }

    @SuppressLint("SetTextI18n")
    public void DeleteFiles() {
        if (IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_delete, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        textView.setText("Are you sure you want to delete (" + selectCount + ") audio(s)?");

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
        for (int i = 0; i < audioEntList.size(); i++) {
            if (audioEntList.get(i).GetFileCheck()) {
                new File(audioEntList.get(i).getFolderLockAudioLocation()).delete();
                DeleteFromDatabase(audioEntList.get(i).getId());
            }
        }
    }

    public void DeleteFromDatabase(int i) {
        audioDAL = new AudioDAL(this);
        try {
            audioDAL.OpenWrite();
            audioDAL.DeleteAudioById(i);
            if (audioDAL == null) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (audioDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (audioDAL != null) {
                audioDAL.close();
            }
            throw th;
        }
        audioDAL.close();
    }

    public void SelectedCount() {
        files.clear();
        selectCount = 0;
        for (int i = 0; i < audioEntList.size(); i++) {
            if (audioEntList.get(i).GetFileCheck()) {
                files.add(audioEntList.get(i).getFolderLockAudioLocation());
                selectCount++;
            }
        }
    }

    private boolean IsFileCheck() {
        for (int i = 0; i < audioEntList.size(); i++) {
            if (audioEntList.get(i).GetFileCheck()) {
                return false;
            }
        }
        return true;
    }

    public void MoveFiles() {
        audioDAL = new AudioDAL(this);
        audioDAL.OpenWrite();
        String[] _folderNameArray = audioDAL.GetPlayListNames(Common.FolderId);
        if (IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectdocumentmsg_move, Toast.LENGTH_SHORT).show();
        } else if (_folderNameArray.length > 0) {
            GetFolderNameFromDB();
        } else {
            Toast.makeText(this, R.string.toast_OneFolder, Toast.LENGTH_SHORT).show();
        }
    }

    public void Move(String str, String str2, String str3) {
        String str4;
        AudioPlayListEnt GetAlbum = GetAlbum(str3);
        for (int i = 0; i < audioEntList.size(); i++) {
            if (audioEntList.get(i).GetFileCheck()) {
                if (audioEntList.get(i).getAudioName().contains("#")) {
                    str4 = audioEntList.get(i).getAudioName();
                } else {
                    str4 = Utilities.ChangeFileExtention(audioEntList.get(i).getAudioName());
                }
                String str5 = str2 + "/" + str4;
                try {
                    if (Utilities.MoveFileWithinDirectories(audioEntList.get(i).getFolderLockAudioLocation(), str5)) {
                        UpdateFileLocationInDatabase(audioEntList.get(i), str5, GetAlbum.getId());
                        Common.FolderId = GetAlbum.getId();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void UpdateFileLocationInDatabase(AudioEnt audioEnt, String str, int i) {
        audioEnt.setFolderLockAudioLocation(str);
        audioEnt.setPlayListId(i);
        try {
            AudioDAL audioDAL2 = new AudioDAL(this);
            audioDAL2.OpenWrite();
            audioDAL2.UpdateAudiosLocation(audioEnt);
            if (audioDAL == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (audioDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (audioDAL != null) {
                audioDAL.close();
            }
            throw th;
        }
        audioDAL.close();
    }

    public AudioPlayListEnt GetAlbum(String str) {
        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
        try {
            audioPlayListDAL.OpenRead();
            return audioPlayListDAL.GetPlayList(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Throwable th) {
            audioPlayListDAL.close();
            throw th;
        }
    }

    private void GetFolderNameFromDB() {
        audioDAL = new AudioDAL(this);
        try {
            audioDAL.OpenWrite();
            _folderNameArrayForMove = audioDAL.GetMovePlayListNames(Common.FolderId);
            MoveDocumentDialog();
            if (audioDAL == null) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (audioDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (audioDAL != null) {
                audioDAL.close();
            }
            throw th;
        }
        audioDAL.close();
    }

    public void MoveDocumentDialog() {
        final com.rey.material.app.Dialog dialog = new com.rey.material.app.Dialog(this);
        dialog.setContentView(R.layout.move_customlistview);
        dialog.setTitle(R.string.lbl_Moveto);
        ListView listView = dialog.findViewById(R.id.ListViewfolderslist);
        listView.setAdapter(new MoveAlbumAdapter(this, 17367043, _folderNameArrayForMove, R.drawable.audio_list_icon));

        listView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (_folderNameArrayForMove != null) {
                SelectedCount();
                showMoveProgress();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Common.isMove = true;
                            dialog.dismiss();
                            AudioActivity audioActivity = AudioActivity.this;
                            audioActivity.moveToFolderLocation = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + _folderNameArrayForMove.get(i);
                            Move(folderLocation, moveToFolderLocation, _folderNameArrayForMove.get(i));
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

    public void ShareAudio() {
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
                            String str2 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS;
                            ArrayList<String> arrayList2 = new ArrayList<>();
                            ArrayList<Uri> arrayList3 = new ArrayList<>();
                            for (int i = 0; i < audioEntList.size(); i++) {
                                if (audioEntList.get(i).GetFileCheck()) {
                                    try {
                                        str2 = Utilities.CopyTemporaryFile(AudioActivity.this, audioEntList.get(i).getFolderLockAudioLocation(), str2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    arrayList2.add(str2);
                                    arrayList3.add(FileProvider.getUriForFile(AudioActivity.this, getPackageName(), new File(str2)));
                                }
                            }
                            intent2.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList3);
                        }
                    }
                    Intent createChooser = Intent.createChooser(arrayList.remove(0), "Share Via");
                    createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", arrayList.toArray(new Parcelable[0]));
                    createChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    createChooser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    public void LoadFilesFromDB(int i) {
        audioEntList = new ArrayList<>();
        AudioDAL audioDAL = new AudioDAL(this);
        audioDAL.OpenRead();
        audioEntList = audioDAL.GetAudiosByAlbumId(Common.FolderId, i);
        Common.sortType = i;
        audioDAL.close();
        audioFileAdapter = new AudioFileAdapter(this, this, 1, audioEntList, false, _ViewBy);
        imagegrid.setAdapter(audioFileAdapter);
        audioFileAdapter.notifyDataSetChanged();
        if (audioEntList.size() < 1) {
            ll_file_grid.setVisibility(View.INVISIBLE);
            ll_file_empty.setVisibility(View.VISIBLE);
            file_empty_icon.setBackgroundResource(R.drawable.ic_audio_empty_icon);
            lbl_file_empty.setText(R.string.lbl_No_audio);
            return;
        }
        ll_file_grid.setVisibility(View.VISIBLE);
        ll_file_empty.setVisibility(View.INVISIBLE);
    }

    public void CopyTempFile(String str) {
        File file = new File(str);
        try {
            String guessContentTypeFromName = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(Constants.FILE + file.getAbsolutePath()), guessContentTypeFromName);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Common.SelectedCount = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        Common.IsWorkInProgress = true;
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
        handle.removeCallbacksAndMessages(null);
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SetcheckFlase();
        IsSortingDropdown = false;
        isEditMode = false;
        IsSelectAll = false;
        ll_EditAlbum.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Common.SelectedCount = 0;
        if (i == 4) {
            if (isEditMode) {
                SetcheckFlase();
                IsSortingDropdown = false;
                isEditMode = false;
                IsSelectAll = false;
                Common.IsSelectAll = false;
                ll_EditAlbum.setVisibility(View.GONE);
                invalidateOptionsMenu();
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            startActivity(new Intent(this, AudioPlayListActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void SelectedItemsCount(int i) {
        String selectedCount = Integer.toString(i);
        Log.i("selectedCount", selectedCount);
    }

    public enum SortBy {
        Time,
        Name,
        Size
    }
}
