package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser;

import android.content.Context;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.PhotoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;

public class AlbumsGalleryPhotosMethods {
    public void AddAlbumToDatabase(Context context, String str) {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumName(str);
        photoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + str);
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(context);
        try {
            photoAlbumDAL.OpenWrite();
            photoAlbumDAL.AddPhotoAlbum(photoAlbum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoAlbumDAL.close();
            throw th;
        }
        photoAlbumDAL.close();
    }

    public void UpdateAlbumInDatabase(Context context, int i, String str) {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setId(i);
        photoAlbum.setAlbumName(str);
        photoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + str);
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(context);
        try {
            photoAlbumDAL.OpenWrite();
            photoAlbumDAL.UpdateAlbumName(photoAlbum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoAlbumDAL.close();
            throw th;
        }
        photoAlbumDAL.close();
    }

    public void AddPhotoToDatabase(Context context, int i, String str, String str2, String str3) {
        Photo photo = new Photo();
        photo.setPhotoName(str);
        photo.setFolderLockPhotoLocation(str2);
        photo.setOriginalPhotoLocation(str3);
        photo.setAlbumId(i);
        PhotoDAL photoDAL = new PhotoDAL(context);
        try {
            photoDAL.OpenWrite();
            photoDAL.AddPhotos(photo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            photoDAL.close();
            throw th;
        }
        photoDAL.close();
    }
}
