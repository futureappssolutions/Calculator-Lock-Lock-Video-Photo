package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportAlbumEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.DocumentsFolderGalleryMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DocumentDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.DocumentFolder;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DocumentFolderDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.DocumentSystemFileAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.DocumentsEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.FoldersImportAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.FileUtils;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.OnItemClickListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DocumentsImportActivity extends Activity implements AccelerometerListener, SensorEventListener, OnItemClickListener {
    private final ArrayList<DocumentsEnt> fileImportEntList = new ArrayList<>();
    private final ArrayList<String> selectPath = new ArrayList<>();
    private final ArrayList<String> spinnerValues = new ArrayList<>();
    private final List<List<DocumentsEnt>> fileImportEntListShowList = new ArrayList<>();
    private final List<ImportAlbumEnt> importAlbumEnts = new ArrayList<>();
    private ArrayList<DocumentsEnt> fileImportEntListShow = new ArrayList<>();

    private FoldersImportAdapter adapter;
    private DocumentSystemFileAdapter filesAdapter;
    private RecyclerView album_import_ListView;
    private GridView imagegrid;
    private AppCompatImageView btnSelectAll;
    private TextView lbl_import_photo_album_topbaar;
    private TextView lbl_photo_video_empty;
    private AppCompatButton btnImport;
    private LinearLayout ll_photo_video_empty;
    private ImageView photo_video_empty_icon;
    private SensorManager sensorManager;
    private ProgressDialog myProgressDialog = null;
    private String folderName;

    private boolean IsExceptionInImportPhotos = false;
    private boolean isAlbumClick = false;
    private boolean IsSelectAll = false;
    private int folderId;
    private int selectCount;

    public Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Intent intent;
            if (message.what == 1) {
                hideProgress();
                if (fileImportEntListShow.size() > 0) {
                    filesAdapter = new DocumentSystemFileAdapter(DocumentsImportActivity.this, 1, fileImportEntListShow);
                    imagegrid.setAdapter(filesAdapter);
                } else {
                    btnSelectAll.setEnabled(false);
                    btnImport.setEnabled(false);
                }
            } else if (message.what == 3) {
                if (Common.IsImporting) {
                    RefershGalleryforKitkat();
                    Common.IsImporting = false;
                    if (IsExceptionInImportPhotos) {
                        IsExceptionInImportPhotos = false;
                    } else {
                        Toast.makeText(DocumentsImportActivity.this, selectCount + " document(s) imported successfully", Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                    if (!Common.IsWorkInProgress) {
                        SecurityLocksCommon.IsAppDeactive = false;
                        if (isAlbumClick) {
                            intent = new Intent(DocumentsImportActivity.this, DocumentsActivity.class);
                        } else {
                            intent = new Intent(DocumentsImportActivity.this, DocumentsFolderActivity.class);
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

    private void ShowImportProgress() {
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
        Advertisement.showBannerAds(DocumentsImportActivity.this, ll_banner);

        SecurityLocksCommon.IsAppDeactive = true;
        Common.IsWorkInProgress = false;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        ProgressBar progress = findViewById(R.id.prbLoading);

        album_import_ListView = findViewById(R.id.album_import_ListView);
        imagegrid = findViewById(R.id.customGalleryGrid);
        lbl_import_photo_album_topbaar = findViewById(R.id.lbl_import_photo_album_topbaar);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnImport = findViewById(R.id.btnImport);
        ll_photo_video_empty = findViewById(R.id.ll_photo_video_empty);
        photo_video_empty_icon = findViewById(R.id.photo_video_empty_icon);
        lbl_photo_video_empty = findViewById(R.id.lbl_photo_video_empty);

        lbl_import_photo_album_topbaar.setText(R.string.lbl_import_photo_album_select_folder_topbaar);

        folderName = null;
        folderId = Common.FolderId;

        album_import_ListView.setLayoutManager(new LinearLayoutManager(DocumentsImportActivity.this, RecyclerView.VERTICAL, false));

        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
        documentFolderDAL.OpenRead();
        DocumentFolder GetFolderById = documentFolderDAL.GetFolderById(Integer.toString(Common.FolderId));
        documentFolderDAL.close();
        folderName = GetFolderById.getFolderName();
        Log.e("foldername", "" + folderName);

        DocumentFileBind();
        GetFolders();
        progress.setVisibility(View.GONE);

        for (DocumentsEnt next : fileImportEntList) {
            if (spinnerValues.get(0).contains(Objects.requireNonNull(new File(next.getOriginalDocumentLocation()).getParent()))) {
                fileImportEntListShow.add(next);
            }
        }

        btnImport.setOnClickListener(view -> {
            Advertisement.getInstance((DocumentsImportActivity.this)).showFull((DocumentsImportActivity.this), () -> {
                OnImportClick();
            });
        });


        filesAdapter = new DocumentSystemFileAdapter(DocumentsImportActivity.this, 1, fileImportEntListShow);
        imagegrid.setAdapter(filesAdapter);
    }

    @Override
    public void onItemClick(int position) {
        isAlbumClick = true;
        lbl_import_photo_album_topbaar.setText(R.string.lbl_import_doc_album_select_doc_topbaar);
        album_import_ListView.setVisibility(View.INVISIBLE);
        imagegrid.setVisibility(View.VISIBLE);
        btnSelectAll.setVisibility(View.VISIBLE);
        adapter = new FoldersImportAdapter(DocumentsImportActivity.this, importAlbumEnts, false, this);
        album_import_ListView.setAdapter(adapter);
        fileImportEntListShow.clear();
        for (DocumentsEnt next2 : fileImportEntList) {
            if (spinnerValues.get(position).equals(new File(next2.getOriginalDocumentLocation()).getParent())) {
                next2.GetFileCheck();
                fileImportEntListShow.add(next2);
            }
        }

        filesAdapter = new DocumentSystemFileAdapter(DocumentsImportActivity.this, 1, fileImportEntListShow);
        imagegrid.setAdapter(filesAdapter);
        filesAdapter.notifyDataSetChanged();
        if (fileImportEntListShow.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            photo_video_empty_icon.setBackgroundResource(R.drawable.ic_video_empty_icon);
            lbl_photo_video_empty.setText(R.string.no_docs);
        }
    }

    private void DocumentFileBind() {
        for (File next : new FileUtils().FindFiles(new String[]{"doc", "pdf", "txt", "xlsx", "docx", "ppt", "pptx", "xls", "csv", "dbk", "dot", "dotx", "gdoc", "pdax", "pda", "rtf", "rpt", "uoml", "uof", "stw", "xps", "wrd", "wpt", "wps", "epub"})) {
            DocumentsEnt documentsEnt = new DocumentsEnt();
            documentsEnt.SetFile(next);
            documentsEnt.setDocumentName(next.getName());
            documentsEnt.setOriginalDocumentLocation(next.getAbsolutePath());
            documentsEnt.SetFileCheck(false);
            fileImportEntList.add(documentsEnt);
            ImportAlbumEnt importAlbumEnt = new ImportAlbumEnt();
            if (spinnerValues.size() <= 0 || !spinnerValues.contains(next.getParent())) {
                importAlbumEnt.SetAlbumName(next.getParent());
                importAlbumEnts.add(importAlbumEnt);
                spinnerValues.add(next.getParent());
            }
        }
        if (fileImportEntList.size() <= 0) {
            btnSelectAll.setEnabled(false);
            btnImport.setEnabled(false);
        }
    }

    public void GetFolders() {
        adapter = new FoldersImportAdapter(DocumentsImportActivity.this, importAlbumEnts, false, this);
        album_import_ListView.setAdapter(adapter);
        if (importAlbumEnts.size() <= 0) {
            album_import_ListView.setVisibility(View.INVISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            ll_photo_video_empty.setVisibility(View.VISIBLE);
            photo_video_empty_icon.setBackgroundResource(R.drawable.ic_video_empty_icon);
            lbl_photo_video_empty.setText(R.string.no_docs);
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

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public void OnImportClick() {
        final StorageOptionSharedPreferences GetObject = StorageOptionSharedPreferences.GetObject(this);
        if (!IsFileCheck()) {
            Toast.makeText(this, R.string.toast_unselectdocmsg_import, Toast.LENGTH_SHORT).show();
        } else if (Common.GetFileSize(selectPath) < Common.GetTotalFree()) {
            int albumCheckCount = albumCheckCount();
            if (albumCheckCount >= 2) {
                final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.confirmation_message_box);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                TextView textView = dialog.findViewById(R.id.tvmessagedialogtitle);
                textView.setText("Are you sure you want to import " + albumCheckCount + " folders? Importing may take time according to the size of your data.");

                dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
                    if (Build.VERSION.SDK_INT >= 23) {
                        Import();
                    } else if (GetObject.GetSDCardUri().length() > 0) {
                        Import();
                    } else if (!GetObject.GetISDAlertshow()) {
                        final Dialog dialog2 = new Dialog(DocumentsImportActivity.this, R.style.FullHeightDialog);
                        dialog2.setContentView(R.layout.sdcard_permission_alert_msgbox);
                        dialog2.setCancelable(false);
                        dialog2.setCanceledOnTouchOutside(false);
                        ((TextView) dialog2.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.lblSdCardAlertMsgDocs);

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
                    adapter = new FoldersImportAdapter(DocumentsImportActivity.this, importAlbumEnts, false, this);
                    album_import_ListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
                ((TextView) dialog2.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.lblSdCardAlertMsgDocs);

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

    public void Import() {
        SelectedCount();
        ShowImportProgress();
        Common.IsWorkInProgress = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    ImportDocuments();
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

    public void ImportDocuments() {
        if (isAlbumClick) {
            ImportOnlyDocumentsSDCard();
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
                    File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + file.getName());
                    folderName = file.getName();
                    if (file2.exists()) {
                        int i3 = 1;
                        while (i3 < 100) {
                            folderName = file.getName() + "(" + i3 + ")";
                            String sb = StorageOptionsCommon.STORAGEPATH +
                                    StorageOptionsCommon.DOCUMENTS +
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
                    DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
                    documentFolderDAL.OpenRead();
                    int GetLastFolderId = documentFolderDAL.GetLastFolderId();
                    folderId = GetLastFolderId;
                    Common.FolderId = GetLastFolderId;
                    documentFolderDAL.close();
                    ImportAlbumsDocumentsSDCard(i);
                    i++;
                }
            }
        }
    }

    public void ImportAlbumsDocumentsSDCard(int i) {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        List<DocumentsEnt> list = fileImportEntListShowList.get(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).GetFileCheck()) {
                File file = new File(list.get(i2).getOriginalDocumentLocation());
                try {
                    String NSHideFile = Utilities.NSHideFile(this, file, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + folderName + "/"));
                    Utilities.NSEncryption(new File(NSHideFile));
                    if (NSHideFile.length() > 0) {
                        AddDocumentToDatabase(FileName(list.get(i2).getOriginalDocumentLocation()), list.get(i2).getOriginalDocumentLocation(), NSHideFile);
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

    public void ImportOnlyDocumentsSDCard() {
        Common.IsImporting = true;
        SecurityLocksCommon.IsAppDeactive = true;
        int size = fileImportEntListShow.size();
        for (int i = 0; i < size; i++) {
            if (fileImportEntListShow.get(i).GetFileCheck()) {
                File file = new File(fileImportEntListShow.get(i).getOriginalDocumentLocation());
                try {
                    String NSHideFile = Utilities.NSHideFile(this, file, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + folderName + "/"));
                    Utilities.NSEncryption(new File(NSHideFile));
                    if (NSHideFile.length() > 0) {
                        AddDocumentToDatabase(FileName(fileImportEntListShow.get(i).getOriginalDocumentLocation()), fileImportEntListShow.get(i).getOriginalDocumentLocation(), NSHideFile);
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

    public void AddDocumentToDatabase(String str, String str2, String str3) {
        DocumentsEnt documentsEnt = new DocumentsEnt();
        documentsEnt.setDocumentName(str);
        documentsEnt.setFolderLockDocumentLocation(str3);
        documentsEnt.setOriginalDocumentLocation(str2);
        documentsEnt.setFolderId(folderId);
        DocumentDAL documentDAL = new DocumentDAL(this);
        try {
            documentDAL.OpenWrite();
            documentDAL.AddDocuments(documentsEnt, str3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentDAL.close();
            throw th;
        }
        documentDAL.close();
    }

    public void AddFolderToDatabase(String str) {
        DocumentFolder documentFolder = new DocumentFolder();
        documentFolder.setFolderName(str);
        documentFolder.setFolderLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + str);
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
        try {
            documentFolderDAL.OpenWrite();
            documentFolderDAL.AddDocumentFolder(documentFolder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentFolderDAL.close();
            throw th;
        }
        documentFolderDAL.close();
    }

    private boolean IsFileCheck() {
        for (int i = 0; i < importAlbumEnts.size(); i++) {
            if (importAlbumEnts.get(i).GetAlbumFileCheck()) {
                fileImportEntListShow = new ArrayList<>();
                for (DocumentsEnt next : fileImportEntList) {
                    if (spinnerValues.get(i).equals(new File(next.getOriginalDocumentLocation()).getParent())) {
                        fileImportEntListShow.add(next);
                    }
                    for (int i2 = 0; i2 < fileImportEntListShow.size(); i2++) {
                        fileImportEntListShow.get(i2).SetFileCheck(true);
                    }
                }
                fileImportEntListShowList.add(fileImportEntListShow);
            }
        }
        selectPath.clear();
        for (int i3 = 0; i3 < fileImportEntListShow.size(); i3++) {
            if (fileImportEntListShow.get(i3).GetFileCheck()) {
                selectPath.add(fileImportEntListShow.get(i3).getOriginalDocumentLocation());
                return true;
            }
        }
        return false;
    }

    private void SelectedCount() {
        selectCount = 0;
        for (int i = 0; i < fileImportEntListShow.size(); i++) {
            if (fileImportEntListShow.get(i).GetFileCheck()) {
                selectCount++;
            }
        }
    }

    public void btnSelectAllonClick(View view) {
        SelectAllPhotos();
        filesAdapter = new DocumentSystemFileAdapter(this, 1, fileImportEntListShow);
        imagegrid.setAdapter(filesAdapter);
        filesAdapter.notifyDataSetChanged();
    }

    public void btnBackonClick(View view) {
        Back();
    }

    private void SelectAllPhotos() {
        if (IsSelectAll) {
            for (int i = 0; i < fileImportEntListShow.size(); i++) {
                fileImportEntListShow.get(i).SetFileCheck(false);
            }
            IsSelectAll = false;
            btnSelectAll.setImageResource(R.drawable.ic_unselectallicon);
            return;
        }
        for (int i2 = 0; i2 < fileImportEntListShow.size(); i2++) {
            fileImportEntListShow.get(i2).SetFileCheck(true);
        }
        IsSelectAll = true;
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

    @SuppressLint("SetTextI18n")
    public void Back() {
        if (isAlbumClick) {
            isAlbumClick = false;
            lbl_import_photo_album_topbaar.setText("Import Folders");
            album_import_ListView.setVisibility(View.VISIBLE);
            imagegrid.setVisibility(View.INVISIBLE);
            btnSelectAll.setVisibility(View.INVISIBLE);
            for (int i = 0; i < fileImportEntListShow.size(); i++) {
                fileImportEntListShow.get(i).SetFileCheck(false);
            }
            IsSelectAll = false;
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, DocumentsActivity.class));
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
    public void onConfigurationChanged(Configuration configuration) {
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
        if (i == 4) {
            if (isAlbumClick) {
                isAlbumClick = false;
                lbl_import_photo_album_topbaar.setText("Import Albums");
                album_import_ListView.setVisibility(View.VISIBLE);
                imagegrid.setVisibility(View.INVISIBLE);
                btnSelectAll.setVisibility(View.INVISIBLE);
                for (int i2 = 0; i2 < fileImportEntListShow.size(); i2++) {
                    fileImportEntListShow.get(i2).SetFileCheck(false);
                }
                IsSelectAll = false;
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, DocumentsActivity.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
