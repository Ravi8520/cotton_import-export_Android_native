package com.cotton.importexport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.cotton.importexport.R;
import com.cotton.importexport.api.APIClient;
import com.cotton.importexport.databinding.ActivityCreateAccountBinding;
import com.cotton.importexport.models.login.LoginModel;
import com.cotton.importexport.utils.AppUtil;
import com.cotton.importexport.utils.CustomDialog;
import com.cotton.importexport.utils.SessionUtil;
import com.cotton.importexport.utils.Utils;
import com.cotton.importexport.utils.ValidationUtil;

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




    public static String MobileNo = "mobile_no";
    private String mobileno;

    private CreateAccountActivity mContext;

    ActivityCreateAccountBinding binding;
    private SessionUtil mSessionUtil;
    private String usertype;

    CustomDialog customDialog;

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
            mobileno = intent.getStringExtra(MobileNo);
            binding.edtMobileNumber.setText(mobileno);
        }

        binding.edtMobileNumber.setEnabled(false);

        binding.edtGstNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        binding.btnCreate.setOnClickListener(view->{
            btn_createOnClick();
        });

        binding.btnCreate.setOnClickListener(view->{
            img_backOnClick();
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
        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.edtGstNumber.setError("Please Enter Proper GST Number");
            binding.edtGstNumber.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailAddress.getText().toString()).matches()) {
            binding.edtEmailAddress.setError("Please Enter Proper Email Format");
            binding.edtEmailAddress.requestFocus();
            return false;
        }
        if (binding.edtEmailAddress.getText().toString().isEmpty()) {
            binding.edtEmailAddress.setError("Please Enter Email");
            binding.edtEmailAddress.requestFocus();
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
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtContactPerson,binding.mTilcontactPerson, mContext.getString(R.string.lbl_contact_person), 3, mContext.getString(R.string.err_name_character, 3), 20, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtPassword, binding.mTilpassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtConfrimPassword, binding.mTilconfrimPassword, mContext.getString(R.string.err_enter_password), 6, mContext.getString(R.string.err_password, 6), 15, "")
                && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtCompanyName, binding.mTilcpmpanyName, mContext.getString(R.string.err_company_name), 4, mContext.getString(R.string.err_company, 4), 60, "");
    }

    protected void btn_createOnClick() {
        if (isValidForm()) {
            Log.e("usertype", "usertype==" + usertype);
            if (usertype == null) {
                Toast.makeText(mContext, "Please Select User Type", Toast.LENGTH_SHORT).show();
            } else {
                //are equal
                CreateAccount();
            }
        }
    }

    private void CreateAccount() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("mobile_number", binding.edtMobileNumber.getText().toString().trim());
            object.put("name", binding.edtContactPerson.getText().toString().trim());
            object.put("email", binding.edtEmailAddress.getText().toString().trim());
            object.put("password", binding.edtPassword.getText().toString().trim());
            object.put("company_name", binding.edtCompanyName.getText().toString().trim());
            object.put("gst_no", binding.edtGstNumber.getText().toString().trim());
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
                       // startActivity(new Intent(mContext, WitingApprovelActivity.class));
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