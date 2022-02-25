package com.calculator.vaultlocker.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.calculator.vaultlocker.Model.ToDoDB_Pojo;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.DB.DatabaseHelper;

import java.util.ArrayList;

public class ToDoDAL {
    Constants constants = new Constants();
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;

    public ToDoDAL(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void OpenRead() {
        this.database = this.databaseHelper.getReadableDatabase();
    }

    public void OpenWrite() {
        this.database = this.databaseHelper.getWritableDatabase();
    }

    public void close() {
        this.database.close();
    }

    public void addToDoInfoInDatabase(ToDoDB_Pojo toDoDB_Pojo) {
        OpenRead();
        ContentValues contentValues = new ContentValues();
        this.constants.getClass();
        contentValues.put("ToDoName", toDoDB_Pojo.getToDoFileName());
        this.constants.getClass();
        contentValues.put("ToDoFileLocation", toDoDB_Pojo.getToDoFileLocation());
        this.constants.getClass();
        contentValues.put("ToDoTask1", toDoDB_Pojo.getToDoFileTask1());
        this.constants.getClass();
        contentValues.put("ToDoTask1IsChecked", toDoDB_Pojo.isToDoFileTask1IsChecked());
        this.constants.getClass();
        contentValues.put("ToDoTask2", toDoDB_Pojo.getToDoFileTask2());
        this.constants.getClass();
        contentValues.put("ToDoTask2IsChecked", toDoDB_Pojo.isToDoFileTask2IsChecked());
        this.constants.getClass();
        contentValues.put("ToDoCreatedDate", toDoDB_Pojo.getToDoFileCreatedDate());
        this.constants.getClass();
        contentValues.put("ToDoModifiedDate", toDoDB_Pojo.getToDoFileModifiedDate());
        this.constants.getClass();
        contentValues.put("ToDoColor", toDoDB_Pojo.getToDoFileColor());
        this.constants.getClass();
        contentValues.put("ToDoIsDecoy", toDoDB_Pojo.getToDoFileIsDecoy());
        this.constants.getClass();
        contentValues.put("ToDoFinished", toDoDB_Pojo.isToDoFinished());
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.insert("TableToDo", null, contentValues);
        close();
    }

    @SuppressLint("Range")
    public ArrayList<ToDoDB_Pojo> getAllToDoInfoFromDatabase(String str) {
        OpenRead();
        ArrayList<ToDoDB_Pojo> arrayList = new ArrayList<>();
        Cursor rawQuery = this.database.rawQuery(str, null);
        if (rawQuery.moveToFirst()) {
            do {
                ToDoDB_Pojo toDoDB_Pojo = new ToDoDB_Pojo();
                boolean z = false;
                toDoDB_Pojo.setToDoId(rawQuery.getInt(0));
                toDoDB_Pojo.setToDoFileName(rawQuery.getString(1));
                toDoDB_Pojo.setToDoFileLocation(rawQuery.getString(2));
                toDoDB_Pojo.setToDoFileTask1(rawQuery.getString(3));
                this.constants.getClass();
                toDoDB_Pojo.setToDoFileTask1IsChecked(rawQuery.getInt(rawQuery.getColumnIndex("ToDoTask1IsChecked")) != 0);
                toDoDB_Pojo.setToDoFileTask2(rawQuery.getString(5));
                this.constants.getClass();
                toDoDB_Pojo.setToDoFileTask2IsChecked(rawQuery.getInt(rawQuery.getColumnIndex("ToDoTask2IsChecked")) != 0);
                toDoDB_Pojo.setToDoFileCreatedDate(rawQuery.getString(7));
                toDoDB_Pojo.setToDoFileModifiedDate(rawQuery.getString(8));
                toDoDB_Pojo.setToDoFileColor(rawQuery.getString(9));
                toDoDB_Pojo.setToDoFileIsDecoy(rawQuery.getInt(10));
                this.constants.getClass();
                if (rawQuery.getInt(rawQuery.getColumnIndex("ToDoFinished")) != 0) {
                    z = true;
                }
                toDoDB_Pojo.setToDoFinished(z);
                arrayList.add(toDoDB_Pojo);
            } while (rawQuery.moveToNext());
            close();
            return arrayList;
        }
        close();
        return arrayList;
    }

    public int GetToDoDbFileIntegerEntity(String str) {
        OpenRead();
        Cursor rawQuery = this.database.rawQuery(str, null);
        int i = 0;
        while (rawQuery.moveToNext()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        close();
        return i;
    }

    public boolean IsFileAlreadyExist(String str) {
        OpenRead();
        Cursor rawQuery = this.database.rawQuery(str, null);
        boolean z = false;
        while (rawQuery.moveToNext()) {
            z = true;
        }
        rawQuery.close();
        close();
        return z;
    }

    public void updateToDoFileInfoInDatabase(ToDoDB_Pojo toDoDB_Pojo, String str, String str2) {
        OpenWrite();
        ContentValues contentValues = new ContentValues();
        this.constants.getClass();
        contentValues.put("ToDoName", toDoDB_Pojo.getToDoFileName());
        this.constants.getClass();
        contentValues.put("ToDoFileLocation", toDoDB_Pojo.getToDoFileLocation());
        this.constants.getClass();
        contentValues.put("ToDoTask1", toDoDB_Pojo.getToDoFileTask1());
        this.constants.getClass();
        contentValues.put("ToDoTask1IsChecked", toDoDB_Pojo.isToDoFileTask1IsChecked());
        this.constants.getClass();
        contentValues.put("ToDoTask2", toDoDB_Pojo.getToDoFileTask2());
        this.constants.getClass();
        contentValues.put("ToDoTask2IsChecked", toDoDB_Pojo.isToDoFileTask2IsChecked());
        this.constants.getClass();
        contentValues.put("ToDoModifiedDate", toDoDB_Pojo.getToDoFileModifiedDate());
        this.constants.getClass();
        contentValues.put("ToDoColor", toDoDB_Pojo.getToDoFileColor());
        this.constants.getClass();
        contentValues.put("ToDoIsDecoy", toDoDB_Pojo.getToDoFileIsDecoy());
        this.constants.getClass();
        contentValues.put("ToDoFinished", toDoDB_Pojo.isToDoFinished());
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.update("TableToDo", contentValues, str + " = ? ", new String[]{String.valueOf(str2)});
        close();
    }

    public void updateToDoFileTasksInDatabase(ToDoDB_Pojo toDoDB_Pojo, String str, String str2) {
        OpenWrite();
        ContentValues contentValues = new ContentValues();
        this.constants.getClass();
        contentValues.put("ToDoModifiedDate", toDoDB_Pojo.getToDoFileModifiedDate());
        this.constants.getClass();
        contentValues.put("ToDoTask1", toDoDB_Pojo.getToDoFileTask1());
        this.constants.getClass();
        contentValues.put("ToDoTask2", toDoDB_Pojo.getToDoFileTask2());
        this.constants.getClass();
        contentValues.put("ToDoTask1IsChecked", toDoDB_Pojo.isToDoFileTask1IsChecked());
        this.constants.getClass();
        contentValues.put("ToDoTask2IsChecked", toDoDB_Pojo.isToDoFileTask2IsChecked());
        this.constants.getClass();
        contentValues.put("ToDoFinished", toDoDB_Pojo.isToDoFinished());
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.update("TableToDo", contentValues, str + " = ? ", new String[]{String.valueOf(str2)});
        close();
    }

    public void deleteToDoFileFromDatabase(String str, String str2) {
        OpenWrite();
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.delete("TableToDo", str + " = ? ", new String[]{String.valueOf(str2)});
        close();
    }
}
