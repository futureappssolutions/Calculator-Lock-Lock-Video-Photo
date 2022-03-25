package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettingsSharedPreferences {
    static Context context;
    static SharedPreferences myPrefs;
    private static String _DocAlbumViewIsGrid = "DocAlbumViewIsGrid";
    private static String _GallerySortBy = "GallerySortBy";
    private static String _GalleryViewBy = "GalleryViewBy";
    private static String _IsDontShowAudioHelp = "IsDontShowAudioHelp";
    private static String _IsDontShowMiscallaneousHelp = "IsDontShowMiscallaneousHelp";
    private static String _IsDontShowMsgLock = "IsDontShowThumbMsg";
    private static String _IsDontShowThumbLockMsg = "IsDontShowThumbLockMsg";
    private static String _PhotoAlbumViewIsGrid = "PhotoAlbumViewIsGrid";
    private static String _UrlIndexSocialMedia = "UrlIndexSocialMedia";
    private static String _VideoAlbumViewIsGrid = "VideoAlbumViewIsGrid";
    private static String _fileName = "AppSettings";
    private static String _isPurchased = "isPurchased";
    private static String _lastUrlSocialMedia = "LastUrlSocialMedia";
    private static String _sortByDocumentFolders = "SortByDocumentFolders";
    private static String _sortByPhotosAlbums = "SortByPhotosAlbums";
    private static String _sortByVideosAlbums = "SortByVideosAlbums";
    private static String _viewByAudio = "ViewByAudio";
    private static AppSettingsSharedPreferences appSettingsSharedPreferences;

    private AppSettingsSharedPreferences() {
    }

    public static AppSettingsSharedPreferences GetObject(Context context2) {
        if (appSettingsSharedPreferences == null) {
            appSettingsSharedPreferences = new AppSettingsSharedPreferences();
        }
        context = context2;
        myPrefs = context2.getSharedPreferences(_fileName, 0);
        return appSettingsSharedPreferences;
    }

    public boolean GetPhotoAlbumViewIsGrid() {
        return myPrefs.getBoolean(_PhotoAlbumViewIsGrid, true);
    }

    public int GetAudioViewBy() {
        return myPrefs.getInt(_viewByAudio, 0);
    }

    public void SetAudioViewBy(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_viewByAudio, i);
        edit.commit();
    }

    public void SetPhotoAlbumViewIsGrid(Boolean bool) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_PhotoAlbumViewIsGrid, bool.booleanValue());
        edit.commit();
    }

    public boolean GetVideoAlbumViewIsGrid() {
        return myPrefs.getBoolean(_VideoAlbumViewIsGrid, true);
    }

    public void SetVideoAlbumViewIsGrid(Boolean bool) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_VideoAlbumViewIsGrid, bool.booleanValue());
        edit.commit();
    }

    public boolean GetDocAlbumViewIsGrid() {
        return myPrefs.getBoolean(_DocAlbumViewIsGrid, true);
    }

    public void SetDocAlbumViewIsGrid(Boolean bool) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_DocAlbumViewIsGrid, bool.booleanValue());
        edit.commit();
    }

    public int GetPhotosAlbumsSortBy() {
        return myPrefs.getInt(_sortByPhotosAlbums, 0);
    }

    public boolean getIsPurchased() {
        return myPrefs.getBoolean(_isPurchased, false);
    }

    public void setIsPurchased(boolean z) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_isPurchased, z);
        edit.commit();
    }

    public void SetPhotosAlbumsSortBy(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_sortByPhotosAlbums, i);
        edit.commit();
    }

    public int GetVideosAlbumsSortBy() {
        return myPrefs.getInt(_sortByVideosAlbums, 0);
    }

    public void SetVideosAlbumsSortBy(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_sortByVideosAlbums, i);
        edit.commit();
    }

    public int GetDocumentFoldersSortBy() {
        return myPrefs.getInt(_sortByDocumentFolders, 0);
    }

    public void SetDocumentFoldersSortBy(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_sortByDocumentFolders, i);
        edit.commit();
    }

    public void SetIsDontShowThumbLockMsg(Boolean bool) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_IsDontShowThumbLockMsg, bool.booleanValue());
        edit.commit();
    }

    public boolean GetIsDontShowThumbLockMsg() {
        return myPrefs.getBoolean(_IsDontShowThumbLockMsg, false);
    }

    public void SetIsDontShowMsgLock(Boolean bool) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_IsDontShowMsgLock, bool.booleanValue());
        edit.commit();
    }

    public boolean GetIsDontShowMsgLock() {
        return myPrefs.getBoolean(_IsDontShowMsgLock, false);
    }

    public void SetLastUrlSocialMedia(String str) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putString(_lastUrlSocialMedia, str);
        edit.commit();
    }

    public String GetLastUrlSocialMedia() {
        return myPrefs.getString(_lastUrlSocialMedia, "");
    }

    public void SetUrlIndexSocialMedia(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_UrlIndexSocialMedia, i);
        edit.commit();
    }

    public int GetUrlIndexSocialMedia() {
        return myPrefs.getInt(_UrlIndexSocialMedia, 0);
    }

    public int GetGallerySortBy() {
        return myPrefs.getInt(_GallerySortBy, 1);
    }

    public void SetGallerySortBy(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_GallerySortBy, i);
        edit.commit();
    }

    public int GetGalleryViewBy() {
        return myPrefs.getInt(_GalleryViewBy, 1);
    }

    public void SetGalleryViewBy(int i) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putInt(_GalleryViewBy, i);
        edit.commit();
    }

    public void SetIsDontShowAudioHelp(Boolean bool) {
        SharedPreferences.Editor edit = myPrefs.edit();
        edit.putBoolean(_IsDontShowAudioHelp, bool.booleanValue());
        edit.commit();
    }

    public boolean GetIsDontShowAudioHelp() {
        return myPrefs.getBoolean(_IsDontShowAudioHelp, false);
    }
}
