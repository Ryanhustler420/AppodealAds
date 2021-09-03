package com.appodeal.test.helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class Utils {

    public static void showToast(Activity activity, String text) {
        Log.d("AppodealDemoApp", text);
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

}
