package com.cotton.importexport.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.cotton.importexport.R;
import com.cotton.importexport.api.APIClient;
import com.cotton.importexport.databinding.ActivityLoginBinding;
import com.cotton.importexport.models.login.LoginModel;
import com.cotton.importexport.utils.AppUtil;
import com.cotton.importexport.utils.Constants;
import com.cotton.importexport.utils.CustomDialog;
import com.cotton.importexport.utils.SessionUtil;
import com.cotton.importexport.utils.Utils;
import com.cotton.importexport.utils.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private LoginActivity mContext;


    public static String create_account = "";

    ActivityLoginBinding binding;
    private SessionUtil mSessionUtil;

    private CustomDialog customDialog;
    private String manufacturer;
    private String model;
    private String versionRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        // int version = Build.VERSION.SDK_INT;
        versionRelease = Build.VERSION.RELEASE;

       /* binding.layoutMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });*/


        binding.txtCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_account = "create";
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    loginwithotp();

                }
            }
        });

        binding.txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }


    private boolean isValidForm() {
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtMobileNumber, binding.mTilMobile, mContext.getString(R.string.err_enter_mobile_number), 10, mContext.getString(R.string.err_enter_valid_mobile_number))
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtPassword, binding.mTilPassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password_contain_character, 6), 15, "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        create_account = "";
    }


    private void loginwithotp() {
        try {
            JSONObject object = new JSONObject();
            object.put("mobile_number", binding.edtMobileNumber.getText().toString());
            object.put("password", binding.edtPassword.getText().toString());
            object.put("fcm_token", mSessionUtil.getFcmtoken());
            object.put("device_os", "android");
            object.put("os_version", versionRelease);
            object.put("device_model", manufacturer + model);

            String data = object.toString();
            Log.e("data", "data==" + mSessionUtil.getFcmtoken());

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
                        map.put(SessionUtil.PASS, binding.edtPassword.getText().toString().trim());
                        mSessionUtil.setData(map);
                        //startActivity(new Intent(mContext, CompanyListActivity.class));
                        //finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(LoginActivity.this);
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