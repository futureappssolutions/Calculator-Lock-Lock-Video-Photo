package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Preferences;

public class Advertisement {
    public static boolean isShow = true;

    public static MyCallback myCallback;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static Advertisement mInstance;
    private static InterstitialAd mInterstitialAd;
    private static AdView fbadView;
    private static NativeAd fbnativeAd;

    public Advertisement(Activity activity) {
        Advertisement.activity = activity;
    }


    public static Advertisement getInstance(Activity activity) {
        Advertisement.activity = activity;
        if (mInstance == null) {
            mInstance = new Advertisement(activity);
        }
        return mInstance;
    }


    public static void preLoadAds(Activity activity) {
        preLoadFull(activity);
        preLoadBanner(activity);
        preLoadNative(activity);
    }


    private static void preLoadBanner(Activity activity) {
        fbadView = new com.facebook.ads.AdView(activity, "1027321451519329_1027322114852596", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        fbadView.loadAd(fbadView.buildLoadAdConfig().withAdListener(adListener).build());
    }


    private static void preLoadFull(Activity activity) {
        mInterstitialAd = new com.facebook.ads.InterstitialAd(activity, "1027321451519329_1027322178185923");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                isShow = false;
                preLoadFull(activity);
                interstitialCallBack();
                handlerSec();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                interstitialCallBack();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
                mInterstitialAd = (com.facebook.ads.InterstitialAd) ad;
                isShow = true;
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        mInterstitialAd.loadAd(mInterstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
    }


    private static void preLoadNative(Activity activity) {
        fbnativeAd = new com.facebook.ads.NativeAd(activity, "1027321451519329_1027322258185915");
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
            }

            @Override
            public void onAdLoaded(Ad ad) {
                fbnativeAd = (com.facebook.ads.NativeAd) ad;
                // Native ad is loaded and ready to be displayed
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
            }
        };
        // Request an ad
        fbnativeAd.loadAd(fbnativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }

    public static void interstitialCallBack() {
        if (myCallback != null) {
            myCallback.callbackCall();
            myCallback = null;
        }
    }


    public interface MyCallback {
        void callbackCall();
    }


    public static void showFull(MyCallback myCallback) {
        Advertisement.myCallback = myCallback;
        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
            if (mInterstitialAd == null) {
                interstitialCallBack();
            } else {
                if (isShow) {
                    mInterstitialAd.show();
                    isShow = false;
                    preLoadFull(activity);
                } else {
                    interstitialCallBack();
                }
            }
        }
    }


    public static void showBanner(Activity activity, LinearLayout ll_banner) {
        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
            if (fbadView != null) {
                if (fbadView.getParent() != null) {
                    ((ViewGroup) fbadView.getParent()).removeView(fbadView);
                }
                ll_banner.addView(fbadView);
                fbadView = null;
                preLoadBanner(activity);
            }
        }
    }

    public static void shoNativeAds(Activity activity,com.facebook.ads.NativeAdLayout native_fb_Layout) {
        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
            if (fbnativeAd != null) {
                NativeAdLayout adView = (NativeAdLayout) LayoutInflater.from(activity).inflate(R.layout.fb_native, null);
                native_fb_Layout.addView(adView);
                inflateAd(activity,fbnativeAd, adView);
                fbnativeAd = null;
                preLoadNative(activity);
            }
        }
    }


    public static void handlerSec() {
        new CountDownTimer(6 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isShow = true;
            }
        }.start();
    }


    private static void inflateAd(Activity activity,NativeAd nativeAd, NativeAdLayout nativeAdLayout) {
        nativeAd.unregisterView();
        // Add the AdOptionsView
        LinearLayout adChoicesContainer = nativeAdLayout.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = nativeAdLayout.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = nativeAdLayout.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = nativeAdLayout.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = nativeAdLayout.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = nativeAdLayout.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = nativeAdLayout.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = nativeAdLayout.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                nativeAdLayout, nativeAdMedia, nativeAdIcon, clickableViews);
    }
