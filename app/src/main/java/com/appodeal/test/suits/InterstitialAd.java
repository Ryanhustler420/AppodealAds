package com.appodeal.test.suits;

import android.app.Activity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.test.helpers.Utils;

import static com.appodeal.test.activities.parent.SuperActivity.APP_KEY;

public class InterstitialAd extends AppODeal {

    public InterstitialAd(Activity activity) {
        super(activity, true);
    }

    public void initSdk() {
        Appodeal.initialize(activity, APP_KEY, Appodeal.INTERSTITIAL, consent);
        Appodeal.setInterstitialCallbacks(new AppodealInterstitialCallbacks(activity, false));
    }

    public boolean show() {
        return Appodeal.show(activity, Appodeal.INTERSTITIAL);
    }

    public void setAutoCache(boolean isChecked) {
        Appodeal.setAutoCache(Appodeal.INTERSTITIAL, isChecked);
    }

    public void setTriggerOnLoadedOnPrecache(boolean isChecked) {
        Appodeal.setTriggerOnLoadedOnPrecache(Appodeal.INTERSTITIAL, isChecked);
    }

    public void disableAdNetworks() {
        disableNetworks(Appodeal.INTERSTITIAL);
    }

    public void cacheAd() {
        Appodeal.cache(activity, Appodeal.INTERSTITIAL);
    }

    public boolean isPreCached() {
        return Appodeal.isPrecache(Appodeal.INTERSTITIAL);
    }

    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.INTERSTITIAL);
    }

    private static class AppodealInterstitialCallbacks implements InterstitialCallbacks {

        private final Activity activity;
        private final boolean showToast;

        public AppodealInterstitialCallbacks(Activity activity, boolean showToast) {
            this.activity = activity;
            this.showToast = showToast;
        }

        @Override
        public void onInterstitialLoaded(boolean isPrecache) {
            if (showToast)
                Utils.showToast(activity, String.format("onInterstitialLoaded, isPrecache: %s", isPrecache));
        }

        @Override
        public void onInterstitialFailedToLoad() {
            if (showToast)
                Utils.showToast(activity, "onInterstitialFailedToLoad");
        }

        @Override
        public void onInterstitialShown() {
            if (showToast)
                Utils.showToast(activity, "onInterstitialShown");
        }

        @Override
        public void onInterstitialShowFailed() {
            if (showToast)
                Utils.showToast(activity, "onInterstitialShowFailed");
        }

        @Override
        public void onInterstitialClicked() {
            if (showToast)
                Utils.showToast(activity, "onInterstitialClicked");
        }

        @Override
        public void onInterstitialClosed() {
            if (showToast)
                Utils.showToast(activity, "onInterstitialClosed");
        }

        @Override
        public void onInterstitialExpired() {
            if (showToast)
                Utils.showToast(activity, "onInterstitialExpired");
        }

    }

}
