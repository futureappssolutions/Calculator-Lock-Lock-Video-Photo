package com.calculator.vaultlocker.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.calculator.vaultlocker.Activity.ActivityMain;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private final Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, "android.permission.USE_FINGERPRINT") == 0) {
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    @Override
    public void onAuthenticationError(int i, CharSequence charSequence) {
        Toast.makeText(context, "Authentication error\n" + charSequence, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int i, CharSequence charSequence) {
        Toast.makeText(context, "Authentication help\n" + charSequence, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
        Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
        SecurityLocksCommon.IsnewloginforAd = true;
        SecurityLocksCommon.IsFakeAccount = 0;
        Intent intent = new Intent(context, ActivityMain.class);
        ((Activity) context).overridePendingTransition(17432576, 17432577);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
