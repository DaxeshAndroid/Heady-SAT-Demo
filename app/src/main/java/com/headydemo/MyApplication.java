package com.headydemo;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dakshesh.khatri on 27-09-2018.
 */

public class MyApplication extends Application {
    public static String BASE_URL = "https://stark-spire-93433.herokuapp.com/";

    public static Context mcontext;


    public MyApplication() {
        mcontext = this;

    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


}
