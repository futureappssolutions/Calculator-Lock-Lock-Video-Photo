package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.notes.NotesCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.NotesFileDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.NotesFilesDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.NotesFolderDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.NotesFolderDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.NotesFolderGridViewAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.FileUtils;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.wallet.CommonSharedPreferences;
import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFoldersActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private boolean isGridview = true;
    private boolean isEdittable = false;
    private int folderPosition = -1;
    private Constants constants;
    private Dialog dialog;
    private GridView gv_NotesFolder;
    private LinearLayout ll_NotesFolderEdit;
    private NotesCommon notesCommon;
    private SensorManager sensorManager;
    private NotesFilesDAL notesFilesDAL;
    private NotesFolderDAL notesFolderDAL;
    private List<NotesFolderDB_Pojo> notesFolderPojoList;
    private NotesFolderGridViewAdapter notesFolderadapter;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notes_folders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        gv_NotesFolder = findViewById(R.id.gv_NotesFolder);
        ll_NotesFolderEdit = findViewById(R.id.ll_NotesFolderEdit);

        LinearLayout ll_rename_btn = findViewById(R.id.ll_rename_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBanner(NotesFoldersActivity.this, ll_banner);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        notesCommon = new NotesCommon();
        notesFolderPojoList = new ArrayList<>();
        notesFolderDAL = new NotesFolderDAL(this);
        notesFilesDAL = new NotesFilesDAL(this);
        constants = new Constants();

        SecurityLocksCommon.IsAppDeactive = true;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Notes");
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        CommonSharedPreferences commonSharedPreferences = CommonSharedPreferences.GetObject(this);
        isGridview = commonSharedPreferences.get_viewByNotesFolder() != 0;

        checkIsDefaultFolderCreated();
        setGridview();

        gv_NotesFolder.setOnItemClickListener(new ItemClickListener());
        ll_rename_btn.setOnClickListener(new ClickListener());
        ll_delete_btn.setOnClickListener(new ClickListener());
        gv_NotesFolder.setOnItemLongClickListener(new ItemLongClickListeners());

        try {
            File file = new File(getFilesDir(), "Recordings");
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_cloud_view_sort, menu);
        ((SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search))).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                List<NotesFolderDB_Pojo> arrayList = new ArrayList<>();
                if (str.length() > 0) {
                    for (NotesFolderDB_Pojo notesFolderDB_Pojo : notesFolderPojoList) {
                        if (notesFolderDB_Pojo.getNotesFolderName().toLowerCase().contains(str)) {
                            arrayList.add(notesFolderDB_Pojo);
                        }
                    }
                } else {
                    arrayList = notesFolderPojoList;
                }
                bindSearchResult(arrayList);
                return true;
            }
        });
        return true;
    }

    public void bindSearchResult(List<NotesFolderDB_Pojo> list) {
        notesFolderadapter = new NotesFolderGridViewAdapter(this, list);
        notesFolderadapter.setIsGridview(isGridview);
        gv_NotesFolder.setAdapter(notesFolderadapter);
        notesFolderadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void checkIsDefaultFolderCreated() {
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES, getResources().getString(R.string.my_notes));
        if (!file.exists()) {
            file.mkdirs();
        }

        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
        constants.getClass();
        sb.append("NotesFolderName");
        sb.append(" = '");
        sb.append(getResources().getString(R.string.my_notes));
        sb.append("' AND ");
        constants.getClass();
        sb.append("NotesFolderIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        if (!notesFolderDAL.IsFolderAlreadyExist(sb.toString())) {
            String currentDate = notesCommon.getCurrentDate();
            NotesFolderDB_Pojo notesFolderDB_Pojo = new NotesFolderDB_Pojo();
            notesFolderDB_Pojo.setNotesFolderName(getResources().getString(R.string.my_notes));
            notesFolderDB_Pojo.setNotesFolderLocation(file.getAbsolutePath());
            notesFolderDB_Pojo.setNotesFolderCreatedDate(currentDate);
            notesFolderDB_Pojo.setNotesFolderModifiedDate(currentDate);
            notesFolderDB_Pojo.setNotesFolderFilesSortBy(1);
            notesFolderDB_Pojo.setNotesFolderColor(String.valueOf(getResources().getColor(R.color.ColorLightBlue)));
            notesFolderDB_Pojo.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
            notesFolderDAL.addNotesFolderInfoInDatabase(notesFolderDB_Pojo);
        }
    }

    public void setGridview() {
        StringBuilder sb2 = new StringBuilder();
        constants.getClass();
        sb2.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
        constants.getClass();
        sb2.append("NotesFolderIsDecoy");
        sb2.append(" = ");
        sb2.append(SecurityLocksCommon.IsFakeAccount);
        sb2.append(" ORDER BY ");
        constants.getClass();
        sb2.append("NotesFolderModifiedDate");
        sb2.append(" DESC");
        notesFolderPojoList = notesFolderDAL.getAllNotesFolderInfoFromDatabase(sb2.toString());

        notesFolderadapter = new NotesFolderGridViewAdapter(this, notesFolderPojoList);
        notesFolderadapter.setFocusedPosition(0);
        notesFolderadapter.setIsEdit(false);
        notesFolderadapter.setIsGridview(isGridview);
        gv_NotesFolder.setAdapter(notesFolderadapter);
        notesFolderadapter.notifyDataSetChanged();
    }

    public void fabClicked(View view) {
        addFolderDialog();
    }

    public void addFolderDialog() {
        dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.dialog_add_notes_folder);
        dialog.setCancelable(true);

        final EditText editText = dialog.findViewById(R.id.et_folderName);
        final ImageView imageView = dialog.findViewById(R.id.iv_selectedColor);
        final ColorPicker colorPicker = dialog.findViewById(R.id.folder_colorPicker);
        colorPicker.addSVBar(dialog.findViewById(R.id.svbar));
        colorPicker.addOpacityBar(dialog.findViewById(R.id.opacitybar));

        colorPicker.requestFocus();
        colorPicker.setColor(getResources().getColor(R.color.ColorLightBlue));
        colorPicker.setOldCenterColor(getResources().getColor(R.color.ColorLightBlue));
        imageView.setBackgroundColor(getResources().getColor(R.color.ColorLightBlue));

        colorPicker.setOnColorChangedListener(imageView::setBackgroundColor);

        ((TextView) dialog.findViewById(R.id.lbl_Create_Edit_Album)).setText(getResources().getString(R.string.add_folder));
        editText.setHint(R.string.add_folder_hint);

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            String trim = editText.getText().toString().trim();
            if (trim.equals("") || trim.isEmpty()) {
                Toast.makeText(NotesFoldersActivity.this, "Enter folder Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            createFolder(trim, String.valueOf(colorPicker.getColor()));
            Log.i("color", String.valueOf(colorPicker.getColor()));
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void createFolder(String str, String str2) {
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES, str);
        constants.getClass();
        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("NotesFolderName");
        sb.append(" = '");
        sb.append(str);
        sb.append("' AND ");
        constants.getClass();
        sb.append("NotesFolderIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        String concat = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ".concat(sb.toString());
        String currentDate = notesCommon.getCurrentDate();
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            NotesFolderDB_Pojo notesFolderDB_Pojo = new NotesFolderDB_Pojo();
            notesFolderDB_Pojo.setNotesFolderName(str);
            notesFolderDB_Pojo.setNotesFolderLocation(file.getAbsolutePath());
            notesFolderDB_Pojo.setNotesFolderCreatedDate(currentDate);
            notesFolderDB_Pojo.setNotesFolderModifiedDate(currentDate);
            notesFolderDB_Pojo.setNotesFolderFilesSortBy(1);
            notesFolderDB_Pojo.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
            notesFolderDB_Pojo.setNotesFolderColor(str2);
            if (!notesFolderDAL.IsFolderAlreadyExist(concat)) {
                notesFolderDAL.addNotesFolderInfoInDatabase(notesFolderDB_Pojo);
                Toast.makeText(this, getResources().getString(R.string.note_folder_created_successfully), Toast.LENGTH_SHORT).show();
                setGridview();
                dialog.dismiss();
                return;
            }
            Toast.makeText(this, getResources().getString(R.string.note_folder_exists), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renameFolder(NotesFolderDB_Pojo notesFolderDB_Pojo, String str, String str2) {
        File file = new File(notesFolderDB_Pojo.getNotesFolderLocation());
        File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES, str);
        if (str.equals(notesFolderDB_Pojo.getNotesFolderName())) {
            notesFolderDB_Pojo.setNotesFolderName(str);
            notesFolderDB_Pojo.setNotesFolderLocation(file2.getAbsolutePath());
            notesFolderDB_Pojo.setNotesFolderModifiedDate(notesCommon.getCurrentDate());
            notesFolderDB_Pojo.setNotesFolderColor(str2);
            notesFolderDB_Pojo.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
            constants.getClass();
            notesFolderDAL.updateNotesFolderFromDatabase(notesFolderDB_Pojo, "NotesFolderId", String.valueOf(notesFolderDB_Pojo.getNotesFolderId()));
            updateNotesFolderPath(notesFolderDB_Pojo.getNotesFolderId(), str, file2.getAbsolutePath());
            setGridview();
            Toast.makeText(this, getResources().getString(R.string.note_folder_renamed_successfully), Toast.LENGTH_SHORT).show();
            return;
        }
        if (file.exists()) {
            StringBuilder sb = new StringBuilder();
            constants.getClass();
            sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
            constants.getClass();
            sb.append("NotesFolderId");
            sb.append(" = ");
            sb.append(notesFolderDB_Pojo.getNotesFolderId());
            sb.append(" AND ");
            constants.getClass();
            sb.append("NotesFolderIsDecoy");
            sb.append(" = ");
            sb.append(SecurityLocksCommon.IsFakeAccount);
            if (notesFolderDAL.IsFolderAlreadyExist(sb.toString())) {
                StringBuilder sb2 = new StringBuilder();
                constants.getClass();
                sb2.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
                constants.getClass();
                sb2.append("NotesFolderName");
                sb2.append(" = '");
                sb2.append(str);
                sb2.append("' AND ");
                constants.getClass();
                sb2.append("NotesFolderIsDecoy");
                sb2.append(" = ");
                sb2.append(SecurityLocksCommon.IsFakeAccount);
                if (notesFolderDAL.IsFolderAlreadyExist(sb2.toString())) {
                    Toast.makeText(this, getResources().getString(R.string.note_folder_exists), Toast.LENGTH_SHORT).show();
                } else if (file.renameTo(file2)) {
                    notesFolderDB_Pojo.setNotesFolderName(str);
                    notesFolderDB_Pojo.setNotesFolderLocation(file2.getAbsolutePath());
                    notesFolderDB_Pojo.setNotesFolderModifiedDate(notesCommon.getCurrentDate());
                    notesFolderDB_Pojo.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                    notesFolderDB_Pojo.setNotesFolderColor(str2);
                    constants.getClass();
                    notesFolderDAL.updateNotesFolderFromDatabase(notesFolderDB_Pojo, "NotesFolderId", String.valueOf(notesFolderDB_Pojo.getNotesFolderId()));
                    updateNotesFolderPath(notesFolderDB_Pojo.getNotesFolderId(), str, file2.getAbsolutePath());
                    setGridview();
                }
                Toast.makeText(this, getResources().getString(R.string.note_folder_renamed_successfully), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, getResources().getString(R.string.note_folder_exists), Toast.LENGTH_SHORT).show();
    }

    public boolean deleteFolder(int i, String str) {
        File file = new File(str);
        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
        constants.getClass();
        sb.append("NotesFolderId");
        sb.append(" = ");
        sb.append(i);
        sb.append(" AND ");
        constants.getClass();
        sb.append("NotesFolderIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        if (notesFolderDAL.IsFolderAlreadyExist(sb.toString())) {
            constants.getClass();
            notesFolderDAL.deleteNotesFolderFromDatabase("NotesFolderId", String.valueOf(i));
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            assert listFiles != null;
            for (File file2 : listFiles) {
                if (file2.exists()) {
                    file2.delete();
                    constants.getClass();
                    notesFilesDAL.deleteNotesFileFromDatabase("NotesFolderId", String.valueOf(i));
                }
            }
        }
        return file.delete();
    }

    public void updateNotesFolderPath(int i, String str, String str2) {
        new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ");
        constants.getClass();
        sb.append("NotesFolderId");
        sb.append(" = ");
        sb.append(i);
        sb.append(" AND ");
        constants.getClass();
        sb.append("NotesFileIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        for (NotesFileDB_Pojo notesFileDB_Pojo : notesFilesDAL.getAllNotesFileInfoFromDatabase(sb.toString())) {
            notesFileDB_Pojo.setNotesFileLocation(str2 + File.separator + notesFileDB_Pojo.getNotesFileName() + StorageOptionsCommon.NOTES_FILE_EXTENSION);
            constants.getClass();
            notesFilesDAL.updateNotesFileLocationInDatabase(notesFileDB_Pojo, "NotesFolderId", String.valueOf(i));
        }
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public void show_Dialog(View view, final int i) {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        switch (view.getId()) {
            case R.id.ll_delete_btn:
                dialog.setContentView(R.layout.confirmation_message_box);
                ((TextView) dialog.findViewById(R.id.tvmessagedialogtitle)).setText(getResources().getString(R.string.delete_toast) + " " + notesFolderPojoList.get(i).getNotesFolderName() + " ?");

                dialog.findViewById(R.id.ll_Ok).setOnClickListener(view2 -> {
                    NotesFoldersActivity notesFoldersActivity = NotesFoldersActivity.this;
                    if (notesFoldersActivity.deleteFolder(notesFoldersActivity.notesFolderPojoList.get(i).getNotesFolderId(), notesFolderPojoList.get(i).getNotesFolderLocation())) {
                        NotesFoldersActivity notesFoldersActivity2 = NotesFoldersActivity.this;
                        Toast.makeText(notesFoldersActivity2, notesFoldersActivity2.getResources().getString(R.string.note_folder_deleted_successfully), Toast.LENGTH_SHORT).show();
                        setGridview();
                    } else {
                        NotesFoldersActivity notesFoldersActivity3 = NotesFoldersActivity.this;
                        Toast.makeText(notesFoldersActivity3, notesFoldersActivity3.getResources().getString(R.string.note_folder_not_deleted), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                });

                dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view2 -> dialog.dismiss());
                break;
            case R.id.ll_rename_btn:
                dialog.setContentView(R.layout.dialog_add_notes_folder);
                TextView textView = dialog.findViewById(R.id.lbl_Create_Edit_Album);
                LinearLayout linearLayout = dialog.findViewById(R.id.ll_Ok);
                LinearLayout linearLayout2 = dialog.findViewById(R.id.ll_Cancel);
                final EditText editText = dialog.findViewById(R.id.et_folderName);
                final ImageView imageView = dialog.findViewById(R.id.iv_selectedColor);
                final ColorPicker colorPicker = dialog.findViewById(R.id.folder_colorPicker);
                colorPicker.addSVBar(dialog.findViewById(R.id.svbar));
                colorPicker.addOpacityBar(dialog.findViewById(R.id.opacitybar));
                colorPicker.requestFocus();
                editText.setText(notesFolderPojoList.get(i).getNotesFolderName());
                try {
                    int parseInt = Integer.parseInt(notesFolderPojoList.get(i).getNotesFolderColor());
                    colorPicker.setColor(parseInt);
                    colorPicker.setOldCenterColor(parseInt);
                    imageView.setBackgroundColor(parseInt);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                colorPicker.setOnColorChangedListener(imageView::setBackgroundColor);
                textView.setText("Rename");
                editText.setHint(R.string.add_folder_hint);

                linearLayout.setOnClickListener(view2 -> {
                    String trim = editText.getText().toString().trim();
                    if (trim.equals("") || trim.isEmpty()) {
                        Toast.makeText(NotesFoldersActivity.this, "Enter Folder name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NotesFoldersActivity notesFoldersActivity = NotesFoldersActivity.this;
                    notesFoldersActivity.renameFolder(notesFoldersActivity.notesFolderPojoList.get(i), trim, String.valueOf(colorPicker.getColor()));
                    dialog.dismiss();
                });

                linearLayout2.setOnClickListener(view2 -> dialog.dismiss());
                break;
        }
        dialog.show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onBackPressed() {
        if (isEdittable) {
            isEdittable = false;
            ll_NotesFolderEdit.setVisibility(View.GONE);
            setGridview();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    public class ClickListener implements View.OnClickListener {
        public ClickListener() {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            if (isEdittable && folderPosition >= 0) {
                switch (view.getId()) {
                    case R.id.ll_delete_btn:
                        if (notesFolderPojoList.get(folderPosition).getNotesFolderName().equals("My Notes")) {
                            Toast.makeText(NotesFoldersActivity.this, "Default folder can not be deleted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        show_Dialog(view, folderPosition);
                        ll_NotesFolderEdit.setVisibility(View.GONE);
                        isEdittable = false;
                        setGridview();
                        return;
                    case R.id.ll_rename_btn:
                        if (notesFolderPojoList.get(folderPosition).getNotesFolderName().equals("My Notes")) {
                            Toast.makeText(NotesFoldersActivity.this, "Default folder can not be renamed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        show_Dialog(view, folderPosition);
                        ll_NotesFolderEdit.setVisibility(View.GONE);
                        isEdittable = false;
                        setGridview();
                        return;
                    default:
                }
            }
        }
    }

    public class ItemClickListener implements AdapterView.OnItemClickListener {
        public ItemClickListener() {
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (isEdittable) {
                isEdittable = false;
                ll_NotesFolderEdit.setVisibility(View.GONE);
                notesFolderadapter.setFocusedPosition(i);
                notesFolderadapter.setIsEdit(isEdittable);
                gv_NotesFolder.setAdapter(notesFolderadapter);
                notesFolderadapter.notifyDataSetChanged();
                return;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            ll_NotesFolderEdit.setVisibility(View.GONE);
            NotesCommon.CurrentNotesFolder = notesFolderPojoList.get(i).getNotesFolderName();
            NotesCommon.CurrentNotesFolderId = notesFolderPojoList.get(i).getNotesFolderId();
            startActivity(new Intent(NotesFoldersActivity.this, NotesFilesActivity.class));
            finish();
        }
    }

    public class ItemLongClickListeners implements AdapterView.OnItemLongClickListener {
        public ItemLongClickListeners() {
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (isEdittable) {
                isEdittable = false;
                ll_NotesFolderEdit.setVisibility(View.GONE);
            } else {
                isEdittable = true;
                ll_NotesFolderEdit.setVisibility(View.VISIBLE);
            }
            folderPosition = i;
            notesFolderadapter.setFocusedPosition(i);
            notesFolderadapter.setIsEdit(isEdittable);
            gv_NotesFolder.setAdapter(notesFolderadapter);
            notesFolderadapter.notifyDataSetChanged();
            return true;
        }
    }
}
