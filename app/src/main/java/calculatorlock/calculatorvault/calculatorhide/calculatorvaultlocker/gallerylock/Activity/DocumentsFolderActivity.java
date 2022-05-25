package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.AppDocumentsAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DocumentDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.DocumentFolder;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DocumentFolderDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.DocumentsEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.DocumentsFolderAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.DocumentsFolderGalleryMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.AppSettingsSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class DocumentsFolderActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    public static int albumPosition = 0;
    public static boolean isEdit = false;
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
    private DocumentsFolderAdapter adapter;
    private AppDocumentsAdapter docadapter;
    private ArrayList<DocumentFolder> documentFolders;
    private ArrayList<DocumentsEnt> documentList;
    private GridView gridView;
    private boolean isSearch = false;
    private LinearLayout ll_EditAlbum;
    private int position;
    private int AlbumId = 0;
    private boolean IsMoreDropdown = false;
    private int _SortBy = 0;
    private String folderName = "";
    private SensorManager sensorManager;

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
        setContentView(R.layout.activity_docuement_folders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RelativeLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_rename_btn = findViewById(R.id.ll_rename_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);
        FloatingActionButton btn_Add_Album = findViewById(R.id.btn_Add_Album);


        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(DocumentsFolderActivity.this, ll_banner);

        gridView = findViewById(R.id.AlbumsGalleryGrid);
        Progress = findViewById(R.id.prbLoading);
        ll_EditAlbum = findViewById(R.id.ll_EditAlbum);
        gridView = findViewById(R.id.AlbumsGalleryGrid);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Documents");
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        Common.IsPhoneGalleryLoad = true;
        SecurityLocksCommon.IsAppDeactive = true;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        AppSettingsSharedPreferences appSettingsSharedPreferences = AppSettingsSharedPreferences.GetObject(this);
        _SortBy = appSettingsSharedPreferences.GetDocumentFoldersSortBy();

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsMoreDropdown) {
                IsMoreDropdown = false;
            }
            return false;
        });

        Progress.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            GetFodlersFromDatabase(_SortBy);
            Message message = new Message();
            message.what = 1;
            handle.sendMessage(message);
        }, 300);

        btn_Add_Album.setOnClickListener(view -> {
            if (!DocumentsFolderActivity.isEdit) {
                AddAlbumPopup();
            }
        });

        gridView.setOnItemClickListener((adapterView, view, i, j) -> {
            DocumentsFolderActivity.albumPosition = gridView.getFirstVisiblePosition();
            if (DocumentsFolderActivity.isEdit) {
                DocumentsFolderActivity.isEdit = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, i, DocumentsFolderActivity.isEdit);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else if (isSearch) {
                isSearch = false;
                int id = documentList.get(i).getId();
                DocumentDAL documentDAL = new DocumentDAL(DocumentsFolderActivity.this);
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
                CopyTempFile(file2.getAbsolutePath());
            } else {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.FolderId = documentFolders.get(i).getId();
                startActivity(new Intent(DocumentsFolderActivity.this, DocumentsActivity.class));
                finish();
            }
        });

        gridView.setOnItemLongClickListener((adapterView, view, i, j) -> {
            DocumentsFolderActivity.albumPosition = gridView.getFirstVisiblePosition();
            if (DocumentsFolderActivity.isEdit) {
                DocumentsFolderActivity.isEdit = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, i, DocumentsFolderActivity.isEdit);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                DocumentsFolderActivity.isEdit = true;
                ll_EditAlbum.setVisibility(View.VISIBLE);
                position = i;
                AlbumId = Common.FolderId;
                folderName = documentFolders.get(position).getFolderName();
                adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, i, DocumentsFolderActivity.isEdit);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (DocumentsFolderActivity.albumPosition != 0) {
                gridView.setSelection(DocumentsFolderActivity.albumPosition);
            }
            return true;
        });

        ll_rename_btn.setOnClickListener(view -> {
            if (documentFolders.get(position).getId() != 1) {
                EditAlbumPopup(documentFolders.get(position).getId(), documentFolders.get(position).getFolderName(), documentFolders.get(position).getFolderLocation());
                return;
            }
            Toast.makeText(DocumentsFolderActivity.this, R.string.lbl_default_folder_notrenamed, Toast.LENGTH_SHORT).show();
            DocumentsFolderActivity.isEdit = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, 0, DocumentsFolderActivity.isEdit);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        ll_delete_btn.setOnClickListener(view -> {
            if (documentFolders.get(position).getId() != 1) {
                DeleteALertDialog(documentFolders.get(position).getId(), documentFolders.get(position).getFolderName(), documentFolders.get(position).getFolderLocation());
                return;
            }
            Toast.makeText(DocumentsFolderActivity.this, R.string.lbl_default_folder_notdeleted, Toast.LENGTH_SHORT).show();
            DocumentsFolderActivity.isEdit = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, 0, DocumentsFolderActivity.isEdit);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        int i = albumPosition;
        if (i != 0) {
            gridView.setSelection(i);
            albumPosition = 0;
        }
        documentBind();
    }

    private void documentBind() {
        DocumentDAL documentDAL = new DocumentDAL(this);
        documentDAL.OpenRead();
        documentList = (ArrayList) documentDAL.GetAllDocuments();
        docadapter = new AppDocumentsAdapter(this, 17367043, documentList, false);
        gridView.setAdapter(adapter);
        documentDAL.close();
    }

    public void btnBackonClick(View view) {
        if (isEdit) {
            SecurityLocksCommon.IsAppDeactive = false;
            isEdit = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new DocumentsFolderAdapter(this, 17367043, documentFolders, 0, isEdit);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (isSearch) {
            isSearch = false;
            GetFodlersFromDatabase(_SortBy);
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
    }

    public void GetFodlersFromDatabase(int i) {
        isEdit = false;
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
        try {
            documentFolderDAL.OpenRead();
            documentFolders = (ArrayList) documentFolderDAL.GetFolders(i);
            adapter = new DocumentsFolderAdapter(this, 17367043, documentFolders, 0, isEdit);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentFolderDAL.close();
            throw th;
        }
    }

    public void AddAlbumPopup() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.album_add_edit_popup, null);
        bottomSheetDialog.setContentView(inflate);

        TextView textView = inflate.findViewById(R.id.lbl_Create_Edit_Album);
        textView.setText(R.string.lbl_Document_folder_Create_Album);

        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);
        editText.setHint(R.string.lbl_Document_folder_Create_Folder_enter);

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (editText.getEditableText().toString().length() <= 0 || editText.getEditableText().toString().trim().isEmpty()) {
                Toast.makeText(DocumentsFolderActivity.this, R.string.lbl_Document_folder_Create_Folder_please_enter, Toast.LENGTH_SHORT).show();
                return;
            }
            folderName = editText.getEditableText().toString();
            File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.DOCUMENTS + folderName);
            if (file.exists()) {
                Toast.makeText(DocumentsFolderActivity.this, "\"" + folderName + "\" already exist", Toast.LENGTH_SHORT).show();
            } else {
                file.mkdirs();
                DocumentsFolderGalleryMethods documentsFolderGalleryMethods = new DocumentsFolderGalleryMethods();
                documentsFolderGalleryMethods.AddFolderToDatabase(DocumentsFolderActivity.this, folderName);
                Toast.makeText(DocumentsFolderActivity.this, R.string.lbl_Document_folder_Create_Album_Success, Toast.LENGTH_SHORT).show();
                GetFodlersFromDatabase(_SortBy);
                bottomSheetDialog.dismiss();
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
        textView.setText(R.string.lbl_Document_folder_Rename_Album);

        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);
        editText.setHint(R.string.lbl_Document_folder_Create_Folder_enter);

        if (str.length() > 0) {
            editText.setText(str);
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (editText.getEditableText().toString().length() <= 0 || editText.getEditableText().toString().trim().isEmpty()) {
                Toast.makeText(DocumentsFolderActivity.this, R.string.lbl_Document_folder_Create_Folder_please_enter, Toast.LENGTH_SHORT).show();
                return;
            }
            folderName = editText.getEditableText().toString();
            if (new File(str2).exists()) {
                File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + folderName);
                if (file.exists()) {
                    Toast.makeText(DocumentsFolderActivity.this, "\"" + folderName + "\" already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + str);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file2.renameTo(file)) {
                    DocumentsFolderGalleryMethods documentsFolderGalleryMethods = new DocumentsFolderGalleryMethods();
                    documentsFolderGalleryMethods.UpdateAlbumInDatabase(DocumentsFolderActivity.this, i, folderName);
                    Toast.makeText(DocumentsFolderActivity.this, R.string.lbl_Photos_Album_Create_Album_Success_renamed, Toast.LENGTH_SHORT).show();
                    GetFodlersFromDatabase(_SortBy);
                    bottomSheetDialog.dismiss();
                    ll_EditAlbum.setVisibility(View.GONE);
                }
            }
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            ll_EditAlbum.setVisibility(View.GONE);
            DocumentsFolderActivity.isEdit = false;
            adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, position, DocumentsFolderActivity.isEdit);
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

        if (str.length() > 9) {
            textView.setText("Are you sure you want to delete " + str.substring(0, 8) + "... including its data?");
        } else {
            textView.setText("Are you sure you want to delete " + str + " including its data?");
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            DeleteAlbum(i, str, str2);
            bottomSheetDialog.dismiss();
            ll_EditAlbum.setVisibility(View.GONE);
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            DocumentsFolderActivity.isEdit = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new DocumentsFolderAdapter(DocumentsFolderActivity.this, 17367043, documentFolders, position, DocumentsFolderActivity.isEdit);
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
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this);
        try {
            documentFolderDAL.OpenWrite();
            documentFolderDAL.DeleteFolderById(i);
            Toast.makeText(this, R.string.lbl_Photos_Album_delete_success, Toast.LENGTH_SHORT).show();
            GetFodlersFromDatabase(_SortBy);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentFolderDAL.close();
            throw th;
        }
        documentFolderDAL.close();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_cloud_view_sort, menu);
        menu.findItem(R.id.action_search).setIcon(R.drawable.top_srch_icon);

        ((SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                if (str.length() > 0) {
                    isSearch = true;
                    ArrayList<DocumentsEnt> arrayList = new ArrayList<>();
                    for (DocumentsEnt next : documentList) {
                        if (next.getDocumentName().toLowerCase().contains(str)) {
                            arrayList.add(next);
                        }
                    }
                    gridView.setNumColumns(1);
                    gridView.setVerticalSpacing(Utilities.convertDptoPix(getApplicationContext(), 4));
                    docadapter = new AppDocumentsAdapter(DocumentsFolderActivity.this, 17367043, arrayList, false);
                    gridView.setAdapter(docadapter);
                } else {
                    isSearch = false;
                    GetFodlersFromDatabase(_SortBy);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(getApplicationContext(), ActivityMain.class));
            finish();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    public void CopyTempFile(String str) {
        File file = new File(str);
        try {
            Utilities.NSDecryption(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse(Constants.FILE + file.getAbsolutePath()), guessContentTypeFromName);
        startActivity(intent);
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
    public void onPause() {
        ll_EditAlbum.setVisibility(View.GONE);
        sensorManager.unregisterListener(this);
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
            if (isEdit) {
                SecurityLocksCommon.IsAppDeactive = false;
                isEdit = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new DocumentsFolderAdapter(this, 17367043, documentFolders, 0, isEdit);
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
