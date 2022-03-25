package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DatabaseHelper;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity.Photos_Gallery_Actitvity;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Video;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Globals;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoDAL {
    Context con;
    SQLiteDatabase database;
    DatabaseHelper helper;

    public VideoDAL(Context context) {
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

    public void AddVideos(Video video) {
        File file = new File(video.getFolderLockVideoLocation());
        ContentValues contentValues = new ContentValues();
        contentValues.put("video_name", video.getVideoName());
        contentValues.put("fl_video_location", video.getFolderLockVideoLocation());
        contentValues.put("original_video_location", video.getOriginalVideoLocation());
        contentValues.put("thumbnail_video_location", video.getthumbnail_video_location());
        contentValues.put("album_id", video.getAlbumId());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        contentValues.put("FileSize", file.length());
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.insert("tbl_videos", null, contentValues);
    }

    public List<Video> GetVideos() {
        ArrayList<Video> arrayList = new ArrayList<>();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_videos where IsFakeAccount =" + SecurityLocksCommon.IsFakeAccount + " ORDER BY ModifiedDateTime DESC", null);
        while (rawQuery.moveToNext()) {
            Video video = new Video();
            video.setId(rawQuery.getInt(0));
            video.setVideoName(rawQuery.getString(1));
            video.setFolderLockVideoLocation(rawQuery.getString(2));
            video.setOriginalVideoLocation(rawQuery.getString(3));
            video.setthumbnail_video_location(rawQuery.getString(4));
            video.setAlbumId(rawQuery.getInt(5));
            video.setDateTime(rawQuery.getString(7));
            video.set_modifiedDateTime(rawQuery.getString(9));
            arrayList.add(video);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<Video> GetVideoByAlbumId(int i, int i2) {
        ArrayList<Video> arrayList = new ArrayList<>();
        String str = "SELECT * FROM tbl_videos where album_id = " + i;
        if (Photos_Gallery_Actitvity.SortBy.Time.ordinal() == i2) {
            str = "SELECT * FROM tbl_videos where album_id = " + i + " ORDER BY ModifiedDateTime DESC";
        } else if (Photos_Gallery_Actitvity.SortBy.Name.ordinal() == i2) {
            str = "SELECT * FROM tbl_videos where album_id = " + i + " ORDER BY video_name COLLATE NOCASE ASC";
        } else if (Photos_Gallery_Actitvity.SortBy.Size.ordinal() == i2) {
            str = "SELECT * FROM tbl_videos where album_id = " + i + " ORDER BY FileSize ASC";
        }
        Cursor rawQuery = this.database.rawQuery(str, null);
        while (rawQuery.moveToNext()) {
            Video video = new Video();
            video.setId(rawQuery.getInt(0));
            video.setVideoName(rawQuery.getString(1));
            video.setFolderLockVideoLocation(rawQuery.getString(2));
            video.setOriginalVideoLocation(rawQuery.getString(3));
            video.setthumbnail_video_location(rawQuery.getString(4));
            video.setAlbumId(rawQuery.getInt(5));
            video.set_modifiedDateTime(rawQuery.getString(9));
            video.SetFileCheck(false);
            arrayList.add(video);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<Video> GetVideosById(int i) {
        ArrayList<Video> arrayList = new ArrayList<>();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_videos where album_id =" + i, null);
        while (rawQuery.moveToNext()) {
            Video video = new Video();
            video.setId(rawQuery.getInt(0));
            video.setVideoName(rawQuery.getString(1));
            video.setFolderLockVideoLocation(rawQuery.getString(2));
            video.setOriginalVideoLocation(rawQuery.getString(3));
            video.setthumbnail_video_location(rawQuery.getString(4));
            video.setAlbumId(rawQuery.getInt(5));
            video.set_modifiedDateTime(rawQuery.getString(9));
            arrayList.add(video);
        }
        rawQuery.close();
        return arrayList;
    }

    public Video GetCoverVideo(int i) {
        Video video = new Video();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_videos where album_id = " + i + " ORDER BY RANDOM() limit 1", null);
        while (rawQuery.moveToNext()) {
            video.setId(rawQuery.getInt(0));
            video.setVideoName(rawQuery.getString(1));
            video.setFolderLockVideoLocation(rawQuery.getString(2));
            video.setOriginalVideoLocation(rawQuery.getString(3));
            video.setthumbnail_video_location(rawQuery.getString(4));
            video.setAlbumId(rawQuery.getInt(5));
            video.set_modifiedDateTime(rawQuery.getString(9));
        }
        rawQuery.close();
        return video;
    }

    public void DeleteVideoById(int i) {
        this.database.delete("tbl_videos", "_id = ?", new String[]{String.valueOf(i)});
        close();
    }

    public void DeleteVideoByAlbumId(int i) {
        for (Video video : GetVideosById(i)) {
            this.database.delete("tbl_videos", "_id = ?", new String[]{String.valueOf(video.getId())});
            File file = new File(video.getFolderLockVideoLocation());
            if (file.exists()) {
                file.delete();
            }
            File file2 = new File(video.getthumbnail_video_location());
            if (file2.exists()) {
                file2.delete();
            }
            File file3 = new File(file2.getParent());
            if (file3.exists()) {
                file3.delete();
            }
        }
        close();
    }

    public String[] GetAlbumNames(int i) {
        ArrayList<Globals> arrayList = new ArrayList<>();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_video_albums where _id != " + i + " AND IsFakeAccount =" + SecurityLocksCommon.IsFakeAccount, null);
        String[] strArr = new String[rawQuery.getCount()];
        int i2 = 0;
        while (rawQuery.moveToNext()) {
            strArr[i2] = rawQuery.getString(1);
            i2++;
            Globals globals = new Globals();
            globals.SetAlbumName(rawQuery.getString(1));
            arrayList.add(globals);
        }
        rawQuery.close();
        return strArr;
    }

    public List<String> GetMoveAlbumNames(int i) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_video_albums where _id != " + i + " AND IsFakeAccount =" + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            arrayList.add(rawQuery.getString(1));
        }
        rawQuery.close();
        return arrayList;
    }

    public void UpdateVideoLocationById(Video video) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("fl_video_location", video.getFolderLockVideoLocation());
        contentValues.put("thumbnail_video_location", video.getthumbnail_video_location());
        contentValues.put("album_id", video.getAlbumId());
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        this.database.update("tbl_videos", contentValues, "_id = ?", new String[]{String.valueOf(video.getId())});
        close();
    }

    public void UpdateAlbumVideoLocation(int i, String str) {
        String str2;
        for (Video video : GetVideosById(i)) {
            ContentValues contentValues = new ContentValues();
            if (video.getVideoName().contains("#")) {
                str2 = video.getVideoName();
            } else {
                str2 = Utilities.ChangeFileExtention(video.getVideoName());
            }
            contentValues.put("fl_video_location", str + "/" + str2);
            contentValues.put("thumbnail_video_location", str + "/VideoThumnails/" + Utilities.FileName(video.getthumbnail_video_location()));
            contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
            this.database.update("tbl_videos", contentValues, "_id = ?", new String[]{String.valueOf(video.getId())});
        }
        close();
    }

    public int GetVideoCountByAlbumId(int i) {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_videos where album_id = " + i, null);
        int i2 = 0;
        while (rawQuery.moveToNext()) {
            i2++;
        }
        rawQuery.close();
        return i2;
    }

    public boolean IsFileAlreadyExist(String str) {
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tbl_videos where fl_video_location ='" + str + "'", null);
        boolean z = false;
        while (rawQuery.moveToNext()) {
            z = true;
        }
        rawQuery.close();
        return z;
    }
}
