package com.example.asztar.wetklinikmobileapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;

public class Connectivity {
    public static boolean connectionIsUp(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null)
                return true;
        return false;
    }
}
