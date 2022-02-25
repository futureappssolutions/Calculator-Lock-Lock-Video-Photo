package com.calculator.vaultlocker.XMLParser;

import android.content.Context;

import com.calculator.vaultlocker.DB.VideoAlbumDAL;
import com.calculator.vaultlocker.Model.VideoAlbum;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;

public class AlbumsGalleryVideosMethods {
    public void AddAlbumToDatabase(Context context, String str) {
        VideoAlbum videoAlbum = new VideoAlbum();
        videoAlbum.setAlbumName(str);
        videoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + str);
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(context);
        try {
            videoAlbumDAL.OpenWrite();
            videoAlbumDAL.AddVideoAlbum(videoAlbum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoAlbumDAL.close();
            throw th;
        }
        videoAlbumDAL.close();
    }

    public void UpdateAlbumInDatabase(Context context, int i, String str) {
        VideoAlbum videoAlbum = new VideoAlbum();
        videoAlbum.setId(i);
        videoAlbum.setAlbumName(str);
        videoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + str);
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(context);
        try {
            videoAlbumDAL.OpenWrite();
            videoAlbumDAL.UpdateAlbumName(videoAlbum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoAlbumDAL.close();
            throw th;
        }
        videoAlbumDAL.close();
    }
}
