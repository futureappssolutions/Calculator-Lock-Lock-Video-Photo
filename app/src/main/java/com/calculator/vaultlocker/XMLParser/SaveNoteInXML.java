package com.calculator.vaultlocker.XMLParser;

import android.app.Activity;
import android.content.Intent;
import android.util.Xml;
import android.widget.Toast;

import com.calculator.vaultlocker.Activity.NotesFilesActivity;
import com.calculator.vaultlocker.DB.NotesFilesDAL;
import com.calculator.vaultlocker.DB.NotesFolderDAL;
import com.calculator.vaultlocker.common.Flaes;
import com.calculator.vaultlocker.Model.NotesFileDB_Pojo;
import com.calculator.vaultlocker.Model.NotesFolderDB_Pojo;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.notes.NotesCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SaveNoteInXML {
    Constants constants;
    File newFile;
    String noteName;
    NotesFilesDAL notesFilesDAL;
    File oldFile;

    public void SaveNote(Activity activity, HashMap<String, String> hashMap, String str, boolean z) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        Exception e;
        String str9;
        String str10 = null;
        String str11;
        String str12;
        String str13;
        String str14 = null;
        double d = 0;
        Throwable th;
        String str15 = null;
        Throwable th2 = null;
        String str16 = null;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22 = null;
        String str23 = null;
        String str24 = null;
        String str25 = null;
        String str26 = null;
        String str27 = null;
        String str28 = null;
        String str29 = null;
        String concat = null;
        String str30;
        double d2;
        NotesFolderDAL notesFolderDAL = null;
        StringBuilder sb = null;
        String str31;
        String str32;
        Object obj;
        String str33 = null;
        IOException e2;
        Exception e3;
        this.constants = new Constants();
        this.noteName = hashMap.get("Title");
        this.newFile = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES + NotesCommon.CurrentNotesFolder + File.separator, this.noteName + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        this.oldFile = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES + NotesCommon.CurrentNotesFolder + File.separator, str + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        String str34 = "NotesFolderIsDecoy";
        String str35 = " AND ";
        String str36 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ";
        String str37 = "note_datetime_c";
        double d3 = Double.parseDouble("date");
        String str38 = "note_datetime_m";
        double d4 = 0.0d;

        if (!z) {
            try {
                try {
                    try {
                        if (this.newFile.exists()) {
                            try {
                                try {
                                    Toast.makeText(activity, (int) R.string.toast_exists, Toast.LENGTH_SHORT).show();
                                    if (z) {
                                        try {
                                            this.constants.getClass();
                                            StringBuilder sb2 = new StringBuilder();
                                            this.constants.getClass();
                                            sb2.append("NotesFileName");
                                            sb2.append(" = '");
                                            sb2.append(str);
                                            sb2.append("' AND ");
                                            this.constants.getClass();
                                            sb2.append("NotesFileIsDecoy");
                                            sb2.append(" = ");
                                            sb2.append(SecurityLocksCommon.IsFakeAccount);
                                            concat = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ".concat(sb2.toString());
                                        } catch (Exception e4) {
                                            e = e4;
                                            str6 = "' AND ";
                                            str7 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                                            str4 = str34;
                                            str3 = str35;
                                            str2 = "NotesFolderId";
                                            str5 = "NotesFileIsDecoy";
                                            str8 = "NotesFileId";
                                            e.printStackTrace();
                                            if (z) {
                                            }
                                            if (this.newFile.exists()) {
                                            }
                                            this.notesFilesDAL = new NotesFilesDAL(activity);
                                            new NotesFileDB_Pojo();
                                            NotesFileDB_Pojo notesFileInfoFromDatabase = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                            notesFileInfoFromDatabase.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                            notesFileInfoFromDatabase.setNotesFileName(this.noteName);
                                            notesFileInfoFromDatabase.setNotesFileText(hashMap.get("Text"));
                                            notesFileInfoFromDatabase.setNotesFileCreatedDate(hashMap.get(str37));
                                            notesFileInfoFromDatabase.setNotesFileModifiedDate(hashMap.get(str38));
                                            notesFileInfoFromDatabase.setNotesfileColor(hashMap.get("NoteColor"));
                                            notesFileInfoFromDatabase.setNotesFileLocation(this.newFile.getAbsolutePath());
                                            notesFileInfoFromDatabase.setNotesFileFromCloud(0);
                                            notesFileInfoFromDatabase.setNotesFileSize(d4);
                                            notesFileInfoFromDatabase.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                            if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                            }
                                            NotesFolderDAL notesFolderDAL2 = new NotesFolderDAL(activity);
                                            new NotesFolderDB_Pojo();
                                            StringBuilder sb3 = new StringBuilder();
                                            this.constants.getClass();
                                            sb3.append(str36);
                                            this.constants.getClass();
                                            sb3.append(str2);
                                            sb3.append(" = ");
                                            sb3.append(NotesCommon.CurrentNotesFolderId);
                                            sb3.append(str3);
                                            this.constants.getClass();
                                            sb3.append(str4);
                                            sb3.append(" = ");
                                            sb3.append(SecurityLocksCommon.IsFakeAccount);
                                            NotesFolderDB_Pojo notesFolderInfoFromDatabase = notesFolderDAL2.getNotesFolderInfoFromDatabase(sb3.toString());
                                            notesFolderInfoFromDatabase.setNotesFolderModifiedDate(hashMap.get(str38));
                                            notesFolderInfoFromDatabase.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                            this.constants.getClass();
                                            notesFolderDAL2.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase, str2, String.valueOf(notesFolderInfoFromDatabase.getNotesFolderId()));
                                            SecurityLocksCommon.IsAppDeactive = false;
                                            activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                            activity.finish();
                                            return;
                                        }
                                    } else {
                                        this.constants.getClass();
                                        StringBuilder sb4 = new StringBuilder();
                                        this.constants.getClass();
                                        sb4.append("NotesFileName");
                                        sb4.append(" = '");
                                        sb4.append(this.noteName);
                                        sb4.append("' AND ");
                                        this.constants.getClass();
                                        sb4.append("NotesFileIsDecoy");
                                        sb4.append(" = ");
                                        sb4.append(SecurityLocksCommon.IsFakeAccount);
                                        concat = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ".concat(sb4.toString());
                                    }
                                    if (this.newFile.exists()) {
                                        str13 = "NotesFileIsDecoy";
                                        str12 = "' AND ";
                                        try {
                                            d = (double) this.newFile.length();
                                        } catch (Exception e5) {
                                            e = e5;
                                            str7 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                                            str4 = str34;
                                            str3 = str35;
                                            str2 = "NotesFolderId";
                                            str30 = "NotesFileId";
                                            str5 = str13;
                                            str6 = str12;
                                            str8 = str30;
                                            e.printStackTrace();
                                            if (z) {
                                            }
                                            if (this.newFile.exists()) {
                                            }
                                            this.notesFilesDAL = new NotesFilesDAL(activity);
                                            new NotesFileDB_Pojo();
                                            NotesFileDB_Pojo notesFileInfoFromDatabase2 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                            notesFileInfoFromDatabase2.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                            notesFileInfoFromDatabase2.setNotesFileName(this.noteName);
                                            notesFileInfoFromDatabase2.setNotesFileText(hashMap.get("Text"));
                                            notesFileInfoFromDatabase2.setNotesFileCreatedDate(hashMap.get(str37));
                                            notesFileInfoFromDatabase2.setNotesFileModifiedDate(hashMap.get(str38));
                                            notesFileInfoFromDatabase2.setNotesfileColor(hashMap.get("NoteColor"));
                                            notesFileInfoFromDatabase2.setNotesFileLocation(this.newFile.getAbsolutePath());
                                            notesFileInfoFromDatabase2.setNotesFileFromCloud(0);
                                            notesFileInfoFromDatabase2.setNotesFileSize(d4);
                                            notesFileInfoFromDatabase2.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                            if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                            }
                                            NotesFolderDAL notesFolderDAL22 = new NotesFolderDAL(activity);
                                            new NotesFolderDB_Pojo();
                                            StringBuilder sb32 = new StringBuilder();
                                            this.constants.getClass();
                                            sb32.append(str36);
                                            this.constants.getClass();
                                            sb32.append(str2);
                                            sb32.append(" = ");
                                            sb32.append(NotesCommon.CurrentNotesFolderId);
                                            sb32.append(str3);
                                            this.constants.getClass();
                                            sb32.append(str4);
                                            sb32.append(" = ");
                                            sb32.append(SecurityLocksCommon.IsFakeAccount);
                                            NotesFolderDB_Pojo notesFolderInfoFromDatabase2 = notesFolderDAL22.getNotesFolderInfoFromDatabase(sb32.toString());
                                            notesFolderInfoFromDatabase2.setNotesFolderModifiedDate(hashMap.get(str38));
                                            notesFolderInfoFromDatabase2.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                            this.constants.getClass();
                                            notesFolderDAL22.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase2, str2, String.valueOf(notesFolderInfoFromDatabase2.getNotesFolderId()));
                                            SecurityLocksCommon.IsAppDeactive = false;
                                            activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                            activity.finish();
                                            return;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            str11 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                                            str14 = "NotesFolderId";
                                            d = Double.parseDouble("NotesFileId");
                                            if (z) {
                                            }
                                            if (this.newFile.exists()) {
                                            }
                                            this.notesFilesDAL = new NotesFilesDAL(activity);
                                            new NotesFileDB_Pojo();
                                            NotesFileDB_Pojo notesFileInfoFromDatabase3 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                            notesFileInfoFromDatabase3.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                            notesFileInfoFromDatabase3.setNotesFileName(this.noteName);
                                            notesFileInfoFromDatabase3.setNotesFileText(hashMap.get("Text"));
                                            notesFileInfoFromDatabase3.setNotesFileCreatedDate(hashMap.get(str37));
                                            notesFileInfoFromDatabase3.setNotesFileModifiedDate(hashMap.get(str38));
                                            notesFileInfoFromDatabase3.setNotesfileColor(hashMap.get("NoteColor"));
                                            notesFileInfoFromDatabase3.setNotesFileLocation(this.newFile.getAbsolutePath());
                                            notesFileInfoFromDatabase3.setNotesFileFromCloud(0);
                                            notesFileInfoFromDatabase3.setNotesFileSize(d4);
                                            notesFileInfoFromDatabase3.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                            if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                            }
                                            NotesFolderDAL notesFolderDAL3 = new NotesFolderDAL(activity);
                                            new NotesFolderDB_Pojo();
                                            StringBuilder sb5 = new StringBuilder();
                                            this.constants.getClass();
                                            sb5.append(str36);
                                            this.constants.getClass();
                                            sb5.append(str15);
                                            sb5.append(" = ");
                                            sb5.append(NotesCommon.CurrentNotesFolderId);
                                            sb5.append(str35);
                                            this.constants.getClass();
                                            sb5.append(str34);
                                            sb5.append(" = ");
                                            sb5.append(SecurityLocksCommon.IsFakeAccount);
                                            NotesFolderDB_Pojo notesFolderInfoFromDatabase3 = notesFolderDAL3.getNotesFolderInfoFromDatabase(sb5.toString());
                                            notesFolderInfoFromDatabase3.setNotesFolderModifiedDate(hashMap.get(str38));
                                            notesFolderInfoFromDatabase3.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                            this.constants.getClass();
                                            notesFolderDAL3.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase3, str15, String.valueOf(notesFolderInfoFromDatabase3.getNotesFolderId()));
                                            SecurityLocksCommon.IsAppDeactive = false;
                                            activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                            activity.finish();
                                            throw th2;
                                        }
                                    } else {
                                        str13 = "NotesFileIsDecoy";
                                        str12 = "' AND ";
                                        d = 0.0d;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    str13 = "NotesFileIsDecoy";
                                    str12 = "' AND ";
                                }
                            } catch (Exception e6) {
                                e = e6;
                                str17 = "NotesFileIsDecoy";
                                str18 = "NotesFileId";
                                str6 = "' AND ";
                                str7 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                                str4 = str34;
                                str3 = str35;
                                str2 = "NotesFolderId";
                                str5 = str17;
                                str8 = str18;
                                e.printStackTrace();
                                if (z) {
                                    this.constants.getClass();
                                    StringBuilder sb6 = new StringBuilder();
                                    str9 = str8;
                                    this.constants.getClass();
                                    sb6.append("NotesFileName");
                                    sb6.append(" = '");
                                    sb6.append(str);
                                    sb6.append(str6);
                                    this.constants.getClass();
                                    sb6.append(str5);
                                    sb6.append(" = ");
                                    sb6.append(SecurityLocksCommon.IsFakeAccount);
                                    str10 = str7.concat(sb6.toString());
                                } else {
                                    str9 = str8;
                                    this.constants.getClass();
                                    StringBuilder sb7 = new StringBuilder();
                                    this.constants.getClass();
                                    sb7.append("NotesFileName");
                                    sb7.append(" = '");
                                    sb7.append(this.noteName);
                                    sb7.append(str6);
                                    this.constants.getClass();
                                    sb7.append(str5);
                                    sb7.append(" = ");
                                    sb7.append(SecurityLocksCommon.IsFakeAccount);
                                    str10 = str7.concat(sb7.toString());
                                }
                                double length = this.newFile.exists() ? (double) this.newFile.length() : d4;
                                this.notesFilesDAL = new NotesFilesDAL(activity);
                                new NotesFileDB_Pojo();
                                NotesFileDB_Pojo notesFileInfoFromDatabase22 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                notesFileInfoFromDatabase22.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                notesFileInfoFromDatabase22.setNotesFileName(this.noteName);
                                notesFileInfoFromDatabase22.setNotesFileText(hashMap.get("Text"));
                                notesFileInfoFromDatabase22.setNotesFileCreatedDate(hashMap.get(str37));
                                notesFileInfoFromDatabase22.setNotesFileModifiedDate(hashMap.get(str38));
                                notesFileInfoFromDatabase22.setNotesfileColor(hashMap.get("NoteColor"));
                                notesFileInfoFromDatabase22.setNotesFileLocation(this.newFile.getAbsolutePath());
                                notesFileInfoFromDatabase22.setNotesFileFromCloud(0);
                                notesFileInfoFromDatabase22.setNotesFileSize(length);
                                notesFileInfoFromDatabase22.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                    NotesFilesDAL notesFilesDAL = this.notesFilesDAL;
                                    this.constants.getClass();
                                    notesFilesDAL.updateNotesFileInfoInDatabase(notesFileInfoFromDatabase22, str9, String.valueOf(notesFileInfoFromDatabase22.getNotesFileId()));
                                } else {
                                    this.notesFilesDAL.addNotesFilesInfoInDatabase(notesFileInfoFromDatabase22);
                                }
                                NotesFolderDAL notesFolderDAL222 = new NotesFolderDAL(activity);
                                new NotesFolderDB_Pojo();
                                StringBuilder sb322 = new StringBuilder();
                                this.constants.getClass();
                                sb322.append(str36);
                                this.constants.getClass();
                                sb322.append(str2);
                                sb322.append(" = ");
                                sb322.append(NotesCommon.CurrentNotesFolderId);
                                sb322.append(str3);
                                this.constants.getClass();
                                sb322.append(str4);
                                sb322.append(" = ");
                                sb322.append(SecurityLocksCommon.IsFakeAccount);
                                NotesFolderDB_Pojo notesFolderInfoFromDatabase22 = notesFolderDAL222.getNotesFolderInfoFromDatabase(sb322.toString());
                                notesFolderInfoFromDatabase22.setNotesFolderModifiedDate(hashMap.get(str38));
                                notesFolderInfoFromDatabase22.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                this.constants.getClass();
                                notesFolderDAL222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase22, str2, String.valueOf(notesFolderInfoFromDatabase22.getNotesFolderId()));
                                SecurityLocksCommon.IsAppDeactive = false;
                                activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                activity.finish();
                                return;
                            }
                            try {
                                this.notesFilesDAL = new NotesFilesDAL(activity);
                                new NotesFileDB_Pojo();
                                NotesFileDB_Pojo notesFileInfoFromDatabase4 = this.notesFilesDAL.getNotesFileInfoFromDatabase(concat);
                                str11 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                                try {
                                    try {
                                        notesFileInfoFromDatabase4.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                        notesFileInfoFromDatabase4.setNotesFileName(this.noteName);
                                        notesFileInfoFromDatabase4.setNotesFileText(hashMap.get("Text"));
                                        notesFileInfoFromDatabase4.setNotesFileCreatedDate(hashMap.get(str37));
                                        notesFileInfoFromDatabase4.setNotesFileModifiedDate(hashMap.get(str38));
                                        notesFileInfoFromDatabase4.setNotesfileColor(hashMap.get("NoteColor"));
                                        notesFileInfoFromDatabase4.setNotesFileLocation(this.newFile.getAbsolutePath());
                                        notesFileInfoFromDatabase4.setNotesFileFromCloud(0);
                                        notesFileInfoFromDatabase4.setNotesFileSize(d);
                                        notesFileInfoFromDatabase4.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        try {
                                            if (this.notesFilesDAL.IsFileAlreadyExist(concat)) {
                                                try {
                                                    NotesFilesDAL notesFilesDAL2 = this.notesFilesDAL;
                                                    this.constants.getClass();
                                                    d2 = d;
                                                    d = Double.parseDouble("NotesFileId");
                                                    try {
                                                        notesFilesDAL2.updateNotesFileInfoInDatabase(notesFileInfoFromDatabase4, String.valueOf(d), String.valueOf(notesFileInfoFromDatabase4.getNotesFileId()));
                                                        d = d;
                                                        d3 = d2;
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                        d4 = d2;
                                                        str14 = "NotesFolderId";
                                                        if (z) {
                                                            this.constants.getClass();
                                                            StringBuilder sb8 = new StringBuilder();
                                                            this.constants.getClass();
                                                            sb8.append("NotesFileName");
                                                            sb8.append(" = '");
                                                            sb8.append(str);
                                                            sb8.append(str12);
                                                            this.constants.getClass();
                                                            sb8.append(str13);
                                                            sb8.append(" = ");
                                                            sb8.append(SecurityLocksCommon.IsFakeAccount);
                                                            str16 = str11.concat(sb8.toString());
                                                            th2 = th;
                                                            str15 = str14;
                                                        } else {
                                                            th2 = th;
                                                            this.constants.getClass();
                                                            StringBuilder sb9 = new StringBuilder();
                                                            str15 = str14;
                                                            this.constants.getClass();
                                                            sb9.append("NotesFileName");
                                                            sb9.append(" = '");
                                                            sb9.append(this.noteName);
                                                            sb9.append(str12);
                                                            this.constants.getClass();
                                                            sb9.append(str13);
                                                            sb9.append(" = ");
                                                            sb9.append(SecurityLocksCommon.IsFakeAccount);
                                                            str16 = str11.concat(sb9.toString());
                                                        }
                                                        double length2 = this.newFile.exists() ? (double) this.newFile.length() : d4;
                                                        this.notesFilesDAL = new NotesFilesDAL(activity);
                                                        new NotesFileDB_Pojo();
                                                        NotesFileDB_Pojo notesFileInfoFromDatabase32 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                                        notesFileInfoFromDatabase32.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                                        notesFileInfoFromDatabase32.setNotesFileName(this.noteName);
                                                        notesFileInfoFromDatabase32.setNotesFileText(hashMap.get("Text"));
                                                        notesFileInfoFromDatabase32.setNotesFileCreatedDate(hashMap.get(str37));
                                                        notesFileInfoFromDatabase32.setNotesFileModifiedDate(hashMap.get(str38));
                                                        notesFileInfoFromDatabase32.setNotesfileColor(hashMap.get("NoteColor"));
                                                        notesFileInfoFromDatabase32.setNotesFileLocation(this.newFile.getAbsolutePath());
                                                        notesFileInfoFromDatabase32.setNotesFileFromCloud(0);
                                                        notesFileInfoFromDatabase32.setNotesFileSize(length2);
                                                        notesFileInfoFromDatabase32.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                                        if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                                            NotesFilesDAL notesFilesDAL3 = this.notesFilesDAL;
                                                            this.constants.getClass();
                                                            notesFilesDAL3.updateNotesFileInfoInDatabase(notesFileInfoFromDatabase32, String.valueOf(d), String.valueOf(notesFileInfoFromDatabase32.getNotesFileId()));
                                                        } else {
                                                            this.notesFilesDAL.addNotesFilesInfoInDatabase(notesFileInfoFromDatabase32);
                                                        }
                                                        NotesFolderDAL notesFolderDAL32 = new NotesFolderDAL(activity);
                                                        new NotesFolderDB_Pojo();
                                                        StringBuilder sb52 = new StringBuilder();
                                                        this.constants.getClass();
                                                        sb52.append(str36);
                                                        this.constants.getClass();
                                                        sb52.append(str15);
                                                        sb52.append(" = ");
                                                        sb52.append(NotesCommon.CurrentNotesFolderId);
                                                        sb52.append(str35);
                                                        this.constants.getClass();
                                                        sb52.append(str34);
                                                        sb52.append(" = ");
                                                        sb52.append(SecurityLocksCommon.IsFakeAccount);
                                                        NotesFolderDB_Pojo notesFolderInfoFromDatabase32 = notesFolderDAL32.getNotesFolderInfoFromDatabase(sb52.toString());
                                                        notesFolderInfoFromDatabase32.setNotesFolderModifiedDate(hashMap.get(str38));
                                                        notesFolderInfoFromDatabase32.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                                        this.constants.getClass();
                                                        notesFolderDAL32.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase32, str15, String.valueOf(notesFolderInfoFromDatabase32.getNotesFolderId()));
                                                        SecurityLocksCommon.IsAppDeactive = false;
                                                        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                                        activity.finish();
                                                        throw th2;
                                                    }
                                                } catch (Throwable th6) {
                                                    th = th6;
                                                    d2 = d;
                                                    d = Double.parseDouble("NotesFileId");
                                                }
                                            } else {
                                                d3 = d;
                                                d = Double.parseDouble("NotesFileId");
                                                try {
                                                    this.notesFilesDAL.addNotesFilesInfoInDatabase(notesFileInfoFromDatabase4);
                                                    d = d;
                                                    d3 = d3;
                                                } catch (Throwable th7) {
                                                    th = th7;
                                                    str14 = "NotesFolderId";
                                                    double d5 = z == true ? 1 : 0;
                                                    long j = z == true ? 1 : 0;
                                                    d4 = d5;
                                                    if (z) {
                                                    }
                                                    if (this.newFile.exists()) {
                                                    }
                                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                                    new NotesFileDB_Pojo();
                                                    NotesFileDB_Pojo notesFileInfoFromDatabase322 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                                    notesFileInfoFromDatabase322.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                                    notesFileInfoFromDatabase322.setNotesFileName(this.noteName);
                                                    notesFileInfoFromDatabase322.setNotesFileText(hashMap.get("Text"));
                                                    notesFileInfoFromDatabase322.setNotesFileCreatedDate(hashMap.get(str37));
                                                    notesFileInfoFromDatabase322.setNotesFileModifiedDate(hashMap.get(str38));
                                                    notesFileInfoFromDatabase322.setNotesfileColor(hashMap.get("NoteColor"));
                                                    notesFileInfoFromDatabase322.setNotesFileLocation(this.newFile.getAbsolutePath());
                                                    notesFileInfoFromDatabase322.setNotesFileFromCloud(0);
                                                    notesFileInfoFromDatabase322.setNotesFileSize(d4);
                                                    notesFileInfoFromDatabase322.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                                    if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                                    }
                                                    NotesFolderDAL notesFolderDAL322 = new NotesFolderDAL(activity);
                                                    new NotesFolderDB_Pojo();
                                                    StringBuilder sb522 = new StringBuilder();
                                                    this.constants.getClass();
                                                    sb522.append(str36);
                                                    this.constants.getClass();
                                                    sb522.append(str15);
                                                    sb522.append(" = ");
                                                    sb522.append(NotesCommon.CurrentNotesFolderId);
                                                    sb522.append(str35);
                                                    this.constants.getClass();
                                                    sb522.append(str34);
                                                    sb522.append(" = ");
                                                    sb522.append(SecurityLocksCommon.IsFakeAccount);
                                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase322 = notesFolderDAL322.getNotesFolderInfoFromDatabase(sb522.toString());
                                                    notesFolderInfoFromDatabase322.setNotesFolderModifiedDate(hashMap.get(str38));
                                                    notesFolderInfoFromDatabase322.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                                    this.constants.getClass();
                                                    notesFolderDAL322.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase322, str15, String.valueOf(notesFolderInfoFromDatabase322.getNotesFolderId()));
                                                    SecurityLocksCommon.IsAppDeactive = false;
                                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                                    activity.finish();
                                                    throw th2;
                                                }
                                            }
                                            notesFolderDAL = new NotesFolderDAL(activity);
                                            new NotesFolderDB_Pojo();
                                            sb = new StringBuilder();
                                            this.constants.getClass();
                                        } catch (Exception e7) {
                                            e = e7;
                                        }
                                    } catch (Exception e8) {
                                        e = e8;
                                        d3 = d;
                                        d = Double.parseDouble("NotesFileId");
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                    d3 = d;
                                    str14 = "NotesFolderId";
                                    d = Double.parseDouble("NotesFileId");
                                    double d52 = z == true ? 1 : 0;
                                    long j2 = z == true ? 1 : 0;
                                    d4 = d52;
                                    if (z) {
                                    }
                                    if (this.newFile.exists()) {
                                    }
                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                    new NotesFileDB_Pojo();
                                    NotesFileDB_Pojo notesFileInfoFromDatabase3222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                    notesFileInfoFromDatabase3222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                    notesFileInfoFromDatabase3222.setNotesFileName(this.noteName);
                                    notesFileInfoFromDatabase3222.setNotesFileText(hashMap.get("Text"));
                                    notesFileInfoFromDatabase3222.setNotesFileCreatedDate(hashMap.get(str37));
                                    notesFileInfoFromDatabase3222.setNotesFileModifiedDate(hashMap.get(str38));
                                    notesFileInfoFromDatabase3222.setNotesfileColor(hashMap.get("NoteColor"));
                                    notesFileInfoFromDatabase3222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                    notesFileInfoFromDatabase3222.setNotesFileFromCloud(0);
                                    notesFileInfoFromDatabase3222.setNotesFileSize(d4);
                                    notesFileInfoFromDatabase3222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                    }
                                    NotesFolderDAL notesFolderDAL3222 = new NotesFolderDAL(activity);
                                    new NotesFolderDB_Pojo();
                                    StringBuilder sb5222 = new StringBuilder();
                                    this.constants.getClass();
                                    sb5222.append(str36);
                                    this.constants.getClass();
                                    sb5222.append(str15);
                                    sb5222.append(" = ");
                                    sb5222.append(NotesCommon.CurrentNotesFolderId);
                                    sb5222.append(str35);
                                    this.constants.getClass();
                                    sb5222.append(str34);
                                    sb5222.append(" = ");
                                    sb5222.append(SecurityLocksCommon.IsFakeAccount);
                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase3222 = notesFolderDAL3222.getNotesFolderInfoFromDatabase(sb5222.toString());
                                    notesFolderInfoFromDatabase3222.setNotesFolderModifiedDate(hashMap.get(str38));
                                    notesFolderInfoFromDatabase3222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    this.constants.getClass();
                                    notesFolderDAL3222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase3222, str15, String.valueOf(notesFolderInfoFromDatabase3222.getNotesFolderId()));
                                    SecurityLocksCommon.IsAppDeactive = false;
                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                    activity.finish();
                                    throw th2;
                                }
                                try {
                                    sb.append(str36);
                                    this.constants.getClass();
                                    str14 = "NotesFolderId";
                                    try {
                                        sb.append(str14);
                                        sb.append(" = ");
                                        str36 = str36;
                                    } catch (Exception e9) {
                                        e = e9;
                                        str36 = str36;
                                    } catch (Throwable th9) {
                                        th = th9;
                                        str36 = str36;
                                    }
                                } catch (Exception e10) {
                                    e = e10;
                                    str36 = str36;
                                    d4 = d3;
                                    str4 = str34;
                                    str3 = str35;
                                    str2 = "NotesFolderId";
                                    str19 = String.valueOf(d);
                                    str5 = str13;
                                    str6 = str12;
                                    str7 = str11;
                                    str8 = str19;
                                    e.printStackTrace();
                                    if (z) {
                                    }
                                    if (this.newFile.exists()) {
                                    }
                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                    new NotesFileDB_Pojo();
                                    NotesFileDB_Pojo notesFileInfoFromDatabase222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                    notesFileInfoFromDatabase222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                    notesFileInfoFromDatabase222.setNotesFileName(this.noteName);
                                    notesFileInfoFromDatabase222.setNotesFileText(hashMap.get("Text"));
                                    notesFileInfoFromDatabase222.setNotesFileCreatedDate(hashMap.get(str37));
                                    notesFileInfoFromDatabase222.setNotesFileModifiedDate(hashMap.get(str38));
                                    notesFileInfoFromDatabase222.setNotesfileColor(hashMap.get("NoteColor"));
                                    notesFileInfoFromDatabase222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                    notesFileInfoFromDatabase222.setNotesFileFromCloud(0);
                                    notesFileInfoFromDatabase222.setNotesFileSize(d4);
                                    notesFileInfoFromDatabase222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                    }
                                    NotesFolderDAL notesFolderDAL2222 = new NotesFolderDAL(activity);
                                    new NotesFolderDB_Pojo();
                                    StringBuilder sb3222 = new StringBuilder();
                                    this.constants.getClass();
                                    sb3222.append(str36);
                                    this.constants.getClass();
                                    sb3222.append(str2);
                                    sb3222.append(" = ");
                                    sb3222.append(NotesCommon.CurrentNotesFolderId);
                                    sb3222.append(str3);
                                    this.constants.getClass();
                                    sb3222.append(str4);
                                    sb3222.append(" = ");
                                    sb3222.append(SecurityLocksCommon.IsFakeAccount);
                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase222 = notesFolderDAL2222.getNotesFolderInfoFromDatabase(sb3222.toString());
                                    notesFolderInfoFromDatabase222.setNotesFolderModifiedDate(hashMap.get(str38));
                                    notesFolderInfoFromDatabase222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    this.constants.getClass();
                                    notesFolderDAL2222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase222, str2, String.valueOf(notesFolderInfoFromDatabase222.getNotesFolderId()));
                                    SecurityLocksCommon.IsAppDeactive = false;
                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                    activity.finish();
                                    return;
                                } catch (Throwable th10) {
                                    th = th10;
                                    str36 = str36;
                                    str14 = "NotesFolderId";
                                    double d522 = z == true ? 1 : 0;
                                    long j22 = z == true ? 1 : 0;
                                    d4 = d522;
                                    if (z) {
                                    }
                                    if (this.newFile.exists()) {
                                    }
                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                    new NotesFileDB_Pojo();
                                    NotesFileDB_Pojo notesFileInfoFromDatabase32222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                    notesFileInfoFromDatabase32222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                    notesFileInfoFromDatabase32222.setNotesFileName(this.noteName);
                                    notesFileInfoFromDatabase32222.setNotesFileText(hashMap.get("Text"));
                                    notesFileInfoFromDatabase32222.setNotesFileCreatedDate(hashMap.get(str37));
                                    notesFileInfoFromDatabase32222.setNotesFileModifiedDate(hashMap.get(str38));
                                    notesFileInfoFromDatabase32222.setNotesfileColor(hashMap.get("NoteColor"));
                                    notesFileInfoFromDatabase32222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                    notesFileInfoFromDatabase32222.setNotesFileFromCloud(0);
                                    notesFileInfoFromDatabase32222.setNotesFileSize(d4);
                                    notesFileInfoFromDatabase32222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                    }
                                    NotesFolderDAL notesFolderDAL32222 = new NotesFolderDAL(activity);
                                    new NotesFolderDB_Pojo();
                                    StringBuilder sb52222 = new StringBuilder();
                                    this.constants.getClass();
                                    sb52222.append(str36);
                                    this.constants.getClass();
                                    sb52222.append(str15);
                                    sb52222.append(" = ");
                                    sb52222.append(NotesCommon.CurrentNotesFolderId);
                                    sb52222.append(str35);
                                    this.constants.getClass();
                                    sb52222.append(str34);
                                    sb52222.append(" = ");
                                    sb52222.append(SecurityLocksCommon.IsFakeAccount);
                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase32222 = notesFolderDAL32222.getNotesFolderInfoFromDatabase(sb52222.toString());
                                    notesFolderInfoFromDatabase32222.setNotesFolderModifiedDate(hashMap.get(str38));
                                    notesFolderInfoFromDatabase32222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    this.constants.getClass();
                                    notesFolderDAL32222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase32222, str15, String.valueOf(notesFolderInfoFromDatabase32222.getNotesFolderId()));
                                    SecurityLocksCommon.IsAppDeactive = false;
                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                    activity.finish();
                                    throw th2;
                                }
                            } catch (Exception e11) {
                                e = e11;
                                str30 = "NotesFileId";
                                str7 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                                d4 = d;
                                str4 = str34;
                                str3 = str35;
                                str2 = "NotesFolderId";
                                str5 = str13;
                                str6 = str12;
                                str8 = str30;
                                e.printStackTrace();
                                if (z) {
                                }
                                if (this.newFile.exists()) {
                                }
                                this.notesFilesDAL = new NotesFilesDAL(activity);
                                new NotesFileDB_Pojo();
                                NotesFileDB_Pojo notesFileInfoFromDatabase2222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                notesFileInfoFromDatabase2222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                notesFileInfoFromDatabase2222.setNotesFileName(this.noteName);
                                notesFileInfoFromDatabase2222.setNotesFileText(hashMap.get("Text"));
                                notesFileInfoFromDatabase2222.setNotesFileCreatedDate(hashMap.get(str37));
                                notesFileInfoFromDatabase2222.setNotesFileModifiedDate(hashMap.get(str38));
                                notesFileInfoFromDatabase2222.setNotesfileColor(hashMap.get("NoteColor"));
                                notesFileInfoFromDatabase2222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                notesFileInfoFromDatabase2222.setNotesFileFromCloud(0);
                                notesFileInfoFromDatabase2222.setNotesFileSize(d4);
                                notesFileInfoFromDatabase2222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                }
                                NotesFolderDAL notesFolderDAL22222 = new NotesFolderDAL(activity);
                                new NotesFolderDB_Pojo();
                                StringBuilder sb32222 = new StringBuilder();
                                this.constants.getClass();
                                sb32222.append(str36);
                                this.constants.getClass();
                                sb32222.append(str2);
                                sb32222.append(" = ");
                                sb32222.append(NotesCommon.CurrentNotesFolderId);
                                sb32222.append(str3);
                                this.constants.getClass();
                                sb32222.append(str4);
                                sb32222.append(" = ");
                                sb32222.append(SecurityLocksCommon.IsFakeAccount);
                                NotesFolderDB_Pojo notesFolderInfoFromDatabase2222 = notesFolderDAL22222.getNotesFolderInfoFromDatabase(sb32222.toString());
                                notesFolderInfoFromDatabase2222.setNotesFolderModifiedDate(hashMap.get(str38));
                                notesFolderInfoFromDatabase2222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                this.constants.getClass();
                                notesFolderDAL22222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase2222, str2, String.valueOf(notesFolderInfoFromDatabase2222.getNotesFolderId()));
                                SecurityLocksCommon.IsAppDeactive = false;
                                activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                activity.finish();
                                return;
                            } catch (Throwable th11) {
                                th = th11;
                                d3 = d;
                                str11 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                            }
                            try {
                                sb.append(NotesCommon.CurrentNotesFolderId);
                                try {
                                    sb.append(str35);
                                    str35 = str35;
                                    this.constants.getClass();
                                    try {
                                        sb.append(str34);
                                        sb.append(" = ");
                                        str34 = str34;
                                        sb.append(SecurityLocksCommon.IsFakeAccount);
                                        NotesFolderDB_Pojo notesFolderInfoFromDatabase4 = notesFolderDAL.getNotesFolderInfoFromDatabase(sb.toString());
                                        notesFolderInfoFromDatabase4.setNotesFolderModifiedDate(hashMap.get(str38));
                                        notesFolderInfoFromDatabase4.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        this.constants.getClass();
                                        notesFolderDAL.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase4, str14, String.valueOf(notesFolderInfoFromDatabase4.getNotesFolderId()));
                                        SecurityLocksCommon.IsAppDeactive = false;
                                        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                        activity.finish();
                                        return;
                                    } catch (Exception e12) {
                                        e = e12;
                                        str4 = str34;
                                        str2 = str14;
                                        d4 = d3;
                                        str20 = String.valueOf(d);
                                        str3 = str35;
                                        str19 = str20;
                                        str5 = str13;
                                        str6 = str12;
                                        str7 = str11;
                                        str8 = str19;
                                        e.printStackTrace();
                                        if (z) {
                                        }
                                        if (this.newFile.exists()) {
                                        }
                                        this.notesFilesDAL = new NotesFilesDAL(activity);
                                        new NotesFileDB_Pojo();
                                        NotesFileDB_Pojo notesFileInfoFromDatabase22222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                        notesFileInfoFromDatabase22222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                        notesFileInfoFromDatabase22222.setNotesFileName(this.noteName);
                                        notesFileInfoFromDatabase22222.setNotesFileText(hashMap.get("Text"));
                                        notesFileInfoFromDatabase22222.setNotesFileCreatedDate(hashMap.get(str37));
                                        notesFileInfoFromDatabase22222.setNotesFileModifiedDate(hashMap.get(str38));
                                        notesFileInfoFromDatabase22222.setNotesfileColor(hashMap.get("NoteColor"));
                                        notesFileInfoFromDatabase22222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                        notesFileInfoFromDatabase22222.setNotesFileFromCloud(0);
                                        notesFileInfoFromDatabase22222.setNotesFileSize(d4);
                                        notesFileInfoFromDatabase22222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                        }
                                        NotesFolderDAL notesFolderDAL222222 = new NotesFolderDAL(activity);
                                        new NotesFolderDB_Pojo();
                                        StringBuilder sb322222 = new StringBuilder();
                                        this.constants.getClass();
                                        sb322222.append(str36);
                                        this.constants.getClass();
                                        sb322222.append(str2);
                                        sb322222.append(" = ");
                                        sb322222.append(NotesCommon.CurrentNotesFolderId);
                                        sb322222.append(str3);
                                        this.constants.getClass();
                                        sb322222.append(str4);
                                        sb322222.append(" = ");
                                        sb322222.append(SecurityLocksCommon.IsFakeAccount);
                                        NotesFolderDB_Pojo notesFolderInfoFromDatabase22222 = notesFolderDAL222222.getNotesFolderInfoFromDatabase(sb322222.toString());
                                        notesFolderInfoFromDatabase22222.setNotesFolderModifiedDate(hashMap.get(str38));
                                        notesFolderInfoFromDatabase22222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        this.constants.getClass();
                                        notesFolderDAL222222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase22222, str2, String.valueOf(notesFolderInfoFromDatabase22222.getNotesFolderId()));
                                        SecurityLocksCommon.IsAppDeactive = false;
                                        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                        activity.finish();
                                        return;
                                    } catch (Throwable th12) {
                                        th = th12;
                                        str34 = str34;
                                        double d5222 = z == true ? 1 : 0;
                                        long j222 = z == true ? 1 : 0;
                                        d4 = d5222;
                                        if (z) {
                                        }
                                        if (this.newFile.exists()) {
                                        }
                                        this.notesFilesDAL = new NotesFilesDAL(activity);
                                        new NotesFileDB_Pojo();
                                        NotesFileDB_Pojo notesFileInfoFromDatabase322222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                        notesFileInfoFromDatabase322222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                        notesFileInfoFromDatabase322222.setNotesFileName(this.noteName);
                                        notesFileInfoFromDatabase322222.setNotesFileText(hashMap.get("Text"));
                                        notesFileInfoFromDatabase322222.setNotesFileCreatedDate(hashMap.get(str37));
                                        notesFileInfoFromDatabase322222.setNotesFileModifiedDate(hashMap.get(str38));
                                        notesFileInfoFromDatabase322222.setNotesfileColor(hashMap.get("NoteColor"));
                                        notesFileInfoFromDatabase322222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                        notesFileInfoFromDatabase322222.setNotesFileFromCloud(0);
                                        notesFileInfoFromDatabase322222.setNotesFileSize(d4);
                                        notesFileInfoFromDatabase322222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                        }
                                        NotesFolderDAL notesFolderDAL322222 = new NotesFolderDAL(activity);
                                        new NotesFolderDB_Pojo();
                                        StringBuilder sb522222 = new StringBuilder();
                                        this.constants.getClass();
                                        sb522222.append(str36);
                                        this.constants.getClass();
                                        sb522222.append(str15);
                                        sb522222.append(" = ");
                                        sb522222.append(NotesCommon.CurrentNotesFolderId);
                                        sb522222.append(str35);
                                        this.constants.getClass();
                                        sb522222.append(str34);
                                        sb522222.append(" = ");
                                        sb522222.append(SecurityLocksCommon.IsFakeAccount);
                                        NotesFolderDB_Pojo notesFolderInfoFromDatabase322222 = notesFolderDAL322222.getNotesFolderInfoFromDatabase(sb522222.toString());
                                        notesFolderInfoFromDatabase322222.setNotesFolderModifiedDate(hashMap.get(str38));
                                        notesFolderInfoFromDatabase322222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        this.constants.getClass();
                                        notesFolderDAL322222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase322222, str15, String.valueOf(notesFolderInfoFromDatabase322222.getNotesFolderId()));
                                        SecurityLocksCommon.IsAppDeactive = false;
                                        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                        activity.finish();
                                        throw th2;
                                    }
                                } catch (Exception e13) {
                                    e = e13;
                                    str3 = str35;
                                    str2 = str14;
                                    d4 = d3;
                                    str4 = str34;
                                    str19 = String.valueOf(d);
                                    str5 = str13;
                                    str6 = str12;
                                    str7 = str11;
                                    str8 = str19;
                                    e.printStackTrace();
                                    if (z) {
                                    }
                                    if (this.newFile.exists()) {
                                    }
                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                    new NotesFileDB_Pojo();
                                    NotesFileDB_Pojo notesFileInfoFromDatabase222222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                    notesFileInfoFromDatabase222222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                    notesFileInfoFromDatabase222222.setNotesFileName(this.noteName);
                                    notesFileInfoFromDatabase222222.setNotesFileText(hashMap.get("Text"));
                                    notesFileInfoFromDatabase222222.setNotesFileCreatedDate(hashMap.get(str37));
                                    notesFileInfoFromDatabase222222.setNotesFileModifiedDate(hashMap.get(str38));
                                    notesFileInfoFromDatabase222222.setNotesfileColor(hashMap.get("NoteColor"));
                                    notesFileInfoFromDatabase222222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                    notesFileInfoFromDatabase222222.setNotesFileFromCloud(0);
                                    notesFileInfoFromDatabase222222.setNotesFileSize(d4);
                                    notesFileInfoFromDatabase222222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                    }
                                    NotesFolderDAL notesFolderDAL2222222 = new NotesFolderDAL(activity);
                                    new NotesFolderDB_Pojo();
                                    StringBuilder sb3222222 = new StringBuilder();
                                    this.constants.getClass();
                                    sb3222222.append(str36);
                                    this.constants.getClass();
                                    sb3222222.append(str2);
                                    sb3222222.append(" = ");
                                    sb3222222.append(NotesCommon.CurrentNotesFolderId);
                                    sb3222222.append(str3);
                                    this.constants.getClass();
                                    sb3222222.append(str4);
                                    sb3222222.append(" = ");
                                    sb3222222.append(SecurityLocksCommon.IsFakeAccount);
                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase222222 = notesFolderDAL2222222.getNotesFolderInfoFromDatabase(sb3222222.toString());
                                    notesFolderInfoFromDatabase222222.setNotesFolderModifiedDate(hashMap.get(str38));
                                    notesFolderInfoFromDatabase222222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    this.constants.getClass();
                                    notesFolderDAL2222222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase222222, str2, String.valueOf(notesFolderInfoFromDatabase222222.getNotesFolderId()));
                                    SecurityLocksCommon.IsAppDeactive = false;
                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                    activity.finish();
                                    return;
                                } catch (Throwable th13) {
                                    th = th13;
                                    str35 = str35;
                                }
                            } catch (Exception e14) {
                                e = e14;
                                str2 = str14;
                                d4 = d3;
                                str21 = String.valueOf(d);
                                str4 = str34;
                                str20 = str21;
                                str3 = str35;
                                str19 = str20;
                                str5 = str13;
                                str6 = str12;
                                str7 = str11;
                                str8 = str19;
                                e.printStackTrace();
                                if (z) {
                                }
                                if (this.newFile.exists()) {
                                }
                                this.notesFilesDAL = new NotesFilesDAL(activity);
                                new NotesFileDB_Pojo();
                                NotesFileDB_Pojo notesFileInfoFromDatabase2222222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                notesFileInfoFromDatabase2222222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                notesFileInfoFromDatabase2222222.setNotesFileName(this.noteName);
                                notesFileInfoFromDatabase2222222.setNotesFileText(hashMap.get("Text"));
                                notesFileInfoFromDatabase2222222.setNotesFileCreatedDate(hashMap.get(str37));
                                notesFileInfoFromDatabase2222222.setNotesFileModifiedDate(hashMap.get(str38));
                                notesFileInfoFromDatabase2222222.setNotesfileColor(hashMap.get("NoteColor"));
                                notesFileInfoFromDatabase2222222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                notesFileInfoFromDatabase2222222.setNotesFileFromCloud(0);
                                notesFileInfoFromDatabase2222222.setNotesFileSize(d4);
                                notesFileInfoFromDatabase2222222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                }
                                NotesFolderDAL notesFolderDAL22222222 = new NotesFolderDAL(activity);
                                new NotesFolderDB_Pojo();
                                StringBuilder sb32222222 = new StringBuilder();
                                this.constants.getClass();
                                sb32222222.append(str36);
                                this.constants.getClass();
                                sb32222222.append(str2);
                                sb32222222.append(" = ");
                                sb32222222.append(NotesCommon.CurrentNotesFolderId);
                                sb32222222.append(str3);
                                this.constants.getClass();
                                sb32222222.append(str4);
                                sb32222222.append(" = ");
                                sb32222222.append(SecurityLocksCommon.IsFakeAccount);
                                NotesFolderDB_Pojo notesFolderInfoFromDatabase2222222 = notesFolderDAL22222222.getNotesFolderInfoFromDatabase(sb32222222.toString());
                                notesFolderInfoFromDatabase2222222.setNotesFolderModifiedDate(hashMap.get(str38));
                                notesFolderInfoFromDatabase2222222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                this.constants.getClass();
                                notesFolderDAL22222222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase2222222, str2, String.valueOf(notesFolderInfoFromDatabase2222222.getNotesFolderId()));
                                SecurityLocksCommon.IsAppDeactive = false;
                                activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                activity.finish();
                                return;
                            } catch (Throwable th14) {
                                th = th14;
                            }
                        } else {
                            str13 = "NotesFileIsDecoy";
                            str12 = "' AND ";
                            d = Double.parseDouble("NotesFileId");
                            str11 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                            str14 = "NotesFolderId";
                            try {
                                this.newFile.createNewFile();
                                str22 = String.valueOf(d);
                                str23 = str14;
                                str24 = str34;
                                str25 = str35;
                                str26 = str36;
                                str27 = str13;
                                str28 = str12;
                                str29 = str11;
                            } catch (Exception e15) {
                                e = e15;
                                str2 = str14;
                                str21 = String.valueOf(d);
                                str4 = str34;
                                str20 = str21;
                                str3 = str35;
                                str19 = str20;
                                str5 = str13;
                                str6 = str12;
                                str7 = str11;
                                str8 = str19;
                                e.printStackTrace();
                                if (z) {
                                }
                                if (this.newFile.exists()) {
                                }
                                this.notesFilesDAL = new NotesFilesDAL(activity);
                                new NotesFileDB_Pojo();
                                NotesFileDB_Pojo notesFileInfoFromDatabase22222222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str10);
                                notesFileInfoFromDatabase22222222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                notesFileInfoFromDatabase22222222.setNotesFileName(this.noteName);
                                notesFileInfoFromDatabase22222222.setNotesFileText(hashMap.get("Text"));
                                notesFileInfoFromDatabase22222222.setNotesFileCreatedDate(hashMap.get(str37));
                                notesFileInfoFromDatabase22222222.setNotesFileModifiedDate(hashMap.get(str38));
                                notesFileInfoFromDatabase22222222.setNotesfileColor(hashMap.get("NoteColor"));
                                notesFileInfoFromDatabase22222222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                notesFileInfoFromDatabase22222222.setNotesFileFromCloud(0);
                                notesFileInfoFromDatabase22222222.setNotesFileSize(d4);
                                notesFileInfoFromDatabase22222222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                if (this.notesFilesDAL.IsFileAlreadyExist(str10)) {
                                }
                                NotesFolderDAL notesFolderDAL222222222 = new NotesFolderDAL(activity);
                                new NotesFolderDB_Pojo();
                                StringBuilder sb322222222 = new StringBuilder();
                                this.constants.getClass();
                                sb322222222.append(str36);
                                this.constants.getClass();
                                sb322222222.append(str2);
                                sb322222222.append(" = ");
                                sb322222222.append(NotesCommon.CurrentNotesFolderId);
                                sb322222222.append(str3);
                                this.constants.getClass();
                                sb322222222.append(str4);
                                sb322222222.append(" = ");
                                sb322222222.append(SecurityLocksCommon.IsFakeAccount);
                                NotesFolderDB_Pojo notesFolderInfoFromDatabase22222222 = notesFolderDAL222222222.getNotesFolderInfoFromDatabase(sb322222222.toString());
                                notesFolderInfoFromDatabase22222222.setNotesFolderModifiedDate(hashMap.get(str38));
                                notesFolderInfoFromDatabase22222222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                this.constants.getClass();
                                notesFolderDAL222222222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase22222222, str2, String.valueOf(notesFolderInfoFromDatabase22222222.getNotesFolderId()));
                                SecurityLocksCommon.IsAppDeactive = false;
                                activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                activity.finish();
                                return;
                            } catch (Throwable th15) {
                                th = th15;
                                if (z) {
                                }
                                if (this.newFile.exists()) {
                                }
                                this.notesFilesDAL = new NotesFilesDAL(activity);
                                new NotesFileDB_Pojo();
                                NotesFileDB_Pojo notesFileInfoFromDatabase3222222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str16);
                                notesFileInfoFromDatabase3222222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                notesFileInfoFromDatabase3222222.setNotesFileName(this.noteName);
                                notesFileInfoFromDatabase3222222.setNotesFileText(hashMap.get("Text"));
                                notesFileInfoFromDatabase3222222.setNotesFileCreatedDate(hashMap.get(str37));
                                notesFileInfoFromDatabase3222222.setNotesFileModifiedDate(hashMap.get(str38));
                                notesFileInfoFromDatabase3222222.setNotesfileColor(hashMap.get("NoteColor"));
                                notesFileInfoFromDatabase3222222.setNotesFileLocation(this.newFile.getAbsolutePath());
                                notesFileInfoFromDatabase3222222.setNotesFileFromCloud(0);
                                notesFileInfoFromDatabase3222222.setNotesFileSize(d4);
                                notesFileInfoFromDatabase3222222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                if (this.notesFilesDAL.IsFileAlreadyExist(str16)) {
                                }
                                NotesFolderDAL notesFolderDAL3222222 = new NotesFolderDAL(activity);
                                new NotesFolderDB_Pojo();
                                StringBuilder sb5222222 = new StringBuilder();
                                this.constants.getClass();
                                sb5222222.append(str36);
                                this.constants.getClass();
                                sb5222222.append(str15);
                                sb5222222.append(" = ");
                                sb5222222.append(NotesCommon.CurrentNotesFolderId);
                                sb5222222.append(str35);
                                this.constants.getClass();
                                sb5222222.append(str34);
                                sb5222222.append(" = ");
                                sb5222222.append(SecurityLocksCommon.IsFakeAccount);
                                NotesFolderDB_Pojo notesFolderInfoFromDatabase3222222 = notesFolderDAL3222222.getNotesFolderInfoFromDatabase(sb5222222.toString());
                                notesFolderInfoFromDatabase3222222.setNotesFolderModifiedDate(hashMap.get(str38));
                                notesFolderInfoFromDatabase3222222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                this.constants.getClass();
                                notesFolderDAL3222222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase3222222, str15, String.valueOf(notesFolderInfoFromDatabase3222222.getNotesFolderId()));
                                SecurityLocksCommon.IsAppDeactive = false;
                                activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                activity.finish();
                                throw th2;
                            }
                        }
                    } catch (Exception e16) {
                        e = e16;
                        str17 = "NotesFileIsDecoy";
                        str18 = "NotesFileId";
                    }
                } catch (Throwable th16) {
                    th = th16;
                    str13 = "NotesFileIsDecoy";
                    str12 = "' AND ";
                    d = Double.parseDouble("NotesFileId");
                    str11 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
                }
            } catch (Exception e17) {
                e = e17;
                str4 = str34;
                str3 = str35;
                str2 = "NotesFolderId";
                str5 = "NotesFileIsDecoy";
                str8 = "NotesFileId";
                str6 = "' AND ";
                str7 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
            }
        } else {
            str24 = str34;
            str25 = str35;
            str23 = "NotesFolderId";
            str26 = str36;
            str27 = "NotesFileIsDecoy";
            str28 = "' AND ";
            str29 = "SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ";
            if (this.noteName.equals(str)) {
                str22 = "NotesFileId";
                if (!this.newFile.exists()) {
                    try {
                        this.newFile.createNewFile();
                    } catch (IOException e18) {
                        e18.printStackTrace();
                    }
                }
            } else if (this.oldFile.exists()) {
                str22 = "NotesFileId";
                this.oldFile.renameTo(this.newFile);
            } else {
                str22 = "NotesFileId";
            }
        }
        XmlSerializer newSerializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        try {
            newSerializer.setOutput(stringWriter);
            obj = "NoteColor";
        } catch (IOException e19) {
            e2 = e19;
            obj = "NoteColor";
        } catch (Exception e20) {
            e3 = e20;
            obj = "NoteColor";
        }
        try {
            newSerializer.startDocument(null, true);
            newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            newSerializer.startTag(null, "dict");
            Iterator<Map.Entry<String, String>> it = hashMap.entrySet().iterator();
            String str39 = String.valueOf(d3);
            while (it.hasNext()) {
                Map.Entry<String, String> next = it.next();
                str32 = str37;
                if (!next.getKey().equals(str37)) {
                    try {
                        if (!next.getKey().equals(str38)) {
                            str31 = str38;
                            if (next.getKey().equals("Text")) {
                                try {
                                    newSerializer.startTag(null, "key");
                                    newSerializer.text(next.getKey());
                                    newSerializer.endTag(null, "key");
                                    newSerializer.startTag(null, "string");
                                    newSerializer.text(Flaes.getencodedstring(next.getValue()));
                                    newSerializer.endTag(null, "string");
                                } catch (IOException e21) {
                                    e2 = e21;
                                    e2.printStackTrace();
                                    if (z) {
                                    }
                                    if (this.newFile.exists()) {
                                    }
                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                    new NotesFileDB_Pojo();
                                    NotesFileDB_Pojo notesFileInfoFromDatabase5 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
                                    notesFileInfoFromDatabase5.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                    notesFileInfoFromDatabase5.setNotesFileName(this.noteName);
                                    notesFileInfoFromDatabase5.setNotesFileText(hashMap.get("Text"));
                                    notesFileInfoFromDatabase5.setNotesFileCreatedDate(hashMap.get(str32));
                                    notesFileInfoFromDatabase5.setNotesFileModifiedDate(hashMap.get(str31));
                                    notesFileInfoFromDatabase5.setNotesfileColor(hashMap.get(obj));
                                    notesFileInfoFromDatabase5.setNotesFileLocation(this.newFile.getAbsolutePath());
                                    notesFileInfoFromDatabase5.setNotesFileFromCloud(0);
                                    notesFileInfoFromDatabase5.setNotesFileSize(d3);
                                    notesFileInfoFromDatabase5.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
                                    }
                                    NotesFolderDAL notesFolderDAL4 = new NotesFolderDAL(activity);
                                    new NotesFolderDB_Pojo();
                                    StringBuilder sb10 = new StringBuilder();
                                    this.constants.getClass();
                                    sb10.append(str26);
                                    this.constants.getClass();
                                    sb10.append(str23);
                                    sb10.append(" = ");
                                    sb10.append(NotesCommon.CurrentNotesFolderId);
                                    sb10.append(str25);
                                    this.constants.getClass();
                                    sb10.append(str24);
                                    sb10.append(" = ");
                                    sb10.append(SecurityLocksCommon.IsFakeAccount);
                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase5 = notesFolderDAL4.getNotesFolderInfoFromDatabase(sb10.toString());
                                    notesFolderInfoFromDatabase5.setNotesFolderModifiedDate(hashMap.get(str31));
                                    notesFolderInfoFromDatabase5.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    this.constants.getClass();
                                    notesFolderDAL4.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase5, str23, String.valueOf(notesFolderInfoFromDatabase5.getNotesFolderId()));
                                    SecurityLocksCommon.IsAppDeactive = false;
                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                    activity.finish();
                                } catch (Exception e22) {
                                    e3 = e22;
                                    e3.printStackTrace();
                                    if (z) {
                                    }
                                    if (this.newFile.exists()) {
                                    }
                                    this.notesFilesDAL = new NotesFilesDAL(activity);
                                    new NotesFileDB_Pojo();
                                    NotesFileDB_Pojo notesFileInfoFromDatabase52 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
                                    notesFileInfoFromDatabase52.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                                    notesFileInfoFromDatabase52.setNotesFileName(this.noteName);
                                    notesFileInfoFromDatabase52.setNotesFileText(hashMap.get("Text"));
                                    notesFileInfoFromDatabase52.setNotesFileCreatedDate(hashMap.get(str32));
                                    notesFileInfoFromDatabase52.setNotesFileModifiedDate(hashMap.get(str31));
                                    notesFileInfoFromDatabase52.setNotesfileColor(hashMap.get(obj));
                                    notesFileInfoFromDatabase52.setNotesFileLocation(this.newFile.getAbsolutePath());
                                    notesFileInfoFromDatabase52.setNotesFileFromCloud(0);
                                    notesFileInfoFromDatabase52.setNotesFileSize(d4);
                                    notesFileInfoFromDatabase52.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
                                    }
                                    NotesFolderDAL notesFolderDAL42 = new NotesFolderDAL(activity);
                                    new NotesFolderDB_Pojo();
                                    StringBuilder sb102 = new StringBuilder();
                                    this.constants.getClass();
                                    sb102.append(str26);
                                    this.constants.getClass();
                                    sb102.append(str23);
                                    sb102.append(" = ");
                                    sb102.append(NotesCommon.CurrentNotesFolderId);
                                    sb102.append(str25);
                                    this.constants.getClass();
                                    sb102.append(str24);
                                    sb102.append(" = ");
                                    sb102.append(SecurityLocksCommon.IsFakeAccount);
                                    NotesFolderDB_Pojo notesFolderInfoFromDatabase52 = notesFolderDAL42.getNotesFolderInfoFromDatabase(sb102.toString());
                                    notesFolderInfoFromDatabase52.setNotesFolderModifiedDate(hashMap.get(str31));
                                    notesFolderInfoFromDatabase52.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    this.constants.getClass();
                                    notesFolderDAL42.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase52, str23, String.valueOf(notesFolderInfoFromDatabase52.getNotesFolderId()));
                                    SecurityLocksCommon.IsAppDeactive = false;
                                    activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                                    activity.finish();
                                }
                            } else {
                                newSerializer.startTag(null, "key");
                                newSerializer.text(next.getKey());
                                newSerializer.endTag(null, "key");
                                newSerializer.startTag(null, "string");
                                newSerializer.text(next.getValue());
                                newSerializer.endTag(null, "string");
                            }
                            newSerializer.startTag(null, "key");
                            newSerializer.text(next.getKey());
                            newSerializer.endTag(null, "key");
                            newSerializer.startTag(null, str39);
                            newSerializer.text(next.getValue());
                            newSerializer.endTag(null, str39);
                            str39 = str39;
                            str37 = str32;
                            it = it;
                            str38 = str31;
                        }
                    } catch (IOException e23) {
                        e2 = e23;
                        str31 = str38;
                        e2.printStackTrace();
                        if (z) {
                        }
                        if (this.newFile.exists()) {
                        }
                        this.notesFilesDAL = new NotesFilesDAL(activity);
                        new NotesFileDB_Pojo();
                        NotesFileDB_Pojo notesFileInfoFromDatabase522 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
                        notesFileInfoFromDatabase522.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                        notesFileInfoFromDatabase522.setNotesFileName(this.noteName);
                        notesFileInfoFromDatabase522.setNotesFileText(hashMap.get("Text"));
                        notesFileInfoFromDatabase522.setNotesFileCreatedDate(hashMap.get(str32));
                        notesFileInfoFromDatabase522.setNotesFileModifiedDate(hashMap.get(str31));
                        notesFileInfoFromDatabase522.setNotesfileColor(hashMap.get(obj));
                        notesFileInfoFromDatabase522.setNotesFileLocation(this.newFile.getAbsolutePath());
                        notesFileInfoFromDatabase522.setNotesFileFromCloud(0);
                        notesFileInfoFromDatabase522.setNotesFileSize(d4);
                        notesFileInfoFromDatabase522.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                        if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
                        }
                        NotesFolderDAL notesFolderDAL422 = new NotesFolderDAL(activity);
                        new NotesFolderDB_Pojo();
                        StringBuilder sb1022 = new StringBuilder();
                        this.constants.getClass();
                        sb1022.append(str26);
                        this.constants.getClass();
                        sb1022.append(str23);
                        sb1022.append(" = ");
                        sb1022.append(NotesCommon.CurrentNotesFolderId);
                        sb1022.append(str25);
                        this.constants.getClass();
                        sb1022.append(str24);
                        sb1022.append(" = ");
                        sb1022.append(SecurityLocksCommon.IsFakeAccount);
                        NotesFolderDB_Pojo notesFolderInfoFromDatabase522 = notesFolderDAL422.getNotesFolderInfoFromDatabase(sb1022.toString());
                        notesFolderInfoFromDatabase522.setNotesFolderModifiedDate(hashMap.get(str31));
                        notesFolderInfoFromDatabase522.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                        this.constants.getClass();
                        notesFolderDAL422.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase522, str23, String.valueOf(notesFolderInfoFromDatabase522.getNotesFolderId()));
                        SecurityLocksCommon.IsAppDeactive = false;
                        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                        activity.finish();
                    } catch (Exception e24) {
                        e3 = e24;
                        str31 = str38;
                        e3.printStackTrace();
                        if (z) {
                        }
                        if (this.newFile.exists()) {
                        }
                        this.notesFilesDAL = new NotesFilesDAL(activity);
                        new NotesFileDB_Pojo();
                        NotesFileDB_Pojo notesFileInfoFromDatabase5222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
                        notesFileInfoFromDatabase5222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
                        notesFileInfoFromDatabase5222.setNotesFileName(this.noteName);
                        notesFileInfoFromDatabase5222.setNotesFileText(hashMap.get("Text"));
                        notesFileInfoFromDatabase5222.setNotesFileCreatedDate(hashMap.get(str32));
                        notesFileInfoFromDatabase5222.setNotesFileModifiedDate(hashMap.get(str31));
                        notesFileInfoFromDatabase5222.setNotesfileColor(hashMap.get(obj));
                        notesFileInfoFromDatabase5222.setNotesFileLocation(this.newFile.getAbsolutePath());
                        notesFileInfoFromDatabase5222.setNotesFileFromCloud(0);
                        notesFileInfoFromDatabase5222.setNotesFileSize(d4);
                        notesFileInfoFromDatabase5222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                        if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
                        }
                        NotesFolderDAL notesFolderDAL4222 = new NotesFolderDAL(activity);
                        new NotesFolderDB_Pojo();
                        StringBuilder sb10222 = new StringBuilder();
                        this.constants.getClass();
                        sb10222.append(str26);
                        this.constants.getClass();
                        sb10222.append(str23);
                        sb10222.append(" = ");
                        sb10222.append(NotesCommon.CurrentNotesFolderId);
                        sb10222.append(str25);
                        this.constants.getClass();
                        sb10222.append(str24);
                        sb10222.append(" = ");
                        sb10222.append(SecurityLocksCommon.IsFakeAccount);
                        NotesFolderDB_Pojo notesFolderInfoFromDatabase5222 = notesFolderDAL4222.getNotesFolderInfoFromDatabase(sb10222.toString());
                        notesFolderInfoFromDatabase5222.setNotesFolderModifiedDate(hashMap.get(str31));
                        notesFolderInfoFromDatabase5222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                        this.constants.getClass();
                        notesFolderDAL4222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase5222, str23, String.valueOf(notesFolderInfoFromDatabase5222.getNotesFolderId()));
                        SecurityLocksCommon.IsAppDeactive = false;
                        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
                        activity.finish();
                    }
                }
                str31 = str38;
                newSerializer.startTag(null, "key");
                newSerializer.text(next.getKey());
                newSerializer.endTag(null, "key");
                newSerializer.startTag(null, str39);
                newSerializer.text(next.getValue());
                newSerializer.endTag(null, str39);
                str39 = str39;
                str37 = str32;
                it = it;
                str38 = str31;
            }
            str32 = str37;
            str31 = str38;
            newSerializer.endTag(null, "dict");
            newSerializer.endDocument();
            newSerializer.flush();
            FileOutputStream fileOutputStream = new FileOutputStream(this.newFile);
            fileOutputStream.write(stringWriter.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e25) {
            e2 = e25;
            str32 = str37;
            str31 = str38;
            e2.printStackTrace();
            if (z) {
            }
            if (this.newFile.exists()) {
            }
            this.notesFilesDAL = new NotesFilesDAL(activity);
            new NotesFileDB_Pojo();
            NotesFileDB_Pojo notesFileInfoFromDatabase52222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
            notesFileInfoFromDatabase52222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
            notesFileInfoFromDatabase52222.setNotesFileName(this.noteName);
            notesFileInfoFromDatabase52222.setNotesFileText(hashMap.get("Text"));
            notesFileInfoFromDatabase52222.setNotesFileCreatedDate(hashMap.get(str32));
            notesFileInfoFromDatabase52222.setNotesFileModifiedDate(hashMap.get(str31));
            notesFileInfoFromDatabase52222.setNotesfileColor(hashMap.get(obj));
            notesFileInfoFromDatabase52222.setNotesFileLocation(this.newFile.getAbsolutePath());
            notesFileInfoFromDatabase52222.setNotesFileFromCloud(0);
            notesFileInfoFromDatabase52222.setNotesFileSize(d4);
            notesFileInfoFromDatabase52222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
            if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
            }
            NotesFolderDAL notesFolderDAL42222 = new NotesFolderDAL(activity);
            new NotesFolderDB_Pojo();
            StringBuilder sb102222 = new StringBuilder();
            this.constants.getClass();
            sb102222.append(str26);
            this.constants.getClass();
            sb102222.append(str23);
            sb102222.append(" = ");
            sb102222.append(NotesCommon.CurrentNotesFolderId);
            sb102222.append(str25);
            this.constants.getClass();
            sb102222.append(str24);
            sb102222.append(" = ");
            sb102222.append(SecurityLocksCommon.IsFakeAccount);
            NotesFolderDB_Pojo notesFolderInfoFromDatabase52222 = notesFolderDAL42222.getNotesFolderInfoFromDatabase(sb102222.toString());
            notesFolderInfoFromDatabase52222.setNotesFolderModifiedDate(hashMap.get(str31));
            notesFolderInfoFromDatabase52222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
            this.constants.getClass();
            notesFolderDAL42222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase52222, str23, String.valueOf(notesFolderInfoFromDatabase52222.getNotesFolderId()));
            SecurityLocksCommon.IsAppDeactive = false;
            activity.startActivity(new Intent(activity, NotesFilesActivity.class));
            activity.finish();
        } catch (Exception e26) {
            e3 = e26;
            str32 = str37;
            str31 = str38;
            e3.printStackTrace();
            if (z) {
            }
            if (this.newFile.exists()) {
            }
            this.notesFilesDAL = new NotesFilesDAL(activity);
            new NotesFileDB_Pojo();
            NotesFileDB_Pojo notesFileInfoFromDatabase522222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
            notesFileInfoFromDatabase522222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
            notesFileInfoFromDatabase522222.setNotesFileName(this.noteName);
            notesFileInfoFromDatabase522222.setNotesFileText(hashMap.get("Text"));
            notesFileInfoFromDatabase522222.setNotesFileCreatedDate(hashMap.get(str32));
            notesFileInfoFromDatabase522222.setNotesFileModifiedDate(hashMap.get(str31));
            notesFileInfoFromDatabase522222.setNotesfileColor(hashMap.get(obj));
            notesFileInfoFromDatabase522222.setNotesFileLocation(this.newFile.getAbsolutePath());
            notesFileInfoFromDatabase522222.setNotesFileFromCloud(0);
            notesFileInfoFromDatabase522222.setNotesFileSize(d4);
            notesFileInfoFromDatabase522222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
            if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
            }
            NotesFolderDAL notesFolderDAL422222 = new NotesFolderDAL(activity);
            new NotesFolderDB_Pojo();
            StringBuilder sb1022222 = new StringBuilder();
            this.constants.getClass();
            sb1022222.append(str26);
            this.constants.getClass();
            sb1022222.append(str23);
            sb1022222.append(" = ");
            sb1022222.append(NotesCommon.CurrentNotesFolderId);
            sb1022222.append(str25);
            this.constants.getClass();
            sb1022222.append(str24);
            sb1022222.append(" = ");
            sb1022222.append(SecurityLocksCommon.IsFakeAccount);
            NotesFolderDB_Pojo notesFolderInfoFromDatabase522222 = notesFolderDAL422222.getNotesFolderInfoFromDatabase(sb1022222.toString());
            notesFolderInfoFromDatabase522222.setNotesFolderModifiedDate(hashMap.get(str31));
            notesFolderInfoFromDatabase522222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
            this.constants.getClass();
            notesFolderDAL422222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase522222, str23, String.valueOf(notesFolderInfoFromDatabase522222.getNotesFolderId()));
            SecurityLocksCommon.IsAppDeactive = false;
            activity.startActivity(new Intent(activity, NotesFilesActivity.class));
            activity.finish();
        }
        if (z) {
            this.constants.getClass();
            StringBuilder sb11 = new StringBuilder();
            this.constants.getClass();
            sb11.append("NotesFileName");
            sb11.append(" = '");
            sb11.append(str);
            sb11.append(str28);
            this.constants.getClass();
            sb11.append(str27);
            sb11.append(" = ");
            sb11.append(SecurityLocksCommon.IsFakeAccount);
            str33 =
            str29.concat(sb11.toString());
        } else {
            this.constants.getClass();
            StringBuilder sb12 = new StringBuilder();
            this.constants.getClass();
            sb12.append("NotesFileName");
            sb12.append(" = '");
            sb12.append(this.noteName);
            sb12.append(str28);
            this.constants.getClass();
            sb12.append(str27);
            sb12.append(" = ");
            sb12.append(SecurityLocksCommon.IsFakeAccount);
            str33 = str29.concat(sb12.toString());
        }
        double length3 = this.newFile.exists() ? (double) this.newFile.length() : 0.0d;
        this.notesFilesDAL = new NotesFilesDAL(activity);
        new NotesFileDB_Pojo();
        NotesFileDB_Pojo notesFileInfoFromDatabase5222222 = this.notesFilesDAL.getNotesFileInfoFromDatabase(str33);
        notesFileInfoFromDatabase5222222.setNotesFileFolderId(NotesCommon.CurrentNotesFolderId);
        notesFileInfoFromDatabase5222222.setNotesFileName(this.noteName);
        notesFileInfoFromDatabase5222222.setNotesFileText(hashMap.get("Text"));
        notesFileInfoFromDatabase5222222.setNotesFileCreatedDate(hashMap.get(str32));
        notesFileInfoFromDatabase5222222.setNotesFileModifiedDate(hashMap.get(str31));
        notesFileInfoFromDatabase5222222.setNotesfileColor(hashMap.get(obj));
        notesFileInfoFromDatabase5222222.setNotesFileLocation(this.newFile.getAbsolutePath());
        notesFileInfoFromDatabase5222222.setNotesFileFromCloud(0);
        notesFileInfoFromDatabase5222222.setNotesFileSize(length3);
        notesFileInfoFromDatabase5222222.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
        if (this.notesFilesDAL.IsFileAlreadyExist(str33)) {
            NotesFilesDAL notesFilesDAL4 = this.notesFilesDAL;
            this.constants.getClass();
            notesFilesDAL4.updateNotesFileInfoInDatabase(notesFileInfoFromDatabase5222222, str22, String.valueOf(notesFileInfoFromDatabase5222222.getNotesFileId()));
        } else {
            this.notesFilesDAL.addNotesFilesInfoInDatabase(notesFileInfoFromDatabase5222222);
        }
        NotesFolderDAL notesFolderDAL4222222 = new NotesFolderDAL(activity);
        new NotesFolderDB_Pojo();
        StringBuilder sb10222222 = new StringBuilder();
        this.constants.getClass();
        sb10222222.append(str26);
        this.constants.getClass();
        sb10222222.append(str23);
        sb10222222.append(" = ");
        sb10222222.append(NotesCommon.CurrentNotesFolderId);
        sb10222222.append(str25);
        this.constants.getClass();
        sb10222222.append(str24);
        sb10222222.append(" = ");
        sb10222222.append(SecurityLocksCommon.IsFakeAccount);
        NotesFolderDB_Pojo notesFolderInfoFromDatabase5222222 = notesFolderDAL4222222.getNotesFolderInfoFromDatabase(sb10222222.toString());
        notesFolderInfoFromDatabase5222222.setNotesFolderModifiedDate(hashMap.get(str31));
        notesFolderInfoFromDatabase5222222.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
        this.constants.getClass();
        notesFolderDAL4222222.updateNotesFolderFromDatabase(notesFolderInfoFromDatabase5222222, str23, String.valueOf(notesFolderInfoFromDatabase5222222.getNotesFolderId()));
        SecurityLocksCommon.IsAppDeactive = false;
        activity.startActivity(new Intent(activity, NotesFilesActivity.class));
        activity.finish();
    }
}
