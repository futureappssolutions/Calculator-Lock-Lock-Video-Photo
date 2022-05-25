package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerListener;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.ToDoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.ToDoReadFromXML;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoRowViewsPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoTask;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.ToDoWriteInXML;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import com.facebook.ads.NativeAdLayout;
import com.flask.colorpicker.ColorPickerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddToDoActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    private Constants constants;
    private ToDoDAL dal;
    private EditText et_ToDoTitle;
    private LayoutInflater inflater;
    private AppCompatButton ll_addTask;
    private ScrollView ll_main;
    private TableLayout tl_TodoTasks;
    private ToDoPojo toDo;
    private String tempTitle = "";
    private String toDoColor = "#33aac0ff";
    private String toDoTitle = "";
    private SensorManager sensorManager;
    private ToDoAddOnClickListeners onClickListener;
    private List<ToDoRowViewsPojo> rowViewsList;
    private boolean hasModified = false;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_todo_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tl_TodoTasks = findViewById(R.id.tl_TodoTasks);
        ll_addTask = findViewById(R.id.ll_addTask);
        ll_main = findViewById(R.id.ll_main);
        et_ToDoTitle = findViewById(R.id.et_ToDoTitle);


        FrameLayout fl_native = findViewById(R.id.fl_native);
        Advertisement.showNativeAds(AddToDoActivity.this, fl_native);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        dal = new ToDoDAL(this);
        rowViewsList = new ArrayList<>();
        onClickListener = new ToDoAddOnClickListeners();
        toDo = new ToDoPojo();
        constants = new Constants();

        SecurityLocksCommon.IsAppDeactive = true;

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        ll_addTask.setOnClickListener(onClickListener);

        try {
            if (Common.ToDoListEdit) {
                fillDataInToDoList();
                return;
            }
            addNewRow("", false);
            constants.getClass();
            tempTitle = "To do " + (dal.GetToDoDbFileIntegerEntity("SELECT \t    COUNT(*)  \t\t\t\t\t\t   FROM  TableToDo") + 1) + "";
            et_ToDoTitle.setHint(tempTitle);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void fillDataInToDoList() {
        ToDoReadFromXML toDoReadFromXML = new ToDoReadFromXML();
        toDo = toDoReadFromXML.ReadToDoList(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TODOLIST + Common.ToDoListName + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        if (toDo != null) {
            try {
                et_ToDoTitle.setText(toDo.getTitle());
                toDoColor = toDo.getTodoColor();
                for (ToDoTask next : toDo.getToDoList()) {
                    addNewRow(next.getToDo(), next.isChecked());
                }
                ll_main.setBackgroundColor(Color.parseColor(toDoColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addNewRow(String str, boolean z) {
        TableRow tableRow = new TableRow(getApplicationContext());
        ToDoRowViewsPojo toDoRowViewsPojo = new ToDoRowViewsPojo();
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -1, 1.0f);
        tableRow.setLayoutParams(layoutParams);
        View inflate = inflater.inflate(R.layout.tablerow_todo_add, null);
        tableRow.addView(inflate, layoutParams);
        tl_TodoTasks.addView(tableRow);

        ImageView imageView = inflate.findViewById(R.id.iv_RowUp);
        ImageView imageView2 = inflate.findViewById(R.id.iv_RowDown);
        ImageView imageView3 = inflate.findViewById(R.id.iv_RowDel);

        InputFilter[] inputFilterArr = {new InputFilter.LengthFilter(30)};
        EditText editText = inflate.findViewById(R.id.et_text);
        editText.requestFocus();
        editText.setInputType(65536);
        editText.setImeOptions(5);
        editText.setFilters(inputFilterArr);

        imageView.setOnClickListener(onClickListener);
        imageView2.setOnClickListener(onClickListener);
        imageView3.setOnClickListener(onClickListener);
        editText.setOnClickListener(onClickListener);

        toDoRowViewsPojo.setEt_text(editText);
        toDoRowViewsPojo.setIv_rowUp(imageView);
        toDoRowViewsPojo.setIv_rowDown(imageView2);
        toDoRowViewsPojo.setIv_rowDel(imageView3);

        if (Common.ToDoListEdit) {
            editText.setText(str);
            toDoRowViewsPojo.setChecked(z);
        }
        rowViewsList.add(toDoRowViewsPojo);
    }

    public TableLayout swapDownTableRow(View view) {
        TableRow tableRow = (TableRow) ((LinearLayout) ((LinearLayout) view.getParent()).getParent()).getParent();
        int indexOfChild = tl_TodoTasks.indexOfChild(tableRow);
        int i = indexOfChild + 1;
        TableRow tableRow2 = (TableRow) tl_TodoTasks.getChildAt(i);
        try {
            if (indexOfChild != tl_TodoTasks.getChildCount() - 1) {
                tl_TodoTasks.removeView(tableRow);
                tl_TodoTasks.addView(tableRow, i);
                tableRow.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_row_up));
                tl_TodoTasks.removeView(tableRow2);
                tl_TodoTasks.addView(tableRow2, indexOfChild);
                tableRow2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_row_down));
                return tl_TodoTasks;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rowViewsList.remove(indexOfChild);
            rowViewsList.add(i, rowViewsList.get(indexOfChild));
        }
        return tl_TodoTasks;
    }

    public TableLayout swapUpTableRow(View view) {
        TableRow tableRow = (TableRow) ((LinearLayout) ((LinearLayout) view.getParent()).getParent()).getParent();
        int indexOfChild = tl_TodoTasks.indexOfChild(tableRow);
        int i = indexOfChild - 1;
        TableRow tableRow2 = (TableRow) tl_TodoTasks.getChildAt(i);
        if (indexOfChild > 0) {
            try {
                tl_TodoTasks.removeView(tableRow);
                tl_TodoTasks.addView(tableRow, i);
                tableRow.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_row_down));
                tl_TodoTasks.removeView(tableRow2);
                tl_TodoTasks.addView(tableRow2, indexOfChild);
                tableRow2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_row_up));
                return tl_TodoTasks;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rowViewsList.remove(indexOfChild);
                rowViewsList.add(i, rowViewsList.get(indexOfChild));
            }
        }
        return tl_TodoTasks;
    }

    public void SaveToDoList() {
        boolean z;
        String currentDateTime2 = Utilities.getCurrentDateTime2();
        toDo.setTitle(toDoTitle);
        toDo.setTodoColor(toDoColor);
        toDo.setDateModified(currentDateTime2);

        ArrayList<ToDoTask> arrayList = new ArrayList<>();
        for (ToDoRowViewsPojo toDoRowViewsPojo : rowViewsList) {
            ToDoTask toDoTask = new ToDoTask();
            toDoTask.setToDo(toDoRowViewsPojo.getEt_text().getText().toString());
            toDoTask.setChecked(toDoRowViewsPojo.isChecked());
            arrayList.add(toDoTask);
            if (toDoRowViewsPojo.getEt_text().getText().toString().trim().equals("")) {
                Toast.makeText(this, "Can't save empty field", Toast.LENGTH_SHORT).show();
                Utilities.hideKeyboard(ll_addTask, this);
                return;
            }
        }
        toDo.setToDoList(arrayList);

        ToDoWriteInXML toDoWriteInXML = new ToDoWriteInXML();
        if (Common.ToDoListEdit) {
            z = toDoWriteInXML.saveToDoList(this, toDo, toDo.getTitle(), Common.ToDoListEdit);
        } else {
            toDo.setDateCreated(currentDateTime2);
            z = toDoWriteInXML.saveToDoList(this, toDo, toDoTitle, Common.ToDoListEdit);
        }

        if (z) {
            ToDoDB_Pojo toDoDB_Pojo = new ToDoDB_Pojo();
            toDoDB_Pojo.setToDoFileName(toDoTitle);
            toDoDB_Pojo.setToDoFileLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TODOLIST + toDoTitle + StorageOptionsCommon.NOTES_FILE_EXTENSION);
            toDoDB_Pojo.setToDoFileColor(toDoColor);
            toDoDB_Pojo.setToDoFileCreatedDate(currentDateTime2);
            toDoDB_Pojo.setToDoFileModifiedDate(currentDateTime2);
            toDoDB_Pojo.setToDoFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
            toDoDB_Pojo.setToDoFileTask1(arrayList.get(0).getToDo());
            toDoDB_Pojo.setToDoFileTask1IsChecked(arrayList.get(0).isChecked());
            if (arrayList.size() >= 2) {
                toDoDB_Pojo.setToDoFileTask2(arrayList.get(1).getToDo());
                toDoDB_Pojo.setToDoFileTask2IsChecked(arrayList.get(1).isChecked());
            }

            if (Common.ToDoListEdit) {
                constants.getClass();
                dal.updateToDoFileInfoInDatabase(toDoDB_Pojo, "ToDoId", String.valueOf(Common.ToDoListId));
            } else {
                dal.addToDoInfoInDatabase(toDoDB_Pojo);
            }

            StringBuilder sb = new StringBuilder();
            constants.getClass();
            sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableToDo WHERE ");
            constants.getClass();
            sb.append("ToDoName");
            sb.append("='");
            sb.append(toDoTitle);
            sb.append("'");
            Common.ToDoListId = dal.GetToDoDbFileIntegerEntity(sb.toString());
            if (Common.ToDoListEdit) {
                Common.ToDoListEdit = false;
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(this, ViewToDoActivity.class));
                finish();
                overridePendingTransition(17432576, 17432577);
            } else {
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(this, ToDoActivity.class));
                finish();
                overridePendingTransition(17432576, 17432577);
            }
        } else {
            Toast.makeText(this, "Failed to Save ToDo", Toast.LENGTH_SHORT).show();
        }
    }

    public void setToDoColor() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.dialog_note_color_picker);
        final ColorPickerView colorPickerView = dialog.findViewById(R.id.color_picker_view);
        colorPickerView.setAlpha(0.3f);
        colorPickerView.setDensity(4);

        dialog.findViewById(R.id.tv_no).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.tv_yes).setOnClickListener(view -> {
            try {
                int selectedColor = colorPickerView.getSelectedColor();
                String str = "#33" + Integer.toHexString(selectedColor).substring(2);
                ll_main.setBackgroundColor(Color.parseColor(str));
                toDoColor = str;
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void showDiscardDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        ((TextView) dialog.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.discard_changes);

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (Common.ToDoListEdit) {
                Common.ToDoListEdit = false;
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(AddToDoActivity.this, ViewToDoActivity.class));
                finish();
                overridePendingTransition(17432576, 17432577);
            } else {
                SecurityLocksCommon.IsAppDeactive = false;
                startActivity(new Intent(AddToDoActivity.this, ToDoActivity.class));
                finish();
                overridePendingTransition(17432576, 17432577);
            }
            dialog.cancel();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_color, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            switch (itemId) {
                case R.id.action_menu_add:
                    String trim = et_ToDoTitle.getText().toString().trim();
                    toDoTitle = trim;
                    if (trim.trim().equals("")) {
                        toDoTitle = tempTitle;
                    }
                    if (isNoSpecialCharsInNameExceptBrackets(toDoTitle)) {
                        Common.ToDoListName = toDoTitle;
                        SaveToDoList();

                        break;
                    } else {
                        Toast.makeText(this, "Todo name is incorrect", Toast.LENGTH_SHORT).show();
                        break;
                    }
                case R.id.action_menu_color:
                    setToDoColor();
                    break;
            }
        } else if (hasModified) {
            showDiscardDialog();
        } else if (Common.ToDoListEdit) {
            Common.ToDoListEdit = false;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ViewToDoActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ToDoActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (hasModified) {
            showDiscardDialog();
        } else if (Common.ToDoListEdit) {
            Common.ToDoListEdit = false;
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ViewToDoActivity.class));
            finish();
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ToDoActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
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

    public class ToDoAddOnClickListeners implements View.OnClickListener {
        private ToDoAddOnClickListeners() {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id != R.id.et_text) {
                if (id != R.id.ll_addTask) {
                    switch (id) {
                        case R.id.iv_RowDel:
                            if (rowViewsList.size() != 1) {
                                TableRow tableRow = (TableRow) ((LinearLayout) ((LinearLayout) view.getParent()).getParent()).getParent();
                                try {
                                    rowViewsList.remove(tl_TodoTasks.indexOfChild(tableRow));
                                    tl_TodoTasks.removeView(tableRow);
                                    tl_TodoTasks.requestLayout();
                                    hasModified = true;
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else {
                                return;
                            }
                        case R.id.iv_RowDown:
                            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_pulse));
                            tl_TodoTasks = swapDownTableRow(view);
                            hasModified = true;
                            return;
                        case R.id.iv_RowUp:
                            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_pulse));
                            tl_TodoTasks = swapUpTableRow(view);
                            hasModified = true;
                            return;
                        default:
                    }
                } else {
                    addNewRow("", false);
                    hasModified = true;
                }
            }
        }
    }

    public boolean isNoSpecialCharsInNameExceptBrackets(String str) {
        return Pattern.compile("^[a-zA-Z.0-9() -]+$").matcher(str).matches();
    }
}
