package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Base64;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.SaveNoteInXML;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class NotesCommon {
    public static String CurrentNotesFile = null;
    public static String CurrentNotesFolder = null;
    public static int CurrentNotesFolderId = 0;
    public static boolean isEdittingNote = false;
    public HashMap<String, String> hashMap;
    public SaveNoteInXML saveNoteInXML;

    public void createNote(Activity activity, String str, String str2, String str3, String str4, String str5, String str6, String str7, boolean z) {
        hashMap = new HashMap<>();
        hashMap.put("Title", str3);
        hashMap.put("Text", str2);
        hashMap.put("audioData", str5);
        hashMap.put("NoteColor", str);
        hashMap.put("note_datetime_c", str6);
        hashMap.put("note_datetime_m", str7);
        hashMap.put("note_id", "A5688328-2CE5-4B5A-8668-0D015F3C113A");
        saveNoteInXML = new SaveNoteInXML();
        saveNoteInXML.SaveNote(activity, hashMap, str4, z);
    }

    public String getEncodedAudio(String str) {
        try {
            return Base64.encodeToString(FileUtils.readFileToByteArray(new File(str)), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getDecodedAudio(String str, String str2) {
        try {
            byte[] decode = Base64.decode(str, 0);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            fileOutputStream.write(decode);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinedEditText setEditTextFullPage(LinedEditText linedEditText) {
        int height = linedEditText.getHeight() / linedEditText.getLineHeight();
        if (linedEditText.getLineCount() > height) {
            height = linedEditText.getLineCount();
        }
        for (int i = 0; i < height; i++) {
            linedEditText.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        linedEditText.setSelection(0);
        return linedEditText;
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentDate() {
        return new SimpleDateFormat("EEE d MMM yyyy, HH:mm:ss aaa").format(Calendar.getInstance().getTime());
    }

    public int getProgressPercentage(long j, long j2) {
        Double.valueOf(0.0d);
        double d = (double) ((long) ((int) (j / 1000)));
        double d2 = (double) ((long) ((int) (j2 / 1000)));
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d);
        Double.isNaN(d2);
        return Double.valueOf((d / d2) * 100.0d).intValue();
    }

    public int progressToTimer(int i, int i2) {
        double d = i;
        Double.isNaN(d);
        Double.isNaN(d);
        double d2 = i2 / 1000;
        Double.isNaN(d2);
        Double.isNaN(d2);
        return ((int) ((d / 100.0d) * d2)) * 1000;
    }

    public String milliSecondsToTimer(long j) {
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

    public boolean hasNoData(String str) {
        return Pattern.compile("[\\n\\r ]+$").matcher(str).matches();
    }

    public boolean isNoSpecialCharsInName(String str) {
        return Pattern.compile("^[a-zA-Z.0-9 -]+$").matcher(str).matches();
    }
}
