package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;


public class CustomApplicationClass extends Application {
    public static CustomApplicationClass application;
    //AppOpenManager appOpenManager;

    public CustomApplicationClass() {
        application = this;
    }

    public static CustomApplicationClass getApp() {
        if (application == null) {
            application = new CustomApplicationClass();
        }
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        AudienceNetworkAds.initialize(this);

//        MobileAds.initialize(this, initializationStatus -> {
//        });

        //appOpenManager = new AppOpenManager(this);
    }
}