//    private static final String TAG = "AdsTags";
//    public static InterstitialAd mInterstitialAd;
//    public static NativeAd mNativeAd = null;
//    public static AdView mAdView = null;
//    public static MyCallback myCallback;
//
//
//    public static android.os.CountDownTimer allcount60;
//    public static boolean adsdisplay = false;
//
//    public static void preLoadAds(Activity activity) {
//        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
//            preLoadFull(activity);
//            preLoadBanner(activity);
//            preLoadNative(activity);
//        }
//    }
//
//    public static void preLoadFull(Activity activity) {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(activity, "ca-app-pub-6447655601926357/9428432720", adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        mInterstitialAd = interstitialAd;
//                        Log.d(TAG, "Google Full Load: onAdLoaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
//                        Log.d(TAG, "Google Full Fail : " + loadAdError.getMessage());
//                        mInterstitialAd = null;
//                    }
//                });
//    }
//
//
//    public static void preLoadBanner(Activity activity) {
//        AdView adView = new AdView(activity);
//        adView.setAdUnitId("ca-app-pub-6447655601926357/1741514397");
//        AdSize adSize = getAdSize(activity);
//        adView.setAdSize(adSize);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                mAdView = adView;
//                Log.d(TAG, "Google Banner Load: onAdLoaded");
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                mAdView = null;
//                Log.d(TAG, "Google Banner Fail : " + loadAdError.getCode() + " : " + loadAdError.getMessage());
//
//            }
//
//            @Override
//            public void onAdOpened() {
//            }
//
//            @Override
//            public void onAdClosed() {
//            }
//        });
//    }
//
//
//    public static void preLoadNative(Activity activity) {
//        AdLoader.Builder builder = new AdLoader.Builder(activity, "ca-app-pub-6447655601926357/9556441145").forNativeAd(nativeAd -> {
//            mNativeAd = nativeAd;
//            Log.d(TAG, "Google Native Load: onAdLoaded");
//        });
//
//        AdLoader adLoader = builder.withAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                mNativeAd = null;
//                @SuppressLint("DefaultLocale") String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(),
//                        loadAdError.getMessage());
//                Log.d(TAG, "Google Native Fail : " + error);
//            }
//        }).build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }
//
//
//    public static AdSize getAdSize(Activity context) {
//        Display display = context.getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float widthPixels = outMetrics.widthPixels;
//        float density = outMetrics.density;
//
//        int adWidth = (int) (widthPixels / density);
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
//    }
//
//    public static void interstitialCallBack() {
//        if (myCallback != null) {
//            myCallback.callbackCall();
//            myCallback = null;
//        }
//    }
//
//
//
//
//    public static void showFullAds(Activity activity, MyCallback myCallback) {
//        Advertisement.myCallback = myCallback;
//        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
//            if (mInterstitialAd == null) {
//                interstitialCallBack();
//            } else {
//                mInterstitialAd.show(activity);
//                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        // Called when fullscreen content is dismissed.
//                        Log.e(TAG, "The ad was dismissed.");
//                        preLoadFull(activity);
//                        interstitialCallBack();
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                        // Called when fullscreen content failed to show.
//                        Log.e(TAG, "The ad failed to show.");
//                        interstitialCallBack();
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        // Called when fullscreen content is shown.
//                        // Make sure to set your reference to null so you don't
//                        // show it a second time.
//                        mInterstitialAd = null;
//                        Log.e(TAG, "The ad was shown.");
//                    }
//                });
//            }
//        }
//    }
//
//    public static void showBannerAds(Activity activity, LinearLayout ll_banner) {
//        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
//            if (mAdView != null) {
//                if (mAdView.getParent() != null) {
//                    ((ViewGroup) mAdView.getParent()).removeView(mAdView);
//                }
//                ll_banner.addView(mAdView);
//                mAdView = null;
//                preLoadBanner(activity);
//            }
//        }
//    }
//
//    public static void showNativeAds(Activity activity, FrameLayout frameLayout) {
//        if (!(Preferences.getActive_Weekly().equals("true") || Preferences.getActive_Monthly().equals("true") || Preferences.getActive_Yearly().equals("true"))) {
//            if (mNativeAd != null) {
//                NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_new, null);
//                frameLayout.addView(adView);
//                populateUnifiedNativeAdView(mNativeAd, adView);
//                mNativeAd = null;
//                preLoadNative(activity);
//            }
//        }
//    }
//
//    private static void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
//        MediaView mediaView = adView.findViewById(R.id.ad_media);
//        adView.setMediaView(mediaView);
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
//            @Override
//            public void onChildViewAdded(View parent, View child) {
//                if (child instanceof ImageView) {
//                    ImageView imageView = (ImageView) child;
//                    imageView.setAdjustViewBounds(true);
//                }
//            }
//
//            @Override
//            public void onChildViewRemoved(View parent, View child) {
//
//            }
//        });
//
//        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
//
//        if (nativeAd.getBody() == null) {
//            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
//        } else {
//            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
//        } else {
//            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
//        } else {
//            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getAdvertiser() == null) {
//            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//        adView.setNativeAd(nativeAd);
//    }
//
//    public interface MyCallback {
//        void callbackCall();
//    }
}
