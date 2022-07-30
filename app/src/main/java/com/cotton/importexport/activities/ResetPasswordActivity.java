package com.cotton.importexport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.cotton.importexport.R;
import com.cotton.importexport.api.APIClient;
import com.cotton.importexport.databinding.ActivityResetPasswordBinding;
import com.cotton.importexport.models.login.LoginModel;
import com.cotton.importexport.utils.AppUtil;
import com.cotton.importexport.utils.SessionUtil;
import com.cotton.importexport.utils.Utils;
import com.cotton.importexport.utils.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {


    private ResetPasswordActivity mContext;

    public static String MobileNo = "mobile_no";
    private String mobileno;
    ActivityResetPasswordBinding binding;
    private SessionUtil mSessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent = getIntent();
        if (intent != null) {
            mobileno = intent.getStringExtra(MobileNo);
        }
        binding.layoutMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        binding.backarrow.setOnClickListener(view -> {
            img_backOnClick();
        });
        binding.btnSetPassword.setOnClickListener(view -> {
            btn_set_passwordOnClick();
        });
    }


    private boolean isValidForm() {
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtPassword, binding.mTilPassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password_contain_character, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtConfrimPassword, binding.mTilconfrimPassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password_contain_character, 6), 15, "");
    }

    protected void img_backOnClick() {
        onBackPressed();
    }

    protected void btn_set_passwordOnClick() {
        final String password = binding.edtPassword.getText().toString();
        final String confirmPassword = binding.edtConfrimPassword.getText().toString();
        if (isValidForm()) {
            if (password.equals(confirmPassword)) {
                //are equal
                Reset_password();
            } else {
                binding.mTilconfrimPassword.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(mContext, R.color.dark_red));
                //are different
                Toast.makeText(mContext, "Password Not Match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Reset_password() {
        try {
            JSONObject object = new JSONObject();
            object.put("mobile_number", mobileno);
            object.put("password", binding.edtPassword.getText().toString().trim());
            object.put("confirm_password", binding.edtConfrimPassword.getText().toString().trim());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().Reset_password(mSessionUtil.getApiToken(), data);
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
                    Log.e("response", "response==" + dataa);
                    if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(ResetPasswordActivity.this);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }
}