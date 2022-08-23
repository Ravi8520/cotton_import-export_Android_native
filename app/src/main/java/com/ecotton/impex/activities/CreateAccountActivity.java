package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.databinding.ActivityCreateAccountBinding;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {


    public static String EMAIL_ADDRESS = "email";
    private String emailID;

    private CreateAccountActivity mContext;

    ActivityCreateAccountBinding binding;
    private SessionUtil mSessionUtil;
    private String usertype;

    CustomDialog customDialog;

    String selected_country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        Intent intent = getIntent();
        if (intent != null) {
            emailID = intent.getStringExtra(EMAIL_ADDRESS);
            binding.edtEmailAddress.setText(emailID);
        }

        binding.edtEmailAddress.setEnabled(false);

        binding.edtGstNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        binding.btnCreate.setOnClickListener(view -> {
            btn_createOnClick();
        });

        binding.backarrow.setOnClickListener(view -> {
            img_backOnClick();
        });

        binding.ccp1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = binding.ccp1.getSelectedCountryCodeWithPlus();
            }

        });


    }

    public static boolean isValidGSTNo(String str) {
        // Regex to check valid
        // GST (Goods and Services Tax) number
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
                + "[A-Z]{1}[1-9A-Z]{1}"
                + "Z[0-9A-Z]{1}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find the matching
        // between the given string
        // and the regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
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
                && ValidationUtil.isBlankETAndTextInputError(mContext,binding.edtEmailAddress,binding.mTilemailAddress,mContext.getString(R.string.err_enter_email_number))
                && ValidationUtil.isValidEmailETErr(mContext, binding.edtEmailAddress, binding.mTilemailAddress, mContext.getString(R.string.err_enter_email_format))
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtMobileNumber, binding.mTilmobileNumber, mContext.getString(R.string.err_enter_mobile_number), 10, mContext.getString(R.string.err_enter_valid_mobile_number))
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtPassword, binding.mTilpassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtConfrimPassword, binding.mTilconfrimPassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtCompanyName, binding.mTilcpmpanyName, mContext.getString(R.string.err_company_name), 4, mContext.getString(R.string.err_company, 4), 60, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtIecNumber, binding.mTiliecNumber, mContext.getString(R.string.err_iec_number), 10, mContext.getString(R.string.err_enter_valid_iec_number));
    }

    protected void btn_createOnClick() {
        if (isValidForm()) {
            CreateAccount();
        }
    }

    private void CreateAccount() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("mobile_number", binding.edtMobileNumber.getText().toString().trim());
            object.put("country_code", selected_country_code);
            object.put("name", binding.edtContactPerson.getText().toString().trim());
            object.put("email", binding.edtEmailAddress.getText().toString().trim());
            object.put("password", binding.edtPassword.getText().toString().trim());
            object.put("company_name", binding.edtCompanyName.getText().toString().trim());
            object.put("gst_no", binding.edtGstNumber.getText().toString().trim());
            object.put("iec", binding.edtIecNumber.getText().toString().trim());


            String data = object.toString();
            Log.e("data", "data==" + data);
            Log.e("getApiToken", "getApiToken" + mSessionUtil.getApiToken());
            Call<ResponseBody> call = APIClient.getInstance().Registration(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    customDialog.dismissProgress(mContext);
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
                        AppUtil.showToast1(mContext, model.getMessage());
                        Intent intent = new Intent(mContext, WitingApprovelActivity.class);
                        intent.putExtra("create", "create");
                        startActivity(intent);
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(CreateAccountActivity.this);
                    } else {
                        AppUtil.showToast(mContext, model.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void img_backOnClick() {
        startActivity(new Intent(mContext, VerifyOTPActivity.class));
        finish();
    }
}