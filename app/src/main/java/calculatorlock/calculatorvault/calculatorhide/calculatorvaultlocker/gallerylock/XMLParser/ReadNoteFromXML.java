package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Flaes;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ReadNoteFromXML {
    public HashMap<String, String> hashMap = null;
    public Boolean inDataTag = false;
    public String CurrentTag = "";
    public String key = "";
    public String value = "";

    public HashMap<String, String> ReadNote(String str) {
        try {
            File file = new File(str);
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new InputStreamReader(new ByteArrayInputStream(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8).getBytes())));
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2) {
                    String name = newPullParser.getName();
                    this.CurrentTag = name;
                    if (name.equals("dict")) {
                        this.inDataTag = true;
                        this.hashMap = new HashMap<>();
                    }
                } else if (eventType == 3) {
                    if (this.CurrentTag.equals("string") || (this.CurrentTag.equals("date") && !this.value.equals("") && !this.key.equals(""))) {
                        this.hashMap.put(this.key, this.value);
                        this.value = "";
                        this.key = "";
                    }
                    if (newPullParser.getName().equals("dict")) {
                        this.inDataTag = false;
                    }
                    this.CurrentTag = "";
                } else if (eventType != 4) {
                } else if (this.inDataTag && this.hashMap != null) {
                    String str2 = this.CurrentTag;
                    switch (str2) {
                        case "string":
                            if (!this.key.equals("Text")) {
                                this.value = newPullParser.getText();
                            } else {
                                try {
                                    this.value = Flaes.getdecodedstring(newPullParser.getText());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "key":
                            this.key = newPullParser.getText();
                            break;
                        case "date":
                            this.value = newPullParser.getText();
                            break;
                    }
                }
            }
        } catch (IOException | XmlPullParserException e2) {
            e2.printStackTrace();
        }
        return this.hashMap;
    }
}
