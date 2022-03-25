package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser;

import android.content.Context;
import android.util.Log;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Flaes;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesFieldPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletEntryPojo;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EntryReadXml {
    public static WalletEntryPojo parseXML(Context context, String str) {
        ArrayList<WalletCategoriesFieldPojo> arrayList = new ArrayList<>();
        WalletEntryPojo walletEntryPojo = new WalletEntryPojo();
        WalletCategoriesFieldPojo walletCategoriesFieldPojo = new WalletCategoriesFieldPojo();
        Boolean.valueOf(false);
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new InputStreamReader(new ByteArrayInputStream(Flaes.getdecodedstring(IOUtils.toString(new FileInputStream(str), StandardCharsets.UTF_8)).getBytes())));
            String str2 = "";

            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2) {
                    str2 = newPullParser.getName();
                    if (str2.equals("EntryInfo")) {
                        Boolean.valueOf(true);
                        walletEntryPojo = new WalletEntryPojo();
                    }
                    if (str2.equalsIgnoreCase("Field")) {
                        walletCategoriesFieldPojo = new WalletCategoriesFieldPojo();
                    }
                } else if (eventType == 3) {
                    if (newPullParser.getName().equals("CategoryInfo")) {
                        Boolean.valueOf(false);
                    }
                    str2 = "";
                } else if (eventType == 4) {
                    try {
                        String text = newPullParser.getText();
                        switch (str2) {
                            case "IsSecured":
                                walletCategoriesFieldPojo.setSecured(Boolean.parseBoolean(text));
                                arrayList.add(walletCategoriesFieldPojo);
                                break;
                            case "Name":
                                walletCategoriesFieldPojo.setFieldName(text);
                                break;
                            case "Value":
                                if (text.equals("none")) {
                                    walletCategoriesFieldPojo.setFieldValue("");
                                } else {
                                    walletCategoriesFieldPojo.setFieldValue(text);
                                }
                                break;
                            case "CategoryName":
                                walletEntryPojo.setCategoryName(newPullParser.getText());
                                break;
                            case "EntryName":
                                walletEntryPojo.setEntryName(newPullParser.getText());
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Entryreadxmleee11", "" + e);
                    }
                }
            }
            walletEntryPojo.setFields(arrayList);
            return walletEntryPojo;
        } catch (Exception e2) {
            Log.e("Entryreadxml", "" + e2);
            return null;
        }
    }
}
