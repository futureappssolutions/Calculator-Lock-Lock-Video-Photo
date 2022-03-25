package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common;

import java.util.regex.Pattern;


public class RecoveryOfCredentialsMethods {
    public boolean isEmailValid(String str) {
        return Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", 2).matcher(str).matches();
    }
}
