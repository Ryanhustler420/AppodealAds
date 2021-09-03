package com.appodeal.test.activities.parent;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.appodeal.ads.Appodeal;
import com.appodeal.test.R;
import com.appodeal.test.suits.AppODeal;
import com.explorestack.consent.Consent;
import com.explorestack.consent.ConsentForm;
import com.explorestack.consent.ConsentFormListener;
import com.explorestack.consent.exception.ConsentManagerException;

import java.util.List;

public abstract class SuperActivity extends FragmentActivity {
    public static String APP_KEY = "fee50c333ff3825fd6ad6d38cff78154de3025546d47a84f";

    protected AppODeal appODeal;

    @Nullable
    protected ConsentForm consentForm;
    protected Switch consentSwitch;
    protected boolean consent = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appODeal = new AppODeal(this, true);
    }

    @Override
    public void onBackPressed() {
        ViewGroup root = findViewById(android.R.id.content);
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            Object tag = child.getTag();
            if (tag != null && tag.equals("appodeal")) {
                root.removeView(child);
                return;
            }
        }
        super.onBackPressed();
    }

    // Displaying ConsentManger Consent request form
    public void showUpdateConsentForm() {
        if (consentForm == null) {
            consentForm = new ConsentForm.Builder(this).withListener(new ConsentFormListener() {
                @Override
                public void onConsentFormLoaded() {
                    // Show ConsentManager Consent request form
                    consentForm.showAsActivity();
                }

                @Override
                public void onConsentFormError(ConsentManagerException error) {
                    Toast.makeText(SuperActivity.this, "Consent form error: " + error.getReason(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConsentFormOpened() {
                    //ignore
                }

                @Override
                public void onConsentFormClosed(Consent consent) {
                    boolean hasConsent = consent.getStatus() == Consent.Status.PERSONALIZED && consent.getStatus() != Consent.Status.NON_PERSONALIZED;
                    consentSwitch.setChecked(hasConsent);
                    // Update local Consent value with resolved Consent value
                    SuperActivity.this.consent = hasConsent;
                    // Update Appodeal SDK Consent value with resolved Consent value
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
        ListView listView = new ListView(this);
        List<String> networks = Appodeal.getNetworks(this, adType);
        ArrayAdapter<String> networksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, networks);
        listView.setAdapter(networksAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = networksAdapter.getItem(position);
            if (item == null) return;
            Appodeal.disableNetwork(SuperActivity.this, item, adType);
            networksAdapter.remove(networksAdapter.getItem(position));
            networksAdapter.notifyDataSetChanged();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.disableNetworks));
        builder.setView(listView);
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
