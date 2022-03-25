package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks;

import android.content.Context;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;

import java.util.ArrayList;


public class SecurityLocksActivityMethods {
    String LoginOption = "";

    public ArrayList<SecurityLocksEnt> GetSecurityCredentialsDetail(Context context) {
        this.LoginOption = SecurityLocksSharedPreferences.GetObject(context).GetLoginType();
        ArrayList<SecurityLocksEnt> arrayList = new ArrayList<>();
        SecurityLocksEnt securityLocksEnt = new SecurityLocksEnt();
        securityLocksEnt.SetLoginOption(R.string.lblsetting_SecurityCredentials_Cal_Pin);
        if (SecurityLocksCommon.LoginOptions.Calculator.toString().equals(this.LoginOption)) {
            securityLocksEnt.SetisCheck(true);
            securityLocksEnt.SetDrawable(R.drawable.calculator_icon);
        } else {
            securityLocksEnt.SetisCheck(false);
            securityLocksEnt.SetDrawable(R.drawable.calculator_icon);
        }
        arrayList.add(securityLocksEnt);
        SecurityLocksEnt securityLocksEnt2 = new SecurityLocksEnt();
        securityLocksEnt2.SetLoginOption(R.string.lblsetting_SecurityCredentials_Pin);
        if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(this.LoginOption)) {
            securityLocksEnt2.SetisCheck(true);
            securityLocksEnt2.SetDrawable(R.drawable.pin_icon);
        } else {
            securityLocksEnt2.SetisCheck(false);
            securityLocksEnt2.SetDrawable(R.drawable.pin_icon);
        }
        arrayList.add(securityLocksEnt2);
        SecurityLocksEnt securityLocksEnt3 = new SecurityLocksEnt();
        securityLocksEnt3.SetLoginOption(R.string.lblsetting_SecurityCredentials_Pattern);
        if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(this.LoginOption)) {
            securityLocksEnt3.SetisCheck(true);
            securityLocksEnt3.SetDrawable(R.drawable.pattern_icon);
        } else {
            securityLocksEnt3.SetisCheck(false);
            securityLocksEnt3.SetDrawable(R.drawable.pattern_icon);
        }
        arrayList.add(securityLocksEnt3);
        SecurityLocksEnt securityLocksEnt4 = new SecurityLocksEnt();
        securityLocksEnt4.SetLoginOption(R.string.lblsetting_SecurityCredentials_Password);
        if (SecurityLocksCommon.LoginOptions.Password.toString().equals(this.LoginOption)) {
            securityLocksEnt4.SetisCheck(true);
            securityLocksEnt4.SetDrawable(R.drawable.password_icon);
        } else {
            securityLocksEnt4.SetisCheck(false);
            securityLocksEnt4.SetDrawable(R.drawable.password_icon);
        }
        arrayList.add(securityLocksEnt4);
        return arrayList;
    }
}
