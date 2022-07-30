package com.cotton.importexport;


import android.util.Log;

import androidx.annotation.NonNull;

public class MyApp extends DarkThemeApplication {
    public static MyApp instance;

    public void onCreate() {
        super.onCreate();
        try {

            instance = this;
              Class.forName("android.os.AsyncTask");


        } catch (ClassNotFoundException e) {
        }

    }

}