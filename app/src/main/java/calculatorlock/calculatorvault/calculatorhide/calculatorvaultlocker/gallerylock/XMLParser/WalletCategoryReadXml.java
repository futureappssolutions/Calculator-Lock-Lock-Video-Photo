package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser;

import android.util.Log;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesFieldPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.WalletCategoriesPojo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class WalletCategoryReadXml {
    public static WalletCategoriesPojo ReadCategoryFromXml(String str) {
        Log.e("wallpaper", "" + str);
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(str.getBytes()));
            ArrayList arrayList = new ArrayList();
            newPullParser.setInput(inputStreamReader);
            Boolean bool = false;
            WalletCategoriesPojo walletCategoriesPojo = null;
            WalletCategoriesFieldPojo walletCategoriesFieldPojo = null;
            String str2 = "";
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType != 2) {
                    if (eventType != 3) {
                        if (eventType == 4) {
                            try {
                                if (bool.booleanValue() && walletCategoriesPojo != null) {
                                    if (str2.equals("IconIndex")) {
                                        walletCategoriesPojo.setCategoryIconIndex(Integer.parseInt(newPullParser.getText()));
                                    } else if (str2.equals("CategoryName")) {
                                        Log.e("CategoryName", "" + newPullParser.getText());
                                        walletCategoriesPojo.setCategoryName(newPullParser.getText());
                                    } else if (str2.equals("Field")) {
                                        Log.e("Field", "" + newPullParser.getText());
                                        walletCategoriesFieldPojo.setFieldName(newPullParser.getText());
                                    } else if (str2.equals("IsSecured")) {
                                        if (walletCategoriesFieldPojo != null) {
                                            walletCategoriesFieldPojo.setSecured(Boolean.parseBoolean(newPullParser.getText()));
                                            arrayList.add(walletCategoriesFieldPojo);
                                            walletCategoriesFieldPojo = null;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (newPullParser.getName() == "CategoryInfo") {
                        bool = false;
                    }
                    str2 = "";
                } else {
                    str2 = newPullParser.getName();
                    if (str2.equals("CategoryInfo")) {
                        walletCategoriesPojo = new WalletCategoriesPojo();
                        bool = true;
                    } else if (str2.equals("Field")) {
                        walletCategoriesFieldPojo = new WalletCategoriesFieldPojo();
                    }
                }
            }
            walletCategoriesPojo.setCategoryFields(arrayList);
            return walletCategoriesPojo;
        } catch (Exception unused) {
            return null;
        }
    }
}
