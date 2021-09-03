package com.appodeal.test.suits;

import android.app.Activity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.test.helpers.Utils;

import static com.appodeal.test.activities.parent.SuperActivity.APP_KEY;

public class BannerAd extends AppODeal {

    private BannerPosition bannerPosition = BannerPosition.BANNER;

    public BannerAd(Activity activity, int bannerViewId) {
        super(activity, true);
        setBannerViewId(bannerViewId);
    }

    public void initSdk() {
        Appodeal.initialize(activity, APP_KEY, Appodeal.BANNER, consent);
        Appodeal.setBannerCallbacks(new AppodealBannerCallbacks(activity, false));
    }

    public boolean show() {
        return Appodeal.show(activity, bannerPosition.getValue());
    }

    public void setAutoCache(boolean isChecked) {
        Appodeal.setAutoCache(Appodeal.BANNER, isChecked);
    }

    public void setTriggerOnLoadedOnPrecache(boolean isChecked) {
        Appodeal.setTriggerOnLoadedOnPrecache(bannerPosition.getValue(), isChecked);
    }

    public void disableAdNetworks() {
        disableNetworks(Appodeal.BANNER);
    }

    public void cacheAd() {
        Appodeal.cache(activity, Appodeal.BANNER);
    }

    public boolean isPreCached() {
        return Appodeal.isPrecache(bannerPosition.getValue());
    }

    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.BANNER);
    }

    public void setBannerPosition(BannerPosition bannerPosition) {
        this.bannerPosition = bannerPosition;
    }

    public void hide() {
        Appodeal.hide(activity, Appodeal.BANNER);
    }

    public void destroy() {
        Appodeal.destroy(Appodeal.BANNER);
    }

    public void setBannerViewId(int viewId) {
        Appodeal.setBannerViewId(viewId);
    }

    public void setSmartBanner(boolean isChecked) {
        Appodeal.setSmartBanners(isChecked);
    }

    public void set728x90Banners(boolean isChecked) {
        Appodeal.set728x90Banners(isChecked);
    }

    public void setBannerAnimation(boolean isChecked) {
        Appodeal.setBannerAnimation(isChecked);
    }

    public static enum BannerPosition {
        BANNER(Appodeal.BANNER),
        BOTTOM(Appodeal.BANNER_BOTTOM),
        TOP(Appodeal.BANNER_TOP),
        VIEW(Appodeal.BANNER_VIEW),
        LEFT(Appodeal.BANNER_LEFT),
        RIGHT(Appodeal.BANNER_RIGHT);

        private final int value;

        BannerPosition(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static class AppodealBannerCallbacks implements BannerCallbacks {

        private final Activity activity;
        private final boolean showToast;

        public AppodealBannerCallbacks(Activity activity, boolean showToast) {
            this.activity = activity;
            this.showToast = showToast;
        }

        @Override
        public void onBannerLoaded(int height, boolean isPrecache) {
            if (showToast)
                Utils.showToast(activity, String.format("onBannerLoaded, %sdp, isPrecache: %s", height, isPrecache));
        }

        @Override
        public void onBannerFailedToLoad() {
            if (showToast)
                Utils.showToast(activity, "onBannerFailedToLoad");
        }

        @Override
        public void onBannerShown() {
            if (showToast)
                Utils.showToast(activity, "onBannerShown");
        }

        @Override
        public void onBannerShowFailed() {
            if (showToast)
                Utils.showToast(activity, "onBannerShowFailed");
        }

        @Override
        public void onBannerClicked() {
            if (showToast)
                Utils.showToast(activity, "onBannerClicked");
        }

        @Override
        public void onBannerExpired() {
            if (showToast)
                Utils.showToast(activity, "onBannerExpired");
        }

    }

}
