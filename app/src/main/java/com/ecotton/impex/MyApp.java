package com.ecotton.impex;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.ecotton.impex.models.BuyerFilterRequest;
import com.ecotton.impex.models.dashboard.DashBoardModel;
import com.ecotton.impex.utils.SessionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends DarkThemeApplication {
    public static MyApp instance;
    public static String orderID = "";
    public static List<DashBoardModel.CompanyModel> stateWiseList = new ArrayList<>();
    public static SessionUtil mSessionUtil;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public static BuyerFilterRequest filterRequest;
    public static String BaseURL = "";

    public void onCreate() {
        super.onCreate();
        try {
            instance = this;
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            filterRequest = new BuyerFilterRequest();
            mSessionUtil = new SessionUtil(this);

            FirebaseMessaging.getInstance().subscribeToTopic("news")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Subscribed";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe failed";
                            }
                            Log.e("TAG", msg);
                        }
                    });
            Class.forName("android.os.AsyncTask");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}