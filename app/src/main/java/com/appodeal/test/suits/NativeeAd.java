package com.appodeal.test.suits;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NativeIconView;
import com.appodeal.ads.NativeMediaView;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import com.appodeal.ads.native_ad.views.NativeAdViewContentStream;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;
import com.appodeal.test.R;
import com.appodeal.test.helpers.Utils;

import java.util.LinkedList;
import java.util.List;

import static com.appodeal.test.activities.parent.SuperActivity.APP_KEY;

public class NativeeAd extends AppODeal {

    public NativeeAd(Activity activity) {
        super(activity, true);
    }

    public void initSdk() {
        Appodeal.initialize(activity, APP_KEY, Appodeal.NATIVE, consent);
        Appodeal.setNativeCallbacks(new AppodealNativeCallbacks(activity, false));
    }

    public void setAutoCache(boolean isChecked) {
        Appodeal.setAutoCache(Appodeal.NATIVE, isChecked);
    }

    public void setTriggerOnLoadedOnPrecache(boolean isChecked) {
        Appodeal.setTriggerOnLoadedOnPrecache(Appodeal.NATIVE, isChecked);
    }

    public void disableAdNetworks() {
        disableNetworks(Appodeal.NATIVE);
    }

    public void unregisterViewForInteractionNativeAdView(NativeAdView nativeAdView) {
        if (nativeAdView == null) return;
        nativeAdView.unregisterViewForInteraction();
    }

    public void destroyNativeAdView(NativeAdView nativeAdView) {
        if (nativeAdView == null) return;
        nativeAdView.destroy();
    }

    public void registerNativeAd(NativeAdView nativeAdView, com.appodeal.ads.NativeAd nativeAd) {
        if (nativeAdView == null || nativeAd == null) return;
        nativeAdView.registerView(nativeAd);
    }

    public boolean isAdLoaded() {
        return Appodeal.isLoaded(Appodeal.NATIVE);
    }

    public void cacheAd(int numberOfAds) {
        if (numberOfAds == 1) Appodeal.cache(activity, Appodeal.NATIVE);
        else Appodeal.cache(activity, Appodeal.NATIVE, numberOfAds);
    }

    public List<com.appodeal.ads.NativeAd> getNativeAds(int numberOfAds) {
        return Appodeal.getNativeAds(numberOfAds);
    }

    public void setNativeAdType(NativeAdType type) {
        Appodeal.setNativeAdType(type.getType());
    }

    public void setRequiredNativeMediaAssetType(MediaAssetType type) {
        Appodeal.setRequiredNativeMediaAssetType(type.getType());
    }

    public enum NativeAdType {

        AUTO(Native.NativeAdType.Auto),
        VIDEO(Native.NativeAdType.Video),
        NO_VIDEO(Native.NativeAdType.NoVideo);

        private final Native.NativeAdType type;

        NativeAdType(Native.NativeAdType type) {
            this.type = type;
        }

        public Native.NativeAdType getType() {
            return type;
        }

    }

    public enum MediaAssetType {

        ALL(Native.MediaAssetType.ALL),
        ICON(Native.MediaAssetType.ICON),
        IMAGE(Native.MediaAssetType.IMAGE);

        private final Native.MediaAssetType type;

        MediaAssetType(Native.MediaAssetType type) {
            this.type = type;
        }

        public Native.MediaAssetType getType() {
            return type;
        }

    }

    private static class AppodealNativeCallbacks implements NativeCallbacks {

        private final Activity activity;
        private final boolean showToast;

        public AppodealNativeCallbacks(Activity activity, boolean showToast) {
            this.activity = activity;
            this.showToast = showToast;
        }

        @Override
        public void onNativeLoaded() {
            if (showToast)
                Utils.showToast(activity, "onNativeLoaded");
        }

        @Override
        public void onNativeFailedToLoad() {
            if (showToast)
                Utils.showToast(activity, "onNativeFailedToLoad");
        }

        @Override
        public void onNativeShown(com.appodeal.ads.NativeAd nativeAd) {
            if (showToast)
                Utils.showToast(activity, "onNativeShown");
        }

        @Override
        public void onNativeShowFailed(com.appodeal.ads.NativeAd nativeAd) {
            if (showToast)
                Utils.showToast(activity, "onNativeShowFailed");
        }

        @Override
        public void onNativeClicked(com.appodeal.ads.NativeAd nativeAd) {
            if (showToast)
                Utils.showToast(activity, "onNativeClicked");
        }

        @Override
        public void onNativeExpired() {
            if (showToast)
                Utils.showToast(activity, "onNativeExpired");
        }

    }

    //  Adapter

    public static class NativeListAdapter {

        String placementName = "default";
        private final List<com.appodeal.ads.NativeAd> nativeAdList = new LinkedList<>();
        private final LinearLayout nativeListView;
        private int type;

        public NativeListAdapter(LinearLayout nativeListView, int type) {
            this.nativeListView = nativeListView;
            this.type = type;
        }

        public void addNativeAd(com.appodeal.ads.NativeAd nativeAd) {
            nativeAdList.add(nativeAd);
        }

        public void setTemplate(int type) {
            this.type = type;
        }

        public void rebuild() {
            nativeListView.removeAllViews();
            for (com.appodeal.ads.NativeAd nativeAd : nativeAdList) {
                nativeListView.addView(getView(nativeAd));
            }
        }

