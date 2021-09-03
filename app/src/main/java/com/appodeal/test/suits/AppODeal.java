package com.appodeal.test.suits;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.UserSettings;
import com.appodeal.ads.utils.Log;
import com.appodeal.ads.utils.PermissionsHelper;
import com.appodeal.test.R;
import com.appodeal.test.helpers.Utils;
import com.explorestack.consent.Consent;
import com.explorestack.consent.ConsentForm;
import com.explorestack.consent.ConsentFormListener;
import com.explorestack.consent.exception.ConsentManagerException;

import java.util.List;

import static com.appodeal.test.activities.parent.SuperActivity.APP_KEY;

public class AppODeal {

    Activity activity;

    @Nullable
    protected ConsentForm consentForm;

    int age = 25;
    boolean consent;

    public AppODeal(Activity activity, boolean consent) {
        this.activity = activity;
        this.consent = consent;
        if (checkPermission())
            initAppODealSDK();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Appodeal.requestAndroidMPermissions(activity, new AppodealPermissionCallbacks(activity));
        } else return false;

        // we can store the last consent value to shared preference as well,
        // consent =  getIntent().getBooleanExtra(CONSENT, false);

        /*if (savedInstanceState == null) consent = getIntent().getBooleanExtra(CONSENT, false);
        else {
            Consent.Status consentStatus = ConsentManager.getInstance(this).getConsentStatus();
            consent = consentStatus == Consent.Status.PERSONALIZED || consentStatus == Consent.Status.PARTLY_PERSONALIZED;
        }*/
        return true;
    }

    private void initAppODealSDK() {
        Appodeal.setUserAge(age);
        Appodeal.setUserGender(UserSettings.Gender.MALE);
        Appodeal.initialize(activity, APP_KEY, Appodeal.NONE, consent);
    }

    public String getVersion() {
        return Appodeal.getVersion();
    }

    public boolean isSharedAdsInstanceAcrossActivities() {
        return Appodeal.isSharedAdsInstanceAcrossActivities();
    }

    public void setNoneAsLogLevel() {
        Appodeal.setLogLevel(Log.LogLevel.none);
    }

    public void setDebugAsLogLevel() {
        Appodeal.setLogLevel(Log.LogLevel.debug);
    }

    public void setVerboseAsLogLevel() {
        Appodeal.setLogLevel(Log.LogLevel.verbose);
    }

    public void setTesting(boolean isTestingEnvironment) {
        Appodeal.setTesting(isTestingEnvironment);
    }

    public void setChildDirectedTreatment(boolean shouldRidAdultContentForChildren) {
        Appodeal.setChildDirectedTreatment(shouldRidAdultContentForChildren);
    }

    public void useLoadedAdToOtherAdPlaceholder(boolean shouldShare) {
        Appodeal.setSharedAdsInstanceAcrossActivities(shouldShare);
    }

    public void showUpdateConsentForm() {
        if (consentForm == null) {
            consentForm = new ConsentForm.Builder(activity).withListener(new ConsentFormListener() {
                @Override
                public void onConsentFormLoaded() {
                    // Show ConsentManager Consent request form
                    consentForm.showAsActivity();
                }

                @Override
                public void onConsentFormError(ConsentManagerException error) {
                    Toast.makeText(activity, "Consent form error: " + error.getReason(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConsentFormOpened() {
                    //ignore
                }

                @Override
                public void onConsentFormClosed(Consent consent) {
                    boolean hasConsent = consent.getStatus() == Consent.Status.PERSONALIZED && consent.getStatus() != Consent.Status.NON_PERSONALIZED;
                    // consentSwitch.setChecked(hasConsent);
                    // - Update local Consent value with resolved Consent value
                    // SuperFragmentActivity.this.consent = hasConsent;
                    // - Update Appodeal SDK Consent value with resolved Consent value
                    Appodeal.updateConsent(hasConsent);
                }
            }).build();
        }
        // If Consent request form is already loaded, then we can display it, otherwise, we should load it first
        if (consentForm.isLoaded()) {
            consentForm.showAsActivity();
        } else {
            consentForm.load();
        }
    }

    protected void disableNetworks(int adType) {
        ListView listView = new ListView(activity);
        List<String> networks = Appodeal.getNetworks(activity, adType);
        ArrayAdapter<String> networksAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, networks);
        listView.setAdapter(networksAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = networksAdapter.getItem(position);
            if (item == null) return;
            Appodeal.disableNetwork(activity, item, adType);
            networksAdapter.remove(networksAdapter.getItem(position));
            networksAdapter.notifyDataSetChanged();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.disableNetworks));
        builder.setView(listView);
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private static class AppodealPermissionCallbacks implements PermissionsHelper.AppodealPermissionCallbacks {

        private final Activity activity;

        public AppodealPermissionCallbacks(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void writeExternalStorageResponse(int result) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(activity, "WRITE_EXTERNAL_STORAGE permission was granted");
            } else {
                Utils.showToast(activity, "WRITE_EXTERNAL_STORAGE permission was NOT granted");
            }
        }

        @Override
        public void accessCoarseLocationResponse(int result) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(activity, "ACCESS_COARSE_LOCATION permission was granted");
            } else {
                Utils.showToast(activity, "ACCESS_COARSE_LOCATION permission was NOT granted");
            }
        }

    }

}
