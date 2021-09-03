package com.appodeal.test.suits;

import android.app.Activity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.MrecCallbacks;
import com.appodeal.test.helpers.Utils;

import static com.appodeal.test.activities.parent.SuperActivity.APP_KEY;

public class MrecAd extends AppODeal {

    public MrecAd(Activity activity, int mrecViewId) {
        super(activity, true);
        setMrecViewId(mrecViewId);
    }

    public void initSdk() {
        Appodeal.initialize(activity, APP_KEY, Appodeal.MREC, consent);
        Appodeal.setMrecCallbacks(new AppodealMrecCallbacks(activity, false));
    }

    public boolean show() {
        return Appodeal.show(activity, Appodeal.MREC);
    }

    public void setAutoCache(boolean isChecked) {
        Appodeal.setAutoCache(Appodeal.MREC, isChecked);
    }

    public void setTriggerOnLoadedOnPrecache(boolean isChecked) {
        Appodeal.setTriggerOnLoadedOnPrecache(Appodeal.MREC, isChecked);
    }

    public void disableAdNetworks() {
        disableNetworks(Appodeal.MREC);
    }

    public void cacheAd() {
        Appodeal.cache(activity, Appodeal.MREC);
    }

    public boolean isPreCached() {
        return Appodeal.isPrecache(Appodeal.MREC);
    }

    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.MREC);
    }

    public void hide() {
        Appodeal.hide(activity, Appodeal.MREC);
    }

    public void destroy() {
        Appodeal.destroy(Appodeal.MREC);
    }

    public void setMrecViewId(int viewId) {
        Appodeal.setMrecViewId(viewId);
    }

    private static class AppodealMrecCallbacks implements MrecCallbacks {

        private final Activity activity;
        private final boolean showToast;

        public AppodealMrecCallbacks(Activity activity, boolean showToast) {
            this.activity = activity;
            this.showToast = showToast;
        }

        @Override
        public void onMrecLoaded(boolean isPrecache) {
            if (showToast)
                Utils.showToast(activity, String.format("onMrecLoaded, isPrecache: %s", isPrecache));
        }

        @Override
        public void onMrecFailedToLoad() {
            if (showToast)
                Utils.showToast(activity, "onMrecFailedToLoad");
        }

        @Override
        public void onMrecShown() {
            if (showToast)
                Utils.showToast(activity, "onMrecShown");
        }

        @Override
        public void onMrecShowFailed() {
            if (showToast)
                Utils.showToast(activity, "onMrecShowFailed");
        }

        @Override
        public void onMrecClicked() {
            if (showToast)
                Utils.showToast(activity, "onMrecClicked");
        }

        @Override
        public void onMrecExpired() {
            if (showToast)
                Utils.showToast(activity, "onMrecExpired");
        }

    }

}
