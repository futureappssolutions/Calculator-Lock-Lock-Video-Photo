package com.calculator.vaultlocker.Activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.Adapter.MoveNoteAdapter;
import com.calculator.vaultlocker.notes.NotesCommon;
import com.calculator.vaultlocker.Model.NotesFileDB_Pojo;
import com.calculator.vaultlocker.DB.NotesFilesDAL;
import com.calculator.vaultlocker.Adapter.NotesFilesGridViewAdapter;
import com.calculator.vaultlocker.DB.NotesFolderDAL;
import com.calculator.vaultlocker.Model.NotesFolderDB_Pojo;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesFilesActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private GridView gv_NotesFiles;
    private LinearLayout ll_NotesFileEdit;
    private LinearLayout ll_noNotes;
    private Constants constants;
    private SensorManager sensorManager;
    private NotesCommon notesCommon;
    private NotesFilesDAL notesFilesDAL;
    private NotesFolderDAL notesFolderDAL;
    private List<Integer> focusedPosition;
    private NotesFolderDB_Pojo currentFolderDBInfo;
    private List<NotesFileDB_Pojo> notesFileDB_PojoList;
    private List<NotesFolderDB_Pojo> notesFolderDB_PojoList;
    private NotesFilesGridViewAdapter notesFilesGridViewAdapter;
    private boolean isEdittable = false;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notes_files);

        LinearLayout ll_move_btn = findViewById(R.id.ll_move_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        gv_NotesFiles = findViewById(R.id.gv_NotesFiles);
        ll_NotesFileEdit = findViewById(R.id.ll_NotesFileEdit);
        ll_noNotes = findViewById(R.id.ll_noNotes);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        notesFileDB_PojoList = new ArrayList<>();
        notesCommon = new NotesCommon();
        notesFilesDAL = new NotesFilesDAL(this);
        notesFolderDAL = new NotesFolderDAL(this);
        notesFolderDB_PojoList = new ArrayList<>();
        focusedPosition = new ArrayList<>();
        constants = new Constants();

        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
        constants.getClass();
        sb.append("NotesFolderIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        sb.append(" ORDER BY ");
        constants.getClass();
        sb.append("NotesFolderModifiedDate");
        sb.append(" DESC");
        notesFolderDB_PojoList = notesFolderDAL.getAllNotesFolderInfoFromDatabase(sb.toString());
        currentFolderDBInfo = new NotesFolderDB_Pojo();

        SecurityLocksCommon.IsAppDeactive = true;
        getCurrentFolder();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(NotesCommon.CurrentNotesFolder);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        setGridview();

        gv_NotesFiles.setOnItemClickListener(new NotesFilesOnItemClickListener());
        gv_NotesFiles.setOnItemLongClickListener(new NotesFilesOnItemLongClickListener());
        ll_move_btn.setOnClickListener(new NotesFilesOnClickListener());
        ll_delete_btn.setOnClickListener(new NotesFilesOnClickListener());
    }

    public void getCurrentFolder() {
        StringBuilder sb = new StringBuilder();
        constants.getClass();
        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
        constants.getClass();
        sb.append("NotesFolderId");
        sb.append(" = ");
        sb.append(NotesCommon.CurrentNotesFolderId);
        sb.append(" AND ");
        constants.getClass();
        sb.append("NotesFolderIsDecoy");
        sb.append(" = ");
        sb.append(SecurityLocksCommon.IsFakeAccount);
        currentFolderDBInfo = notesFolderDAL.getNotesFolderInfoFromDatabase(sb.toString());
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
                List<NotesFileDB_Pojo> arrayList = new ArrayList<>();
                if (str.length() > 0) {
                    for (NotesFileDB_Pojo notesFileDB_Pojo : notesFileDB_PojoList) {
                        if (notesFileDB_Pojo.getNotesFileName().toLowerCase().contains(str)) {
                            arrayList.add(notesFileDB_Pojo);
                        }
                    }
                } else {
                    arrayList = notesFileDB_PojoList;
                }
                bindSearchResult(arrayList);
                return true;
            }
        });
        return true;
    }

    public void bindSearchResult(List<NotesFileDB_Pojo> list) {
        notesFilesGridViewAdapter = new NotesFilesGridViewAdapter(this, list);
        gv_NotesFiles.setAdapter(notesFilesGridViewAdapter);
        notesFilesGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, NotesFoldersActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setGridview() {
        StringBuilder sb5 = new StringBuilder();
        constants.getClass();
        StringBuilder sb6 = new StringBuilder();
        constants.getClass();
        sb6.append("NotesFolderId");
        sb6.append(" = ");
        sb6.append(NotesCommon.CurrentNotesFolderId);
        sb5.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ".concat(sb6.toString()));
        sb5.append(" AND ");
        constants.getClass();
        sb5.append("NotesFileIsDecoy");
        sb5.append(" = ");
        sb5.append(SecurityLocksCommon.IsFakeAccount);
        sb5.append(" ORDER BY ");
        constants.getClass();
        sb5.append("NotesFileModifiedDate");
        sb5.append(" DESC");
        notesFileDB_PojoList = notesFilesDAL.getAllNotesFileInfoFromDatabase(sb5.toString());

        if (notesFileDB_PojoList.size() > 0) {
            notesFilesGridViewAdapter = new NotesFilesGridViewAdapter(this, notesFileDB_PojoList);
            notesFilesGridViewAdapter.setIsEdit(false);
            gv_NotesFiles.setAdapter(notesFilesGridViewAdapter);
            notesFilesGridViewAdapter.notifyDataSetChanged();
            ll_noNotes.setVisibility(View.GONE);
            return;
        }
        ll_noNotes.setVisibility(View.VISIBLE);
    }

    public void fabClicked(View view) {
        NotesCommon.isEdittingNote = false;
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, MyNoteActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isEdittable) {
            notesFilesGridViewAdapter.removeAllFocusedPosition();
            focusedPosition.clear();
            isEdittable = false;
            ll_NotesFileEdit.setVisibility(View.GONE);
            setGridview();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, NotesFoldersActivity.class));
        finish();
    }

    public boolean deleteNote(int i, String str) {
        File file = new File(str);
        try {
            constants.getClass();
            notesFilesDAL.deleteNotesFileFromDatabase("NotesFileId", String.valueOf(i));
            if (file.exists()) {
                return file.delete();
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public void moveNote(int i, String str) {
        for (int i2 = 0; i2 < focusedPosition.size(); i2++) {
            int intValue = focusedPosition.get(i2);
            new NotesFileDB_Pojo();
            NotesFileDB_Pojo notesFileDB_Pojo = notesFileDB_PojoList.get(intValue);
            File file = new File(notesFileDB_Pojo.getNotesFileLocation());
            File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES, str);
            String str2 = file2.getAbsolutePath() + File.separator + notesFileDB_Pojo.getNotesFileName() + StorageOptionsCommon.NOTES_FILE_EXTENSION;
            if (file.exists()) {
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                try {
                    FileUtils.moveToDirectory(file, file2, true);
                    notesFileDB_Pojo.setNotesFileFolderId(i);
                    notesFileDB_Pojo.setNotesFileName(notesFileDB_Pojo.getNotesFileName());
                    notesFileDB_Pojo.setNotesFileModifiedDate(notesCommon.getCurrentDate());
                    notesFileDB_Pojo.setNotesFileLocation(str2);
                    notesFileDB_Pojo.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                    constants.getClass();
                    notesFilesDAL.updateNotesFileInfoInDatabase(notesFileDB_Pojo, "NotesFileId", String.valueOf(notesFileDB_PojoList.get(intValue).getNotesFileId()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.toast_exists, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.toast_not_exists, Toast.LENGTH_SHORT).show();
            }
        }
        setGridview();
        Toast.makeText(this, R.string.toast_move, Toast.LENGTH_SHORT).show();
    }

    public void NoteMoveDialog() {
        final ArrayList<NotesFolderDB_Pojo> arrayList = new ArrayList<>();
        for (NotesFolderDB_Pojo notesFolderDB_Pojo : notesFolderDB_PojoList) {
            if (!notesFolderDB_Pojo.getNotesFolderName().equals(NotesCommon.CurrentNotesFolder)) {
                arrayList.add(notesFolderDB_Pojo);
            }
        }
        if (arrayList.size() > 0) {
            final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.move_customlistview);
            ListView listView = dialog.findViewById(R.id.ListViewfolderslist);
            listView.setAdapter(new MoveNoteAdapter(this, arrayList));

            listView.setOnItemClickListener((adapterView, view, i, j) -> {
                moveNote(arrayList.get(i).getNotesFolderId(), arrayList.get(i).getNotesFolderName());
                dialog.dismiss();
            });
            dialog.show();
            return;
        }
        Toast.makeText(this, R.string.no_other_folder_exists, Toast.LENGTH_SHORT).show();
        notesFilesGridViewAdapter.removeAllFocusedPosition();
        focusedPosition.clear();
    }

    public void noteDeleteDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.tvmessagedialogtitle)).setText(getResources().getString(R.string.delete_note));

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            for (int i = 0; i < focusedPosition.size(); i++) {
                int intValue = focusedPosition.get(i);
                if (!deleteNote(notesFileDB_PojoList.get(intValue).getNotesFileId(), notesFileDB_PojoList.get(intValue).getNotesFileLocation())) {
                    Toast.makeText(NotesFilesActivity.this, notesFileDB_PojoList.get(intValue).getNotesFileName() + " could not be deleted", Toast.LENGTH_SHORT).show();
                }
            }
            setGridview();
            dialog.dismiss();
            notesFilesGridViewAdapter.removeAllFocusedPosition();
            focusedPosition.clear();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            dialog.dismiss();
            notesFilesGridViewAdapter.removeAllFocusedPosition();
            focusedPosition.clear();
        });
        dialog.show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
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

    public class NotesFilesOnClickListener implements View.OnClickListener {
        public NotesFilesOnClickListener() {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            if (isEdittable && focusedPosition.size() > 0) {
                switch (view.getId()) {
                    case R.id.ll_delete_btn:
                        noteDeleteDialog();
                        ll_NotesFileEdit.setVisibility(View.GONE);
                        isEdittable = false;
                        setGridview();
                        return;
                    case R.id.ll_move_btn:
                        NoteMoveDialog();
                        ll_NotesFileEdit.setVisibility(View.GONE);
                        isEdittable = false;
                        setGridview();
                        return;
                    default:
                }
            }
        }
    }

    public class NotesFilesOnItemClickListener implements AdapterView.OnItemClickListener {
        public NotesFilesOnItemClickListener() {
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (isEdittable) {
                if (focusedPosition.contains(i)) {
                    focusedPosition.remove(Integer.valueOf(i));
                    notesFilesGridViewAdapter.removeFocusedPosition(i);
                    if (focusedPosition.size() == 0) {
                        isEdittable = false;
                        ll_NotesFileEdit.setVisibility(View.GONE);
                        notesFilesGridViewAdapter.removeAllFocusedPosition();
                        focusedPosition.clear();
                    }
                } else {
                    focusedPosition.add(i);
                    notesFilesGridViewAdapter.setFocusedPosition(i);
                }
                notesFilesGridViewAdapter.setIsEdit(isEdittable);
                gv_NotesFiles.setAdapter(notesFilesGridViewAdapter);
                notesFilesGridViewAdapter.notifyDataSetChanged();
                return;
            }
            NotesCommon.isEdittingNote = true;
            SecurityLocksCommon.IsAppDeactive = false;
            NotesCommon.CurrentNotesFile = notesFileDB_PojoList.get(i).getNotesFileName();
            startActivity(new Intent(NotesFilesActivity.this, MyNoteActivity.class));
            finish();
        }
    }

    public class NotesFilesOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
        public NotesFilesOnItemLongClickListener() {
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (focusedPosition.contains(i)) {
                focusedPosition.remove(Integer.valueOf(i));
                notesFilesGridViewAdapter.removeFocusedPosition(i);
            } else {
                focusedPosition.add(i);
                notesFilesGridViewAdapter.setFocusedPosition(i);
            }
            if (focusedPosition.size() > 0) {
                isEdittable = true;
                ll_NotesFileEdit.setVisibility(View.VISIBLE);
            } else {
                isEdittable = false;
                ll_NotesFileEdit.setVisibility(View.GONE);
            }
            notesFilesGridViewAdapter.setIsEdit(isEdittable);
            gv_NotesFiles.setAdapter(notesFilesGridViewAdapter);
            notesFilesGridViewAdapter.notifyDataSetChanged();
            return true;
        }
    }
}