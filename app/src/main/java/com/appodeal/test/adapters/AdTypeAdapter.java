package com.appodeal.test.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appodeal.test.R;

public class AdTypeAdapter extends FragmentPagerAdapter {

    public AdTypeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return AdTypePages.values().length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new AdTypeFragment();
        Bundle args = new Bundle();
        args.putInt("layout", AdTypePages.values()[position].getLayout());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return AdTypePages.values()[position].name();
    }

    public static class AdTypeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int layoutId = args.getInt("layout");
            return inflater.inflate(layoutId, container, false);
        }
    }


    public enum AdTypePages {
        Interstitial(R.layout.section_interstitial, R.id.interstitialLayout),
        RVideo(R.layout.section_rewarded_video, R.id.rewardedVideoLayout), Banner(R.layout.section_banner, R.id.bannerLayout),
        MREC(R.layout.section_mrec, R.id.MrecLayout), Native(R.layout.section_native_ad, R.id.nativeLayout);

        private final int layout;
        private final int id;

        AdTypePages(int layout, int id) {
            this.layout = layout;
            this.id = id;
        }

        public int getLayout() {
            return layout;
        }

        public int getId() {
            return id;
        }
    }

}
