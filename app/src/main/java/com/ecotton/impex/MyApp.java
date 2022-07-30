package com.ecotton.impex;


public class MyApp extends DarkThemeApplication {
    public static MyApp instance;

    public void onCreate() {
        super.onCreate();
        try {

            instance = this;
              Class.forName("android.os.AsyncTask");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}