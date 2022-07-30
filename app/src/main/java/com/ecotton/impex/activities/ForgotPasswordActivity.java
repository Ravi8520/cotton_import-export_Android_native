package com.ecotton.impex.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.databinding.ActivityForgotPasswordBinding;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Constants;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {


    private Context mContext;
    String mobileno;

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = getIntent();
        if (intent != null) {
            mobileno = intent.getStringExtra(VerifyOTPActivity.NEWMOBILE_NO);
            binding.edtEmail.setText(mobileno);
        }
        binding.btnSubmit.setOnClickListener(view -> {
            if (isValidForm()) {
                if (LoginActivity.create_account.equals("create")) {
                    VerifyMono();
                } else {
                    Log.e("Forgot_password", "Forgot_password==");
                    Forgot_password();
                }
            }
        });
        binding.backarrow.setOnClickListener(view -> {
            onBackPressed();
        });
    }


    private void Forgot_password() {
        try {
            JSONObject object = new JSONObject();
            object.put("mobile_number", binding.edtEmail.getText().toString());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().Forgot_password(Constants.AUTH, data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String dataa = null;
                    try {
                        dataa = new String(response.body().bytes());
                        Log.e("response", "response==" + dataa);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    LoginModel model = gson.fromJson(dataa, LoginModel.class);
                    if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, model.getMessage());
                        Intent intent = new Intent(mContext, VerifyOTPActivity.class);
                        intent.putExtra(VerifyOTPActivity.MOBILE_NO, binding.edtEmail.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(ForgotPasswordActivity.this);
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

    private void VerifyMono() {
        try {
            JSONObject object = new JSONObject();
            object.put("mobile_number", binding.edtEmail.getText().toString());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().VerifyMobileNo(Constants.AUTH, data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String dataa = null;
                    try {
                        dataa = new String(response.body().bytes());
                        Log.e("response", "response==" + dataa);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    LoginModel model = gson.fromJson(dataa, LoginModel.class);
                    if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                        Intent intent = new Intent(mContext, VerifyOTPActivity.class);
                        intent.putExtra(VerifyOTPActivity.MOBILE_NO, binding.edtEmail.getText().toString().trim());
                        intent.putExtra(VerifyOTPActivity.ISINVITED, model.getData().getIs_invited());
                        startActivity(intent);
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(ForgotPasswordActivity.this);
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



    private boolean isValidForm() {
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtEmail, binding.mTilEmail, mContext.getString(R.string.err_enter_mobile_number), 10, mContext.getString(R.string.err_enter_valid_mobile_number));
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }
}