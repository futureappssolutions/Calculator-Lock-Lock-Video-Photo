package com.calculator.vaultlocker.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.calculator.vaultlocker.Model.PhotoAlbum;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumDAL {
    Context con;
    SQLiteDatabase database;
    DatabaseHelper helper;

    public PhotoAlbumDAL(Context context) {
        this.helper = new DatabaseHelper(context);
        this.con = context;
    }

    public void OpenRead() throws SQLException {
        this.database = this.helper.getReadableDatabase();
    }

    public void OpenWrite() throws SQLException {
        this.database = this.helper.getWritableDatabase();
    }

    public void close() {
        this.database.close();
    }

    public void AddPhotoAlbum(PhotoAlbum photoAlbum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("album_name", photoAlbum.getAlbumName());
        contentValues.put("fl_album_location", photoAlbum.getAlbumLocation());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        contentValues.put("SortBy", 0);
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.insert("tbl_photo_albums", null, contentValues);
    }

    public List<PhotoAlbum> GetAlbums() {
        ArrayList<PhotoAlbum> arrayList = new ArrayList<>();
        PhotoDAL photoDAL = new PhotoDAL(this.con);
        photoDAL.OpenRead();
        String str = "SELECT * FROM tbl_photo_albums Where IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount + " ORDER BY _id";

        Cursor rawQuery = this.database.rawQuery(str, null);
        while (rawQuery.moveToNext()) {
            PhotoAlbum photoAlbum = new PhotoAlbum();
            photoAlbum.setId(rawQuery.getInt(0));
            photoAlbum.setAlbumName(rawQuery.getString(1));
            photoAlbum.setAlbumLocation(rawQuery.getString(2));
            photoAlbum.set_modifiedDateTime(rawQuery.getString(6));
            photoAlbum.setPhotoCount(photoDAL.GetPhotoCountByAlbumId(rawQuery.getInt(0)));
            arrayList.add(photoAlbum);
        }
        rawQuery.close();
        photoDAL.close();
        return arrayList;
    }

    public int GetSortByAlbumId(int i) {
        Cursor rawQuery = this.database.rawQuery("SELECT SortBy FROM tbl_photo_albums where _id = " + i, null);
        int i2 = 0;
        while (rawQuery.moveToNext()) {
            i2 = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i2;
    }

    public PhotoAlbum GetAlbum(String str) {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_photo_albums where album_name = '" + str + "' AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            photoAlbum.setId(rawQuery.getInt(0));
            photoAlbum.setAlbumName(rawQuery.getString(1));
            photoAlbum.setAlbumLocation(rawQuery.getString(2));
            photoAlbum.set_modifiedDateTime(rawQuery.getString(6));
        }
        rawQuery.close();
        return photoAlbum;
    }

    public PhotoAlbum GetAlbumById(String str) {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_photo_albums where _id =" + str, null);
        while (rawQuery.moveToNext()) {
            photoAlbum.setId(rawQuery.getInt(0));
            photoAlbum.setAlbumName(rawQuery.getString(1));
            photoAlbum.setAlbumLocation(rawQuery.getString(2));
            photoAlbum.set_modifiedDateTime(rawQuery.getString(6));
        }
        rawQuery.close();
        return photoAlbum;
    }

    public String GetAlbumName(String str) {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_photo_albums where _id =" + str, null);
        String str2 = "My Photos";
        while (rawQuery.moveToNext()) {
            str2 = rawQuery.getString(1);
        }
        rawQuery.close();
        return str2;
    }

    public void UpdateAlbumName(PhotoAlbum photoAlbum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("album_name", photoAlbum.getAlbumName());
        contentValues.put("fl_album_location", photoAlbum.getAlbumLocation());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.update("tbl_photo_albums", contentValues, "_id = ?", new String[]{String.valueOf(photoAlbum.getId())});
        close();
        PhotoDAL photoDAL = new PhotoDAL(this.con);
        photoDAL.OpenWrite();
        photoDAL.UpdateAlbumPhotoLocation(photoAlbum.getId(), photoAlbum.getAlbumLocation());
        photoDAL.close();
    }

    public void UpdateAlbumLocation(int i, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("fl_album_location", str);
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.update("tbl_photo_albums", contentValues, "_id = ?", new String[]{String.valueOf(i)});
        close();
    }

    public void DeleteAlbumById(int i) {
        this.database.delete("tbl_photo_albums", "_id = ?", new String[]{String.valueOf(i)});
        close();
        PhotoDAL photoDAL = new PhotoDAL(this.con);
        photoDAL.OpenWrite();
        photoDAL.DeletePhotoByAlbumId(i);
        photoDAL.close();
    }

    public int GetLastAlbumId() {
        Cursor rawQuery = this.database.rawQuery("SELECT _id FROM tbl_photo_albums WHERE _id = (SELECT MAX(_id)  FROM tbl_photo_albums)", null);
        int i = 0;
        while (rawQuery.moveToNext()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i;
    }

    public int IfAlbumNameExistReturnId(String str) {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_photo_albums where album_name ='" + str + "'", null);
        int i = 0;
        while (rawQuery.moveToNext()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i;
    }
}
