package com.calculator.vaultlocker.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.calculator.vaultlocker.Model.NotesFolderDB_Pojo;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.DB.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class NotesFolderDAL {
    Constants constants = new Constants();
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;

    public NotesFolderDAL(Context context) {
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

    public void addNotesFolderInfoInDatabase(NotesFolderDB_Pojo notesFolderDB_Pojo) {
        OpenWrite();
        ContentValues contentValues = new ContentValues();
        this.constants.getClass();
        contentValues.put("NotesFolderName", notesFolderDB_Pojo.getNotesFolderName());
        this.constants.getClass();
        contentValues.put("NotesFolderLocation", notesFolderDB_Pojo.getNotesFolderLocation());
        this.constants.getClass();
        contentValues.put("NotesFolderCreatedDate", notesFolderDB_Pojo.getNotesFolderCreatedDate());
        this.constants.getClass();
        contentValues.put("NotesFolderModifiedDate", notesFolderDB_Pojo.getNotesFolderModifiedDate());
        this.constants.getClass();
        contentValues.put("NotesFolderSortBy", notesFolderDB_Pojo.getNotesFolderFilesSortBy());
        this.constants.getClass();
        contentValues.put("NotesFolderIsDecoy", notesFolderDB_Pojo.getNotesFolderIsDecoy());
        this.constants.getClass();
        contentValues.put("NotesFolderColor", notesFolderDB_Pojo.getNotesFolderColor());
        this.constants.getClass();
        contentValues.put("NotesFolderViewBy", notesFolderDB_Pojo.getNotesFolderFilesViewBy());
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.insert("TableNotesFolder", null, contentValues);
        close();
    }

    @SuppressLint("Range")
    public NotesFolderDB_Pojo getNotesFolderInfoFromDatabase(String str) {
        OpenRead();
        NotesFolderDB_Pojo notesFolderDB_Pojo = new NotesFolderDB_Pojo();
        Cursor rawQuery = this.database.rawQuery(str, null);
        while (rawQuery.moveToNext()) {
            notesFolderDB_Pojo.setNotesFolderId(rawQuery.getInt(0));
            notesFolderDB_Pojo.setNotesFolderName(rawQuery.getString(1));
            notesFolderDB_Pojo.setNotesFolderLocation(rawQuery.getString(2));
            notesFolderDB_Pojo.setNotesFolderCreatedDate(rawQuery.getString(3));
            notesFolderDB_Pojo.setNotesFolderModifiedDate(rawQuery.getString(4));
            notesFolderDB_Pojo.setNotesFolderFilesSortBy(rawQuery.getInt(5));
            this.constants.getClass();
            notesFolderDB_Pojo.setNotesFolderIsDecoy(rawQuery.getInt(rawQuery.getColumnIndex("NotesFolderIsDecoy")));
            this.constants.getClass();
            notesFolderDB_Pojo.setNotesFolderColor(rawQuery.getString(rawQuery.getColumnIndex("NotesFolderColor")));
            this.constants.getClass();
            notesFolderDB_Pojo.setNotesFolderFilesViewBy(rawQuery.getInt(rawQuery.getColumnIndex("NotesFolderViewBy")));
        }
        close();
        return notesFolderDB_Pojo;
    }

    @SuppressLint("Range")
    public List<NotesFolderDB_Pojo> getAllNotesFolderInfoFromDatabase(String str) {
        OpenRead();
        ArrayList<NotesFolderDB_Pojo> arrayList = new ArrayList<>();
        Cursor rawQuery = this.database.rawQuery(str, null);
        if (rawQuery.moveToFirst()) {
            do {
                NotesFolderDB_Pojo notesFolderDB_Pojo = new NotesFolderDB_Pojo();
                notesFolderDB_Pojo.setNotesFolderId(rawQuery.getInt(0));
                notesFolderDB_Pojo.setNotesFolderName(rawQuery.getString(1));
                notesFolderDB_Pojo.setNotesFolderLocation(rawQuery.getString(2));
                notesFolderDB_Pojo.setNotesFolderCreatedDate(rawQuery.getString(3));
                notesFolderDB_Pojo.setNotesFolderModifiedDate(rawQuery.getString(4));
                notesFolderDB_Pojo.setNotesFolderFilesSortBy(rawQuery.getInt(5));
                this.constants.getClass();
                notesFolderDB_Pojo.setNotesFolderIsDecoy(rawQuery.getInt(rawQuery.getColumnIndex("NotesFolderIsDecoy")));
                this.constants.getClass();
                notesFolderDB_Pojo.setNotesFolderColor(rawQuery.getString(rawQuery.getColumnIndex("NotesFolderColor")));
                this.constants.getClass();
                notesFolderDB_Pojo.setNotesFolderFilesViewBy(rawQuery.getInt(rawQuery.getColumnIndex("NotesFolderViewBy")));
                arrayList.add(notesFolderDB_Pojo);
            } while (rawQuery.moveToNext());
            close();
            return arrayList;
        }
        close();
        return arrayList;
    }

    public boolean IsFolderAlreadyExist(String str) {
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

    public void updateNotesFolderFromDatabase(NotesFolderDB_Pojo notesFolderDB_Pojo, String str, String str2) {
        OpenWrite();
        ContentValues contentValues = new ContentValues();
        this.constants.getClass();
        contentValues.put("NotesFolderName", notesFolderDB_Pojo.getNotesFolderName());
        this.constants.getClass();
        contentValues.put("NotesFolderLocation", notesFolderDB_Pojo.getNotesFolderLocation());
        this.constants.getClass();
        contentValues.put("NotesFolderModifiedDate", notesFolderDB_Pojo.getNotesFolderModifiedDate());
        this.constants.getClass();
        contentValues.put("NotesFolderIsDecoy", notesFolderDB_Pojo.getNotesFolderIsDecoy());
        this.constants.getClass();
        contentValues.put("NotesFolderSortBy", notesFolderDB_Pojo.getNotesFolderFilesSortBy());
        this.constants.getClass();
        contentValues.put("NotesFolderColor", notesFolderDB_Pojo.getNotesFolderColor());
        this.constants.getClass();
        contentValues.put("NotesFolderViewBy", notesFolderDB_Pojo.getNotesFolderFilesViewBy());
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.update("TableNotesFolder", contentValues, str + " = ? ", new String[]{String.valueOf(str2)});
        close();
    }

    public void updateNotesFolderLocationInDatabase(NotesFolderDB_Pojo notesFolderDB_Pojo, String str, String str2) {
        OpenWrite();
        ContentValues contentValues = new ContentValues();
        this.constants.getClass();
        contentValues.put("NotesFolderLocation", notesFolderDB_Pojo.getNotesFolderLocation());
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.update("TableNotesFolder", contentValues, str + " = ? ", new String[]{String.valueOf(str2)});
        close();
    }

    public void deleteNotesFolderFromDatabase(String str, String str2) {
        OpenWrite();
        SQLiteDatabase sQLiteDatabase = this.database;
        this.constants.getClass();
        sQLiteDatabase.delete("TableNotesFolder", str + " = ? ", new String[]{String.valueOf(str2)});
        close();
    }
}
