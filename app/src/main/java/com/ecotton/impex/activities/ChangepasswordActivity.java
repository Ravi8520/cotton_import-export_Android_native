package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.databinding.ActivityChangepasswordBinding;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangepasswordActivity extends AppCompatActivity {

    ActivityChangepasswordBinding binding;
    private ChangepasswordActivity mContext;
    private SessionUtil mSessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangepasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        mSessionUtil = new SessionUtil(mContext);

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = binding.edtPassword.getText().toString();
                final String confirmPassword = binding.edtConfrimPassword.getText().toString();
                if (isValidForm()) {
                    if (password.equals(confirmPassword)) {
                        //are equal
                        Change_Password();
                    } else {
                        binding.etConfirmpass.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(mContext, R.color.dark_red));
                        //are different
                        Toast.makeText(mContext, "Password Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void Change_Password() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("current_password", binding.edtCurrentPassword.getText().toString().trim());
            object.put("password", binding.edtPassword.getText().toString().trim());
            object.put("confirm_password", binding.edtConfrimPassword.getText().toString().trim());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().Change_password(mSessionUtil.getApiToken(), data);
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
                        HashMap<String, String> map = new HashMap<>();
                        map.put(SessionUtil.API_TOKEN, mSessionUtil.getApiToken());
                        map.put(SessionUtil.MOBILE_NO, mSessionUtil.getMobileNo());
                        map.put(SessionUtil.PASS, binding.edtPassword.getText().toString().trim());
                        map.put(SessionUtil.COMPANY_NAME, mSessionUtil.getCompanyName());
                        map.put(SessionUtil.USERID, mSessionUtil.getUserid());
                        map.put(SessionUtil.USER_TYPE, mSessionUtil.getUsertype());
                        map.put(SessionUtil.COMPANY_ID, mSessionUtil.getCompanyId());
                        mSessionUtil.setData(map);
                        startActivity(new Intent(mContext, HomeActivity.class));
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(ChangepasswordActivity.this);
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
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtCurrentPassword, binding.etCurrentpass, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password_contain_character, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtPassword, binding.etNewpass, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password_contain_character, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtConfrimPassword, binding.etConfirmpass, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password_contain_character, 6), 15, "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}