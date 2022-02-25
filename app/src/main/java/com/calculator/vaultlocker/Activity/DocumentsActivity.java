package com.calculator.vaultlocker.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
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

import com.calculator.vaultlocker.BuildConfig;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.Adapter.AppDocumentsAdapter;
import com.calculator.vaultlocker.DB.DocumentDAL;
import com.calculator.vaultlocker.Model.DocumentFolder;
import com.calculator.vaultlocker.DB.DocumentFolderDAL;
import com.calculator.vaultlocker.Model.DocumentsEnt;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.Adapter.MoveAlbumAdapter;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.calculator.vaultlocker.utilities.Utilities;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DocumentsActivity extends BaseActivity {
    private final ArrayList<String> files = new ArrayList<>();
    public boolean isEditMode = false;
    public boolean IsSortingDropdown = false;
    public boolean IsSelectAll = false;
    public int fileCount = 0;
    public int selectCount;
    public int _SortBy = 1;
    private List<String> _folderNameArrayForMove = null;
    private List<DocumentsEnt> documentEntList;
    private String moveToFolderLocation;
    private String folderLocation;
    private ImageView file_empty_icon;
    private GridView imagegrid;
    private TextView lbl_file_empty;
    private DocumentDAL documentDAL;
    private SensorManager sensorManager;
    private ProgressDialog myProgressDialog;

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 2) {
                DocumentsActivity.this.hideProgress();
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(DocumentsActivity.this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(DocumentsActivity.this, R.string.Move_error, Toast.LENGTH_SHORT).show();
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(DocumentsActivity.this, R.string.Delete_error, Toast.LENGTH_SHORT).show();
                }
            } else if (message.what == 4) {
                Toast.makeText(DocumentsActivity.this, R.string.toast_share, Toast.LENGTH_LONG).show();
            } else if (message.what == 3) {
                if (Common.isUnHide) {
                    Common.isUnHide = false;
                    Toast.makeText(DocumentsActivity.this, R.string.toast_unhide, Toast.LENGTH_LONG).show();
                    DocumentsActivity.this.hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(DocumentsActivity.this, DocumentsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        DocumentsActivity.this.startActivity(intent);
                        DocumentsActivity.this.finish();
                    }
                } else if (Common.isDelete) {
                    Common.isDelete = false;
                    Toast.makeText(DocumentsActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    DocumentsActivity.this.hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent2 = new Intent(DocumentsActivity.this, DocumentsActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        DocumentsActivity.this.startActivity(intent2);
                        DocumentsActivity.this.finish();
                    }
                } else if (Common.isMove) {
                    Common.isMove = false;
                    Toast.makeText(DocumentsActivity.this, R.string.toast_move, Toast.LENGTH_SHORT).show();
                    DocumentsActivity.this.hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent3 = new Intent(DocumentsActivity.this, DocumentsActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        DocumentsActivity.this.startActivity(intent3);
                        DocumentsActivity.this.finish();
                    }
                }
            }
            super.handleMessage(message);
        }
    };

    private AppDocumentsAdapter appDocumentsAdapter;
    private LinearLayout ll_EditFiles;
    private LinearLayout ll_file_empty;
    private LinearLayout ll_file_grid;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_documents);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RelativeLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        LinearLayout ll_unhide_btn = findViewById(R.id.ll_unhide_btn);
        LinearLayout ll_move_btn = findViewById(R.id.ll_move_btn);
        LinearLayout ll_share_btn = findViewById(R.id.ll_share_btn);
        FloatingActionButton fabImpGallery = findViewById(R.id.btn_impGallery);

        this.ll_EditFiles = findViewById(R.id.ll_EditPhotos);
        this.imagegrid = findViewById(R.id.customGalleryGrid);
        this.ll_file_empty = findViewById(R.id.ll_photo_video_empty);
        this.ll_file_grid = findViewById(R.id.ll_photo_video_grid);
        this.file_empty_icon = findViewById(R.id.photo_video_empty_icon);
        this.lbl_file_empty = findViewById(R.id.lbl_photo_video_empty);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        toolbar.setNavigationOnClickListener(view -> DocumentsActivity.this.Back());

        SecurityLocksCommon.IsAppDeactive = true;
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.ll_file_grid.setVisibility(View.VISIBLE);
        this.ll_file_empty.setVisibility(View.INVISIBLE);

        fabImpGallery.setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            Common.IsCameFromPhotoAlbum = false;
            DocumentsActivity.this.startActivity(new Intent(DocumentsActivity.this, DocumentsImportActivity.class));
            DocumentsActivity.this.finish();
        });

        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
        documentFolderDAL.OpenRead();
        DocumentFolder GetFolderById = documentFolderDAL.GetFolderById(Integer.toString(Common.FolderId));
        this._SortBy = documentFolderDAL.GetSortByFolderId(Common.FolderId);
        documentFolderDAL.close();

        String folderName = GetFolderById.getFolderName();
        Common.DocumentFolderName = folderName;
        this.folderLocation = GetFolderById.getFolderLocation();
        Objects.requireNonNull(getSupportActionBar()).setTitle(folderName);


        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (DocumentsActivity.this.IsSortingDropdown) {
                DocumentsActivity.this.IsSortingDropdown = false;
            }
            return false;
        });

        this.imagegrid.setOnItemClickListener((adapterView, view, i, j) -> {
            if (!DocumentsActivity.this.isEditMode) {
                int id = DocumentsActivity.this.documentEntList.get(i).getId();
                DocumentDAL documentDAL = new DocumentDAL(DocumentsActivity.this);
                documentDAL.OpenRead();
                String folderLockDocumentLocation = documentDAL.GetDocumentById(Integer.toString(id)).getFolderLockDocumentLocation();
                documentDAL.close();
                String FileName = Utilities.FileName(folderLockDocumentLocation);
                if (FileName.contains("#")) {
                    FileName = Utilities.ChangeFileExtentionToOrignal(FileName);
                }
                File file = new File(folderLockDocumentLocation);
                File file2 = new File(file.getParent() + "/" + FileName);
                file.renameTo(file2);
                DocumentsActivity.this.CopyTempFile(file2.getAbsolutePath());
            }
        });

        this.imagegrid.setOnItemLongClickListener((adapterView, view, i, j) -> {
            Common.PhotoThumbnailCurrentPosition = DocumentsActivity.this.imagegrid.getFirstVisiblePosition();
            isEditMode = true;
            DocumentsActivity.this.ll_EditFiles.setVisibility(View.VISIBLE);
            DocumentsActivity.this.invalidateOptionsMenu();
            DocumentsActivity.this.documentEntList.get(i).SetFileCheck(true);
            appDocumentsAdapter = new AppDocumentsAdapter(DocumentsActivity.this, 1, documentEntList, true);
            DocumentsActivity.this.imagegrid.setAdapter(DocumentsActivity.this.appDocumentsAdapter);
            DocumentsActivity.this.appDocumentsAdapter.notifyDataSetChanged();
            if (Common.PhotoThumbnailCurrentPosition != 0) {
                DocumentsActivity.this.imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
            }
            return true;
        });

        ll_delete_btn.setOnClickListener(view -> DocumentsActivity.this.DeleteFiles());

        ll_unhide_btn.setOnClickListener(view -> DocumentsActivity.this.UnhideFiles());

        ll_move_btn.setOnClickListener(view -> DocumentsActivity.this.MoveFiles());

        ll_share_btn.setOnClickListener(view -> {
            if (!DocumentsActivity.this.IsFileCheck()) {
                Toast.makeText(DocumentsActivity.this, R.string.toast_unselectphotomsg_share, Toast.LENGTH_SHORT).show();
            } else {
                DocumentsActivity.this.ShareDocuments();
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
            LoadFilesFromDB(this._SortBy);
            if (Common.PhotoThumbnailCurrentPosition == 0) {
                this.imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
                Common.PhotoThumbnailCurrentPosition = 0;
            }
        }
    }

    private void SetcheckFlase() {
        for (int i = 0; i < this.documentEntList.size(); i++) {
            this.documentEntList.get(i).SetFileCheck(false);
        }
        appDocumentsAdapter = new AppDocumentsAdapter(this, 1, this.documentEntList, false);
        this.imagegrid.setAdapter(appDocumentsAdapter);
        this.appDocumentsAdapter.notifyDataSetChanged();
        if (Common.PhotoThumbnailCurrentPosition != 0) {
            this.imagegrid.setSelection(Common.PhotoThumbnailCurrentPosition);
            Common.PhotoThumbnailCurrentPosition = 0;
        }
    }

    public void btnBackonClick(View view) {
        Back();
    }

    public void Back() {
        if (this.isEditMode) {
            SetcheckFlase();
            this.isEditMode = false;
            this.IsSortingDropdown = false;
            this.ll_EditFiles.setVisibility(View.GONE);
            this.IsSelectAll = false;
            invalidateOptionsMenu();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        Common.FolderId = 0;
        Common.DocumentFolderName = StorageOptionsCommon.DOCUMENTS_DEFAULT_ALBUM;
        startActivity(new Intent(this, DocumentsFolderActivity.class));
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void UnhideFiles() {
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
            textView.setText("Are you sure you want to restore (" + this.selectCount + ") document(s)?");

            dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
                DocumentsActivity.this.showUnhideProgress();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            dialog.dismiss();
                            Common.isUnHide = true;
                            DocumentsActivity.this.Unhide();
                            Common.IsWorkInProgress = true;
                            Message message = new Message();
                            message.what = 3;
                            DocumentsActivity.this.handle.sendMessage(message);
                            Common.IsWorkInProgress = false;
                        } catch (Exception unused) {
                            Message message2 = new Message();
                            message2.what = 3;
                            DocumentsActivity.this.handle.sendMessage(message2);
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
        for (int i = 0; i < this.documentEntList.size(); i++) {
            if (this.documentEntList.get(i).GetFileCheck()) {
                if (Utilities.NSUnHideFile(this, this.documentEntList.get(i).getFolderLockDocumentLocation(), this.documentEntList.get(i).getOriginalDocumentLocation())) {
                    DeleteFromDatabase(this.documentEntList.get(i).getId());
                } else {
                    Toast.makeText(this, R.string.Unhide_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void DeleteFiles() {
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectphotomsg_delete, Toast.LENGTH_SHORT).show();
            return;
        }
        SelectedCount();
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(true);

        TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
        textView.setText("Are you sure you want to delete (" + this.selectCount + ") document(s)?");

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            DocumentsActivity.this.showDeleteProgress();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Common.isDelete = true;
                        dialog.dismiss();
                        DocumentsActivity.this.Delete();
                        Common.IsWorkInProgress = true;
                        Message message = new Message();
                        message.what = 3;
                        DocumentsActivity.this.handle.sendMessage(message);
                        Common.IsWorkInProgress = false;
                    } catch (Exception unused) {
                        Message message2 = new Message();
                        message2.what = 3;
                        DocumentsActivity.this.handle.sendMessage(message2);
                    }
                }
            }.start();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void Delete() {
        for (int i = 0; i < this.documentEntList.size(); i++) {
            if (this.documentEntList.get(i).GetFileCheck()) {
                new File(this.documentEntList.get(i).getFolderLockDocumentLocation()).delete();
                DeleteFromDatabase(this.documentEntList.get(i).getId());
            }
        }
    }

    public void DeleteFromDatabase(int i) {
        documentDAL = new DocumentDAL(this);
        try {
            documentDAL.OpenWrite();
            this.documentDAL.DeleteDocumentById(i);
            if (documentDAL == null) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (documentDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (documentDAL != null) {
                documentDAL.close();
            }
            throw th;
        }
        documentDAL.close();
    }

    public void SelectedCount() {
        this.files.clear();
        this.selectCount = 0;
        for (int i = 0; i < this.documentEntList.size(); i++) {
            if (this.documentEntList.get(i).GetFileCheck()) {
                this.files.add(this.documentEntList.get(i).getFolderLockDocumentLocation());
                this.selectCount++;
            }
        }
    }

    public boolean IsFileCheck() {
        for (int i = 0; i < this.documentEntList.size(); i++) {
            if (this.documentEntList.get(i).GetFileCheck()) {
                return true;
            }
        }
        return false;
    }

    public void MoveFiles() {
        documentDAL = new DocumentDAL(this);
        documentDAL.OpenWrite();
        String[] _folderNameArray = this.documentDAL.GetFolderNames(Common.FolderId);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectdocumentmsg_move, Toast.LENGTH_SHORT).show();
        } else if (_folderNameArray.length > 0) {
            GetFolderNameFromDB();
        } else {
            Toast.makeText(this, R.string.toast_OneFolder, Toast.LENGTH_SHORT).show();
        }
    }

    public void Move(String str, String str2, String str3) {
        String str4;
        DocumentFolder GetAlbum = GetAlbum(str3);
        for (int i = 0; i < this.documentEntList.size(); i++) {
            if (this.documentEntList.get(i).GetFileCheck()) {
                if (this.documentEntList.get(i).getDocumentName().contains("#")) {
                    str4 = this.documentEntList.get(i).getDocumentName();
                } else {
                    str4 = Utilities.ChangeFileExtention(this.documentEntList.get(i).getDocumentName());
                }
                String str5 = str2 + "/" + str4;
                try {
                    if (Utilities.MoveFileWithinDirectories(this.documentEntList.get(i).getFolderLockDocumentLocation(), str5)) {
                        UpdateFileLocationInDatabase(this.documentEntList.get(i), str5, GetAlbum.getId());
                        Common.FolderId = GetAlbum.getId();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void UpdateFileLocationInDatabase(DocumentsEnt documentsEnt, String str, int i) {
        documentsEnt.setFolderLockDocumentLocation(str);
        documentsEnt.setFolderId(i);
        try {
            DocumentDAL documentDAL2 = new DocumentDAL(this);
            documentDAL2.OpenWrite();
            documentDAL2.UpdateDocumentLocation(documentsEnt);
            if (documentDAL == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (documentDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (documentDAL != null) {
                documentDAL.close();
            }
            throw th;
        }
        documentDAL.close();
    }

    public DocumentFolder GetAlbum(String str) {
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
        try {
            documentFolderDAL.OpenRead();
            return documentFolderDAL.GetFolder(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Throwable th) {
            documentFolderDAL.close();
            throw th;
        }
    }

    private void GetFolderNameFromDB() {
        documentDAL = new DocumentDAL(this);
        try {
            documentDAL.OpenWrite();
            this._folderNameArrayForMove = this.documentDAL.GetMoveFolderNames(Common.FolderId);
            MovePhotoDialog();
            if (documentDAL == null) {
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (documentDAL == null) {
                return;
            }
        } catch (Throwable th) {
            if (documentDAL != null) {
                documentDAL.close();
            }
            throw th;
        }
        documentDAL.close();
    }

    public void MovePhotoDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.move_customlistview);
        ListView listView = dialog.findViewById(R.id.ListViewfolderslist);
        listView.setAdapter(new MoveAlbumAdapter(this, 17367043, this._folderNameArrayForMove, R.drawable.ic_notesfolder_thumb_icon));

        listView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (DocumentsActivity.this._folderNameArrayForMove != null) {
                DocumentsActivity.this.SelectedCount();
                DocumentsActivity.this.showMoveProgress();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Common.isMove = true;
                            dialog.dismiss();
                            moveToFolderLocation = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + DocumentsActivity.this._folderNameArrayForMove.get(i);
                            DocumentsActivity.this.Move(DocumentsActivity.this.folderLocation, DocumentsActivity.this.moveToFolderLocation, DocumentsActivity.this._folderNameArrayForMove.get(i));
                            Common.IsWorkInProgress = true;
                            Message message = new Message();
                            message.what = 3;
                            DocumentsActivity.this.handle.sendMessage(message);
                            Common.IsWorkInProgress = false;
                        } catch (Exception unused) {
                            Message message2 = new Message();
                            message2.what = 3;
                            DocumentsActivity.this.handle.sendMessage(message2);
                        }
                    }
                }.start();
            }
        });
        dialog.show();
    }

    public void ShareDocuments() {
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
                    for (ResolveInfo resolveInfo : DocumentsActivity.this.getPackageManager().queryIntentActivities(intent, 0)) {
                        String str = resolveInfo.activityInfo.packageName;
                        if (!str.equals(getPackageName()) && !str.equals("com.dropbox.android") && !str.equals("com.facebook.katana")) {
                            Intent intent2 = new Intent("android.intent.action.SEND_MULTIPLE");
                            intent2.setType("image/*");
                            intent2.setPackage(str);
                            arrayList.add(intent2);
                            String str2 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS;
                            ArrayList<String> arrayList2 = new ArrayList<>();
                            ArrayList<Uri> arrayList3 = new ArrayList<>();
                            for (int i = 0; i < DocumentsActivity.this.documentEntList.size(); i++) {
                                if (DocumentsActivity.this.documentEntList.get(i).GetFileCheck()) {
                                    try {
                                        str2 = Utilities.CopyTemporaryFile(DocumentsActivity.this, DocumentsActivity.this.documentEntList.get(i).getFolderLockDocumentLocation(), str2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    arrayList2.add(str2);
                                    arrayList3.add(FileProvider.getUriForFile(DocumentsActivity.this, BuildConfig.APPLICATION_ID, new File(str2)));
                                }
                            }
                            intent2.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList3);
                        }
                    }
                    Intent createChooser = Intent.createChooser(arrayList.remove(0), "Share Via");
                    createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", arrayList.toArray(new Parcelable[0]));
                    DocumentsActivity.this.startActivity(createChooser);
                    Message message = new Message();
                    message.what = 4;
                    DocumentsActivity.this.handle.sendMessage(message);
                } catch (Exception unused) {
                    Message message2 = new Message();
                    message2.what = 4;
                    DocumentsActivity.this.handle.sendMessage(message2);
                }
            }
        }.start();
    }

    public void LoadFilesFromDB(int i) {
        this.documentEntList = new ArrayList<>();
        DocumentDAL documentDAL = new DocumentDAL(this);
        documentDAL.OpenRead();
        this.fileCount = documentDAL.GetDocumentCountByFolderId(Common.FolderId);
        this.documentEntList = documentDAL.GetDocuments(Common.FolderId, i);
        documentDAL.close();
        appDocumentsAdapter = new AppDocumentsAdapter(this, 1, this.documentEntList, false);
        this.imagegrid.setAdapter(appDocumentsAdapter);
        this.appDocumentsAdapter.notifyDataSetChanged();
        if (this.documentEntList.size() < 1) {
            this.ll_file_grid.setVisibility(View.INVISIBLE);
            this.ll_file_empty.setVisibility(View.VISIBLE);
            this.file_empty_icon.setBackgroundResource(R.drawable.ic_documents_empty_icon);
            this.lbl_file_empty.setText(R.string.lbl_No_Documents);
            return;
        }
        this.ll_file_grid.setVisibility(View.VISIBLE);
        this.ll_file_empty.setVisibility(View.INVISIBLE);
    }

    public void CopyTempFile(String str) {
        File file = new File(str);
        try {
            Utilities.NSDecryption(file);
            SecurityLocksCommon.IsAppDeactive = false;
            String guessContentTypeFromName = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file), guessContentTypeFromName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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
        new Thread() {
            @Override
            public void run() {
                try {
                    Utilities.changeFileExtention(StorageOptionsCommon.DOCUMENTS);
                } catch (Exception unused) {
                    Log.v("Login Activity", "error in changeVideosExtention method");
                }
            }
        }.start();

        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        SetcheckFlase();
        this.IsSortingDropdown = false;
        this.isEditMode = false;
        this.ll_EditFiles.setVisibility(View.GONE);
        this.IsSelectAll = false;
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread() {
            @Override
            public void run() {
                try {
                    Utilities.changeFileExtention(StorageOptionsCommon.DOCUMENTS);
                } catch (Exception unused) {
                    Log.v("Login Activity", "error in changeVideosExtention method");
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            Common.isOpenCameraorGalleryFromApp = false;
            if (this.isEditMode) {
                SetcheckFlase();
                this.IsSortingDropdown = false;
                this.isEditMode = false;
                this.ll_EditFiles.setVisibility(View.GONE);
                this.IsSelectAll = false;
                invalidateOptionsMenu();
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = 0;
            Common.DocumentFolderName = StorageOptionsCommon.DOCUMENTS_DEFAULT_ALBUM;
            startActivity(new Intent(this, DocumentsFolderActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
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

    public enum SortBy {
        Time,
        Name,
        Size
    }
}
