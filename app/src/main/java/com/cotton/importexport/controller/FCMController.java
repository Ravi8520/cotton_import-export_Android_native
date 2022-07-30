package com.cotton.importexport.controller;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.cotton.importexport.utils.AppUtil;
import com.cotton.importexport.utils.PrintLog;
import com.cotton.importexport.utils.SessionUtil;
import com.cotton.importexport.utils.ValidationUtil;


public class FCMController {
    private static final String TAG = FCMController.class.getSimpleName();
    private Activity mActivity;
    private SessionUtil mSesionUtil;

    public FCMController() {
    }

    public FCMController(Activity mActivity) {
        this.mActivity = mActivity;
        mSesionUtil = new SessionUtil(mActivity);
    }

    /***
     * Generate FCM Token from Firebase.
     */
    public void generateFCMToken() {
        try {
            if (AppUtil.isConnectingToInternet(mActivity) == false) {
                return;
            }

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isSuccessful()) {
                        if (ValidationUtil.validateString(task.getResult())) {
                            String firebaseToken = task.getResult();
                            String prefToken = mSesionUtil.getFcmtoken();

                            if (!prefToken.equalsIgnoreCase(firebaseToken)) {
                                mSesionUtil.setFCMToken(firebaseToken);
                                //  updateFCMToken();
                                PrintLog.d(TAG, firebaseToken);
                            }
                        }
                    }
                }
            });

           /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(mActivity, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {

                    if (instanceIdResult != null
                            && ValidationUtil.validateString(instanceIdResult.getToken())) {

                        String firebaseToken = instanceIdResult.getToken().trim();
                        String prefToken = mSesionUtil.getFcmtoken();

                        if (!prefToken.equalsIgnoreCase(firebaseToken)) {
                            mSesionUtil.setFCMToken(firebaseToken);
                            //  updateFCMToken();
                            PrintLog.d(TAG, firebaseToken);
                        }
                    }
                }
            });*/

            /*FirebaseInstanceId.getInstance().getInstanceId().addOnFailureListener(mActivity, new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {

                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Remove FCM Token to firebase.
     */
    public static void removeFCMToken() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FirebaseMessaging.getInstance().deleteToken();
                        //FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Update FCM Token to server.
     */
    /*private static void updateFCMToken() {
        try {
            if (!NetworkUtil.isNetworkAvailable(mActivity)
                    || ValidationUtil.validateString(AppConfigs.mUserFCMToken) == false) {
                return;
            }

            VolleyResponseListener mResponseListener = new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(JSONObject responseJSON) {
                }
            };

            JSONObject mPostParamData = new JSONObject();
            mPostParamData.put("token", AppConfigs.mUserFCMToken);
            mPostParamData.put("deviceId", StatusUtil.getDeviceUUID(mActivity));

            VolleyNetworkMethods.updateFCMToken(mActivity, mResponseListener, mPostParamData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
