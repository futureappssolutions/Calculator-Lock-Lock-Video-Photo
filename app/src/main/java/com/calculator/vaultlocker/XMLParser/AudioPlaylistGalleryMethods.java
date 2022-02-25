package com.calculator.vaultlocker.XMLParser;

import android.content.Context;

import com.calculator.vaultlocker.DB.AudioPlayListDAL;
import com.calculator.vaultlocker.Model.AudioPlayListEnt;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;

public class AudioPlaylistGalleryMethods {
    public void AddPlaylistToDatabase(Context context, String str) {
        AudioPlayListEnt audioPlayListEnt = new AudioPlayListEnt();
        audioPlayListEnt.setPlayListName(str);
        audioPlayListEnt.setPlayListLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + str);
        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(context);
        try {
            audioPlayListDAL.OpenWrite();
            audioPlayListDAL.AddAudioPlayList(audioPlayListEnt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            audioPlayListDAL.close();
            throw th;
        }
        audioPlayListDAL.close();
    }

    public void UpdatePlaylistInDatabase(Context context, int i, String str) {
        AudioPlayListEnt audioPlayListEnt = new AudioPlayListEnt();
        audioPlayListEnt.setId(i);
        audioPlayListEnt.setPlayListName(str);
        audioPlayListEnt.setPlayListLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + str);
        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(context);
        try {
            audioPlayListDAL.OpenWrite();
            audioPlayListDAL.UpdatePlayListName(audioPlayListEnt);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            audioPlayListDAL.close();
            throw th;
        }
        audioPlayListDAL.close();
    }
}
