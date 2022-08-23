package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.databinding.ActivityIsInvitedCreateAccountBinding;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Constants;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IsInvitedCreateAccountActivity extends AppCompatActivity {

    ActivityIsInvitedCreateAccountBinding binding;
    IsInvitedCreateAccountActivity mContext;
    SessionUtil mSessionUtil;
    public static String NAME = "name";
    public static String EMAIL = "email";
    public static String COMPANY_NAME = "company_name";
    public static String COMPANY_GST_NO = "company_gst_no";
    public static String COMPANY_TYPE = "company_type";
    public static String COMPANY_BROKER_NAME = "company_broker_name";
    public static String COMPANY_BROKER_CODE = "company_broker_code";
    public static String COMPANY_ID = "company_id";
    public static String IEC_NUMBER = "Iec_number";


    private String name;
    private String email;
    private String company_name;
    private String company_gst_no;
    private String company_type;
    private String company_broker_code;
    private int comapny_id;
    private String manufacturer;
    private String model;
    private String versionRelease;
    String selected_country_code;
    private String iecnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIsInvitedCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);

        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        // int version = Build.VERSION.SDK_INT;
        versionRelease = Build.VERSION.RELEASE;

        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra(EMAIL);
            name = intent.getStringExtra(NAME);
            company_name = intent.getStringExtra(COMPANY_NAME);
            company_gst_no = intent.getStringExtra(COMPANY_GST_NO);
            company_type = intent.getStringExtra(COMPANY_TYPE);
            company_broker_code = intent.getStringExtra(COMPANY_BROKER_CODE);
            comapny_id = intent.getIntExtra(COMPANY_ID, 0);
            iecnumber = intent.getStringExtra(IEC_NUMBER);

            binding.edtEmailAddress.setText(email);
            binding.edtEmailAddress.setEnabled(false);

            binding.edtContactPerson.setText(name);
            binding.edtContactPerson.setEnabled(true);

            binding.edtCompanyName.setText(company_name);
            binding.edtCompanyName.setEnabled(false);

            binding.edtGstNumber.setText(company_gst_no);
            binding.edtGstNumber.setEnabled(false);

            binding.edtIecNumber.setText(iecnumber);
            binding.edtIecNumber.setEnabled(false);

        }

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    CreateAccount();
                }
            }
        });
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.ccp1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = binding.ccp1.getSelectedCountryCodeWithPlus();
            }

        });
    }

    private void CreateAccount() {
        try {
            JSONObject object = new JSONObject();
            object.put("mobile_number", binding.edtMobileNumber.getText().toString().trim());
            object.put("country_code", selected_country_code);
            object.put("name", name);
            object.put("email", email);
            object.put("password", binding.edtPassword.getText().toString().trim());
            object.put("company_name", company_name);
            object.put("gst_no", company_gst_no);
            object.put("iec", iecnumber);
            object.put("company_id", comapny_id);
            object.put("user_type", company_type);
            object.put("is_invited", 1);
            object.put("referral_code", company_broker_code);

            String data = object.toString();

            Log.e("data", "data==" + data);

            Call<ResponseBody> call = APIClient.getInstance().Registration(mSessionUtil.getApiToken(), data);
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
                        loginwithotp();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(mContext);
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


    private void loginwithotp() {
        try {
            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("password", binding.edtPassword.getText().toString());
            object.put("fcm_token", mSessionUtil.getFcmtoken());
            object.put("device_os", "android");
            object.put("os_version", versionRelease);
            object.put("device_model", manufacturer + model);

            String data = object.toString();

            Log.e("data", "data==" + data);

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
                        Intent intent = new Intent(mContext, LoginAsActivity.class);
                        intent.putExtra(LoginAsActivity.COMPANY_ID, comapny_id);
                        startActivity(intent);
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(mContext);
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
        String passwordInput = binding.edtGstNumber.getText().toString();
        Pattern PASSWORD_PATTERN =
                Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}"
                        + "[A-Z]{1}[1-9A-Z]{1}"
                        + "Z[0-9A-Z]{1}$");
        if (!TextUtils.isEmpty(passwordInput) && !PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.edtGstNumber.setError("Please Enter Proper GST Number");
            binding.edtGstNumber.requestFocus();
            return false;
        }


        final String password = binding.edtPassword.getText().toString();
        final String confirmPassword = binding.edtConfrimPassword.getText().toString();


        if (!password.equals(confirmPassword)) {
            binding.mTilconfrimPassword.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(mContext, R.color.dark_red));
            Toast.makeText(mContext, "Password Are Not Match", Toast.LENGTH_SHORT).show();
            binding.edtConfrimPassword.requestFocus();
            return false;
        }
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtContactPerson, binding.mTilcontactPerson, mContext.getString(R.string.lbl_contact_person), 3, mContext.getString(R.string.err_name_character, 3), 20, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtEmailAddress, binding.mTilemailAddress, mContext.getString(R.string.err_enter_email_number))
                && ValidationUtil.isValidEmailETErr(mContext, binding.edtEmailAddress, binding.mTilemailAddress, mContext.getString(R.string.err_enter_email_format))
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtMobileNumber, binding.mTilmobileNumber, mContext.getString(R.string.err_enter_mobile_number), 10, mContext.getString(R.string.err_enter_valid_mobile_number))
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtPassword, binding.mTilpassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtConfrimPassword, binding.mTilconfrimPassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtCompanyName, binding.mTilcpmpanyName, mContext.getString(R.string.err_company_name), 4, mContext.getString(R.string.err_company, 4), 60, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtIecNumber, binding.mTiliecNumber, mContext.getString(R.string.err_iec_number), 10, mContext.getString(R.string.err_enter_valid_iec_number));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}