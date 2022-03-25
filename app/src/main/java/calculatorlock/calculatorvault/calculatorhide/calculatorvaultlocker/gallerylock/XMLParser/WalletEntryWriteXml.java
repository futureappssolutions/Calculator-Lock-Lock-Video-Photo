package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser;

import android.content.Context;
import android.util.Xml;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Flaes;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesFieldPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletEntryFileDB_Pojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletEntryPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.wallet.WalletCommon;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;


public class WalletEntryWriteXml {
    WalletEntryFileDB_Pojo entryFileinfoModel;

    public static boolean write(WalletEntryPojo walletEntryPojo, Context context, Boolean bool) {
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.WALLET + WalletCommon.walletCurrentCategoryName);
        String absolutePath = file.getAbsolutePath();
        File file2 = new File(absolutePath, StorageOptionsCommon.ENTRY + walletEntryPojo.getEntryName() + StorageOptionsCommon.WALLET_ENTRY_FILE_EXTENSION);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            if (bool.booleanValue() && !file2.exists()) {
                file2.createNewFile();
            }
            XmlSerializer newSerializer = Xml.newSerializer();
            StringWriter stringWriter = new StringWriter();
            newSerializer.setOutput(stringWriter);
            newSerializer.startDocument(null, true);
            newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            newSerializer.startTag(null, "EntryInfo");
            newSerializer.startTag(null, "CategoryName");
            newSerializer.text(walletEntryPojo.getCategoryName());
            newSerializer.endTag(null, "CategoryName");
            newSerializer.startTag(null, "EntryName");
            newSerializer.text(walletEntryPojo.getEntryName());
            newSerializer.endTag(null, "EntryName");
            for (WalletCategoriesFieldPojo walletCategoriesFieldPojo : walletEntryPojo.getFields()) {
                newSerializer.startTag(null, "Field");
                newSerializer.startTag(null, "Name");
                newSerializer.text(walletCategoriesFieldPojo.getFieldName());
                newSerializer.endTag(null, "Name");
                newSerializer.startTag(null, "Value");
                if (walletCategoriesFieldPojo.getFieldValue().equals("")) {
                    newSerializer.text("none");
                } else {
                    newSerializer.text(walletCategoriesFieldPojo.getFieldValue());
                }
                newSerializer.endTag(null, "Value");
                newSerializer.startTag(null, "IsSecured");
                newSerializer.text(String.valueOf(walletCategoriesFieldPojo.isSecured()));
                newSerializer.endTag(null, "IsSecured");
                newSerializer.endTag(null, "Field");
            }
            newSerializer.endTag(null, "EntryInfo");
            newSerializer.endDocument();
            newSerializer.flush();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(Flaes.getencodedstring(stringWriter.toString()).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
