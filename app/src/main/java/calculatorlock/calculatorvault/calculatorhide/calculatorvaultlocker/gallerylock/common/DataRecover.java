package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common;

import android.content.Context;
import android.util.Log;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DocumentDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.DocumentFolder;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.DocumentFolderDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.DocumentsEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.NotesFileDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.NotesFilesDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.NotesFolderDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.NotesFolderDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.ReadNoteFromXML;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Photo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.PhotoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.PhotoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.Video;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.VideoAlbum;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoAlbumDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.VideoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.WalletCategoriesDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesFileDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.wallet.WalletCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.WalletEntriesDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletEntryFileDB_Pojo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DataRecover {
    Context context;

    public void RecoverALLData(Context context) {
        this.context = context;
        if (!StorageOptionsCommon.IsDeviceHaveMoreThenOneStorage) {
            RecoverPhotos(StorageOptionsCommon.STORAGEPATH, true);
            RecoverVideos(StorageOptionsCommon.STORAGEPATH, true);
            RecoverDocuments(StorageOptionsCommon.STORAGEPATH, true);
            RecoverNotes(StorageOptionsCommon.STORAGEPATH, true);
            RecoverWalletEntries(StorageOptionsCommon.STORAGEPATH, true);
        } else if (!StorageOptionsCommon.STORAGEPATH.equals(StorageOptionsCommon.STORAGEPATH_1)) {
            RecoverPhotos(StorageOptionsCommon.STORAGEPATH, true);
            RecoverPhotos(StorageOptionsCommon.STORAGEPATH_1, false);
            RecoverVideos(StorageOptionsCommon.STORAGEPATH, true);
            RecoverVideos(StorageOptionsCommon.STORAGEPATH_1, false);
            RecoverDocuments(StorageOptionsCommon.STORAGEPATH, true);
            RecoverDocuments(StorageOptionsCommon.STORAGEPATH_1, false);
            RecoverNotes(StorageOptionsCommon.STORAGEPATH, true);
            RecoverNotes(StorageOptionsCommon.STORAGEPATH_1, false);
            RecoverWalletEntries(StorageOptionsCommon.STORAGEPATH, true);
            RecoverWalletEntries(StorageOptionsCommon.STORAGEPATH_1, false);
        } else if (!StorageOptionsCommon.STORAGEPATH.equals(StorageOptionsCommon.STORAGEPATH_2)) {
            RecoverPhotos(StorageOptionsCommon.STORAGEPATH, true);
            RecoverPhotos(StorageOptionsCommon.STORAGEPATH_2, false);
            RecoverVideos(StorageOptionsCommon.STORAGEPATH, true);
            RecoverVideos(StorageOptionsCommon.STORAGEPATH_2, false);
            RecoverDocuments(StorageOptionsCommon.STORAGEPATH, true);
            RecoverDocuments(StorageOptionsCommon.STORAGEPATH_2, false);
            RecoverNotes(StorageOptionsCommon.STORAGEPATH, true);
            RecoverNotes(StorageOptionsCommon.STORAGEPATH_2, false);
            RecoverWalletEntries(StorageOptionsCommon.STORAGEPATH, true);
            RecoverWalletEntries(StorageOptionsCommon.STORAGEPATH_2, false);
        }
    }

    private void RecoverPhotos(String str, boolean z) {
        boolean z2;
        String str2;
        boolean z3;
        File file = new File(str + StorageOptionsCommon.PHOTOS + "/");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            boolean z4 = false;
            assert listFiles != null;
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    File[] listFiles2 = file2.listFiles();
                    assert listFiles2 != null;
                    for (File file3 : listFiles2) {
                        if (file3.isFile()) {
                            String absolutePath = file3.getAbsolutePath();
                            if (!z) {
                                try {
                                    absolutePath = Utilities.RecoveryHideFileSDCard(this.context, file3, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + "/" + file2.getName()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    z4 = true;
                                }
                            }
                            if (!z4) {
                                if (file3.getName().contains("#")) {
                                    str2 = Utilities.ChangeFileExtentionToOrignal(file3.getName());
                                } else {
                                    str2 = file3.getName();
                                }
                                String str3 = StorageOptionsCommon.STORAGEPATH + "Calculator Vault Unhide Data/" + str2;
                                PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this.context);
                                photoAlbumDAL.OpenRead();
                                int IfAlbumNameExistReturnId = photoAlbumDAL.IfAlbumNameExistReturnId(file2.getName());
                                if (IfAlbumNameExistReturnId == 0) {
                                    AddPhotoAlbumToDatabase(file2.getName());
                                    IfAlbumNameExistReturnId = photoAlbumDAL.GetLastAlbumId();
                                }
                                photoAlbumDAL.close();
                                PhotoDAL photoDAL = new PhotoDAL(this.context);
                                photoDAL.OpenRead();
                                if (absolutePath.contains("'")) {
                                    z2 = z4;
                                    z3 = photoDAL.IsFileAlreadyExist(absolutePath.replaceAll("'", "''"));
                                } else {
                                    z2 = z4;
                                    z3 = photoDAL.IsFileAlreadyExist(absolutePath);
                                }
                                photoDAL.close();
                                if (!z3) {
                                    StorageOptionsCommon.IsUserHasDataToRecover = true;
                                    AddPhotoToDatabase(file3.getName(), absolutePath, str3, IfAlbumNameExistReturnId);
                                }
                            } else {
                                z2 = z4;
                            }
                            z4 = z2;
                        }
                    }
                }
            }
        }
    }

    private void RecoverVideos(String str, boolean z) {
        int i;
        int i2;
        String str2;
        boolean z2 = false;
        String str3;
        int i3;
        boolean z3;
        String str4 = null;
        File file;
        String str5;
        IOException e;
        DataRecover dataRecover = this;
        String str6 = str;
        File file2 = new File(str6 + StorageOptionsCommon.VIDEOS);
        if (file2.exists()) {
            File[] listFiles = file2.listFiles();
            assert listFiles != null;
            int length = listFiles.length;
            boolean z4 = false;
            int i4 = 0;
            while (i4 < length) {
                File file3 = listFiles[i4];
                if (file3.isDirectory() && !file3.getName().equals("VideoThumnails")) {
                    File[] listFiles2 = file3.listFiles();
                    assert listFiles2 != null;
                    int length2 = listFiles2.length;
                    int i5 = 0;
                    while (i5 < length2) {
                        File file4 = listFiles2[i5];
                        if (file4.isFile()) {
                            String absolutePath = file4.getAbsolutePath();
                            if (!z) {
                                boolean z5 = z4;
                                String sb2 = StorageOptionsCommon.STORAGEPATH +
                                        StorageOptionsCommon.VIDEOS +
                                        file3.getName() +
                                        "/";
                                try {
                                    str5 = absolutePath;
                                    try {
                                        str4 = Utilities.RecoveryHideFileSDCard(dataRecover.context, file4, new File(sb2));
                                    } catch (IOException e2) {
                                        e = e2;
                                        e.printStackTrace();
                                        str4 = str5;
                                        z5 = true;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(sb2);
                                        str2 = str4;
                                        sb3.append("VideoThumnails/");
                                        file = new File(sb3.toString());
                                        if (!file.exists()) {
                                        }
                                        String name = file4.getName();
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str6);
                                        i2 = i5;
                                        sb4.append(StorageOptionsCommon.VIDEOS);
                                        sb4.append(file3.getName());
                                        sb4.append("/VideoThumnails/thumbnil-");
                                        sb4.append(name.substring(0, name.lastIndexOf("#")));
                                        sb4.append("#jpg");
                                        Utilities.UnHideThumbnail(dataRecover.context, sb4.toString(), sb2 + "VideoThumnails/thumbnil-" + file4.getName().substring(0, file4.getName().lastIndexOf("#")) + "#jpg");
                                        z4 = z5;
                                        if (z4) {
                                        }
                                        i = i2;
                                        z4 = z2;
                                        i5 = i + 1;
                                        dataRecover = this;
                                        str6 = str;
                                    }
                                } catch (IOException e3) {
                                    e = e3;
                                    str5 = absolutePath;
                                }
                                StringBuilder sb32 = new StringBuilder();
                                sb32.append(sb2);
                                str2 = str4;
                                sb32.append("VideoThumnails/");
                                file = new File(sb32.toString());
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                String name2 = file4.getName();
                                StringBuilder sb42 = new StringBuilder();
                                sb42.append(str6);
                                i2 = i5;
                                sb42.append(StorageOptionsCommon.VIDEOS);
                                sb42.append(file3.getName());
                                sb42.append("/VideoThumnails/thumbnil-");
                                sb42.append(name2.substring(0, name2.lastIndexOf("#")));
                                sb42.append("#jpg");
                                try {
                                    Utilities.UnHideThumbnail(dataRecover.context, sb42.toString(), sb2 + "VideoThumnails/thumbnil-" + file4.getName().substring(0, file4.getName().lastIndexOf("#")) + "#jpg");
                                    z4 = z5;
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                    z4 = true;
                                }
                            } else {
                                str2 = absolutePath;
                                i2 = i5;
                            }
                            if (z4) {
                                if (file4.getName().contains("#")) {
                                    str3 = Utilities.ChangeFileExtentionToOrignal(file4.getName());
                                } else {
                                    str3 = file4.getName();
                                }
                                String str7 = StorageOptionsCommon.STORAGEPATH + "Calculator Vault Unhide Data/" + str3;
                                VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(dataRecover.context);
                                videoAlbumDAL.OpenRead();
                                int IfAlbumNameExistReturnId = videoAlbumDAL.IfAlbumNameExistReturnId(file3.getName());
                                if (IfAlbumNameExistReturnId == 0) {
                                    dataRecover.AddVideoAlbumToDatabase(file3.getName());
                                    z2 = z4;
                                    if (!new File(file3.getAbsolutePath()).exists()) {
                                        file3.mkdirs();
                                    }
                                    i3 = videoAlbumDAL.GetLastAlbumId();
                                } else {
                                    z2 = z4;
                                    i3 = IfAlbumNameExistReturnId;
                                }
                                videoAlbumDAL.close();
                                VideoDAL videoDAL = new VideoDAL(dataRecover.context);
                                videoDAL.OpenRead();
                                if (str2.contains("'")) {
                                    z3 = videoDAL.IsFileAlreadyExist(str2.replaceAll("'", "''"));
                                } else {
                                    z3 = videoDAL.IsFileAlreadyExist(str2);
                                }
                                videoDAL.close();
                                if (!z3) {
                                    String str8 = StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.VIDEOS + file3.getName() + "/VideoThumnails/thumbnil-" + file4.getName().substring(0, file4.getName().lastIndexOf("#")) + "#jpg";
                                    StorageOptionsCommon.IsUserHasDataToRecover = true;
                                    if (new File(str8).exists()) {
                                        i = i2;
                                        AddVideoToDatabase(file4.getName(), str2, str7, str8, i3);
                                    } else {
                                        i = i2;
                                    }
                                    z4 = z2;
                                }
                            } else {
                                z2 = z4;
                            }
                            i = i2;
                            z4 = z2;
                        } else {
                            i = i5;
                        }
                        i5 = i + 1;
                        dataRecover = this;
                        str6 = str;
                    }
                }
                i4++;
                dataRecover = this;
                str6 = str;
            }
        }
    }

    private void RecoverDocuments(String str, boolean z) {
        boolean z2;
        String str2;
        boolean z3;
        File file = new File(str + StorageOptionsCommon.DOCUMENTS + "/");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            boolean z4 = false;
            assert listFiles != null;
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    File[] listFiles2 = file2.listFiles();
                    assert listFiles2 != null;
                    for (File file3 : listFiles2) {
                        if (file3.isFile()) {
                            String absolutePath = file3.getAbsolutePath();
                            if (!z) {
                                try {
                                    absolutePath = Utilities.RecoveryHideFileSDCard(this.context, file3, new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + "/" + file2.getName()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    z4 = true;
                                }
                            }
                            if (!z4) {
                                if (file3.getName().contains("#")) {
                                    str2 = Utilities.ChangeFileExtentionToOrignal(file3.getName());
                                } else {
                                    str2 = file3.getName();
                                }
                                String str3 = StorageOptionsCommon.STORAGEPATH + Common.UnhideKitkatAlbumName + str2;
                                DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this.context);
                                documentFolderDAL.OpenRead();
                                int IfFolderNameExistReturnId = documentFolderDAL.IfFolderNameExistReturnId(file2.getName());
                                if (IfFolderNameExistReturnId == 0) {
                                    AddDocumentFolderToDatabase(file2.getName());
                                    IfFolderNameExistReturnId = documentFolderDAL.GetLastFolderId();
                                }
                                documentFolderDAL.close();
                                DocumentDAL documentDAL = new DocumentDAL(this.context);
                                documentDAL.OpenRead();
                                if (absolutePath.contains("'")) {
                                    z2 = z4;
                                    z3 = documentDAL.IsFileAlreadyExist(absolutePath.replaceAll("'", "''"));
                                } else {
                                    z2 = z4;
                                    z3 = documentDAL.IsFileAlreadyExist(absolutePath);
                                }
                                documentDAL.close();
                                if (!z3) {
                                    StorageOptionsCommon.IsUserHasDataToRecover = true;
                                    AddDocumentToDatabase(file3.getName(), absolutePath, str3, IfFolderNameExistReturnId);
                                }
                            } else {
                                z2 = z4;
                            }
                            z4 = z2;
                        }
                    }
                }
            }
        }
    }

    private void RecoverNotes(String str, boolean z) {
        int i;
        int i2;
        String str2;
        int i3;
        File[] fileArr;
        int i4;
        File[] fileArr2;
        String str3;
        boolean z2;
        boolean z3;
        DataRecover dataRecover = this;
        NotesFilesDAL notesFilesDAL = new NotesFilesDAL(dataRecover.context);
        NotesFolderDAL notesFolderDAL = new NotesFolderDAL(dataRecover.context);
        Constants constants = new Constants();
        File file = new File(str + StorageOptionsCommon.NOTES + "/");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            assert listFiles != null;
            int length = listFiles.length;
            int i5 = 0;
            boolean z4 = false;
            int i6 = 0;
            while (i6 < length) {
                File file2 = listFiles[i6];
                if (file2.isDirectory()) {
                    String name = file2.getName();
                    File[] listFiles2 = file2.listFiles();
                    assert listFiles2 != null;
                    int length2 = listFiles2.length;
                    int i7 = 0;
                    while (i7 < length2) {
                        File file3 = listFiles2[i7];
                        if (file3.isFile()) {
                            String name2 = file3.getName();
                            String substring = name2.substring(i5, name2.lastIndexOf("."));
                            String absolutePath = file3.getAbsolutePath();
                            File file4 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES + name);
                            StringBuilder sb = new StringBuilder();
                            sb.append(substring);
                            fileArr2 = listFiles;
                            sb.append(StorageOptionsCommon.NOTES_FILE_EXTENSION);
                            File file5 = new File(file4, sb.toString());
                            i4 = length;
                            if (!z) {
                                try {
                                    if (!file4.exists()) {
                                        file4.mkdirs();
                                    }
                                    absolutePath = Utilities.RecoveryEntryFile(dataRecover.context, file3, file5);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    str3 = absolutePath;
                                    z2 = true;
                                }
                            }
                            z2 = z4;
                            str3 = absolutePath;
                            if (!z2) {
                                StringBuilder sb2 = new StringBuilder();
                                constants.getClass();
                                sb2.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ");
                                constants.getClass();
                                sb2.append("NotesFileName");
                                sb2.append(" = '");
                                sb2.append(substring);
                                sb2.append("' AND ");
                                constants.getClass();
                                z3 = z2;
                                sb2.append("NotesFileIsDecoy");
                                sb2.append(" = ");
                                sb2.append(SecurityLocksCommon.IsFakeAccount);
                                String sb3 = sb2.toString();
                                StringBuilder sb4 = new StringBuilder();
                                constants.getClass();
                                fileArr = listFiles2;
                                sb4.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFolder WHERE ");
                                constants.getClass();
                                sb4.append("NotesFolderName");
                                sb4.append(" = '");
                                sb4.append(name);
                                sb4.append("' AND ");
                                constants.getClass();
                                sb4.append("NotesFolderIsDecoy");
                                sb4.append(" = ");
                                sb4.append(SecurityLocksCommon.IsFakeAccount);
                                String sb5 = sb4.toString();
                                double length3 = (double) str3.length();
                                new HashMap();
                                NotesFileDB_Pojo notesFileDB_Pojo = new NotesFileDB_Pojo();
                                NotesFolderDB_Pojo notesFolderDB_Pojo = new NotesFolderDB_Pojo();
                                i3 = length2;
                                HashMap<String, String> ReadNote = new ReadNoteFromXML().ReadNote(str3);
                                if (ReadNote != null) {
                                    i2 = i6;
                                    i = i7;
                                    if (!notesFolderDAL.IsFolderAlreadyExist(sb5)) {
                                        notesFolderDB_Pojo.setNotesFolderName(name);
                                        str2 = name;
                                        notesFolderDB_Pojo.setNotesFolderLocation(file2.getAbsolutePath());
                                        notesFolderDB_Pojo.setNotesFolderCreatedDate(ReadNote.get("note_datetime_c"));
                                        notesFolderDB_Pojo.setNotesFolderModifiedDate(ReadNote.get("note_datetime_m"));
                                        notesFolderDB_Pojo.setNotesFolderFilesSortBy(1);
                                        notesFolderDB_Pojo.setNotesFolderIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                        notesFolderDAL.addNotesFolderInfoInDatabase(notesFolderDB_Pojo);
                                    } else {
                                        str2 = name;
                                    }
                                    new NotesFolderDB_Pojo();
                                    notesFileDB_Pojo.setNotesFileFolderId(notesFolderDAL.getNotesFolderInfoFromDatabase(sb5).getNotesFolderId());
                                    notesFileDB_Pojo.setNotesFileName(substring);
                                    notesFileDB_Pojo.setNotesFileText(ReadNote.get("Text"));
                                    notesFileDB_Pojo.setNotesFileCreatedDate(ReadNote.get("note_datetime_c"));
                                    notesFileDB_Pojo.setNotesFileModifiedDate(ReadNote.get("note_datetime_m"));
                                    notesFileDB_Pojo.setNotesFileLocation(str3);
                                    notesFileDB_Pojo.setNotesFileFromCloud(0);
                                    notesFileDB_Pojo.setNotesFileSize(length3);
                                    notesFileDB_Pojo.setNotesFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    if (notesFilesDAL.IsFileAlreadyExist(sb3)) {
                                        constants.getClass();
                                        notesFilesDAL.updateNotesFileInfoInDatabase(notesFileDB_Pojo, "NotesFileId", String.valueOf(notesFileDB_Pojo.getNotesFileId()));
                                    } else {
                                        notesFilesDAL.addNotesFilesInfoInDatabase(notesFileDB_Pojo);
                                    }
                                    z4 = z3;
                                } else {
                                    i2 = i6;
                                    str2 = name;
                                }
                            } else {
                                z3 = z2;
                                i2 = i6;
                                str2 = name;
                                fileArr = listFiles2;
                                i3 = length2;
                            }
                            i = i7;
                            z4 = z3;
                        } else {
                            fileArr2 = listFiles;
                            i4 = length;
                            i2 = i6;
                            str2 = name;
                            fileArr = listFiles2;
                            i3 = length2;
                            i = i7;
                        }
                        i7 = i + 1;
                        dataRecover = this;
                        listFiles = fileArr2;
                        length = i4;
                        listFiles2 = fileArr;
                        length2 = i3;
                        name = str2;
                        i6 = i2;
                        i5 = 0;
                    }
                }
                i6++;
                dataRecover = this;
                listFiles = listFiles;
                length = length;
                i5 = 0;
            }
        }
    }

    private void RecoverWalletEntries(String str, boolean z) {
        int i;
        int i2;
        int i3;
        File[] fileArr;
        int i4;
        File[] fileArr2;
        String str2;
        boolean z2;
        boolean z3;
        DataRecover dataRecover = this;
        WalletEntriesDAL walletEntriesDAL = new WalletEntriesDAL(dataRecover.context);
        WalletCategoriesDAL walletCategoriesDAL = new WalletCategoriesDAL(dataRecover.context);
        Constants constants = new Constants();
        WalletCommon walletCommon = new WalletCommon();
        File file = new File(str + StorageOptionsCommon.WALLET + "/");
        walletCommon.createDefaultCategories(dataRecover.context);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            int length = listFiles.length;
            boolean z4 = false;
            int i5 = 0;
            while (i5 < length) {
                File file2 = listFiles[i5];
                if (file2.isDirectory()) {
                    String name = file2.getName();
                    File[] listFiles2 = file2.listFiles();
                    int length2 = listFiles2.length;
                    int i6 = 0;
                    while (i6 < length2) {
                        File file3 = listFiles2[i6];
                        if (file3.isFile()) {
                            String fileNameWithoutExtention = Utilities.getFileNameWithoutExtention(file3.getName());
                            String absolutePath = file3.getAbsolutePath();
                            StringBuilder sb = new StringBuilder();
                            fileArr2 = listFiles;
                            sb.append(StorageOptionsCommon.STORAGEPATH);
                            sb.append(StorageOptionsCommon.WALLET);
                            sb.append(name);
                            File file4 = new File(sb.toString());
                            StringBuilder sb2 = new StringBuilder();
                            i4 = length;
                            sb2.append(StorageOptionsCommon.ENTRY);
                            sb2.append(fileNameWithoutExtention);
                            sb2.append(StorageOptionsCommon.NOTES_FILE_EXTENSION);
                            File file5 = new File(file4, sb2.toString());
                            fileArr = listFiles2;
                            if (!z) {
                                try {
                                    if (!file4.exists()) {
                                        file4.mkdirs();
                                    }
                                    absolutePath = Utilities.RecoveryEntryFile(dataRecover.context, file3, file5);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    str2 = absolutePath;
                                    z2 = true;
                                }
                            }
                            str2 = absolutePath;
                            z2 = z4;
                            if (!z2) {
                                StringBuilder sb3 = new StringBuilder();
                                constants.getClass();
                                sb3.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletEntries WHERE ");
                                constants.getClass();
                                sb3.append("WalletEntryFileIsDecoy");
                                sb3.append(" = ");
                                sb3.append(SecurityLocksCommon.IsFakeAccount);
                                sb3.append(" AND ");
                                constants.getClass();
                                z3 = z2;
                                sb3.append("WalletEntryFileName");
                                sb3.append(" = '");
                                sb3.append(fileNameWithoutExtention);
                                i3 = length2;
                                sb3.append("'");
                                String sb4 = sb3.toString();
                                i2 = i5;
                                StringBuilder sb5 = new StringBuilder();
                                constants.getClass();
                                i = i6;
                                sb5.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableWalletCategories WHERE ");
                                constants.getClass();
                                sb5.append("WalletCategoriesFileIsDecoy");
                                sb5.append(" = ");
                                sb5.append(SecurityLocksCommon.IsFakeAccount);
                                sb5.append(" AND ");
                                constants.getClass();
                                sb5.append("WalletCategoriesFileName");
                                sb5.append(" = '");
                                sb5.append(name);
                                sb5.append("'");
                                String sb6 = sb5.toString();
                                WalletEntryFileDB_Pojo walletEntryFileDB_Pojo = new WalletEntryFileDB_Pojo();
                                WalletCategoriesFileDB_Pojo walletCategoriesFileDB_Pojo = new WalletCategoriesFileDB_Pojo();
                                WalletCategoriesPojo currentCategoryData = walletCommon.getCurrentCategoryData(dataRecover.context, name);
                                String currentDate = walletCommon.getCurrentDate();
                                if (!walletCategoriesDAL.IsWalletCategoryAlreadyExist(sb6)) {
                                    walletCategoriesFileDB_Pojo.setCategoryFileName(name);
                                    walletCategoriesFileDB_Pojo.setCategoryFileLocation(file2.getAbsolutePath());
                                    walletCategoriesFileDB_Pojo.setCategoryFileCreatedDate(currentDate);
                                    walletCategoriesFileDB_Pojo.setCategoryFileModifiedDate(currentDate);
                                    walletCategoriesFileDB_Pojo.setCategoryFileSortBy(1);
                                    walletCategoriesFileDB_Pojo.setCategoryFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    walletCategoriesDAL.addWalletCategoriesInfoInDatabase(walletCategoriesFileDB_Pojo);
                                } else {
                                    WalletCategoriesFileDB_Pojo categoryInfoFromDatabase = walletCategoriesDAL.getCategoryInfoFromDatabase(sb6);
                                    categoryInfoFromDatabase.setCategoryFileModifiedDate(currentDate);
                                    categoryInfoFromDatabase.setCategoryFileLocation(file2.getAbsolutePath());
                                    categoryInfoFromDatabase.setCategoryFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                    constants.getClass();
                                    walletCategoriesDAL.updateCategoryFromDatabase(categoryInfoFromDatabase, "WalletCategoriesFileId", String.valueOf(categoryInfoFromDatabase.getCategoryFileId()));
                                }
                                walletEntryFileDB_Pojo.setCategoryId(walletCategoriesDAL.getCategoryInfoFromDatabase(sb6).getCategoryFileId());
                                walletEntryFileDB_Pojo.setEntryFileName(fileNameWithoutExtention);
                                walletEntryFileDB_Pojo.setEntryFileCreatedDate(currentDate);
                                walletEntryFileDB_Pojo.setEntryFileModifiedDate(currentDate);
                                walletEntryFileDB_Pojo.setEntryFileLocation(str2);
                                walletEntryFileDB_Pojo.setCategoryFileIconIndex(currentCategoryData.getCategoryIconIndex());
                                walletEntryFileDB_Pojo.setEntryFileIsDecoy(SecurityLocksCommon.IsFakeAccount);
                                if (walletEntriesDAL.IsWalletEntryAlreadyExist(sb4)) {
                                    constants.getClass();
                                    walletEntriesDAL.updateEntryInDatabase(walletEntryFileDB_Pojo, "WalletEntryFileId", String.valueOf(walletEntryFileDB_Pojo.getEntryFileId()));
                                } else {
                                    walletEntriesDAL.addWalletEntriesInfoInDatabase(walletEntryFileDB_Pojo);
                                }
                            } else {
                                z3 = z2;
                                i2 = i5;
                                i3 = length2;
                                i = i6;
                            }
                            z4 = z3;
                        } else {
                            fileArr2 = listFiles;
                            i4 = length;
                            i2 = i5;
                            fileArr = listFiles2;
                            i3 = length2;
                            i = i6;
                        }
                        i6 = i + 1;
                        dataRecover = this;
                        listFiles = fileArr2;
                        length = i4;
                        listFiles2 = fileArr;
                        length2 = i3;
                        i5 = i2;
                    }
                }
                i5++;
                dataRecover = this;
                listFiles = listFiles;
                length = length;
            }
        }
    }

    private void AddPhotoAlbumToDatabase(String str) {
        PhotoAlbum photoAlbum = new PhotoAlbum();
        photoAlbum.setAlbumName(str);
        photoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.PHOTOS + str);
        PhotoAlbumDAL photoAlbumDAL = new PhotoAlbumDAL(this.context);
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

    private void AddVideoAlbumToDatabase(String str) {
        VideoAlbum videoAlbum = new VideoAlbum();
        videoAlbum.setAlbumName(str);
        videoAlbum.setAlbumLocation(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.VIDEOS + str);
        VideoAlbumDAL videoAlbumDAL = new VideoAlbumDAL(this.context);
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

    private void AddPhotoToDatabase(String str, String str2, String str3, int i) {
        if (str.contains("#")) {
            str = Utilities.ChangeFileExtentionToOrignal(str);
        }
        Log.d("Path", str2);
        Photo photo = new Photo();
        photo.setPhotoName(str);
        photo.setFolderLockPhotoLocation(str2);
        photo.setOriginalPhotoLocation(str3);
        photo.setAlbumId(i);
        PhotoDAL photoDAL = new PhotoDAL(this.context);
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

    private void AddVideoToDatabase(String str, String str2, String str3, String str4, int i) {
        if (str.contains("#")) {
            str = Utilities.ChangeFileExtentionToOrignal(str);
        }
        Video video = new Video();
        video.setVideoName(str);
        video.setFolderLockVideoLocation(str2);
        video.setOriginalVideoLocation(str3);
        video.setthumbnail_video_location(str4);
        video.setAlbumId(i);
        VideoDAL videoDAL = new VideoDAL(this.context);
        try {
            videoDAL.OpenWrite();
            videoDAL.AddVideos(video);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            videoDAL.close();
            throw th;
        }
        videoDAL.close();
    }

    private void AddDocumentFolderToDatabase(String str) {
        DocumentFolder documentFolder = new DocumentFolder();
        documentFolder.setFolderName(str);
        documentFolder.setFolderLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + str);
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(this.context);
        try {
            documentFolderDAL.OpenWrite();
            documentFolderDAL.AddDocumentFolder(documentFolder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentFolderDAL.close();
            throw th;
        }
        documentFolderDAL.close();
    }

    private void AddDocumentToDatabase(String str, String str2, String str3, int i) {
        if (str.contains("#")) {
            str = Utilities.ChangeFileExtentionToOrignal(str);
        }
        DocumentsEnt documentsEnt = new DocumentsEnt();
        documentsEnt.setDocumentName(str);
        documentsEnt.setFolderLockDocumentLocation(str2);
        documentsEnt.setOriginalDocumentLocation(str3);
        documentsEnt.setFolderId(i);
        DocumentDAL documentDAL = new DocumentDAL(this.context);
        try {
            documentDAL.OpenWrite();
            documentDAL.AddDocuments(documentsEnt, str2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentDAL.close();
            throw th;
        }
        documentDAL.close();
    }
}
