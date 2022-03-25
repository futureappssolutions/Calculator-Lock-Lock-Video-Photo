package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoCheckableRowPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.ToDoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.ToDoReadFromXML;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoTask;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.ToDoWriteInXML;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewToDoActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private Constants constants;
    private LayoutInflater inflater;
    private ScrollView ll_main;
    private TableLayout tl_TodoTasks;
    private ToDoPojo toDoList;
    private SensorManager sensorManager;
    private List<ToDoCheckableRowPojo> rowList;
    private boolean isModified = false;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_todo_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tl_TodoTasks = findViewById(R.id.tl_TodoTasks);
        ll_main = findViewById(R.id.ll_main);

        SecurityLocksCommon.IsAppDeactive = true;
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        toDoList = new ToDoPojo();
        rowList = new ArrayList<>();
        constants = new Constants();

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        getToDoListFromFile();
        fillToDoDataInTableLayout();
    }

    public void getToDoListFromFile() {
        ToDoReadFromXML toDoReadFromXML = new ToDoReadFromXML();
        toDoList = toDoReadFromXML.ReadToDoList(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TODOLIST + Common.ToDoListName + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        Objects.requireNonNull(getSupportActionBar()).setTitle(toDoList.getTitle());
        ll_main.setBackgroundColor(Color.parseColor(toDoList.getTodoColor()));
    }

    public void fillToDoDataInTableLayout() {
        if (toDoList != null) {
            int i = 0;
            while (i < toDoList.getToDoList().size()) {
                try {
                    ToDoCheckableRowPojo toDoCheckableRowPojo = new ToDoCheckableRowPojo();
                    TableRow tableRow = new TableRow(getApplicationContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -1, 1.0f);
                    tableRow.setLayoutParams(layoutParams);

                    View inflate = inflater.inflate(R.layout.tablerow_todo_view, null);

                    final TextView textView = inflate.findViewById(R.id.tv_text);
                    final CheckBox checkBox = inflate.findViewById(R.id.cb_done);

                    int i2 = i + 1;

                    ((TextView) inflate.findViewById(R.id.tv_rowNo)).setText(String.valueOf(i2));
                    checkBox.setChecked(toDoList.getToDoList().get(i).isChecked());
                    Utilities.strikeTextview(textView, toDoList.getToDoList().get(i).getToDo(), checkBox.isChecked());
                    tableRow.addView(inflate, layoutParams);
                    tl_TodoTasks.addView(tableRow);
                    toDoCheckableRowPojo.setCb_done(checkBox);
                    toDoCheckableRowPojo.setTv_text(textView);
                    rowList.add(toDoCheckableRowPojo);

                    checkBox.setOnClickListener(view -> {
                        boolean isChecked = checkBox.isChecked();
                        Utilities.strikeTextview(textView, textView.getText().toString(), isChecked);
                        isModified = true;
                    });

                    textView.setOnClickListener(view -> {
                        checkBox.setChecked(!checkBox.isChecked());
                        Utilities.strikeTextview(textView, textView.getText().toString(), !checkBox.isChecked());
                        isModified = true;
                    });
                    i = i2;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void SaveDataBeforeNavigation() {
        if (isModified) {
            ArrayList<ToDoTask> arrayList = new ArrayList<>();
            boolean z = true;
            for (int i = 0; i < rowList.size(); i++) {
                boolean isChecked = rowList.get(i).getCb_done().isChecked();
                ToDoTask toDoTask = new ToDoTask();
                toDoTask.setChecked(isChecked);
                toDoTask.setToDo(rowList.get(i).getTv_text().getText().toString());
                arrayList.add(toDoTask);
                if (!isChecked) {
                    z = false;
                }
            }
            if (arrayList.size() > 0) {
                String currentDateTime2 = Utilities.getCurrentDateTime2();
                toDoList.deleteTodoList();
                toDoList.setToDoList(arrayList);
                toDoList.setDateModified(currentDateTime2);
                ToDoWriteInXML toDoWriteInXML = new ToDoWriteInXML();
                if (toDoWriteInXML.saveToDoList(this, toDoList, toDoList.getTitle(), true)) {
                    ToDoDAL toDoDAL = new ToDoDAL(this);
                    ToDoDB_Pojo toDoDB_Pojo = new ToDoDB_Pojo();
                    toDoDB_Pojo.setToDoFileModifiedDate(currentDateTime2);
                    toDoDB_Pojo.setToDoFileTask1(arrayList.get(0).getToDo());
                    toDoDB_Pojo.setToDoFileTask1IsChecked(arrayList.get(0).isChecked());
                    if (arrayList.size() >= 2) {
                        toDoDB_Pojo.setToDoFileTask2(arrayList.get(1).getToDo());
                        toDoDB_Pojo.setToDoFileTask2IsChecked(arrayList.get(1).isChecked());
                    }
                    toDoDB_Pojo.setToDoFinished(z);
                    constants.getClass();
                    toDoDAL.updateToDoFileTasksInDatabase(toDoDB_Pojo, "ToDoId", String.valueOf(Common.ToDoListId));
                }
            }
        }
    }

    public void deleteToDo() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        ((TextView) dialog.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.delete_todo);

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            try {
                dialog.dismiss();
                File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TODOLIST + Common.ToDoListName + StorageOptionsCommon.NOTES_FILE_EXTENSION);
                if (file.exists() && file.delete()) {
                    new ToDoDAL(ViewToDoActivity.this).deleteToDoFileFromDatabase("ToDoId", String.valueOf(Common.ToDoListId));
                    SecurityLocksCommon.IsAppDeactive = false;
                    startActivity(new Intent(ViewToDoActivity.this, ToDoActivity.class));
                    finish();
                    overridePendingTransition(17432576, 17432577);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        SaveDataBeforeNavigation();
        Common.ToDoListEdit = false;
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ToDoActivity.class));
        finish();
        overridePendingTransition(17432576, 17432577);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_del, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            SaveDataBeforeNavigation();
            Common.ToDoListEdit = false;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ToDoActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        } else if (itemId == R.id.action_menu_del) {
            deleteToDo();
        } else if (itemId == R.id.action_menu_edit) {
            SaveDataBeforeNavigation();
            SecurityLocksCommon.IsAppDeactive = false;
            Common.ToDoListEdit = true;
            startActivity(new Intent(this, AddToDoActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
        return super.onOptionsItemSelected(menuItem);
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
}