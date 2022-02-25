package com.calculator.vaultlocker.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.calculator.vaultlocker.notes.SystemBarTintManager;
import com.calculator.vaultlocker.notes.UIElementsHelper;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Common {
    public static final int HackAttemptedTotal = 3;
    public static final MediaPlayer mediaplayer = new MediaPlayer();
    public static final MediaPlayer voiceplayer = new MediaPlayer();
    public static String AudioFolderName = StorageOptionsCommon.AUDIOS_DEFAULT_ALBUM;
    public static String[] ColorsArray = {"#049372", "#049372", "#049372", "#049372", "#049372", "#049372", "#049372", "#049372", "#049372", "#049372", "#049372", "#049372"};
    public static Activity CurrentActivity = null;
    public static int CurrentTrackId = 0;
    public static int CurrentTrackIndex = 0;
    public static int CurrentTrackNextIndex = 0;
    public static Activity CurrentWebBrowserActivity = null;
    public static Activity CurrentWebServerActivity = null;
    public static String DocumentFolderName = StorageOptionsCommon.DOCUMENTS_DEFAULT_ALBUM;
    public static int EncryptBytesSize = 121;
    public static int FolderId = 0;
    public static int GalleryThumbnailCurrentPosition = 0;
    public static int HackAttemptCount = 0;
    public static int InterstitialAdCount = 1;
    public static boolean IsCameFromAppGallery = false;
    public static boolean IsCameFromFeatureActivity = false;
    public static boolean IsCameFromGalleryFeature = false;
    public static boolean IsCameFromPhotoAlbum = false;
    public static boolean IsChangeVideoExtentionInProcess = false;
    public static boolean IsImporting = false;
    public static boolean IsOpenFile = false;
    public static boolean IsSelectAll = false;
    public static boolean IsStart = false;
    public static boolean IsWebBrowserActive = false;
    public static boolean IsWorkInProgress = false;
    public static String LastWebBrowserUrl = "http://www.Example.net";
    public static int MaxFileSizeInMB = 500;
    public static int MemoriesThumbnailCurrentPosition = 0;
    public static String PhotoFolderName = StorageOptionsCommon.PHOTOS_DEFAULT_ALBUM;
    public static int PhotoThumbnailCurrentPosition = 0;
    public static int PlayListId = 0;
    public static int SelectedCount = 0;
    public static boolean ToDoListEdit = false;
    public static int ToDoListId = 0;
    public static String ToDoListName = null;
    public static String UnhideKitkatAlbumName = "/Unlocked Files/";
    public static String VideoFolderName = StorageOptionsCommon.VIDEOS_DEFAULT_ALBUM;
    public static int VideoThumbnailCurrentPosition = 0;
    public static boolean WhatsNew = false;
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static boolean isDelete = false;
    public static boolean isFolderImport = false;
    public static boolean isMove = false;
    public static boolean isOpenCameraorGalleryFromApp = false;
    public static boolean isPurchased = true;
    public static boolean isShared = false;
    public static boolean isSignOutSuccessfully = false;
    public static boolean isUnHide = false;
    public static int loginCount = 0;

    public static int sortType = 0;
    public static boolean IsPhoneGalleryLoad = true;

    public static void applyKitKatTranslucency(Activity activity) {
        setTranslucentStatus(activity, true);
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setNavigationBarTintEnabled(true);
        systemBarTintManager.setTintDrawable(UIElementsHelper.getGeneralActionBarBackground(activity));
    }

    @SuppressLint("WrongConstant")
    private static void setTranslucentStatus(Activity activity, boolean z) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (z) {
            attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        } else {
            attributes.flags &= -67108865;
        }
        window.setAttributes(attributes);
    }

    public static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).threadPriority(3).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).build());
    }

    public static float GetTotalMemory() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        return ((((float) statFs.getBlockCount()) * ((float) statFs.getBlockSize())) / 1.07374182E9f) * 1024.0f;
    }

    public static float GetTotalFree() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        return ((((float) statFs.getAvailableBlocks()) * ((float) statFs.getBlockSize())) / 1.07374182E9f) * 1024.0f;
    }

    public static float GetFileSize(ArrayList<String> arrayList) {
        Iterator<String> it = arrayList.iterator();
        float f = 0.0f;
        while (it.hasNext()) {
            f += ((float) (new File(it.next()).length() / 1024)) / 1024.0f;
        }
        return f;
    }

    public static int getProgressPercentage(long j, long j2) {
        Double.valueOf(0.0d);
        double d = (double) ((long) ((int) (j / 1000)));
        double d2 = (double) ((long) ((int) (j2 / 1000)));
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d);
        Double.isNaN(d2);
        return Double.valueOf((d / d2) * 100.0d).intValue();
    }

    public static String milliSecondsToTimer(long j) {
        String str;
        String str2;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i > 0) {
            str = i + ":";
        } else {
            str = "";
        }
        if (i3 < 10) {
            str2 = "0" + i3;
        } else {
            str2 = "" + i3;
        }
        return str + i2 + ":" + str2;
    }

    public static int progressToTimer(int i, int i2) {
        double d = i;
        Double.isNaN(d);
        Double.isNaN(d);
        double d2 = i2 / 1000;
        Double.isNaN(d2);
        Double.isNaN(d2);
        return ((int) ((d / 100.0d) * d2)) * 1000;
    }


    public enum ActivityType {
        Music,
        Document,
        Miscellaneous
    }
}
