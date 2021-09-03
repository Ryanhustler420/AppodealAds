package com.appodeal.test.suits;

import android.app.Activity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.test.helpers.Utils;

import java.util.Locale;

import static com.appodeal.test.activities.parent.SuperActivity.APP_KEY;

public class RewardVideoAd extends AppODeal {

    public RewardVideoAd(Activity activity) {
        super(activity, true);
    }

    public void initSdk() {
        Appodeal.initialize(activity, APP_KEY, Appodeal.REWARDED_VIDEO, consent);
        Appodeal.setRewardedVideoCallbacks(new AppodealRewardedVideoCallbacks(activity, false));
    }

    public boolean show() {
        return Appodeal.show(activity, Appodeal.REWARDED_VIDEO);
    }

    public void setAutoCache(boolean isChecked) {
        Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, isChecked);
    }

    public void setTriggerOnLoadedOnPrecache(boolean isChecked) {
        Appodeal.setTriggerOnLoadedOnPrecache(Appodeal.REWARDED_VIDEO, isChecked);
    }

    public void disableAdNetworks() {
        disableNetworks(Appodeal.REWARDED_VIDEO);
    }

    public void cacheAd() {
        Appodeal.cache(activity, Appodeal.REWARDED_VIDEO);
    }

    public boolean isPreCached() {
        return Appodeal.isPrecache(Appodeal.REWARDED_VIDEO);
    }

    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.REWARDED_VIDEO);
    }

    private static class AppodealRewardedVideoCallbacks implements RewardedVideoCallbacks {

        private final Activity activity;
        private final boolean showToast;

        public AppodealRewardedVideoCallbacks(Activity activity, boolean showToast) {
            this.activity = activity;
            this.showToast = showToast;
        }

        @Override
        public void onRewardedVideoLoaded(boolean isPrecache) {
            if (showToast)
                Utils.showToast(activity, "onRewardedVideoLoaded, isPrecache: " + isPrecache);
        }

        @Override
        public void onRewardedVideoFailedToLoad() {
            if (showToast)
                Utils.showToast(activity, "onRewardedVideoFailedToLoad");
        }

        @Override
        public void onRewardedVideoShown() {
            if (showToast)
                Utils.showToast(activity, "onRewardedVideoShown");
        }

        @Override
        public void onRewardedVideoShowFailed() {
            if (showToast)
                Utils.showToast(activity, "onRewardedVideoShowFailed");
        }

        @Override
        public void onRewardedVideoClicked() {
            if (showToast)
                Utils.showToast(activity, "onRewardedVideoClicked");
        }

        @Override
        public void onRewardedVideoFinished(double amount, String name) {
            if (showToast)
                Utils.showToast(activity, String.format(Locale.ENGLISH, "onRewardedVideoFinished. Reward: %.2f %s", amount, name));
        }

        @Override
        public void onRewardedVideoClosed(boolean finished) {
            if (showToast)
                Utils.showToast(activity, String.format("onRewardedVideoClosed,  finished: %s", finished));
        }

        @Override
        public void onRewardedVideoExpired() {
            if (showToast)
                Utils.showToast(activity, "onRewardedVideoExpired");
        }

    }

}
