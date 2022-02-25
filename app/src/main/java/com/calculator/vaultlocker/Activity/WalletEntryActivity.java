package com.calculator.vaultlocker.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.calculator.vaultlocker.DB.WalletCategoriesDAL;
import com.calculator.vaultlocker.DB.WalletEntriesDAL;
import com.calculator.vaultlocker.Model.WalletCategoriesFieldPojo;
import com.calculator.vaultlocker.Model.WalletCategoriesFileDB_Pojo;
import com.calculator.vaultlocker.Model.WalletCategoriesPojo;
import com.calculator.vaultlocker.Model.WalletEntryFileDB_Pojo;
import com.calculator.vaultlocker.Model.WalletEntryItemsPojo;
import com.calculator.vaultlocker.Model.WalletEntryPojo;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.XMLParser.EntryReadXml;
import com.calculator.vaultlocker.XMLParser.WalletEntryWriteXml;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.wallet.WalletCommon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WalletEntryActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {
    Constants constants;
    List<WalletEntryItemsPojo> entryFieldItemsList;
    WalletCategoriesFieldPojo entryFieldsModel;
    ImageView iv_categoryIcon;
    LinearLayout ll_focus;
    TableLayout tableLayout;
    TextView toolbar_title;
    TextView tv_categoryName;
    WalletCategoriesDAL walletCategoriesDAL;
    WalletCategoriesPojo walletCategoriesPojo;
    WalletCommon walletCommon;
    WalletEntriesDAL walletEntriesDAL;
    int entryAction = 0;
    String entryName = "";
    WalletEntryPojo entryPojo = null;
    String filePath = "";
    private SensorManager sensorManager;
    private Toolbar toolbar;

    @Override // com.calculator.vaultlocker.panicswitch.AccelerometerListener
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wallet_entry);
        getWindow().setFlags(1024, 1024);
        this.toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.tableLayout = (TableLayout) findViewById(R.id.tableLayoutNewEntryFields);
        this.iv_categoryIcon = (ImageView) findViewById(R.id.iv_categoryIcon);
        this.ll_focus = (LinearLayout) findViewById(R.id.ll_focus);
        this.tv_categoryName = (TextView) findViewById(R.id.tv_categoryName);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.walletCommon = new WalletCommon();
        this.constants = new Constants();
        this.walletEntriesDAL = new WalletEntriesDAL(this);
        this.walletCategoriesDAL = new WalletCategoriesDAL(this);
        setSupportActionBar(this.toolbar);
        this.toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        getSupportActionBar().setTitle(WalletCommon.walletCurrentCategoryName);
        SecurityLocksCommon.IsAppDeactive = true;
        if (WalletCommon.walletCurrentEntryName.equals("")) {
            this.entryAction = entryActions.New.ordinal();
        } else {
            this.entryAction = entryActions.View.ordinal();
            getCurrentEntryFromXml();
        }
        this.walletCategoriesPojo = this.walletCommon.getCurrentCategoryData(this, WalletCommon.walletCurrentCategoryName);
        try {
            TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.wallet_drawables_list);
            this.iv_categoryIcon.setImageResource(obtainTypedArray.getResourceId(this.walletCategoriesPojo.getCategoryIconIndex(), -1));
            obtainTypedArray.recycle();
        } catch (Exception unused) {
        }
        this.tv_categoryName.setText(WalletCommon.walletCurrentCategoryName);
        updateTableLayout();
    }

    public void getCurrentEntryFromXml() {
        String str = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.WALLET + WalletCommon.walletCurrentCategoryName + File.separator + StorageOptionsCommon.ENTRY + WalletCommon.walletCurrentEntryName + StorageOptionsCommon.WALLET_ENTRY_FILE_EXTENSION;
        this.filePath = str;
        this.entryPojo = EntryReadXml.parseXML(this, str);
        Log.e("entryPojo", "" + this.entryPojo);
    }

    public void updateTableLayout() {
        if (this.entryAction == entryActions.New.ordinal()) {
            initLayout(this.walletCategoriesPojo.getCategoryFields());
        } else {
            WalletEntryPojo walletEntryPojo = this.entryPojo;
            if (walletEntryPojo != null) {
                initLayout(walletEntryPojo.getFields());
            }
        }
        this.ll_focus.requestFocus();
    }

    public void initLayout(List<WalletCategoriesFieldPojo> list) {
        this.entryFieldItemsList = new ArrayList();
        int i = 0;
        for (WalletCategoriesFieldPojo walletCategoriesFieldPojo : list) {
            addNewRowInTableLayout(walletCategoriesFieldPojo, i);
            i++;
        }
        this.tableLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom));
    }

    public void addNewRowInTableLayout(WalletCategoriesFieldPojo walletCategoriesFieldPojo, int i) {
        InputFilter[] inputFilterArr = {new InputFilter.LengthFilter(30)};
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-1, -1, 1.0f);
        TableRow tableRow = new TableRow(getApplicationContext());
        tableRow.setLayoutParams(layoutParams);
        View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_wallet_entry_list, (ViewGroup) null);
        tableRow.addView(inflate, layoutParams);
        WalletEntryItemsPojo walletEntryItemsPojo = new WalletEntryItemsPojo();
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.leftMargin = 15;
        layoutParams2.rightMargin = 15;
        TextView textView = (TextView) inflate.findViewById(R.id.tv_entryTitle);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_entryValue);
        CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.cb_entryIsSecured);
        textView.setTag(Integer.valueOf(i));
        editText.setTag(Integer.valueOf(i));
        textView.setText(walletCategoriesFieldPojo.getFieldName());
        editText.setInputType(65536);
        editText.setFilters(inputFilterArr);
        editText.setLayoutParams(layoutParams2);
        if (this.entryAction != entryActions.View.ordinal()) {
            editText.setBackgroundResource(R.drawable.white_et_curve_edge);
            editText.setText(walletCategoriesFieldPojo.getFieldValue());
            editText.setPadding(10, 10, 10, 10);
        } else if (walletCategoriesFieldPojo.getFieldValue().equals("")) {
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
        } else {
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setBackgroundColor(getResources().getColor(17170445));
            editText.setText(walletCategoriesFieldPojo.getFieldValue());
            if (walletCategoriesFieldPojo.isSecured()) {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.calculator.vaultlocker.Activity.WalletEntryActivity.1
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        if (compoundButton.isChecked()) {
                            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                });
                checkBox.setChecked(walletCategoriesFieldPojo.isSecured());
            }
        }
        walletEntryItemsPojo.setTextView(textView);
        walletEntryItemsPojo.setEditText(editText);
        walletEntryItemsPojo.setSecured(walletCategoriesFieldPojo.isSecured());
        this.entryFieldItemsList.add(walletEntryItemsPojo);
        this.tableLayout.addView(tableRow);
    }

    public void clearData() {
        this.entryFieldItemsList = null;
        this.tableLayout.removeAllViews();
    }

    public void addEntryToDatabase(String str, boolean z) {
        String currentDate = this.walletCommon.getCurrentDate();
        try {
            WalletEntryFileDB_Pojo walletEntryFileDB_Pojo = new WalletEntryFileDB_Pojo();
            walletEntryFileDB_Pojo.setCategoryId(WalletCommon.WalletCurrentCategoryId);
            walletEntryFileDB_Pojo.setEntryFileName(str);
            walletEntryFileDB_Pojo.setEntryFileLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.WALLET + WalletCommon.walletCurrentCategoryName + File.separator + StorageOptionsCommon.ENTRY + str + StorageOptionsCommon.WALLET_ENTRY_FILE_EXTENSION);
            walletEntryFileDB_Pojo.setEntryFileCreatedDate(currentDate);
            walletEntryFileDB_Pojo.setEntryFileModifiedDate(currentDate);
            walletEntryFileDB_Pojo.setCategoryFileIconIndex(this.walletCategoriesPojo.getCategoryIconIndex());
            walletEntryFileDB_Pojo.setEntriesFileSortBy(0);
            walletEntryFileDB_Pojo.setEntryFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
            if (z) {
                WalletEntriesDAL walletEntriesDAL = this.walletEntriesDAL;
                this.constants.getClass();
                walletEntriesDAL.updateEntryInDatabase(walletEntryFileDB_Pojo, "WalletEntryFileId", String.valueOf(walletEntryFileDB_Pojo.getEntryFileId()));
            } else {
                this.walletEntriesDAL.addWalletEntriesInfoInDatabase(walletEntryFileDB_Pojo);
            }
            new WalletCategoriesFileDB_Pojo();
            WalletCategoriesDAL walletCategoriesDAL = this.walletCategoriesDAL;
            StringBuilder sb = new StringBuilder();
            this.constants.getClass();
            sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletCategories WHERE ");
            this.constants.getClass();
            sb.append("WalletCategoriesFileIsDecoy");
            sb.append(" = ");
            sb.append(SecurityLocksCommon.IsFakeAccount);
            sb.append(" AND ");
            this.constants.getClass();
            sb.append("WalletCategoriesFileId");
            sb.append(" = ");
            sb.append(walletEntryFileDB_Pojo.getCategoryId());
            WalletCategoriesFileDB_Pojo categoryInfoFromDatabase = walletCategoriesDAL.getCategoryInfoFromDatabase(sb.toString());
            categoryInfoFromDatabase.setCategoryFileModifiedDate(currentDate);
            WalletCategoriesDAL walletCategoriesDAL2 = this.walletCategoriesDAL;
            this.constants.getClass();
            walletCategoriesDAL2.updateCategoryFromDatabase(categoryInfoFromDatabase, "WalletCategoriesFileId", String.valueOf(walletEntryFileDB_Pojo.getCategoryId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveEntry() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        String capitalizeFirstLetter = this.walletCommon.capitalizeFirstLetter(this.entryFieldItemsList.get(0).getEditText().getText().toString());
        this.entryName = capitalizeFirstLetter;
        if (capitalizeFirstLetter.length() <= 0) {
            Toast.makeText(this, (int) R.string.entry_name_enter, Toast.LENGTH_SHORT).show();
            return false;
        } else if (this.walletCommon.isNoSpecialCharsInName(this.entryName)) {
            for (WalletEntryItemsPojo walletEntryItemsPojo : this.entryFieldItemsList) {
                WalletCategoriesFieldPojo walletCategoriesFieldPojo = new WalletCategoriesFieldPojo();
                this.entryFieldsModel = walletCategoriesFieldPojo;
                walletCategoriesFieldPojo.setFieldName(this.walletCommon.capitalizeFirstLetter(walletEntryItemsPojo.getTextView().getText().toString()));
                this.entryFieldsModel.setFieldValue(walletEntryItemsPojo.getEditText().getText().toString());
                this.entryFieldsModel.setSecured(walletEntryItemsPojo.isSecured());
                arrayList.add(this.entryFieldsModel);
            }
            WalletEntryPojo walletEntryPojo = new WalletEntryPojo();
            walletEntryPojo.setCategoryName(WalletCommon.walletCurrentCategoryName);
            walletEntryPojo.setEntryName(this.entryName);
            walletEntryPojo.setFields(arrayList);
            WalletEntriesDAL walletEntriesDAL = this.walletEntriesDAL;
            StringBuilder sb = new StringBuilder();
            this.constants.getClass();
            sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletEntries WHERE ");
            this.constants.getClass();
            sb.append("WalletEntryFileIsDecoy");
            sb.append(" = ");
            sb.append(SecurityLocksCommon.IsFakeAccount);
            sb.append(" AND ");
            this.constants.getClass();
            sb.append("WalletEntryFileName");
            sb.append(" = '");
            sb.append(this.entryName);
            sb.append("'");
            boolean IsWalletEntryAlreadyExist = walletEntriesDAL.IsWalletEntryAlreadyExist(sb.toString());
            if (this.entryAction == entryActions.New.ordinal()) {
                if (!IsWalletEntryAlreadyExist) {
                    z = WalletEntryWriteXml.write(walletEntryPojo, this, true);
                } else {
                    Toast.makeText(this, (int) R.string.entry_exists, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (WalletCommon.walletCurrentEntryName.equals(this.entryName) || !IsWalletEntryAlreadyExist) {
                z = WalletEntryWriteXml.write(walletEntryPojo, this, false);
            } else {
                Toast.makeText(this, (int) R.string.entry_exists, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (z) {
                Toast.makeText(this, (int) R.string.entry_saved, Toast.LENGTH_SHORT).show();
                addEntryToDatabase(this.entryName, IsWalletEntryAlreadyExist);
                return true;
            }
            Toast.makeText(this, (int) R.string.entry_not_saved, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(this, (int) R.string.special_characters_not_allowed, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean deleteEntry(int i, String str) {
        File file = new File(str);
        try {
            WalletEntriesDAL walletEntriesDAL = this.walletEntriesDAL;
            this.constants.getClass();
            walletEntriesDAL.deleteEntryFromDatabase("WalletEntryFileId", String.valueOf(i));
            if (file.exists()) {
                return file.delete();
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public void ShowDeleteDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        ((TextView) dialog.findViewById(R.id.tvmessagedialogtitle)).setText(getResources().getString(R.string.delete_entry));
        ((LinearLayout) dialog.findViewById(R.id.ll_Ok)).setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.Activity.WalletEntryActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WalletEntryActivity walletEntryActivity = WalletEntryActivity.this;
                walletEntryActivity.entryName = walletEntryActivity.walletCommon.capitalizeFirstLetter(WalletEntryActivity.this.entryFieldItemsList.get(0).getEditText().getText().toString());
                StringBuilder sb = new StringBuilder();
                WalletEntryActivity.this.constants.getClass();
                sb.append("SELECT \t\tWalletEntryFileId FROM  TableWalletEntries WHERE ");
                WalletEntryActivity.this.constants.getClass();
                sb.append("WalletEntryFileName");
                sb.append(" = '");
                sb.append(WalletEntryActivity.this.entryName);
                sb.append("'");
                int GetWalletEntriesIntegerEntity = WalletEntryActivity.this.walletEntriesDAL.GetWalletEntriesIntegerEntity(sb.toString());
                WalletEntryActivity walletEntryActivity2 = WalletEntryActivity.this;
                if (walletEntryActivity2.deleteEntry(GetWalletEntriesIntegerEntity, walletEntryActivity2.filePath)) {
                    Toast.makeText(WalletEntryActivity.this, (int) R.string.entry_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WalletEntryActivity.this, (int) R.string.entry_not_deleted, Toast.LENGTH_SHORT).show();
                }
                WalletEntryActivity.this.closeActivity();
                dialog.dismiss();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ll_Cancel)).setOnClickListener(new View.OnClickListener() { // from class: com.calculator.vaultlocker.Activity.WalletEntryActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void closeActivity() {
        if (this.entryAction == entryActions.Edit.ordinal()) {
            this.entryAction = entryActions.View.ordinal();
            clearData();
            invalidateOptionsMenu();
            updateTableLayout();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, WalletEntriesActivity.class));
        finish();
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.entryAction == entryActions.New.ordinal() || this.entryAction == entryActions.Edit.ordinal()) {
            getMenuInflater().inflate(R.menu.menu_save, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit_del, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            closeActivity();
        } else if (itemId == R.id.action_menu_del) {
            ShowDeleteDialog();
        } else if (itemId == R.id.action_menu_edit) {
            this.entryAction = entryActions.Edit.ordinal();
            invalidateOptionsMenu();
            clearData();
            updateTableLayout();
        } else if (itemId == R.id.action_save) {
            if (saveEntry()) {
                WalletCommon.walletCurrentEntryName = this.entryName;
                this.entryAction = entryActions.View.ordinal();
                clearData();
                invalidateOptionsMenu();
                getCurrentEntryFromXml();
                updateTableLayout();
            }
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.ll_focus.getWindowToken(), 0);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        closeActivity();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        SensorManager sensorManager = this.sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override // com.calculator.vaultlocker.panicswitch.AccelerometerListener
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }


    public enum entryActions {
        New,
        View,
        Edit
    }
}
