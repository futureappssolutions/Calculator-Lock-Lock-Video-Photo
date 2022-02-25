package com.calculator.vaultlocker.XMLParser;

import android.content.Context;

import com.calculator.vaultlocker.DB.DocumentFolderDAL;
import com.calculator.vaultlocker.Model.DocumentFolder;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;

public class DocumentsFolderGalleryMethods {
    public void AddFolderToDatabase(Context context, String str) {
        DocumentFolder documentFolder = new DocumentFolder();
        documentFolder.setFolderName(str);
        documentFolder.setFolderLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + str);
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(context);
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

    public void UpdateAlbumInDatabase(Context context, int i, String str) {
        DocumentFolder documentFolder = new DocumentFolder();
        documentFolder.setId(i);
        documentFolder.setFolderName(str);
        documentFolder.setFolderLocation(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.DOCUMENTS + str);
        DocumentFolderDAL documentFolderDAL = new DocumentFolderDAL(context);
        try {
            documentFolderDAL.OpenWrite();
            documentFolderDAL.UpdateFolderName(documentFolder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            documentFolderDAL.close();
            throw th;
        }
        documentFolderDAL.close();
    }
}
