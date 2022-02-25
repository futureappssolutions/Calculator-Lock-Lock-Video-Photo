package com.calculator.vaultlocker.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.calculator.vaultlocker.Activity.PhotosAlbumActivty;
import com.calculator.vaultlocker.Model.VideoAlbum;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;


public class VideoAlbumDAL {
    Context con;
    SQLiteDatabase database;
    DatabaseHelper helper;

    public VideoAlbumDAL(Context context) {
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

    public void AddVideoAlbum(VideoAlbum videoAlbum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("album_name", videoAlbum.getAlbumName());
        contentValues.put("fl_album_location", videoAlbum.getAlbumLocation());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        contentValues.put("SortBy", 0);
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.insert("tbl_video_albums", null, contentValues);
    }

    public List<VideoAlbum> GetAlbums(int i) {
        ArrayList<VideoAlbum> arrayList = new ArrayList<>();
        VideoDAL videoDAL = new VideoDAL(this.con);
        videoDAL.OpenRead();
        String str = "SELECT * FROM tbl_video_albums Where IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount + " ORDER BY _id";
        if (PhotosAlbumActivty.SortBy.Time.ordinal() == i) {
            str = "SELECT * FROM tbl_video_albums Where IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount + " ORDER BY ModifiedDateTime DESC";
        } else if (PhotosAlbumActivty.SortBy.Name.ordinal() == i) {
            str = "SELECT * FROM tbl_video_albums Where IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount + " ORDER BY album_name COLLATE NOCASE ASC";
        }
        Cursor rawQuery = this.database.rawQuery(str, null);
        while (rawQuery.moveToNext()) {
            VideoAlbum videoAlbum = new VideoAlbum();
            videoAlbum.setId(rawQuery.getInt(0));
            videoAlbum.setAlbumName(rawQuery.getString(1));
            videoAlbum.setAlbumLocation(rawQuery.getString(2));
            videoAlbum.set_modifiedDateTime(rawQuery.getString(6));
            videoAlbum.setVideoCount(videoDAL.GetVideoCountByAlbumId(rawQuery.getInt(0)));
            arrayList.add(videoAlbum);
        }
        rawQuery.close();
        videoDAL.close();
        return arrayList;
    }

    public int GetSortByAlbumId(int i) {
        Cursor rawQuery = this.database.rawQuery("SELECT SortBy FROM tbl_video_albums where _id = " + i, null);
        int i2 = 0;
        while (rawQuery.moveToNext()) {
            i2 = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i2;
    }

    public VideoAlbum GetAlbum(String str) {
        VideoAlbum videoAlbum = new VideoAlbum();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_video_albums where album_name = '" + str + "' AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            videoAlbum.setId(rawQuery.getInt(0));
            videoAlbum.setAlbumName(rawQuery.getString(1));
            videoAlbum.setAlbumLocation(rawQuery.getString(2));
            videoAlbum.set_modifiedDateTime(rawQuery.getString(6));
        }
        rawQuery.close();
        return videoAlbum;
    }

    public VideoAlbum GetAlbumById(int i) {
        VideoAlbum videoAlbum = new VideoAlbum();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_video_albums where _id = '" + i + "' AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            videoAlbum.setId(rawQuery.getInt(0));
            videoAlbum.setAlbumName(rawQuery.getString(1));
            videoAlbum.setAlbumLocation(rawQuery.getString(2));
            videoAlbum.set_modifiedDateTime(rawQuery.getString(6));
        }
        rawQuery.close();
        return videoAlbum;
    }

    public VideoAlbum GetAlbumById(String str) {
        VideoAlbum videoAlbum = new VideoAlbum();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_video_albums where _id = '" + str + "' AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            videoAlbum.setId(rawQuery.getInt(0));
            videoAlbum.setAlbumName(rawQuery.getString(1));
            videoAlbum.setAlbumLocation(rawQuery.getString(2));
            videoAlbum.set_modifiedDateTime(rawQuery.getString(6));
        }
        rawQuery.close();
        return videoAlbum;
    }

    public String GetAlbumName(String str) {
        String str2 = "My Videos";
        String sb = "SELECT * FROM tbl_video_albums where album_name = '" +
                str2 +
                "' AND IsFakeAccount = " +
                SecurityLocksCommon.IsFakeAccount;
        Cursor rawQuery = this.database.rawQuery(sb, null);
        while (rawQuery.moveToNext()) {
            str2 = rawQuery.getString(1);
        }
        rawQuery.close();
        return str2;
    }

    public void UpdateAlbumName(VideoAlbum videoAlbum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("album_name", videoAlbum.getAlbumName());
        contentValues.put("fl_album_location", videoAlbum.getAlbumLocation());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.update("tbl_video_albums", contentValues, "_id = ?", new String[]{String.valueOf(videoAlbum.getId())});
        close();
        VideoDAL videoDAL = new VideoDAL(this.con);
        videoDAL.OpenWrite();
        videoDAL.UpdateAlbumVideoLocation(videoAlbum.getId(), videoAlbum.getAlbumLocation());
        videoDAL.close();
    }

    public void DeleteAlbumById(int i) {
        this.database.delete("tbl_video_albums", "_id = ?", new String[]{String.valueOf(i)});
        close();
        VideoDAL videoDAL = new VideoDAL(this.con);
        videoDAL.OpenWrite();
        videoDAL.DeleteVideoByAlbumId(i);
        videoDAL.close();
    }

    public int GetLastAlbumId() {
        Cursor rawQuery = this.database.rawQuery("SELECT _id FROM tbl_video_albums WHERE _id = (SELECT MAX(_id)  FROM tbl_video_albums)", null);
        int i = 0;
        while (rawQuery.moveToNext()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i;
    }

    public int IfAlbumNameExistReturnId(String str) {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_video_albums where album_name ='" + str + "'", null);
        int i = 0;
        while (rawQuery.moveToNext()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i;
    }
}
