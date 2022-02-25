package com.calculator.vaultlocker.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Vector;

public class FileUtils {
    private static final String DIRECTORY = Environment.getExternalStorageDirectory().getPath();
    private static final String DIRECTORY2 = "/mnt/extSdCard";
    private static final String DIRECTORY3 = "/storage/sdcard1";

    public static void deleteDirectory(File file) throws IOException {
        if (file.exists()) {
            if (!isSymlink(file)) {
                org.apache.commons.io.FileUtils.cleanDirectory(file);
            }
            if (!file.delete()) {
                throw new IOException("Unable to delete directory " + file + ".");
            }
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file != null) {
            if (file.getParent() != null) {
                file = new File(Objects.requireNonNull(file.getParentFile()).getCanonicalFile(), file.getName());
            }
            return !file.getCanonicalFile().equals(file.getAbsoluteFile());
        }
        throw new NullPointerException("File must not be null");
    }

    public ArrayList<File> FindFiles(String[] strArr) {
        ArrayList<File> arrayList = new ArrayList<>();
        FilenameFilter[] filenameFilterArr = new FilenameFilter[strArr.length];
        int i = 0;
        for (final String str : strArr) {
            filenameFilterArr[i] = (file, str2) -> str2.endsWith("." + str);
            i++;
        }
        Collections.addAll(arrayList, listFilesAsArray(new File(DIRECTORY), filenameFilterArr));
        File file2 = new File(DIRECTORY2);
        if (file2.exists() && file2.isDirectory()) {
            Collections.addAll(arrayList, listFilesAsArray(new File(DIRECTORY2), filenameFilterArr));
        }
        File file4 = new File(DIRECTORY3);
        if (file4.exists() && file4.isDirectory()) {
            Collections.addAll(arrayList, listFilesAsArray(new File(DIRECTORY3), filenameFilterArr));
        }
        return arrayList;
    }

    private File[] listFilesAsArray(File file, FilenameFilter[] filenameFilterArr) {
        Collection<File> listFiles = listFiles(file, filenameFilterArr, -1);
        return listFiles.toArray(new File[0]);
    }

    private Collection<File> listFiles(File file, FilenameFilter[] filenameFilterArr, int i) {
        Vector<File> vector = new Vector<>();
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                for (FilenameFilter filenameFilter : filenameFilterArr) {
                    if (filenameFilter.accept(file, file2.getName())) {
                        vector.add(file2);
                        Log.v("FileUtils", "Added: " + file2.getName());
                    }
                }
                if (i <= -1 || (i > 0 && file2.isDirectory())) {
                    int i2 = i - 1;
                    vector.addAll(listFiles(file2, filenameFilterArr, i2));
                    i = i2 + 1;
                }
            }
        }
        return vector;
    }

    private String filename(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }
}
