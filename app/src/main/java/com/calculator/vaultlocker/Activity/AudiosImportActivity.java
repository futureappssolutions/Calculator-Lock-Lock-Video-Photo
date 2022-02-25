package com.calculator.vaultlocker.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vaultlocker.common.Flaes;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.DB.AudioDAL;
import com.calculator.vaultlocker.Model.AudioEnt;
import com.calculator.vaultlocker.Adapter.AudioFoldersImportAdapter;
import com.calculator.vaultlocker.DB.AudioPlayListDAL;
import com.calculator.vaultlocker.Model.AudioPlayListEnt;
import com.calculator.vaultlocker.Adapter.AudioSystemFileAdapter;
import com.calculator.vaultlocker.Model.ImportAlbumEnt;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionSharedPreferences;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.calculator.vaultlocker.utilities.FileUtils;
import com.calculator.vaultlocker.utilities.OnItemClickListener;
import com.calculator.vaultlocker.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AudiosImportActivity extends BaseActivity implements OnItemClickListener {
    private final List<List<AudioEnt>> audioEntListShowList = new ArrayList<>();
    private final List<ImportAlbumEnt> importAlbumEnts = new ArrayList<>();
    private final ArrayList<String> spinnerValues = new ArrayList<>();
    private final ArrayList<AudioEnt> audioImportEntList = new ArrayList<>();
    private final ArrayList<String> selectPath = new ArrayList<>();
    public String selectedCount;
    public String folderName = "";
    public String folderPath = "";
    public boolean isAlbumClick = false;
    public boolean IsExceptionInImportPhotos = false;
    public boolean IsSelectAll = false;
    private ArrayList<AudioEnt> audioImportEntListShow = new ArrayList<>();
    private RecyclerView album_import_ListView;
    private AppCompatImageView btnSelectAll;
    private ImageView document_empty_icon;
    private GridView imagegrid;
    private TextView lbl_photo_video_empty;
    private LinearLayout ll_photo_video_empty;
    private ProgressDialog myProgressDialog = null;
    private AudioSystemFileAdapter filesAdapter;
    private AudioFoldersImportAdapter folderadapter;
    private int folderId;
    private int selectCount;
    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Intent intent;
            if (message.what == 1) {
                hideProgress();
                filesAdapter = new AudioSystemFileAdapter(AudiosImportActivity.this, 1, audioImportEntListShow);
                imagegrid.setAdapter(filesAdapter);
                filesAdapter.notifyDataSetChanged();
                if (audioImportEntListShow.size() <= 0) {
                    album_import_ListView.setVisibility(View.INVISIBLE);
                    imagegrid.setVisibility(View.INVISIBLE);
                    btnSelectAll.setVisibility(View.INVISIBLE);
                    ll_photo_video_empty.setVisibility(View.VISIBLE);
                }
            } else if (message.what == 3) {
                if (Common.IsImporting) {
                    Common.IsImporting = false;
                    Toast.makeText(AudiosImportActivity.this, selectCount + " File(s) imported successfully", Toast.LENGTH_SHORT).show();
                    hideProgress();
                    SecurityLocksCommon.IsAppDeactive = false;
                    if (Common.isFolderImport) {
                        Common.isFolderImport = false;
                        intent = new Intent(AudiosImportActivity.this, AudioActivity.class);
                    } else {
                        intent = new Intent(AudiosImportActivity.this, AudioActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            } else if (message.what == 2) {
                hideProgress();
            }
            super.handleMessage(message);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void ShowImportProgress() {
        myProgressDialog = ProgressDialog.show(this, null, "Your data is being copied... this may take a few moments... ", true);
    }

    public void hideProgress() {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        Back();
        invalidateOptionsMenu();
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_import_album_list);

        SecurityLocksCommon.IsAppDeactive = true;
        Common.IsWorkInProgress = false;

        btnSelectAll = findViewById(R.id.btnSelectAll);
        album_import_ListView = findViewById(R.id.album_import_ListView);
        imagegrid = findViewById(R.id.customGalleryGrid);
        ll_photo_video_empty = findViewById(R.id.ll_photo_video_empty);
        document_empty_icon = findViewById(R.id.photo_video_empty_icon);
        lbl_photo_video_empty = findViewById(R.id.lbl_photo_video_empty);

        TextView lbl_import_photo_album_topbaar = findViewById(R.id.lbl_import_photo_album_topbaar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppCompatButton btnImport = findViewById(R.id.btnImport);
        ProgressBar progress = findViewById(R.id.prbLoading);

        setSupportActionBar(toolbar);
        lbl_import_photo_album_topbaar.setText("Select Audio");

        StorageOptionsCommon.STORAGEPATH = StorageOptionSharedPreferences.GetObject(this).GetStoragePath();
        folderId = Common.FolderId;
        folderName = null;

        album_import_ListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
        audioPlayListDAL.OpenWrite();
        AudioPlayListEnt GetPlayListById = audioPlayListDAL.GetPlayListById(Common.FolderId);
        folderId = GetPlayListById.getId();
        folderName = GetPlayListById.getPlayListName();
        folderPath = GetPlayListById.getPlayListLocation();

        btnSelectAll.setOnClickListener(view -> {
            SelectAllAudios();
            filesAdapter = new AudioSystemFileAdapter(AudiosImportActivity.this, 1, audioImportEntListShow);
            imagegrid.setAdapter(filesAdapter);
            filesAdapter.notifyDataSetChanged();
        });

        btnImport.setOnClickListener(view -> OnImportClick());
        AudioFileBind();
        GetFolders();

        for (AudioEnt next : audioImportEntList) {
            if (spinnerValues.get(0).contains(Objects.requireNonNull(new File(next.getOriginalAudioLocation()).getParent()))) {
                audioImportEntListShow.add(next);
            }
        }

        filesAdapter = new AudioSystemFileAdapter(this, 1, audioImportEntListShow);
        imagegrid.setAdapter(filesAdapter);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        isAlbumClick = true;
        album_import_ListView.setVisibility(View.INVISIBLE);
        imagegrid.setVisibility(View.VISIBLE);
        btnSelectAll.setVisibility(View.VISIBLE);
        folderadapter = new AudioFoldersImportAdapter(AudiosImportActivity.this, importAlbumEnts, false, this);
        album_import_ListView.setAdapter(folderadapter);
        audioImportEntListShow.clear();
        for (AudioEnt next2 : audioImportEntList) {
            if (spinnerValues.get(position).equals(new File(next2.getOriginalAudioLocation()).getParent())) {
                next2.GetFileCheck();
                audioImportEntListShow.add(next2);
            }
        }

        filesAdapter = new AudioSystemFileAdapter(AudiosImportActivity.this, 1, audioImportEntListShow);
        imagegrid.setAdapter(filesAdapter);
        filesAdapter.notifyDataSetChanged();
        if (audioImportEntListShow.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            document_empty_icon.setBackgroundResource(R.drawable.ic_audio_empty_icon);
            lbl_photo_video_empty.setText(R.string.no_audio);
        }
        invalidateOptionsMenu();
    }

    private void AudioFileBind() {
        for (File next : new FileUtils().FindFiles(new String[]{"mp3", "wav", "m4a"})) {
            AudioEnt audioEnt = new AudioEnt();
            audioEnt.SetFile(next);
            audioEnt.setAudioName(next.getName());
            audioEnt.setOriginalAudioLocation(next.getAbsolutePath());
            audioEnt.setPlayListId(Common.PlayListId);
            audioEnt.SetFileCheck(false);
            audioEnt.SetFileImage(null);
            audioImportEntList.add(audioEnt);
            ImportAlbumEnt importAlbumEnt = new ImportAlbumEnt();
            if (spinnerValues.size() <= 0 || !spinnerValues.contains(next.getParent())) {
                importAlbumEnt.SetAlbumName(next.getParent());
                importAlbumEnt.Set_Activity_type(Common.ActivityType.Music.toString());
                importAlbumEnts.add(importAlbumEnt);
                spinnerValues.add(next.getParent());
            }
        }
    }

    public void GetFolders() {
        folderadapter = new AudioFoldersImportAdapter(AudiosImportActivity.this, importAlbumEnts, false, this);
        album_import_ListView.setAdapter(folderadapter);
        if (importAlbumEnts.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            document_empty_icon.setBackgroundResource(R.drawable.ic_audio_empty_icon);
            lbl_photo_video_empty.setText(R.string.no_audio);
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

    @SuppressLint("SetTextI18n")
    public void OnImportClick() {
        final StorageOptionSharedPreferences GetObject = StorageOptionSharedPreferences.GetObject(this);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectaudiomsg_import, Toast.LENGTH_SHORT).show();
        } else if (Common.GetFileSize(selectPath) < Common.GetTotalFree()) {
            int albumCheckCount = albumCheckCount();
            if (albumCheckCount >= 2) {
                final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.confirmation_message_box);
                dialog.setCancelable(true);

                TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
                textView.setText("Are you sure you want to import " + albumCheckCount + " folders? Importing may take time according to the size of your data.");

                dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
                    for (int i = 0; i < importAlbumEnts.size(); i++) {
                        importAlbumEnts.get(i).SetAlbumFileCheck(false);
                    }
                    folderadapter = new AudioFoldersImportAdapter(AudiosImportActivity.this, importAlbumEnts, false, this);
                    album_import_ListView.setAdapter(folderadapter);
                    folderadapter.notifyDataSetChanged();
                    dialog.dismiss();
                });
                dialog.show();

                dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
                    if (Build.VERSION.SDK_INT >= 23) {
                        Import();
                    } else if (GetObject.GetSDCardUri().length() > 0) {
                        Import();
                    } else if (!GetObject.GetISDAlertshow()) {
                        LollipopSdCardPermissionDialog();
                    } else {
                        Import();
                    }
                });
                dialog.show();
            } else if (Build.VERSION.SDK_INT >= 23) {
                Import();
            } else if (GetObject.GetSDCardUri().length() > 0) {
                Import();
            } else if (!GetObject.GetISDAlertshow()) {
                LollipopSdCardPermissionDialog();
            } else {
                Import();
            }
        }
    }

    public void LollipopSdCardPermissionDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.sdcard_permission_alert_msgbox);
        dialog.setCancelable(true);

        ((CheckBox) dialog.findViewById(R.id.cbalertdialog)).setOnCheckedChangeListener((compoundButton, z) -> StorageOptionSharedPreferences.GetObject(AudiosImportActivity.this).SetISDAlertshow(z));

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            dialog.dismiss();
            Import();
        });
        dialog.show();
    }

    public void Import() {
        SelectedCount();
        ShowImportProgress();
        Common.IsWorkInProgress = true;
        new Thread() { // from class: com.calculator.vaultlocker.Activity.AudiosImportActivity.10
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    ImportAudio();
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

    public void ImportAudio() {
        if (isAlbumClick) {
            ImportOnlyAudioSDCard();
        } else {
            importFolder();
        }
    }

    public void importFolder() {
        if (importAlbumEnts.size() > 0) {
            int i = 0;
            for (int i2 = 0; i2 < importAlbumEnts.size(); i2++) {
                if (importAlbumEnts.get(i2).GetAlbumFileCheck()) {
                    File file = new File(importAlbumEnts.get(i2).GetAlbumName());
                    File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + file.getName());
                    folderName = file.getName();
                    if (file2.exists()) {
                        int i3 = 1;
                        while (i3 < 100) {
                            folderName = file.getName() + "(" + i3 + ")";
                            String sb = StorageOptionsCommon.STORAGEPATH +
                                    StorageOptionsCommon.AUDIOS +
                                    folderName;
                            File file3 = new File(sb);
                            if (!file3.exists()) {
                                i3 = 100;
                            }
                            i3++;
                            file2 = file3;
                        }
                    }
                    AddFolderToDatabase(folderName);
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
                    audioPlayListDAL.OpenRead();
                    int GetLastPlayListId = audioPlayListDAL.GetLastPlayListId();
                    folderId = GetLastPlayListId;
                    Common.FolderId = GetLastPlayListId;
                    audioPlayListDAL.close();
                    ImportAlbumsAudioSDCard(i);
                    i++;
                }
            }
        }
    }

    public void ImportAlbumsAudioSDCard(int i) {
        String str;
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        List<AudioEnt> list = audioEntListShowList.get(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).GetFileCheck()) {
                File file = new File(list.get(i2).getOriginalAudioLocation());
                File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + folderName);
                try {
                    File file3 = new File(file2.getAbsolutePath() + "/" + Utilities.ChangeFileExtention(file.getName()));
                    if (file.exists()) {
                        File parentFile = file3.getParentFile();
                        assert parentFile != null;
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        file3.createNewFile();
                        Flaes.encryptUsingCipherStream_AES128(file, file3);
                        str = file3.getAbsolutePath();
                    } else {
                        str = "";
                    }
                    if (file.exists() && file3.exists()) {
                        file.delete();
                    }
                    if (str.length() > 0) {
                        AddAudioToDatabase(FileName(list.get(i2).getOriginalAudioLocation()), list.get(i2).getOriginalAudioLocation(), str);
                    }
                    if (Build.VERSION.SDK_INT < 23 && StorageOptionSharedPreferences.GetObject(this).GetSDCardUri().length() > 0) {
                        Utilities.DeleteSDcardImageLollipop(this, file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    IsExceptionInImportPhotos = true;
                    e.printStackTrace();
                }
            }
        }
    }

    public void ImportOnlyAudioSDCard() {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        int size = audioImportEntListShow.size();
        for (int i = 0; i < size; i++) {
            if (audioImportEntListShow.get(i).GetFileCheck()) {
                File file = new File(audioImportEntListShow.get(i).getOriginalAudioLocation());
                File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + folderName);
                String str = "";
                try {
                    File file3 = new File(file2.getAbsolutePath() + "/" + Utilities.ChangeFileExtention(file.getName()));
                    if (file.exists()) {
                        File parentFile = file3.getParentFile();
                        assert parentFile != null;
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        file3.createNewFile();
                        Flaes.encryptUsingCipherStream_AES128(file, file3);
                        str = file3.getAbsolutePath();
                        if (file.exists() && file3.exists()) {
                            file.delete();
                        }
                    }
                    if (Build.VERSION.SDK_INT < 23 && StorageOptionSharedPreferences.GetObject(this).GetSDCardUri().length() > 0) {
                        Utilities.DeleteSDcardImageLollipop(this, file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    IsExceptionInImportPhotos = true;
                    e.printStackTrace();
                }
                if (str.length() > 0) {
                    String[] split = str.split("/");
                    AddAudioToDatabase(Utilities.ChangeFileExtentionToOrignal(split[split.length - 1]), audioImportEntListShow.get(i).getOriginalAudioLocation(), str);
                }
            }
        }
        Common.SelectedCount = 0;
        Common.IsSelectAll = false;
    }

    public void AddAudioToDatabase(String str, String str2, String str3) {
        AudioEnt audioEnt = new AudioEnt();
        audioEnt.setAudioName(str);
        audioEnt.setFolderLockAudioLocation(str3);
        audioEnt.setOriginalAudioLocation(str2);
        audioEnt.setPlayListId(folderId);
        AudioDAL audioDAL = new AudioDAL(this);
        try {
            audioDAL.OpenWrite();
            audioDAL.AddAudio(audioEnt, str3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            audioDAL.close();
            throw th;
        }
        audioDAL.close();
    }

    public void AddFolderToDatabase(String str) {
        AudioPlayListEnt audioPlayListEnt = new AudioPlayListEnt();
        audioPlayListEnt.setPlayListName(str);
        audioPlayListEnt.setPlayListLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + str);
        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
        try {
            audioPlayListDAL.OpenWrite();
            audioPlayListDAL.AddAudioPlayList(audioPlayListEnt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            audioPlayListDAL.close();
            throw th;
        }
        audioPlayListDAL.close();
    }

    private boolean IsFileCheck() {
        for (int i = 0; i < importAlbumEnts.size(); i++) {
            if (importAlbumEnts.get(i).GetAlbumFileCheck()) {
                audioImportEntListShow = new ArrayList<>();
                for (AudioEnt next : audioImportEntList) {
                    if (spinnerValues.get(i).equals(new File(next.getOriginalAudioLocation()).getParent())) {
                        audioImportEntListShow.add(next);
                    }
                    for (int i2 = 0; i2 < audioImportEntListShow.size(); i2++) {
                        audioImportEntListShow.get(i2).SetFileCheck(true);
                    }
                }
                audioEntListShowList.add(audioImportEntListShow);
            }
        }
        selectPath.clear();
        for (int i3 = 0; i3 < audioImportEntListShow.size(); i3++) {
            if (audioImportEntListShow.get(i3).GetFileCheck()) {
                selectPath.add(audioImportEntListShow.get(i3).getOriginalAudioLocation());
                return true;
            }
        }
        return false;
    }

    private void SelectedCount() {
        selectCount = 0;
        for (int i = 0; i < audioImportEntListShow.size(); i++) {
            if (audioImportEntListShow.get(i).GetFileCheck()) {
                selectCount++;
            }
        }
    }

    public void btnBackonClick(View view) {
        Back();
    }

    public void SelectAllAudios() {
        if (IsSelectAll) {
            for (int i = 0; i < audioImportEntListShow.size(); i++) {
                audioImportEntListShow.get(i).SetFileCheck(false);
            }
            IsSelectAll = false;
            Common.IsSelectAll = false;
            SelectedItemsCount(0);
            Common.SelectedCount = 0;
            btnSelectAll.setImageResource(R.drawable.ic_unselectallicon);
            return;
        }
        for (int i2 = 0; i2 < audioImportEntListShow.size(); i2++) {
            audioImportEntListShow.get(i2).SetFileCheck(true);
        }
        Common.SelectedCount = audioImportEntListShow.size();
        IsSelectAll = true;
        Common.IsSelectAll = true;
        btnSelectAll.setImageResource(R.drawable.ic_selectallicon);
    }

    public String FileName(String str) {
        for (int length = str.length() - 1; length > 0; length--) {
            if (str.charAt(length) == " /".charAt(1)) {
                return str.substring(length + 1);
            }
        }
        return "";
    }

    public void Back() {
        Common.SelectedCount = 0;
        if (isAlbumClick) {
            isAlbumClick = false;
            album_import_ListView.setVisibility(View.VISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            for (int i = 0; i < audioImportEntListShow.size(); i++) {
                audioImportEntListShow.get(i).SetFileCheck(false);
            }
            IsSelectAll = false;
            Common.IsSelectAll = false;
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, AudioActivity.class));
        finish();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Common.SelectedCount = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (SecurityLocksCommon.IsAppDeactive && !Common.IsWorkInProgress) {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Common.SelectedCount = 0;
        if (i == 4) {
            if (isAlbumClick) {
                isAlbumClick = false;
                album_import_ListView.setVisibility(View.VISIBLE);
                imagegrid.setVisibility(View.INVISIBLE);
                btnSelectAll.setVisibility(View.INVISIBLE);
                for (int i2 = 0; i2 < audioImportEntListShow.size(); i2++) {
                    audioImportEntListShow.get(i2).SetFileCheck(false);
                }
                IsSelectAll = false;
                Common.IsSelectAll = false;
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, AudioPlayListActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void SelectedItemsCount(int i) {
        selectedCount = Integer.toString(i);
        invalidateOptionsMenu();
    }
}
