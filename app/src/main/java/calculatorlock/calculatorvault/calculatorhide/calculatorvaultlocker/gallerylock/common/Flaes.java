package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common;

import android.util.Base64;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class Flaes {
    private static final String type = "AES";

    public static String getencodedstring(String str) throws Exception {
        Key kiVar = getki();
        Cipher instance = Cipher.getInstance(type);
        instance.init(1, kiVar);
        return Base64.encodeToString(instance.doFinal(str.getBytes()), 0);
    }

    public static String getdecodedstring(String str) throws Exception {
        Key kiVar = getki();
        Cipher instance = Cipher.getInstance(type);
        instance.init(2, kiVar);
        return new String(instance.doFinal(Base64.decode(str, 0)));
    }

    private static Key getki() throws Exception {
        return new SecretKeySpec(getkv(), type);
    }

    public static byte[] getkv() {
        byte[] bArr = new byte[16];
        int i = 120;
        int i2 = 50;
        for (int i3 = 0; i3 < 16; i3++) {
            bArr[i3] = (byte) i;
            if (i2 % 2 == 0) {
                i2--;
                i -= i2;
            } else {
                i2++;
                i += i2;
            }
        }
        return bArr;
    }

    public static void decryptUsingCipherStream_AES128(File file, File file2) {
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            Key kiVar = getki();
            Cipher instance = Cipher.getInstance(type);
            instance.init(2, kiVar);
            IOUtils.copy(fileInputStream, new CipherOutputStream(fileOutputStream, instance));
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptUsingCipherStream_AES128(File file, File file2) {
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            Key kiVar = getki();
            Cipher instance = Cipher.getInstance(type);
            instance.init(1, kiVar);
            IOUtils.copy(fileInputStream, new CipherOutputStream(fileOutputStream, instance));
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
