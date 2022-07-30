package com.cotton.importexport.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.cotton.importexport.R;
import com.cotton.importexport.api.APIClient;
import com.cotton.importexport.controller.FCMController;
import com.cotton.importexport.models.login.LoginModel;
import com.cotton.importexport.utils.AppUtil;
import com.cotton.importexport.utils.Constants;
import com.cotton.importexport.utils.SessionUtil;
import com.cotton.importexport.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    ImageView img_splash;
    SessionUtil mSessionUtil;
    Context mContext;
    private FCMController mFCMController;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private String manufacturer;
    private String model;
    private String versionRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img_splash = findViewById(R.id.img_splash);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        img_splash.startAnimation(animation);

        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        mFCMController = new FCMController(this);
        mFCMController.generateFCMToken();
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        // int version = Build.VERSION.SDK_INT;
        versionRelease = Build.VERSION.RELEASE;
        //PrintLog.d(TAG,"P= "+ AppUtil.getDecryptString(mContext,"4e1258aae34886df7ffd309eb376737153a1110e2c19d0e936d2587c31112f415e28a0bfa346449fe47a7d20452b11d2a36aa93e1757ace50e3c36f9b4bd2e252ec7e2e210d459d419b5d17e2b1057a8fa5745b74711b245a025b3d1b22668227e2f11d1253f950d8b44b09a01adf14c2fe8dcb41759b4f0c1bef22b001c2ff1a6739287467a22250f776a38a4bd25e0"));
        if (isOnline()) {
            if (mSessionUtil.isLogin()) {
                if (mSessionUtil.getMobileNo().isEmpty() || mSessionUtil.getPass().isEmpty() || mSessionUtil.getApiToken().isEmpty() || mSessionUtil.getUserid().isEmpty()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                } else {
                    autologin();
                }

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    }
                }, 3000);
            }
        } else {
            Toast.makeText(mContext, "No Internet", Toast.LENGTH_LONG).show();
        }

       /* String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        Log.e("MyActivity", "manufacturer " + manufacturer
                + " \n model " + model
                + " \n versionRelease " + versionRelease);*/
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void autologin() {
        try {
            JSONObject object = new JSONObject();
            object.put("mobile_number", mSessionUtil.getMobileNo());
            object.put("password", mSessionUtil.getPass());
            object.put("fcm_token", mSessionUtil.getFcmtoken());
            object.put("device_os", "android");
            object.put("os_version", versionRelease);
            object.put("device_model", manufacturer + model);
            String data = object.toString();
            Log.e("getFcmtoken", "getFcmtoken==" + mSessionUtil.getFcmtoken());
            Call<ResponseBody> call = APIClient.getInstance().Login(Constants.AUTH, data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String dataa = null;
                    try {
                        dataa = new String(response.body().bytes());
                        Log.e("Login", "Login==" + dataa);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    LoginModel model = gson.fromJson(dataa, LoginModel.class);
                    if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(SessionUtil.API_TOKEN, model.getData().getApiToken());
                        map.put(SessionUtil.MOBILE_NO, model.getData().getMobileNumber());
                        map.put(SessionUtil.COMPANY_NAME, mSessionUtil.getCompanyName());
                        map.put(SessionUtil.USER_TYPE, mSessionUtil.getUsertype());
                        map.put(SessionUtil.PASS, mSessionUtil.getPass());
                        map.put(SessionUtil.USERID, mSessionUtil.getUserid());
                        map.put(SessionUtil.COMPANY_ID, mSessionUtil.getCompanyId());
                        mSessionUtil.setData(map);
                        if (model.getData().getIs_user_plan() == 1) {
                           // startActivity(new Intent(mContext, HomeActivity.class));
                          //  finish();
                        } else {
                          //  Intent intent = new Intent(mContext, MywalletPlansActivity.class);
                          //  startActivity(intent);
                        }
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(SplashActivity.this);
                    } else {
                        AppUtil.showToast(mContext, model.getMessage());
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}