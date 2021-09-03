package com.appodeal.test.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.test.activities.parent.SuperActivity;
import com.appodeal.test.adapters.AdTypeAdapter;
import com.appodeal.test.R;
import com.appodeal.test.helpers.SimplifyOnItemSelectedListener;
import com.appodeal.test.layout.AdTypeViewPager;
import com.appodeal.test.layout.HorizontalNumberPicker;
import com.appodeal.test.layout.SlidingTabLayout;
import com.appodeal.test.suits.MrecAd;
import com.appodeal.test.suits.BannerAd;
import com.appodeal.test.suits.NativeeAd;
import com.appodeal.test.suits.RewardVideoAd;
import com.appodeal.test.suits.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SuperActivity {

    private List<NativeAd> nativeAds = new ArrayList<>();
    private ArrayAdapter<String> logLevelAdapter;

    SlidingTabLayout slidingTabLayout;
    Switch sharedAdsInstanceSwitch;
    CompoundButton testModeSwitch;
    Spinner logLevelSpinner;
    TextView sdkTextView;
    ViewPager pager;

    InterstitialAd interstitialAd;
    RewardVideoAd rewardVideoAd;
    NativeeAd nativeeAd;
    BannerAd bannerAd;
    MrecAd mrecAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initVariables();
        populateData();
        hookListeners();
    }

    private void initViews() {
        consentSwitch = findViewById(R.id.consentSwitch);
        sdkTextView = findViewById(R.id.sdkTextView);
        testModeSwitch = findViewById(R.id.testModeSwitch);
        sharedAdsInstanceSwitch = findViewById(R.id.sharedAdsInstanceSwitch);
        logLevelSpinner = findViewById(R.id.logLevelList);

        pager = (AdTypeViewPager) findViewById(R.id.pager);
        slidingTabLayout = findViewById(R.id.slidingTabLayout);
    }

    private void initVariables() {
        logLevelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.logLevels));
        interstitialAd = new InterstitialAd(this);
        rewardVideoAd = new RewardVideoAd(this);
        bannerAd = new BannerAd(this, R.id.appodealBannerView);
        mrecAd = new MrecAd(this, R.id.appodealMrecView);
        nativeeAd = new NativeeAd(this);
    }

    private void populateData() {
        consentSwitch.setChecked(consent);
        sdkTextView.setText(getString(R.string.sdkTextView, appODeal.getVersion()));
        sharedAdsInstanceSwitch.setChecked(appODeal.isSharedAdsInstanceAcrossActivities());

        appODeal.setNoneAsLogLevel();
    }

    private void hookListeners() {
        consentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                appODeal.showUpdateConsentForm();
                consentSwitch.setChecked(isChecked);
            }
        });

        testModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> appODeal.setTesting(isChecked));
        sharedAdsInstanceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> appODeal.useLoadedAdToOtherAdPlaceholder(isChecked));

        logLevelSpinner.setAdapter(logLevelAdapter);
        logLevelSpinner.setOnItemSelectedListener(new SimplifyOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        appODeal.setNoneAsLogLevel();
                        break;
                    case 1:
                        appODeal.setDebugAsLogLevel();
                        break;
                    case 2:
                        appODeal.setVerboseAsLogLevel();
                        break;
                }
            }
        });

        pager.setOffscreenPageLimit(AdTypeAdapter.AdTypePages.values().length);
        pager.setAdapter(new AdTypeAdapter(getSupportFragmentManager()));
        pager.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child.findViewById(AdTypeAdapter.AdTypePages.Interstitial.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheInterstitialSwitch = findViewById(R.id.autoCacheInterstitialSwitch);
                    autoCacheInterstitialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        interstitialAd.setAutoCache(isChecked);
                        Button interstitialCacheButton = findViewById(R.id.interstitialCacheButton);
                        if (isChecked) {
                            interstitialCacheButton.setVisibility(View.GONE);
                        } else {
                            interstitialCacheButton.setVisibility(View.VISIBLE);
                        }
                    });

                    final CompoundButton onLoadedSwitch = findViewById(R.id.onLoadedInterstitialSwitch);
                    onLoadedSwitch.setText(getString(R.string.onLoadedInterstitialSwitch, "expensive"));
                    onLoadedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            onLoadedSwitch.setText(getString(R.string.onLoadedInterstitialSwitch, "both"));
                        } else {
                            onLoadedSwitch.setText(getString(R.string.onLoadedInterstitialSwitch, "expensive"));
                        }
                        interstitialAd.setTriggerOnLoadedOnPrecache(isChecked);
                    });
                }

                if (child.findViewById(AdTypeAdapter.AdTypePages.RVideo.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheRewardedVideoSwitch = findViewById(R.id.autoCacheRewardedVideoSwitch);
                    autoCacheRewardedVideoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        rewardVideoAd.setAutoCache(isChecked);
                        Button rewardedVideoCacheButton = findViewById(R.id.rewardedVideoCacheButton);
                        if (isChecked) {
                            rewardedVideoCacheButton.setVisibility(View.GONE);
                        } else {
                            rewardedVideoCacheButton.setVisibility(View.VISIBLE);
                        }
                        // rewardVideoAd.setTriggerOnLoadedOnPrecache(isChecked); not allow to do, also very expensive
                    });
                }

                if (child.findViewById(AdTypeAdapter.AdTypePages.Native.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheNativeSwitch = findViewById(R.id.autoCacheNativeSwitch);
                    autoCacheNativeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> nativeeAd.setAutoCache(isChecked));

                    Spinner sMediaAssets = findViewById(R.id.s_media_assets);
                    sMediaAssets.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.native_media_assets)));
                    sMediaAssets.setOnItemSelectedListener(new SimplifyOnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    nativeeAd.setRequiredNativeMediaAssetType(NativeeAd.MediaAssetType.ALL);
                                    break;
                                case 1:
                                    nativeeAd.setRequiredNativeMediaAssetType(NativeeAd.MediaAssetType.ICON);
                                    break;
                                case 2:
                                    nativeeAd.setRequiredNativeMediaAssetType(NativeeAd.MediaAssetType.IMAGE);
                                    break;
                            }
                        }
                    });

                    Spinner nativeTemplateSpinner = findViewById(R.id.native_template_list);
                    ArrayAdapter<String> nativeTemplateAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.nativeTemplates));
                    nativeTemplateSpinner.setAdapter(nativeTemplateAdapter);
                    nativeTemplateSpinner.setOnItemSelectedListener(new SimplifyOnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            updateNativeList(position);
                        }
                    });

                    Spinner nativeTypeSpinner = findViewById(R.id.native_type_list);
                    ArrayAdapter<String> nativeTypeAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.nativeTypes));
                    nativeTypeSpinner.setAdapter(nativeTypeAdapter);
                    nativeTypeSpinner.setOnItemSelectedListener(new SimplifyOnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    nativeeAd.setNativeAdType(NativeeAd.NativeAdType.AUTO);
                                    break;
                                case 1:
                                    nativeeAd.setNativeAdType(NativeeAd.NativeAdType.NO_VIDEO);
                                    break;
                                case 2:
                                    nativeeAd.setNativeAdType(NativeeAd.NativeAdType.VIDEO);
                                    break;
                            }
                        }
                    });
                }

                if (child.findViewById(AdTypeAdapter.AdTypePages.Banner.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheBannerSwitch = findViewById(R.id.autoCacheBannerSwitch);
                    autoCacheBannerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        bannerAd.setAutoCache(isChecked);
                        Button bannerCacheButton = findViewById(R.id.bannerCacheButton);
                        if (isChecked) {
                            bannerCacheButton.setVisibility(View.GONE);
                        } else {
                            bannerCacheButton.setVisibility(View.VISIBLE);
                        }
                    });

                    CompoundButton smartBannersSwitch = findViewById(R.id.smartBannersSwitch);
                    smartBannersSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> bannerAd.setSmartBanner(isChecked));

                    CompoundButton bigBannersSwitch = findViewById(R.id.bigBannersSwitch);
                    bigBannersSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> bannerAd.set728x90Banners(isChecked));

                    CompoundButton bannersAnimateSwitch = findViewById(R.id.bannersAnimateBannersSwitch);
                    bannersAnimateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> bannerAd.setBannerAnimation(isChecked));

                    Spinner bannerPositionSpinner = findViewById(R.id.bannerPositionList);
                    ArrayAdapter<BannerAd.BannerPosition> bannerPositionsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, BannerAd.BannerPosition.values());
                    bannerPositionSpinner.setAdapter(bannerPositionsAdapter);
                }

                if (child.findViewById(AdTypeAdapter.AdTypePages.MREC.getId()) != null && child.getTag() == null) {
                    child.setTag(true);
                    CompoundButton autoCacheMrecSwitch = findViewById(R.id.autoCacheMrecSwitch);
                    autoCacheMrecSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        mrecAd.setAutoCache(isChecked);
                        Button MrecCacheButton = findViewById(R.id.mrecCacheButton);
                        if (isChecked) {
                            MrecCacheButton.setVisibility(View.GONE);
                        } else {
                            MrecCacheButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(pager);
    }

    public void setChildDirectedTreatment(View v) {
        v.setEnabled(false);
        appODeal.setChildDirectedTreatment(true);
    }

    /* INTERSTITIAL ADS STARTS */

    public void interstitialChooseNetworks(View v) {
        interstitialAd.disableAdNetworks();
    }

    public void initInterstitialSdkButton(View v) {
        interstitialAd.initSdk();
    }

    public void isInterstitialLoadedButton(View v) {
        Toast.makeText(this, String.valueOf(interstitialAd.isAdLoaded()), Toast.LENGTH_SHORT).show();
    }

    public void isInterstitialLoadedPrecacheButton(View v) {
        Toast.makeText(this, String.valueOf(interstitialAd.isPreCached()), Toast.LENGTH_SHORT).show();
    }

    public void interstitialCacheButton(View v) {
        interstitialAd.cacheAd();
    }

    public void interstitialShowButton(View v) {
        boolean isShown = interstitialAd.show();
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    /* INTERSTITIAL ADS ENDS */

    /* REWARDED VIDEO ADS STARTS */

    public void rewardedVideoChooseNetworks(View v) {
        rewardVideoAd.disableAdNetworks();
    }

    public void initRewardedVideoSdkButton(View v) {
        rewardVideoAd.initSdk();
    }

    public void isRewardedVideoLoadedButton(View v) {
        Toast.makeText(this, String.valueOf(rewardVideoAd.isAdLoaded()), Toast.LENGTH_SHORT).show();
    }

    public void rewardedVideoCacheButton(View v) {
        rewardVideoAd.cacheAd();
    }

    public void rewardedVideoShowButton(View v) {
        boolean isShown = rewardVideoAd.show();
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    /* REWARDED VIDEO ADS ENDS */

    /* MREC ADS STARTS */

    public void mrecChooseNetworks(View v) {
        mrecAd.disableAdNetworks();
    }

    public void initMrecSdkButton(View v) {
        mrecAd.setMrecViewId(R.id.appodealMrecView);
        mrecAd.initSdk();
    }

    public void isMrecLoadedButton(View v) {
        Toast.makeText(this, String.valueOf(mrecAd.isAdLoaded()), Toast.LENGTH_SHORT).show();
    }

    public void mrecCacheButton(View v) {
        mrecAd.cacheAd();
    }

    public void mrecShowButton(View v) {
        mrecAd.setMrecViewId(R.id.appodealMrecView);
        boolean isShown = mrecAd.show();
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void mrecHideButton(View v) {
        mrecAd.hide();
    }

    public void mrecDestroyButton(View v) {
        mrecAd.destroy();
    }

    /* MREC ADS STARTS */

    /* BANNER ADS STARTS */

    public void bannerChooseNetworks(View v) {
        bannerAd.disableAdNetworks();
    }

    public void initBannerSdkButton(View v) {
        bannerAd.setBannerViewId(R.id.appodealBannerView);
        bannerAd.initSdk();
    }

    public void isBannerLoadedButton(View v) {
        Toast.makeText(this, String.valueOf(bannerAd.isAdLoaded()), Toast.LENGTH_SHORT).show();
    }

    public void bannerCacheButton(View v) {
        bannerAd.cacheAd();
    }

    public void bannerShowButton(View v) {
        Spinner bannerPositionSpinner = findViewById(R.id.bannerPositionList);
        bannerAd.setBannerPosition((BannerAd.BannerPosition) bannerPositionSpinner.getSelectedItem());
        boolean isShown = bannerAd.show();
        Toast.makeText(this, String.valueOf(isShown), Toast.LENGTH_SHORT).show();
    }

    public void bannerHideButton(View v) {
        bannerAd.hide();
    }

    public void bannerDestroyButton(View v) {
        bannerAd.destroy();
    }

    /* BANNER ADS STARTS */

    /* NATIVE ADS STARTS */

    public void initNativeSdkButton(View v) {
        nativeeAd.initSdk();
    }

    public void nativeChooseNetworks(View v) {
        nativeeAd.disableAdNetworks();
    }

    public void nativeCacheButton(View v) {
        hideNativeAds();

        HorizontalNumberPicker numberPicker = findViewById(R.id.nativeAdsCountPicker);
        nativeeAd.cacheAd(numberPicker.getNumber());
    }

    public void isNativeLoadedButton(View v) {
        Toast.makeText(this, String.valueOf(nativeeAd.isAdLoaded()), Toast.LENGTH_SHORT).show();
    }

    public void nativeShowButton(View v) {
        hideNativeAds();
        HorizontalNumberPicker numberPicker = findViewById(R.id.nativeAdsCountPicker);
        nativeAds = nativeeAd.getNativeAds(numberPicker.getNumber());
        LinearLayout nativeAdsListView = findViewById(R.id.nativeAdsListView);
        Spinner nativeTemplateSpinner = findViewById(R.id.native_template_list);
        NativeeAd.NativeListAdapter nativeListViewAdapter =
                new NativeeAd.NativeListAdapter(nativeAdsListView, nativeTemplateSpinner.getSelectedItemPosition());
        for (NativeAd nativeAd : nativeAds) nativeListViewAdapter.addNativeAd(nativeAd);
        nativeAdsListView.setTag(nativeListViewAdapter);
        nativeListViewAdapter.rebuild();
    }

    public void nativeHideButton(View v) {
        hideNativeAds();
    }

    public void unRegisterNativeAds(View v) {
        LinearLayout nativeListView = findViewById(R.id.nativeAdsListView);
        int childCount = nativeListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            NativeAdView child = (NativeAdView) nativeListView.getChildAt(i);
            child.unregisterViewForInteraction();
        }
    }

    public void destroyNativeAds(View v) {
        LinearLayout nativeListView = findViewById(R.id.nativeAdsListView);
        int childCount = nativeListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            NativeAdView child = (NativeAdView) nativeListView.getChildAt(i);
            nativeeAd.destroyNativeAdView(child);
        }
    }

    public void registerNativeAds(View v) {
        LinearLayout nativeListView = findViewById(R.id.nativeAdsListView);
        int childCount = nativeListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            NativeAdView child = (NativeAdView) nativeListView.getChildAt(i);
            nativeeAd.registerNativeAd(child, nativeAds.get(i));
        }
    }

    private void hideNativeAds() {
        LinearLayout nativeListView = findViewById(R.id.nativeAdsListView);
        int childCount = nativeListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            NativeAdView child = (NativeAdView) nativeListView.getChildAt(i);
            nativeeAd.unregisterViewForInteractionNativeAdView(child);
            nativeeAd.destroyNativeAdView(child);
        }
        nativeListView.removeAllViews();
        NativeeAd.NativeListAdapter nativeListViewAdapter = (NativeeAd.NativeListAdapter) nativeListView.getTag();
        if (nativeListViewAdapter != null) nativeListViewAdapter.clear();
    }

    private void updateNativeList(int position) {
        LinearLayout nativeListView = findViewById(R.id.nativeAdsListView);
        NativeeAd.NativeListAdapter nativeListViewAdapter = (NativeeAd.NativeListAdapter) nativeListView.getTag();
        if (nativeListViewAdapter != null) {
            nativeListViewAdapter.setTemplate(position);
            nativeListViewAdapter.rebuild();
        }
    }

    public void showInRecyclerView(View v) {
        Spinner nativeTemplateSpinner = findViewById(R.id.native_template_list);
        startActivity(NativeActivity.newIntent(this, nativeTemplateSpinner.getSelectedItemPosition()));
    }

    /* NATIVE ADS ENDS */

}