        public void clear() {
            for (com.appodeal.ads.NativeAd nativeAd : nativeAdList) {
                nativeAd.destroy();
            }
            nativeAdList.clear();
        }

        private View getView(com.appodeal.ads.NativeAd nativeAd) {
            NativeAdView nativeAdView = null;
            switch (type) {
                case 0:
                    nativeAdView = fillCustomNativeAdView(nativeAd);
                    break;
                case 1:
                    nativeAdView = new NativeAdViewNewsFeed(nativeListView.getContext(), nativeAd, placementName);
                    break;
                case 2:
                    nativeAdView = new NativeAdViewAppWall(nativeListView.getContext(), nativeAd, placementName);
                    break;
                case 3:
                    nativeAdView = new NativeAdViewContentStream(nativeListView.getContext(), nativeAd, placementName);
                    break;
                case 4:
                    nativeAdView = fillCustomWithoutIconNativeAdView(nativeAd);
                    break;
            }
            return nativeAdView;
        }

        private NativeAdView fillCustomNativeAdView(com.appodeal.ads.NativeAd nativeAd) {
            NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(nativeListView.getContext()).inflate(R.layout.include_native_ads,
                    nativeListView, false);
            TextView tvTitle = nativeAdView.findViewById(R.id.tv_title);
            tvTitle.setText(nativeAd.getTitle());
            nativeAdView.setTitleView(tvTitle);
            TextView tvDescription = nativeAdView.findViewById(R.id.tv_description);
            tvDescription.setText(nativeAd.getDescription());
            nativeAdView.setDescriptionView(tvDescription);
            RatingBar ratingBar = nativeAdView.findViewById(R.id.rb_rating);
            if (nativeAd.getRating() == 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(nativeAd.getRating());
                ratingBar.setStepSize(0.1f);
            }
            nativeAdView.setRatingView(ratingBar);
            Button ctaButton = nativeAdView.findViewById(R.id.b_cta);
            ctaButton.setText(nativeAd.getCallToAction());
            nativeAdView.setCallToActionView(ctaButton);
            NativeIconView nativeIconView = nativeAdView.findViewById(R.id.icon);
            nativeAdView.setNativeIconView(nativeIconView);
            View providerView = nativeAd.getProviderView(nativeListView.getContext());
            if (providerView != null) {
                if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) providerView.getParent()).removeView(providerView);
                }
                FrameLayout providerViewContainer = nativeAdView.findViewById(R.id.provider_view);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                providerViewContainer.addView(providerView, layoutParams);
            }
            nativeAdView.setProviderView(providerView);
            TextView tvAgeRestrictions = nativeAdView.findViewById(R.id.tv_age_restriction);
            if (nativeAd.getAgeRestrictions() != null) {
                tvAgeRestrictions.setText(nativeAd.getAgeRestrictions());
                tvAgeRestrictions.setVisibility(View.VISIBLE);
            } else {
                tvAgeRestrictions.setVisibility(View.GONE);
            }
            NativeMediaView nativeMediaView = nativeAdView.findViewById(R.id.appodeal_media_view_content);
            nativeAdView.setNativeMediaView(nativeMediaView);
            nativeAdView.registerView(nativeAd, placementName);
            nativeAdView.setVisibility(View.VISIBLE);
            return nativeAdView;
        }

        private NativeAdView fillCustomWithoutIconNativeAdView(com.appodeal.ads.NativeAd nativeAd) {
            NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(nativeListView.getContext()).inflate(R.layout.native_ads_without_icon,
                    nativeListView, false);
            TextView tvTitle = nativeAdView.findViewById(R.id.tv_title);
            tvTitle.setText(nativeAd.getTitle());
            nativeAdView.setTitleView(tvTitle);
            TextView tvDescription = nativeAdView.findViewById(R.id.tv_description);
            tvDescription.setText(nativeAd.getDescription());
            nativeAdView.setDescriptionView(tvDescription);
            RatingBar ratingBar = nativeAdView.findViewById(R.id.rb_rating);
            if (nativeAd.getRating() == 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(nativeAd.getRating());
                ratingBar.setStepSize(0.1f);
            }
            nativeAdView.setRatingView(ratingBar);
            Button ctaButton = nativeAdView.findViewById(R.id.b_cta);
            ctaButton.setText(nativeAd.getCallToAction());
            nativeAdView.setCallToActionView(ctaButton);
            View providerView = nativeAd.getProviderView(nativeListView.getContext());
            if (providerView != null) {
                if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) providerView.getParent()).removeView(providerView);
                }
                FrameLayout providerViewContainer = nativeAdView.findViewById(R.id.provider_view);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                providerViewContainer.addView(providerView, layoutParams);
            }
            nativeAdView.setProviderView(providerView);
            TextView tvAgeRestrictions = nativeAdView.findViewById(R.id.tv_age_restriction);
            if (nativeAd.getAgeRestrictions() != null) {
                tvAgeRestrictions.setText(nativeAd.getAgeRestrictions());
                tvAgeRestrictions.setVisibility(View.VISIBLE);
            } else {
                tvAgeRestrictions.setVisibility(View.GONE);
            }
            NativeMediaView nativeMediaView = nativeAdView.findViewById(R.id.appodeal_media_view_content);
            nativeAdView.setNativeMediaView(nativeMediaView);
            nativeAdView.registerView(nativeAd, placementName);
            nativeAdView.setVisibility(View.VISIBLE);
            return nativeAdView;
        }
    }

}
