package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.documentfile.provider.DocumentFile;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class Utilities {

    public static void hideKeyboard(View view, Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void DeleteAlbum(File file) throws IOException {
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        file.delete();
    }

    public static String getCurrentDateTime2() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public static void strikeTextview(TextView textView, String str, boolean z) {
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        if (z) {
            textView.setText(str, TextView.BufferType.SPANNABLE);
            ((Spannable) textView.getText()).setSpan(strikethroughSpan, 0, str.length(), 33);
        } else {
            textView.setText(str);
        }
    }

    public static String FileSize(String str) {
        long j = 0;
        try {
            FileChannel fileChannel = new FileInputStream(str).getChannel();
            j = fileChannel.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long j2 = j / 1024;
        if (j2 > 1000) {
            return j / 1048576 + "mb";
        }
        return j2 + "kb";
    }

    public static String getFileNameWithoutExtention(String str) {
        String name = new File(str).getName();
        for (int length = name.length() - 1; length > 0; length--) {
            if (name.charAt(length) == "_".charAt(0)) {
                int lastIndexOf = name.lastIndexOf(".");
                return lastIndexOf > 0 ? name.substring(length + 1, lastIndexOf) : name;
            }
        }
        return "";
    }

    public static String NSHideFile(Context context, File file, File file2) throws IOException {
        if (file.exists()) {
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2.getAbsolutePath() + "/" + ChangeFileExtention(file.getName()));
            if (file3.exists()) {
                file3 = GetFileName(ChangeFileExtention(file.getName()), file3, file2.getAbsolutePath());
            }
            file3.createNewFile();
            FileChannel fileChannel = null;
            try {
                FileChannel channel = new FileInputStream(file).getChannel();
                try {
                    fileChannel = new FileOutputStream(file3, true).getChannel();
                    if (file.getAbsolutePath().contains(StorageOptionsCommon.STORAGEPATH)) {
                        file.renameTo(file3);
                    } else {
                        long size = channel.size();
                        long abs = Math.abs(size / 1048576);
                        if (abs > ((long) Common.MaxFileSizeInMB)) {
                            int CalculateChunkCounts = CalculateChunkCounts(abs);
                            double d = (double) size;
                            double d2 = CalculateChunkCounts;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            int abs2 = Math.abs((int) Math.ceil(d / d2));
                            for (int i = 0; i < CalculateChunkCounts; i++) {
                                channel.transferTo(abs2 * i, abs2, fileChannel);
                            }
                        } else {
                            channel.transferTo(0, size, fileChannel);
                        }
                        channel.close();
                        if (fileChannel != null) {
                            fileChannel.close();
                        }
                        if (file.exists() && file3.exists()) {
                            file.delete();
                        }
                    }
                    return file3.getAbsolutePath();
                } catch (Exception e) {
                    Log.e("TAG", "NSHideFile: " + e);
                    return "";
                }
            } catch (Exception e2) {
                Log.e("TAG", "NSHideFile unused2: " + e2);
                if (fileChannel != null) {
                    fileChannel.close();
                }
            }
        }
        return "";
    }

    public static boolean NSUnHideFile(Context context, String str, String str2) throws IOException {
        str2 = Environment.getExternalStorageDirectory().getPath() + Common.UnhideKitkatAlbumName + FileName(str2);
        File file = new File(str);
        File file2 = new File(str2);
        if (file2.exists()) {
            file2 = GetDesFileNameForUnHide(file2.getAbsolutePath(), file2.getName(), file2);
        }
        File file3 = new File(Objects.requireNonNull(file2.getParent()));
        if (!file3.exists() && !file3.mkdirs()) {
            file2 = new File(Environment.getExternalStorageDirectory().getPath() + Common.UnhideKitkatAlbumName + FileName(str2));
            if (file2.exists()) {
                file2 = GetDesFileNameForUnHide(file2.getAbsolutePath(), file2.getName(), file2);
            }
            if (!file3.exists() && !file3.mkdirs()) {
                return false;
            }
        }
        if (file2.createNewFile() && file3.exists()) {
            FileChannel fileChannel = null;
            try {
                if (!file2.getAbsolutePath().contains(StorageOptionsCommon.STORAGEPATH) || !file.renameTo(file2)) {
                    try {
                        long size = fileChannel.size();
                        long abs = Math.abs(size / 1048576);
                        if (abs > ((long) Common.MaxFileSizeInMB)) {
                            int CalculateChunkCounts = CalculateChunkCounts(abs);
                            double d = (double) size;
                            double d2 = CalculateChunkCounts;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            int abs2 = Math.abs((int) Math.ceil(d / d2));
                            for (int i = 0; i < CalculateChunkCounts; i++) {
                                FileChannel fileChannel2 = null;
                                fileChannel2.transferTo((abs2 * i) + 0, abs2, null);
                            }
                        } else {
                            FileChannel fileChannel3 = null;
                            fileChannel3.transferTo(0, size, null);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT >= StorageOptionsCommon.Kitkat) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(file2.getAbsolutePath())));
                    context.sendBroadcast(intent);
                }
                if (!file.exists() || !file2.exists()) {
                    NSDecryption(file2);
                    return true;
                }
                NSDecryption(file2);
                file.delete();
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static File GetDesFileNameForUnHide(String str, String str2, File file) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, str.lastIndexOf(47)));
        sb.append("/");
        String sb2 = sb.toString();
        String substring = str2.substring(0, str2.lastIndexOf(46));
        int lastIndexOf = str2.lastIndexOf(46);
        String str3 = lastIndexOf > Math.max(str2.lastIndexOf(47), str2.lastIndexOf(92)) ? "." + str2.substring(lastIndexOf + 1) : "";
        for (int i = 0; i < 100; i++) {
            file = new File(sb2 + "/" + (substring + "(" + i + ")" + str3));
            if (!file.exists()) {
                return file;
            }
        }
        return file;
    }

    public static boolean MoveFileWithinDirectories(String str, String str2) throws IOException {
        File file = new File(str);
        File file2 = new File(str2);
        File file3 = new File(file2.getParent());
        if (!file3.exists()) {
            file3.mkdirs();
        }
        if (file2.exists()) {
            return file.renameTo(GetFileName(file.getName(), file2, file2.getParent()));
        } else return file.renameTo(file2);
    }

    public static String CopyTemporaryFile(Context context, String str, String str2) throws IOException {
        File file = new File(str);
        String FileName = FileName(str);
        File file2 = new File(str2 + ChangeFileExtentionToOrignal(FileName));
        File file3 = new File(file2.getParent());
        if (!file3.exists() && !file3.mkdirs()) {
            String FileName2 = FileName(str2);
            file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/" + FileName2);
            if (!file3.exists()) {
                file3.mkdirs();
            }
        }
        file2.createNewFile();
        if (!file3.exists()) {
            return file2.getAbsolutePath();
        }
        FileChannel fileChannel = null;
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            try {
                fileChannel = new FileOutputStream(file2).getChannel();
                long size = channel.size();
                long abs = Math.abs(size / 1048576);
                if (abs > ((long) Common.MaxFileSizeInMB)) {
                    int CalculateChunkCounts = CalculateChunkCounts(abs);
                    double d = (double) size;
                    double d2 = CalculateChunkCounts;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    int abs2 = Math.abs((int) Math.ceil(d / d2));
                    for (int i = 0; i < CalculateChunkCounts; i++) {
                        channel.transferTo((abs2 * i) + 0, abs2, fileChannel);
                    }
                } else {
                    channel.transferTo(0, size, fileChannel);
                }
                if (channel != null) {
                    channel.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
                NSDecryption(file2);
                return file2.getAbsolutePath();
            } catch (Exception unused) {
                return file2.getAbsolutePath();
            }
        } catch (Exception unused2) {
            if (fileChannel != null) {
                fileChannel.close();
            }
            return file2.getAbsolutePath();
        }
    }

    public static boolean UnHideThumbnail(Context context, String str, String str2) throws IOException {
        File file = new File(str);
        File file2 = new File(str2);
        if (file2.exists()) {
            file2 = GetDesFileNameForUnHide(file2.getAbsolutePath(), file2.getName(), file2);
        }
        File file3 = new File(file2.getParent());
        if ((file3.exists() || file3.mkdirs() || file2.createNewFile()) && file3.exists()) {
            try {
                FileChannel channel = new FileInputStream(file).getChannel();
                FileChannel channel2 = new FileOutputStream(file2).getChannel();
                channel.transferTo(0, channel.size(), channel2);
                if (Build.VERSION.SDK_INT >= StorageOptionsCommon.Kitkat) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(file2.getAbsolutePath())));
                    context.sendBroadcast(intent);
                }
                if (channel != null) {
                    channel.close();
                }
                if (channel2 != null) {
                    channel2.close();
                }
                if (!file.exists() || !file2.exists()) {
                    return true;
                }
                file.delete();
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static String RecoveryHideFileSDCard(Context context, File file, File file2) throws IOException {
        File file3;
        if (file.exists()) {
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (file.getName().contains("#")) {
                file3 = new File(file2.getAbsolutePath() + "/" + file.getName());
            } else {
                file3 = new File(file2.getAbsolutePath() + "/" + ChangeFileExtention(file.getName()));
            }
            if (file3.exists()) {
                file3 = GetFileName(file.getName(), file3, file2.getAbsolutePath());
            }
            file3.createNewFile();
            FileChannel fileChannel = null;
            try {
                FileChannel channel = new FileInputStream(file).getChannel();
                try {
                    FileChannel channel2 = new FileOutputStream(file3).getChannel();
                    if (file.getAbsolutePath().contains(StorageOptionsCommon.STORAGEPATH)) {
                        file.renameTo(file3);
                    } else {
                        long size = channel.size();
                        long abs = Math.abs(size / 1048576);
                        if (abs > ((long) Common.MaxFileSizeInMB)) {
                            int CalculateChunkCounts = CalculateChunkCounts(abs);
                            double d = (double) size;
                            double d2 = CalculateChunkCounts;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            int abs2 = Math.abs((int) Math.ceil(d / d2));
                            for (int i = 0; i < CalculateChunkCounts; i++) {
                                channel.transferTo((abs2 * i) + 0, abs2, channel2);
                            }
                        } else {
                            channel.transferTo(0, size, channel2);
                        }
                    }
                    if (Build.VERSION.SDK_INT >= StorageOptionsCommon.Kitkat) {
                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                        intent.setData(Uri.fromFile(new File(file.getAbsolutePath())));
                        context.sendBroadcast(intent);
                    }
                    if (channel != null) {
                        channel.close();
                    }
                    if (channel2 != null) {
                        channel2.close();
                    }
                    if (file.exists() && file3.exists()) {
                        file.delete();
                    }
                    return file3.getAbsolutePath();
                } catch (Exception unused) {
                    return "";
                }
            } catch (Exception unused2) {
                if (0 != 0) {
                    fileChannel.close();
                }
            }
        }
        return "";
    }

    public static String RecoveryEntryFile(Context context, File file, File file2) throws IOException {
        if (!file.exists()) {
            return file2.getAbsolutePath();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        FileChannel fileChannel = null;
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            try {
                fileChannel = new FileOutputStream(file2).getChannel();
                if (file.getAbsolutePath().contains(StorageOptionsCommon.STORAGEPATH)) {
                    file.renameTo(file2);
                } else {
                    long size = channel.size();
                    long abs = Math.abs(size / 1048576);
                    if (abs > ((long) Common.MaxFileSizeInMB)) {
                        int CalculateChunkCounts = CalculateChunkCounts(abs);
                        double d = (double) size;
                        double d2 = CalculateChunkCounts;
                        Double.isNaN(d);
                        Double.isNaN(d2);
                        Double.isNaN(d);
                        Double.isNaN(d2);
                        int abs2 = Math.abs((int) Math.ceil(d / d2));
                        for (int i = 0; i < CalculateChunkCounts; i++) {
                            channel.transferTo((abs2 * i) + 0, abs2, fileChannel);
                        }
                    } else {
                        channel.transferTo(0, size, fileChannel);
                    }
                }
                if (Build.VERSION.SDK_INT >= StorageOptionsCommon.Kitkat) {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(file.getAbsolutePath())));
                    context.sendBroadcast(intent);
                }
                if (channel != null) {
                    channel.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
                if (file.exists() && file2.exists()) {
                    file.delete();
                }
                return file2.getAbsolutePath();
            } catch (Exception unused) {
                if (channel != null) {
                    channel.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
                return file2.getAbsolutePath();
            }
        } catch (Exception unused2) {
            return file2.getAbsolutePath();
        }
    }

    private static int CalculateChunkCounts(long j) {
        int i = 2;
        do {
            i++;
        } while (j / ((long) i) > ((long) Common.MaxFileSizeInMB));
        return i;
    }

    public static void NSEncryption(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        byte[] bArr = new byte[Common.EncryptBytesSize];
        randomAccessFile.read(bArr, 0, Common.EncryptBytesSize);
        randomAccessFile.seek(0);
        randomAccessFile.write(ReverseBytes(bArr));
        randomAccessFile.close();
    }

    public static void NSDecryption(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        byte[] bArr = new byte[Common.EncryptBytesSize];
        randomAccessFile.read(bArr, 0, Common.EncryptBytesSize);
        randomAccessFile.seek(0);
        randomAccessFile.write(ReverseBytes(bArr));
        randomAccessFile.close();
    }

    public static String NSVideoDecryptionDuringPlay(File file) throws IOException {
        File file2 = new File(file.getParent() + File.separator + ChangeFileExtentionToOrignal(FileName(file.getAbsolutePath())));
        file.renameTo(file2);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
        byte[] bArr = new byte[Common.EncryptBytesSize];
        randomAccessFile.read(bArr, 0, Common.EncryptBytesSize);
        randomAccessFile.seek(0);
        randomAccessFile.write(ReverseBytes(bArr));
        randomAccessFile.close();
        return file2.getAbsolutePath();
    }

    public static String NSVideoEncryptionAfterPlay(File file) throws IOException {
        File file2 = new File(file.getParent() + File.separator + ChangeFileExtention(FileName(file.getAbsolutePath())));
        file.renameTo(file2);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
        byte[] bArr = new byte[Common.EncryptBytesSize];
        randomAccessFile.read(bArr, 0, Common.EncryptBytesSize);
        randomAccessFile.seek(0);
        randomAccessFile.write(ReverseBytes(bArr));
        randomAccessFile.close();
        return file2.getAbsolutePath();
    }

    public static byte[] ReverseBytes(byte[] bArr) {
        if (bArr == null) {
            return bArr;
        }
        int length = bArr.length - 1;
        for (int i = 0; length > i; i++) {
            byte b = bArr[length];
            bArr[length] = bArr[i];
            bArr[i] = b;
            length--;
        }
        return bArr;
    }

    public static String ChangeFileExtentionToOrignal(String str) {
        String substring = str.substring(0, str.lastIndexOf(35));
        int lastIndexOf = str.lastIndexOf(35);
        return substring + ("." + str.substring(lastIndexOf + 1));
    }

    public static String FileName(String str) {
        for (int length = str.length() - 1; length > 0; length--) {
            if (str.charAt(length) == " /".charAt(1)) {
                return str.substring(length + 1);
            }
        }
        return "";
    }

    private static File GetFileName(String str, File file, String str2) {
        String str3 = "";
        int i = 0;
        if (str.contains("#")) {
            String substring = str.substring(0, str.lastIndexOf(35));
            int lastIndexOf = str.lastIndexOf(35);
            if (lastIndexOf > Math.max(str.lastIndexOf(47), str.lastIndexOf(92))) {
                str3 = "#" + str.substring(lastIndexOf + 1);
            }
            while (i < 100) {
                File file2 = new File(str2 + "/" + (substring + "(" + i + ")" + str3));
                if (!file2.exists()) {
                    return file2;
                }
                i++;
                file = file2;
            }
        } else {
            String substring2 = str.substring(0, str.lastIndexOf(46));
            int lastIndexOf2 = str.lastIndexOf(46);
            if (lastIndexOf2 > Math.max(str.lastIndexOf(47), str.lastIndexOf(92))) {
                str3 = "." + str.substring(lastIndexOf2 + 1);
            }
            while (i < 100) {
                File file3 = new File(str2 + "/" + (substring2 + "(" + i + ")" + str3));
                if (!file3.exists()) {
                    return file3;
                }
                i++;
                file = file3;
            }
        }
        return file;
    }

    public static String ChangeFileExtention(String str) {
        String substring = str.substring(0, str.lastIndexOf(46));
        int lastIndexOf = str.lastIndexOf(46);
        return substring + ("#" + str.substring(lastIndexOf + 1));
    }

    public static void CheckDeviceStoragePaths(Context context) {
        new ArrayList();
        if (Build.VERSION.SDK_INT < StorageOptionsCommon.Kitkat) {
            StorageOptionsCommon.IsStorageSDCard = context.getSharedPreferences("StorageOption", Context.MODE_MULTI_PROCESS).getBoolean("IsStorageSDCard", false);
            ArrayList<String> externalMountss = getExternalMountss();
            if (externalMountss.size() > 0) {
                StorageOptionsCommon.IsDeviceHaveMoreThenOneStorage = true;
                for (int i = 0; i < externalMountss.size(); i++) {
                    String str = externalMountss.get(i);
                    String[] split = str.split("/");
                    String str2 = split[2];
                    if (!str2.equals("sdcard") && !str2.equals("sdcard0") && new File(str).exists()) {
                        StorageOptionsCommon.STORAGEPATH_2 = str + "/";
                    } else if (str2.equals("media_rw")) {
                        StorageOptionsCommon.STORAGEPATH_2 = "/" + split[1] + "/" + split[3] + "/";
                    }
                }
            } else {
                StorageOptionsCommon.IsDeviceHaveMoreThenOneStorage = false;
            }
        } else {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(externalStoragePublicDirectory.getParent());
            if (new File("/storage/sdcard/").exists()) {
                StorageOptionsCommon.STORAGEPATH = externalStoragePublicDirectory.getParent() + File.separator;
            }
            if (new File("/storage/sdcard0/").exists()) {
                StorageOptionsCommon.STORAGEPATH = externalStoragePublicDirectory.getParent() + File.separator;
            } else if (file.exists()) {
                StorageOptionsCommon.STORAGEPATH = externalStoragePublicDirectory.getParent() + File.separator;
            }
            File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + StorageOptionsCommon.PHOTOS_DEFAULT_ALBUM);
            if (!file2.exists()) {
                file2.mkdirs();
                file2.exists();
            }
            File file3 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.STORAGE, "don't delete this folder.txt");
            if (!file3.exists()) {
                try {
                    file3.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    fileOutputStream.write("Warning! \nDo Not Delete this folder; it contains Calculator Vault Encrypted data.".getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SharedPreferences.Editor edit = context.getSharedPreferences("StorageOption", Context.MODE_MULTI_PROCESS).edit();
            edit.putString("STORAGEPATH", StorageOptionsCommon.STORAGEPATH);
            edit.commit();
        }
        File externalStoragePublicDirectory2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file4 = new File(externalStoragePublicDirectory2.getParent());
        File file5 = new File(StorageOptionsCommon.STORAGEPATH_2);
        if (file4.exists()) {
            StorageOptionsCommon.STORAGEPATH_1 = externalStoragePublicDirectory2.getParent() + File.separator;
        }
        File file6 = new File(StorageOptionsCommon.STORAGEPATH_1 + StorageOptionsCommon.PHOTOS + StorageOptionsCommon.PHOTOS_DEFAULT_ALBUM);
        if (!file6.exists()) {
            file6.mkdirs();
            file6.exists();
        }
        File file7 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.STORAGE, "don't delete this folder.txt");
        if (!file7.exists()) {
            try {
                file7.createNewFile();
                FileOutputStream fileOutputStream2 = new FileOutputStream(file7);
                fileOutputStream2.write("Warning! \nDo Not Delete this folder, it contains Calculator Vault data.".getBytes());
                fileOutputStream2.flush();
                fileOutputStream2.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (!file5.exists()) {
            StorageOptionsCommon.IsDeviceHaveMoreThenOneStorage = false;
        }
    }

    public static Bitmap DecodeFile(File file) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            int i = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            while ((options.outWidth / i) / 2 >= 70 && (options.outHeight / i) / 2 >= 70) {
                i *= 2;
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = i;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, options2);
        } catch (FileNotFoundException unused) {
            return null;
        }
    }

    public static ArrayList<String> getExternalMountss() {
        ArrayList<String> arrayList = new ArrayList<>();
        String str = "";
        try {
            Process start = new ProcessBuilder().command("mount").redirectErrorStream(true).start();
            start.waitFor();
            InputStream inputStream = start.getInputStream();
            byte[] bArr = new byte[1024];
            while (inputStream.read(bArr) != -1) {
                str = str + new String(bArr);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] split = str.split(IOUtils.LINE_SEPARATOR_UNIX);
        for (String str2 : split) {
            if (!str2.toLowerCase(Locale.US).contains("asec") && str2.matches("(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*")) {
                String[] split2 = str2.split(" ");
                for (String str3 : split2) {
                    if (str3.startsWith("/") && !str3.toLowerCase(Locale.US).contains("vold")) {
                        arrayList.add(str3);
                    }
                }
            }
        }
        return arrayList;
    }

    public static int getScreenOrientation(Context context) {
        Activity activity = (Activity) context;
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        defaultDisplay.getOrientation();
        int i = activity.getResources().getConfiguration().orientation;
        if (i != 0) {
            return i;
        }
        if (defaultDisplay.getWidth() == defaultDisplay.getHeight()) {
            return 3;
        }
        return defaultDisplay.getWidth() < defaultDisplay.getHeight() ? 1 : 2;
    }

    public static boolean isNetworkOnline(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
            if (networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
                if (networkInfo2 == null) {
                    return false;
                }
                return networkInfo2.getState() == NetworkInfo.State.CONNECTED;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("EEE dd MMM yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static int convertDptoPix(Context context, int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, context.getResources().getDisplayMetrics());
    }

    public static Uri getLastPhotoOrVideo(Context context) {
        Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "date_added"}, null, null, "date_added DESC");
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.moveToFirst();
        String string = query.getString(columnIndexOrThrow);
        query.close();
        return Uri.fromFile(new File(string));
    }

    public static void changeFileExtention(String str) {
        try {
            File file = str.equals(StorageOptionsCommon.VIDEOS) ? new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + "/") : null;
            if (str.equals(StorageOptionsCommon.DOCUMENTS)) {
                file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + "/");
            }
            if (file.exists()) {
                File[] listFiles = file.listFiles();
                for (File file2 : listFiles) {
                    if (file2.isDirectory()) {
                        File[] listFiles2 = file2.listFiles();
                        for (File file3 : listFiles2) {
                            if (file3.isFile()) {
                                String absolutePath = file3.getAbsolutePath();
                                if (!FileName(absolutePath).contains("#")) {
                                    NSVideoEncryptionAfterPlay(new File(absolutePath));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception unused) {
            Log.v("changeVideosExtention", "error in changeVideosExtention method");
        }
    }

    public static void DeleteSDcardImageLollipop(Context context, String str) {
        DeleteFile(context, str, DocumentFile.fromTreeUri(context, Uri.parse(StorageOptionSharedPreferences.GetObject(context).GetSDCardUri())));
    }

    public static void DeleteFile(Context context, String str, DocumentFile documentFile) {
        File file = new File(str);
        traverseDoc(context, documentFile, new ArrayList(Arrays.asList(file.getParent().split("/"))), file.getName());
    }

    public static void traverseDoc(Context context, DocumentFile documentFile, ArrayList<String> arrayList, String str) {
        DocumentFile[] listFiles = documentFile.listFiles();
        for (DocumentFile documentFile2 : listFiles) {
            if (documentFile2.isDirectory()) {
                if (arrayList.contains(documentFile2.getName())) {
                    traverseDoc(context, documentFile2, arrayList, str);
                    return;
                }
            } else if (documentFile2.isFile() && documentFile2.getName().equals(str)) {
                try {
                    if (DocumentsContract.deleteDocument(context.getContentResolver(), documentFile2.getUri())) {
                        return;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
