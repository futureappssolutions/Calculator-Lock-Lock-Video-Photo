package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.AudioDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioPlayListEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DatabaseHelper;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayListDAL {
    Context con;
    SQLiteDatabase database;
    DatabaseHelper helper;

    public AudioPlayListDAL(Context context) {
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

    public void AddAudioPlayList(AudioPlayListEnt audioPlayListEnt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PlayListName", audioPlayListEnt.getPlayListName());
        contentValues.put("FlPlayListLocation", audioPlayListEnt.getPlayListLocation());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        contentValues.put("SortBy", 0);
        contentValues.put("ModifiedDateTime", Utilities.getCurrentDateTime());
        long insert = this.database.insert("tblAudioPlayList", null, contentValues);
        Log.e("assaudioplaylist", "" + insert);
    }

    public int GetSortByPlaylistId(int i) {
        Cursor rawQuery = this.database.rawQuery("SELECT SortBy FROM tblAudioPlayList where Id = " + i, null);
        int i2 = 0;
        while (rawQuery.moveToNext()) {
            i2 = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i2;
    }

    public List<AudioPlayListEnt> GetPlayLists() {
        ArrayList<AudioPlayListEnt> arrayList = new ArrayList<>();
        AudioDAL audioDAL = new AudioDAL(this.con);
        audioDAL.OpenRead();
        String str = "SELECT * FROM tblAudioPlayList Where IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount + " ORDER BY Id";
        Cursor rawQuery = this.database.rawQuery(str, null);
        while (rawQuery.moveToNext()) {
            AudioPlayListEnt audioPlayListEnt = new AudioPlayListEnt();
            audioPlayListEnt.setId(rawQuery.getInt(0));
            audioPlayListEnt.setPlayListName(rawQuery.getString(1));
            audioPlayListEnt.setPlayListLocation(rawQuery.getString(2));
            audioPlayListEnt.set_modifiedDateTime(rawQuery.getString(6));
            audioPlayListEnt.set_fileCount(audioDAL.GetAudiosCountByFolderId(rawQuery.getInt(0)));
            arrayList.add(audioPlayListEnt);
        }
        rawQuery.close();
        return arrayList;
    }

    public AudioPlayListEnt GetPlayList(String str) {
        AudioPlayListEnt audioPlayListEnt = new AudioPlayListEnt();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tblAudioPlayList where PlayListName = '" + str + "' AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            audioPlayListEnt.setId(rawQuery.getInt(0));
            audioPlayListEnt.setPlayListName(rawQuery.getString(1));
            audioPlayListEnt.setPlayListLocation(rawQuery.getString(2));
        }
        rawQuery.close();
        return audioPlayListEnt;
    }

    public AudioPlayListEnt GetPlayListById(int i) {
        AudioPlayListEnt audioPlayListEnt = new AudioPlayListEnt();
        Cursor rawQuery = this.database.rawQuery("SELECT * FROM tblAudioPlayList where Id = " + i + " AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount, null);
        while (rawQuery.moveToNext()) {
            audioPlayListEnt.setId(rawQuery.getInt(0));
            audioPlayListEnt.setPlayListName(rawQuery.getString(1));
            audioPlayListEnt.setPlayListLocation(rawQuery.getString(2));
        }
        rawQuery.close();
        return audioPlayListEnt;
    }

    public void UpdatePlayListLocationOnly(AudioPlayListEnt audioPlayListEnt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FlPlayListLocation", audioPlayListEnt.getPlayListLocation());
        this.database.update("tblAudioPlayList", contentValues, "Id = ?", new String[]{String.valueOf(audioPlayListEnt.getId())});
        close();
        AudioDAL audioDAL = new AudioDAL(this.con);
        audioDAL.OpenWrite();
        audioDAL.UpdateAudioPlayListLocation(audioPlayListEnt.getId(), audioPlayListEnt.getPlayListLocation());
        audioDAL.close();
    }

    public void UpdatePlayListName(AudioPlayListEnt audioPlayListEnt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PlayListName", audioPlayListEnt.getPlayListName());
        contentValues.put("FlPlayListLocation", audioPlayListEnt.getPlayListLocation());
        contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
        this.database.update("tblAudioPlayList", contentValues, "Id = ?", new String[]{String.valueOf(audioPlayListEnt.getId())});
        close();
        AudioDAL audioDAL = new AudioDAL(this.con);
        audioDAL.OpenWrite();
        audioDAL.UpdateAudioPlayListLocation(audioPlayListEnt.getId(), audioPlayListEnt.getPlayListLocation());
        audioDAL.close();
    }

    public void DeletePlayListById(int i) {
        this.database.delete("tblAudioPlayList", "Id = ?", new String[]{String.valueOf(i)});
        close();
        AudioDAL audioDAL = new AudioDAL(this.con);
        audioDAL.OpenWrite();
        audioDAL.DeleteAudios(i);
        audioDAL.close();
    }

    public int GetLastPlayListId() {
        Cursor rawQuery = this.database.rawQuery("SELECT Id FROM tblAudioPlayList WHERE Id = (SELECT MAX(Id)  FROM tblAudioPlayList)", null);
        int i = 0;
        while (rawQuery.moveToNext()) {
            i = rawQuery.getInt(0);
        }
        rawQuery.close();
        return i;
    }

}
